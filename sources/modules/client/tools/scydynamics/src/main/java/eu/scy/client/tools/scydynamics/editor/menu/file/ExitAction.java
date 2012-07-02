package eu.scy.client.tools.scydynamics.editor.menu.file;

import java.awt.event.ActionEvent;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JOptionPane;

import eu.scy.client.tools.scydynamics.editor.ModelEditor;
import eu.scy.client.tools.scydynamics.editor.Util;
import eu.scy.client.tools.scydynamics.logging.ModellingLogger;
import eu.scy.client.tools.scydynamics.store.SCYDynamicsStore.StoreType;

@SuppressWarnings("serial")
public class ExitAction extends AbstractAction {
	
	private final static Logger debugLogger = Logger.getLogger(ExitAction.class.getName());
	private final Icon smallIcon = Util.getImageIcon("exit16.png");
	private ModelEditor editor;

	public ExitAction(ModelEditor editor) {
		super();
		this.editor = editor;
		putValue(Action.NAME, "Exit");
		putValue(Action.SMALL_ICON, smallIcon);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int n = JOptionPane.showConfirmDialog(editor.getAbstractModelling(), "Do you want to save the model\nbefore exiting?", "exiting...", JOptionPane.YES_NO_CANCEL_OPTION);
		if (n == JOptionPane.YES_OPTION) {
			try {
				editor.getSCYDynamicsStore().saveAsModel();	
			} catch (Exception ex) {
				debugLogger.severe(ex.getMessage());
				JOptionPane.showMessageDialog(javax.swing.JOptionPane.getFrameForComponent(editor),
					    "The model could not be stored:\n"+ex.getMessage(),
					    "Warning",
					    JOptionPane.WARNING_MESSAGE);
			}
		} else if (n == JOptionPane.NO_OPTION){
			// doing nothing
		} else if (n == JOptionPane.CANCEL_OPTION) {
			return;
		}
		editor.getActionLogger().logSimpleAction(ModellingLogger.EXIT_APPLICATION);
		editor.getActionLogger().close();
		editor.doAutosave(StoreType.ON_EXIT);
		editor.getAbstractModelling().setVisible(false);
		editor.getAbstractModelling().dispose();
		System.exit(0);
	}

}
