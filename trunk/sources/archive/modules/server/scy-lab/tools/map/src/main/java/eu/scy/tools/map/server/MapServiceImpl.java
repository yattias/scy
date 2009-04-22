package eu.scy.tools.map.server;

import java.util.Collection;
import java.util.Vector;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import eu.scy.tools.map.client.MapService;
import eu.scy.tools.map.client.MarkerBean;

public class MapServiceImpl extends RemoteServiceServlet implements MapService {

    private static final long serialVersionUID = -113529265744115830L;

    Vector<MarkerBean> markers;

    public MapServiceImpl() {
        markers = new Vector<MarkerBean>();
    }

    public boolean addMarker(MarkerBean marker) {
        return markers.add(marker);
    }

    public MarkerBean getMarkerAtPosition(double latitude, double longitude) {
        for (MarkerBean marker : markers) {
            if (marker.getLatitude() == latitude && marker.getLongitude() == longitude) {
                return marker;
            }
        }
        return null;
    }

    public boolean updateMarkerInfo(MarkerBean marker, String info) {
        // FIXME: Is there a better way?
        markers.remove(marker);
        marker.setInfo(info);
        markers.add(marker);
        return true;
    }

    public boolean updateMarkerPosition(MarkerBean marker, double latitude, double longitude) {
        // FIXME: Is there a better way?
        markers.remove(marker);
        marker.setLatitude(latitude);
        marker.setLongitude(longitude);
        markers.add(marker);
        return true;
    }

    public Collection<MarkerBean> getMarkers() {
        return markers;
    }

    public boolean removeMarker(MarkerBean marker) {
        return markers.remove(marker);
    }
}
