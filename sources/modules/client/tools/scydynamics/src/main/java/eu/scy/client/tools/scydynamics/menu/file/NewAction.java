package eu.scy.client.tools.scydynamics.menu.file;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.KeyStroke;

import eu.scy.client.tools.scydynamics.editor.ModelEditor;
import eu.scy.client.tools.scydynamics.editor.Util;
import eu.scy.client.tools.scydynamics.logging.ModellingLogger;
import eu.scy.client.tools.scydynamics.store.SCYDynamicsStore.StoreType;

@SuppressWarnings("serial")
public class NewAction extends AbstractAction {
	
	final Icon smallIcon = Util.getImageIcon("new16.png");
	private ModelEditor editor;

	public NewAction(ModelEditor editor) {
		super();
		this.editor = editor;
		putValue(Action.NAME, editor.getBundle().getString("EDITOR_NEW"));
		putValue(Action.SMALL_ICON, smallIcon);
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control N"));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		editor.getSelection().addUndoPoint();
		editor.setNewModel();
		editor.doAutosave(StoreType.ON_NEW);
		editor.getActionLogger().logSimpleAction(ModellingLogger.MODEL_NEW);
	}

}
