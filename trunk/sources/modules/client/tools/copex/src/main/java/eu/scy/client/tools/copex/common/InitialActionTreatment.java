/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.common;

import eu.scy.client.tools.copex.utilities.MyConstants;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import org.jdom.Element;
import org.jdom.JDOMException;


/**
 * initial action - treatment
 * uses data
 * produces data
 * @author Marjolaine
 */
public class InitialActionTreatment extends InitialNamedAction implements Cloneable {
    public final static String TAG_INITIAL_ACTION_TREATMENT = "initial_action_treatment";
    public final static String TAG_INITIAL_ACTION_TREATMENT_REF = "initial_action_treatment_ref";
    public final static String TAG_INITIAL_ACTION_TREATMENT_NB_DATA_PROD = "nb_data_prod";
    /* nb data prod */
    private int nbDataProd ;
    /* list output*/
    private ArrayList<InitialTreatmentOutput> listOutput;

    public InitialActionTreatment(long dbKey, String code, List<LocalText> listLibelle, boolean isSetting, InitialActionVariable variable, boolean draw, boolean repeat, int nbDataProd, ArrayList<InitialTreatmentOutput> listOutput, Element defaultDraw) {
        super(dbKey, code, listLibelle, isSetting, variable, draw, repeat, defaultDraw);
        this.nbDataProd = nbDataProd;
        this.listOutput = listOutput ;
    }

    public InitialActionTreatment(Element xmlElem, long idAction, Locale locale, long idActionParam, List<PhysicalQuantity> listPhysicalQuantity, List<TypeMaterial> listTypeMaterial, long idOutput) throws JDOMException {
	super(xmlElem);
        if (xmlElem.getName().equals(TAG_INITIAL_ACTION_TREATMENT)) {
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
            nbDataProd =Integer.parseInt(xmlElem.getChild(TAG_INITIAL_ACTION_TREATMENT_NB_DATA_PROD).getText());
            listOutput = new ArrayList();
            for (Iterator<Element> variablElem = xmlElem.getChildren(InitialTreatmentOutput.TAG_INITIAL_OUTPUT_TREATMENT).iterator(); variablElem.hasNext();) {
                listOutput.add(new InitialTreatmentOutput(variablElem.next(), idOutput++, listPhysicalQuantity));
            }
        }
	else {
            throw(new JDOMException("Initial action treatment expects <"+TAG_INITIAL_ACTION_TREATMENT+"> as root element, but found <"+xmlElem.getName()+">."));
	}
    }

    public InitialActionTreatment(Element xmlElem,List<InitialNamedAction> list) throws JDOMException{
        super(xmlElem);
        if (xmlElem.getName().equals(TAG_INITIAL_ACTION_TREATMENT_REF)) {
            code = xmlElem.getChild(TAG_INITIAL_NAMED_ACTION_CODE).getText();
            for(Iterator<InitialNamedAction> q = list.iterator();q.hasNext();){
                InitialNamedAction p = q.next();
                if(p instanceof InitialActionTreatment){
                    if(p.getCode().equals(code)){
                        this.dbKey = p.getDbKey();
                        this.listLibelle = p.getListLibelle();
                        this.isSetting = p.isSetting() ;
                        this.variable = p.getVariable() ;
                        this.draw = p.isDraw();
                        this.repeat = p.isRepeat();
                        this.nbDataProd = ((InitialActionTreatment)p).getNbDataProd();
                        this.listOutput = ((InitialActionTreatment)p).getListOutput();
                    }
                }
            }
        }else {
            throw(new JDOMException("Initial action treatment expects <"+TAG_INITIAL_ACTION_TREATMENT_REF+"> as root element, but found <"+xmlElem.getName()+">."));
	}
    }


    public int getNbDataProd() {
        return nbDataProd;
    }

    public void setNbDataProd(int nbDataProd) {
        this.nbDataProd = nbDataProd;
    }

    public ArrayList<InitialTreatmentOutput> getListOutput() {
        return listOutput;
    }

    public void setListOutput(ArrayList<InitialTreatmentOutput> listOutput) {
        this.listOutput = listOutput;
    }

    

    @Override
    public Object clone() {
        InitialActionTreatment a = (InitialActionTreatment) super.clone() ;

        ArrayList<InitialTreatmentOutput> list = new ArrayList();
        for (int i=0; i<listOutput.size(); i++){
            list.add((InitialTreatmentOutput)this.listOutput.get(i).clone());
        }
        a.setListOutput(list);
        a.setNbDataProd(this.nbDataProd);
        return a;
    }

    @Override
    public Element toXML(){
        Element element = super.toXML(new Element(TAG_INITIAL_ACTION_TREATMENT));
        element.addContent(new Element(TAG_INITIAL_ACTION_TREATMENT_NB_DATA_PROD).setText(Integer.toString(nbDataProd)));
        for (Iterator<InitialTreatmentOutput> o = listOutput.iterator(); o.hasNext();) {
            element.addContent(o.next().toXML());
        }
        return element;
    }

    @Override
    public Element toXMLRef(){
        Element element = super.toXMLRef(new Element(TAG_INITIAL_ACTION_TREATMENT_REF));
        return element;
    }

}
