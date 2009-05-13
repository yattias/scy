package eu.scy.lab.client.desktop.buddies;

import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.ToolbarMenuButton;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.tree.TreePanel;

public class Buddies {

	Panel panel;

	public Buddies() {
		panel = new Panel();
		panel.setBorder(false);
		panel.setPaddings(5);
		panel.setAutoScroll(true);
		panel.setAutoHeight(true);
		panel.setAutoWidth(true);
		panel.setLayout(new FitLayout());
		
		final TreePanel treePanel = new BuddyTree();
		treePanel.setHeader(false);
		treePanel.setBorder(false);
		treePanel.setAutoHeight(true);
		treePanel.setAutoWidth(true);
	
		//XXX
		treePanel.setAutoScroll(true);

		panel.add(treePanel);

	}

	public Panel getPanel() {
		return this.panel;
	}
	
	public Toolbar createToolbar(){
		Toolbar toolbar = new Toolbar();

		ToolbarMenuButton options = new ToolbarMenuButton("Options");
		options.setIcon("");
		toolbar.addButton(options);
		
		ToolbarButton addBuddy = new ToolbarButton();
		addBuddy.setIcon("res/icons/Chat2x16.png");
		addBuddy.setTitle("add a new Buddy to contact list");
		toolbar.addButton(addBuddy);
		
		return toolbar;
	}

	
}