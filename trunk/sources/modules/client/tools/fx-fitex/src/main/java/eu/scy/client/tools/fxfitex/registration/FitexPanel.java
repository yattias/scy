/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.fxfitex.registration;

import java.awt.BorderLayout;
import java.util.Iterator;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.jdom.Element;
import org.jdom.JDOMException;
import roolo.elo.JDomStringConversion;
import eu.scy.actionlogging.Action;
import eu.scy.actionlogging.DevNullActionLogger;
import eu.scy.actionlogging.SystemOutActionLogger;
import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.actionlogging.api.IAction;
import eu.scy.actionlogging.api.IActionLogger;
import eu.scy.client.common.datasync.DataSyncException;
import eu.scy.client.common.datasync.ISyncListener;
import eu.scy.client.common.datasync.ISyncSession;
import eu.scy.client.common.scyi18n.ResourceBundleWrapper;
import eu.scy.common.datasync.ISyncObject;
import eu.scy.elo.contenttype.dataset.DataSetHeader;
import eu.scy.elo.contenttype.dataset.DataSetRow;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import eu.scy.client.tools.fitex.main.DataProcessToolPanel;
import eu.scy.client.tools.dataProcessTool.logger.FitexProperty;
import eu.scy.client.tools.dataProcessTool.utilities.ActionDataProcessTool;
import java.awt.Container;
import java.awt.Dimension;
import java.io.File;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

/**
 * swing panel for fitex
 * @author Marjolaine
 */

public class FitexPanel extends JPanel implements ActionDataProcessTool, ISyncListener{
    private final static String SYNC_OBJECT_TYPE = "type";
    private final static String TYPE_DATASET_HEADER = "datasetheader";
    private final static String TYPE_DATASET_ROW = "datasetrow";
    private final static String SIMULATOR_NAME = "scysimulator";


    /* data process visualization tool */
    private DataProcessToolPanel dataProcessPanel = null;
    private File lastUsedFileImport = null;

    private ToolBrokerAPI tbi;
    private ISyncSession session = null;
    private String session_name = "n/a";
    private String eloUri = "n/a";
    private IActionLogger actionLogger;
    private final Logger debugLogger;
    private String toolName;

    private ResourceBundleWrapper bundle;

    /* Constructor data ToolImpl panel - blank */
    public FitexPanel(String toolName) {
        super();
        this.toolName = toolName;
        debugLogger = Logger.getLogger(FitexPanel.class.getName());
        this.setLayout(new BorderLayout());
        this.bundle = new ResourceBundleWrapper(this);
    }

    public void initFitex(){
        initDataProcessTool();
        load();
    }

    public void setEloUri(String eloUri){
        this.eloUri = eloUri;
    }

    public void setTBI(ToolBrokerAPI tbi) {
        this.tbi = tbi;
    }

    public void setSession(ISyncSession session) {
        if(this.session != null)
            session.removeSyncListener(this);
        this.session = session;
    }

    
    /* initialization action logger */
    public void initActionLogger(){
        if(tbi != null){
            actionLogger = tbi.getActionLogger();
            //actionLogger = new SystemOutActionLogger();
        }else{
            actionLogger = new DevNullActionLogger();
            
        }
    }

    // joins the session for sync.
    public ISyncSession joinSession(String mucID){
        if(session != null){
            leaveSession(session.getId());
        }
        try {
            session = tbi.getDataSyncService().joinSession(mucID, this, toolName);
	} catch (DataSyncException e) {
		JOptionPane.showMessageDialog(null, getBundleString("FX-FITEX.MSG_ERROR_SYNC"));
		e.printStackTrace();
                return session;
	}
        if (session == null) {
            JOptionPane.showMessageDialog(null, getBundleString("FX-FITEX.MSG_ERROR_SYNC"));
            return session;
        }
//        else{
//            readAllSyncObjects();
//        }
        return session;
    }

    public void leaveSession(String mucID){
        if(session != null){
            session.removeSyncListener(this);
        }
        session = null;
    }

    public String getSessionID() {
        if (session == null) {
            return null;
        } else {
            return session.getId();
        }
    }

    // read all objects from a session, first the header and then the rows
    public void readAllSyncObjects(){
        List<ISyncObject> syncObjects = null;
	try {
		syncObjects = session.getAllSyncObjects();
	} catch (DataSyncException e) {
		JOptionPane.showMessageDialog(null, getBundleString("FX-FITEX.MSG_ERROR_SYNC"));
		e.printStackTrace();
	}
        if(syncObjects == null)
            return;
        //debugLogger.log(Level.INFO, "readAllSyncObjects ("+syncObjects.size()+") "+session.getId());
        // find the header first
        for (ISyncObject syncObject : syncObjects) {
            if (syncObject.getProperties() != null && syncObject.getProperty(TYPE_DATASET_HEADER) != null) {
                readSyncObject(syncObject);
            }
        }
        // add all value row now
        for (ISyncObject syncObject : syncObjects) {
            if (syncObject.getProperties() != null && syncObject.getProperty(TYPE_DATASET_ROW) != null) {
                readSyncObject(syncObject);
            }
        }
    }

