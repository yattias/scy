/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.common;

import eu.scy.client.tools.copex.utilities.CopexUtilities;
import eu.scy.client.tools.copex.utilities.MyConstants;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.JDOMException;

/**
 * represente une quantite
 * @author MBO
 */
public class Quantity implements Cloneable{
    /* tag names */
    public final static String TAG_PARAMETER = "quantity";
    public final static String TAG_PARAMETER_REF = "quantity_ref";
    private final static String TAG_PARAMETER_ID = "id";
    private final static String TAG_PARAMETER_NAME = "name";
    private final static String TAG_PARAMETER_TYPE = "type";
    private final static String TAG_PARAMETER_VALUE = "value";
    private final static String TAG_PARAMETER_UNCERTAINTY = "uncertainty";

    /* cle primaire bd */
    private long dbKey;
    private String code;
    /* nom */
    private List<LocalText> listName;
    /*type  */
    private List<LocalText> listType;
    /* valeur */
    private double value;
    /* incertitude */
    private List<LocalText> listUncertainty;
    /* unite */
    private CopexUnit unit;

    // CONSTRUCTEURS
    public Quantity(long dbKey, List<LocalText> listName, List<LocalText> listType, double value, List<LocalText> listUncertainty, CopexUnit unit) {
        this.dbKey = dbKey;
        this.code = "QUANTITY";
        if(listName.size()> 0){
            code += "_"+listName.get(0).getText();
        }
        this.listName = listName;
        this.listType = listType;
        this.value = value;
        this.listUncertainty = listUncertainty;
        this.unit = unit;
    }

    public Quantity(long dbKey, Locale locale, String name, String type, double value, String uncertainty, CopexUnit unit) {
        this.dbKey = dbKey;
        this.code = "QUANTITY_"+name;
        this.listName = new LinkedList<LocalText>();
        listName.add(new LocalText(name, locale));
        this.listType = new LinkedList<LocalText>();
        listType.add(new LocalText(type, locale));
        this.value = value;
        this.listUncertainty = new LinkedList<LocalText>();
        listUncertainty.add(new LocalText(uncertainty, locale));
        this.unit = unit;
    }

    public Quantity(Element xmlElem, long dbKey, List<PhysicalQuantity> listPhysicalQuantity) throws JDOMException {
		if (xmlElem.getName().equals(TAG_PARAMETER)) {
            code = xmlElem.getChildText(TAG_PARAMETER_ID);
            this.dbKey = dbKey;
            listName = new LinkedList<LocalText>();
            for (Iterator<Element> variableElem = xmlElem.getChildren(TAG_PARAMETER_NAME).iterator(); variableElem.hasNext();) {
                Element e = variableElem.next();
                Locale l = new Locale(e.getAttribute(MyConstants.XMLNAME_LANGUAGE).getValue());
                listName.add(new LocalText(e.getText(), l));
            }
            listType = new LinkedList<LocalText>();
            for (Iterator<Element> variableElem = xmlElem.getChildren(TAG_PARAMETER_TYPE).iterator(); variableElem.hasNext();) {
                Element e = variableElem.next();
                Locale l = new Locale(e.getAttribute(MyConstants.XMLNAME_LANGUAGE).getValue());
                listType.add(new LocalText(e.getText(), l));
            }
            value = Double.parseDouble(xmlElem.getChildText(TAG_PARAMETER_VALUE));
            listUncertainty = new LinkedList<LocalText>();
            for (Iterator<Element> variableElem = xmlElem.getChildren(TAG_PARAMETER_UNCERTAINTY).iterator(); variableElem.hasNext();) {
                Element e = variableElem.next();
                Locale l = new Locale(e.getAttribute(MyConstants.XMLNAME_LANGUAGE).getValue());
                listUncertainty.add(new LocalText(e.getText(), l));
            }
            unit = new CopexUnit(xmlElem.getChild(CopexUnit.TAG_UNIT_REF), listPhysicalQuantity);

		} else {
			throw(new JDOMException("Quantity  expects <"+TAG_PARAMETER+"> as root element, but found <"+xmlElem.getName()+">."));
		}
	}

