package eu.scy.client.tools.scydynamics.model;

import java.util.HashMap;

public class FormulaMapper {

	/*
	 * The ForumulaMapper provides a mapping from the operators and functions
	 * recognized by the JEP libraries to the SimQuest simulation engine. Also,
	 * it ensures that only "valid" and tested operators are used.
	 */
	private static FormulaMapper instance = null;

	private HashMap<String, String> operatorMap;
	private HashMap<String, String> functionMap;

	private FormulaMapper() {
		createOperatorMap();
		createFunctionMap();
	}

	private void createOperatorMap() {
		operatorMap = new HashMap<String, String>();
		operatorMap.put("+", "+");
		operatorMap.put("-", "-");
		operatorMap.put("*", "*");
		operatorMap.put("/", "/");
		operatorMap.put("uminus", "-.");
		operatorMap.put("%", "mod");
		operatorMap.put("^", "^");
	}

	private void createFunctionMap() {
		functionMap = new HashMap<String, String>();
		functionMap.put("sin", "sin");
		functionMap.put("cos", "cos");
		functionMap.put("tan", "tan");
		functionMap.put("asin", "arcsin");
		functionMap.put("acos", "arccos");
		functionMap.put("atan", "arctan");
		functionMap.put("asinh", "arcsinh");
		functionMap.put("acosh", "arccosh");
		functionMap.put("atanh", "arctanh");
		functionMap.put("abs", "abs");
		functionMap.put("round", "round");
		functionMap.put("sqrt", "sqrt");
		functionMap.put("floor", "floor");
		functionMap.put("ln", "log");
		functionMap.put("log", "log10");
		functionMap.put("exp", "exp");
		functionMap.put("rand", "random");
		functionMap.put("min", "min_binary");
		functionMap.put("max", "max_binary");
		functionMap.put("sign", "sign");
	}

	public static FormulaMapper getInstance() {
		if (instance == null) {
			instance = new FormulaMapper();
		}
		return instance;
	}

	public HashMap<String, String> getOperatorMap() {
		return operatorMap;
	}

	public HashMap<String, String> getFunctionMap() {
		return functionMap;
	}

}
