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
 * unit (of a physical quantity)
 * @author Marjolaine
 */
public class CopexUnit implements Cloneable{
    public final static String TAG_UNIT = "unit";
    public final static String TAG_UNIT_REF = "unit_ref";
    public final static String TAG_UNIT_ID = "id";
    public final static String TAG_UNIT_FACTOR = "factor";
    public final static String TAG_UNIT_SYMBOL = "symbol";
    public final static String TAG_UNIT_NAME = "name";

    /* db identifier  */
    private long dbKey;

    private String id;
    private List<LocalText> listSymbol;
    private List<LocalText> listName;
    private double factor;

    public CopexUnit(long dbKey, List<LocalText> listName, List<LocalText> listSymbol, double factor) {
        this.dbKey = dbKey;
        this.listName = listName;
        this.listSymbol = listSymbol;
        this.factor = factor;
    }

    public CopexUnit(Element xmlElem, long dbKey) throws JDOMException {
        if (xmlElem.getName().equals(TAG_UNIT)) {
            id = xmlElem.getChild(TAG_UNIT_ID).getText();
            this.dbKey = dbKey;
            if(xmlElem.getChild("dbKey") != null){
                try{
                    this.dbKey = Long.parseLong(xmlElem.getChild("dbKey").getText());
                }catch(NumberFormatException ex){
                }
            }
            listName = new LinkedList<LocalText>();
            for (Iterator<Element> variableElem = xmlElem.getChildren(TAG_UNIT_NAME).iterator(); variableElem.hasNext();) {
                Element e = variableElem.next();
                Locale l = new Locale(e.getAttribute(MyConstants.XMLNAME_LANGUAGE).getValue());
                listName.add(new LocalText(e.getText(), l));
            }
            listSymbol = new LinkedList<LocalText>();
            for (Iterator<Element> variableElem = xmlElem.getChildren(TAG_UNIT_SYMBOL).iterator(); variableElem.hasNext();) {
                Element e = variableElem.next();
                Locale l = new Locale(e.getAttribute(MyConstants.XMLNAME_LANGUAGE).getValue());
                listSymbol.add(new LocalText(e.getText(), l));
            }
            factor = Double.NaN;
            try{
                factor = Double.parseDouble(xmlElem.getChild(TAG_UNIT_FACTOR).getText());
            }catch(NumberFormatException e){
            }
        }
	else {
            throw(new JDOMException("Unit expects <"+TAG_UNIT+"> as root element, but found <"+xmlElem.getName()+">."));
	}
    }

    public CopexUnit(Element xmlElem, List<PhysicalQuantity> listPhysicalQuantity) throws JDOMException {
        if (xmlElem.getName().equals(TAG_UNIT_REF)) {
            id = xmlElem.getChild(TAG_UNIT_ID).getText();
            for(Iterator<PhysicalQuantity> q = listPhysicalQuantity.iterator();q.hasNext();){
                List<CopexUnit> listUnit = q.next().getListUnit();
                for(Iterator<CopexUnit> unit = listUnit.iterator();unit.hasNext();){
                    CopexUnit u  = unit.next();
                    if(u.getId().equals(id)){
                        this.dbKey = u.getDbKey();
                        this.listName = u.getListName();
                        this.listSymbol = u.getListSymbol();
                        this.factor = u.getFactor();
                    }
                }
            }
        }else {
            throw(new JDOMException("Unit expects <"+TAG_UNIT_REF+"> as root element, but found <"+xmlElem.getName()+">."));
	}
    }


    /** constructor of the unit
     * @param name
     * @param symbol
     * @param factor
     */
    public CopexUnit(String name, String symbol, double factor ){
        this.dbKey = -1;
        this.id = "";
        this.factor = factor;
        this.listSymbol = new LinkedList();
        this.listName = new LinkedList();
        setName(name, Locale.getDefault());
        setSymbol(symbol, Locale.getDefault());
    }

