package nl.utwente.gw.modelling.listeners;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import nl.utwente.gw.modelling.editor.ModelEditor;

public class EditorActionListener implements ActionListener {
	
	private ModelEditor editor;

	public EditorActionListener(ModelEditor editor) {
		this.editor = editor;
	}

	public void actionPerformed(ActionEvent evt) {
		String cmd = evt.getActionCommand();
		//System.out.println("JdActionListener: command: "+cmd);

		if ("EditorCursorTB".equals(cmd)) {
			editor.setAction(ModelEditor.A_CURSOR, cmd);
		} else if ("EditorDeleteTB".equals(cmd)) {
			editor.setAction(ModelEditor.A_DELETE, cmd);
		} else if ("EditorStockTB".equals(cmd)) {
			editor.setAction(ModelEditor.A_STOCK, cmd);
		} else if ("EditorAuxTB".equals(cmd)) {
			editor.setAction(ModelEditor.A_AUX, cmd);
		} else if ("EditorConstantTB".equals(cmd)) {
			editor.setAction(ModelEditor.A_CONSTANT, cmd);
		} else if ("EditorDatasetTB".equals(cmd)) {
			editor.setAction(ModelEditor.A_DATASET, cmd);
		} else if ("EditorFlowTB".equals(cmd)) {
			editor.setAction(ModelEditor.A_FLOW, cmd);
		} else if ("EditorRelationTB".equals(cmd)) {
			editor.setAction(ModelEditor.A_RELATION, cmd);
		}	
	}

}
