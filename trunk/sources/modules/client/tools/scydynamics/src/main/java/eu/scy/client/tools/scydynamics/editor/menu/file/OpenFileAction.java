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
public class OpenFileAction extends AbstractAction {
	
	private final static Logger debugLogger = Logger.getLogger(OpenFileAction.class.getName());
	private final Icon smallIcon = Util.getImageIcon("open16.png");
	private ModelEditor editor;

	public OpenFileAction(ModelEditor editor) {
		super();
		this.editor = editor;
		putValue(Action.NAME, "Open...");
		putValue(Action.SMALL_ICON, smallIcon);
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control O"));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			editor.getSelection().addUndoPoint();
			editor.getSCYDynamicsStore().loadModel();
		} catch (Exception ex) {
			debugLogger.severe(ex.getMessage());
			JOptionPane.showMessageDialog(javax.swing.JOptionPane.getFrameForComponent(editor),
				    "The model could not be loaded:\n"+ex.getMessage(),
				    "Warning",
				    JOptionPane.WARNING_MESSAGE);
		}
	}

}
