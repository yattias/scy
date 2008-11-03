package eu.scy.tools.gstyler.client;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.TabPanel;

import eu.scy.tools.gstyler.client.graph.application.GraphApplication;
import eu.scy.tools.gstyler.client.graph.application.GraphPlugin;
import eu.scy.tools.gstyler.client.plugins.mindmap.MindmapPlugin;
import eu.scy.tools.gstyler.client.plugins.qoc.QOCPlugin;

/**
 * Class responsible for managing plugins and adding their UI.
 * 
 * FIXME: Need to differentiate between Plugin and Palette?
 */
public class PluginManager extends TabPanel {

    public ArrayList<GraphPlugin> plugins;

    public PluginManager(GraphApplication gstyler) {
        setSize("100%", "300px");
        plugins = new ArrayList<GraphPlugin>();

        addPlugin(new MindmapPlugin(gstyler));
        addPlugin(new QOCPlugin(gstyler));
    }

    private void addPlugin(GraphPlugin plugin) {
        plugins.add(plugin);
        add(plugin.getUI(), plugin.getName());
        selectTab(getWidgetCount() - 1);
    }

    public GraphPlugin getActivePlugin() {
        return plugins.get(getTabBar().getSelectedTab());
    }
}
