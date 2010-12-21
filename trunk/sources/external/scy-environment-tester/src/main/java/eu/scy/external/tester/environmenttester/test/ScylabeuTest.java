package eu.scy.external.tester.environmenttester.test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import eu.scy.external.tester.environmenttester.Controller;
import eu.scy.external.tester.environmenttester.TesterConfig;

public class ScylabeuTest implements ITest {
	private String name;
	private String desc;
	private TestResult rslt;
	private Controller ctrl;
	private String host;
	private String path;
	
	
	public ScylabeuTest() {
		this.name = "ScyLab.eu Test";
		this.desc = "This test checks the connection to the scy-lab content repository";
		host = "www.scy-lab.eu";
		path = "/content/el/mission1/startPage/A_Challenge.html";
		
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
		InputStream is = null;
		try {
			URL url = new URL("http", host, 80, path);
			URLConnection urlCon = url.openConnection();
			urlCon.setConnectTimeout(TesterConfig.TIMEOUT);
			is = urlCon.getInputStream();
			rslt.setResultText("Connected to "+host+path);
		}
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
