/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import org.jdom.Element;
import org.jdom.JDOMException;

/**
 * general config copex
 * @author Marjolaine
 */
public class CopexConfig {
    public final static String TAG_COPEX = "copex";
    public final static String TAG_COPEX_QUANTITIES = "quantities";
    public final static String TAG_COPEX_STRATEGY_MATERIAL = "strategy_material";
    public final static String TAG_COPEX_MATERIALS = "materials";
    public final static String TAG_COPEX_PRESTRUCTURED_ACTIONS = "actions_prestructured";


    private List<PhysicalQuantity> listQuantities;
    private List<MaterialStrategy> listMaterialStrategy;
    private List<TypeMaterial> listTypeMaterial;
    private List<Material> listMaterial;
    private List<InitialNamedAction> listInitialNamedAction;

    public CopexConfig(List<PhysicalQuantity> listQuantities, List<MaterialStrategy> listMaterialStrategy,List<TypeMaterial> listTypeMaterial, List<Material> listMaterial, List<InitialNamedAction> listInitialNamedAction) {
        this.listQuantities = listQuantities;
        this.listMaterialStrategy = listMaterialStrategy;
        this.listTypeMaterial = listTypeMaterial;
        this.listMaterial = listMaterial;
        this.listInitialNamedAction = listInitialNamedAction;
    }

    public CopexConfig(Element xmlElem, Locale locale,  long idMaterialStrategy, long idPhysicalQuantity, long idUnit, long idTypeMaterial, long idMaterial, long idQuantity,long idAction, long idActionParam, long idOutput) throws JDOMException {
        if (xmlElem.getName().equals(TAG_COPEX)) {
            if (xmlElem.getChild(TAG_COPEX_QUANTITIES) != null){
                listQuantities = new LinkedList<PhysicalQuantity>();
                for (Iterator<Element> variablElem = xmlElem.getChild(TAG_COPEX_QUANTITIES).getChildren(PhysicalQuantity.TAG_QUANTITY).iterator(); variablElem.hasNext();) {
                    listQuantities.add(new PhysicalQuantity(variablElem.next(), idPhysicalQuantity++, idUnit));
                }
            }
            if (xmlElem.getChild(TAG_COPEX_STRATEGY_MATERIAL) != null){
                listMaterialStrategy = new LinkedList<MaterialStrategy>();
                for (Iterator<Element> variablElem = xmlElem.getChild(TAG_COPEX_STRATEGY_MATERIAL).getChildren(MaterialStrategy.TAG_MATERIAL_STRATEGY).iterator(); variablElem.hasNext();) {
                    listMaterialStrategy.add(new MaterialStrategy(variablElem.next(), idMaterialStrategy++));
                }
            }
            if (xmlElem.getChild(TAG_COPEX_MATERIALS) != null){
                listTypeMaterial = new LinkedList<TypeMaterial>();
                for (Iterator<Element> variablElem = xmlElem.getChild(TAG_COPEX_MATERIALS).getChildren(TypeMaterial.TAG_TYPE).iterator(); variablElem.hasNext();) {
                    listTypeMaterial.add(new TypeMaterial(variablElem.next(), idTypeMaterial++));
                }
                listMaterial = new LinkedList<Material>();
                for (Iterator<Element> variablElem = xmlElem.getChild(TAG_COPEX_MATERIALS).getChildren(Material.TAG_MATERIAL).iterator(); variablElem.hasNext();) {
                    listMaterial.add(new Material(variablElem.next(), idMaterial++, listTypeMaterial, listQuantities, idQuantity++));
                }
            }
            if(xmlElem.getChild(TAG_COPEX_PRESTRUCTURED_ACTIONS) != null){
                listInitialNamedAction = new LinkedList();
                for (Iterator<Element> variablElem = xmlElem.getChild(TAG_COPEX_PRESTRUCTURED_ACTIONS).getChildren(InitialNamedAction.TAG_INITIAL_NAMED_ACTION).iterator(); variablElem.hasNext();) {
                    listInitialNamedAction.add(new InitialNamedAction(variablElem.next(), idAction++, locale, idActionParam++, listQuantities, listTypeMaterial));
                }
                for (Iterator<Element> variablElem = xmlElem.getChild(TAG_COPEX_PRESTRUCTURED_ACTIONS).getChildren(InitialActionChoice.TAG_INITIAL_ACTION_CHOICE).iterator(); variablElem.hasNext();) {
                    listInitialNamedAction.add(new InitialActionChoice(variablElem.next(), idAction++, locale, idActionParam++, listQuantities, listTypeMaterial, idOutput++));
                }
                for (Iterator<Element> variablElem = xmlElem.getChild(TAG_COPEX_PRESTRUCTURED_ACTIONS).getChildren(InitialActionAcquisition.TAG_INITIAL_ACTION_ACQUISITION).iterator(); variablElem.hasNext();) {
                    listInitialNamedAction.add(new InitialActionAcquisition(variablElem.next(), idAction++, locale, idActionParam++, listQuantities, listTypeMaterial, idOutput++));
                }
                for (Iterator<Element> variablElem = xmlElem.getChild(TAG_COPEX_PRESTRUCTURED_ACTIONS).getChildren(InitialActionManipulation.TAG_INITIAL_ACTION_MANIPULATION).iterator(); variablElem.hasNext();) {
                    listInitialNamedAction.add(new InitialActionManipulation(variablElem.next(), idAction++, locale, idActionParam++, listQuantities, listTypeMaterial, idOutput++));
                }
                for (Iterator<Element> variablElem = xmlElem.getChild(TAG_COPEX_PRESTRUCTURED_ACTIONS).getChildren(InitialActionTreatment.TAG_INITIAL_ACTION_TREATMENT).iterator(); variablElem.hasNext();) {
                    listInitialNamedAction.add(new InitialActionTreatment(variablElem.next(), idAction++, locale, idActionParam++, listQuantities, listTypeMaterial, idOutput++));
                }
            }
	} else {
            throw(new JDOMException("Copex config expects <"+TAG_COPEX+"> as root element, but found <"+xmlElem.getName()+">."));
	}
    }


