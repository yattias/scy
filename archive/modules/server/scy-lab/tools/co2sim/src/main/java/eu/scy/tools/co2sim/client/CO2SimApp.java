package eu.scy.tools.co2sim.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

public class CO2SimApp implements EntryPoint {

    public void onModuleLoad() {
        RootPanel.get().add( new CO2Sim() );
    }

}
