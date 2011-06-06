package eu.scy.tools.math.ui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;

import eu.scy.tools.math.ui.UIUtils;

public class FormulaHelpAction extends AbstractAction {

	private String type;

	public FormulaHelpAction(String type) {
		putValue(Action.NAME, type + " Formula Guide");
		putValue(Action.SHORT_DESCRIPTION, "Guide for " + type + " forumlas");
		this.type = type;
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		
		
		String title = type + " Formula Guide";
		String info; 
			if( type.equals(UIUtils._2D))
				info = UIUtils.notation2DHelpMessage;
			else
				info = UIUtils.notation3DHelpMessage;	
		
		UIUtils.showInformation(info, title);
		
		
		
	}

}
