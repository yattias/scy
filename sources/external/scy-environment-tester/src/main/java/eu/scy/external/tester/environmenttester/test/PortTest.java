package eu.scy.external.tester.environmenttester.test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import eu.scy.common.configuration.Configuration;
import eu.scy.external.tester.environmenttester.Controller;
import eu.scy.external.tester.environmenttester.TesterConfig;

public class PortTest implements ITest {
	
	private String name;
	private String desc;
	private TestResult rslt;
	private Controller ctrl;
        private final int port;
        private final boolean errorOnFail;

	
        public PortTest(int port, boolean errorOnFail) {
            this.port = port;
            this.errorOnFail = errorOnFail;
            name = "Port " + port + " Test";
            desc = "This test will try to connect on scy.collide.info on Port " + port + ".";
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

		String host = Configuration.getInstance().getOpenFireHost();
		InputStream is = null;
		try {
			URL url = new URL("http", host, port, "/tomcat.gif");
			URLConnection urlCon = url.openConnection();
			urlCon.setConnectTimeout(TesterConfig.TIMEOUT);
			is = urlCon.getInputStream(); 
			rslt.setResultText("Port " + port + " works: Connected to "+host+":"+port);
		}
		catch(IOException e) {
		        if (errorOnFail) {
		            rslt.addError("Could not establish a connection on port " + port + ". IO Exception: " + e.getMessage());
		        } else {
		            rslt.addWarning("Could not establish a connection on port " + port + ". No worries, if the \"Port 80 Test\" was successful.");
		        }
		}
		finally {
			if(is != null) {
				try { is.close(); } catch (Exception e) {}
			}
			rslt.setTestName(name);
			rslt.setResultText(desc);
			ctrl.returnResult(rslt);
		}
	}

}