    public List<Material> getListMaterial() {
        return listMaterial;
    }

    public List<MaterialStrategy> getListMaterialStrategy() {
        return listMaterialStrategy;
    }
    public ArrayList<MaterialStrategy> getArrayListMaterialStrategy() {
        ArrayList<MaterialStrategy> l = new ArrayList();
        for(Iterator<MaterialStrategy> s = listMaterialStrategy.iterator();s.hasNext();){
            l.add(s.next());
        }
        return l;
    }

    public List<PhysicalQuantity> getListQuantities() {
        return listQuantities;
    }
    public ArrayList<PhysicalQuantity> getArrayListQuantities() {
        ArrayList<PhysicalQuantity> l = new ArrayList();
        for(Iterator<PhysicalQuantity> q = listQuantities.iterator();q.hasNext();){
            l.add(q.next());
        }
        return l;
    }

    public List<TypeMaterial> getListTypeMaterial() {
        return listTypeMaterial;
    }

    public List<InitialNamedAction> getListInitialNamedAction() {
        return listInitialNamedAction;
    }


    // toXML
    public Element toXML(){
        Element element = new Element(TAG_COPEX);
        Element e1 = new Element(TAG_COPEX_QUANTITIES);
        for (Iterator<PhysicalQuantity> m = listQuantities.iterator(); m.hasNext();) {
            e1.addContent(m.next().toXML());
        }
        element.addContent(e1);
        Element e2  = new Element(TAG_COPEX_STRATEGY_MATERIAL);
        for (Iterator<MaterialStrategy> m = listMaterialStrategy.iterator(); m.hasNext();) {
            e2.addContent(m.next().toXML());
        }
        element.addContent(e2);
        Element e3 = new Element(TAG_COPEX_MATERIALS);
        for (Iterator<TypeMaterial> m = listTypeMaterial.iterator(); m.hasNext();) {
            e3.addContent(m.next().toXML());
        }
        for (Iterator<Material> m = listMaterial.iterator(); m.hasNext();) {
            e3.addContent(m.next().toXML());
        }
        element.addContent(e3);
        Element e4 = new Element(TAG_COPEX_PRESTRUCTURED_ACTIONS);
        for (Iterator<TypeMaterial> m = listTypeMaterial.iterator(); m.hasNext();) {
            e4.addContent(m.next().toXML());
        }
        element.addContent(e4);
		return element;
    }

}
