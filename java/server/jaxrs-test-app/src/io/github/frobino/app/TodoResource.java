package io.github.frobino.app;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TodoResource {
    @Context
    UriInfo uriInfo;
    @Context
    Request request;
    int id;
    public TodoResource(UriInfo uriInfo, Request request, String id) {
        this.uriInfo = uriInfo;
        this.request = request;
        this.id = Integer.parseInt(id);
    }

    /*
	 * (R) read a single todo from the todos list.
	 * Which todo to read is specified in the URI.
	 * 
	 * This is just to return a json when browsing to model/1.
	 * Maybe not needed for this example?
	 */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Todo getTodo() {
    	Todo todo = TodoDao.instance.getModel().get(id);
        if (todo == null)
        	throw new RuntimeException("Get: Todo with " + id +  " not found");
        return todo;
    }

    /*
     * (U) Update (using a post).
     * 
     * FIXME: should this be a PUT? It looks like it is idempotent.
     * I have to learn how to use "HTTP verbs" (i.e. PUT, POST, etc.) better.
     * 
     * RESTFUL service essence:
     * It is all about "create resources and apply the HTTP verbs against it".
     * 
     * Better options:
     * - Toggle is not a restful action, so PUT cannot be used.
     *   Separate end point (create the "toggle todo resource"),
     *   e.g. POST model/toggle/1
     *   or PUT/POST/DELETE/GET model/1/toggled
     * - create a separate PATCH [{"op": "toggle"}, {"op": "editText", "id": <id>, "text": "updatedText"}]
     * - use QueryParam?
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateTodo(final Todo c) {
    	int updatedId = c.getId();
    	String updatedText = c.getText();
    	boolean updatedComplete = c.getComplete();
    	
    	TodoDao.instance.getModel().replace(updatedId, new Todo(updatedId, updatedText, updatedComplete));
    	
        return Response.status(200)  
                .entity(" Received todo: "+ c.getId() + " " + c.getText() + " " + c.getComplete())  
                .build();
    }
    
    /*
     * (U) Update (using a patch).
     * 
     * Test like this:
     * curl -H "Content-Type: application/json" --data '{"color": "new_value"}' -X PATCH http://127.0.0.1:8080/jaxrs-test-app/crunchify/model/1
     */
    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    public Response editTodo(InputStream is) {
    	
    	ObjectMapper objectMapper = new ObjectMapper();
    	JsonNode jsonNode = null;
    	String op = "";
		try {
			jsonNode = objectMapper.readTree(is);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (jsonNode == null)
	        return Response.status(500)  
	                .entity("Client is sending something weird")  
	                .build();
		
    	op = jsonNode.get("op").asText();		
		if (op == null)
	        return Response.status(500)  
	                .entity("Operation non valid")  
	                .build();
		
		switch (op) {
		case "toggle":
			Todo todoToToggle = TodoDao.instance.getModel().get(this.id);
			boolean currentCompeteFlag = todoToToggle.getComplete();
			String currentText = todoToToggle.getText();
    		TodoDao.instance.getModel().replace(this.id, new Todo(this.id, currentText, !currentCompeteFlag));
			break;
		/*
		case "edit":
	    	String newText = jsonNode.get("text").asText();		
			if (newText == null)
		        return Response.status(500)  
		                .entity("Edit operation failed")  
		                .build();
			
			Todo todoToEdit = TodoDao.instance.getModel().get(this.id);
			currentCompeteFlag = todoToEdit.getComplete();
    		TodoDao.instance.getModel().replace(this.id, new Todo(this.id, newText, currentCompeteFlag));
			break;
		*/
		default:
	        return Response.status(500)  
	                .entity("Operation non valid")  
	                .build();
		}

        return Response.status(200)  
                .entity("Addsomethinghere: " + op)  
                .build();

    }

    /*
     * (D) delete a single todo from the todos list.
	 * Which todo to delete is specified in the URI.
     */
    @DELETE
    public Response deleteTodo() {
		TodoDao.instance.getModel().remove(id);

        // frobino: TODO
        // this._commit(this.todos);
	
		// frobino: TODO if/else response
		return Response.noContent().build();
    }

    /*
    private Response putAndGetResponse(Todo todo) {
        Response res;
        if(TodoDao.instance.getModel().contains(todo)) {
            res = Response.noContent().build();
        } else {
            res = Response.created(uriInfo.getAbsolutePath()).build();
        }
        TodoDao.instance.getModel().add(todo);
        return res;
    }
    */

}