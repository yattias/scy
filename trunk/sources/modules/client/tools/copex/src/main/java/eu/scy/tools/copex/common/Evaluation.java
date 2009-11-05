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
 * evaluation of the proc/question
 * @author Marjolaine
 */
public class Evaluation implements Cloneable{
    /* tag names */
    public final static String TAG_PROC_EVALUTATION = "proc_evaluation";
    public final static String TAG_EVALUATION = "evaluation";
    public final static String TAG_EVALUATION_HIDE = "evaluation_hide";

    private long dbKey;
    private List<LocalText> listEvaluation;
    private boolean hide;

    public Evaluation(Locale locale){
        this.dbKey = -1;
        this.listEvaluation = new LinkedList();
        this.listEvaluation.add(new LocalText("", locale));
        this.hide = false;
    }
    public Evaluation(long dbKey, List<LocalText> listEvaluation, boolean hide) {
        this.dbKey = dbKey;
        this.listEvaluation = listEvaluation;
        this.hide = hide;
    }

    public Evaluation(Element xmlElem, long dbKey) throws JDOMException {
		if (xmlElem.getName().equals(TAG_PROC_EVALUTATION)) {
			this.dbKey = dbKey;
            listEvaluation = new LinkedList<LocalText>();
            for (Iterator<Element> variableElem = xmlElem.getChildren(TAG_EVALUATION).iterator(); variableElem.hasNext();) {
                Element e = variableElem.next();
                Locale l = new Locale(e.getAttribute(MyConstants.XMLNAME_LANGUAGE).getValue());
                listEvaluation.add(new LocalText(e.getText(), l));
            }
            hide = xmlElem.getChild(TAG_EVALUATION_HIDE).getText().equals(MyConstants.XML_BOOLEAN_TRUE);
		} else {
			throw(new JDOMException("Evaluation expects <"+TAG_PROC_EVALUTATION+"> as root element, but found <"+xmlElem.getName()+">."));
		}
	}

    public long getDbKey() {
        return dbKey;
    }

    public void setDbKey(long dbKey) {
        this.dbKey = dbKey;
    }

    public List<LocalText> getListEvaluation() {
        return listEvaluation;
    }

    public void setListEvaluation(List<LocalText> listEvaluation) {
        this.listEvaluation = listEvaluation;
    }

    public String getEvaluation(Locale locale) {
        return CopexUtilities.getText(listEvaluation, locale);
    }

    public void setEvaluation(LocalText text) {
        int id = CopexUtilities.getIdText(text.getLocale(), listEvaluation);
        if(id ==-1){
            this.listEvaluation.add(text);
        }else{
            this.listEvaluation.set(id, text);
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
            Evaluation e = (Evaluation) super.clone() ;
            long dbKeyC = this.dbKey;
            List<LocalText> listEvaluationC = new LinkedList();
            for (Iterator<LocalText> t = listEvaluation.iterator(); t.hasNext();) {
                listEvaluationC.add((LocalText)t.next().clone());
            }
            e.setListEvaluation(listEvaluationC);
            e.setDbKey(dbKeyC);
            e.setHide(new Boolean(this.hide));
            return e;
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError();
        }
    }

    // toXML
    public Element toXML(){
        Element element = new Element(TAG_PROC_EVALUTATION);
		if(listEvaluation != null && listEvaluation.size() > 0){
            for (Iterator<LocalText> t = listEvaluation.iterator(); t.hasNext();) {
                LocalText l = t.next();
                Element e = new Element(TAG_EVALUATION);
                e.setText(l.getText());
                e.setAttribute(new Attribute(MyConstants.XMLNAME_LANGUAGE, l.getLocale().getLanguage()));
                element.addContent(e);
            }
        }
        element.addContent(new Element(TAG_EVALUATION_HIDE).setText(hide ? MyConstants.XML_BOOLEAN_TRUE : MyConstants.XML_BOOLEAN_FALSE));
        return element;
    }
}
