package eu.scy.tools.math.ui.actions;

import java.awt.event.ActionEvent;
import java.util.logging.Logger;

import javax.swing.AbstractAction;

import eu.scy.tools.math.controller.MathToolController;

public class SaveShapesAction extends AbstractAction {

	private static Logger log = Logger.getLogger("SaveShapesAction.class"); //$NON-NLS-1$

	
	private MathToolController mathToolController;

	public SaveShapesAction(MathToolController mathToolController) {
		super("Save"); //$NON-NLS-1$
		this.mathToolController = mathToolController;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		this.mathToolController.save();
	}

}
