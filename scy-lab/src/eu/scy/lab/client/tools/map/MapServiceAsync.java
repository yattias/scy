package eu.scy.lab.client.tools.map;

import java.util.Collection;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MapServiceAsync {
	public void addMarker(MarkerBean marker, AsyncCallback<Boolean> callback);
	public void removeMarker(MarkerBean marker, AsyncCallback<Boolean> callback);
	public void getMarkers(AsyncCallback<Collection<MarkerBean>> callback);
	public void getMarkerAtPosition(double latitude, double longitude, AsyncCallback<MarkerBean> callback);
	public void updateMarkerPosition(MarkerBean marker, double latitude, double longitude, AsyncCallback<?> callback);
	public void updateMarkerInfo(MarkerBean marker, String info, AsyncCallback<?> callback);
}