    public Quantity(Element xmlElem, List<Quantity> list) throws JDOMException {
        if (xmlElem.getName().equals(TAG_PARAMETER_REF)) {
			code = xmlElem.getChild(TAG_PARAMETER_ID).getText();
            for(Iterator<Quantity> q = list.iterator();q.hasNext();){
                Quantity qtt = q.next();
                if(qtt.getCode().equals(code)){
                    this.dbKey = qtt.getDbKey();
                    this.listName = qtt.getListName();
                    this.listType = qtt.getListType();
                    this.value = qtt.getValue();
                    this.listUncertainty = qtt.getListUncertainty();
                    this.unit = qtt.getUnit();
                }
            }
        }else {
			throw(new JDOMException("Quantity expects <"+TAG_PARAMETER_REF+"> as root element, but found <"+xmlElem.getName()+">."));
		}
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<LocalText> getListName() {
        return listName;
    }

    public void setListName(List<LocalText> listName) {
        this.listName = listName;
    }

    public List<LocalText> getListType() {
        return listType;
    }

    public void setListType(List<LocalText> listType) {
        this.listType = listType;
    }

    public List<LocalText> getListUncertainty() {
        return listUncertainty;
    }

    public void setListUncertainty(List<LocalText> listUncertainty) {
        this.listUncertainty = listUncertainty;
    }

    public String getName(Locale locale){
        return CopexUtilities.getText(listName, locale);
    }
    public String getType(Locale locale){
        return CopexUtilities.getText(listType, locale);
    }

    public String getUncertainty(Locale locale){
        return CopexUtilities.getText(listUncertainty, locale);
    }
    public CopexUnit getUnit() {
        return unit;
    }

    public void setUnit(CopexUnit unit) {
        this.unit = unit;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public long getDbKey() {
        return dbKey;
    }

    public void setDbKey(long dbKey) {
        this.dbKey = dbKey;
    }

    public String toDisplay(Locale locale){
        return getName(locale)+" = "+getValue()+" "+getUnit().getSymbol(locale);
    }


     @Override
    public Object clone()  {
        try {
            Quantity quantity = (Quantity) super.clone() ;
            long dbKeyC = this.dbKey;
            List<LocalText> listNameC = new LinkedList();
            for (Iterator<LocalText> t = listName.iterator(); t.hasNext();) {
                listNameC.add((LocalText)t.next().clone());
            }
            unit.setListName(listNameC);
            List<LocalText> listTypeC = new LinkedList();
            for (Iterator<LocalText> t = listType.iterator(); t.hasNext();) {
                listTypeC.add((LocalText)t.next().clone());
            }
            unit.setListName(listNameC);
            double valueC = new Double(this.value);
            List<LocalText> listUncertaintyC = new LinkedList();
            for (Iterator<LocalText> t = listUncertainty.iterator(); t.hasNext();) {
                listUncertaintyC.add((LocalText)t.next().clone());
            }
            CopexUnit unitC = null;
            if (this.unit != null)
                    unitC = (CopexUnit)this.unit.clone();
            
            quantity.setDbKey(dbKeyC);
            quantity.setValue(valueC);
            quantity.setUnit(unitC);
            
            return quantity;
        } catch (CloneNotSupportedException e) { 
            // this shouldn't happen, since we are Cloneable
            throw new InternalError();
        }
    }

     // toXML
    public Element toXML(){
        Element element = new Element(TAG_PARAMETER);
		element.addContent(new Element(TAG_PARAMETER_ID).setText(code));
        if(listName != null && listName.size() > 0){
            for (Iterator<LocalText> t = listName.iterator(); t.hasNext();) {
                LocalText l = t.next();
                Element e = new Element(TAG_PARAMETER_NAME);
                e.setText(l.getText());
                e.setAttribute(new Attribute(MyConstants.XMLNAME_LANGUAGE, l.getLocale().getLanguage()));
                element.addContent(e);
            }
        }
        if(listType != null && listType.size() > 0){
            for (Iterator<LocalText> t = listType.iterator(); t.hasNext();) {
                LocalText l = t.next();
                Element e = new Element(TAG_PARAMETER_TYPE);
                e.setText(l.getText());
                e.setAttribute(new Attribute(MyConstants.XMLNAME_LANGUAGE, l.getLocale().getLanguage()));
                element.addContent(e);
            }
        }
        element.addContent(new Element(TAG_PARAMETER_VALUE).setText(Double.toString(value)));
        if(listUncertainty != null && listUncertainty.size() > 0){
            for (Iterator<LocalText> t = listUncertainty.iterator(); t.hasNext();) {
                LocalText l = t.next();
                Element e = new Element(TAG_PARAMETER_UNCERTAINTY);
                e.setText(l.getText());
                e.setAttribute(new Attribute(MyConstants.XMLNAME_LANGUAGE, l.getLocale().getLanguage()));
                element.addContent(e);
            }
        }
        if (unit != null)
            element.addContent(unit.toXMLRef());

		return element;
    }

    public Element toXMLRef(){
        Element element = new Element(TAG_PARAMETER_REF);
		element.addContent(new Element(TAG_PARAMETER_ID).setText(code));
        return element;
    }

}
