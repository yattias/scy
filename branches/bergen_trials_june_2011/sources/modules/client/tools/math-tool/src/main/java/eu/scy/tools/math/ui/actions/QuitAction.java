package eu.scy.tools.math.ui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import eu.scy.tools.math.ui.Messages;

public class QuitAction extends AbstractAction {

	public QuitAction() {
		super("Quit"); //$NON-NLS-1$
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		System.exit(0);
	}

}
