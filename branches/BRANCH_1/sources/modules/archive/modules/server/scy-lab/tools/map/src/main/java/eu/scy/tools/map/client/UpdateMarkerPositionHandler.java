package eu.scy.tools.map.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.maps.client.event.MarkerDragEndHandler;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.user.client.rpc.AsyncCallback;


public class UpdateMarkerPositionHandler implements MarkerDragEndHandler {

    private Marker marker;
    private MarkerBean markerBean;

    public UpdateMarkerPositionHandler(Marker marker, MarkerBean markerBean) {
        this.marker = marker;
        this.markerBean = markerBean;
    }

    public void onDragEnd(MarkerDragEndEvent event) {
        AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {

            // FIXME: Error handling
            public void onFailure(Throwable caught) {
                GWT.log("Could not move marker.", caught);
            }
            public void onSuccess(Boolean result) {}

        };
        MapServiceSwitch.getInstance().updateMarkerPosition(markerBean, marker.getLatLng().getLatitude(), marker.getLatLng().getLongitude(), callback);
    }

}
