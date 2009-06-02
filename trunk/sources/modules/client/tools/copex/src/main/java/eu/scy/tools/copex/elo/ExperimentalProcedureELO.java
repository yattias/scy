/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.elo;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.jdom.Element;
import org.jdom.JDOMException;

/**
 * proc au format xml
 * @author Marjolaine
 */
public class ExperimentalProcedureELO implements Cloneable {
    /*tag name  */
    public static final String TAG_EXP_PROC = "experimental_procedure" ;
    public static final String TAG_EXP_PROC_NAME = "name" ;
    public static final String TAG_EXP_PROC_ID = "id" ;
    public static final String TAG_EXP_PROC_INIT_PROC = "initial_proc" ;
    public static final String TAG_EXP_PROC_LIST_MAT_USE = "material_use" ;

    /* nom */
    private String name;
    /* id */
    private String idProc;
    /* mission de référence */
    private XMLMission mission;
    /*id proc initial */
    private String idInitProc;
    /*protocole */
    private XMLQuestion question;
    /*feuille de données */
    private XMLDataSheet ds;
    /* materiel utilises */
    private List<XMLMaterialUse> listMaterialUse;

    public ExperimentalProcedureELO(String name, String idProc,  XMLMission mission, String idIinitProc, XMLQuestion question, XMLDataSheet ds, List<XMLMaterialUse> listMaterialUse) {
        this.name = name;
        this.idProc = idProc;
        this.mission = mission;
        this.idInitProc = idIinitProc ;
        this.question = question;
        this.ds = ds;
        this.listMaterialUse = listMaterialUse;
    }

     public ExperimentalProcedureELO(Element xmlElem) throws JDOMException {
		if (xmlElem.getName().equals(TAG_EXP_PROC)) {
			idProc = xmlElem.getChild(TAG_EXP_PROC_ID).getText();
            name = xmlElem.getChild(TAG_EXP_PROC_NAME).getText() ;
            mission = new XMLMission(xmlElem.getChild(XMLMission.TAG_MISSION));
            idInitProc = xmlElem.getChild(TAG_EXP_PROC_INIT_PROC).getText() ;
            question = new XMLQuestion(xmlElem.getChild(XMLQuestion.TAG_QUESTION));
            if (xmlElem.getChild(XMLDataSheet.TAG_DATASHEET) != null){
                ds = new XMLDataSheet(xmlElem.getChild(XMLDataSheet.TAG_DATASHEET));
            }
            if (xmlElem.getChild(TAG_EXP_PROC_LIST_MAT_USE) != null){
                listMaterialUse = new LinkedList<XMLMaterialUse>();
                for (Iterator<Element> variableElem = xmlElem.getChildren(XMLMaterialUse.TAG_MATERIAL).iterator(); variableElem.hasNext();) {
                    listMaterialUse.add(new XMLMaterialUse(variableElem.next()));
                }
            }
		} else {
			throw(new JDOMException("ExperimentalProcedureXML expects <"+TAG_EXP_PROC+"> as root element, but found <"+xmlElem.getName()+">."));
		}
	}

    
    public XMLDataSheet getDs() {
        return ds;
    }

    public String getIdProc() {
        return idProc;
    }
    
    public String getName() {
        return name;
    }

    public XMLQuestion getQuestion() {
        return question;
    }

    public String getIdInitProc() {
        return idInitProc;
    }

    public List<XMLMaterialUse> getListMaterialUse() {
        return listMaterialUse;
    }

    public XMLMission getMission() {
        return mission;
    }

   

     // toXML
    public Element toXML(){
        Element element = new Element(TAG_EXP_PROC);
        element.addContent(new Element(TAG_EXP_PROC_ID).setText(idProc));
        element.addContent(new Element(TAG_EXP_PROC_NAME).setText(name));
        element.addContent(new Element(TAG_EXP_PROC_INIT_PROC).setText(idInitProc));
        element.addContent(mission.toXML());
        element.addContent(question.toXML());
        if(ds != null){
            Element eDs = ds.toXML();
            if (eDs != null)
                element.addContent(eDs);
        }
        if(listMaterialUse != null && listMaterialUse.size() > 0){
            Element listMat = new Element(TAG_EXP_PROC_LIST_MAT_USE);
            for (Iterator<XMLMaterialUse> mat = listMaterialUse.iterator(); mat.hasNext();) {
                    listMat.addContent(mat.next().toXML());
            }
            element.addContent(listMat);
        }

		return element;
    }

}
