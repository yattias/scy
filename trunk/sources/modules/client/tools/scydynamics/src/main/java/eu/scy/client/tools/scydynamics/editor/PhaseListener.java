package eu.scy.client.tools.scydynamics.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import colab.um.xml.model.JxmModel;

import eu.scy.client.tools.scydynamics.editor.ModelEditor.Mode;
import eu.scy.client.tools.scydynamics.logging.ModellingLogger;

public class PhaseListener implements ActionListener {

	private final static Logger debugLogger = Logger.getLogger(PhaseListener.class.getName());

	private ModelEditor editor;
	private JxmModel quickSaveModel;

	public PhaseListener(ModelEditor editor) {
		this.editor = editor;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equalsIgnoreCase("nextPhase")) {
			int answer = JOptionPane.showConfirmDialog(editor, "Do you want to proceed to the next phase?\n" + "(You cannot go back.)", "Are you sure?", JOptionPane.YES_NO_OPTION);
			if (answer == JOptionPane.YES_OPTION) {
				quickSaveModel = editor.getModel().getXmModel();
				editor.setMode(Mode.QUALITATIVE_MODELLING);
				editor.getFileToolbar().getPhaseButton().setEnabled(false);
				editor.getActionLogger().logSimpleAction(ModellingLogger.NEXT_PHASE, editor.getModelXML());
			}
		} else if (e.getActionCommand().equalsIgnoreCase("reset")) {
			if (editor.getMode().equals(Mode.MODEL_SKETCHING)) {
				// fall back to given model
				int answer = JOptionPane.showConfirmDialog(editor, "Do you want to reset to the start-model?\n" + "(Your changes will be lost.)", "Are you sure?", JOptionPane.YES_NO_OPTION);
				if (answer == JOptionPane.YES_OPTION) {
					debugLogger.info("loading start-model");
					loadStartModel();
					editor.getActionLogger().logSimpleAction(ModellingLogger.MODEL_RESET, editor.getModelXML());
				}
			} else if (editor.getMode().equals(Mode.QUALITATIVE_MODELLING)) {
				// fall back to quick-save model
				int answer = JOptionPane.showConfirmDialog(editor, "Do you want to reset to the first qualitative model?\n" + "(Your changes will be lost.)", "Are you sure?", JOptionPane.YES_NO_OPTION);
				if (answer == JOptionPane.YES_OPTION) {
					debugLogger.info("restoring quicksave-model");
					editor.setJxmModel(quickSaveModel);
					editor.getActionLogger().logSimpleAction(ModellingLogger.MODEL_RESET, editor.getModelXML());
				}
			}
		}

	}

	private void loadStartModel() {
		String loadOnStart = editor.getProperties().getProperty("loadOnStart");
		if (loadOnStart == null || loadOnStart.isEmpty()) {
			debugLogger.warning("couldn't find property 'loadOnStart'");
			return;
		} else {
			try {
				editor.getSCYDynamicsStore().loadModel(loadOnStart);
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(javax.swing.JOptionPane.getFrameForComponent(editor),
					    "The model could not be loaded:\n"+ex.getMessage(),
					    "Warning",
					    JOptionPane.WARNING_MESSAGE);
			}
		}
	}

}
