/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.common;

import eu.scy.client.tools.copex.utilities.CopexUtilities;
import eu.scy.client.tools.copex.utilities.MyConstants;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.JDOMException;

/**
 * represente le materiel
 * le materiel peut etre 
 * - lie a une proc initial : materiel disponible
 * - lie a un protocole eleve : materiel utilise
 * - lie a une action
 * les materiels sont regroupes par type de materiel
 * @author MBO
 */
public class Material implements Cloneable {
    /*tag names */
    public final static String TAG_MATERIAL = "material";
    public final static String TAG_MATERIAL_REF = "material_ref";
    private final static String TAG_MATERIAL_ID = "id";
    private final static String TAG_MATERIAL_NAME = "name";
    private final static String TAG_MATERIAL_DESCRIPTION = "info";

    /* cle BD */
    private long dbKey;
    private String code;
    /* nom du materiel */
    private List<LocalText> listName;
    /* description */
    private List<LocalText> listDescription;
    /* type de materiel */
    private List<TypeMaterial> listType;
    /* parametres */
    private List<Parameter> listParameters;

    

    // CONSTRUCTEURS
    public Material(long dbKey, List<LocalText> listName, List<LocalText> listDescription) {
        this.dbKey = dbKey;
        this.code = "";
        this.listName = listName;
        this.listDescription = listDescription;
        this.listType = new ArrayList();
        this.listParameters = new ArrayList();
    }

    public Material(long dbKey, String code, List<LocalText> listName, List<LocalText> listDescription, List<TypeMaterial> listType, List<Parameter> listParameters) {
        this.dbKey = dbKey;
        this.code = code;
        this.listName = listName;
        this.listDescription = listDescription;
        this.listType = listType;
        this.listParameters = listParameters;
    }

    public Material(List<LocalText> listName, List<LocalText> listDescription, List<TypeMaterial> listType, List<Parameter> listParameters){
        this.dbKey = -1;
        this.code = "";
        this.listName = listName;
        this.listDescription = listDescription;
        this.listType = listType;
        this.listParameters = listParameters;
    }


    public Material(Element xmlElem, long dbKey, List<TypeMaterial> listTypeMaterial, List<PhysicalQuantity> listPhysicalQuantity, long idQuantity) throws JDOMException {
		if (xmlElem.getName().equals(TAG_MATERIAL)) {
			code = xmlElem.getChild(TAG_MATERIAL_ID).getText();
            this.dbKey = dbKey;
            listName = new LinkedList<LocalText>();
            for (Iterator<Element> variableElem = xmlElem.getChildren(TAG_MATERIAL_NAME).iterator(); variableElem.hasNext();) {
                Element e = variableElem.next();
                Locale l = new Locale(e.getAttribute(MyConstants.XMLNAME_LANGUAGE).getValue());
                listName.add(new LocalText(e.getText(), l));
            }
            listDescription = new LinkedList<LocalText>();
            for (Iterator<Element> variableElem = xmlElem.getChildren(TAG_MATERIAL_DESCRIPTION).iterator(); variableElem.hasNext();) {
                Element e = variableElem.next();
                Locale l = new Locale(e.getAttribute(MyConstants.XMLNAME_LANGUAGE).getValue());
                listDescription.add(new LocalText(e.getText(), l));
            }
            listType = new LinkedList<TypeMaterial>();
			for (Iterator<Element> variableElem = xmlElem.getChildren(TypeMaterial.TAG_TYPE_REF).iterator(); variableElem.hasNext();) {
				listType.add(new TypeMaterial(variableElem.next(), listTypeMaterial));
			}
            listParameters = new LinkedList<Parameter>();
			for (Iterator<Element> variableElem = xmlElem.getChildren(Parameter.TAG_PARAMETER).iterator(); variableElem.hasNext();) {
				listParameters.add(new Parameter(variableElem.next(), idQuantity++, listPhysicalQuantity));
			}
		} else {
			throw(new JDOMException("Material expects <"+TAG_MATERIAL+"> as root element, but found <"+xmlElem.getName()+">."));
		}
	}

    public Material(Element xmlElem, List<Material> list) throws JDOMException {
        if (xmlElem.getName().equals(TAG_MATERIAL_REF)) {
			code = xmlElem.getChild(TAG_MATERIAL_ID).getText();
            dbKey = -1;
            for(Iterator<Material> m = list.iterator();m.hasNext();){
                Material material = m.next();
                if(material.getCode().equals(code)){
                    this.dbKey = material.getDbKey();
                    this.listName = material.getListName();
                    this.listDescription = material.getListDescription();
                    this.listType = material.getListType();
                    this.listParameters = material.getListParameters();
                }
            }
        }else {
			throw(new JDOMException("Material expects <"+TAG_MATERIAL+"> as root element, but found <"+xmlElem.getName()+">."));
		}
    }

    // GETTER AND SETTER
    
    public long getDbKey() {
        return dbKey;
    }

