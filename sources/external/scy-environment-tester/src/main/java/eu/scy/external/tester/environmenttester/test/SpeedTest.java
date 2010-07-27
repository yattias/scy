package eu.scy.external.tester.environmenttester.test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Vector;

import eu.scy.external.tester.environmenttester.Controller;

public class SpeedTest implements ITest {

	
	private TestResult rslt;
	private Controller ctrl;
	private String desc;
	private String name;
	
	public SpeedTest() {
		this.name = "Speed Test";
		this.desc = "This test is used to check your connections bandwidth";
		this.rslt = new TestResult();
	}
	@Override
	public String getDescription() {
		return this.desc;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setCtrl(Controller ctrl) {
		this.ctrl = ctrl;
		
	}
	
	@Override
	public TestResult getResult() {
		return rslt;
	}

	@Override
	public void run() {
		Vector<Long> durations = new Vector<Long>();
		InputStream is = null; 
		URL url;
		try {
			url = new URL("http://188.40.38.84/~pg/scy/5mb");
			int input = 1;
			input = 1;
			is = url.openStream();
			long check = 0;
			long start = System.currentTimeMillis();
			while(input != -1) {
				input = is.read();
				check++;
				if(check == 1048576) {
					durations.add(System.currentTimeMillis());
					check = 0;
				}
			}
			long stop = System.currentTimeMillis();
			long duration = stop-start;
			durations.add(duration);
			is.close();
			Vector<Double> results = new Vector<Double>();
			double kb = 1024;
			long startDuration = start;
			for(int i = 0; i < durations.size()-1; i++) {
				long item = durations.get(i);
				double time = (double)(item-startDuration)/1000;
				double speed = kb/time;
				if(speed < 25) {
					rslt.addWarning("dl speed below 25kb/s: "+speed+"kb/s");
				}
				results.add(speed);
				startDuration = item;
			}
			double result = 0;
			int i;
			for(i = 0; i < results.size(); i++) {
				double item = results.get(i);
				result += item;
			}
			//System.out.println("average speed: " + result/i + "kb/s");
			rslt.setTestName(name);
			//TODO: min speed, max speed, average speed, floor speed to i,ii
			rslt.setResultText("average speed: " + result/i + "kb/s");
		}
		catch(Exception e) {
			rslt.addError(e.getMessage());
		}
		finally {
			if(is != null) {
				try { is.close(); } catch(IOException e) {}
			}
			rslt.setTestName(name);
			ctrl.returnResult(rslt);
		}
		
	}

}
