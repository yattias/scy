package eu.scy.external.tester.environmenttester.test;

import eu.scy.external.tester.environmenttester.Controller;

public interface ITest extends Runnable {
	public void setCtrl(Controller ctrl);
	public String getName();
	public String getDescription();
	public TestResult getResult();
}
