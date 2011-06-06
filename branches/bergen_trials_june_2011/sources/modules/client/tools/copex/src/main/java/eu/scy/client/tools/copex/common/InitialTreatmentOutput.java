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
 * output d'une action treatment
 * @author Marjolaine
 */
public class InitialTreatmentOutput extends InitialOutput implements Cloneable {
    public final static String TAG_INITIAL_OUTPUT_TREATMENT = "initial_treatment_output";
    public final static String TAG_INITIAL_OUTPUT_TREATMENT_REF = "initial_treatment_output_ref";
    /* unites des data produites */
    private CopexUnit[] unitDataProd;

    
    public InitialTreatmentOutput(long dbKey, List<LocalText>  listTextProd, List<LocalText>  listName, CopexUnit[] unitDataProd) {
        super(dbKey, listTextProd, listName);
        this.unitDataProd = unitDataProd ;
    }

    public InitialTreatmentOutput(Element xmlElem, long idOutput, List<PhysicalQuantity> listPhysicalQuantity) throws JDOMException {
        super(xmlElem);
		if (xmlElem.getName().equals(TAG_INITIAL_OUTPUT_TREATMENT)) {
            this.dbKey = idOutput;
            this.code = xmlElem.getChild(TAG_INITIAL_OUTPUT_CODE).getText();
            listName = new LinkedList<LocalText>();
            for (Iterator<Element> variableElem = xmlElem.getChildren(TAG_INITIAL_OUTPUT_PARAM_NAME).iterator(); variableElem.hasNext();) {
                Element e = variableElem.next();
                Locale l = new Locale(e.getAttribute(MyConstants.XMLNAME_LANGUAGE).getValue());
                listName.add(new LocalText(e.getText(), l));
            }
            listTextProd = new LinkedList<LocalText>();
            for (Iterator<Element> variableElem = xmlElem.getChildren(TAG_INITIAL_OUTPUT_TEXT_PROD).iterator(); variableElem.hasNext();) {
                Element e = variableElem.next();
                Locale l = new Locale(e.getAttribute(MyConstants.XMLNAME_LANGUAGE).getValue());
                listTextProd.add(new LocalText(e.getText(), l));
            }
            unitDataProd = new CopexUnit[xmlElem.getChildren(CopexUnit.TAG_UNIT_REF).size()];
            int i=0;
            for (Iterator<Element> variablElem = xmlElem.getChildren(CopexUnit.TAG_UNIT_REF).iterator(); variablElem.hasNext();) {
                unitDataProd[i++] =new CopexUnit(variablElem.next(),listPhysicalQuantity);
            }

		} else {
			throw(new JDOMException("Initial treatment output expects <"+TAG_INITIAL_OUTPUT_TREATMENT+"> as root element, but found <"+xmlElem.getName()+">."));
		}
	}


    public InitialTreatmentOutput(Element xmlElem, List<InitialTreatmentOutput> list) throws JDOMException {
        super(xmlElem);
		if (xmlElem.getName().equals(TAG_INITIAL_OUTPUT_TREATMENT_REF)) {
            this.code = xmlElem.getChild(TAG_INITIAL_OUTPUT_CODE).getText();
            for(Iterator<InitialTreatmentOutput> a = list.iterator();a.hasNext();){
                InitialTreatmentOutput p = a.next();
                if(p.getCode().equals(code)){
                    this.dbKey = p.getDbKey();
                    this.listName = p.getListName();
                    this.listTextProd = p.getListTextProd();
                    this.unitDataProd = p.getUnitDataProd();
                }
            }
        }else {
			throw(new JDOMException("Initial treatment output expects <"+TAG_INITIAL_OUTPUT_TREATMENT_REF+"> as root element, but found <"+xmlElem.getName()+">."));
		}
    }

    public CopexUnit[] getUnitDataProd() {
        return unitDataProd;
    }

    public void setUnitDataProd(CopexUnit[] unitDataProd) {
        this.unitDataProd = unitDataProd;
    }



    @Override
    protected Object clone(){
        InitialTreatmentOutput a = (InitialTreatmentOutput) super.clone() ;
        CopexUnit[] unitDataProdC = new CopexUnit[this.unitDataProd.length];
        for (int i=0; i<this.unitDataProd.length; i++){
            unitDataProdC[i] = (CopexUnit)this.unitDataProd[i].clone();
        }
        a.setUnitDataProd(unitDataProdC);
        return a;
    }

    // toXML
    @Override
    public Element toXML(){
        Element el = new Element(TAG_INITIAL_OUTPUT_TREATMENT);
        Element element = super.toXML(el);
        for(int i=0; i<unitDataProd.length; i++){
            element.addContent(unitDataProd[i].toXMLRef());
        }
		return element;
    }

    @Override
    public Element toXMLRef(){
        return super.toXMLRef(new Element(TAG_INITIAL_OUTPUT_TREATMENT_REF));
    }
}
