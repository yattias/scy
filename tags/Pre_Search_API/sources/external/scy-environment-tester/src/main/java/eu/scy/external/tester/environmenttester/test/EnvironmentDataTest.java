package eu.scy.external.tester.environmenttester.test;

import eu.scy.external.tester.environmenttester.Controller;

public class EnvironmentDataTest implements ITest  {
	
	private String name;
	private String desc;
	private TestResult rslt;
	private Controller ctrl;
	
	
	public EnvironmentDataTest() {
		this.name = "Environment Data Test";
		this.desc = "This test collects data about your system.";
		this.rslt = new TestResult();
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
		StringBuilder sb = new StringBuilder("Environment Data");
		sb.append(System.getProperty("line.separator"));
		sb.append("Java version: ").append(System.getProperty("java.version"));
		sb.append(System.getProperty("line.separator"));
		sb.append("Java class Version: ").append(System.getProperty("java.class.version"));
		sb.append(System.getProperty("line.separator"));
		sb.append("OS name: ").append(System.getProperty("os.name"));
		sb.append(System.getProperty("line.separator"));
		sb.append("OS architecture: ").append(System.getProperty("os.arch"));
		sb.append(System.getProperty("line.separator"));
		sb.append("OS version: ").append(System.getProperty("os.version"));
		sb.append(System.getProperty("line.separator"));
		sb.append("OS type: ").append(System.getProperty("sun.desktop"));
		sb.append(System.getProperty("line.separator"));
		sb.append("OS patch level: ").append(System.getProperty("sun.os.patch.level"));
		sb.append(System.getProperty("line.separator"));
		sb.append("Memory total: ").append(Runtime.getRuntime().totalMemory());
		sb.append(System.getProperty("line.separator"));
		sb.append("Memory max: ").append(Runtime.getRuntime().maxMemory());
		sb.append(System.getProperty("line.separator"));
		sb.append("CPU processors: ").append(Runtime.getRuntime().availableProcessors());
		sb.append(System.getProperty("line.separator"));
		sb.append("CPU set: ").append(System.getProperty("sun.cpu.isalist"));
		sb.append(System.getProperty("line.separator"));
		sb.append("CPU data model: ").append(System.getProperty("sun.arch.data.model"));
		sb.append(System.getProperty("line.separator"));
		sb.append("User region: ").append(System.getProperty("user.region"));
		sb.append(System.getProperty("line.separator"));
		sb.append("User language: ").append(System.getProperty("user.language"));
		//create result
		rslt.setTestName(name);
		rslt.setResultText(sb.toString());
		ctrl.returnResult(rslt);
	}

}
