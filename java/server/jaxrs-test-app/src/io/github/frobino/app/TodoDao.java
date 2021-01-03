package io.github.frobino.app;

import java.util.Vector;

public enum TodoDao {
    instance;

    private Vector<Todo> todos = new Vector<Todo>();

    private TodoDao() {

    	/*
        Todo todo = new Todo("1", "Learn REST");
        todo.setDescription("Read https://www.vogella.com/tutorials/REST/article.html");
        contentProvider.put("1", todo);
        todo = new Todo("2", "Do something");
        todo.setDescription("Read complete http://www.vogella.com");
        contentProvider.put("2", todo);
        */

    }
    public Vector<Todo> getModel(){
        return todos;
    }

}