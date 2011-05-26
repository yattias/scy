package eu.scy.external.tester.environmenttester.test;

import org.jivesoftware.smack.BOSHConfiguration;
import org.jivesoftware.smack.BOSHConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import eu.scy.common.configuration.Configuration;
import eu.scy.external.tester.environmenttester.Controller;

public class XMPPOverHTTPTest implements ITest {

	private String name;
	private String desc;
	private Controller ctrl;
	private TestResult rslt;
	
	public XMPPOverHTTPTest() {
		this.name ="XMPP over HTTP Connection Test";
		this.desc = "This test will try to connect to the SCY XMPP Server over HTTP";
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
	        BOSHConnection connection = null;
		try {
		        BOSHConfiguration config = new BOSHConfiguration(false, Configuration.getInstance().getOpenFireHTTPHost(), Configuration.getInstance().getOpenFireHTTPPort(), "/http-bind/", Configuration.getInstance().getOpenFireHost());
                        connection = new BOSHConnection((BOSHConfiguration) config);
                        connection.connect();
                        connection.login("scy", "scy", "SET");
			rslt.setResultText("XMPP over HTTP works.");
			connection.disconnect();
		} catch(XMPPException e) {
			rslt.addWarning("Could not establish a XMPP over HTTP connection. You will not be able to connect via HTTP!");
		} finally {
		        if (connection != null) {
		            connection.disconnect();
		        }
			rslt.setTestName(name);
			ctrl.returnResult(rslt);
		}
	}

}
