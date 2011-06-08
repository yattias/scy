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
 * initial parameter for an initial action - type: material
 * @author Marjolaine
 */
public class InitialParamMaterial extends InitialActionParam {
    public final static String TAG_INITIAL_PARAM_MATERIAL = "initial_param_material";
    public final static String TAG_INITIAL_PARAM_MATERIAL_REF = "initial_param_material_ref";
    public final static String TAG_INITIAL_PARAM_MATERIAL_ANDTYPE = "and_type";
    public final static String TAG_INITIAL_PARAM_MATERIAL_ALLTYPE = "allType";
    public final static String TAG_INITIAL_PARAM_MATERIAL_MAT2 = "type2";

    /* material type*/
    private TypeMaterial typeMaterial;
    /* possibly type of material 2 */
    private TypeMaterial typeMaterial2;
    /* material1 &/or material2?*/
    private boolean andTypes;
    /* all type in the procedure*/
    private boolean allType;
    /* possibly, quantity param*/
    private InitialParamQuantity paramQuantity ;

    public InitialParamMaterial(long dbKey, List<LocalText>  listParamName, TypeMaterial typeMaterial, TypeMaterial typeMaterial2, boolean andTypes, InitialParamQuantity paramQuantity, boolean allType) {
        super(dbKey, listParamName);
        this.typeMaterial = typeMaterial;
        this.typeMaterial2 = typeMaterial2;
        this.andTypes =andTypes ;
        this.paramQuantity = paramQuantity ;
        this.allType = allType ;
    }

    public InitialParamMaterial(Element xmlElem, long idActionParam, List<TypeMaterial> listTypeMaterial, List<InitialParamQuantity> listParamQuantity) throws JDOMException {
        super(xmlElem);
	if (xmlElem.getName().equals(TAG_INITIAL_PARAM_MATERIAL)) {
            //this.dbKey = idActionParam;
            this.code = xmlElem.getChild(TAG_INITIAL_ACTION_PARAM_CODE).getText();
            this.dbKey = Long.parseLong(code);
            listParamName = new LinkedList<LocalText>();
            for (Iterator<Element> variableElem = xmlElem.getChildren(TAG_INITIAL_ACTION_PARAM_NAME).iterator(); variableElem.hasNext();) {
                Element e = variableElem.next();
                Locale l = new Locale(e.getAttribute(MyConstants.XMLNAME_LANGUAGE).getValue());
                listParamName.add(new LocalText(e.getText(), l));
            }
            andTypes = xmlElem.getChild(TAG_INITIAL_PARAM_MATERIAL_ANDTYPE).getText().equals(MyConstants.XML_BOOLEAN_TRUE);
            allType = xmlElem.getChild(TAG_INITIAL_PARAM_MATERIAL_ALLTYPE).getText().equals(MyConstants.XML_BOOLEAN_TRUE);
            typeMaterial = null;
            if(xmlElem.getChild(TypeMaterial.TAG_TYPE_REF) != null)
                typeMaterial = new TypeMaterial(xmlElem.getChild(TypeMaterial.TAG_TYPE_REF), listTypeMaterial);
            if(xmlElem.getChild(TAG_INITIAL_PARAM_MATERIAL_MAT2) != null){
                typeMaterial2 = new TypeMaterial(xmlElem.getChild(TAG_INITIAL_PARAM_MATERIAL_MAT2).getChild(TypeMaterial.TAG_TYPE_REF), listTypeMaterial);
            }
            if(xmlElem.getChild(InitialParamQuantity.TAG_INITIAL_PARAM_QUANTITY_REF) != null){
                paramQuantity = new InitialParamQuantity(xmlElem, listParamQuantity);
            }

	} else {
            throw(new JDOMException("Initial action param material expects <"+TAG_INITIAL_PARAM_MATERIAL+"> as root element, but found <"+xmlElem.getName()+">."));
	}
    }

