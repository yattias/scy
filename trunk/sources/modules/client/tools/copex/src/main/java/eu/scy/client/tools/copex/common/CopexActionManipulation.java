/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.common;

import eu.scy.client.tools.copex.main.CopexPanel;
import java.util.ArrayList;
import eu.scy.client.tools.copex.utilities.MyConstants;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.JDOMException;

/**
 *  action - manipulation type
 * @author Marjolaine
 */
public class CopexActionManipulation extends CopexActionParam implements Cloneable{
    public final static String TAG_ACTION_MANIPULATION = "action_manipulation";
    public final static String TAG_ACTION_MANIPULATION_LIST_MAT_PROD = "list_material_prod";


    /* list of material prod. : Material or Material list */
    private ArrayList<Object> listMaterialProd;

    public CopexActionManipulation(List<LocalText> listDescription, List<LocalText> listComments, InitialNamedAction namedAction, Object[] tabParam, ArrayList<Object> listMaterialProd) {
        super(listDescription, listComments, namedAction, tabParam);
        this.listMaterialProd = listMaterialProd;
    }

    public CopexActionManipulation(long dbKey, List<LocalText> listName, List<LocalText> listDescription, List<LocalText> listComments, String taskImage,Element draw,  boolean isVisible, TaskRight taskRight, long dbkeyBrother, long dbKeyChild, InitialNamedAction namedAction, Object[] tabParam, ArrayList<Object> listMaterialProd, TaskRepeat taskRepeat) {
        super(dbKey, listName, listDescription, listComments, taskImage, draw, isVisible, taskRight, dbkeyBrother, dbKeyChild, namedAction, tabParam, taskRepeat);
        this.listMaterialProd = listMaterialProd;
    }

    public CopexActionManipulation(long dbKey, List<LocalText> listName, List<LocalText> listDescription, List<LocalText> listComments, String taskImage,Element draw,  boolean isVisible, TaskRight taskRight, InitialNamedAction namedAction, Object[] tabParam, ArrayList<Object> listMaterialProd, TaskRepeat taskRepeat) {
        super(dbKey, listName, listDescription, listComments, taskImage, draw, isVisible, taskRight, namedAction, tabParam, taskRepeat);
        this.listMaterialProd = listMaterialProd;
    }

    public CopexActionManipulation(long dbKey, Locale locale, String name, String description, String comment, String taskImage,Element draw,  boolean isVisible, TaskRight taskRight, InitialNamedAction namedAction, Object[] tabParam, ArrayList<Object> listMaterialProd, TaskRepeat taskRepeat) {
        super(dbKey, locale, name, description, comment, taskImage, draw, isVisible, taskRight, namedAction, tabParam, taskRepeat);
        this.listMaterialProd = listMaterialProd;
    }

