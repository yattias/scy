/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.common;

import eu.scy.client.tools.copex.utilities.CopexUtilities;
import eu.scy.client.tools.copex.utilities.MyConstants;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.JDOMException;

/**
 * type of a material
 * @author marjolaine
 */
public class TypeMaterial implements Cloneable {
    /* tag names */
    public final static String TAG_TYPE = "type";
    public final static String TAG_TYPE_REF = "type_ref";
    private final static String TAG_TYPE_ID = "id";
    private final static String TAG_TYPE_NAME = "name";

    private long dbKey;
    private List<LocalText> listType;
    private String code;

    public TypeMaterial(long dbKey, List<LocalText> listType) {
        this.dbKey = dbKey;
        this.listType = listType;
        this.code = "";
    }

    public TypeMaterial(long dbKey, List<LocalText> listType, String code) {
        this.dbKey = dbKey;
        this.listType = listType;
        this.code = code;
    }

   public TypeMaterial(Element xmlElem, long dbKey) throws JDOMException {
		if (xmlElem.getName().equals(TAG_TYPE)) {
            this.dbKey = dbKey;
			code = xmlElem.getChild(TAG_TYPE_ID).getText();
            listType = new LinkedList<LocalText>();
            for (Iterator<Element> variableElem = xmlElem.getChildren(TAG_TYPE_NAME).iterator(); variableElem.hasNext();) {
                Element e = variableElem.next();
                Locale l = new Locale(e.getAttribute(MyConstants.XMLNAME_LANGUAGE).getValue());
                listType.add(new LocalText(e.getText(), l));
            }
		} else {
			throw(new JDOMException("Type Material expects <"+TAG_TYPE+"> as root element, but found <"+xmlElem.getName()+">."));
		}
	}

    public TypeMaterial(Element xmlElem, List<TypeMaterial> list) throws JDOMException {
        if (xmlElem.getName().equals(TAG_TYPE_REF)) {
			code = xmlElem.getChild(TAG_TYPE_ID).getText();
            for(Iterator<TypeMaterial> type = list.iterator();type.hasNext();){
                TypeMaterial t = type.next();
                if(t.getCode().equals(code)){
                    this.dbKey = t.getDbKey();
                    this.listType = t.getListType();
                }
            }
        }else {
            throw(new JDOMException("Type Material expects <"+TAG_TYPE_REF+"> as root element, but found <"+xmlElem.getName()+">."));
	}
    }

     public long getDbKey() {
        return dbKey;
    }

    public List<LocalText> getListType() {
        return listType;
    }

    public void setListType(List<LocalText> listType) {
        this.listType = listType;
    }
   public String getType(){
        return CopexUtilities.getText(listType, Locale.getDefault());
    }
   public void setType(String name){
        LocalText l = new LocalText(name, Locale.getDefault());
        setType(l);
    }

   public void setType(LocalText text){
        int i = CopexUtilities.getIdText(text.getLocale(), listType);
        if(i ==-1){
            this.listType.add(text);
        }else{
            this.listType.set(i, text);
        }
    }
    public void setDbKey(long dbKey) {
        this.dbKey = dbKey;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String toString(Locale locale) {
        return CopexUtilities.getText(listType, locale);
    }
    
    @Override
    public Object clone()  {
        try {
            TypeMaterial t = (TypeMaterial) super.clone() ;
            long dbKeyC = this.dbKey;
            List<LocalText> listTypeC = new LinkedList();
            for (Iterator<LocalText> s = listType.iterator(); s.hasNext();) {
                listTypeC.add((LocalText)s.next().clone());
            }
            t.setListType(listTypeC);
            
            t.setDbKey(dbKeyC);
            t.setCode(new String(code));
            
            return t;
        } catch (CloneNotSupportedException e) { 
	    // this shouldn't happen, since we are Cloneable
	    throw new InternalError();
	}
    }
    /* retourne le type de materiel dans une liste - null sinon */
    public static TypeMaterial getTypeMaterial(ArrayList<TypeMaterial> listT, long dbKey){
         for (Iterator iter=listT.iterator();iter.hasNext(); ){
             TypeMaterial t = (TypeMaterial)iter.next();
             if (t.getDbKey() == dbKey)
                 return t;
         }
         return null;
    }

    // toXML
    public Element toXML(){
        Element element = new Element(TAG_TYPE);
        element.addContent(new Element(TAG_TYPE_ID).setText(code.equals("")?""+dbKey:code));
        if(listType != null && listType.size() > 0){
            for (Iterator<LocalText> t = listType.iterator(); t.hasNext();) {
                LocalText l = t.next();
                Element e = new Element(TAG_TYPE_NAME);
                e.setText(l.getText());
                e.setAttribute(new Attribute(MyConstants.XMLNAME_LANGUAGE, l.getLocale().getLanguage()));
                element.addContent(e);
            }
        }
		return element;
    }

    public Element toXMLRef(){
        Element element = new Element(TAG_TYPE_REF);
		element.addContent(new Element(TAG_TYPE_ID).setText(code.equals("")?""+dbKey:code));
        return element;
    }
    
}