    public void setDbKey(long dbKey) {
        this.dbKey = dbKey;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<LocalText> getListDescription() {
        return listDescription;
    }

    public void setListDescription(List<LocalText> listDescription) {
        this.listDescription = listDescription;
    }

    public void setDescription(LocalText text){
        int id = CopexUtilities.getIdText(text.getLocale(), listDescription);
        if(id ==-1){
            this.listDescription.add(text);
        }else{
            this.listDescription.set(id, text);
        }
    }

     public void setDescription(String text){
        for(Iterator<LocalText> t = listDescription.iterator();t.hasNext();){
            t.next().setText(text);
        }
    }

    public List<LocalText> getListName() {
        return listName;
    }

    public void setListName(List<LocalText> listName) {
        this.listName = listName;
    }

    public List<Parameter> getListParameters() {
        return listParameters;
    }

    public void setListParameters(List<Parameter> listParameters) {
        this.listParameters = listParameters;
    }

    public List<TypeMaterial> getListType() {
        return listType;
    }

    public void setListType(List<TypeMaterial> listType) {
        this.listType = listType;
    }

    public String getName(Locale locale){
        return CopexUtilities.getText(listName, locale);
    }

    public String getDescription(Locale locale){
        return CopexUtilities.getText(listDescription, locale);
    }
    
    // METHODES
    /* rend la chaine a afficher sur l'ihm */
//    public String toDisplay(){
//        String s = getName()+" (";
//        s += getTypeToDisplay();
//        s += ")";
//        return s;
//
//    }

    public String toDisplay(Locale locale){
        String s = CopexUtilities.getText(listName, locale);
        return s;

    }
    /* rend la chaine a afficher sur l'ihm : liste des types */
    public String getTypeToDisplay(Locale locale){
        String s = "";
        int nb = listType.size();
        for (int i=0; i<nb; i++){
            if (i>0)
                s += " - ";
            s += listType.get(i).toString(locale);
        }
        return s;
        
    }
    /* affichage des parametres */
    public String getParametersToDisplay(Locale locale){
        String s = "";
        int nb = listParameters.size();
        for(int i=0; i<nb; i++){
            Parameter param = listParameters.get(i);
            s+= param.toDisplay(locale);
            if(i<nb-1)
                s+="\n";
        }
        return s;
    }


    /* ajout d'un type */
    public void addType(TypeMaterial type){
        this.listType.add(type);
    }
    
    
    /* ajout d'un parametre */
    public void addParam(Parameter p){
        this.listParameters.add(p);
    }
    
    /* retourn vrai si le materiel est de ce type*/
    public boolean isType(TypeMaterial t){
        int n = this.listType.size();
        for (int i=0; i<n; i++){
            if (t.getDbKey() == this.listType.get(i).getDbKey())
                return true;
        }
        return false;
    }
    
    
    
    @Override
    public Object clone()  {
        try {
            Material material = (Material) super.clone() ;
            long dbKeyC = this.dbKey;
            material.setDbKey(dbKeyC);
            material.setCode(new String(code));
            List<LocalText> listNameC = new LinkedList();
            for (Iterator<LocalText> t = listName.iterator(); t.hasNext();) {
                listNameC.add((LocalText)t.next().clone());
            }
            material.setListName(listNameC);
            List<LocalText> listDescriptionC = new LinkedList();
            for (Iterator<LocalText> t = listDescription.iterator(); t.hasNext();) {
                listDescriptionC.add((LocalText)t.next().clone());
            }
            material.setListDescription(listDescriptionC);
            ArrayList<TypeMaterial>  listT = null;
            if (this.listType != null){
                listT = new ArrayList();
                int nbT = this.listType.size();
                for (int i=0; i<nbT; i++){
                    listT.add((TypeMaterial)this.listType.get(i).clone());
                }
            }
            ArrayList<Parameter>  listP = null;
            if (this.listParameters != null){
                listP = new ArrayList();
                int nbP =  this.listParameters.size();
                for (int i=0; i<nbP; i++){
                    listP.add((Parameter)this.listParameters.get(i).clone());
                }
            }
            
            material.setListType(listT);
            material.setListParameters(listP);
            
            return material;
        } catch (CloneNotSupportedException e) { 
	    // this shouldn't happen, since we are Cloneable
	    throw new InternalError();
	}
    }
    
    /* renvoit true si le material a ce type */
    public boolean isType(long dbKeyType){
        int nbType = this.listType.size();
        for (int i=0; i<nbType; i++){
            if (this.listType.get(i).getDbKey() == dbKeyType){
                return true;
            }
        }
        return false;
    }

    // toXML
    public Element toXML(){
        Element element = new Element(TAG_MATERIAL);
		element.addContent(new Element(TAG_MATERIAL_ID).setText(code));
        if(listName != null && listName.size() > 0){
            for (Iterator<LocalText> t = listName.iterator(); t.hasNext();) {
                LocalText l = t.next();
                Element e = new Element(TAG_MATERIAL_NAME);
                e.setText(l.getText());
                e.setAttribute(new Attribute(MyConstants.XMLNAME_LANGUAGE, l.getLocale().getLanguage()));
                element.addContent(e);
            }
        }
        if(listDescription != null && listDescription.size() > 0){
            for (Iterator<LocalText> t = listDescription.iterator(); t.hasNext();) {
                LocalText l = t.next();
                Element e = new Element(TAG_MATERIAL_DESCRIPTION);
                e.setText(l.getText());
                e.setAttribute(new Attribute(MyConstants.XMLNAME_LANGUAGE, l.getLocale().getLanguage()));
                element.addContent(e);
            }
        }
        for (Iterator<TypeMaterial> type = listType.iterator(); type.hasNext();) {
				element.addContent(type.next().toXMLRef());
		}
        if (listParameters != null){
            for (Iterator<Parameter> p = listParameters.iterator(); p.hasNext();) {
				element.addContent(p.next().toXML());
            }
        }
		return element;
    }

    public Element toXMLRef(){
        Element element = new Element(TAG_MATERIAL_REF);
		element.addContent(new Element(TAG_MATERIAL_ID).setText(code));
        return element;
    }
    
}
