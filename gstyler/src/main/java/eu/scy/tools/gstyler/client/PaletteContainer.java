package eu.scy.tools.gstyler.client;

import com.google.gwt.user.client.ui.TabPanel;

import eu.scy.tools.gstyler.client.graph.application.GraphApplication;
import eu.scy.tools.gstyler.client.graph.application.GraphPlugin;
import eu.scy.tools.gstyler.client.plugins.mindmap.MindmapPlugin;

/**
 * Class responsible for adding Palettes to the right part of GStylers UI.
 */
public class PaletteContainer extends TabPanel {

    public PaletteContainer(GraphApplication gstyler) {
        setSize("100%", "300px");
        addPalette(new MindmapPlugin(gstyler));
    }

    private void addPalette(GraphPlugin palette) {
        add(palette.getUI(), palette.getName());
        selectTab(getWidgetCount() - 1);
    }
}
