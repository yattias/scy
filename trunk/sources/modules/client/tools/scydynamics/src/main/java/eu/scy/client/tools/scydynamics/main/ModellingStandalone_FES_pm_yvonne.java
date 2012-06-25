package eu.scy.client.tools.scydynamics.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Properties;

import eu.scy.client.desktop.localtoolbroker.LocalFileActionLogger;
import eu.scy.client.tools.scydynamics.logging.ModellingLoggerFES;
import eu.scy.client.tools.scydynamics.logging.TimedEvent;
import eu.scy.client.tools.scydynamics.store.FileStore;
import eu.scy.client.tools.scydynamics.store.SCYDynamicsStore.StoreType;

@SuppressWarnings("serial")
public class ModellingStandalone_FES_pm_yvonne extends AbstractModellingStandalone implements ActionListener {

	private HashSet<eu.scy.client.tools.scydynamics.logging.TimedEvent> timedEvents;
	
	public ModellingStandalone_FES_pm_yvonne() {
		super("SCYDynamics - standalone version - FES");
		String logDir = ((FileStore) getEditor().getSCYDynamicsStore()).getLogDir().getAbsolutePath();
		LocalFileActionLogger fileLogger = new LocalFileActionLogger(logDir);
		fileLogger.setEnableLogging(true);
		ModellingLoggerFES actionLogger = new ModellingLoggerFES(fileLogger, getEditor().getUsername(), "exp_pm_yvonne");
		getEditor().setActionLogger(actionLogger);
		loadOnStart();

		this.timedEvents = new HashSet<TimedEvent>();
		for (int i = 1; i < 120; i++) {
			// autosave every 5 minutes, up to 6 hours (120 times)
			// 300000 millis = 5 minutes
			this.timedEvents.add(new TimedEvent(this, "autosaveFile", i * 300000));
		}
	}
	
	@Override
	public Properties getProperties() {
		Properties props = new Properties();
		props.put("editor.saveasdataset", "false");
		props.put("show.popouttabs", "false");
		props.put("editor.mode", "model_sketching");
		props.put("editor.modes_selectable", "false");
		props.put("editor.export_to_sqv", "false");
		props.put("editor.fixedcalculationmethod", "euler");
		props.put("showFeedback", "true");
		props.put("showPhaseChangeButton", "true");
		props.put("askUsername", "true");
		props.put("multiPlotCheckbox", "false");
		props.put("autoSave", "true");

		props.put("editor.showStopButton", "false");

		//props.put("editor.reference_model", "intro_reference_model.xml");
		props.put("editor.reference_model", "/eu/scy/client/tools/scydynamics/resources/domains/fes/exp_reference_model.xml");
		props.put("editor.concept_set", "/eu/scy/client/tools/scydynamics/resources/domains/fes/exp_concept_set.xml");
		props.put("editor.simulation_settings", "/eu/scy/client/tools/scydynamics/resources/domains/fes/exp_simulation_settings.xml");

		props.put("loadOnStart", "/eu/scy/client/tools/scydynamics/resources/domains/fes/pm_yvonne.xml");

		// loading from a file is not longer used
		// File confFile = new File("scydynamics_FES.properties");
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
		new ModellingStandalone_FES_pm_yvonne();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("autosaveFile")) {
			getEditor().doAutosave(StoreType.TIMER);
		}
	}

}
