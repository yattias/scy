package eu.scy.external.tester.environmenttester.test;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import eu.scy.common.configuration.Configuration;
import eu.scy.external.tester.environmenttester.Controller;

public class XMPPTest implements ITest {

	private String name;
	private String desc;
	private Controller ctrl;
	private TestResult rslt;
	
	public XMPPTest() {
		this.name ="XMPP Connection Test";
		this.desc = "This test will try to connect to the SCY XMPP Server";
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
		try {
//			ConnectionConfiguration cfg = new ConnectionConfiguration(Configuration.getInstance().getOpenFireHost(), Configuration.getInstance().getOpenFirePort());
			ConnectionConfiguration cfg = new ConnectionConfiguration(Configuration.getInstance().getOpenFireHost(), Configuration.getInstance().getOpenFirePort());
			XMPPConnection con = new XMPPConnection(cfg);
			con.connect();
			con.login("pg", "pgtest"); //FIXME: use a "test-only" user :)
			rslt.setResultText("XMPP works.");
			con.disconnect();
		}
		catch(XMPPException e) {
			rslt.addError("XMPP Exception: "+e.getStackTrace());
		}
		finally {
			rslt.setTestName(name);
			ctrl.returnResult(rslt);
		}
	}

}
