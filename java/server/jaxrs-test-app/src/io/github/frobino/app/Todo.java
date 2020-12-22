package io.github.frobino.app;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Todo
 */
@XmlRootElement
public class Todo {
    private int id;
    private String text;
    private Boolean complete;
    
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