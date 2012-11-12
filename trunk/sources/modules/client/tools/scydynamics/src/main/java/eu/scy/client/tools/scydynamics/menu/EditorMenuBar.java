package eu.scy.client.tools.scydynamics.menu;

import javax.swing.JMenuBar;

import eu.scy.client.tools.scydynamics.editor.ModelEditor;
import eu.scy.client.tools.scydynamics.menu.edit.EditMenu;
import eu.scy.client.tools.scydynamics.menu.examples.ExamplesMenu;
import eu.scy.client.tools.scydynamics.menu.file.FileMenu;

@SuppressWarnings("serial")
public class EditorMenuBar extends JMenuBar {
	
	private FileMenu fileMenu;
	private EditMenu editMenu;
	private ExamplesMenu examplesMenu;

	public EditorMenuBar(ModelEditor editor) {
		super();
		this.fileMenu = new FileMenu(editor);
		this.editMenu = new EditMenu(editor);
		this.examplesMenu = new ExamplesMenu(editor);
		this.add(fileMenu);
		this.add(editMenu);
		this.add(examplesMenu);
	}
	
	public void setUndoEnabled(boolean enabled) {
		editMenu.setUndoEnabled(enabled);
	}
	
	public void setRedoEnabled(boolean enabled) {
		editMenu.setRedoEnabled(enabled);
	}

}
