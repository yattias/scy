package eu.scy.client.tools.scydynamics.editor.menu.edit;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.KeyStroke;

import eu.scy.client.tools.scydynamics.editor.ModelEditor;
import eu.scy.client.tools.scydynamics.editor.Util;

@SuppressWarnings("serial")
public class DeleteAction extends AbstractAction {
	
	private final static Logger debugLogger = Logger.getLogger(DeleteAction.class.getName());
	private final Icon smallIcon = Util.getImageIcon("delete16.png");
	private final Icon largeIcon = Util.getImageIcon("delete24.png");
	private ModelEditor editor;
	
	public DeleteAction(ModelEditor editor, boolean showName) {
		super();
		this.editor = editor;
		if (showName) {
			putValue(Action.NAME, "Delete");
		}
		putValue(Action.SMALL_ICON, smallIcon);
		putValue(Action.LARGE_ICON_KEY, largeIcon);
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("DELETE"));
	}
	
	public DeleteAction(ModelEditor editor) {
		this(editor, true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			editor.getSelection().deleteSelection(editor);
		} catch (Exception ex) {
			debugLogger.warning(ex.getMessage());
		}
	}

}
