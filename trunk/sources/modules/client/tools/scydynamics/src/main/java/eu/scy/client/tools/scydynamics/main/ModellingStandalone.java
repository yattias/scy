package eu.scy.client.tools.scydynamics.main;

import java.util.Properties;

@SuppressWarnings("serial")
public class ModellingStandalone extends AbstractModellingStandalone {

	public ModellingStandalone() {
		super("SCYDynamics");
	}

	@Override
	public Properties getProperties() {
		Properties props = new Properties();
		props.put("editor.saveasdataset", "true");
		props.put("show.popouttabs", "true");
		props.put("editor.mode", "quantitative_modelling");
		props.put("editor.modes_selectable", "false");
		props.put("editor.export_to_sqv", "false");
		props.put("showFeedback", "false");
		props.put("editor.showStopButton", "true");
		props.put("autoSave", "false");
		// props.put("editor.fixedcalculationmethod", "euler");

		//
		// File confFile = new File("scydynamics.properties");
		// try {
		// LOGGER.log(Level.INFO,
		// "ModellingStandalone.getProperties(). expecting file at {0}",
		// confFile.getAbsolutePath());
		// if (confFile.exists()) {
		// props.load(new FileInputStream(confFile));
		// }
		// } catch (IOException ex) {
		// LOGGER.warning(ex.getMessage());
		// }
		return props;
	}

	public static void main(String[] args) {
		new ModellingStandalone();
	}

	
}
