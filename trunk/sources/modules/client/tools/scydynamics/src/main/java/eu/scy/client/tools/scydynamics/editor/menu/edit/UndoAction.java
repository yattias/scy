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
public class UndoAction extends AbstractAction {
	
	private final static Logger debugLogger = Logger.getLogger(UndoAction.class.getName());
	private final Icon smallIcon = Util.getImageIcon("undo16.png");
	private ModelEditor editor;

	public UndoAction(ModelEditor editor) {
		super();
		this.editor = editor;
		putValue(Action.NAME, "Undo");
		putValue(Action.SMALL_ICON, smallIcon);
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control Z"));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			editor.getSelection().undo();
		} catch (Exception ex) {
			debugLogger.warning(ex.getMessage());
		}
	}

}
