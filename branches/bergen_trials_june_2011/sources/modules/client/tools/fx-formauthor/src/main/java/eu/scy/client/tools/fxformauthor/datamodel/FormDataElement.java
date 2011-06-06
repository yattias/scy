/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.fxformauthor.datamodel;

import java.util.ArrayList;

/**
 *
 * @author pg
 */
public class FormDataElement {
    private String title;
    private FormElementDataType type;
    private int _Cardinality;
    private ArrayList<FormDataEvent> events;
    private ArrayList<byte[]> _DataList;

    public FormDataElement(String title, FormElementDataType type, int card) {
        events = new ArrayList<FormDataEvent>();
        _DataList = new ArrayList<byte[]>();
        this.title = title;
        this.type = type;
        this._Cardinality = card;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public FormElementDataType getType() {
        return type;
    }

    public void setType(FormElementDataType type) {
        this.type = type;
    }

    public int getCardinality() {
        return _Cardinality;
    }

    public int getUsedCardinality() {
        return _DataList.size();
    }

    public void setCardinality(int cardinality) {
        this._Cardinality = cardinality;
    }

    public ArrayList<FormDataEvent> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<FormDataEvent> newEvents) {
        events = newEvents;
    }

    public void deleteEvent(FormDataEvent event) {
        events.remove(event);
    }

    public void addEvent(FormDataEvent event) {
        events.add(event);
    }
    
    public boolean addData(byte[] data) {
        boolean result = false;
        int pos = _DataList.size();
        if (_Cardinality == 0) {
            _DataList.add(data);
            result = true;
        }
        else {
            int count = Integer.valueOf(_Cardinality);
            if (pos < count) {
                _DataList.add(data);
                result = true;
            }
            else {
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
                    }
            }
            return result;
    }

    public ArrayList<byte[]> getStoredDataList() {
        return _DataList;
    }



}
