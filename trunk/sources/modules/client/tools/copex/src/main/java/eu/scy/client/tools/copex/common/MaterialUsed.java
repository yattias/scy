/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.common;

import eu.scy.client.tools.copex.utilities.CopexUtilities;
import eu.scy.client.tools.copex.utilities.MyConstants;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.JDOMException;

/**
 * material used or not in a procedure by a learner
 * @author Marjolaine
 */
public class MaterialUsed implements Cloneable{
    public final static String TAG_MATERIAL_USED = "material_used";
    public final static String TAG_MATERIAL_USED_COMMENT = "comment";
    public final static String TAG_MATERIAL_USED_USED = "used";
    public final static String TAG_MATERIAL_USED_USERMATERIAL = "user_material";

    
    /* material */
    private Material material;
    /* comments */
    private List<LocalText> listComments;
    /* used or not */
    private boolean used;
   /* canbe remove : si materiel de l'etudiant et que aucune tache n'est liee a ce materiel */
    private boolean canBeRemove;
    /* use automatically by copex (=> used=true)*/
    private boolean autoUsed;

    public MaterialUsed(Material material, List<LocalText> listComments, boolean used) {
        this.material = material;
        this.listComments = listComments;
        this.used = used;
        this.canBeRemove = material.isUserMaterial();
        this.autoUsed = false;
    }


    public MaterialUsed(Material material, LocalText comment, boolean used) {
        this.material = material;
        this.listComments = new LinkedList();
        this.listComments.add(comment);
        this.used = used;
        this.canBeRemove = material.isUserMaterial();
        this.autoUsed = false;
    }

    public MaterialUsed(Element xmlElem, List<Material> listMaterial, long idMaterial, List<TypeMaterial> listTypeMaterial, List<PhysicalQuantity> listPhysicalQuantity, long idQuantity) throws JDOMException {
	if (xmlElem.getName().equals(TAG_MATERIAL_USED)) {
            if(xmlElem.getChild(Material.TAG_MATERIAL) != null)
                material = new Material(xmlElem.getChild(Material.TAG_MATERIAL), idMaterial, listTypeMaterial, listPhysicalQuantity, idQuantity);
            else if(xmlElem.getChild(Material.TAG_MATERIAL_REF) != null){
                material = new Material(xmlElem.getChild(Material.TAG_MATERIAL_REF), listMaterial);
            }
            used = xmlElem.getChild(TAG_MATERIAL_USED_USED).getText().equals(MyConstants.XML_BOOLEAN_TRUE);
            boolean userMat = true;
            if(xmlElem.getChild(TAG_MATERIAL_USED_USERMATERIAL) != null ){
                userMat = xmlElem.getChild(TAG_MATERIAL_USED_USERMATERIAL).getText().equals(MyConstants.XML_BOOLEAN_TRUE);
            }
            if(userMat)
                material.setMaterialSource(new MaterialSourceUser());
            listComments = new LinkedList<LocalText>();
            for (Iterator<Element> variableElem = xmlElem.getChildren(TAG_MATERIAL_USED_COMMENT).iterator(); variableElem.hasNext();) {
                Element e = variableElem.next();
                Locale l = new Locale(e.getAttribute(MyConstants.XMLNAME_LANGUAGE).getValue());
                listComments.add(new LocalText(e.getText(), l));
            }
            this.autoUsed = false;
	} else {
            throw(new JDOMException("Material used expects <"+TAG_MATERIAL_USED+"> as root element, but found <"+xmlElem.getName()+">."));
	}
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public List<LocalText> getListComments() {
        return listComments;
    }

    public void setListComments(List<LocalText> listComments) {
        this.listComments = listComments;
    }

    public String getComment(Locale locale){
        return CopexUtilities.getText(listComments, locale);
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    
    public boolean isCanBeRemove() {
        return canBeRemove;
    }

    public void setCanBeRemove(boolean canBeRemove) {
        this.canBeRemove = canBeRemove;
    }

    public void setComment(LocalText comment){
        int id = CopexUtilities.getIdText(comment.getLocale(), listComments);
        if(id ==-1){
            this.listComments.add(comment);
        }else{
            this.listComments.set(id, comment);
        }
    }

    public void setComment(String text){
        for(Iterator<LocalText> t = listComments.iterator();t.hasNext();){
            t.next().setText(text);
        }
    }

    public boolean isAutoUsed() {
        return autoUsed;
    }

    public void setAutoUsed(boolean autoUsed) {
        this.autoUsed = autoUsed;
    }

    @Override
    public Object clone() {
        try {
            MaterialUsed mUsed = (MaterialUsed) super.clone() ;
            Material m = (Material)this.material.clone() ;
            String c = null;
            List<LocalText> listCommentC = new LinkedList();
            for (Iterator<LocalText> t = listComments.iterator(); t.hasNext();) {
                listCommentC.add((LocalText)t.next().clone());
            }
            mUsed.setListComments(listCommentC);
            mUsed.setMaterial(m);
            mUsed.setUsed(new Boolean(used));
            mUsed.setCanBeRemove(new Boolean (canBeRemove));
            mUsed.setAutoUsed(new Boolean(autoUsed));
            return mUsed;
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError();
        }
    }

    // toXML
    public Element toXML(){
        Element element = new Element(TAG_MATERIAL_USED);
        if(material.isUserMaterial())
            element.addContent(material.toXML());
        else
            element.addContent(material.toXMLRef());
        element.addContent(new Element(TAG_MATERIAL_USED_USED).setText(used ? MyConstants.XML_BOOLEAN_TRUE : MyConstants.XML_BOOLEAN_FALSE));
        //element.addContent(new Element(TAG_MATERIAL_USED_USERMATERIAL).setText(userMaterial ? MyConstants.XML_BOOLEAN_TRUE : MyConstants.XML_BOOLEAN_FALSE));
        if(listComments != null && listComments.size() > 0){
            for (Iterator<LocalText> t = listComments.iterator(); t.hasNext();) {
                LocalText l = t.next();
                Element e = new Element(TAG_MATERIAL_USED_COMMENT);
                e.setText(l.getText());
                e.setAttribute(new Attribute(MyConstants.XMLNAME_LANGUAGE, l.getLocale().getLanguage()));
                element.addContent(e);
            }
        }
        return element;
    }

    public void setMaterialUsed(boolean b){
        if(b || (!b && this.autoUsed))
            this.setUsed(b);
        this.setAutoUsed(b);
    }

    public boolean isUserMaterial(){
        return material.isUserMaterial();
    }
}
