package eu.scy.mobile.toolbroker.demo.ui;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.Gauge;
import java.util.Timer;
import java.util.TimerTask;

public class ProgressBar extends Alert {
	private int counter = 0, max = 10, interval = 1000;
	private Gauge gauge;
	private boolean active;

	public ProgressBar(String title, String description) {
		super(title);
		setString(description);
		setTimeout(3600 * 1000);
		gauge = new Gauge(null, false, max - 1, 0);
		setIndicator(gauge);
	}

	public void start() {
		active = true;
		TimerTask task = new TimerTask() {
			public void run() {
				counter = (counter + 1) % max;
				setCount(counter);
			}
		};
		Timer timer = new Timer();
		timer.schedule(task, 0, interval);
	}

	public void setActive(boolean b) {
		active = b;
	}
	public void setCount(int c) {

		try {
			gauge.setValue(c);
		}
		catch (Exception e) {}
	}
}

