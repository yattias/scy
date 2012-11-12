package eu.scy.client.tools.scydynamics.menu.examples;

import java.awt.event.ActionEvent;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;

import eu.scy.client.tools.scydynamics.editor.ModelEditor;

@SuppressWarnings("serial")
public class ExampleAction extends AbstractAction {
	
	private final static Logger debugLogger = Logger.getLogger(ExampleAction.class.getName());
	private ModelEditor editor;
	private String fileName;

	public ExampleAction(ModelEditor editor, String exampleName, String fileName) {
		super();
		this.editor = editor;
		this.fileName = fileName;
		putValue(Action.NAME, exampleName);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int answer = JOptionPane.NO_OPTION;
		if (!editor.getModel().getObjects().isEmpty()) {
			answer = JOptionPane.showConfirmDialog(editor, "Do you want to save the current model\nbefore loading the example?", "load example...", JOptionPane.YES_NO_CANCEL_OPTION);
		}
		if (answer == JOptionPane.YES_OPTION) {
			try {
				editor.getSCYDynamicsStore().saveAsModel();	
			} catch (Exception ex) {
				debugLogger.severe(ex.getMessage());
				JOptionPane.showMessageDialog(javax.swing.JOptionPane.getFrameForComponent(editor),
					    "The model could not be stored:\n"+ex.getMessage(),
					    "Warning",
					    JOptionPane.WARNING_MESSAGE);
			}
		} else if (answer == JOptionPane.NO_OPTION){
			try {
				editor.getSelection().addUndoPoint();
				editor.getSCYDynamicsStore().loadModel(fileName);
			} catch (Exception ex) {
				debugLogger.severe(ex.getMessage());
				JOptionPane.showMessageDialog(javax.swing.JOptionPane.getFrameForComponent(editor),
					    "The model could not be loaded:\n"+ex.getMessage(),
					    "Warning",
					    JOptionPane.WARNING_MESSAGE);
			}
		} else if (answer == JOptionPane.CANCEL_OPTION) {
			return;
		}
		
	}

}
