package eu.scy.tools.simquestviewer.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;


public class SimQuestViewerApp implements EntryPoint {

    public void onModuleLoad() {
        RootPanel.get().add(new SimQuestViewer() );
    }

}
