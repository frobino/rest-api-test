package io.github.frobino.app;

import java.util.List;
import java.util.Vector;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * Model
 */
@Path("/model")
public class Model {
    
    // Allows to insert contextual objects into the class,
    // e.g. ServletContext, Request, Response, UriInfo
    @Context
    UriInfo uriInfo;
    @Context
    Request request;
	
    public Model(){}
	
	@POST
	@Consumes(MediaType.TEXT_PLAIN)
    public Response addTodo(final String todoText) {
		Vector<Todo> todos = TodoDao.instance.getModel();
		
        int id = todos.size() > 0 ? todos.elementAt(todos.size() - 1).getId() + 1 : 1;
        Todo todo = new Todo(id, todoText, false);
        todos.add(todo);

        return Response.status(201)  
               .entity(" Product added successfuly. Todos size: "+ todos.size())  
               .build();  
        
        // frobino: TODO
        // this._commit(this.todos);
    }
    
	/*
	@POST
	@Consumes(MediaType.TEXT_PLAIN)
    public void deleteTodo(int id) {
		Vector<Todo> todos = TodoDao.instance.getModel();
    	todos.removeIf(todo -> (todo.getId() == id));
    	
        // frobino: TODO
        // this._commit(this.todos);
    }
    */

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public List<Todo> getTodos() {
    	return TodoDao.instance.getModel();
    }

    // Defines that the next path parameter after todos is
    // treated as a parameter and passed to the TodoResources
    // Allows to type http://localhost:8080/com.vogella.jersey.todo/rest/todos/1
    // 1 will be treaded as parameter todo and passed to TodoResource
    @Path("{todo}")
    public TodoResource getTodo(@PathParam("todo") String id) {
        return new TodoResource(uriInfo, request, id);
    }
}