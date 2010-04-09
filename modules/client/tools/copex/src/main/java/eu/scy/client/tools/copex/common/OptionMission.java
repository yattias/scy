/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.common;

import eu.scy.client.tools.copex.utilities.MyConstants;
import org.jdom.Element;
import org.jdom.JDOMException;

/**
 * options for a mission
 * @author Marjolaine
 */
public class OptionMission implements Cloneable {

    /* tag names */
    public final static String TAG_OPTION = "options";
    private final static String TAG_OPTION_ADD_PROC = "add_proc";
    private final static String TAG_OPTION_TRACE = "trace";
    
    private boolean canAddProc;
    private boolean trace;

    public OptionMission(boolean canAddProc, boolean trace) {
        this.canAddProc = canAddProc;
        this.trace = trace;
    }

    public OptionMission(Element xmlElem) throws JDOMException {
		if (xmlElem.getName().equals(TAG_OPTION)) {
            canAddProc = xmlElem.getChild(TAG_OPTION_ADD_PROC).getText().equals(MyConstants.XML_BOOLEAN_TRUE);
            trace = xmlElem.getChild(TAG_OPTION_TRACE).getText().equals(MyConstants.XML_BOOLEAN_TRUE);
		} else {
			throw(new JDOMException("mission options expects <"+TAG_OPTION+"> as root element, but found <"+xmlElem.getName()+">."));
		}
	}


    /**
     * Get the value of trace
     *
     * @return the value of trace
     */
    public boolean isTrace() {
        return trace;
    }

    /**
     * Set the value of trace
     *
     * @param trace new value of trace
     */
    public void setTrace(boolean trace) {
        this.trace = trace;
    }


   

    /**
     * Get the value of canAddProc
     *
     * @return the value of canAddProc
     */
    public boolean isCanAddProc() {
        return canAddProc;
    }

    /**
     * Set the value of canAddProc
     *
     * @param canAddProc new value of canAddProc
     */
    public void setCanAddProc(boolean canAddProc) {
        this.canAddProc = canAddProc;
    }

     @Override
    public Object clone()  {
        try {
            OptionMission o = (OptionMission) super.clone() ;
            o.setCanAddProc(new Boolean(this.canAddProc));
            o.setTrace(new Boolean(this.trace));
            return o;
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError();
        }
    }

     // toXML
    public Element toXML(){
        Element element = new Element(TAG_OPTION);
        element.addContent(new Element(TAG_OPTION_ADD_PROC).setText(canAddProc ? MyConstants.XML_BOOLEAN_TRUE : MyConstants.XML_BOOLEAN_FALSE));
        element.addContent(new Element(TAG_OPTION_TRACE).setText(trace ? MyConstants.XML_BOOLEAN_TRUE : MyConstants.XML_BOOLEAN_FALSE));
		return element;
    }


}
