package eu.scy.client.tools.scydynamics.main;

import java.awt.BorderLayout;
import java.util.Properties;
import javax.swing.*;
import eu.scy.client.tools.scydynamics.editor.ModelEditor;
import eu.scy.client.tools.scydynamics.menu.EditorMenuBar;
import eu.scy.client.tools.scydynamics.store.AppletStore;

@SuppressWarnings("serial")
public class ModellingApplet extends JApplet {

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
	}

	public Properties getProperties() {
		Properties props = new Properties();
		props.put("editor.saveasdataset", "true");
		props.put("editor.showMenu", "true");
		props.put("editor.filetoolbar", "false");
		props.put("show.popouttabs", "false");
		props.put("editor.mode", "quantitative_modelling");
		props.put("editor.modes_selectable", "false");
		props.put("editor.export_to_sqv", "false");
		props.put("showFeedback", "false");
		props.put("editor.showStopButton", "true");
		props.put("autoSave", "false");
		props.put("editor.fixedcalculationmethod", "euler");
		return props;
	}

}
