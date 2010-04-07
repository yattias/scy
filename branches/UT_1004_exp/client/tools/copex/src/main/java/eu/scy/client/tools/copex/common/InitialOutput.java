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
 * output of a task
 * @author Marjolaine
 */
public abstract class InitialOutput implements Cloneable{
    public final static String TAG_INITIAL_OUTPUT_CODE = "code_output";
    public final static String TAG_INITIAL_OUTPUT_PARAM_NAME = "param_name";
    public final static String TAG_INITIAL_OUTPUT_TEXT_PROD = "text_prod";

    /* identifiant */
    protected long dbKey;
    protected String code;
    /*text prod */
    protected List<LocalText>  listTextProd;
    /*nom du param */
    protected List<LocalText>  listName;

    public InitialOutput(long dbKey, List<LocalText>  listTextProd, List<LocalText>  listName) {
        this.dbKey = dbKey;
        this.listName = listName;
        this.listTextProd = listTextProd;
        this.code = "";
    }

    public InitialOutput(Element xmlElem) throws JDOMException {
    }

    public long getDbKey() {
        return dbKey;
    }

    public void setDbKey(long dbKey) {
        this.dbKey = dbKey;
    }

    public String getName(Locale locale) {
        return CopexUtilities.getText(listName, locale);
    }

    public void setName(LocalText text) {
        int id = CopexUtilities.getIdText(text.getLocale(), listName);
        if(id ==-1){
            this.listName.add(text);
        }else{
            this.listName.set(id, text);
        }
    }

    public void setName(String text) {
        for(Iterator<LocalText> t = listName.iterator();t.hasNext();){
            t.next().setText(text);
        }
    }

    public String getTextProd(Locale locale) {
        return CopexUtilities.getText(listTextProd, locale);
    }

    public void setTextProd(LocalText text) {
        int id = CopexUtilities.getIdText(text.getLocale(), listTextProd);
        if(id ==-1){
            this.listTextProd.add(text);
        }else{
            this.listTextProd.set(id, text);
        }
    }

    public void setTextProd(String text) {
        for(Iterator<LocalText> t = listTextProd.iterator();t.hasNext();){
            t.next().setText(text);
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

    public List<LocalText> getListTextProd() {
        return listTextProd;
    }

    public void setListTextProd(List<LocalText> listTextProd) {
        this.listTextProd = listTextProd;
    }



    @Override
    protected Object clone(){
        try {
            InitialOutput o = (InitialOutput) super.clone() ;

            o.setDbKey(this.dbKey);
            o.setCode(new String(code));
            List<LocalText> listNameC = new LinkedList();
            for (Iterator<LocalText> t = listName.iterator(); t.hasNext();) {
                listNameC.add((LocalText)t.next().clone());
            }
            o.setListName(listNameC);
            List<LocalText> listTextProdC = new LinkedList();
            for (Iterator<LocalText> t = listTextProd.iterator(); t.hasNext();) {
                listTextProdC.add((LocalText)t.next().clone());
            }
            o.setListTextProd(listTextProdC);

            return o;
        }catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError();
        }
    }

    // toXML
    public Element toXML(){
        return null;
    }

    public Element toXML(Element element){
        element.addContent(new Element(TAG_INITIAL_OUTPUT_CODE).setText(code));
        if(listName != null && listName.size() > 0){
            for (Iterator<LocalText> t = listName.iterator(); t.hasNext();) {
                LocalText l = t.next();
                Element e = new Element(TAG_INITIAL_OUTPUT_PARAM_NAME);
                e.setText(l.getText());
                e.setAttribute(new Attribute(MyConstants.XMLNAME_LANGUAGE, l.getLocale().getLanguage()));
                element.addContent(e);
            }
        }
        if(listTextProd != null && listTextProd.size() > 0){
            for (Iterator<LocalText> t = listTextProd.iterator(); t.hasNext();) {
                LocalText l = t.next();
                Element e = new Element(TAG_INITIAL_OUTPUT_TEXT_PROD);
                e.setText(l.getText());
                e.setAttribute(new Attribute(MyConstants.XMLNAME_LANGUAGE, l.getLocale().getLanguage()));
                element.addContent(e);
            }
        }
        return element;
    }

    public Element toXMLRef(){
        return null;
    }

    public Element toXMLRef(Element element){
        element.addContent(new Element(TAG_INITIAL_OUTPUT_CODE).setText(code));
        return element;
    }

}
