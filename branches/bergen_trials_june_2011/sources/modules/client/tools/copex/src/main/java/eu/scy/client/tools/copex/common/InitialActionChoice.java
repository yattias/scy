/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.common;

import eu.scy.client.tools.copex.utilities.MyConstants;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import org.jdom.Element;
import org.jdom.JDOMException;
/**
 * action de type choice
 * TODO condition
 * uses data
 * @author Marjolaine
 */
public class InitialActionChoice extends InitialNamedAction{

    public final static String TAG_INITIAL_ACTION_CHOICE = "initial_action_choice";
    public final static String TAG_INITIAL_ACTION_CHOICE_REF = "initial_action_choice_ref";

    public InitialActionChoice(long dbKey, String code, List<LocalText> listLibelle, boolean isSetting, InitialActionVariable variable, boolean draw, boolean repeat, Element defaultDraw) {
        super(dbKey, code, listLibelle, isSetting, variable, draw, repeat, defaultDraw);
    }

    public InitialActionChoice(Element xmlElem, long idAction, Locale locale, long idActionParam, List<PhysicalQuantity> listPhysicalQuantity, List<TypeMaterial> listTypeMaterial, long idOutput) throws JDOMException {
		super(xmlElem);
        if (xmlElem.getName().equals(TAG_INITIAL_ACTION_CHOICE)) {
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
        }
		else {
			throw(new JDOMException("Initial action choice expects <"+TAG_INITIAL_ACTION_CHOICE+"> as root element, but found <"+xmlElem.getName()+">."));
		}
    }

    public InitialActionChoice(Element xmlElem,List<InitialNamedAction> list) throws JDOMException{
        super(xmlElem);
        if (xmlElem.getName().equals(TAG_INITIAL_ACTION_CHOICE_REF)) {
			code = xmlElem.getChild(TAG_INITIAL_NAMED_ACTION_CODE).getText();
            for(Iterator<InitialNamedAction> q = list.iterator();q.hasNext();){
                InitialNamedAction p = q.next();
                if(p.getCode().equals(code)){
                    this.dbKey = p.getDbKey();
                    this.listLibelle = p.getListLibelle();
                    this.isSetting = p.isSetting() ;
                    this.variable = p.getVariable() ;
                    this.draw = p.isDraw();
                    this.repeat = p.isRepeat();
                }
            }
        }else {
			throw(new JDOMException("Initial action choice expects <"+TAG_INITIAL_ACTION_CHOICE_REF+"> as root element, but found <"+xmlElem.getName()+">."));
		}
    }

    @Override
    public Element toXML(){
        Element element = super.toXML(new Element(TAG_INITIAL_ACTION_CHOICE));
        return element;
    }

    @Override
    public Element toXMLRef(){
        Element element = super.toXMLRef(new Element(TAG_INITIAL_ACTION_CHOICE_REF));
        return element;
    }


}
