/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.common;

import java.util.ArrayList;
import eu.scy.client.tools.copex.utilities.MyConstants;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import org.jdom.Element;
import org.jdom.JDOMException;

/**
 * initial action manipulation
 * uses material
 * produces material
 * @author Marjolaine
 */
public class InitialActionManipulation extends InitialNamedAction implements Cloneable{
    public final static String TAG_INITIAL_ACTION_MANIPULATION = "initial_action_manipulation";
    public final static String TAG_INITIAL_ACTION_MANIPULATION_REF = "initial_action_manipulation_ref";
    public final static String TAG_INITIAL_ACTION_MANIPULATION_NB_MATERIAL_PROD = "nb_material_prod";

    /* nb material prod*/
    private int nbMaterialProd;
    /* list  output*/
    private ArrayList<InitialManipulationOutput> listOutput;

    public InitialActionManipulation(long dbKey, String code, List<LocalText> listLibelle, boolean isSetting, InitialActionVariable variable, boolean draw,  boolean repeat, int nbMaterialProd, ArrayList<InitialManipulationOutput> listOutput, Element defaultDraw) {
        super(dbKey, code, listLibelle, isSetting, variable, draw, repeat, defaultDraw);
        this.nbMaterialProd = nbMaterialProd;
        this.listOutput = listOutput ;
    }

    public InitialActionManipulation(Element xmlElem, long idAction, Locale locale, long idActionParam, List<PhysicalQuantity> listPhysicalQuantity, List<TypeMaterial> listTypeMaterial, long idOutput) throws JDOMException {
	super(xmlElem);
        if (xmlElem.getName().equals(TAG_INITIAL_ACTION_MANIPULATION)) {
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
            nbMaterialProd =Integer.parseInt(xmlElem.getChild(TAG_INITIAL_ACTION_MANIPULATION_NB_MATERIAL_PROD).getText());
            listOutput = new ArrayList();
            for (Iterator<Element> variablElem = xmlElem.getChildren(InitialManipulationOutput.TAG_INITIAL_OUTPUT_MANIPULATION).iterator(); variablElem.hasNext();) {
                listOutput.add(new InitialManipulationOutput(variablElem.next(), idOutput, listTypeMaterial));
            }
        }
        else {
            throw(new JDOMException("Initial action manipulation expects <"+TAG_INITIAL_ACTION_MANIPULATION+"> as root element, but found <"+xmlElem.getName()+">."));
	}
    }

    public InitialActionManipulation(Element xmlElem,List<InitialNamedAction> list) throws JDOMException{
        super(xmlElem);
        if (xmlElem.getName().equals(TAG_INITIAL_ACTION_MANIPULATION_REF)) {
            code = xmlElem.getChild(TAG_INITIAL_NAMED_ACTION_CODE).getText();
            for(Iterator<InitialNamedAction> q = list.iterator();q.hasNext();){
                InitialNamedAction p = q.next();
                if(p instanceof InitialActionManipulation){
                    if(p.getCode().equals(code)){
                        this.dbKey = p.getDbKey();
                        this.listLibelle = p.getListLibelle();
                        this.isSetting = p.isSetting() ;
                        this.variable = p.getVariable() ;
                        this.draw = p.isDraw();
                        this.repeat = p.isRepeat();
                        this.nbMaterialProd = ((InitialActionManipulation)p).getNbMaterialProd();
                        this.listOutput = ((InitialActionManipulation)p).getListOutput();
                    }
                }
            }
        }else {
            throw(new JDOMException("Initial action manipulation expects <"+TAG_INITIAL_ACTION_MANIPULATION_REF+"> as root element, but found <"+xmlElem.getName()+">."));
	}
    }

    public int getNbMaterialProd() {
        return nbMaterialProd;
    }

    public void setNbMaterialProd(int nbMaterialProd) {
        this.nbMaterialProd = nbMaterialProd;
    }

    public ArrayList<InitialManipulationOutput> getListOutput() {
        return listOutput;
    }

    public void setListOutput(ArrayList<InitialManipulationOutput> listOutput) {
        this.listOutput = listOutput;
    }

    

    @Override
    public Object clone() {
        InitialActionManipulation a = (InitialActionManipulation) super.clone() ;
        a.setNbMaterialProd(this.nbMaterialProd);
        ArrayList<InitialManipulationOutput> list = new ArrayList();
        for (int i=0; i<listOutput.size(); i++){
            list.add((InitialManipulationOutput)this.listOutput.get(i).clone());
        }
        a.setListOutput(list);
        return a;
    }

    @Override
    public Element toXML(){
        Element element = super.toXML(new Element(TAG_INITIAL_ACTION_MANIPULATION));
        element.addContent(new Element(TAG_INITIAL_ACTION_MANIPULATION_NB_MATERIAL_PROD).setText(Integer.toString(nbMaterialProd)));
        for (Iterator<InitialManipulationOutput> o = listOutput.iterator(); o.hasNext();) {
            element.addContent(o.next().toXML());
        }
        return element;
    }

    @Override
    public Element toXMLRef(){
        Element element = super.toXMLRef(new Element(TAG_INITIAL_ACTION_MANIPULATION_REF));
        return element;
    }

}
