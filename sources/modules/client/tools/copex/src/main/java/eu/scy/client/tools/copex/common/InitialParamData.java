/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.common;

import eu.scy.client.tools.copex.utilities.MyConstants;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import org.jdom.Element;
import org.jdom.JDOMException;

/**
 * parameter - type: data
 * @author Marjolaine
 */
public class InitialParamData extends InitialActionParam{

    public final static String TAG_INITIAL_PARAM_DATA = "initial_param_data";
    public final static String TAG_INITIAL_PARAM_DATA_REF = "initial_param_data_ref";

    
    public InitialParamData(long dbKey, List<LocalText>  listParamName) {
        super(dbKey, listParamName);
    }

    public InitialParamData(Element xmlElem, long idActionParam) throws JDOMException {
        super(xmlElem);
	if (xmlElem.getName().equals(TAG_INITIAL_PARAM_DATA)) {
            //this.dbKey = idActionParam;
            this.code = xmlElem.getChild(TAG_INITIAL_ACTION_PARAM_CODE).getText();
            this.dbKey = Long.parseLong(code);
            listParamName = new LinkedList<LocalText>();
            for (Iterator<Element> variableElem = xmlElem.getChildren(TAG_INITIAL_ACTION_PARAM_NAME).iterator(); variableElem.hasNext();) {
                Element e = variableElem.next();
                Locale l = new Locale(e.getAttribute(MyConstants.XMLNAME_LANGUAGE).getValue());
                listParamName.add(new LocalText(e.getText(), l));
            }

	} else {
            throw(new JDOMException("Initial action param data expects <"+TAG_INITIAL_PARAM_DATA+"> as root element, but found <"+xmlElem.getName()+">."));
	}
    }

    public InitialParamData(Element xmlElem, List<InitialParamData> list) throws JDOMException {
        super(xmlElem);
	if (xmlElem.getName().equals(TAG_INITIAL_PARAM_DATA_REF)) {
            this.code = xmlElem.getChild(TAG_INITIAL_ACTION_PARAM_CODE).getText();
            for(Iterator<InitialParamData> a = list.iterator();a.hasNext();){
                InitialParamData p = a.next();
                if(p.getCode().equals(code)){
                    this.dbKey = p.getDbKey();
                    this.listParamName = p.getListParamName();
                }
            }
        }else {
            throw(new JDOMException("Initial action param data expects <"+TAG_INITIAL_PARAM_DATA_REF+"> as root element, but found <"+xmlElem.getName()+">."));
	}
    }

    // toXML
    @Override
    public Element toXML(){
        Element el = new Element(TAG_INITIAL_PARAM_DATA);
        Element element  = super.toXML(el);
	return element;
    }

    @Override
    public Element toXMLRef(){
        return super.toXMLRef(new Element(TAG_INITIAL_PARAM_DATA_REF));
    }

}
