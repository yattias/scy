package eu.scy.client.tools.scydynamics.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;

import colab.um.draw.JdFigure;
import colab.um.xml.model.JxmModel;
import eu.scy.client.tools.scydynamics.editor.ModelEditor.Mode;
import eu.scy.client.tools.scydynamics.model.ModelUtils;

public class VariableDialogListener extends MouseAdapter implements ActionListener {

	private VariableDialog variableDialog;

	public VariableDialogListener(VariableDialog variableDialog) {
		this.variableDialog = variableDialog;
	}

	public void mouseClicked(MouseEvent e) {
		if (e.getSource() instanceof JList) {
			JList variableList = (JList) e.getSource();
			String selected = (variableList.getSelectedValue() == null ? ""
					: (String) variableList.getSelectedValue());
			ModelUtils.paste(selected, variableDialog.getQuantitativeExpressionTextField());
		}
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getActionCommand() == "okay") {
			if (variableDialog.getEditor().getMode()==Mode.QUALITATIVE_MODELLING && variableDialog.getFigure().getType() == JdFigure.AUX) {
				variableDialog.setQualitativeRelations();
			}

			// potentially needed to create an undo-point
			JxmModel oldModelState = variableDialog.getEditor().getModel().getXmModel();
			
			String oldName = (String) variableDialog.getFigureProperty("label");
			String oldExpr = (String) variableDialog.getFigureProperty("expr");
			String oldUnit = (String) variableDialog.getFigureProperty("unit");

			String newName = ModelUtils.cleanVariableName(variableDialog.getNewName());
			
			boolean closeDialog = true;
			variableDialog.setFigureProperty("label", newName);
			if (variableDialog.getEditor().getDomain()!= null && !newName.equals(oldName)) {
				// domain exists & label has been changed
				//System.out.println("newName: "+newName);
				List<String> proposedNames = variableDialog.getEditor().getDomain().proposeNames(newName);
				if (proposedNames != null && proposedNames.isEmpty()) {
					variableDialog.getEditor().getActionLogger().logTermNotRecognized(newName);
					
					String message = "The label of this object could not be identified.\n" +
							"Would you like to enter a different one?";
					int choice = JOptionPane.showConfirmDialog(JOptionPane.getFrameForComponent(variableDialog), message, "Unknown label...", JOptionPane.YES_NO_OPTION);
					if (choice == JOptionPane.YES_OPTION) {
						closeDialog = false;
					}
				} else if (proposedNames != null){
					variableDialog.getEditor().getActionLogger().logTermNotRecognizedProposals(newName);
					String message = "The label of this object could not be fully identified.\n" +
							"Would you like to use one of the following ones?\n\n";
					String s = (String)JOptionPane.showInputDialog(
							JOptionPane.getFrameForComponent(variableDialog), message, "Choose label...", JOptionPane.PLAIN_MESSAGE,
							null,
							proposedNames.toArray(),
							proposedNames.get(0));
					//System.out.println("choice: "+s);
					if (s != null) {
						newName = s;
						variableDialog.setFigureProperty("label", newName);
					} else {
						variableDialog.setFigureProperty("label", newName);
					}
				}
			}

			String unit = variableDialog.getUnit();
			unit = unit.replaceAll("<", "");
			unit = unit.replaceAll(">", "");
			unit = unit.replaceAll("&", "");

			variableDialog.setFigureProperty("unit", unit);            
			variableDialog.submitFigureProperties(oldName);
			String express = getExpression();
			variableDialog.setFigureProperty("expr", express);
			variableDialog.submitFigureProperties(newName);

			if (!variableDialog.getNewColor().equals(variableDialog.getEditor().getModel().getObjectOfName((String) variableDialog.getFigureProperty("label")).getLabelColor())
					|| !oldName.equals(newName)
					|| !oldExpr.equals(variableDialog.getQuantitativeExpression())
					|| !oldUnit.equals(variableDialog.getUnit())) {
				// name, expression, unit or color has changed, send a change-specification-logevent
				variableDialog.getEditor().getSelection().addUndoPoint(oldModelState);
				variableDialog.getEditor().getActionLogger().logChangeSpecification(variableDialog.getFigure().getID(), newName, variableDialog.getQuantitativeExpression(), variableDialog.getUnit(), variableDialog.getEditor().getModelXML());
				// and set the (possibly new) color of the object
				variableDialog.getEditor().getModel().getObjectOfName((String) variableDialog.getFigureProperty("label")).setLabelColor(variableDialog.getNewColor());
			}
			
			if (variableDialog.getEditor().isSynchronized()) {
				variableDialog.getEditor().getModelSyncControl().changeObject(this.variableDialog.getFigure());
            }
			
			if (closeDialog) {
				variableDialog.closeDialog();
			} else {
				return;
			}

		} else if (event.getActionCommand() == "cancel") {
			variableDialog.closeDialog();
		} else if (event.getActionCommand() == "C") {
			variableDialog.setQuantitativeExpression("");
		} else if (event.getActionCommand() == "color") {
			java.awt.Frame frame = javax.swing.JOptionPane.getFrameForComponent(variableDialog);
			ColorDialog cdialog = new ColorDialog(frame, ((JButton)event.getSource()).getLocationOnScreen(), variableDialog, variableDialog.getBundle());
			cdialog.setVisible(true);
		} else {
			// here go the clicks of the "calculator panel"
			ModelUtils.paste(event.getActionCommand(), variableDialog.getQuantitativeExpressionTextField());
		}
	}

	private String getExpression() {
		if (variableDialog.getEditor().getMode().equals(ModelEditor.Mode.QUANTITATIVE_MODELLING)) {
			String newExpression = variableDialog.getQuantitativeExpression();
			newExpression = newExpression.replaceAll("<", "");
			newExpression = newExpression.replaceAll(">", "");
			newExpression = newExpression.replaceAll("&", "");
			return newExpression;
		} else if (variableDialog.getEditor().getMode().equals(ModelEditor.Mode.QUALITATIVE_MODELLING)) {
			switch (variableDialog.getFigure().getType()) {
			case JdFigure.CONSTANT:
			case JdFigure.STOCK:
				return variableDialog.getQualitativeValue()+"";
			case JdFigure.AUX:
				return ModelUtils.getQualitativeExpression(variableDialog.getFigure(), variableDialog.getQualitativeRelations(), variableDialog.getEditor());
			}
		}
		// default = 0
		return "0";
	}

}
