/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.collaboration;

import eu.scy.client.tools.copex.common.CopexAction;
import eu.scy.client.tools.copex.common.CopexTask;
import eu.scy.client.tools.copex.common.Material;
import eu.scy.client.tools.copex.common.MaterialUsed;
import eu.scy.client.tools.copex.common.PhysicalQuantity;
import eu.scy.client.tools.copex.common.Step;
import eu.scy.client.tools.copex.common.TypeMaterial;
import eu.scy.client.tools.copex.main.CopexPanel;
import eu.scy.client.tools.copex.utilities.CopexUtilities;
import eu.scy.common.datasync.ISyncObject;
import eu.scy.common.datasync.SyncObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom.Element;
import org.jdom.JDOMException;

/**
 *
 * @author Marjolaine
 */
public class CopexSyncManager {
    public final static String TOOLNAME = "copex";
    // sync. name action
    private final static String sync_name = "sync_name";
    private final static String sync_node_value = "node_value";
    private final static String sync_listMaterialCreated = "list_material_created";
    private final static String sync_position = "position";
    private final static String sync_mode_insert = "mode_insert";
    
    private CopexPanel copex;
    private final Logger logger;

    public CopexSyncManager(CopexPanel copex) {
        this.copex = copex;
        logger = Logger.getLogger(CopexSyncManager.class.getName());
    }

    /*sync. node added */
    public void syncNodeAdded(ISyncObject syncObject){
        String syncName = syncObject.getProperty(sync_name);
        if(syncName != null){
            if(syncName.equals(CopexSyncEnum.TASK_ADDED.name())){ // add task
                addTask(syncObject);
            }
        }
    }

    /*sync. node removed */
    public void syncNodeRemoved(ISyncObject syncObject){
        String syncName = syncObject.getProperty(sync_name);
        if(syncName != null){
        }
    }

    /*sync. node changed */
    public void syncNodeChanged(ISyncObject syncObject){
        String syncName = syncObject.getProperty(sync_name);
        if(syncName != null){
            if(syncName.equals(CopexSyncEnum.QUESTION_UPDATED.name())){ // update question
                updateQuestion(syncObject);
            }else if(syncName.equals(CopexSyncEnum.HYPOTHESIS_UPDATED.name())){ // update hypothesis
                updateHypothesis(syncObject);
            }else if(syncName.equals(CopexSyncEnum.GENERAL_PRINCIPLE_UPDATED.name())){ // update general principle
                updateGeneralPrinciple(syncObject);
            }else if(syncName.equals(CopexSyncEnum.EVALUATION_UPDATED.name())){ // update evaluation
                updateEvaluation(syncObject);
            }else if(syncName.equals(CopexSyncEnum.MATERIAL_USED_UPDATED.name())){ // update material used
                updateMaterialUsed(syncObject);
            }
        }
    }

    // UPDATE QUESTION
    // add the event
    public void addSyncUpdateQuestion(String value){
        ISyncObject syncObject = new SyncObject();
        syncObject.setToolname(TOOLNAME);
        syncObject.setProperty(sync_name, CopexSyncEnum.QUESTION_UPDATED.name());
        syncObject.setProperty(sync_node_value, value);
        copex.changeCopexSyncObject(syncObject);
    }

   // calls when received a sync. event
    private void updateQuestion(ISyncObject syncObject){
        String value = syncObject.getProperty(sync_node_value);
        copex.updateQuestion(value);
    }

    // UPDATE HYPOTHESIS
    // add the event
    public void addSyncUpdateHypothesis(String value){
        ISyncObject syncObject = new SyncObject();
        syncObject.setToolname(TOOLNAME);
        syncObject.setProperty(sync_name, CopexSyncEnum.HYPOTHESIS_UPDATED.name());
        syncObject.setProperty(sync_node_value, value);
        copex.changeCopexSyncObject(syncObject);
    }

   // calls when received a sync. event
    private void updateHypothesis(ISyncObject syncObject){
        String value = syncObject.getProperty(sync_node_value);
        copex.updateHypothesis(value);
    }

    // UPDATE GENERAL PRINCIPLE
    // add the event
    public void addSyncUpdateGeneralPrinciple(String value){
        ISyncObject syncObject = new SyncObject();
        syncObject.setToolname(TOOLNAME);
        syncObject.setProperty(sync_name, CopexSyncEnum.GENERAL_PRINCIPLE_UPDATED.name());
        syncObject.setProperty(sync_node_value, value);
        copex.changeCopexSyncObject(syncObject);
    }

   // calls when received a sync. event
    private void updateGeneralPrinciple(ISyncObject syncObject){
        String value = syncObject.getProperty(sync_node_value);
        copex.updateGeneralPrinciple(value);
    }

    // UPDATE EVALUATION
    // add the event
    public void addSyncUpdateEvaluation(String value){
        ISyncObject syncObject = new SyncObject();
        syncObject.setToolname(TOOLNAME);
        syncObject.setProperty(sync_name, CopexSyncEnum.EVALUATION_UPDATED.name());
        syncObject.setProperty(sync_node_value, value);
        copex.changeCopexSyncObject(syncObject);
    }