    public CopexActionManipulation(Element xmlElem, long idTask, List<InitialNamedAction> listInitialNamedAction,long idMaterial, long idRepeat, long idParam, long idValue, long idActionParam, long idQuantity, List<PhysicalQuantity> listPhysicalQuantity, List<TypeMaterial> listTypeMaterial, List<InitialParamData> listInitialParamData, List<InitialParamMaterial> listInitialParamMaterial, List<InitialParamQuantity> listInitialParamQuantity, List<InitialAcquisitionOutput> listInitialAcquisitionOutput, List<InitialManipulationOutput> listInitialManipulationOutput, List<InitialTreatmentOutput> listInitialTreatmentOutput, List<Material> listMaterial, List<ActionParamQuantity> listActionParamQuantity) throws JDOMException {
        super(xmlElem);
        this.dbKeyBrother = -1;
        this.dbKeyChild = -1;
        if (xmlElem.getName().equals(TAG_ACTION_MANIPULATION)) {
            dbKey = idTask;
            listName = new LinkedList<LocalText>();
            for (Iterator<Element> variableElem = xmlElem.getChildren(TAG_TASK_NAME).iterator(); variableElem.hasNext();) {
                Element e = variableElem.next();
                Locale l = new Locale(e.getAttribute(MyConstants.XMLNAME_LANGUAGE).getValue());
                listName.add(new LocalText(e.getText(), l));
            }
            listDescription = new LinkedList<LocalText>();
            for (Iterator<Element> variableElem = xmlElem.getChildren(TAG_TASK_DESCRIPTION).iterator(); variableElem.hasNext();) {
                Element e = variableElem.next();
                Locale l = new Locale(e.getAttribute(MyConstants.XMLNAME_LANGUAGE).getValue());
                listDescription.add(new LocalText(e.getText(), l));
            }
            listComments = new LinkedList<LocalText>();
            for (Iterator<Element> variableElem = xmlElem.getChildren(TAG_TASK_COMMENTS).iterator(); variableElem.hasNext();) {
                Element e = variableElem.next();
                Locale l = new Locale(e.getAttribute(MyConstants.XMLNAME_LANGUAGE).getValue());
                listComments.add(new LocalText(e.getText(), l));
            }
            taskImage = xmlElem.getChildText(TAG_TASK_IMAGE);
            draw = xmlElem.getChild(TAG_TASK_DRAW);
            taskRight = new TaskRight(xmlElem.getChild(TaskRight.TAG_TASK_RIGHT));
            if(xmlElem.getChild(TaskRepeat.TAG_REPEAT_TASK) != null){
                taskRepeat = new TaskRepeat(xmlElem.getChild(TaskRepeat.TAG_REPEAT_TASK), idRepeat++, idParam++, idValue++, idActionParam++, idQuantity++,  listPhysicalQuantity, listTypeMaterial, listInitialParamData, listInitialParamMaterial, listInitialParamQuantity, listInitialAcquisitionOutput, listInitialManipulationOutput, listInitialTreatmentOutput, listMaterial, listActionParamQuantity);
            }
            namedAction = new InitialActionManipulation(xmlElem.getChild(InitialActionManipulation.TAG_INITIAL_ACTION_MANIPULATION_REF), listInitialNamedAction);
            List<Object> listActionParam = new LinkedList();
            for (Iterator<Element> variableElem = xmlElem.getChildren().iterator();variableElem.hasNext();){
                Element e = variableElem.next();
                if(e.getName().equals(ActionParamData.TAG_ACTION_PARAM_DATA)){
                    ActionParamData d = new  ActionParamData(e, idActionParam++, listInitialParamData, idQuantity++, listPhysicalQuantity);
                    listActionParam.add(d);
                }else if(e.getName().equals(ActionParamQuantity.TAG_ACTION_PARAM_QUANTITY)){
                    ActionParamQuantity q = new ActionParamQuantity(e, idActionParam++, listInitialParamQuantity, idQuantity++, listPhysicalQuantity);
                    listActionParam.add(q);
                }else if(e.getName().equals(ActionParamMaterial.TAG_ACTION_PARAM_MATERIAL)){
                    ActionParamMaterial q = new ActionParamMaterial(e, idActionParam++, listInitialParamMaterial,listMaterial, listActionParamQuantity );
                    listActionParam.add(q);
                }else if(e.getName().equals(TAG_ACTION_PARAM_LIST)){
                    ArrayList<ActionParam> list = new ArrayList();
                    for(Iterator<Element> el = e.getChildren(ActionParamData.TAG_ACTION_PARAM_DATA).iterator();el.hasNext();){
                        Element elm = el.next();
                        ActionParamData d = new  ActionParamData(elm, idActionParam++, listInitialParamData, idQuantity++, listPhysicalQuantity);
                        list.add(d);
                    }
                    for(Iterator<Element> el = e.getChildren(ActionParamMaterial.TAG_ACTION_PARAM_MATERIAL).iterator();el.hasNext();){
                        Element elm = el.next();
                        ActionParamMaterial d = new  ActionParamMaterial(elm, idActionParam++, listInitialParamMaterial, listMaterial, listActionParamQuantity);
                        list.add(d);
                    }
                    for(Iterator<Element> el = e.getChildren(ActionParamQuantity.TAG_ACTION_PARAM_QUANTITY).iterator();el.hasNext();){
                        Element elm = el.next();
                        ActionParamQuantity d = new  ActionParamQuantity(elm,idActionParam++, listInitialParamQuantity, idQuantity++, listPhysicalQuantity);
                        list.add(d);
                    }
                    listActionParam.add(list);
                }
            }
            int nbParam = listActionParam.size();
            tabParam = new Object[nbParam];
            sortActionParam(listActionParam, listInitialNamedAction);
            
            listMaterialProd = new ArrayList();
            for (Iterator<Element> variableElem = xmlElem.getChildren().iterator();variableElem.hasNext();){
                Element e = variableElem.next();
                if(e.getName().equals(Material.TAG_MATERIAL)){
                    Material m = new Material(e, idMaterial++, listTypeMaterial, listPhysicalQuantity, idQuantity++);
                    listMaterialProd.add(m);
                }else if (e.getName().equals(Material.TAG_MATERIAL_REF)){
                    Material m = new Material(e, listMaterial);
                    listMaterialProd.add(m);
                }else if(e.getName().equals(TAG_ACTION_MANIPULATION_LIST_MAT_PROD)){
                    ArrayList<Material> list = new ArrayList();
                    for(Iterator<Element> el = e.getChildren(Material.TAG_MATERIAL).iterator();el.hasNext();){
                        Element elm = el.next();
                        Material m = new Material(elm, idMaterial++, listTypeMaterial, listPhysicalQuantity, idQuantity++);
                        list.add(m);
                    }
                    for(Iterator<Element> el = e.getChildren(Material.TAG_MATERIAL_REF).iterator();el.hasNext();){
                        Element elm = el.next();
                        Material m = new Material(elm, listMaterial);
                        list.add(m);
                    }
                    listMaterialProd.add(list);
                }
            }
	} else {
            throw(new JDOMException("Action Manipulation expects <"+TAG_ACTION_MANIPULATION+"> as root element, but found <"+xmlElem.getName()+">."));
	}
    }

