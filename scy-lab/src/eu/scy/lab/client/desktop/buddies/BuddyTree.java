/**
 * 
 */
package eu.scy.lab.client.desktop.buddies;

import com.gwtext.client.widgets.tree.TreeNode;
import com.gwtext.client.widgets.tree.TreePanel;

/**
 * @author Sven
 *
 */
public class BuddyTree extends TreePanel {

	public BuddyTree() {
		
		setAutoScroll(true);

		TreeNode root = new TreeNode("Buddies");

		TreeNode friends = new TreeNode("Friends");
		friends.setExpanded(true);

		TreeNode klaus = new TreeNode("Klaus");
		klaus.setExpanded(true);
		klaus.setIcon("res/icons/buddyx16.png");

		TreeNode steffie = new TreeNode("steffie");
		steffie.setExpanded(true);
		steffie.setIcon("res/icons/buddyx16.png");

		TreeNode sam = new TreeNode("Sam");
		sam.setExpanded(true);
		sam.setIcon("res/icons/buddyx16.png");

		friends.appendChild(klaus);
		friends.appendChild(steffie);
		friends.appendChild(sam);

		TreeNode coWorkers = new TreeNode("Co-Workers");
		coWorkers.setExpanded(true);

		TreeNode adam = new TreeNode("Adam");
		adam.setExpanded(true);
		adam.setIcon("res/icons/buddyx16.png");

		TreeNode sven = new TreeNode("Sven");
		sven.setExpanded(true);
		sven.setIcon("res/icons/buddyx16.png");

		TreeNode stefan = new TreeNode("Stefan");
		stefan.setExpanded(true);
		stefan.setIcon("res/icons/buddyx16.png");

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