package eu.scy.tools.map.client;

import java.util.Collection;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * RPC interface for the MapTool
 */
@RemoteServiceRelativePath("/mapService")
public interface MapService extends RemoteService {
	public boolean addMarker(MarkerBean marker);
	public boolean removeMarker(MarkerBean marker);
	public Collection<MarkerBean> getMarkers();
	public MarkerBean getMarkerAtPosition(double latitude, double longitude);
	public boolean updateMarkerPosition(MarkerBean marker, double newLatitude, double newLongitude);
	public boolean updateMarkerInfo(MarkerBean marker, String newInfo);
}
