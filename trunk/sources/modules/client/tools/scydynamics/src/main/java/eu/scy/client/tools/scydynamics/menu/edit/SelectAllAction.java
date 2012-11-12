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
public class SelectAllAction extends AbstractAction {
	
	private final static Logger debugLogger = Logger.getLogger(SelectAllAction.class.getName());
	private final Icon smallIcon = Util.getImageIcon("selectall16.png");
	private ModelEditor editor;
	
	public SelectAllAction(ModelEditor editor, boolean showName) {
		super();
		this.editor = editor;
		if (showName) {
			putValue(Action.NAME, "Select all");
		}
		putValue(Action.SMALL_ICON, smallIcon);
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control A"));
	}
	
	public SelectAllAction(ModelEditor editor) {
		this(editor, true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			editor.selectAllObjects();
		} catch (Exception ex) {
			debugLogger.warning(ex.getMessage());
		}
	}

}
