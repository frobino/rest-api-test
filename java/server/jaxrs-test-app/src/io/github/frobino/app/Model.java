package io.github.frobino.app;

import java.util.List;
import java.util.Vector;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Model
 */
@Path("/model")
public class Model {
    
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

}