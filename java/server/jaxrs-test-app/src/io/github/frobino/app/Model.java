package io.github.frobino.app;

import java.util.List;
import java.util.Vector;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Model
 */
@Path("/model")
public class Model {

    private Vector<Todo> todos;
    
    public Model(){
    	this.todos = new Vector<Todo>();
    }
	
    // private HashMap<Integer, String> localStorage = new HashMap<>();

	@POST
	@Consumes(MediaType.TEXT_PLAIN)
    public void addTodo(final String todoText) {
        int id = this.todos.size() > 0 ? this.todos.elementAt(this.todos.size() - 1).getId() + 1 : 1;
        Todo todo = new Todo(id, todoText, false);
        this.todos.add(todo);

        // frobino: TODO
        // this._commit(this.todos);
    }
    
    /*
    public void deleteTodo(int id) {
    	this.todos.removeIf(todo -> (todo.getId() == id));
    	
        // frobino: TODO
        // this._commit(this.todos);
    }
    */

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public List<Todo> getTodos() {
    	
        // TODO frobino: dummy, for test, remove
        // Todo todo = new Todo(1, "my 1st todo", false);
        // this.todos.add(todo);
    	
        return this.todos;
    }

}