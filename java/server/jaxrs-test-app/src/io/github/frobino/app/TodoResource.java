package io.github.frobino.app;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

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
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateTodo(final Todo c) {
    	
    	int updatedId = c.getId();
    	String updatedText = c.getText();
    	// FIXME: as shortcut I am using the complete param to differentiate
    	// between updating whole todo, or if to just toggle the complete flag.
    	// Separate end point or QueryParam would be a better option.
    	boolean toggleComplete = c.getComplete();
    	
    	boolean originalCompleteFlag = TodoDao.instance.getModel().get(updatedId).getComplete();
    	String originalText = TodoDao.instance.getModel().get(updatedId).getText();
    	
    	if (toggleComplete)
    		TodoDao.instance.getModel().replace(updatedId, new Todo(updatedId, originalText, !originalCompleteFlag));
    	else
    		TodoDao.instance.getModel().replace(updatedId, new Todo(updatedId, updatedText, originalCompleteFlag));
        
        return Response.status(200)  
                .entity(" Received todo: "+ c.getId() + " " + c.getText() + " " + c.getComplete())  
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

    	/*
        Todo c = TodoDao.instance.getModel().remove(id);
        if(c==null)
            throw new RuntimeException("Delete: Todo with " + id +  " not found");
        */
		
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