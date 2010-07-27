package eu.scy.external.tester.environmenttester;

import org.apache.log4j.BasicConfigurator;

public class Run {
	public static void main(String[] args) {
		BasicConfigurator.configure();
		Model model = new Model();
		View view = new View();
		new Controller(model, view);
	}
}
