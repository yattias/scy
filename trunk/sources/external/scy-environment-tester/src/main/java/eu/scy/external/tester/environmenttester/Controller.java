package eu.scy.external.tester.environmenttester;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import eu.scy.external.tester.environmenttester.test.ITest;
import eu.scy.external.tester.environmenttester.test.TestResult;

public class Controller {
	private Model model;
	private View view;
	private int currentTest = 0;
	public boolean testsRunning = false;
	private Thread testThread;
	private int errors = 0;
        private int warnings = 0;
	
	public Controller(Model model, View view) {
		this.model = model;
		this.view = view;
		
		view.addStartBTListener(new StartActionListener());
		view.addLogActionListener(new LogListener());
	}

	
	public void returnResult(TestResult rslt) {
		model.storeResult(rslt);
		int i = model.getCurrentTest();
		if(rslt.getErrors().size() > 0) {
		    view.changeImage(i, "fail.png");
		    errors++;
		} else if (rslt.getWarnings().size() > 0) {
		    view.changeImage(i, "warning.png");
		    warnings++;
	        } else {
		    view.changeImage(i, "ok.png");
		}
		if(!model.testsDone()) {
			nextTest();
		}
		else {
			testsRunning = false;
			model.resetTestCount();
			if(errors > 0 || warnings > 0) {
			    view.showErrorPopup(errors, warnings);
			} else {
			    view.showOkPopup();
			}
		}
	}
	
	private void nextTest() {
		ITest test = model.getNextTest();
		view.disableTestBT();
		test.setCtrl(this);
		view.changeImage(model.getCurrentTest(), "wait.gif");
		testThread = new Thread(test);
		testThread.start();
	}
	
	private class StartActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			view.startTestMode(model.getTests());
			nextTest();
			testsRunning = true;
		}
	}
	
	public class StoreResultsListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("");
		}

	}
	
	public class LogListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			//generate the log
			Calendar cal = Calendar.getInstance();
			DateFormat df = new SimpleDateFormat();
			StringBuilder 	log  = new StringBuilder("SCY ENVIRONMENT TEST");
			log.append(System.getProperty("line.separator"));
			log.append("----------------");
			log.append(System.getProperty("line.separator"));
			log.append(df.format(cal.getTime()));
			log.append(System.getProperty("line.separator"));
			log.append(System.getProperty("line.separator"));
			int i = 0;
			for(TestResult result:model.getResults()) {
				i++;
				log.append("Test ").append(i).append(": "+result.getTestName());
				log.append(System.getProperty("line.separator"));
				log.append(result.getResultText());
				log.append(System.getProperty("line.separator"));
				log.append("Warnings: ").append(result.getWarnings().size());
				log.append(System.getProperty("line.separator"));
				if(result.getWarnings().size() > 0) {
					for(String warning:result.getWarnings()) {
						log.append(warning);
						log.append(System.getProperty("line.separator"));
					}
				}
				log.append("Errors: ").append(result.getErrors().size());
				log.append(System.getProperty("line.separator"));
				if(result.getErrors().size() > 0) {
					for(String error:result.getErrors()) {
						log.append("\t").append(error);
						log.append(System.getProperty("line.separator"));
					}
				}
				log.append(System.getProperty("line.separator"));
			}
			new LogView(log.toString(), view);
		}
		
	}
}
