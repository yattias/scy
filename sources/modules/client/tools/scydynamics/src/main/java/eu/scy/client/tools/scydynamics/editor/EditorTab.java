package eu.scy.client.tools.scydynamics.editor;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class EditorTab extends JPanel implements ChangeListener {

	private ModelEditor editor;
	private EditorPanel aCanvas;
	private EditorToolbar toolbar;
	private SimulationToolbar simtoolbar;
	private FileToolbar filetoolbar;

	public EditorTab(ModelEditor editor) {
		super();
		this.editor = editor;
		this.setLayout(new BorderLayout());
		aCanvas = new EditorPanel();
		toolbar = new EditorToolbar();
		//simtoolbar = new SimulationToolbar(editor);
		filetoolbar = new FileToolbar(editor);
		this.add(toolbar, BorderLayout.WEST);
		//this.add(simtoolbar, BorderLayout.EAST);
		this.add(filetoolbar, BorderLayout.NORTH);
		this.add(aCanvas, BorderLayout.CENTER);
	}

	public EditorPanel getEditorPanel() {
		return aCanvas;
	}
	
	public EditorToolbar getEditorToolbar() {
		return toolbar;
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
		editor.getActionLogger().logActivateWindow("model", null, this);
	}

	public EditorToolbar getToolbar() {
		return toolbar;
	}
	
}
