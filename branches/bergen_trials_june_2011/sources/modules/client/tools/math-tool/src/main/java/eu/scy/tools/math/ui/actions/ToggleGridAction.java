package eu.scy.tools.math.ui.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JTabbedPane;

import eu.scy.tools.math.controller.MathToolController;
import eu.scy.tools.math.ui.MathTool;
import eu.scy.tools.math.ui.UIUtils;
import eu.scy.tools.math.ui.images.Images;
import eu.scy.tools.math.ui.panels.ShapeCanvas;

public class ToggleGridAction extends AbstractAction {

	private MathToolController mathController;

	public ToggleGridAction(MathToolController mathController) {
		putValue(Action.NAME, "Toggle Grid");
		putValue(Action.SMALL_ICON, Images.Grid.getIcon());
		putValue(Action.SHORT_DESCRIPTION, "Toggles the Grid on and off");
		this.mathController = mathController;
	}

	@Override
	public void actionPerformed(ActionEvent evt) {

		Component c = (Component) evt.getSource();

		MathTool mathTool = (MathTool) UIUtils.componentLookup
				.get(UIUtils.MATH_TOOL_PANEL);

		JTabbedPane tabs = (JTabbedPane) UIUtils.findComponentFromRoot(
				UIUtils.TABS, mathTool.getMainPanel());
		
		
		int selectedIndex = tabs.getSelectedIndex();
		
		ShapeCanvas shapeCanvas = null;
		if( selectedIndex == 0 ) {
			shapeCanvas  = mathController.getShapeCanvases().get(UIUtils._2D);
		} else {
			shapeCanvas  =  mathController.getShapeCanvases().get(UIUtils._3D);
		}
//		Component tabComponentAt = tabs.getSelectedComponent();
//		ShapeCanvas shapeCanvas = (ShapeCanvas) UIUtils.findComponentAt(
//				UIUtils.SHAPE_CANVAS,tabComponentAt);

		shapeCanvas.setShowGrid(!shapeCanvas.isShowGrid());
		shapeCanvas.repaint();
	}

}
