package eu.scy.client.tools.scydynamics.main;

import java.awt.BorderLayout;
import java.util.Properties;

import javax.swing.JApplet;
import javax.swing.JPanel;

import eu.scy.client.tools.scydynamics.editor.ModelEditor;
import eu.scy.client.tools.scydynamics.menu.EditorMenuBar;
import eu.scy.client.tools.scydynamics.store.AppletStore;

@SuppressWarnings("serial")
public class ModellingApplet_Decay extends JApplet {

	private ModelEditor editor;

	@Override
	public void init() {
		JPanel panel = new JPanel();
		editor = new ModelEditor(getProperties(), AbstractModellingStandalone.getUsername(getProperties()), null);
		editor.setSCYDynamicsStore(new AppletStore(editor));
		editor.updateTitle();
		panel.setLayout(new BorderLayout());
		panel.add(editor, BorderLayout.CENTER);
		panel.setSize(600, 400);
		panel.setVisible(true);
		this.getContentPane().add(panel);
		EditorMenuBar menuBar = new EditorMenuBar(editor);
		editor.setEditorMenuBar(menuBar);
		this.setJMenuBar(menuBar);
		loadOnStart();
	}
	
	protected void loadOnStart() {
		String loadOnStart = this.getProperties().getProperty("loadOnStart");
		if (loadOnStart == null || loadOnStart.isEmpty()) {
			return;
		} else {
			try {
				editor.getSCYDynamicsStore().loadModel(loadOnStart);
				editor.getSCYDynamicsStore().setModelName(null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
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
		props.put("askUsername", "false");
		props.put("multiPlotCheckbox", "false");
		props.put("autoSave", "false");
		props.put("editor.showStopButton", "false");

		props.put("editor.reference_model", "/eu/scy/client/tools/scydynamics/resources/domains/golab/decay_reference_model.xml");
		props.put("editor.concept_set", "/eu/scy/client/tools/scydynamics/resources/domains/golab/decay_concept_set.xml");
		props.put("editor.simulation_settings", "/eu/scy/client/tools/scydynamics/resources/domains/golab/decay_simulation_settings.xml");
		props.put("loadOnStart", "/eu/scy/client/tools/scydynamics/resources/domains/golab/pm_decay.xml");

		return props;
	}

}
