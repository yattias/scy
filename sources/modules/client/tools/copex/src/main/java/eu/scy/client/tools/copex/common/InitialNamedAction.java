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
 * initial named action of the initial procedure
 * if this action has setting : could be action choice, manipulation, acquisition, treatment
 * @author Marjolaine
 */
public class InitialNamedAction implements Cloneable {
    public final static String TAG_INITIAL_NAMED_ACTION = "initial_named_action";
    public final static String TAG_INITIAL_NAMED_ACTION_REF = "initial_named_action_ref";
    public final static String TAG_INITIAL_NAMED_ACTION_CODE = "id";
    public final static String TAG_INITIAL_NAMED_ACTION_LIBELLE = "libelle";
    public final static String TAG_INITIAL_NAMED_ACTION_IS_SETTING = "is_setting";
    public final static String TAG_INITIAL_NAMED_ACTION_DRAW = "draw";
    public final static String TAG_INITIAL_NAMED_ACTION_REPEAT = "repeat";
    public final static String TAG_INITIAL_NAMED_ACTION_DEFAULT_DRAW = "default_draw";
    
    /* db key */
    protected long dbKey;
    /* code */
    protected String code;
    /* text */
    protected List<LocalText> listLibelle;
    /* is setting*/
    protected boolean isSetting ;
    /* if isSetting => action variable */
    protected InitialActionVariable variable;
    /* draw authorized*/
    protected boolean draw;
    /* repeat authorized*/
    protected boolean repeat;
    /** default draw*/
    protected Element defaultDraw;

    public InitialNamedAction(long dbKey, String code, List<LocalText> listLibelle, boolean isSetting, InitialActionVariable variable, boolean draw, boolean repeat, Element defaultDraw) {
        this.dbKey = dbKey;
        this.code = code;
        this.listLibelle = listLibelle;
        this.isSetting = isSetting ;
        this.variable = variable ;
        this.draw = draw;
        this.repeat = repeat;
        this.defaultDraw = defaultDraw;
    }

    public InitialNamedAction(Element xmlElem){
    }
    public InitialNamedAction(Element xmlElem, long idAction, Locale locale, long idActionParam, List<PhysicalQuantity> listPhysicalQuantity, List<TypeMaterial> listTypeMaterial) throws JDOMException {
	if (xmlElem.getName().equals(TAG_INITIAL_NAMED_ACTION)) {
            this.dbKey = idAction;
            code = xmlElem.getChild(TAG_INITIAL_NAMED_ACTION_CODE).getText();
            listLibelle = new LinkedList<LocalText>();
            for (Iterator<Element> variableElem = xmlElem.getChildren(TAG_INITIAL_NAMED_ACTION_LIBELLE).iterator(); variableElem.hasNext();) {
                Element e = variableElem.next();
                Locale l = new Locale(e.getAttribute(MyConstants.XMLNAME_LANGUAGE).getValue());
                listLibelle.add(new LocalText(e.getText(), l));
            }
            isSetting = xmlElem.getChild(TAG_INITIAL_NAMED_ACTION_IS_SETTING).getText().equals(MyConstants.XML_BOOLEAN_TRUE);
            draw = xmlElem.getChild(TAG_INITIAL_NAMED_ACTION_DRAW).getText().equals(MyConstants.XML_BOOLEAN_TRUE);
            repeat = xmlElem.getChild(TAG_INITIAL_NAMED_ACTION_REPEAT).getText().equals(MyConstants.XML_BOOLEAN_TRUE);
            if(xmlElem.getChild(InitialActionVariable.TAG_INITIAL_ACTION_VARIABLE) != null){
                variable = new InitialActionVariable(xmlElem.getChild(InitialActionVariable.TAG_INITIAL_ACTION_VARIABLE), locale, idActionParam, listPhysicalQuantity, listTypeMaterial);
            }
            defaultDraw = null;
            if(xmlElem.getChild(TAG_INITIAL_NAMED_ACTION_DEFAULT_DRAW) != null){
                defaultDraw = xmlElem.getChild(TAG_INITIAL_NAMED_ACTION_DEFAULT_DRAW);
            }
        }else {
            throw(new JDOMException("Initial Named Action expects <"+TAG_INITIAL_NAMED_ACTION+"> as root element, but found <"+xmlElem.getName()+">."));
	}
    }

    public InitialNamedAction(Element xmlElem,List<InitialNamedAction> list) throws JDOMException{
        if (xmlElem.getName().equals(TAG_INITIAL_NAMED_ACTION_REF)) {
            this.code = xmlElem.getChild(TAG_INITIAL_NAMED_ACTION_CODE).getText();
            for(Iterator<InitialNamedAction> q = list.iterator();q.hasNext();){
                InitialNamedAction p = q.next();
                if(p.getCode().equals(code)){
                    this.dbKey = p.getDbKey();
                    this.listLibelle = p.getListLibelle();
                    this.isSetting = p.isSetting() ;
                    this.variable = p.getVariable() ;
                    this.draw = p.isDraw();
                    this.repeat = p.isRepeat();
                    this.defaultDraw = p.getDefaultDraw();
                }
            }
        }else {
            throw(new JDOMException("Initial Named Action expects <"+TAG_INITIAL_NAMED_ACTION_REF+"> as root element, but found <"+xmlElem.getName()+">."));
	}
    }

