package eu.scy.external.tester.environmenttester.test;

import com.jezhumble.javasysmon.JavaSysMon;

import eu.scy.external.tester.environmenttester.Controller;

public class CPUTest implements ITest {
	private String name;
	private String desc;
	private TestResult rslt;
	private Controller ctrl;
	
	public CPUTest() {
		name = "CPU Test";
		desc = "Checking the CPU Speed";
	}
	
	@Override
	public String getDescription() {
		return desc;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public TestResult getResult() {
		return rslt;
	}

	@Override
	public void setCtrl(Controller ctrl) {
		this.ctrl = ctrl;
		
	}

	@Override
	public void run() {
		rslt = new TestResult();
		rslt.setTestName(name);
		JavaSysMon monitor = new JavaSysMon();
		StringBuilder text = new StringBuilder("CPU Cores: ");
		text.append(monitor.numCpus());
		text.append(System.getProperty("line.separator"));
		Double cpuSpeed = (double)(monitor.cpuFrequencyInHz()/1000000);
		text.append("CPU frequency: ");
		text.append(cpuSpeed.toString()).append("Mhz");
		text.append(System.getProperty("line.separator"));
		text.append("System Uptime: ");
		text.append(monitor.uptimeInSeconds()/60).append("min");
		rslt.setResultText(text.toString());
		ctrl.returnResult(rslt);
		
	}

}
