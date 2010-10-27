package eu.scy.tools.math.ui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import eu.scy.tools.math.ui.images.Images;

public class ExportToGoogleSketchUp extends AbstractAction {

	public ExportToGoogleSketchUp() {
		putValue(Action.SMALL_ICON, Images.SketchUp.getIcon());
		putValue(Action.SHORT_DESCRIPTION, "Export to Google Sketchup");
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

}
