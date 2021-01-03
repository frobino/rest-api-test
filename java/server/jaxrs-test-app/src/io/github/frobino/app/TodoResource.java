package io.github.frobino.app;

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

    //Application integration
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Todo getTodo() {
        Todo todo = TodoDao.instance.getModel().get(id);
        if(todo==null)
            throw new RuntimeException("Get: Todo with " + id +  " not found");
        return todo;
    }

    /*
    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    public Response putTodo(JAXBElement<Todo> todo) {
        Todo c = todo.getValue();
        return putAndGetResponse(c);
    }
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