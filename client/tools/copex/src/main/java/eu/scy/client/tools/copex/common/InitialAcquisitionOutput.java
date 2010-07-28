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
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.JDOMException;


/**
 * output d'une action acquisition
 * @author Marjolaine
 */
public class InitialAcquisitionOutput extends InitialOutput implements Cloneable {
    public final static String TAG_INITIAL_OUTPUT_ACQUISITION = "initial_acquisition_output";
    public final static String TAG_INITIAL_OUTPUT_ACQUISITION_REF = "initial_acquisition_output_ref";
    /* unites des data produites */
    private CopexUnit[] unitDataProd;

    public InitialAcquisitionOutput(long dbKey, List<LocalText>  listTextProd, List<LocalText>  listName, CopexUnit[] unitDataProd) {
        super(dbKey, listTextProd, listName);
        this.unitDataProd = unitDataProd ;
    }

    public InitialAcquisitionOutput(Element xmlElem, long idOutput, List<PhysicalQuantity> listPhysicalQuantity) throws JDOMException {
        super(xmlElem);
		if (xmlElem.getName().equals(TAG_INITIAL_OUTPUT_ACQUISITION)) {
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
			throw(new JDOMException("Initial acquisition output expects <"+TAG_INITIAL_OUTPUT_ACQUISITION+"> as root element, but found <"+xmlElem.getName()+">."));
		}
	}

    public InitialAcquisitionOutput(Element xmlElem, List<InitialAcquisitionOutput> list) throws JDOMException {
        super(xmlElem);
		if (xmlElem.getName().equals(TAG_INITIAL_OUTPUT_ACQUISITION_REF)) {
            this.code = xmlElem.getChild(TAG_INITIAL_OUTPUT_CODE).getText();
            for(Iterator<InitialAcquisitionOutput> a = list.iterator();a.hasNext();){
                InitialAcquisitionOutput p = a.next();
                if(p.getCode().equals(code)){
                    this.dbKey = p.getDbKey();
                    this.listName = p.getListName();
                    this.listTextProd = p.getListTextProd();
                    this.unitDataProd = p.getUnitDataProd();
                }
            }
        }else {
			throw(new JDOMException("Initial acquisition output expects <"+TAG_INITIAL_OUTPUT_ACQUISITION_REF+"> as root element, but found <"+xmlElem.getName()+">."));
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
        InitialAcquisitionOutput a = (InitialAcquisitionOutput) super.clone() ;

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
        Element el = new Element(TAG_INITIAL_OUTPUT_ACQUISITION);
        Element element = super.toXML(el);
        for(int i=0; i<unitDataProd.length; i++){
            element.addContent(unitDataProd[i].toXMLRef());
        }
		return element;
    }

    @Override
    public Element toXMLRef(){
        return super.toXMLRef(new Element(TAG_INITIAL_OUTPUT_ACQUISITION_REF));
    }
}
