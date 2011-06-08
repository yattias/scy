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
 * strategy material :
 * S0 : no material
 * S1 : list of materials to use
 * S2 : material available
 * S3 : material proposed
 * S4 : material created by user
 * @author Marjolaine
 */
public class MaterialStrategy implements Cloneable{
    /* tag names */
    public final static String TAG_MATERIAL_STRATEGY = "strategy";
    public final static String TAG_MATERIAL_STRATEGY_REF = "strategy_ref";
    public final static String TAG_MATERIAL_STRATEGY_CODE = "code";
    public final static String TAG_MATERIAL_STRATEGY_ISITEM = "is_item";
    public final static String TAG_MATERIAL_STRATEGY_ITEM = "item";
    public final static String TAG_MATERIAL_STRATEGY_ADD_MATERIAL = "add_mat";
    public final static String TAG_MATERIAL_STRATEGY_CHOOSE_MATERIAL = "choose_mat";
    public final static String TAG_MATERIAL_STRATEGY_COMMENT_MATERIAL = "comments_mat";

    
    private long dbKey ;
    private String code;
    private boolean isItem;
    private List<LocalText> listItem;
    private boolean addMaterial;
    private boolean chooseMaterial;
    private boolean commentsMaterial;

    
    public MaterialStrategy(long dbKey, String code, boolean isItem, List<LocalText> listItem, boolean addMaterial, boolean chooseMaterial, boolean commentsMaterial) {
        this.dbKey = dbKey;
        this.code = code;
        this.isItem = isItem;
        this.listItem = listItem;
        this.addMaterial = addMaterial;
        this.chooseMaterial = chooseMaterial;
        this.commentsMaterial = commentsMaterial;
    }

    public MaterialStrategy(Element xmlElem, long idMaterialStrategy) throws JDOMException {
        if (xmlElem.getName().equals(TAG_MATERIAL_STRATEGY)) {
            code = xmlElem.getChildText(TAG_MATERIAL_STRATEGY_CODE);
            dbKey = idMaterialStrategy;
            isItem = xmlElem.getChild(TAG_MATERIAL_STRATEGY_ISITEM).getText().equals(MyConstants.XML_BOOLEAN_TRUE);
            listItem = new LinkedList<LocalText>();
            for (Iterator<Element> variableElem = xmlElem.getChildren(TAG_MATERIAL_STRATEGY_ITEM).iterator(); variableElem.hasNext();) {
                Element e = variableElem.next();
                Locale l = new Locale(e.getAttribute(MyConstants.XMLNAME_LANGUAGE).getValue());
                listItem.add(new LocalText(e.getText(), l));
            }
            addMaterial = xmlElem.getChild(TAG_MATERIAL_STRATEGY_ADD_MATERIAL).getText().equals(MyConstants.XML_BOOLEAN_TRUE);
            chooseMaterial = xmlElem.getChild(TAG_MATERIAL_STRATEGY_CHOOSE_MATERIAL).getText().equals(MyConstants.XML_BOOLEAN_TRUE);
            commentsMaterial = xmlElem.getChild(TAG_MATERIAL_STRATEGY_COMMENT_MATERIAL).getText().equals(MyConstants.XML_BOOLEAN_TRUE);
	} else {
            throw(new JDOMException("Material Strategy expects <"+TAG_MATERIAL_STRATEGY+"> as root element, but found <"+xmlElem.getName()+">."));
	}
    }

    public MaterialStrategy(Element xmlElem, List<MaterialStrategy> list) throws JDOMException {
        if (xmlElem.getName().equals(TAG_MATERIAL_STRATEGY_REF)) {
            code = xmlElem.getChildText(TAG_MATERIAL_STRATEGY_CODE);
            for(Iterator<MaterialStrategy> m = list.iterator();m.hasNext();){
                MaterialStrategy s = m.next();
                if(s.getCode().equals(code)){
                    this.dbKey = s.getDbKey();
                    this.isItem = s.isItem();
                    this.listItem = s.getListItem();
                    this.addMaterial = s.canAddMaterial();
                    this.chooseMaterial = s.canChooseMaterial();
                    this.commentsMaterial = s.canCommentsMaterial();
                }
            }
        }else {
            throw(new JDOMException("Material Strategy expects <"+TAG_MATERIAL_STRATEGY+"> as root element, but found <"+xmlElem.getName()+">."));
	}
    }

