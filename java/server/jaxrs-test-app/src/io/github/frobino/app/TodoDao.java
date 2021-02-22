package io.github.frobino.app;

import java.util.LinkedHashMap;
import java.util.Map;

public enum TodoDao {
    instance;

	// HashMap implementation that stores the order in which keys are inserted.
	// Useful to keep track of generated todoIds.
    private Map<Integer, Todo> todos = new LinkedHashMap<Integer, Todo>();

    // Singleton
    private TodoDao() {}
    
    public Map<Integer, Todo> getModel(){
        return todos;
    }

}