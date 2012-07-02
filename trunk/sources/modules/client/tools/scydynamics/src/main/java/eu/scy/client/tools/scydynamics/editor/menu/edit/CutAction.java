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
public class CutAction extends AbstractAction {
	
	private final static Logger debugLogger = Logger.getLogger(CutAction.class.getName());
	private final Icon smallIcon = Util.getImageIcon("cut16.png");
	private final Icon largeIcon = Util.getImageIcon("cut24.png");
	private ModelEditor editor;
	
	public CutAction(ModelEditor editor, boolean showName) {
		super();
		this.editor = editor;
		if (showName) {
			putValue(Action.NAME, "Cut");
		}
		putValue(Action.SMALL_ICON, smallIcon);
		putValue(Action.LARGE_ICON_KEY, largeIcon);
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control C"));
	}
	
	public CutAction(ModelEditor editor) {
		this(editor, true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			editor.getSelection().cutSelection(editor);
		} catch (Exception ex) {
			debugLogger.warning(ex.getMessage());
		}
	}

}