    public InitialParamMaterial(Element xmlElem, List<InitialParamMaterial> list) throws JDOMException {
        super(xmlElem);
	if (xmlElem.getName().equals(TAG_INITIAL_PARAM_MATERIAL_REF)) {
            this.code = xmlElem.getChild(TAG_INITIAL_ACTION_PARAM_CODE).getText();
            for(Iterator<InitialParamMaterial> a = list.iterator();a.hasNext();){
                InitialParamMaterial p = a.next();
                if(p.getCode().equals(code)){
                    this.dbKey = p.getDbKey();
                    this.listParamName = p.getListParamName();
                    this.typeMaterial = p.getTypeMaterial();
                    this.typeMaterial2 = p.getTypeMaterial2();
                    this.andTypes = p.isAndTypes();
                    this.allType = p.isAllType();
                    this.paramQuantity = p.getParamQuantity();
                }
            }
        }else {
            throw(new JDOMException("Initial action param quantity expects <"+TAG_INITIAL_PARAM_MATERIAL_REF+"> as root element, but found <"+xmlElem.getName()+">."));
	}
    }

    public TypeMaterial getTypeMaterial() {
        return typeMaterial;
    }

    public void setTypeMaterial(TypeMaterial typeMaterial) {
        this.typeMaterial = typeMaterial;
    }

    public InitialParamQuantity getParamQuantity() {
        return paramQuantity;
    }

    public void setParamQuantity(InitialParamQuantity paramQuantity) {
        this.paramQuantity = paramQuantity;
    }

    public TypeMaterial getTypeMaterial2() {
        return typeMaterial2;
    }

    public void setTypeMaterial2(TypeMaterial typeMaterial2) {
        this.typeMaterial2 = typeMaterial2;
    }

    public boolean isAndTypes() {
        return andTypes;
    }

    public void setAndTypes(boolean andTypes) {
        this.andTypes = andTypes;
    }

    public boolean isAllType() {
        return allType;
    }

    public void setAllType(boolean allType) {
        this.allType = allType;
    }

  
    public boolean canAccept(Material m){
        if(allType)
            return true;
        boolean controlType2 = typeMaterial2 != null;
        long dbKeyType2 = -1;
        if (controlType2)
            dbKeyType2 = typeMaterial2.getDbKey();
        if (m.isType(typeMaterial.getDbKey())){
                if ((controlType2 && m.isType(dbKeyType2)&& andTypes) || !controlType2 || (controlType2 && !andTypes) )
                    return true;
         }else  if(controlType2 && !andTypes && m.isType(dbKeyType2)){
                return true;
         }
        return false;
    }

    @Override
    public Object clone() {
        InitialParamMaterial p = (InitialParamMaterial) super.clone() ;

        if(typeMaterial == null)
            p.setTypeMaterial(null);
        else
            p.setTypeMaterial((TypeMaterial)this.getTypeMaterial().clone());
        if (paramQuantity == null)
            p.setParamQuantity(null);
        else
            p.setParamQuantity((InitialParamQuantity)this.paramQuantity.clone());
        if (this.typeMaterial2 == null)
            p.setTypeMaterial2(null);
        else
            p.setTypeMaterial2((TypeMaterial)this.getTypeMaterial2().clone());
        p.setAndTypes(this.andTypes);
        return p;
    }

    // toXML
    @Override
    public Element toXML(){
        Element el = new Element(TAG_INITIAL_PARAM_MATERIAL);
        Element element  = super.toXML(el);
        element.addContent(new Element(TAG_INITIAL_PARAM_MATERIAL_ANDTYPE).setText(andTypes ? MyConstants.XML_BOOLEAN_TRUE : MyConstants.XML_BOOLEAN_FALSE));
        element.addContent(new Element(TAG_INITIAL_PARAM_MATERIAL_ALLTYPE).setText(allType ? MyConstants.XML_BOOLEAN_TRUE : MyConstants.XML_BOOLEAN_FALSE));
        if(typeMaterial != null)
            element.addContent(typeMaterial.toXMLRef());
        if(typeMaterial2 != null){
            Element e = new Element(TAG_INITIAL_PARAM_MATERIAL_MAT2);
            e.addContent(typeMaterial2.toXMLRef());
            element.addContent(e);
        }
        if(paramQuantity != null){
            element.addContent(paramQuantity.toXMLRef());
        }
	return element;
    }

    @Override
    public Element toXMLRef(){
        return super.toXMLRef(new Element(TAG_INITIAL_PARAM_MATERIAL_REF));
    }

}
