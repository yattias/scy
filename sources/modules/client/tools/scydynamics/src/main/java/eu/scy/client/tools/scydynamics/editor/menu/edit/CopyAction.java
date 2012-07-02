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
public class CopyAction extends AbstractAction {
	
	private final static Logger debugLogger = Logger.getLogger(CopyAction.class.getName());
	private final Icon smallIcon = Util.getImageIcon("copy16.png");
	private final Icon largeIcon = Util.getImageIcon("copy24.png");
	private ModelEditor editor;
	
	public CopyAction(ModelEditor editor, boolean showName) {
		super();
		this.editor = editor;
		if (showName) {
			putValue(Action.NAME, "Copy");
		}
		putValue(Action.SMALL_ICON, smallIcon);
		putValue(Action.LARGE_ICON_KEY, largeIcon);
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control C"));
	}
	
	public CopyAction(ModelEditor editor) {
		this(editor, true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			editor.getSelection().copySelection();
		} catch (Exception ex) {
			debugLogger.warning(ex.getMessage());
		}
	}

}
