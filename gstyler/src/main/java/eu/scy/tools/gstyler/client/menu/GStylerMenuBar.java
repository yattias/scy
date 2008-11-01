package eu.scy.tools.gstyler.client.menu;

import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;

import eu.scy.tools.gstyler.client.graph.application.GraphApplication;

public class GStylerMenuBar extends MenuBar {

    public GStylerMenuBar(GraphApplication gstyler) {
        setAnimationEnabled(true);
        setAutoOpen(true);

        MenuBar fileMenu = new MenuBar(true);
        fileMenu.addItem(new AboutMenuItem());
      
        addItem(new MenuItem("File", fileMenu));
    }
}
