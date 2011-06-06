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
 * parametres d'une action initiale nommee
 *  peut etre soit de type quantite soit de type type de materiel
 * @author Marjolaine
 */
public abstract class InitialActionParam implements Cloneable {
    public static final String TAG_INITIAL_ACTION_PARAM_CODE = "code";
    public static final String TAG_INITIAL_ACTION_PARAM_NAME = "name";


    /* identifiant */
    protected long dbKey;
    protected String code;
    /* nom du parametre */
    protected List<LocalText> listParamName;

    // CONSTRUCTOR
    public InitialActionParam(long dbKey, List<LocalText>  listParamName) {
        this.dbKey = dbKey;
        this.listParamName = listParamName;
        this.code = "";
    }

    public InitialActionParam(Element xmlElem) throws JDOMException {
    }
    
    public long getDbKey() {
        return dbKey;
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

    public List<LocalText> getListParamName() {
        return listParamName;
    }

    public void setListParamName(List<LocalText> listParamName) {
        this.listParamName = listParamName;
    }

    public String getParamName(Locale locale) {
        return CopexUtilities.getText(listParamName, locale);
    }

    public void setParamName(LocalText text) {
        int id = CopexUtilities.getIdText(text.getLocale(), listParamName);
        if(id ==-1){
            this.listParamName.add(text);
        }else{
            this.listParamName.set(id, text);
        }
    }

    public void setParamName(String text){
        for(Iterator<LocalText> t = listParamName.iterator();t.hasNext();){
            t.next().setText(text);
        }
    }

    @Override
    public Object clone()  {
        try {
            InitialActionParam p = (InitialActionParam) super.clone() ;
            p.setDbKey(this.dbKey);
            p.setCode(new String(code));
            List<LocalText> listParamNameC = new LinkedList();
            for (Iterator<LocalText> t = listParamName.iterator(); t.hasNext();) {
                listParamNameC.add((LocalText)t.next().clone());
            }
            p.setListParamName(listParamNameC);
            return p;
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError();
        }
    }

    public String toString(Locale locale) {
        return "("+dbKey+") "+getParamName(locale);
    }

    // toXML
    public Element toXML(){
        return null;
    }
    
    public Element toXML(Element element){
	element.addContent(new Element(TAG_INITIAL_ACTION_PARAM_CODE).setText(code.equals("")? ""+dbKey:code));
        if(listParamName != null && listParamName.size() > 0){
            for (Iterator<LocalText> t = listParamName.iterator(); t.hasNext();) {
                LocalText l = t.next();
                Element e = new Element(TAG_INITIAL_ACTION_PARAM_NAME);
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
        element.addContent(new Element(TAG_INITIAL_ACTION_PARAM_CODE).setText(code.equals("")? ""+dbKey:code));
        return element;
    }

}
