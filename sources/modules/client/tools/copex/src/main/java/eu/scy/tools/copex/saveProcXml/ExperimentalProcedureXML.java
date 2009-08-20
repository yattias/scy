/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.saveProcXml;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.jdom.Element;
import org.jdom.JDOMException;

/**
 * proc au format xml
 * @author Marjolaine
 */
public class ExperimentalProcedureXML {
    /*tag name  */
    public static final String TAG_EXP_PROC = "experimental_procedure" ;
    public static final String TAG_EXP_PROC_NAME = "name" ;
    public static final String TAG_EXP_PROC_ID = "id" ;
    public static final String TAG_EXP_PROC_LIST_MAT_USE = "material_use" ;

    /* nom */
    private String name;
    /* id */
    private String idProc;
    /*protocole */
    private Procedure proc;
    /*feuille de donnees */
    private DataSheet ds;
    /* materiel utilises */
    private List<MaterialUse> listMaterialUse;

    public ExperimentalProcedureXML(String name, String idProc, Procedure proc, DataSheet ds, List listMaterialUse) {
        this.name = name;
        this.idProc = idProc;
        this.proc = proc;
        this.ds = ds;
        this.listMaterialUse = listMaterialUse;
    }

     public ExperimentalProcedureXML(Element xmlElem) throws JDOMException {
		if (xmlElem.getName().equals(TAG_EXP_PROC)) {
			idProc = xmlElem.getChild(TAG_EXP_PROC_ID).getText();
            name = xmlElem.getChild(TAG_EXP_PROC_NAME).getText() ;
            proc = new Procedure(xmlElem.getChild(Procedure.TAG_PROCEDURE));
            if (xmlElem.getChild(DataSheet.TAG_DATASHEET) != null){
                ds = new DataSheet(xmlElem.getChild(DataSheet.TAG_DATASHEET));
            }
            if (xmlElem.getChild(TAG_EXP_PROC_LIST_MAT_USE) != null){
                listMaterialUse = new LinkedList<MaterialUse>();
                for (Iterator<Element> variableElem = xmlElem.getChildren(MaterialUse.TAG_MATERIAL).iterator(); variableElem.hasNext();) {
                    listMaterialUse.add(new MaterialUse(variableElem.next()));
                }
            }
		} else {
			throw(new JDOMException("ExperimentalProcedureXML expects <"+TAG_EXP_PROC+"> as root element, but found <"+xmlElem.getName()+">."));
		}
	}


    public DataSheet getDs() {
        return ds;
    }

    public String getIdProc() {
        return idProc;
    }

    public List getListMaterialUse() {
        return listMaterialUse;
    }

    public String getName() {
        return name;
    }

    public Procedure getProc() {
        return proc;
    }

     // toXML
    public Element toXML(){
        Element element = new Element(TAG_EXP_PROC);
        element.addContent(new Element(TAG_EXP_PROC_ID).setText(idProc));
        element.addContent(new Element(TAG_EXP_PROC_NAME).setText(name));
        element.addContent(proc.toXML());
        if(ds != null){
            Element eDs = ds.toXML();
            if (eDs != null)
                element.addContent(eDs);
        }
        if(listMaterialUse != null && listMaterialUse.size() > 0){
            Element listMat = new Element(TAG_EXP_PROC_LIST_MAT_USE);
            for (Iterator<MaterialUse> mat = listMaterialUse.iterator(); mat.hasNext();) {
                    listMat.addContent(mat.next().toXML());
            }
            element.addContent(listMat);
        }

		return element;
    }

}
