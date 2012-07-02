package eu.scy.client.tools.scydynamics.editor.menu.file;

import java.awt.event.ActionEvent;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import eu.scy.client.tools.scydynamics.editor.ModelEditor;
import eu.scy.client.tools.scydynamics.editor.Util;

@SuppressWarnings("serial")
public class SaveFileAction extends AbstractAction {
	
	private final static Logger debugLogger = Logger.getLogger(SaveFileAction.class.getName());
	private final Icon smallIcon = Util.getImageIcon("save16.png");
	private ModelEditor editor;

	public SaveFileAction(ModelEditor editor) {
		super();
		this.editor = editor;
		putValue(Action.NAME, "Save");
		putValue(Action.SMALL_ICON, smallIcon);
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control S"));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			editor.getSCYDynamicsStore().saveModel();
		} catch (Exception ex) {
			debugLogger.severe(ex.getMessage());
			JOptionPane.showMessageDialog(javax.swing.JOptionPane.getFrameForComponent(editor),
				    "The model could not be stored:\n"+ex.getMessage(),
				    "Warning",
				    JOptionPane.WARNING_MESSAGE);
		}	
	}

}
