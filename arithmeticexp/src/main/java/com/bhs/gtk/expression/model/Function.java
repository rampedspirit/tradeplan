package com.bhs.gtk.expression.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class Function {
	private String name;
	private List<String> arguments;
	
	public Function() {
	}
	
	public String getName() {
		return name;
	}
	
	public Function(String name) {
		this.name = name;
		arguments = new ArrayList<>();
	}
	
	public List<String> getArguments() {
		return Collections.unmodifiableList(arguments);
	}
	
	public boolean addArgument (String arg) {
		if(arguments == null) {
			arguments = new ArrayList<>();
		}
		return arguments.add(arg);
	}

}
