package io.github.frobino.app;

import java.util.HashMap;
import java.util.Map;

public enum TodoDao {
    instance;

    private Map<Integer, Todo> todos = new HashMap<Integer, Todo>();

    // Singleton
    private TodoDao() {}
    
    public Map<Integer, Todo> getModel(){
        return todos;
    }

}