    private void sortActionParam(List<Object> listActionParam, List<InitialNamedAction> listInitialNamedAction){
        int nb = listActionParam.size();
        int nbI = listInitialNamedAction.size();
        for (int i=0; i<nb; i++){
            Object o = listActionParam.get(i);
            String code = "";
            if(o instanceof ActionParamQuantity){
                code = ((ActionParamQuantity)o).getInitialParam().getCode();
            }else if(o instanceof ActionParamMaterial){
                code =  ((ActionParamMaterial)o).getInitialParam().getCode();
            }else if(o instanceof ActionParamData){
                code = ((ActionParamData)o).getInitialParam().getCode();
            }else if(o instanceof ArrayList){
                code = ((ArrayList<ActionParam>)o).get(0).getInitialParam().getCode();
            }
            int id = -1;
            for (int j=0; j<nbI; j++){
                InitialActionParam[] t = listInitialNamedAction.get(j).getVariable().getTabParam();
                for(int k=0; k<t.length; k++){
                    if(t[k].getCode().equals(code)){
                        id = k;
                        break;
                    }
                }
            }
            tabParam[id] = listActionParam.get(i);
        }
    }

    public ArrayList<Object> getListMaterialProd() {
        return listMaterialProd;
    }

    public void setListMaterialProd(ArrayList<Object> listMaterialProd) {
        this.listMaterialProd = listMaterialProd;
    }

    @Override
    public Object clone() {
        CopexActionManipulation a = (CopexActionManipulation) super.clone() ;
        ArrayList<Object> listMaterialProdC = new ArrayList();
        int nbM = this.listMaterialProd.size() ;
        for (int i=0; i<nbM; i++){
            if (this.listMaterialProd.get(i) instanceof Material){
                listMaterialProdC.add((Material)((Material)this.listMaterialProd.get(i)).clone());
            }else if (this.listMaterialProd.get(i) instanceof ArrayList){
                ArrayList<Material> l = new ArrayList();
                int n = ((ArrayList)this.listMaterialProd.get(i)).size();
                for (int j=0; j<n; j++){
                    l.add((Material)((ArrayList<Material>)this.listMaterialProd.get(i)).get(j).clone());
                }
                listMaterialProdC.add(l);
            }
        }
        a.setListMaterialProd(listMaterialProdC);
        return a;
    }

    @Override
    public String toDescription(CopexPanel edP) {
        return toDescription(edP.getLocale());
    }

    @Override
    public String toDescription(Locale locale) {
        String s = super.toDescription(locale);
        int nbMatProd = listMaterialProd.size();
        for (int i=0; i<nbMatProd; i++){
            if (this.listMaterialProd.get(i) instanceof Material){
                s += " - "+((InitialActionManipulation)this.namedAction).getListOutput().get(i).getTextProd(locale)+" : "+((Material)listMaterialProd.get(i)).getName(locale);
            }else if (this.listMaterialProd.get(i) instanceof ArrayList){
                int n = ((ArrayList)this.listMaterialProd.get(i)).size();
                s += " - "+((InitialActionManipulation)this.namedAction).getListOutput().get(i).getTextProd(locale)+" : ";
                for (int j=0; j<n; j++){
                    s += ((ArrayList<Material>)this.listMaterialProd.get(i)).get(j).getName(locale)+" | ";
                }
            }
        }
        return s;
    }

    @Override
    public Element toXML(){
        Element element = super.toXML(new Element(TAG_ACTION_MANIPULATION));
        for(int l=0; l<listDescription.size(); l++){
            Locale loc = listDescription.get(l).getLocale();
            String d = toDescription(loc);
            Element el = new Element(TAG_ACTION_NAMED_TOSTRING).setText(d);
            el.setAttribute(new Attribute(MyConstants.XMLNAME_LANGUAGE, loc.getLanguage()));
            element.addContent(el);
        }
        if(listMaterialProd != null && listMaterialProd.size() > 0){
            for (Iterator<Object> m = listMaterialProd.iterator(); m.hasNext();) {
                Object o= m.next();
                if(o instanceof Material){
                    element.addContent(((Material)o).toXML());
                }else if (o instanceof ArrayList){
                    Element e = new Element(TAG_ACTION_MANIPULATION_LIST_MAT_PROD);
                    for (Iterator<Material> p = ((ArrayList<Material>)o).iterator(); p.hasNext();) {
                        e.addContent(p.next().toXML());
                    }
                    element.addContent(e);
                }
            }
        }
        return element;
    }

    @Override
    public List<Material> getAllMaterialProd(){
        List<Material> list = new LinkedList();
        if(listMaterialProd != null){
            for (Iterator<Object> m = listMaterialProd.iterator(); m.hasNext();) {
                Object o= m.next();
                if(o instanceof Material){
                    list.add(((Material)o));
                }else if (o instanceof ArrayList){
                    for (Iterator<Material> p = ((ArrayList<Material>)o).iterator(); p.hasNext();) {
                        list.add(p.next());
                    }
                }
            }
        }
        return list;
    }


}
