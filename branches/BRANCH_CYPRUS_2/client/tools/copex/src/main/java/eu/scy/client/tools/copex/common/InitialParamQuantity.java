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
 * Parametres initial de type quantite
 * @author Marjolaine
 */
public class InitialParamQuantity extends InitialActionParam {
    public final static String TAG_INITIAL_PARAM_QUANTITY = "initial_param_quantity";
    public final static String TAG_INITIAL_PARAM_QUANTITY_REF = "initial_param_quantity_ref";
    public final static String TAG_INITIAl_PARAM_QUANTITY_NAME = "quantity_name";
    /* valeur - fait reference a une grandeur physique */
    private PhysicalQuantity physicalQuantity ;
    /* nom de la quantite */
    private List<LocalText> listQuantityName;

    // CONSTRUCTOR
    public InitialParamQuantity(long dbKey, List<LocalText>  listParamName, PhysicalQuantity physicalQuantity, List<LocalText>  listQuantityName) {
        super(dbKey, listParamName);
        this.physicalQuantity = physicalQuantity;
        this.listQuantityName = listQuantityName;
    }

    public InitialParamQuantity(Element xmlElem, long idActionParam, List<PhysicalQuantity> listPhysicalQuantity) throws JDOMException {
        super(xmlElem);
		if (xmlElem.getName().equals(TAG_INITIAL_PARAM_QUANTITY)) {
            //this.dbKey = idActionParam;
            this.code = xmlElem.getChild(TAG_INITIAL_ACTION_PARAM_CODE).getText();
            this.dbKey = Long.parseLong(code);
            listParamName = new LinkedList<LocalText>();
            for (Iterator<Element> variableElem = xmlElem.getChildren(TAG_INITIAL_ACTION_PARAM_NAME).iterator(); variableElem.hasNext();) {
                Element e = variableElem.next();
                Locale l = new Locale(e.getAttribute(MyConstants.XMLNAME_LANGUAGE).getValue());
                listParamName.add(new LocalText(e.getText(), l));
            }
            this.physicalQuantity = new PhysicalQuantity(xmlElem.getChild(PhysicalQuantity.TAG_QUANTITY_REF), listPhysicalQuantity);
            listQuantityName = new LinkedList<LocalText>();
            for (Iterator<Element> variableElem = xmlElem.getChildren(TAG_INITIAl_PARAM_QUANTITY_NAME).iterator(); variableElem.hasNext();) {
                Element e = variableElem.next();
                Locale l = new Locale(e.getAttribute(MyConstants.XMLNAME_LANGUAGE).getValue());
                listQuantityName.add(new LocalText(e.getText(), l));
            }

		} else {
			throw(new JDOMException("Initial action param quantity expects <"+TAG_INITIAL_PARAM_QUANTITY+"> as root element, but found <"+xmlElem.getName()+">."));
		}
	}

    public InitialParamQuantity(Element xmlElem, List<InitialParamQuantity> list) throws JDOMException {
        super(xmlElem);
		if (xmlElem.getName().equals(TAG_INITIAL_PARAM_QUANTITY_REF)) {
            this.code = xmlElem.getChild(TAG_INITIAL_ACTION_PARAM_CODE).getText();
            for(Iterator<InitialParamQuantity> a = list.iterator();a.hasNext();){
                InitialParamQuantity p = a.next();
                if(p.getCode().equals(code)){
                    this.dbKey = p.getDbKey();
                    this.listParamName = p.getListParamName();
                    this.physicalQuantity = p.getPhysicalQuantity();
                    this.listQuantityName = p.getListQuantityName();
                }
            }
        }else {
			throw(new JDOMException("Initial action param quantity expects <"+TAG_INITIAL_PARAM_QUANTITY_REF+"> as root element, but found <"+xmlElem.getName()+">."));
		}
    }

    public List<LocalText> getListQuantityName() {
        return listQuantityName;
    }

    public void setListQuantityName(List<LocalText> listQuantityName) {
        this.listQuantityName = listQuantityName;
    }

    public PhysicalQuantity getPhysicalQuantity() {
        return physicalQuantity;
    }

    public void setPhysicalQuantity(PhysicalQuantity physicalQuantity) {
        this.physicalQuantity = physicalQuantity;
    }

    public String getQuantityName(Locale locale) {
       return CopexUtilities.getText(listQuantityName, locale);
    }

    public void setQuantityName(LocalText text) {
        int id = CopexUtilities.getIdText(text.getLocale(), listQuantityName);
        if(id ==-1){
            this.listQuantityName.add(text);
        }else{
            this.listQuantityName.set(id, text);
        }
    }

    public void setQuantityName(String text){
        for(Iterator<LocalText> t = listQuantityName.iterator();t.hasNext();){
            t.next().setText(text);
        }
    }

    @Override
    public Object clone() {
        InitialParamQuantity p = (InitialParamQuantity) super.clone() ;

        p.setPhysicalQuantity((PhysicalQuantity)this.physicalQuantity.clone());
        List<LocalText> listQuantityNameC = new LinkedList();
        for (Iterator<LocalText> t = listQuantityName.iterator(); t.hasNext();) {
            listQuantityNameC.add((LocalText)t.next().clone());
        }
        p.setListQuantityName(listQuantityNameC);
        return p;
    }

    // METHOD
    /* retourne la liste des unites possibles pour cette grandeur */
    public List<CopexUnit> getListUnit(){
        return physicalQuantity.getListUnit() ;
    }

    // toXML
    @Override
    public Element toXML(){
        Element el = new Element(TAG_INITIAL_PARAM_QUANTITY);
        Element element = super.toXML(el);
        element.addContent(physicalQuantity.toXMLRef());
        if(listQuantityName != null && listQuantityName.size() > 0){
            for (Iterator<LocalText> t = listQuantityName.iterator(); t.hasNext();) {
                LocalText l = t.next();
                Element e = new Element(TAG_INITIAl_PARAM_QUANTITY_NAME);
                e.setText(l.getText());
                e.setAttribute(new Attribute(MyConstants.XMLNAME_LANGUAGE, l.getLocale().getLanguage()));
                element.addContent(e);
            }
        }
		return element;
    }

    @Override
    public Element toXMLRef(){
        return super.toXMLRef(new Element(TAG_INITIAL_PARAM_QUANTITY_REF));
    }
}