    /** constructor of the unit
     * @param dbKey
     * @param name
     * @param symbol
     * @param factor
     * @param local
     */
    public CopexUnit(long dbKey, String name, String symbol, double factor){
        this.dbKey = dbKey;
        this.id = "";
        this.factor = factor;
        this.listSymbol = new LinkedList();
        this.listName = new LinkedList();
        setName(name, Locale.getDefault());
        setSymbol(symbol, Locale.getDefault());
    }

    public long getDbKey() {
        return dbKey;
    }

    public void setDbKey(long dbKey) {
        this.dbKey = dbKey;
    }

    public double getFactor() {
        return factor;
    }

    public void setFactor(double factor) {
        this.factor = factor;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<LocalText> getListName() {
        return listName;
    }

    public void setListName(List<LocalText> listName) {
        this.listName = listName;
    }

    public List<LocalText> getListSymbol() {
        return listSymbol;
    }

    public void setListSymbol(List<LocalText> listSymbol) {
        this.listSymbol = listSymbol;
    }

    public String getName(Locale locale){
        return CopexUtilities.getText(listName, locale);
    }
    public String getSymbol(Locale locale){
        return CopexUtilities.getText(listSymbol, locale);
    }
    public String getName(){
        return CopexUtilities.getText(listName, Locale.getDefault());
    }
    public String getSymbol(){
        return CopexUtilities.getText(listSymbol, Locale.getDefault());
    }
    public void setName(LocalText text){
        int i = CopexUtilities.getIdText(text.getLocale(), listName);
        if(i ==-1){
            this.listName.add(text);
        }else{
            this.listName.set(i, text);
        }
    }
    public void setName(String name, Locale locale){
        LocalText l = new LocalText(name, locale);
        setName(l);
    }
    public void setSymbol(LocalText text){
        int i = CopexUtilities.getIdText(text.getLocale(), listSymbol);
        if(i ==-1){
            this.listSymbol.add(text);
        }else{
            this.listSymbol.set(i, text);
        }
    }
    public void setName(String name){
        setName(name, Locale.getDefault());
    }
    public void setSymbol(String symbol){
        setSymbol(symbol, Locale.getDefault());
    }
    public void setSymbol(String symbol, Locale locale){
        LocalText l = new LocalText(symbol, locale);
        setSymbol(l);
    }
    @Override
    public Object clone()  {
       try {
            CopexUnit unit = (CopexUnit) super.clone() ;

            unit.setDbKey(this.dbKey);
            if( ! Double.isNaN(factor))
                unit.setFactor(new Double(this.factor));
            if(id != null)
                unit.setId(new String(this.id));
            List<LocalText> listNameC = new LinkedList();
            for (Iterator<LocalText> t = listName.iterator(); t.hasNext();) {
                listNameC.add((LocalText)t.next().clone());
            }
            unit.setListName(listNameC);
            return unit;
        } catch (CloneNotSupportedException e) {
	    // this shouldn't happen, since we are Cloneable
	    throw new InternalError();
        }
    }

    // toXML
    public Element toXML(){
        Element element = new Element(TAG_UNIT);
		element.addContent(new Element(TAG_UNIT_ID).setText(id));
        if(listName != null && listName.size() > 0){
            for (Iterator<LocalText> t = listName.iterator(); t.hasNext();) {
                LocalText l = t.next();
                Element e = new Element(TAG_UNIT_NAME);
                e.setText(l.getText());
                e.setAttribute(new Attribute(MyConstants.XMLNAME_LANGUAGE, l.getLocale().getLanguage()));
                element.addContent(e);
            }
        }
        if(listSymbol != null && listSymbol.size() > 0){
            for (Iterator<LocalText> t = listSymbol.iterator(); t.hasNext();) {
                LocalText l = t.next();
                Element e = new Element(TAG_UNIT_SYMBOL);
                e.setText(l.getText());
                e.setAttribute(new Attribute(MyConstants.XMLNAME_LANGUAGE, l.getLocale().getLanguage()));
                element.addContent(e);
            }
        }
        element.addContent(new Element(TAG_UNIT_FACTOR).setText(Double.toString(factor)));
	return element;
    }

    public Element toXMLRef(){
        Element element = new Element(TAG_UNIT_REF);
	element.addContent(new Element(TAG_UNIT_ID).setText(id));
        return element;
    }
}
