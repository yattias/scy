package eu.scy.client.tools.scydynamics.editor.menu.edit;

import javax.swing.JMenu;

import eu.scy.client.tools.scydynamics.editor.ModelEditor;

@SuppressWarnings("serial")
public class EditMenu extends JMenu {

	private UndoAction undoAction;
	private RedoAction redoAction;
	private CutAction cutAction;
	private CopyAction copyAction;
	private PasteAction pasteAction;
	private DeleteAction deleteAction;
	private SelectAllAction selectAllAction;

	public EditMenu(ModelEditor editor) {
		super("Edit");
		this.setMnemonic('E');
		undoAction = new UndoAction(editor);
		redoAction = new RedoAction(editor);
		undoAction.setEnabled(false);
		redoAction.setEnabled(false);
		this.add(undoAction);	
		this.add(redoAction);
		this.addSeparator();
		cutAction = new CutAction(editor);
		copyAction = new CopyAction(editor);
		pasteAction = new PasteAction(editor);
		deleteAction = new DeleteAction(editor);
		this.add(cutAction);
		this.add(copyAction);
		this.add(pasteAction);
		this.add(deleteAction);
		this.addSeparator();
		selectAllAction = new SelectAllAction(editor);
		this.add(selectAllAction);
	}
	
	public void setUndoEnabled(boolean enabled) {
		undoAction.setEnabled(enabled);
	}
	
	public void setRedoEnabled(boolean enabled) {
		redoAction.setEnabled(enabled);
	}
	
}