package eu.scy.tools.map.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

public class MapToolApp implements EntryPoint {

	public void onModuleLoad() {
		RootPanel.get().add(new MapTool());
	}
	
}
