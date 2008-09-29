package eu.scy.lab.client.tools.map;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

public class MapToolDemo implements EntryPoint {

	public void onModuleLoad() {
		RootPanel.get().add(new MapTool());
	}
	
}
