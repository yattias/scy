package eu.scy.client.tools.scydynamics.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;

import colab.um.draw.JdFigure;
import eu.scy.client.tools.scydynamics.domain.DomainUtils;
import eu.scy.client.tools.scydynamics.editor.ModelEditor.Mode;
import eu.scy.client.tools.scydynamics.model.ModelUtils;
import eu.scy.client.tools.scydynamics.model.SimquestModelQualitative;

public class VariableDialogListener implements ActionListener, MouseListener {

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
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getActionCommand() == "okay") {
			if (variableDialog.getEditor().getMode()==Mode.QUALITATIVE_MODELLING && variableDialog.getFigure().getType() == JdFigure.AUX) {
				variableDialog.setQualitativeRelations();
			}

			String oldName = (String) variableDialog.getFigureProperty("label");
			String oldExpr = (String) variableDialog.getFigureProperty("expr");
			String oldUnit = (String) variableDialog.getFigureProperty("unit");

			// removing spaces and special chars in variable name
			// (as they may crash the simulation engine
			// or xml serialising)
			String newName = variableDialog.getNewName();
			newName = newName.replaceAll("\\s+", "_");
			newName = newName.replaceAll("<", "");
			newName = newName.replaceAll(">", "");
			newName = newName.replaceAll("&", "");
			newName = newName.toLowerCase();
			int labelChoice = JOptionPane.NO_OPTION;
			variableDialog.setFigureProperty("label", newName);
			if (variableDialog.getEditor().getDomain()!= null && !newName.equals(oldName)) {
				System.out.println("newName: "+newName);
				List<String> proposedNames = variableDialog.getEditor().getDomain().proposeNames(newName);
				System.out.println("proposedNames: "+proposedNames);
				if (proposedNames == null) {
					// the label could be found in domain, doing nothing
				} else {
					labelChoice = DomainUtils.chooseDifferentLabelDialog(JOptionPane.getFrameForComponent(variableDialog), proposedNames);
				}
			}

			// cleaning some bad chars in expression
			String express;
			if (variableDialog.getEditor().getMode().equals(ModelEditor.Mode.QUALITATIVE_MODELLING)) {
				String dialogExpress = variableDialog.getQualitativeExpression();
				express = SimquestModelQualitative.getQualitativeValue(newName, dialogExpress, variableDialog.getEditor());
				System.out.println(newName+": "+dialogExpress+" -> "+express);
			} else {
				express = variableDialog.getQuantitativeExpression();
			}
			express = express.replaceAll("<", "");
			express = express.replaceAll(">", "");
			express = express.replaceAll("&", "");
			// cleaning some bad chars in unit
			String unit = variableDialog.getUnit();
			unit = unit.replaceAll("<", "");
			unit = unit.replaceAll(">", "");
			unit = unit.replaceAll("&", "");

			if (variableDialog.getEditor().getMode()==Mode.QUALITATIVE_MODELLING) {
				if (variableDialog.getFigure().getType() == JdFigure.AUX) {
					System.out.println("+++ generating expression for "+newName);
					variableDialog.setFigureProperty("expr", ModelUtils.getQualitativeExpression(variableDialog.getQualitativeRelations(), variableDialog.getEditor().getModel()));
				} else {
					variableDialog.setFigureProperty("expr", variableDialog.getQualitativeExpression()+"");
				}	
			} else {
				variableDialog.setFigureProperty("expr", express);
			}

			if (variableDialog.getEditor().getMode()==Mode.MODEL_SKETCHING && variableDialog.getFigureProperty("expr").toString().isEmpty()) {
				variableDialog.setFigureProperty("expr", "0");
			}

			variableDialog.setFigureProperty("unit", unit);            
			variableDialog.submitFigureProperties(oldName);
			
			if (!variableDialog.getNewColor().equals(variableDialog.getEditor().getModel().getObjectOfName((String) variableDialog.getFigureProperty("label")).getLabelColor())
					|| !oldName.equals(newName)
					|| !oldExpr.equals(variableDialog.getQuantitativeExpression())
					|| !oldUnit.equals(variableDialog.getUnit())) {
				// name, expression, unit or color has changed, send a change-specification-logevent
				variableDialog.getEditor().getActionLogger().logChangeSpecification(variableDialog.getFigure().getID(), newName, variableDialog.getQuantitativeExpression(), variableDialog.getUnit(), variableDialog.getEditor().getModelXML());
				// and set the (possibly new) color of the object
				variableDialog.getEditor().getModel().getObjectOfName((String) variableDialog.getFigureProperty("label")).setLabelColor(variableDialog.getNewColor());
			}

			if (labelChoice == JOptionPane.NO_OPTION) {
				// user said no, closing
				variableDialog.closeDialog();
			} else {
				// user said 'yay', returning
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
	
}
