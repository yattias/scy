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
 * initial procedure
 * @author Marjolaine
 */
public class XMLInitialProc {
    /* tag names */
    public final static String TAG_INITIAL_PROC = "initial_proc";
    private final static String TAG_INITIAL_PROC_ID = "id_proc";
    private final static String TAG_INITIAL_PROC_NAME = "proc_name";
    private final static String TAG_INITIAL_PROC_CODE = "proc_code";

    /*id proc */
    private String idProc;
    /* proc name */
    private String procName;
    /* proc code */
    private String procCode;
    /* question */
    private XMLQuestion question;
    /*type d'actions */
    private XMLActionType actionType;
     /* liste des material disponible */
    private List<XMLMaterial> listMaterialAvailable;

    public XMLInitialProc(String idProc, String procName,String procCode,XMLQuestion question, XMLActionType actionType, List<XMLMaterial> listMaterialAvailable) {
        this.idProc = idProc;
        this.procName = procName;
        this.procCode = procCode ;
        this.question = question;
        this.actionType = actionType;
        this.listMaterialAvailable = listMaterialAvailable;
    }

     public XMLInitialProc(Element xmlElem) throws JDOMException {
		if (xmlElem.getName().equals(TAG_INITIAL_PROC)) {
			idProc = xmlElem.getChild(TAG_INITIAL_PROC_ID).getText();
            procName = xmlElem.getChild(TAG_INITIAL_PROC_NAME).getText();
            procCode = xmlElem.getChild(TAG_INITIAL_PROC_CODE).getText();
            question = new XMLQuestion(xmlElem.getChild(XMLQuestion.TAG_QUESTION));
            actionType = new XMLActionType(xmlElem.getChild(XMLActionType.TAG_ACTION_TYPE));
            if (xmlElem.getChild(XMLMaterial.TAG_MATERIAL) != null){
                listMaterialAvailable = new LinkedList<XMLMaterial>();
                for (Iterator<Element> variablElem = xmlElem.getChildren(XMLMaterial.TAG_MATERIAL).iterator(); variablElem.hasNext();) {
                    listMaterialAvailable.add(new XMLMaterial(variablElem.next()));
                }
            }
		} else {
			throw(new JDOMException("Initial procedure expects <"+TAG_INITIAL_PROC+"> as root element, but found <"+xmlElem.getName()+">."));
		}
	}

    public XMLActionType getActionType() {
        return actionType;
    }

    public String getIdProc() {
        return idProc;
    }

    public String getProcName() {
        return procName;
    }

    public XMLQuestion getQuestion() {
        return question;
    }

    public String getProcCode() {
        return procCode;
    }

    public List<XMLMaterial> getListMaterialAvailable() {
        return listMaterialAvailable;
    }

    // toXML
    public Element toXML(){
        Element element = new Element(TAG_INITIAL_PROC);
		element.addContent(new Element(TAG_INITIAL_PROC_ID).setText(idProc));
        element.addContent(new Element(TAG_INITIAL_PROC_NAME).setText(procName));
        element.addContent(new Element(TAG_INITIAL_PROC_CODE).setText(procCode));
        element.addContent(question.toXML());
        element.addContent(actionType.toXML());
        if (listMaterialAvailable != null){
            for (Iterator<XMLMaterial> m = listMaterialAvailable.iterator(); m.hasNext();) {
                element.addContent(m.next().toXML());
            }
        }
		return element;
    }

}
