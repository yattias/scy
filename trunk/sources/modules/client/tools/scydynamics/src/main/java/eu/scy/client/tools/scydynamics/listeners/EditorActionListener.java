package eu.scy.client.tools.scydynamics.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import eu.scy.client.tools.scydynamics.editor.EditorToolbar;
import eu.scy.client.tools.scydynamics.editor.ModelEditor;
import eu.scy.client.tools.scydynamics.editor.ModelEditor.Mode;

public class EditorActionListener implements ActionListener {

	private ModelEditor editor;

	public EditorActionListener(ModelEditor editor) {
		this.editor = editor;
	}
	
	public void actionPerformed(ActionEvent e) {
		if (editor.getMode()==Mode.BLACK_BOX || editor.getMode()==Mode.CLEAR_BOX) {
    		// editing the model is not allowed -> return!
    		return;
    	}
		String aCmd = e.getActionCommand();
		if (aCmd.equals(EditorToolbar.DELETE+"")) {
			editor.getSelection().deleteSelection(editor);
		} else if (aCmd.equals(EditorToolbar.ALL+"")) {
			editor.selectAllObjects();
		} else if (aCmd.equals(EditorToolbar.COPY+"")) {
			editor.copySelection();
		} else if (aCmd.equals(EditorToolbar.PASTE+"")) {
			editor.pasteSelection();
		} else if (aCmd.equals(EditorToolbar.CUT+"")) {
			editor.cutSelection();
		}
	}
	
}
