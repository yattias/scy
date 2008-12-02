package eu.scy.lab.client.desktop.tools;

import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.Node;
import com.gwtext.client.widgets.tree.TreeNode;
import com.gwtext.client.widgets.tree.event.TreeNodeListenerAdapter;

import eu.scy.lab.client.desktop.Desktop;

/**
 * Single Node in the Tree of Tools representing a tool which is started on double click.
 */
class ToolsTreeNode extends TreeNode {

    public ToolsTreeNode(String name, final String toolID, final Desktop desktop) {
       super(name);
       addListener(new TreeNodeListenerAdapter() {
           
           public void onDblClick(Node node, EventObject e) {
               if (!desktop.getWorkspace().containsComponentID(toolID)) {
                   desktop.getWorkspace().add( createTool() );
               }
               desktop.getWorkspace().activate(toolID);
           }
       });
    }

    /**
     * Overwrite this method to return a new instance of the Tool
     */
    protected Widget createTool() {
        return null;
    }
}