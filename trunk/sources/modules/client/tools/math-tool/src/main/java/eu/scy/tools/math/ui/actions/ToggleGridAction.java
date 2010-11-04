package eu.scy.tools.math.ui.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import eu.scy.tools.math.ui.MathTool;
import eu.scy.tools.math.ui.UIUtils;
import eu.scy.tools.math.ui.images.Images;
import eu.scy.tools.math.ui.panels.ShapeCanvas;

public class ToggleGridAction extends AbstractAction {

	public ToggleGridAction() {
		putValue(Action.NAME, "Toggle Grid");
		putValue(Action.SMALL_ICON, Images.Grid.getIcon());
		putValue(Action.SHORT_DESCRIPTION, "Toggles the Grid on and off");
	}

	@Override
	public void actionPerformed(ActionEvent evt) {

		Component c = (Component) evt.getSource();

		MathTool mathTool = (MathTool) UIUtils.componentLookup
				.get(UIUtils.MATH_TOOL_PANEL);

		ShapeCanvas shapeCanvas = (ShapeCanvas) UIUtils.findComponent(
				UIUtils.SHAPE_CANVAS, mathTool.getMainPanel());

		shapeCanvas.setShowGrid(!shapeCanvas.isShowGrid());
		shapeCanvas.repaint();
	}

}
