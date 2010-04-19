package eu.scy.tools.drawing.client;

import com.google.gwt.user.client.ui.RootPanel;


public class DrawingToolApp {

    public void onModuleLoad() {
        RootPanel.get().add( new DrawingTool() );
    }

}
