package eu.scy.agents.impl;

import java.util.HashMap;

import eu.scy.agents.api.IParameter;

public class Parameter implements IParameter {

	private HashMap<String, Object> paramMap;

	public Parameter() {
		paramMap = new HashMap<String, Object>();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(String name) {
		return (T) paramMap.get(name);
	}

	@Override
	public <T> void set(String name, T value) {
		paramMap.put(name, value);
	}
}
