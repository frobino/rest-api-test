package io.github.frobino.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    
	/*
	 * (C) create a todo.
	 * 
	 * Not using a PUT here because we are providing the URI of the "model" resource,
	 * not the URI identifying the todo.id (e.g. model/1)
	 */
	@POST
	@Consumes(MediaType.TEXT_PLAIN)
    public Response addTodo(final String todoText) {
		Map<Integer,Todo> todos = TodoDao.instance.getModel();
		
        int id = todos.size() > 0 ? todos.get(todos.size()).getId() + 1 : 1;
        Todo todo = new Todo(id, todoText, false);
        todos.put(id, todo);

        // debug printf similar
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

	/*
	 * (R) read the list of todos from the model
	 */
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public List<Todo> getTodos() {
    	return new ArrayList<Todo>(TodoDao.instance.getModel().values());
    }

    // Defines that the next path parameter after todos is
    // treated as a parameter and passed to the TodoResources
    // Allows to type http://localhost:8080/com.vogella.jersey.todo/rest/todos/1
    // 1 will be treaded as parameter todo.id and passed to TodoResource
    @Path("{todoId}")
    public TodoResource getTodo(@PathParam("todoId") String id) {
        return new TodoResource(uriInfo, request, id);
    }
}