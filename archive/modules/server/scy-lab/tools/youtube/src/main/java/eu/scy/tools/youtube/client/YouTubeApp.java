package eu.scy.tools.youtube.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

public class YouTubeApp  implements EntryPoint {

    public void onModuleLoad() {
        RootPanel.get().add( new VideoInput() );
    }

}
