package eu.scy.client.tools.scydynamics.editor;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class EditorTab extends JPanel implements ChangeListener {

	private ModelEditor editor;
	private EditorPanel aCanvas;
	private EditorToolbar toolbar;
	private FileToolbar filetoolbar;

	public EditorTab(ModelEditor editor, ActionListener listener) {
		super();
		this.editor = editor;
		this.setLayout(new BorderLayout());
		aCanvas = new EditorPanel();
		toolbar = new EditorToolbar(listener);
		filetoolbar = new FileToolbar(editor);
		this.add(toolbar, BorderLayout.WEST);
		if (editor.getProperties().get("show.filetoolbar").equals("true")) {			
			this.add(filetoolbar, BorderLayout.NORTH);
		}
		this.add(aCanvas, BorderLayout.CENTER);
	}

	public EditorPanel getEditorPanel() {
		return aCanvas;
	}
	
	public EditorToolbar getEditorToolbar() {
		return toolbar;
	}
	
	public FileToolbar getFileToolbar() {
		return this.filetoolbar;
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
		editor.getActionLogger().logActivateWindow("model", null, this);
	}

	public EditorToolbar getToolbar() {
		return toolbar;
	}
	
}
