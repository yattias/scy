package eu.scy.client.tools.scydynamics.menu.file;

import java.awt.event.ActionEvent;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JOptionPane;

import eu.scy.client.tools.scydynamics.editor.ModelEditor;
import eu.scy.client.tools.scydynamics.editor.Util;

@SuppressWarnings("serial")
public class SaveAsFileAction extends AbstractAction {
	
	private final static Logger debugLogger = Logger.getLogger(SaveAsFileAction.class.getName());
	private final Icon smallIcon = Util.getImageIcon("saveas16.png");
	private ModelEditor editor;

	public SaveAsFileAction(ModelEditor editor) {
		super();
		this.editor = editor;
		putValue(Action.NAME, editor.getBundle().getString("EDITOR_SAVEAS"));
		putValue(Action.SMALL_ICON, smallIcon);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			editor.getSCYDynamicsStore().saveAsModel();
		} catch (Exception ex) {
			debugLogger.severe(ex.getMessage());
			JOptionPane.showMessageDialog(javax.swing.JOptionPane.getFrameForComponent(editor),
				    "The model could not be stored:\n"+ex.getMessage(),
				    "Warning",
				    JOptionPane.WARNING_MESSAGE);
		}
	}

}
