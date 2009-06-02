/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.elo;

import org.jdom.Element;
import org.jdom.JDOMException;

/**
 * mission option
 * @author Marjolaine
 */
public class XMLOption {
    /* tag names */
    public final static String TAG_OPTION = "options";
    private final static String TAG_OPTION_USE_DATASHEET = "use_datasheet";
    private final static String TAG_OPTION_ADD_PROC = "add_proc";

    /*use dataSheet*/
    private boolean  useDataSheet;
    /*can add proc*/
    private boolean canAddProc;

    public XMLOption(boolean useDataSheet, boolean canAddProc) {
        this.useDataSheet = useDataSheet;
        this.canAddProc = canAddProc;
    }

    public XMLOption(Element xmlElem) throws JDOMException {
		if (xmlElem.getName().equals(TAG_OPTION)) {
			useDataSheet = xmlElem.getChild(TAG_OPTION_USE_DATASHEET).getText().equals("true");
            canAddProc = xmlElem.getChild(TAG_OPTION_ADD_PROC).getText().equals("true");
		} else {
			throw(new JDOMException("mission options expects <"+TAG_OPTION+"> as root element, but found <"+xmlElem.getName()+">."));
		}
	}

    public boolean canAddProc() {
        return canAddProc;
    }

    public boolean isUseDataSheet() {
        return useDataSheet;
    }

    // toXML
    public Element toXML(){
        Element element = new Element(TAG_OPTION);
		element.addContent(new Element(TAG_OPTION_USE_DATASHEET).setText(useDataSheet ? "true" : "false"));
        element.addContent(new Element(TAG_OPTION_ADD_PROC).setText(canAddProc ? "true" : "false"));
		return element;
    }


}
