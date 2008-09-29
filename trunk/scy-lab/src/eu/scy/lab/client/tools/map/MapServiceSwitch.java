package eu.scy.lab.client.tools.map;

import java.util.Collection;

import com.google.gwt.core.client.GWT;
import com.google.gwt.gears.client.Factory;
import com.google.gwt.gears.client.GearsException;
import com.google.gwt.gears.client.database.Database;
import com.google.gwt.gears.client.database.DatabaseException;
import com.google.gwt.gears.client.database.ResultSet;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class MapServiceSwitch implements MapServiceAsync {

	private MapServiceAsync mapService;
	private Database db;

	private boolean online = true;

	public static MapServiceSwitch instance;
	
	public static MapServiceSwitch getInstance() {
		if (instance == null) {
			instance = new MapServiceSwitch();
		}
		return instance;
	}
	
	private MapServiceSwitch() {
		this.mapService = (MapServiceAsync) GWT.create(MapService.class);

		if (checkForGears() == true) {
			try {
				db = Factory.getInstance().createDatabase();
				// Create the database if it doesn't exist.
				db.open("scy-tools-map");
				db.execute("create table if not exists markers (Latitude double, Longitude double, Info varchar(255) )");
			} catch (Exception e) {
				Window.alert(e.toString());
			}
		}
	}

	public boolean getOnline() {
		return online;
	}
	
	public void setOnline(boolean online) {
		this.online = online;
	}
	
	public native boolean checkForGears() /*-{
 	//FIXME: Normally should also check for google.gears, but I have no idea on how to get that object from here
		if ($wnd.google) {
			return true;
		} else {
			return false;
		}
    }-*/;

	public void addMarker(MarkerBean marker, AsyncCallback<Boolean> callback) {
		// TODO Auto-generated method stub
	}

	public void getMarkerAtPosition(double latitude, double longitude, AsyncCallback<MarkerBean> callback) {
		if (online) {
			mapService.getMarkerAtPosition(latitude, longitude, callback);
		} else {
			if (checkForGears()) {
				MarkerBean bean = new MarkerBean();
				try {
					Window.alert("getting marker info from db");
					ResultSet rs = db.execute("select info from markers where latitude = ? and longitude = ?", Double.toString(latitude), Double.toString(longitude) );
					if (rs.isValidRow()) {
						bean.setInfo(rs.getFieldAsString(0));
					} else {
						Window.alert("no valid row!");
					}
					rs.close();
					callback.onSuccess(bean);
				} catch (DatabaseException e) {
					Window.alert("Database lookup failed : " + e.getMessage());
					callback.onFailure(new Throwable("Database lookup failed : " + e.getMessage()) );
				}

			} else {
				callback.onFailure(new Throwable("No gears available."));
			}
		}
	}

	public void getMarkers(AsyncCallback<Collection<MarkerBean>> callback) {
		// TODO Auto-generated method stub
	}

	public void removeMarker(MarkerBean marker, AsyncCallback<Boolean> callback) {
		// TODO Auto-generated method stub

	}

	public void updateMarkerInfo(final MarkerBean marker, final String newInfo, AsyncCallback<Boolean> callback) {
		if (checkForGears()) {
			callback = new GearAsyncCallback<Boolean>(callback) {
				@Override
				protected void saveData(Boolean result) {
					try {
						Window.alert("Writing marker info to db: " + newInfo);
						// FIXME: Need to use update if the marker is already there
						db.execute("insert into markers values (?,?,?)", Double.toString(marker.getLatitude()), Double.toString(marker.getLongitude()), newInfo);
					} catch (GearsException e) {
						Window.alert("???" + e.getMessage());
					}
				}
			};
		} else {
			Window.alert("No gears! could not update marker info locally.");
		}
		mapService.updateMarkerInfo(marker, newInfo, callback);
	}

	public void updateMarkerPosition(MarkerBean marker, double latitude, double longitude, AsyncCallback<Boolean> callback) {
		// TODO Auto-generated method stub

	}
}
