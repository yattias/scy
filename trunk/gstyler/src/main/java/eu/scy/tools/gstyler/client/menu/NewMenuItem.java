package eu.scy.tools.gstyler.client.menu;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

import eu.scy.tools.gstyler.client.GStyler;


public class NewMenuItem extends MenuItem {
    
    public NewMenuItem(final GStyler gstyler) {
        super("New", new Command() {

            public void execute() {
                gstyler.getGraph().clear();
            }
            
        });
    }
}