    public boolean canAddMaterial() {
        return addMaterial;
    }

    public void setAddMaterial(boolean addMaterial) {
        this.addMaterial = addMaterial;
    }

    public boolean canChooseMaterial() {
        return chooseMaterial;
    }

    public void setChooseMaterial(boolean chooseMaterial) {
        this.chooseMaterial = chooseMaterial;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean canCommentsMaterial() {
        return commentsMaterial;
    }

    public void setCommentsMaterial(boolean commentsMaterial) {
        this.commentsMaterial = commentsMaterial;
    }

    public long getDbKey() {
        return dbKey;
    }

    public void setDbKey(long dbKey) {
        this.dbKey = dbKey;
    }

    public boolean isItem() {
        return isItem;
    }

    public void setItem(boolean isItem) {
        this.isItem = isItem;
    }

    public List<LocalText> getListItem() {
        return listItem;
    }

    public void setListItem(List<LocalText> listItem) {
        this.listItem = listItem;
    }

    public String getItem(Locale locale){
        return CopexUtilities.getText(listItem, locale);
    }

    @Override
    public Object clone()  {
        try {
            MaterialStrategy s = (MaterialStrategy) super.clone() ;
            s.setDbKey(new Long(this.dbKey));
            s.setCode(new String (this.code));
            List<LocalText> listItemC = new LinkedList();
            for (Iterator<LocalText> t = listItem.iterator(); t.hasNext();) {
                listItemC.add((LocalText)t.next().clone());
            }
            s.setListItem(listItemC);
            s.setItem(new Boolean(this.isItem));
            s.setAddMaterial(new Boolean(this.addMaterial));
            s.setChooseMaterial(new Boolean(this.chooseMaterial));
            s.setCommentsMaterial(new Boolean(this.commentsMaterial));
            return s;
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError();
        }
    }

    // toXML
    public Element toXML(){
        Element element = new Element(TAG_MATERIAL_STRATEGY);
        element.addContent(new Element(TAG_MATERIAL_STRATEGY_CODE).setText(code));
        element.addContent(new Element(TAG_MATERIAL_STRATEGY_ISITEM).setText(isItem ? MyConstants.XML_BOOLEAN_TRUE : MyConstants.XML_BOOLEAN_FALSE));
        if(listItem != null && listItem.size() > 0){
            for (Iterator<LocalText> t = listItem.iterator(); t.hasNext();) {
                LocalText l = t.next();
                Element e = new Element(TAG_MATERIAL_STRATEGY_ITEM);
                e.setText(l.getText());
                e.setAttribute(new Attribute(MyConstants.XMLNAME_LANGUAGE, l.getLocale().getLanguage()));
                element.addContent(e);
            }
        }
        element.addContent(new Element(TAG_MATERIAL_STRATEGY_ADD_MATERIAL).setText(addMaterial ? MyConstants.XML_BOOLEAN_TRUE : MyConstants.XML_BOOLEAN_FALSE));
        element.addContent(new Element(TAG_MATERIAL_STRATEGY_CHOOSE_MATERIAL).setText(chooseMaterial ? MyConstants.XML_BOOLEAN_TRUE : MyConstants.XML_BOOLEAN_FALSE));
        element.addContent(new Element(TAG_MATERIAL_STRATEGY_COMMENT_MATERIAL).setText(commentsMaterial ? MyConstants.XML_BOOLEAN_TRUE : MyConstants.XML_BOOLEAN_FALSE));

        return element;
    }

    public Element toXMLRef(){
        Element element = new Element(TAG_MATERIAL_STRATEGY_REF);
        element.addContent(new Element(TAG_MATERIAL_STRATEGY_CODE).setText(code));
        return element;
    }


    public boolean hasMaterial(){
        return !code.equals("S0");
        //return listItem.size() > 0 || addMaterial ;
    }
}
