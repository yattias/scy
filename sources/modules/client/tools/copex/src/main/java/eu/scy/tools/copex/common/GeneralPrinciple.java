/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.common;

import eu.scy.tools.copex.utilities.CopexUtilities;
import eu.scy.tools.copex.utilities.MyConstants;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.JDOMException;

/**
 * general principle of the question
 * @author Marjolaine
 */
public class GeneralPrinciple implements Cloneable{
    /* tag names */
    public final static String TAG_PROC_GENERAL_PRINCIPLE = "proc_general_principle";
    public final static String TAG_PRINCIPLE = "principle";
    public final static String TAG_PRINCIPLE_DRAWING = "principle_drawing";
    public final static String TAG_PRINCIPLE_HIDE = "principle_hide";


    private long dbKey;
    private List<LocalText> listPrinciple;
    private Element drawing;
    private boolean hide;

    public GeneralPrinciple(Locale locale){
        this.dbKey = -1;
        this.listPrinciple = new LinkedList();
        this.listPrinciple.add(new LocalText("", locale));
        this.drawing = null;
        this.hide = false;
    }


    public GeneralPrinciple(long dbKey, List<LocalText> listPrinciple, Element drawing, boolean hide) {
        this.dbKey = dbKey;
        this.listPrinciple = listPrinciple;
        this.drawing = drawing;
        this.hide = hide;
    }

    public GeneralPrinciple(Element xmlElem, long dbKey) throws JDOMException {
		if (xmlElem.getName().equals(TAG_PROC_GENERAL_PRINCIPLE)) {
            this.dbKey = dbKey;
            listPrinciple = new LinkedList<LocalText>();
            for (Iterator<Element> variableElem = xmlElem.getChildren(TAG_PRINCIPLE).iterator(); variableElem.hasNext();) {
                Element e = variableElem.next();
                Locale l = new Locale(e.getAttribute(MyConstants.XMLNAME_LANGUAGE).getValue());
                listPrinciple.add(new LocalText(e.getText(), l));
            }
            drawing = xmlElem.getChild(TAG_PRINCIPLE_DRAWING);
            hide = xmlElem.getChild(TAG_PRINCIPLE_HIDE).getText().equals(MyConstants.XML_BOOLEAN_TRUE);
		} else {
			throw(new JDOMException("GeneralPrinciple expects <"+TAG_PROC_GENERAL_PRINCIPLE+"> as root element, but found <"+xmlElem.getName()+">."));
		}
	}

    public long getDbKey() {
        return dbKey;
    }

    public void setDbKey(long dbKey) {
        this.dbKey = dbKey;
    }

    public Element getDrawing() {
        return drawing;
    }

    public void setDrawing(Element drawing) {
        this.drawing = drawing;
    }

    public List<LocalText> getListPrinciple() {
        return listPrinciple;
    }

    public void setListPrinciple(List<LocalText> listPrinciple) {
        this.listPrinciple = listPrinciple;
    }


    public String getPrinciple(Locale locale){
        return CopexUtilities.getText(listPrinciple, locale);
    }

    public void setPrinciple(LocalText text){
        int id = CopexUtilities.getIdText(text.getLocale(), listPrinciple);
        if(id ==-1){
            this.listPrinciple.add(text);
        }else{
            this.listPrinciple.set(id, text);
        }
    }
    

    public boolean isHide() {
        return hide;
    }

    public void setHide(boolean hide) {
        this.hide = hide;
    }


    @Override
    public Object clone()  {
        try {
            GeneralPrinciple p = (GeneralPrinciple) super.clone() ;
            long dbKeyC = this.dbKey;
            List<LocalText> listPrincipleC = new LinkedList();
            for (Iterator<LocalText> t = listPrinciple.iterator(); t.hasNext();) {
                listPrincipleC.add((LocalText)t.next().clone());
            }
            p.setListPrinciple(listPrincipleC);
            p.setDbKey(dbKeyC);
            Element d = null;
            if(this.drawing != null){
                d = (Element)this.drawing.clone() ;
            }
            p.setDrawing(d);
            p.setHide(new Boolean(this.hide));
            return p;
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError();
        }
    }

     // toXML
    public Element toXML(){
        Element element = new Element(TAG_PROC_GENERAL_PRINCIPLE);
        if(listPrinciple != null && listPrinciple.size() > 0){
            for (Iterator<LocalText> t = listPrinciple.iterator(); t.hasNext();) {
                LocalText l = t.next();
                Element e = new Element(TAG_PRINCIPLE);
                e.setText(l.getText());
                e.setAttribute(new Attribute(MyConstants.XMLNAME_LANGUAGE, l.getLocale().getLanguage()));
                element.addContent(e);
            }
        }
        element.addContent(new Element(TAG_PRINCIPLE_HIDE).setText(hide ? MyConstants.XML_BOOLEAN_TRUE : MyConstants.XML_BOOLEAN_FALSE));
        if(drawing != null){
            Element e = new Element(TAG_PRINCIPLE_DRAWING) ;
            e.addContent(drawing);
            element.addContent(e);
        }
        return element;
    }
    
}
