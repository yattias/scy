package eu.scy.client.tools.scydynamics.editor.menu.edit;

import java.awt.event.ActionEvent;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.KeyStroke;

import eu.scy.client.tools.scydynamics.editor.ModelEditor;
import eu.scy.client.tools.scydynamics.editor.Util;

@SuppressWarnings("serial")
public class RedoAction extends AbstractAction {
	
	private final static Logger debugLogger = Logger.getLogger(RedoAction.class.getName());
	private final Icon smallIcon = Util.getImageIcon("redo16.png");
	private ModelEditor editor;

	public RedoAction(ModelEditor editor) {
		super();
		this.editor = editor;
		putValue(Action.NAME, "Redo");
		putValue(Action.SMALL_ICON, smallIcon);
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control Y"));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			editor.getSelection().redo();
		} catch (Exception ex) {
			debugLogger.warning(ex.getMessage());
		}
	}

}