    public Element getDefaultDraw() {
        return defaultDraw;
    }

    public void setDefaultDraw(Element defaultDraw) {
        this.defaultDraw = defaultDraw;
    }
    
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getDbKey() {
        return dbKey;
    }

    public void setDbKey(long dbKey) {
        this.dbKey = dbKey;
    }

    public String getLibelle(Locale locale) {
        return CopexUtilities.getText(listLibelle, locale);
    }

    public void setLibelle(LocalText text) {
        int id = CopexUtilities.getIdText(text.getLocale(), listLibelle);
        if(id ==-1){
            this.listLibelle.add(text);
        }else{
            this.listLibelle.set(id, text);
        }
    }

    public void setLibelle(String text){
        for(Iterator<LocalText> t = listLibelle.iterator();t.hasNext();){
            t.next().setText(text);
        }
    }

    public List<LocalText> getListLibelle() {
        return listLibelle;
    }

    public void setListLibelle(List<LocalText> listLibelle) {
        this.listLibelle = listLibelle;
    }

    public boolean isSetting() {
        return isSetting;
    }

    public void setSetting(boolean isSetting) {
        this.isSetting = isSetting;
    }

    public InitialActionVariable getVariable() {
        return variable;
    }

    public void setVariable(InitialActionVariable variable) {
        this.variable = variable;
    }

    public boolean isDraw() {
        return draw;
    }

    public void setDraw(boolean draw) {
        this.draw = draw;
    }

    public boolean isRepeat() {
        return repeat;
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    

    @Override
    public Object clone(){
       try {
            InitialNamedAction a = (InitialNamedAction) super.clone() ;
            a.setDbKey(this.getDbKey());
            a.setCode(new String(this.getCode()));
            List<LocalText> listlibelleC = new LinkedList();
            for (Iterator<LocalText> t = listLibelle.iterator(); t.hasNext();) {
                listlibelleC.add((LocalText)t.next().clone());
            }
            a.setListLibelle(listlibelleC);
            a.setSetting(this.isSetting);
            InitialActionVariable variableC = null;
            if (this.variable != null){
                variableC = (InitialActionVariable)variable.clone();
            }
            a.setVariable(variableC);
            a.setDraw(this.isDraw());
            a.setRepeat(this.isRepeat());
            Element d = null;
            if(this.defaultDraw != null){
                d = (Element)defaultDraw.clone();
            }
            a.setDefaultDraw(d);
            return a;
        } catch (CloneNotSupportedException e) {
	    // this shouldn't happen, since we are Cloneable
	    throw new InternalError();
        }
    }

    // toXML
    public Element toXML(){
        Element element = new Element(TAG_INITIAL_NAMED_ACTION);
	return toXML(element);
    }

    // toXML
    protected Element toXML(Element element){
	element.addContent(new Element(TAG_INITIAL_NAMED_ACTION_CODE).setText(code));
        if(listLibelle != null && listLibelle.size() > 0){
            for (Iterator<LocalText> t = listLibelle.iterator(); t.hasNext();) {
                LocalText l = t.next();
                Element e = new Element(TAG_INITIAL_NAMED_ACTION_LIBELLE);
                e.setText(l.getText());
                e.setAttribute(new Attribute(MyConstants.XMLNAME_LANGUAGE, l.getLocale().getLanguage()));
                element.addContent(e);
            }
        }
        element.addContent(new Element(TAG_INITIAL_NAMED_ACTION_IS_SETTING).setText(isSetting ? MyConstants.XML_BOOLEAN_TRUE : MyConstants.XML_BOOLEAN_FALSE));
        element.addContent(new Element(TAG_INITIAL_NAMED_ACTION_DRAW).setText(draw ? MyConstants.XML_BOOLEAN_TRUE : MyConstants.XML_BOOLEAN_FALSE));
        element.addContent(new Element(TAG_INITIAL_NAMED_ACTION_REPEAT).setText(repeat ? MyConstants.XML_BOOLEAN_TRUE : MyConstants.XML_BOOLEAN_FALSE));
        if(variable != null){
            element.addContent(variable.toXML());
        }
        if(defaultDraw != null){
            element.addContent(new Element(TAG_INITIAL_NAMED_ACTION_DEFAULT_DRAW).setContent(defaultDraw));
        }
	return element;
    }

    public Element toXMLRef(){
        Element element = new Element(TAG_INITIAL_NAMED_ACTION_REF);
		return toXMLRef(element);
    }

    protected Element toXMLRef(Element element){
        element.addContent(new Element(TAG_INITIAL_NAMED_ACTION_CODE).setText(code));
        return element;
    }
    
}
