package io.github.frobino.app;

import java.util.HashMap;
import java.util.Vector;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Model
 */
@Path("/model")
public class Model {

	/*
    private HashMap<Integer, String> localStorage = new HashMap<>();
    private Vector<Todo> todos;

    public void addTodo(String todoText) {
        Todo todo = new Todo();
        int id = this.todos.size() > 0 ? this.todos.elementAt(this.todos.size() - 1).getId() + 1 : 1;
        todo.setId(id);
        todo.setText(todoText);
        todo.setComplete(false);
    
        this.todos.add(todo);

        // frobino: TODO
        // this._commit(this.todos);
    }
    
    
    public void deleteTodo(int id) {
    	this.todos.removeIf(todo -> (todo.getId() == id));
    	
        // frobino: TODO
        // this._commit(this.todos);
    }
    */

    // This method is called if JSON is requested
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Todo getTodos() {
    	
        // TODO frobino: dummy, for test, remove
        Todo todo = new Todo();
        todo.setId(1);
        todo.setText("my 1st todo");
        todo.setComplete(false);
    	
        return todo;
    }

}