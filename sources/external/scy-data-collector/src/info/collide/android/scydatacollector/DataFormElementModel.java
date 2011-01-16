package info.collide.android.scydatacollector;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;

public class DataFormElementModel extends Observable implements Serializable {

    public enum DataFormElementTypes {
        TEXT,
        IMAGE,
        VOICE,
        COUNTER,
        NUMBER,
        GPS,
        DATE,
        TIME
    }

    private static final long serialVersionUID = -6819578854197940163L;

    private String title;

    private DataFormElementTypes type;

    private String cardinality;

    private ArrayList<byte[]> dataList = new ArrayList<byte[]>();

    private ArrayList<DataFormElementEventModel> events;

    public DataFormElementModel(DataCollectorFormModel dcfm) {
        this.addObserver(dcfm);
        setChanged();
    }

    public String getCardinality() {
        return cardinality;
    }

    public ArrayList<byte[]> getDataList() {
        return dataList;
    }

    public ArrayList<DataFormElementEventModel> getEvents() {
        return events;
    }

    public String getTitle() {
        return title;
    }

    public DataFormElementTypes getType() {
        return type;
    }

    public boolean isValid() {
        if (title != null & type != null) {
            return true;
        } else
            return false;
    }

    public void setCardinality(String cardinality) {
        this.cardinality = cardinality;
    }

    public void setDataList(ArrayList<byte[]> dataList) {
        this.dataList = dataList;
        setChanged();
        notifyObservers();
    }

    public void setEvents(ArrayList<DataFormElementEventModel> events) {
        this.events = events;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(DataFormElementTypes type) {
        this.type = type;
    }

    public byte[] getStoredData(int pos) {
        byte[] result = null;
        if (dataList != null) {
            if (pos <= dataList.size() && dataList.size() > 0) {
                if (dataList.get(pos) != null)
                    result = dataList.get(pos);
            }
        }
        return result;
    }

    public void markChanged() {
        setChanged();
    }

    public void setStoredData(byte[] data, int pos) {
        if (pos == 0 && dataList.size() == 0) {
            dataList.add(data);
        } else
            dataList.set(pos, data);
        setChanged();
        notifyObservers();
    }

    public boolean addStoredData(byte[] data) {
        boolean result = false;
        int pos = dataList.size();
        if (cardinality.equalsIgnoreCase("0")) {
            dataList.add(data);
            result = true;
        } else {
            int count = Integer.valueOf(cardinality);
            if (pos < count) {
                dataList.add(data);
                result = true;
            } else {
                if (pos == count) {
                    dataList.set(pos - 1, data);
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

}
