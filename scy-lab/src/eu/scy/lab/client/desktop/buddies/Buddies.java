package eu.scy.lab.client.desktop.buddies;

import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.tree.TreeNode;
import com.gwtext.client.widgets.tree.TreePanel;

public class Buddies {

	Panel panel;

	public Buddies() {
		panel = new Panel("Buddies");
		panel.setBorder(false);
		panel.setPaddings(15);
		panel.setCollapsible(true);

		final TreePanel treePanel = new BuddyTree();
		treePanel.setHeader(false);
		// treePanel.setTitle("Buddies");
		treePanel.setCollapsible(true);
		treePanel.setWidth(190);
		treePanel.setHeight(150);

		panel.add(treePanel);
		panel.collapse();

	}

	public Panel getPanel() {
		return this.panel;
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

			setTitle("Buddies");
			setWidth(200);
			setHeight(400);
			setRootNode(root);
			root.setExpanded(true);
		}
	}
}