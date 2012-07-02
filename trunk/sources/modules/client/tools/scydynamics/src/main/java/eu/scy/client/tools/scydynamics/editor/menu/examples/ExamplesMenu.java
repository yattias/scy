package eu.scy.client.tools.scydynamics.editor.menu.examples;

import javax.swing.JMenu;

import eu.scy.client.tools.scydynamics.editor.ModelEditor;

@SuppressWarnings("serial")
public class ExamplesMenu extends JMenu {

	public ExamplesMenu(ModelEditor editor) {
		super("Examples");
		this.setMnemonic('E');
		this.add(new ExampleAction(editor, "Predator-prey system", "/eu/scy/client/tools/scydynamics/resources/examples/prey_predator.xml"));
		this.add(new ExampleAction(editor, "Leaking bucket", "/eu/scy/client/tools/scydynamics/resources/examples/leaking_bucket.xml"));
		this.add(new ExampleAction(editor, "Mother-daughter decay", "/eu/scy/client/tools/scydynamics/resources/examples/mother_daughter_decay.xml"));
		this.add(new ExampleAction(editor, "Glucose-insulin cycle", "/eu/scy/client/tools/scydynamics/resources/examples/glucose.xml"));
	}
	
}