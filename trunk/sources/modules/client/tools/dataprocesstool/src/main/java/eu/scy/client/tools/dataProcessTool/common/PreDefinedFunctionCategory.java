/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.dataProcessTool.common;

import eu.scy.client.tools.dataProcessTool.utilities.DataConstants;
import eu.scy.client.tools.dataProcessTool.utilities.MyUtilities;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import org.jdom.Element;
import org.jdom.JDOMException;

/**
 * category of pre-defined functions in fitex
 * @author Marjolaine
 */
public class PreDefinedFunctionCategory implements Cloneable {
    public final static String TAG_PRE_DEFINED_CATEGORY="function_class";
    private final static String TAG_PRE_DEFINED_CAT_NAME="name";

    private List<LocalText> listName;
    private ArrayList<PreDefinedFunction> listFunctions;

    public PreDefinedFunctionCategory(List<LocalText> listName) {
        this.listName = listName;
        this.listFunctions = new ArrayList();
    }

    public PreDefinedFunctionCategory(List<LocalText> listName, ArrayList<PreDefinedFunction> listFunctions) {
        this.listName = listName;
        this.listFunctions = listFunctions;
    }

    public PreDefinedFunctionCategory(Element xmlElem) throws JDOMException {
        if (xmlElem.getName().equals(TAG_PRE_DEFINED_CATEGORY)) {
            listName = new LinkedList<LocalText>();
            for (Iterator<Element> variableElem = xmlElem.getChildren(TAG_PRE_DEFINED_CAT_NAME).iterator(); variableElem.hasNext();) {
                Element e = variableElem.next();
                Locale l = new Locale(e.getAttribute(DataConstants.XMLNAME_LANGUAGE).getValue());
                listName.add(new LocalText(e.getText(), l));
            }
            listFunctions = new ArrayList<PreDefinedFunction>();
            for (Iterator<Element> variableElem = xmlElem.getChildren(PreDefinedFunction.TAG_PRE_DEFINED_FUNCTION).iterator(); variableElem.hasNext();) {
                listFunctions.add(new PreDefinedFunction(variableElem.next()));
            }
	}else{
            throw(new JDOMException("PreDefinedFunctionCategory expects <"+TAG_PRE_DEFINED_CATEGORY+"> as root element, but found <"+xmlElem.getName()+">."));
        }
    }


    public String getName(Locale locale) {
        return MyUtilities.getText(listName, locale);
    }

    public List<LocalText> getListName() {
        return listName;
    }

    public void setListName(List<LocalText> listName) {
        this.listName = listName;
    }

    

    public ArrayList<PreDefinedFunction> getListFunctions() {
        return listFunctions;
    }

    public void setListFunctions(ArrayList<PreDefinedFunction> listFunctions) {
        this.listFunctions = listFunctions;
    }

    @Override
    public Object clone()  {
        try {
            PreDefinedFunctionCategory cat = (PreDefinedFunctionCategory) super.clone() ;
            List<LocalText> listNameC = new LinkedList();
            for (Iterator<LocalText> t = listName.iterator(); t.hasNext();) {
                listNameC.add((LocalText)t.next().clone());
            }
            cat.setListName(listNameC);
            ArrayList list = null;
            if(this.listFunctions!=null){
                list = new ArrayList();
                for(Iterator<PreDefinedFunction> l = this.listFunctions.iterator();l.hasNext();){
                    list.add((PreDefinedFunction)l.next().clone());
                }
            }
            cat.setListFunctions(list);

            return cat;
        } catch (CloneNotSupportedException e) {
	    // this shouldn't happen, since we are Cloneable
	    throw new InternalError();
	}
    }


}
