package eu.scy.tools.map.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.event.MarkerClickHandler;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Handler for clicks on markers which receives the text for that marker and displays it in the map's info window
 */
public class ShowMarkerInfoHandler implements MarkerClickHandler {

	private MapWidget map;
	private Marker marker;

	public ShowMarkerInfoHandler(MapWidget map, Marker marker) {
		super();
		this.map = map;
		this.marker = marker;
	}

	public void onClick(MarkerClickEvent event) {
		VerticalPanel panel = new VerticalPanel();
		final TextArea textArea = new TextArea();
		textArea.setEnabled(false);
		textArea.setVisibleLines(5);
		panel.add(textArea);
		
		HorizontalPanel buttonPanel = new HorizontalPanel();
		panel.add(buttonPanel);

		final Button saveButton = new Button("Save");
		saveButton.setEnabled(false);
		buttonPanel.add(saveButton);
		
		final Button removeButton = new Button("Remove");
		removeButton.setEnabled(false);
		buttonPanel.add(removeButton);
		
		final AsyncCallback<MarkerBean> callback = new AsyncCallback<MarkerBean>() {
			public void onFailure(Throwable caught) {
				GWT.log("Failed to get MarkerBean.", caught);
			}

			public void onSuccess(MarkerBean result) {
				GWT.log("Received MarkerBean.", null);
				final MarkerBean markerBean = result;
				
				textArea.setText(result.getInfo());
				textArea.setEnabled(true);
				
				// Enable the SaveButton
				saveButton.setEnabled(true);
				saveButton.addClickListener( new ClickListener() {

					public void onClick(Widget sender) {
						AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
							// FIXME: Error handling
							public void onFailure(Throwable caught) { GWT.log("Failed to update MarkerBean.", caught); }
							public void onSuccess(Boolean result) { GWT.log("Updated MarkerBean.", null); }
						};
						MapServiceSwitch.getInstance().updateMarkerInfo(markerBean, textArea.getText(), callback);
						map.getInfoWindow().close();
					}
				});
				
				// Enable the RemoveButton
				removeButton.setEnabled(true);
				removeButton.addClickListener( new ClickListener() {

					public void onClick(Widget sender) {
						map.removeOverlay(marker);
						AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
							// FIXME: Error handling
							public void onFailure(Throwable caught) { GWT.log("Failed to remove MarkerBean.", caught); }
							public void onSuccess(Boolean result) { GWT.log("Removed MarkerBean.", null); }
						};
						MapServiceSwitch.getInstance().removeMarker(markerBean, callback);
						map.getInfoWindow().close();
					}
					
				});
			}
		};
		
		//mapService.getMarkerAtPosition(marker.getLatLng().getLatitude(), marker.getLatLng().getLongitude(), callback);
		MapServiceSwitch mss = MapServiceSwitch.getInstance();
		mss.getMarkerAtPosition(marker.getLatLng().getLatitude(), marker.getLatLng().getLongitude(), callback);
		
		InfoWindowContent content = new InfoWindowContent(panel);
		map.getInfoWindow().open(marker, content);
	}
}
