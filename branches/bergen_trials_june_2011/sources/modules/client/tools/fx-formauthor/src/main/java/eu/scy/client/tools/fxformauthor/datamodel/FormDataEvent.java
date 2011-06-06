/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.fxformauthor.datamodel;

/**
 *
 * @author pg
 */
public class FormDataEvent {
    private FormEventDataType datatype;
    private FormEventType type;
    private byte[] _Data;

    public FormDataEvent(FormEventDataType datatype, FormEventType type) {
        this.datatype = datatype;
        this.type = type;
    }

    public FormEventDataType getDataType() {
        if(datatype == null) {
           datatype = FormEventDataType.values()[0];
        }
        return this.datatype;
    }

    public void setDataType(FormEventDataType datatype) {
        this.datatype = datatype;
    }

    public FormEventType getType() {
        if(type == null) {
            type = FormEventType.values()[0];
        }
        return this.type;
    }

    public void setType(FormEventType type) {
        this.type = type;
    }

    public boolean addData(byte[] data) {
        _Data = data;
        return true;
    }

    public byte[]  getData() {
        return _Data;
    }
}
