package eu.scy.tools.gstyler.client;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.TabPanel;

import eu.scy.tools.gstyler.client.graph.application.GraphApplication;
import eu.scy.tools.gstyler.client.graph.application.GraphPlugin;
import eu.scy.tools.gstyler.client.plugins.mindmap.MindmapPlugin;
import eu.scy.tools.gstyler.client.plugins.qoc.QOCPlugin;

/**
 * Class responsible for adding Palettes to the right part of GStylers UI.
 */
public class PaletteContainer extends TabPanel {

    public ArrayList<GraphPlugin> plugins;

    public PaletteContainer(GraphApplication gstyler) {
        setSize("100%", "300px");
        plugins = new ArrayList<GraphPlugin>();

        addPlugin(new MindmapPlugin(gstyler));
        addPlugin(new QOCPlugin(gstyler));

        // FIXME: For debugging
        new QOCPlugin(gstyler).addExampleDocument(gstyler.getGraph());
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
