package eu.scy.tools.map.client;

import java.util.Collection;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * RPC interface for the MapTool
 */
public interface MapServiceAsync {
	public void addMarker(MarkerBean marker, AsyncCallback<Boolean> callback);
	public void removeMarker(MarkerBean marker, AsyncCallback<Boolean> callback);
	public void getMarkers(AsyncCallback<Collection<MarkerBean>> callback);
	public void getMarkerAtPosition(double latitude, double longitude, AsyncCallback<MarkerBean> callback);
	public void updateMarkerPosition(MarkerBean marker, double newLatitude, double newLongitude, AsyncCallback<Boolean> callback);
	public void updateMarkerInfo(MarkerBean marker, String newInfo, AsyncCallback<Boolean> callback);
}
