/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.dataProcessTool.common;

import java.util.ArrayList;
import java.util.Iterator;
import org.jdom.Element;

/**
 * element that can be copied
 * @author Marjolaine
 */
public class CopyDataset  {
    private ArrayList<Data> listData;
    private ArrayList<DataHeader> listHeader;
    private ArrayList<Integer> listRow;
    private ArrayList<DataOperation> listOperation;

    private final static String TAG_ROW = "no_row";

    public CopyDataset(ArrayList<Data> listData, ArrayList<DataHeader> listHeader, ArrayList<Integer> listRow, ArrayList<DataOperation> listOperation) {
        this.listData = listData;
        this.listHeader = listHeader;
        this.listRow = listRow;
        this.listOperation = listOperation;
    }

    public ArrayList<Data> getListData() {
        return listData;
    }

    public ArrayList<DataHeader> getListHeader() {
        return listHeader;
    }

    public ArrayList<DataOperation> getListOperation() {
        return listOperation;
    }

    public ArrayList<Integer> getListRow() {
        return listRow;
    }

    /* xml logger */
    public Element toXMLLog(){
        Element e = new Element(Dataset.TAG_DATASET);
        if(listHeader != null){
            for(Iterator<DataHeader> h = listHeader.iterator();h.hasNext();){
                DataHeader header = h.next();
                if(header == null)
                    e.addContent(new Element(DataHeader.TAG_HEADER).setText(""));
                else{
                    e.addContent(header.toXMLLog());
                }
            }
        }
        if(listRow != null){
            for(Iterator<Integer> r = listRow.iterator();r.hasNext();){
                int row = r.next();
                e.addContent(new Element(TAG_ROW).setText(""+row));
            }
        }
        if(listOperation != null){
            for(Iterator<DataOperation> o = listOperation.iterator();o.hasNext();){
                e.addContent(o.next().toXMLLog());
            }
        }
        if(listData != null){
            for(Iterator<Data> d = listData.iterator();d.hasNext();){
                Data data = d.next();
                if(data == null){
                    e.addContent(new Element(Data.TAG_DATA).setText(""));
                }else{
                    e.addContent(data.toXMLLog());
                }
            }
        }
        return e;
    }

}
