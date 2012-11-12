package eu.scy.client.tools.scydynamics.menu.file;

import javax.swing.JMenu;

import eu.scy.client.tools.scydynamics.editor.ModelEditor;

@SuppressWarnings("serial")
public class FileMenu extends JMenu {

	public FileMenu(ModelEditor editor) {
		super("File");
		this.setMnemonic('F');
		this.add(new NewAction(editor));
		this.add(new OpenFileAction(editor));
		this.add(new SaveFileAction(editor));
		this.add(new SaveAsFileAction(editor));
		this.add(new SaveAsDatasetFileAction(editor));
		this.addSeparator();
		this.add(new ExitAction(editor));
	}
	
}