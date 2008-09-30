/**
 * 
 */
package eu.scy.lab.client.desktop.buddies;

import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.tree.TreeNode;
import com.gwtext.client.widgets.tree.TreePanel;

/**
 * @author Sven
 *
 */
public class BuddyTree extends TreePanel {

	public BuddyTree() {
		
		setAutoScroll(true);
//		setLayout(new FitLayout());

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