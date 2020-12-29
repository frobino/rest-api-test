package io.github.frobino.app;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Todo
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Todo {
    private int id;
    private String text;
    private Boolean complete;
    
    Todo(int id, String text, Boolean complete){
        this.id = id;
        this.text = text;
        this.complete = complete;
    }
    
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Boolean getComplete() {
		return complete;
	}
	public void setComplete(Boolean complete) {
		this.complete = complete;
	}
    
}