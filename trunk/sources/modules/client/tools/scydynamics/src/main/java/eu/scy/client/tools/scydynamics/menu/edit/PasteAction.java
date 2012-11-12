package eu.scy.client.tools.scydynamics.menu.edit;

import java.awt.event.ActionEvent;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.KeyStroke;

import eu.scy.client.tools.scydynamics.editor.ModelEditor;
import eu.scy.client.tools.scydynamics.editor.Util;

@SuppressWarnings("serial")
public class PasteAction extends AbstractAction {
	
	private final static Logger debugLogger = Logger.getLogger(PasteAction.class.getName());
	private final Icon smallIcon = Util.getImageIcon("paste16.png");
	private final Icon largeIcon = Util.getImageIcon("paste24.png");
	private ModelEditor editor;
	
	public PasteAction(ModelEditor editor, boolean showName) {
		super();
		this.editor = editor;
		if (showName) {
			putValue(Action.NAME, "Paste");
		}
		putValue(Action.SMALL_ICON, smallIcon);
		putValue(Action.LARGE_ICON_KEY, largeIcon);
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control V"));
	}
	
	public PasteAction(ModelEditor editor) {
		this(editor, true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			editor.getSelection().pasteSelection(editor);
		} catch (Exception ex) {
			debugLogger.warning(ex.getMessage());
		}
	}

}
