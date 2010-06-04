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
 * physical quantity
 * @author Marjolaine
 */
public class PhysicalQuantity implements Cloneable{
    public final static String TAG_QUANTITY = "quantity";
    public final static String TAG_QUANTITY_REF = "quantity_ref";
    public final static String TAG_QUANTITY_ID = "id";
    public final static String TAG_QUANTITY_MIN_VALUE = "min_value";
    public final static String TAG_QUANTITY_MAX_VALUE = "max_value";
    public final static String TAG_QUANTITY_NAME = "name";
    public final static String TAG_QUANTITY_SYMBOL = "symbol";
    
    
    /* identifiant */
    private long dbKey ;
    private String id;
    private Double min_value;
    private Double max_value;
    private List<LocalText> listSymbol;
    private List<LocalText> listName;
    private List<CopexUnit> listUnit;

    public PhysicalQuantity(long dbKey, String id, Double min_value, Double max_value, List<LocalText> listSymbol, List<LocalText> listName, List<CopexUnit> listUnit) {
        this.dbKey = dbKey;
        this.id = id;
        this.min_value = min_value;
        this.max_value = max_value;
        this.listSymbol = listSymbol;
        this.listName = listName;
        this.listUnit = listUnit;
    }

    public PhysicalQuantity(long dbKey,  List<LocalText> listName,List<LocalText> listSymbol, List<CopexUnit> listUnit) {
        this.dbKey = dbKey;
        this.id = "";
        this.min_value = Double.NaN;
        this.max_value = Double.NaN;
        this.listSymbol = listSymbol;
        this.listName = listName;
        this.listUnit = listUnit;
    }

    /** constructor of the physical quantity
     * @param name
     * @param symbol
     * @param local
     */
    public PhysicalQuantity(String name, String symbol, Locale locale ){
        this.dbKey = -1;
        this.id = "";
        this.min_value = Double.NaN;
        this.max_value = Double.NaN;
        this.listSymbol = new LinkedList();
        this.listName = new LinkedList();
        this.listUnit = new LinkedList();
        setName(name, locale);
        setSymbol(symbol, locale);
    }
    /** constructor of the physical quantity
     * @param dbKey
     * @param name
     * @param symbol
     * @param local
     */
    public PhysicalQuantity(long dbKey, String name, String symbol, Locale locale ){
        this.dbKey = dbKey;
        this.id = "";
        this.min_value = Double.NaN;
        this.max_value = Double.NaN;
        this.listSymbol = new LinkedList();
        this.listName = new LinkedList();
        this.listUnit = new LinkedList();
        setName(name, locale);
        setSymbol(symbol, locale);
    }
   
    public PhysicalQuantity(Element xmlElem, long dbKey, long idUnit) throws JDOMException {
		if (xmlElem.getName().equals(TAG_QUANTITY)) {
            this.dbKey = dbKey;
			id = xmlElem.getChild(TAG_QUANTITY_ID).getText();
            listName = new LinkedList<LocalText>();
            for (Iterator<Element> variableElem = xmlElem.getChildren(TAG_QUANTITY_NAME).iterator(); variableElem.hasNext();) {
                Element e = variableElem.next();
                Locale l = new Locale(e.getAttribute(MyConstants.XMLNAME_LANGUAGE).getValue());
                listName.add(new LocalText(e.getText(), l));
            }
            listSymbol = new LinkedList<LocalText>();
            for (Iterator<Element> variableElem = xmlElem.getChildren(TAG_QUANTITY_SYMBOL).iterator(); variableElem.hasNext();) {
                Element e = variableElem.next();
                Locale l = new Locale(e.getAttribute(MyConstants.XMLNAME_LANGUAGE).getValue());
                listSymbol.add(new LocalText(e.getText(), l));
            }
            min_value = Double.NaN;
            if (xmlElem.getChild(TAG_QUANTITY_MIN_VALUE) != null){
                try{
                    min_value = Double.parseDouble(xmlElem.getChild(TAG_QUANTITY_MIN_VALUE).getText());
                }catch(NumberFormatException e){
                }
            }
            max_value = Double.NaN;
            if (xmlElem.getChild(TAG_QUANTITY_MAX_VALUE) != null){
                try{
                    max_value = Double.parseDouble(xmlElem.getChild(TAG_QUANTITY_MAX_VALUE).getText());
                }catch(NumberFormatException e){
                }
            }
            if (xmlElem.getChild(CopexUnit.TAG_UNIT) != null){
                listUnit = new LinkedList<CopexUnit>();
                for (Iterator<Element> variablElem = xmlElem.getChildren(CopexUnit.TAG_UNIT).iterator(); variablElem.hasNext();) {
                    listUnit.add(new CopexUnit(variablElem.next(), idUnit++));
                }
            }
        }
		else {
			throw(new JDOMException("Physical Quantity expects <"+TAG_QUANTITY+"> as root element, but found <"+xmlElem.getName()+">."));
		}
    }

    public PhysicalQuantity(Element xmlElem, List<PhysicalQuantity> list)  throws JDOMException{
        if (xmlElem.getName().equals(TAG_QUANTITY_REF)) {
			id = xmlElem.getChild(TAG_QUANTITY_ID).getText();
            for(Iterator<PhysicalQuantity> q = list.iterator();q.hasNext();){
                PhysicalQuantity p = q.next();
                if(p.getId().equals(id)){
                    this.dbKey = p.getDbKey();
                    this.min_value = p.getMin_value();
                    this.max_value = p.getMax_value();
                    this.listSymbol = p.getListSymbol();
                    this.listName = p.getListName();
                    this.listUnit = p.getListUnit();
                }
            }
        }else {
			throw(new JDOMException("Physical Quantity expects <"+TAG_QUANTITY_REF+"> as root element, but found <"+xmlElem.getName()+">."));
		}
    }


