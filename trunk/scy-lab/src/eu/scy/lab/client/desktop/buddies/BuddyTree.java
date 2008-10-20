/**
 * 
 */
package eu.scy.lab.client.desktop.buddies;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.data.Node;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.MessageBoxConfig;
import com.gwtext.client.widgets.tree.TreeNode;
import com.gwtext.client.widgets.tree.TreePanel;

import eu.scy.lab.client.UserManagement;
import eu.scy.lab.client.UserManagementAsync;
import eu.scy.lab.client.usermanagement.User;

/**
 * @author Sven
 *
 */
public class BuddyTree extends TreePanel {
    
    private TreeNode root;
    private TreeNode buddies;
    
    private UserManagementAsync usermanagement;
    
    public BuddyTree() {
        setAutoScroll(true);
        
        usermanagement = UserManagement.Util.getInstance();
        root = new TreeNode("ROOT");
        buddies = new TreeNode("Buddies");
        root.appendChild(buddies);
        setRootNode(root);
        setRootVisible(false);
        
        Timer t = new Timer() {
            
            @Override
            public void run() {
                usermanagement.getBuddies(User.getInstance().getUsername(), new AsyncCallback<String[][]>() {
                    
                    public void onFailure(Throwable caught) {
                        MessageBoxConfig mbc = new MessageBoxConfig();
                        mbc.setTitle("Login Failure"); // TODO: i18n
                        mbc.setMsg("Could not get Buddies.");
                        mbc.setIconCls(MessageBox.ERROR);
                        mbc.setButtons(MessageBox.OK);
                        MessageBox.show(mbc);
                    }
                    
                    public void onSuccess(String[][] result) {
                        TreeNode userTempNode = null;
                        if (buddies.getChildNodes().length == 0) {
                            for (String[] userStrings : result) {
                                userTempNode = new TreeNode(userStrings[0]);
                                userTempNode.setExpanded(true);
                                userTempNode.setIcon("res/icons/buddyx16.png");
                                buddies.appendChild(userTempNode);
                            }
                        } else {
                            for (String[] userStrings : result) {
                                boolean found = false;
                                
                                for (Node user : buddies.getChildNodes()) {
                                    if (((TreeNode) user).getText().equals(userStrings[0])) {
                                        found = true;
                                    }
                                }
                                if (!found) {
                                    userTempNode = new TreeNode(userStrings[0]);
                                    userTempNode.setExpanded(true);
                                    userTempNode.setIcon("res/icons/buddyx16.png");
                                    buddies.appendChild(userTempNode);
                                }
                            }
                            
                            for (Node user : buddies.getChildNodes()) {
                                boolean found = false;
                                for (String[] userStrings : result) {
                                    if (((TreeNode) user).getText().equals(userStrings[0])) {
                                        found = true;
                                    }
                                }
                                if (!found) {
                                    user.remove();
                                }
                            }
                        }
                        buddies.expand();
                        buddies.setExpanded(true);
                    }
                });
            }
            
        };
        t.scheduleRepeating(3 * 1000);
    }
    
    
}