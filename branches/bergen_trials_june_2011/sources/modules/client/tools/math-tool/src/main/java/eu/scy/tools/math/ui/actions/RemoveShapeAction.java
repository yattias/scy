package eu.scy.tools.math.ui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JOptionPane;

import eu.scy.tools.math.controller.MathToolController;
import eu.scy.tools.math.shapes.IMathShape;
import eu.scy.tools.math.ui.UIUtils;
import eu.scy.tools.math.ui.images.Images;
import eu.scy.tools.math.ui.panels.ControlPanel;
import eu.scy.tools.math.ui.panels.ShapeCanvas;

public class RemoveShapeAction extends AbstractAction {

	private MathToolController mathToolController;
	
	public RemoveShapeAction(MathToolController mathToolController) {
		putValue(Action.NAME, "Remove Shape");
		putValue(Action.SMALL_ICON, Images.Trash.getIcon());
		putValue(Action.SHORT_DESCRIPTION, "Click to removes a selected shape from the canvas.");
		this.mathToolController = mathToolController;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		JComponent source = (JComponent) e.getSource();
		String type = (String) source.getClientProperty(UIUtils.TYPE);
		
		
		
		int showConfirmDialog = JOptionPane.showConfirmDialog(null, "Are you sure you want to remove the selected shape and all its table rows?", "Remove Shape", JOptionPane.YES_NO_OPTION);
		if( showConfirmDialog == JOptionPane.YES_OPTION) {
			mathToolController.removeSelectedShape();
		}
		
		
	}

}
