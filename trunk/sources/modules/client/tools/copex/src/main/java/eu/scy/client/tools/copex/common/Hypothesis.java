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
 * hypothesis of a question
 * @author Marjolaine
 */
public class Hypothesis implements Cloneable{
    /* tag names */
    public final static String TAG_PROC_HYPOTHESIS = "proc_hypothesis";
    public final static String TAG_HYPOTHESIS = "hypothesis";
    public final static String TAG_HYPOTHESIS_COMMENT = "comment";
    public final static String TAG_HYPOTHESIS_HIDE = "hypothesis_hide";


    private long dbKey;
    private List<LocalText> listHypothesis;
    private boolean hide;
    private List<LocalText> listComments;

    public Hypothesis(Locale locale){
        this.dbKey = -1;
        this.listHypothesis = new LinkedList();
        this.listHypothesis.add(new LocalText("", locale));
        this.listComments = new LinkedList();
        this.listComments.add(new LocalText("", locale));
        this.hide = false;
    }
    public Hypothesis(long dbKey, List<LocalText> listHypothesis, List<LocalText> listComments, boolean hide) {
        this.dbKey = dbKey;
        this.listHypothesis = listHypothesis;
        this.listComments = listComments;
        this.hide = hide;
    }
    public Hypothesis(long dbKey, String hypothesis, String comment, boolean hide) {
        this.dbKey = dbKey;
        this.listHypothesis = new LinkedList();
        this.listComments = new LinkedList();
        setHypothesis(hypothesis);
        setComment(comment);
        this.hide = hide;
    }

    public Hypothesis(Element xmlElem, long dbKey) throws JDOMException {
	if (xmlElem.getName().equals(TAG_PROC_HYPOTHESIS)) {
            this.dbKey = dbKey;
            listHypothesis = new LinkedList<LocalText>();
            for (Iterator<Element> variableElem = xmlElem.getChildren(TAG_HYPOTHESIS).iterator(); variableElem.hasNext();) {
                Element e = variableElem.next();
                Locale l = new Locale(e.getAttribute(MyConstants.XMLNAME_LANGUAGE).getValue());
                listHypothesis.add(new LocalText(e.getText(), l));
            }
            listComments = new LinkedList<LocalText>();
            for (Iterator<Element> variableElem = xmlElem.getChildren(TAG_HYPOTHESIS_COMMENT).iterator(); variableElem.hasNext();) {
                Element e = variableElem.next();
                Locale l = new Locale(e.getAttribute(MyConstants.XMLNAME_LANGUAGE).getValue());
                listComments.add(new LocalText(e.getText(), l));
            }
            hide = xmlElem.getChild(TAG_HYPOTHESIS_HIDE).getText().equals(MyConstants.XML_BOOLEAN_TRUE);
        } else {
            throw(new JDOMException("Hypothesis expects <"+TAG_PROC_HYPOTHESIS+"> as root element, but found <"+xmlElem.getName()+">."));
	}
    }


    public long getDbKey() {
        return dbKey;
    }

    public void setDbKey(long dbKey) {
        this.dbKey = dbKey;
    }

    public List<LocalText> getListHypothesis() {
        return listHypothesis;
    }

    public void setListHypothesis(List<LocalText> listHypothesis) {
        this.listHypothesis = listHypothesis;
    }

    public String getHypothesis(Locale locale){
        return CopexUtilities.getText(listHypothesis, locale);
    }

    public void setHypothesis(LocalText text){
        int id = CopexUtilities.getIdText(text.getLocale(), listHypothesis);
        if(id ==-1){
            this.listHypothesis.add(text);
        }else{
            this.listHypothesis.set(id, text);
        }
    }

    public void setHypothesis(String text){
        for(Iterator<LocalText> t = listHypothesis.iterator();t.hasNext();){
            t.next().setText(text);
        }
    }

    public List<LocalText> getListComments() {
        return listComments;
    }

    public void setListComments(List<LocalText> listComments) {
        this.listComments = listComments;
    }
    public String getComment(Locale locale){
        return CopexUtilities.getText(listComments, locale);
    }

    public void setComment(LocalText text){
        int id = CopexUtilities.getIdText(text.getLocale(), listComments);
        if(id ==-1){
            this.listComments.add(text);
        }else{
            this.listComments.set(id, text);
        }
    }

    public void setComment(String text){
        for(Iterator<LocalText> t = listComments.iterator();t.hasNext();){
            t.next().setText(text);
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
            Hypothesis h = (Hypothesis) super.clone() ;
            long dbKeyC = this.dbKey;
            List<LocalText> listHypothesisC = new LinkedList();
            for (Iterator<LocalText> t = listHypothesis.iterator(); t.hasNext();) {
                listHypothesisC.add((LocalText)t.next().clone());
            }
            h.setListHypothesis(listHypothesisC);
            List<LocalText> listCommentsC = new LinkedList();
            for (Iterator<LocalText> t = listComments.iterator(); t.hasNext();) {
                listCommentsC.add((LocalText)t.next().clone());
            }
            h.setListComments(listCommentsC);
            h.setDbKey(dbKeyC);
            h.setHide(new Boolean(hide));
            return h;
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError();
        }
    }

    // toXML
    public Element toXML(){
        Element element = new Element(TAG_PROC_HYPOTHESIS);
        if(listHypothesis != null && listHypothesis.size() > 0){
            for (Iterator<LocalText> t = listHypothesis.iterator(); t.hasNext();) {
                LocalText l = t.next();
                Element e = new Element(TAG_HYPOTHESIS);
                e.setText(l.getText());
                e.setAttribute(new Attribute(MyConstants.XMLNAME_LANGUAGE, l.getLocale().getLanguage()));
                element.addContent(e);
            }
        }
        if(listComments != null && listComments.size() > 0){
            for (Iterator<LocalText> t = listComments.iterator(); t.hasNext();) {
                LocalText l = t.next();
                Element e = new Element(TAG_HYPOTHESIS_COMMENT);
                e.setText(l.getText());
                e.setAttribute(new Attribute(MyConstants.XMLNAME_LANGUAGE, l.getLocale().getLanguage()));
                element.addContent(e);
            }
        }
        element.addContent(new Element(TAG_HYPOTHESIS_HIDE).setText(hide ? MyConstants.XML_BOOLEAN_TRUE : MyConstants.XML_BOOLEAN_FALSE));
        return element;
    }
}
