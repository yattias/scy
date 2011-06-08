/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.common;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import org.jdom.Element;
import org.jdom.JDOMException;


/**
 * labbook mission
 * @author marjolaine
 */
public class CopexMission implements Cloneable{
    
    /** tag names */
    public final static String TAG_MISSION = "mission";
    
    
    /** db id */
    private long dbKey;
    /** mission name */
    private String name;
    /** code of the mission */
    private String code;
    /** description of the mission*/
    private String description;
    /** current mission */
    private boolean activ;
   

    private List<InitialProcedure> listInitialProc;

    private List<TypeMaterial> listType;
    private List<Material> listMaterial;
    private List<InitialNamedAction> listNamedAction;

    public CopexMission(long dbKey, String name, String code, String description){
        this.dbKey = dbKey;
        this.name = name;
        this.code = code;
        this.description = description;
        this.activ = true;
    }

    public CopexMission(String code, String name, String description){
       this.name = name;
       this.code = code;
       this.description = description;
       this.dbKey = -1;
       this.activ = true;
       this.listInitialProc = null;
    }

    public CopexMission(Element xmlElem, Locale locale,long dbKeyMission,long idProc, long idRepeat, long idParam, long idValue, long idAction, long idActionParam, long idQuantity,
            long idMaterial, long idTask,long idHypothesis, long idPrinciple,long idEvaluation,long idTypeMaterial, long idInitialAction,long idOutput,
            List<Material> listMat,  List<TypeMaterial> listTypeMaterial, List<PhysicalQuantity> listPhysicalQuantity, List<InitialNamedAction> listIAction, List<MaterialStrategy> listMaterialStrategy) throws JDOMException {
        if (xmlElem.getName().equals(TAG_MISSION)) {
            // type de material
            for (Iterator<Element> variableElem = xmlElem.getChildren(TypeMaterial.TAG_TYPE).iterator(); variableElem.hasNext();) {
                TypeMaterial type = new TypeMaterial(variableElem.next(), idTypeMaterial++);
                listTypeMaterial.add(type);
            }
            listType = listTypeMaterial;
            // material
            for (Iterator<Element> variableElem = xmlElem.getChildren(Material.TAG_MATERIAL).iterator(); variableElem.hasNext();) {
                Material material = new Material(variableElem.next(), idMaterial++, listTypeMaterial,  listPhysicalQuantity, idQuantity++);
                listMat.add(material);
            }
            listMaterial = listMat;
            // named actions
            for (Iterator<Element> variableElem = xmlElem.getChildren(InitialNamedAction.TAG_INITIAL_NAMED_ACTION).iterator(); variableElem.hasNext();) {
                InitialNamedAction initialNamedAction = new InitialNamedAction(variableElem.next(),idInitialAction++, locale, idActionParam++, listPhysicalQuantity, listTypeMaterial );
                listIAction.add(initialNamedAction);
            }
            listNamedAction = listIAction;
            // initial proc
            listInitialProc = new LinkedList<InitialProcedure>();
            
            for (Iterator<Element> variableElem = xmlElem.getChildren(InitialProcedure.TAG_INITIAL_PROC).iterator(); variableElem.hasNext();) {
                InitialProcedure initProc = new InitialProcedure(variableElem.next(),locale,idProc++,idRepeat++,idParam++,idValue++,idAction++,idActionParam++,idQuantity++,
                    idMaterial++, idTask++,idHypothesis++,idPrinciple++,idEvaluation++,idOutput++,
                    listMaterial, listTypeMaterial, listPhysicalQuantity, listIAction, listMaterialStrategy);
                listInitialProc.add(initProc);
            }

            this.name = "";
            this.code = "";
            this.description = "";
            this.dbKey = dbKeyMission;
            this.activ = true;
        } else {
            throw(new JDOMException("Mission expects <"+TAG_MISSION+"> as root element, but found <"+xmlElem.getName()+">."));
       }
    }

    public List<InitialProcedure> getListInitialProc() {
        return listInitialProc;
    }

    public void setListInitialProc(List<InitialProcedure> listInitialProc) {
        this.listInitialProc = listInitialProc;
    }

    public List<Material> getListMaterial() {
        return listMaterial;
    }

    public void setListMaterial(List<Material> listMaterial) {
        this.listMaterial = listMaterial;
    }

    public List<InitialNamedAction> getListNamedAction() {
        return listNamedAction;
    }

    public void setListNamedAction(List<InitialNamedAction> listNamedAction) {
        this.listNamedAction = listNamedAction;
    }

    

    public List<TypeMaterial> getListType() {
        return listType;
    }

    public void setListType(List<TypeMaterial> listType) {
        this.listType = listType;
    }
   
     @Override
    public Object clone()  {
        try {
            CopexMission mission = (CopexMission) super.clone() ;
            long dbKeyC = this.dbKey;
            String nameC = new String(this.name);
            String codeC = "";
            if (this.code != null)
                codeC = new String(this.code);
            String descC = "";
            if (this.description != null)
                descC = new String(description);
            boolean activC = this.activ;
            List<InitialProcedure> listIP = null;
            if(listInitialProc != null){
                listIP = new LinkedList();
                for (Iterator<InitialProcedure> p = listInitialProc.iterator(); p.hasNext();) {
                    listIP.add((InitialProcedure)p.next().clone());
                }
            }
            List<TypeMaterial> listTypeMaterialC= null;
            if(listType != null){
                listTypeMaterialC = new LinkedList();
                for (Iterator<TypeMaterial> p = listType.iterator(); p.hasNext();) {
                    listTypeMaterialC.add((TypeMaterial)p.next().clone());
                }
            }
            mission.setListType(listTypeMaterialC);

            List<Material> listMaterialC= null;
            if(listMaterial != null){
                listMaterialC = new LinkedList();
                for (Iterator<Material> p = listMaterial.iterator(); p.hasNext();) {
                    listMaterialC.add((Material)p.next().clone());
                }
            }
            mission.setListMaterial(listMaterialC);

            mission.setDbKey(dbKeyC);
            mission.setName(nameC);
            mission.setCode(codeC);
            mission.setDescription(descC);
            mission.setActiv(activC);
            mission.setListInitialProc(listIP);
            return mission;
        } catch (CloneNotSupportedException e) { 
	    // this shouldn't happen, since we are Cloneable
	    throw new InternalError();
	}
    }

     
     public String getName() {
        return name;
    }

    public long getDbKey() {
        return dbKey;
    }

   
    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public void setActiv(boolean activ) {
        this.activ = activ;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDbKey(long dbKey) {
        this.dbKey = dbKey;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    
    public void setName(String name) {
        this.name = name;
    }

    
     // toXML
    public Element toXML(){
        Element element = new Element(TAG_MISSION);
        if(listType != null){
            for (Iterator<TypeMaterial> t = listType.iterator(); t.hasNext();) {
                element.addContent(t.next().toXML());
            }
        }
        if(listInitialProc != null){
            for (Iterator<InitialProcedure> p = listInitialProc.iterator(); p.hasNext();) {
                element.addContent(p.next().toXML());
            }
        }
	return element;
    }


}
