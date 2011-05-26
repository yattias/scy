package eu.scy.external.tester.environmenttester;

import java.util.Vector;

import eu.scy.external.tester.environmenttester.test.CPUTest;
import eu.scy.external.tester.environmenttester.test.EnvironmentDataTest;
import eu.scy.external.tester.environmenttester.test.ITest;
import eu.scy.external.tester.environmenttester.test.PortTest;
import eu.scy.external.tester.environmenttester.test.ScylabeuTest;
import eu.scy.external.tester.environmenttester.test.SpeedTest;
import eu.scy.external.tester.environmenttester.test.TestResult;
import eu.scy.external.tester.environmenttester.test.XMPPOverHTTPTest;
import eu.scy.external.tester.environmenttester.test.XMPPTest;

public class Model {
	
	private Vector<ITest> tests;
	private Vector<TestResult> results;
	private int currentTest = -1;
	
	public Model() {
		//start application logic
		tests = new Vector<ITest>();
		results = new Vector<TestResult>();
		
		tests.add(new PortTest(80, true));
		tests.add(new PortTest(8080, false));
		tests.add(new SpeedTest());
		tests.add(new XMPPTest());
		tests.add(new XMPPOverHTTPTest());
		tests.add(new EnvironmentDataTest());
		tests.add(new CPUTest());
		tests.add(new ScylabeuTest());
		
	}
	
	public Vector<ITest> getTests() {
		return tests;
	}
	public ITest getTest(int id) {
		return tests.get(id);
	}
	
	public ITest getNextTest() {
		currentTest++;
		return tests.get(currentTest);
	}
	
	public boolean testsDone() {
		if(tests.size() == (currentTest + 1)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public int getCurrentTest() {
		return currentTest;
	}
	
	public int getTestCount() {
		return tests.size();
	}
	public void storeResult(TestResult result) {
		results.add(result);
	}
	
	public Vector<TestResult> getResults() {
		return this.results;
	}
	
	public void resetTestCount() {
		this.currentTest = -1;
	}
	
	

}
