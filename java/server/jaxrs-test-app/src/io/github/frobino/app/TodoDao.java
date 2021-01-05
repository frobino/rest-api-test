package io.github.frobino.app;

import java.util.Vector;

public enum TodoDao {
    instance;

	// TODO: replace with hashmap
    private Vector<Todo> todos = new Vector<Todo>();

    // Singleton
    private TodoDao() {}
    
    public Vector<Todo> getModel(){
        return todos;
    }

}