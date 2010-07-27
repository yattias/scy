package eu.scy.external.tester.environmenttester.test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import eu.scy.common.configuration.Configuration;
import eu.scy.external.tester.environmenttester.Controller;

public class Port8080Test implements ITest {
	
	private String name;
	private String desc;
	private TestResult rslt;
	private Controller ctrl;

	
	public Port8080Test() {
		name = "Port 8080 Test";
		desc = "This test will try to connect on scy.collide.info on Port 8080.";
		rslt = new TestResult();
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

		int port = 8080;
		String host = Configuration.getInstance().getOpenFireHost();
		InputStream is = null;
		try {
//			target = new Socket(host, port);
//			System.out.println("conneted to "+host);
//			
//			target.close();
			URL url = new URL("http", host, port, "/tomcat.gif");
			is = url.openStream();
			rslt.setResultText("Port 8080 works: Connected to "+host+":"+port);
		}
//		catch(UnknownHostException e) {
//			rslt.setErrors("Unknown Host Exception "+e.getMessage());
//			System.out.println("unknown host exception");
//		}
		catch(IOException e) {
			rslt.addError("IO Exception: "+e.getMessage());
		}
		finally {
			if(is != null) {
				try { is.close(); } catch (Exception e) { rslt.addError("Could not close InputStream!"); }
			}
			rslt.setTestName(name);
			rslt.setResultText(desc);
			ctrl.returnResult(rslt);
		}
	}

}
