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
public class ModellingStandalone_Thilo extends AbstractModellingStandalone implements ActionListener {

	private HashSet<eu.scy.client.tools.scydynamics.logging.TimedEvent> timedEvents;
	
	public ModellingStandalone_Thilo() {
		super("SCYDynamics - standalone version - T.D.");
		String logDir = ((FileStore) getEditor().getSCYDynamicsStore()).getLogDir().getAbsolutePath();
		LocalFileActionLogger fileLogger = new LocalFileActionLogger(logDir);
		fileLogger.setEnableLogging(true);
		getEditor().setActionLogger(new ModellingLoggerFES(fileLogger, getEditor().getUsername(), "n/a"));
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
		props.put("editor.mode", "quantitative_modelling");
		props.put("editor.modes_selectable", "true");
		props.put("editor.export_to_sqv", "false");
		props.put("editor.fixedcalculationmethod", "euler");
		props.put("showFeedback", "false");
		props.put("showPhaseChangeButton", "false");
		props.put("askUsername", "true");
		props.put("multiPlotCheckbox", "false");
		props.put("autoSave", "true");

		props.put("editor.showStopButton", "true");

		props.put("editor.reference_model", "thilo_reference_model.xml");
		props.put("editor.concept_set", "thilo_concept_set.xml");
		props.put("editor.simulation_settings", "thilo_simulation_settings.xml");

		props.put("loadOnStart", "thilo_model_qualitative_nl.xml");
		//props.put("loadOnStart", "thilo_partial_model.xml");
		return props;
	}

	public static void main(String[] args) {
		new ModellingStandalone_Thilo();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("autosaveFile")) {
			getEditor().doAutosave(StoreType.TIMER);
		}
	}

}
