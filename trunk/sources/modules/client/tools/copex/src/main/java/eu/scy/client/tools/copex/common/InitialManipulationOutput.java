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
 * output for an action - manipulation
 * @author Marjolaine
 */
public class InitialManipulationOutput extends InitialOutput implements Cloneable{
    public final static String TAG_INITIAL_OUTPUT_MANIPULATION = "initial_manipulation_output";
    public final static String TAG_INITIAL_OUTPUT_MANIPULATION_REF = "initial_manipulation_output_ref";
    
    /* list of type of material expected */
    private ArrayList<TypeMaterial> typeMaterialProd ;

    public InitialManipulationOutput(long dbKey, List<LocalText>  listTextProd, List<LocalText>  listName, ArrayList<TypeMaterial> typeMaterialProd) {
        super(dbKey, listTextProd, listName);
        this.typeMaterialProd = typeMaterialProd;
    }

    public InitialManipulationOutput(Element xmlElem, long idOutput, List<TypeMaterial> listTypeMaterial) throws JDOMException {
        super(xmlElem);
	if (xmlElem.getName().equals(TAG_INITIAL_OUTPUT_MANIPULATION)) {
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
            typeMaterialProd = new ArrayList();
            if (xmlElem.getChild(TypeMaterial.TAG_TYPE_REF) != null){
                for (Iterator<Element> variablElem = xmlElem.getChildren(TypeMaterial.TAG_TYPE_REF).iterator(); variablElem.hasNext();) {
                    typeMaterialProd.add(new TypeMaterial(variablElem.next(), listTypeMaterial));
                }
            }

	} else {
        	throw(new JDOMException("Initial manipulation output expects <"+TAG_INITIAL_OUTPUT_MANIPULATION+"> as root element, but found <"+xmlElem.getName()+">."));
	}
    }

    public InitialManipulationOutput(Element xmlElem, List<InitialManipulationOutput> list) throws JDOMException {
        super(xmlElem);
	if (xmlElem.getName().equals(TAG_INITIAL_OUTPUT_MANIPULATION_REF)) {
            this.code = xmlElem.getChild(TAG_INITIAL_OUTPUT_CODE).getText();
            for(Iterator<InitialManipulationOutput> a = list.iterator();a.hasNext();){
                InitialManipulationOutput p = a.next();
                if(p.getCode().equals(code)){
                    this.dbKey = p.getDbKey();
                    this.listName = p.getListName();
                    this.listTextProd = p.getListTextProd();
                    this.typeMaterialProd = p.getTypeMaterialProd();
                }
            }
        }else {
            throw(new JDOMException("Initial manipulation output expects <"+TAG_INITIAL_OUTPUT_MANIPULATION_REF+"> as root element, but found <"+xmlElem.getName()+">."));
	}
    }

    public ArrayList<TypeMaterial> getTypeMaterialProd() {
        return typeMaterialProd;
    }

    public void setTypeMaterialProd(ArrayList<TypeMaterial> typeMaterialProd) {
        this.typeMaterialProd = typeMaterialProd;
    }

    @Override
    protected Object clone(){
        InitialManipulationOutput a = (InitialManipulationOutput) super.clone() ;

        ArrayList<TypeMaterial> typeMaterialProdC = new ArrayList();
        for (int i=0; i<typeMaterialProd.size(); i++){
            typeMaterialProdC.add((TypeMaterial)this.typeMaterialProd.get(i).clone());
        }
        a.setTypeMaterialProd(typeMaterialProdC);

        return a;
    }

    public boolean canAccept(Material m){
        int nb = this.typeMaterialProd.size();
        for (int i=0; i<nb; i++){
            if(m.isType(typeMaterialProd.get(i)))
                return true;
        }
        return false;
    }

    // toXML
    @Override
    public Element toXML(){
        Element el = new Element(TAG_INITIAL_OUTPUT_MANIPULATION);
        Element element = super.toXML(el);
        if (typeMaterialProd != null){
            for (Iterator<TypeMaterial> u = typeMaterialProd.iterator(); u.hasNext();) {
                element.addContent(u.next().toXMLRef());
            }
        }
	return element;
    }

    @Override
    public Element toXMLRef(){
        return super.toXMLRef(new Element(TAG_INITIAL_OUTPUT_MANIPULATION_REF));
    }

}
