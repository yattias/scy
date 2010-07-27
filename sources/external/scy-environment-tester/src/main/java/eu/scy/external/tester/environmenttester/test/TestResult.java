package eu.scy.external.tester.environmenttester.test;

import java.util.Vector;

public class TestResult {
	private String testName;
	private String resultText;
	private Vector<String> errors;
	private Vector<String> warnings;
	
	public TestResult() {
		errors = new Vector<String>();
		warnings = new Vector<String>();
	}
	
	public String getTestName() {
		return testName;
	}
	public void setTestName(String testName) {
		this.testName = testName;
	}
	public String getResultText() {
		return resultText;
	}
	public void setResultText(String resultText) {
		this.resultText = resultText;
	}
	public Vector<String> getErrors() {
		return errors;
	}
	public void addError(String error) {
		this.errors.add(error);
	}
	public Vector<String> getWarnings() {
		return warnings;
	}
	public void addWarning(String warning) {
		this.warnings.add(warning);
	}

}
