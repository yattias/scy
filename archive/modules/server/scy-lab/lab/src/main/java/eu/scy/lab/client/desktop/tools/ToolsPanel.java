package eu.scy.lab.client.desktop.tools;

import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.tree.TreePanel;

import eu.scy.lab.client.desktop.Desktop;

/**
 * Panel containing the Tree of Tools
 */
public class ToolsPanel extends Panel {

    public ToolsPanel(Desktop desktop) {
        setBorder(false);
        setPaddings(5);

        setAutoHeight(true);
        setAutoWidth(true);

        TreePanel treePanel = new ToolsTreePanel(desktop);
        treePanel.setHeader(false);
        treePanel.setBorder(false);

        add(treePanel);
    }

}