package eu.scy.tools.math.ui.actions;

import java.awt.event.ActionEvent;
import java.util.logging.Logger;

import javax.swing.AbstractAction;

import eu.scy.tools.math.controller.MathToolController;

public class OpenShapesAction extends AbstractAction {

	private static Logger log = Logger.getLogger("OpenShapesAction.class"); //$NON-NLS-1$

	
	private MathToolController mathToolController;

	public OpenShapesAction(MathToolController mathToolController) {
		super("Open"); //$NON-NLS-1$
		this.mathToolController = mathToolController;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		this.mathToolController.open();
	}

}
