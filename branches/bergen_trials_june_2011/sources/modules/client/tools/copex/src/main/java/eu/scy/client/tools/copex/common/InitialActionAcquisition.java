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
 * action de type acquisition
 * uses Material
 * produces Data
 * @author Marjolaine
 */
public class InitialActionAcquisition extends InitialNamedAction implements Cloneable {

    public final static String TAG_INITIAL_ACTION_ACQUISITION = "initial_action_acquisition";
    public final static String TAG_INITIAL_ACTION_ACQUISITION_REF = "initial_action_acquisition_ref";
    public final static String TAG_INITIAL_ACTION_ACQUISITION_NB_DATA_PROD = "nb_data_prod";

    /* nb data prod*/
    private int nbDataProd;
    /* liste des output*/
    private ArrayList<InitialAcquisitionOutput> listOutput;

    // CONSTRUCTOR
    public InitialActionAcquisition(long dbKey, String code, List<LocalText> listLibelle, boolean isSetting, InitialActionVariable variable, boolean draw,  boolean repeat, int nbDataProd, ArrayList<InitialAcquisitionOutput> listOutput, Element defaultDraw) {
        super(dbKey, code, listLibelle, isSetting, variable, draw, repeat, defaultDraw);
        this.nbDataProd = nbDataProd;
        this.listOutput = listOutput ;
    }

    public InitialActionAcquisition(Element xmlElem, long idAction, Locale locale, long idActionParam, List<PhysicalQuantity> listPhysicalQuantity, List<TypeMaterial> listTypeMaterial, long idOutput) throws JDOMException {
		super(xmlElem);
        if (xmlElem.getName().equals(TAG_INITIAL_ACTION_ACQUISITION)) {
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
            nbDataProd =Integer.parseInt(xmlElem.getChild(TAG_INITIAL_ACTION_ACQUISITION_NB_DATA_PROD).getText());
            listOutput = new ArrayList();
            for (Iterator<Element> variablElem = xmlElem.getChildren(InitialAcquisitionOutput.TAG_INITIAL_OUTPUT_ACQUISITION).iterator(); variablElem.hasNext();) {
                listOutput.add(new InitialAcquisitionOutput(variablElem.next(), idOutput++, listPhysicalQuantity));
            }
        }
		else {
			throw(new JDOMException("Initial action acquisition expects <"+TAG_INITIAL_ACTION_ACQUISITION+"> as root element, but found <"+xmlElem.getName()+">."));
		}
    }

    public InitialActionAcquisition(Element xmlElem,List<InitialNamedAction> list) throws JDOMException{
        super(xmlElem);
        if (xmlElem.getName().equals(TAG_INITIAL_ACTION_ACQUISITION_REF)) {
			code = xmlElem.getChild(TAG_INITIAL_NAMED_ACTION_CODE).getText();
            for(Iterator<InitialNamedAction> q = list.iterator();q.hasNext();){
                InitialNamedAction p = q.next();
                if(p instanceof InitialActionAcquisition){
                    if(p.getCode().equals(code)){
                        this.dbKey = p.getDbKey();
                        this.listLibelle = p.getListLibelle();
                        this.isSetting = p.isSetting() ;
                        this.variable = p.getVariable() ;
                        this.draw = p.isDraw();
                        this.repeat = p.isRepeat();
                        this.nbDataProd = ((InitialActionAcquisition)p).getNbDataProd();
                        this.listOutput = ((InitialActionAcquisition)p).getListOutput();
                    }
                }
            }
        }else {
			throw(new JDOMException("Initial action acquisition expects <"+TAG_INITIAL_ACTION_ACQUISITION_REF+"> as root element, but found <"+xmlElem.getName()+">."));
		}
    }


    // GETTER AND SETTER
    public int getNbDataProd() {
        return nbDataProd;
    }

    public void setNbDataProd(int nbDataProd) {
        this.nbDataProd = nbDataProd;
    }

    public ArrayList<InitialAcquisitionOutput> getListOutput() {
        return listOutput;
    }

    public void setListOutput(ArrayList<InitialAcquisitionOutput> listOutput) {
        this.listOutput = listOutput;
    }

    
    // OVERRIDE
    @Override
    public Object clone() {
        InitialActionAcquisition a = (InitialActionAcquisition) super.clone() ;

        a.setNbDataProd(this.nbDataProd);
        ArrayList<InitialAcquisitionOutput> list = new ArrayList();
        for (int i=0; i<listOutput.size(); i++){
            list.add((InitialAcquisitionOutput)this.listOutput.get(i).clone());
        }
        a.setListOutput(list);
        return a;
    }

    @Override
    public Element toXML(){
        Element element = super.toXML(new Element(TAG_INITIAL_ACTION_ACQUISITION));
        element.addContent(new Element(TAG_INITIAL_ACTION_ACQUISITION_NB_DATA_PROD).setText(Integer.toString(nbDataProd)));
        for (Iterator<InitialAcquisitionOutput> o = listOutput.iterator(); o.hasNext();) {
            element.addContent(o.next().toXML());
        }
        return element;
    }

    @Override
    public Element toXMLRef(){
        Element element = super.toXMLRef(new Element(TAG_INITIAL_ACTION_ACQUISITION_REF));
        return element;
    }

}
