package eu.scy.lab.client.tools.map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.event.MarkerClickHandler;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Handler for clicks on markers which receives the text for that marker and displays it in the maps info window
 */
public class MyMarkerClickHandler implements MarkerClickHandler {

	private MapWidget map;
	private Marker marker;

	public MyMarkerClickHandler(MapWidget map, Marker marker) {
		super();
		this.map = map;
		this.marker = marker;
	}

	public void onClick(MarkerClickEvent event) {
		VerticalPanel panel = new VerticalPanel();
		final TextArea textArea = new TextArea();

		MapServiceAsync mapService = (MapServiceAsync) GWT.create(MapService.class);
		// FIXME: Use @RemoteServiceRelativePath annotation, which does not seem to work currently
		ServiceDefTarget endpoint = (ServiceDefTarget) mapService;
		String moduleRelativeURL = GWT.getModuleBaseURL() + "mapService";
		endpoint.setServiceEntryPoint(moduleRelativeURL);

		AsyncCallback<String> callback = new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
				System.out.println("Failed to get MarkerInfo: " + caught);
			}

			public void onSuccess(String result) {
				textArea.setText(result);
				textArea.setEnabled(true);
			}
		};
		
		// FIXME: Do RPC
		// mapService.getMarkerInfo("FOOBAR", callback);

		textArea.setEnabled(false);
		textArea.setVisibleLines(5);
		panel.add(textArea);
		Button saveButton = new Button("Save");
		saveButton.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				System.out.println("SaveButton clicked");
			}

		});
		panel.add(saveButton);
		InfoWindowContent content = new InfoWindowContent(panel);
		map.getInfoWindow().open(marker, content);
	}
}
