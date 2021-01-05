package io.github.frobino.app;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBElement;

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
    public List<Todo> getTodo() {
        List<Todo> todo = TodoDao.instance.getModel().stream()
        		.filter(t -> (t.getId() == id))
        		.collect(Collectors.toList());
        if (todo.isEmpty())
        	throw new RuntimeException("Get: Todo with " + id +  " not found");
        return todo;
    }

    /*
     * (U) Update (using a post). TODO
     */
    /*
    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    public Response putTodo(JAXBElement<Todo> todo) {
        Todo c = todo.getValue();
        return putAndGetResponse(c);
    }
    */

    /*
     * (D) delete a single todo from the todos list.
	 * Which todo to delete is specified in the URI.
     */
    @DELETE
    public Response deleteTodo() {
		TodoDao.instance.getModel().removeIf(todo -> (todo.getId() == id));

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