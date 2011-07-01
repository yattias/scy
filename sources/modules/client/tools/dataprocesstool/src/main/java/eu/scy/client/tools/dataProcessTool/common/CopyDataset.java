/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.dataProcessTool.common;

import java.util.ArrayList;
import java.util.Iterator;
import org.jdom.Element;
import org.jdom.JDOMException;

/**
 * element that can be copied
 * @author Marjolaine
 */
public class CopyDataset  {
    public final static String TAG_COPY_DATASET = "copy_dataset";
    private final static String TAG_COPY_DATASET_ROW = "copy_dataset_row";

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

    public CopyDataset(Element xmlElem) throws JDOMException {
        if (xmlElem.getName().equals(TAG_COPY_DATASET)) {
            this.listHeader = new ArrayList();
            for (Iterator<Element> variableElem = xmlElem.getChildren(DataHeader.TAG_HEADER).iterator(); variableElem.hasNext();) {
                listHeader.add(new DataHeader(variableElem.next()));
            }
            this.listData = new ArrayList();
            for (Iterator<Element> variableElem = xmlElem.getChildren(Data.TAG_DATA).iterator(); variableElem.hasNext();) {
                listData.add(new Data(variableElem.next()));
            }
            this.listOperation = new ArrayList();
            for (Iterator<Element> variableElem = xmlElem.getChildren(DataOperation.TAG_OPERATION).iterator(); variableElem.hasNext();) {
                listOperation.add(new DataOperation(variableElem.next()));
            }
            this.listRow = new ArrayList();
            for (Iterator<Element> variableElem = xmlElem.getChildren(TAG_COPY_DATASET_ROW).iterator(); variableElem.hasNext();) {
                int r = Integer.parseInt(variableElem.next().getText());
                listRow.add(r);
            }
        }else {
            throw(new JDOMException("CopyDataset expects <"+TAG_COPY_DATASET+"> as root element, but found <"+xmlElem.getName()+">."));
	}
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
                if(header != null){
                    e.addContent(header.toXMLLog());
                }
            }
        }
        if(listRow != null){
            for(Iterator<Integer> r = listRow.iterator();r.hasNext();){
                int row = r.next();
                e.addContent(new Element(TAG_COPY_DATASET_ROW).setText(Integer.toString(row)));
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
                if(data != null){
                    e.addContent(data.toXMLLog());
                }
            }
        }
        return e;
    }

}
