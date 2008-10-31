package eu.scy.tools.gstyler.client;

import com.google.gwt.user.client.ui.TabPanel;

import eu.scy.tools.gstyler.client.plugins.Palette;
import eu.scy.tools.gstyler.client.plugins.mindmap.MindmapPalette;

/**
 * Class responsible for adding Palettes to the right part of GStylers UI.
 */
public class PaletteContainer extends TabPanel {

    public PaletteContainer(GStyler gstyler) {
        setSize("100%", "300px");
        addPalette(new MindmapPalette(gstyler));
    }

    private void addPalette(Palette palette) {
        add(palette, palette.getName());
        selectTab(getWidgetCount() - 1);
    }
}
