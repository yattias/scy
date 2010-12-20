package eu.scy.tools.math.ui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;

import eu.scy.tools.math.ui.UIUtils;

public class NotationHelpAction extends AbstractAction {

	public NotationHelpAction() {
		putValue(Action.NAME, "Notation Guide");
		putValue(Action.SHORT_DESCRIPTION, "Notation guide for math expressions.");
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		JOptionPane.showMessageDialog(null, UIUtils.notationHelpMessage, "Math Expression Notation", JOptionPane.INFORMATION_MESSAGE, null);
	}

}
