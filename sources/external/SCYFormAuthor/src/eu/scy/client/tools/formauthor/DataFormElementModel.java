package eu.scy.client.tools.formauthor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;

public class DataFormElementModel extends Observable implements Serializable {

	// public enum CardinalityType {
	// ONE, MANY
	// }

	public enum DataFormElementTypes {
		TEXT, IMAGE, VOICE, COUNTER, NUMBER, GPS, DATE, TIME
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -6819578854197940163L;
	private String _Title;
	private DataFormElementTypes _Type;
	private String _Cardinality;
	private Boolean _selected = false;

	private ArrayList<byte[]> _DataList = new ArrayList<byte[]>();

	private ArrayList<DataFormElementEventModel> _Events = new ArrayList<DataFormElementEventModel>();

	public DataFormElementModel() {
		super();
		setChanged();
		_Cardinality="0";
	}

	public String getCardinality() {
		return _Cardinality;
	}

	public ArrayList<byte[]> getDataList() {
		return _DataList;
	}

	public ArrayList<DataFormElementEventModel> getEvents() {
		return _Events;
	}

	public String getTitle() {
		return _Title;
	}

	public DataFormElementTypes getType() {
		return _Type;
	}

	public boolean isValid() {
		if (_Title != null & _Type != null) {
			return true;
		} else
			return false;
	}

	public void setCardinality(String cardinality) {
		_Cardinality = cardinality;
	}

	public void setDataList(ArrayList<byte[]> dataList) {
		_DataList = dataList;
		setChanged();
		notifyObservers();
	}

	public void setEvents(ArrayList<DataFormElementEventModel> events) {
		_Events = events;
	}

	public void setTitle(String title) {
		_Title = title;
	}

	public void setType(DataFormElementTypes type) {
		_Type = type;
	}

	public byte[] getStoredData(int pos) {
		// TODO Auto-generated method stub
		byte[] result = null;
		if (_DataList != null) {
			if (pos <= _DataList.size() && _DataList.size() > 0) {
				if (_DataList.get(pos) != null)
					result = _DataList.get(pos);
				// setChanged();
				// notifyObservers();
			}
		}
		return result;
	}

	public void markChanged() {
		setChanged();
		// notifyObservers();
	}

	public void setStoredData(byte[] data, int pos) {
		// TODO erweitern
		if (pos == 0 && _DataList.size() == 0) {
			_DataList.add(data);
		} else
			_DataList.set(pos, data);
		setChanged();
		notifyObservers();
	}

	// public void setChanged(){
	// setChanged();
	// notifyObservers();
	//		
	// }
	public boolean addStoredData(byte[] data) {
		boolean result = false;
		int pos = _DataList.size();
		if (_Cardinality.equalsIgnoreCase("0")) {
			_DataList.add(data);
			result = true;
		} else {
			int count = Integer.valueOf(_Cardinality);
			if (pos < count) {
				_DataList.add(data);
				result = true;
			} else {
				if (pos == count) {
					_DataList.set(pos - 1, data);
					result = true;
				}
			}
		}
		if (result) {
			setChanged();
			notifyObservers();
		}
		return result;
	}

	public void addEvent() {
		getEvents().add(new DataFormElementEventModel(this));
		setChanged();
		notifyObservers();
	}

	public void removeEvent(DataFormElementEventModel event) {
		getEvents().remove(event);
		setChanged();
		notifyObservers();
	}

	public boolean isSelected() {
		return _selected;
	}

	public void setSelected(boolean b) {
		_selected = b;
		setChanged();
		notifyObservers();
	}

}
