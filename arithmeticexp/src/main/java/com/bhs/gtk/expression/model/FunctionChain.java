package com.bhs.gtk.expression.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class FunctionChain extends Expression {

	private List<Function> functions;

	public List<Function> getFunctions() {
		return Collections.unmodifiableList(functions);
	}
	
	public FunctionChain() {
		functions = new ArrayList<>();
	}

	public boolean addFunction(Function function) {
		if (functions == null) {
			functions = new ArrayList<>();
		}
		return functions.add(function);
	}
	
}