    private void readSyncObject(ISyncObject syncObject){
        debugLogger.log(Level.INFO, "readSyncObject... ");
        if(syncObject.getToolname() != null && syncObject.getToolname().equals(SIMULATOR_NAME)){
            if(syncObject.getProperties() != null) {
                String type = syncObject.getProperty(SYNC_OBJECT_TYPE);
                if(type != null){
                    if(type.equals(TYPE_DATASET_HEADER)){
                        debugLogger.log(Level.INFO, "...of type header ");
                        String dataheader = syncObject.getProperty(TYPE_DATASET_HEADER);
                        if(dataheader != null){
                            try{
                                DataSetHeader header = new DataSetHeader(dataheader);
                                initializeHeader(header);
                            }catch(JDOMException ex){
                                ex.printStackTrace();
                                return;
                            }
                        }
                    }else if (type.equals(TYPE_DATASET_ROW)){
                        debugLogger.log(Level.INFO, "...of type row");
                        String datarow = syncObject.getProperty(TYPE_DATASET_ROW);
                        if(datarow != null){
                            try{
                                DataSetRow row = new DataSetRow(new JDomStringConversion().stringToXml(datarow));
                                updateDataRow(row);
                            }catch(JDOMException ex){
                                ex.printStackTrace();
                                return;
                            }
                        }
                    }
                }
            }
        }else if (syncObject.getToolname() != null && syncObject.getToolname().equals(toolName)){
            if (syncObject.getCreator().equals(session.getUsername())) {
                // creator of the object
            }else{
                dataProcessPanel.syncNodeAdded(syncObject);
            }
        }
    }

    /* initialize model with header*/
    public void initializeHeader(DataSetHeader header){
        dataProcessPanel.initializeHeader(header);
    }

    /* update model with datarow*/
    public void updateDataRow(DataSetRow row){
        // add data row to the instance of data processing tool
        dataProcessPanel.addRow(row);
    }

    private void initDataProcessTool(){
        dataProcessPanel = new DataProcessToolPanel(true, Locale.getDefault());
        dataProcessPanel.addFitexAction(this);
        add(dataProcessPanel, BorderLayout.CENTER);
        setSize(DataProcessToolPanel.PANEL_WIDTH, DataProcessToolPanel.PANEL_HEIGHT);
        setPreferredSize(getSize());
    }

    public void load(){
        dataProcessPanel.loadData();
        setSize(550,355);
    }
    public void newElo(){
        this.dataProcessPanel.newElo();
    }
    /* load ELO into data process tool */
    public void loadELO(String xmlContent){
        this.dataProcessPanel.loadELO(xmlContent);
    }

    /* merge ELO with the current dataset */
    public void mergeELO(Element element){
        this.dataProcessPanel.mergeELO(element);
    }

    /* returns the processed dataset ELO */
    public Element getPDS(){
        return this.dataProcessPanel.getPDS();
    }


    @Override
    public void resizeDataToolPanel(int width, int height) {

    }

    /* logs a user action
     * @param type type of the action (action_added, ...)
     * @param attribute list of the attribute of the action (position in the tree, name of the action...)
     */
    @Override
    public void logAction(String type, List<FitexProperty> attribute) {
        // action
        IAction action = new Action();
        action.setType(type);
        if(tbi != null){
            action.setUser(tbi.getLoginUserName());
            action.addContext(ContextConstants.tool, this.toolName);
            // generic way now
            //action.addContext(ContextConstants.mission, tbi.getMissionSpecificationURI().toString());
            action.addContext(ContextConstants.session, session_name);
            action.addContext(ContextConstants.eloURI, eloUri);
        }

        for(Iterator<FitexProperty> p = attribute.iterator();p.hasNext();){
            FitexProperty property = p.next();
            if(property.getSubElement() == null)
                action.addAttribute(property.getName(), property.getValue());
            else{
            	action.addAttribute(property.getName(), property.getValue());
                action.addAttribute(property.getName()+"_sub", property.getSubElement().getValue());
            }
        }
        // log action
        if(actionLogger != null)
            actionLogger.log(action);
    }

    public void stopFitex(){
        this.dataProcessPanel.endTool();
    }


    @Override
    public void syncObjectAdded(final ISyncObject syncObject) {
        if(syncObject == null)
            return;
         SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    readSyncObject(syncObject);
                }
            });
    }

    @Override
    public void syncObjectChanged(ISyncObject syncObject) {
        if (syncObject.getToolname() != null && syncObject.getToolname().equals(toolName)){
            if (syncObject.getCreator().equals(session.getUsername())) {
                // creator of the object
            }else{
                dataProcessPanel.syncNodeChanged(syncObject);
            }
        }
    }

    @Override
    public void syncObjectRemoved(ISyncObject syncObject) {
        if (syncObject.getToolname() != null && syncObject.getToolname().equals(toolName)){
            if (syncObject.getCreator().equals(session.getUsername())) {
                // creator of the object
            }else{
                dataProcessPanel.syncNodeRemoved(syncObject);
            }
        }
    }

    
   private String getBundleString(String key){
       return this.bundle.getString(key);
   }

   /* returns the interface panel for the thumbnail */
    public Container getInterfacePanel(){
        return dataProcessPanel.getInterfacePanel();
    }

    public Dimension getRealSize(){
        return dataProcessPanel.getInterfacePanel().getSize();
    }

    public void startCollaboration(){
        this.dataProcessPanel.startCollaboration();
    }

    public void endCollaboration(){
        this.dataProcessPanel.endCollaboration();
    }

    @Override
    public void addFitexSyncObject(ISyncObject syncObject) {
        if(session != null)
            session.addSyncObject(syncObject);
    }

    @Override
    public void changeFitexSyncObject(ISyncObject syncObject) {
        if(session != null)
            session.changeSyncObject(syncObject);
    }

    @Override
    public void removeFitexSyncObject(ISyncObject syncObject) {
        if(session != null)
            session.removeSyncObject(syncObject);
    }

    public void setReadOnly(boolean readonly){
       if(dataProcessPanel != null)
            this.dataProcessPanel.setReadOnly(readonly);
    }
}
