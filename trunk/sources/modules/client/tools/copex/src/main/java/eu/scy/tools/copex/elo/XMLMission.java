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
 * definition of an initial mission :
 * initial proc,
 * materials list,
 * options
 * @author Marjolaine
 */
public class XMLMission {
    /* tag names */
    public final static String TAG_MISSION = "mission";

    /*liste des proc initiaux */
    private List<XMLInitialProc> listInitialProc;
    /*options*/
    private XMLOption missionOptions;

    public XMLMission(List<XMLInitialProc> listInitialProc,XMLOption missionOptions) {
        this.listInitialProc = listInitialProc;
        this.missionOptions = missionOptions;
    }

     public XMLMission(Element xmlElem) throws JDOMException {
        if (xmlElem.getName().equals(TAG_MISSION)) {
            listInitialProc = new LinkedList<XMLInitialProc>();
        for (Iterator<Element> variableElem = xmlElem.getChildren(XMLInitialProc.TAG_INITIAL_PROC).iterator(); variableElem.hasNext();) {
                listInitialProc.add(new XMLInitialProc(variableElem.next()));
        }
        
        missionOptions = new XMLOption(xmlElem.getChild(XMLOption.TAG_OPTION));
		} else {
			throw(new JDOMException("Mission expects <"+TAG_MISSION+"> as root element, but found <"+xmlElem.getName()+">."));
		}
	}

    public List<XMLInitialProc> getListInitialProc() {
        return listInitialProc;
    }

    

    public XMLOption getMissionOptions() {
        return missionOptions;
    }

    // toXML
    public Element toXML(){
        Element element = new Element(TAG_MISSION);
        for (Iterator<XMLInitialProc> p = listInitialProc.iterator(); p.hasNext();) {
            element.addContent(p.next().toXML());
        }
        element.addContent(missionOptions.toXML());
		return element;
    }
    
}
