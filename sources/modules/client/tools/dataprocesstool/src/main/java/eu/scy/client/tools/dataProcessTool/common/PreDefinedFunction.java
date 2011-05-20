/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.dataProcessTool.common;

import eu.scy.client.tools.dataProcessTool.utilities.DataConstants;
import eu.scy.client.tools.dataProcessTool.utilities.MyUtilities;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import org.jdom.Element;
import org.jdom.JDOMException;

/**
 * pre-defined function in fitex
 * @author Marjolaine
 */
public class PreDefinedFunction implements Cloneable{
    public final static String TAG_PRE_DEFINED_FUNCTION="function";
    private final static String TAG_PRE_DEFINED_NAME="name";
    private final static String TAG_PRE_DEFINED_ID="id_function";
    private final static String TAG_PRE_DEFINED_TYPE="type";
    private final static String TAG_PRE_DEFINED_DESCRIPTION = "description";
    private final static String TAG_PRE_DEFINED_EXPRESSION = "expression";
    
    private String idFunction;
    private List<LocalText> listName;
    /* type x=f(y) ou y=f(x) cf cst FUNCTION_TYPE_*/
    private char type;
    private String description;
    private String expression;

    public PreDefinedFunction(String idFunction, List<LocalText> listName, char type, String description, String expression) {
        this.idFunction = idFunction;
        this.listName = listName;
        this.type = type;
        this.description = description;
        this.expression = expression;
    }

    public PreDefinedFunction(Element xmlElem) throws JDOMException {
        if (xmlElem.getName().equals(TAG_PRE_DEFINED_FUNCTION)) {
            idFunction = "";
            if(xmlElem.getChild(TAG_PRE_DEFINED_ID) != null){
                idFunction = xmlElem.getChild(TAG_PRE_DEFINED_ID).getText();
            }
            listName = new LinkedList<LocalText>();
            for (Iterator<Element> variableElem = xmlElem.getChildren(TAG_PRE_DEFINED_NAME).iterator(); variableElem.hasNext();) {
                Element e = variableElem.next();
                Locale l = new Locale(e.getAttribute(DataConstants.XMLNAME_LANGUAGE).getValue());
                listName.add(new LocalText(e.getText(), l));
            }
            String t = xmlElem.getChild(TAG_PRE_DEFINED_TYPE).getText();
            if(t.equals(DataConstants.F_X)){
                type = DataConstants.FUNCTION_TYPE_X_FCT_Y ;
            }else if(t.equals(DataConstants.F_Y)){
                type = DataConstants.FUNCTION_TYPE_Y_FCT_X ;
            }else{
                throw(new JDOMException("PreDefinedFunction type is not correct, found <"+xmlElem.getChild(TAG_PRE_DEFINED_TYPE).getText()+">."));
            }
            description = xmlElem.getChild(TAG_PRE_DEFINED_DESCRIPTION).getText();
            expression = xmlElem.getChild(TAG_PRE_DEFINED_EXPRESSION).getText();
	}else{
            throw(new JDOMException("PreDefinedFunction expects <"+TAG_PRE_DEFINED_FUNCTION+"> as root element, but found <"+xmlElem.getName()+">."));
        }
    }

    public String getIdFunction() {
        return idFunction;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
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

    public char getType() {
        return type;
    }

    public void setType(char type) {
        this.type = type;
    }

    public void setIdFunction(String idFunction) {
        this.idFunction = idFunction;
    }

    @Override
    public Object clone()  {
        try {
            PreDefinedFunction fct = (PreDefinedFunction) super.clone() ;
            fct.setIdFunction(new String(idFunction));
            List<LocalText> listNameC = new LinkedList();
            for (Iterator<LocalText> t = listName.iterator(); t.hasNext();) {
                listNameC.add((LocalText)t.next().clone());
            }
            fct.setListName(listNameC);
            fct.setType(new Character(this.type));
            fct.setDescription(new String(this.description));
            fct.setExpression(new String(this.expression));

            return fct;
        } catch (CloneNotSupportedException e) {
	    // this shouldn't happen, since we are Cloneable
	    throw new InternalError();
	}
    }
}
