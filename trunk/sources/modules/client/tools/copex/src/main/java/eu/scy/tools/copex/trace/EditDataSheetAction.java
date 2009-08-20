/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.trace;

import java.util.ArrayList;

/**
 * action de renseigner des cases d'un tableau
 * @author MBO
 */
public class EditDataSheetAction extends TraceAction{

    // ATTRIBUTS
    /*liste des donnees */
    private ArrayList<DataXML> listData;

    // CONSTRUCTEURS
    public EditDataSheetAction(long dbKeyProc, String procName, ArrayList<DataXML> listData) {
        super("EDIT_DATASHEET", dbKeyProc, procName);
        this.listData = listData;
    }
    
    // OVERRIDE
    @Override
    public String getParameterToXML() {
        String xml =  super.getParameterToXML();
        int nbD = listData.size();
        for (int i=0; i<nbD; i++){
            xml += getDataToXML(listData.get(i));
        }
        return xml;
    }

    @Override
    protected void initParameter() {
        super.initParameter();
        addParameters();
    }
    
    public void addParameters(){
        if (listData == null)
            return;
        int nbD = listData.size();
        for (int i=0; i<nbD; i++){
            listData.get(i).addParameters(listParameter);
        }
    }
    
     /* data a  copier */
    private String getDataToXML(DataXML data){
        String xml = data.getDbKeyToXML();
        xml += data.getNoRowToXML();
        xml += data.getNoColToXML();
        xml += data.getDataToXML();
        return xml;
    }
}