    // GETTER AND SETTER
    public long getDbKey() {
        return dbKey;
    }

    public void setDbKey(long dbKey) {
        this.dbKey = dbKey;
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

    public String getName(Locale locale){
        return CopexUtilities.getText(listName, locale);
    }
    public String getSymbol(Locale locale){
        return CopexUtilities.getText(listSymbol, locale);
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
    public void setSymbol(String symbol, Locale locale){
        LocalText l = new LocalText(symbol, locale);
        setSymbol(l);
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

    public List<CopexUnit> getListUnit() {
        return listUnit;
    }

    public void setListUnit(List<CopexUnit> listUnit) {
        this.listUnit = listUnit;
    }

    public Double getMax_value() {
        return max_value;
    }

    public void setMax_value(Double max_value) {
        this.max_value = max_value;
    }

    public Double getMin_value() {
        return min_value;
    }

    public void setMin_value(Double min_value) {
        this.min_value = min_value;
    }

    

    @Override
    public Object clone()  {
       try {
            PhysicalQuantity grandeur = (PhysicalQuantity) super.clone() ;

            grandeur.setDbKey(this.dbKey);
            grandeur.setId(new String(this.id));
            grandeur.setMin_value(new Double(min_value));
            grandeur.setMax_value(new Double(max_value));
            List<LocalText> listNameC = new LinkedList();
            for (Iterator<LocalText> t = listName.iterator(); t.hasNext();) {
                listNameC.add((LocalText)t.next().clone());
            }
            grandeur.setListName(listNameC);
            List<LocalText> listSymbolC = new LinkedList();
            for (Iterator<LocalText> t = listSymbol.iterator(); t.hasNext();) {
                listSymbolC.add((LocalText)t.next().clone());
            }
            grandeur.setListSymbol(listSymbolC);
            List<CopexUnit> listUnitC = new LinkedList();
            for (Iterator<CopexUnit> u = listUnit.iterator(); u.hasNext();) {
                listUnitC.add((CopexUnit)u.next().clone());
            }
            grandeur.setListUnit(listUnitC);
            return grandeur;
        } catch (CloneNotSupportedException e) {
	    // this shouldn't happen, since we are Cloneable
	    throw new InternalError();
        }
    }

    /* retourne l'unite qui correspond au dbKey, null sinon */
    public CopexUnit getUnit(long dbKeyU){
        int nbu = listUnit.size();
        for (int i=0; i<nbu; i++){
            if (listUnit.get(i).getDbKey() == dbKeyU){
                return listUnit.get(i);
            }
        }
        return null;
    }

    public String toString(Locale locale) {
        String s = CopexUtilities.getText(listName, locale) +" : \n";
        for (int i=0; i<this.listUnit.size(); i++){
            s += CopexUtilities.getText(this.listUnit.get(i).getListName(), locale)+" ("+CopexUtilities.getText(this.listUnit.get(i).getListSymbol(), locale)+")";
        }
        return s;
    }

    // toXML
    public Element toXML(){
        Element element = new Element(TAG_QUANTITY);
		element.addContent(new Element(TAG_QUANTITY_ID).setText(id));
        if(listName != null && listName.size() > 0){
            for (Iterator<LocalText> t = listName.iterator(); t.hasNext();) {
                LocalText l = t.next();
                Element e = new Element(TAG_QUANTITY_NAME);
                e.setText(l.getText());
                e.setAttribute(new Attribute(MyConstants.XMLNAME_LANGUAGE, l.getLocale().getLanguage()));
                element.addContent(e);
            }
        }
        if(listSymbol != null && listSymbol.size() > 0){
            for (Iterator<LocalText> t = listSymbol.iterator(); t.hasNext();) {
                LocalText l = t.next();
                Element e = new Element(TAG_QUANTITY_SYMBOL);
                e.setText(l.getText());
                e.setAttribute(new Attribute(MyConstants.XMLNAME_LANGUAGE, l.getLocale().getLanguage()));
                element.addContent(e);
            }
        }
        if (listUnit != null){
            for (Iterator<CopexUnit> u = listUnit.iterator(); u.hasNext();) {
                element.addContent(u.next().toXML());
            }
        }
        if(!Double.isNaN(min_value)){
            element.addContent(new Element(TAG_QUANTITY_MIN_VALUE).setText(Double.toString(min_value)));
        }
        if(!Double.isNaN(max_value)){
            element.addContent(new Element(TAG_QUANTITY_MAX_VALUE).setText(Double.toString(max_value)));
        }
		return element;
    }

    public Element toXMLRef(){
        Element element = new Element(TAG_QUANTITY_REF);
		element.addContent(new Element(TAG_QUANTITY_ID).setText(id));
        return element;
    }

    /** add a unit
     * @param unit to add
     */
    public void addUnit(CopexUnit unit){
        this.listUnit.add(unit);
    }

    /** remove the unit
     * @param unit to add
     * @return true if the physical quantity contains the specified unit
     */
    public boolean removeUnit(CopexUnit unit){
        return this.listUnit.remove(unit);
    }

    /** update the unit
     * @param index of the unit to replace
     * @param unit to be store at the specified position
     */
    public void setUnit(int id, CopexUnit unit){
        this.listUnit.set(id, unit);
    }

    /** get the index of a unit
     * @param unit to search for
     * @return the index of the first occurence, -1 if the physicla quantity does not contain this unit
     */
    public int indexOfUnit(CopexUnit unit){
        return this.listUnit.indexOf(unit);
    }
    

}