   // calls when received a sync. event
    private void updateEvaluation(ISyncObject syncObject){
        String value = syncObject.getProperty(sync_node_value);
        copex.updateEvaluation(value);
    }

    // MATERIAL ADDED
    // add the event
    public void addSyncMaterialAdded(ArrayList<MaterialUsed> listMaterialUsedToCreate, ArrayList<MaterialUsed> listMaterialUsedToDelete, ArrayList<MaterialUsed> listMaterialUsedToUpdate){
        ISyncObject syncObject = new SyncObject();
        syncObject.setToolname(TOOLNAME);
        syncObject.setProperty(sync_name, CopexSyncEnum.MATERIAL_USED_UPDATED.name());
        Element e = new Element(sync_listMaterialCreated);
        for(Iterator<MaterialUsed> m= listMaterialUsedToCreate.iterator();m.hasNext();){
            e.addContent(m.next().toXML());
        }
        syncObject.setProperty(sync_listMaterialCreated, CopexUtilities.xmlToString(e));
        copex.addCopexSyncObject(syncObject);
    }

   // calls when received a sync. event
    private void updateMaterialUsed(ISyncObject syncObject){
        ArrayList<MaterialUsed> listMaterialUsedCreated = new ArrayList();
        ArrayList<MaterialUsed> listMaterialUsedDeleted = new ArrayList();
        ArrayList<MaterialUsed> listMaterialUsedUpdated = new ArrayList();
        Element xmlMaterialCreated = CopexUtilities.stringToXml(syncObject.getProperty(sync_listMaterialCreated));
        List<Material> listMaterial = copex.getListMaterial();
        long idMaterial = copex.getIdMaterial();
        long idQuantity = copex.getIdQuantity();
        List<TypeMaterial> listTypeMaterial = copex.getListTypeMaterial();
        List<PhysicalQuantity> listPhysicalQuantity= copex.getListPhysicalQuantity();
        try{
            for(Iterator<Element> el = xmlMaterialCreated.getChildren(MaterialUsed.TAG_MATERIAL_USED).iterator();el.hasNext();){

                MaterialUsed m = new MaterialUsed(el.next(), listMaterial, idMaterial++, listTypeMaterial, listPhysicalQuantity,  idQuantity++);
                listMaterialUsedCreated.add(m);
            }
            copex.setMaterial(listMaterialUsedCreated, listMaterialUsedDeleted, listMaterialUsedUpdated);
        }catch(JDOMException ex){
            logger.log(Level.SEVERE, "updateMaterialUsed synch. "+ex);
        }
    }

    // ADD TASK
    // add the event
    public void addSyncAddTask(CopexTask task, CopexTask taskSelected, char insertIn){
        ISyncObject syncObject = new SyncObject();
        syncObject.setToolname(TOOLNAME);
        syncObject.setProperty(sync_name, CopexSyncEnum.TASK_ADDED.name());
        syncObject.setProperty(sync_node_value, CopexUtilities.xmlToString(task.toXML()));
        syncObject.setProperty(sync_position, CopexUtilities.xmlToString(taskSelected.toXML()));
        copex.addCopexSyncObject(syncObject);
    }

   // calls when received a sync. event
    private void addTask(ISyncObject syncObject){
//        try{
//            Element e1 = CopexUtilities.stringToXml(syncObject.getProperty(sync_node_value));
//            CopexTask task = null;
//            List<Material> listMaterial = copex.getListMaterial();
//            List<TypeMaterial> listTypeMaterial = copex.getListTypeMaterial();
//            List<PhysicalQuantity> listPhysicalQuantity= copex.getListPhysicalQuantity();
//            if(e1.getChild(Step.TAG_STEP) != null){
//                task = new Step(e1, listPhysicalQuantity, listTypeMaterial, listInitialParamData, listInitialParamMaterial, listInitialParamQuantity, listInitialAcquisitionOutput, listInitialManipulationOutput, listInitialTreatmentOutput, listMaterial, listActionParamQuantity);
//            }else{
//                task = new CopexAction(e1,  listPhysicalQuantity, listTypeMaterial, listInitialParamData, listInitialParamMaterial, listInitialParamQuantity, listInitialAcquisitionOutput, listInitialManipulationOutput, listInitialTreatmentOutput, listMaterial, listActionParamQuantity);
//            }
//            CopexTask taskSelected = null;
//            Element e2 = CopexUtilities.stringToXml(syncObject.getProperty(sync_position));
//            if(e2.getChild(Step.TAG_STEP) != null){
//                taskSelected = new Step(e1, idQuantity, idMaterial, idMaterial, idQuantity, idMaterial, idQuantity, listPhysicalQuantity, listTypeMaterial, null, null, null, null, null, null, listMaterial, null);
//            }else{
//                taskSelected = new CopexAction(e1, idQuantity, idMaterial, idMaterial, idQuantity, idMaterial, idQuantity, listPhysicalQuantity, listTypeMaterial, null, null, null, null, null, null, listMaterial, null);
//            }
//            char insertIn = syncObject.getProperty(sync_mode_insert).charAt(0);
//            copex.addTask(task, taskSelected, insertIn);
//        }catch(JDOMException ex){
//            logger.log(Level.SEVERE, "addTask synch. "+ex);
//        }
        
    }
}
