package eu.scy.tools.map.client;

import com.google.gwt.user.client.rpc.IsSerializable;

public class MarkerBean implements IsSerializable {
	
	private double latitude;
	private double longitude;
	private String info;
	private String uuid;
	
	public MarkerBean() {
		this(0, 0, "DUMMY");
	}
	
	public MarkerBean(double latitude, double longitude) {
		this(latitude, longitude, "");
	}
	
	public MarkerBean(double latitude, double longitude, String info) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
		this.info = info;
		this.uuid = Long.toString( System.currentTimeMillis() );
	}
	
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getUuid() {
		return uuid;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MarkerBean other = (MarkerBean) obj;
		if (info == null) {
			if (other.info != null)
				return false;
		} else if (!info.equals(other.info))
			return false;
		if (latitude != other.latitude)
			return false;
		if (longitude != other.longitude)
			return false;
		if (uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!uuid.equals(other.uuid))
			return false;
		return true;
	}
}
