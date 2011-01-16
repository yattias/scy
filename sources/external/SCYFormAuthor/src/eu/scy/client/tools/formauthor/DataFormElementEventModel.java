package eu.scy.client.tools.formauthor;

import java.io.Serializable;
import java.util.ArrayList;

public class DataFormElementEventModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1599129361644311847L;

	public enum DataFormElementEventDataTypes {
		GPS, TIME, DATE
	}

	public enum DataFormElementEventTypes {
		ONBEFORE, ONAFTER
	}

	private DataFormElementModel _dfem;

	private DataFormElementEventTypes _EventType;

	private DataFormElementEventDataTypes _EventDataType;

	private ArrayList<byte[]> _DataList = new ArrayList<byte[]>();

	public DataFormElementEventModel(DataFormElementModel dfem) {
		_dfem = dfem;
		setEventDataType(DataFormElementEventDataTypes.TIME);
		setEventType(DataFormElementEventTypes.ONBEFORE);
	}

	public ArrayList<byte[]> getDataList() {
		return _DataList;
	}

	public void setDataList(ArrayList<byte[]> dataList) {
		_DataList = dataList;
	}

	public DataFormElementEventTypes getEventType() {
		return _EventType;
	}

	public DataFormElementEventDataTypes getEventDataType() {
		return _EventDataType;
	}

	public void setEventDataType(DataFormElementEventDataTypes eventDataType) {
		_EventDataType = eventDataType;
	}

	public void setEventType(DataFormElementEventTypes eventType) {
		_EventType = eventType;
	}

	public boolean addStoredData(byte[] data) {
		boolean result = false;
		int pos = _DataList.size();
		if (_dfem.getCardinality().equalsIgnoreCase("0")) {
			_DataList.add(data);
			result = true;
		} else {
			int count = Integer.valueOf(_dfem.getCardinality());
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
		return result;
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
}
