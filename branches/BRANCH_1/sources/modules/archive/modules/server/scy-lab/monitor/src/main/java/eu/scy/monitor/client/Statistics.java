package eu.scy.monitor.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

public class Statistics implements EntryPoint {

    public void onModuleLoad() {
        RootPanel.get().add(new StatisticsView());
    }

}
