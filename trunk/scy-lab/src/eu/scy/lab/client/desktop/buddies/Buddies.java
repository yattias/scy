package eu.scy.lab.client.desktop.buddies;

import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.ToolbarMenuButton;
import com.gwtext.client.widgets.tree.TreeNode;
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
		panel.setTopToolbar(createToolbar());

		final TreePanel treePanel = new BuddyTree();
		treePanel.setHeader(false);
		treePanel.setBorder(false);
		treePanel.setAutoHeight(true);
		treePanel.setAutoWidth(true);

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
		addBuddy.setIcon("");
		addBuddy.setTitle("add a new Buddy to contact list");
		toolbar.addButton(addBuddy);
		
		return toolbar;
	}

	class BuddyTree extends TreePanel {

		public BuddyTree() {

			TreeNode root = new TreeNode("Buddies");

			TreeNode friends = new TreeNode("Friends");
			friends.setExpanded(true);

			TreeNode klaus = new TreeNode("Klaus");
			klaus.setExpanded(true);

			TreeNode steffie = new TreeNode("steffie");
			steffie.setExpanded(true);

			TreeNode sam = new TreeNode("Sam");
			sam.setExpanded(true);

			friends.appendChild(klaus);
			friends.appendChild(steffie);
			friends.appendChild(sam);

			TreeNode coWorkers = new TreeNode("Co-Workers");
			coWorkers.setExpanded(true);

			TreeNode adam = new TreeNode("Adam");
			adam.setExpanded(true);

			TreeNode sven = new TreeNode("Sven");
			sven.setExpanded(true);

			TreeNode stefan = new TreeNode("Stefan");
			stefan.setExpanded(true);

			coWorkers.appendChild(adam);
			coWorkers.appendChild(sven);
			coWorkers.appendChild(stefan);

			root.appendChild(friends);
			root.appendChild(coWorkers);

			setRootVisible(false);

			setRootNode(root);
			root.setExpanded(true);
		}
	}
}