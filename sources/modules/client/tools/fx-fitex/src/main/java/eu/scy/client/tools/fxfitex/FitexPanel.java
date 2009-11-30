/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.fxfitex;

import eu.scy.actionlogging.DevNullActionLogger;
import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.actionlogging.api.IActionLogger;
import eu.scy.actionlogging.logger.Action;
import eu.scy.client.common.datasync.DataSyncService;
import eu.scy.client.common.datasync.IDataSyncService;
import eu.scy.client.common.datasync.ISyncListener;
import eu.scy.client.common.datasync.ISyncSession;
import eu.scy.collaborationservice.CollaborationServiceException;
import eu.scy.common.datasync.ISyncObject;
import eu.scy.tools.dataProcessTool.dataTool.DataProcessToolPanel ;
import eu.scy.tools.dataProcessTool.logger.FitexProperty;
import eu.scy.tools.dataProcessTool.logger.FitexLog;
import eu.scy.tools.dataProcessTool.utilities.ActionDataProcessTool;
import eu.scy.elo.contenttype.dataset.DataSet;
import eu.scy.elo.contenttype.dataset.DataSetHeader;
import eu.scy.elo.contenttype.dataset.DataSetRow;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import eu.scy.toolbroker.ToolBrokerImpl;
import java.util.logging.Level;
import java.util.logging.Logger;
import roolo.elo.JDomStringConversion;
import roolo.elo.api.IMetadataKey;
import java.util.List;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import org.jdom.Element;
import java.io.File;
import java.util.Iterator;

import javax.swing.JOptionPane;
import org.jdom.JDOMException;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;


/**
 *
 * @author Marjolaine
 */
//public class FitexPanel extends JPanel implements ActionDataProcessTool, IDataSyncListener{
public class FitexPanel extends JPanel implements ActionDataProcessTool, ISyncListener{
    private final static String SYNC_OBJECT_TYPE = "type";
    private final static String TYPE_DATASET_HEADER = "datasetheader";
    private final static String TYPE_DATASET_ROW = "datasetrow";
    private final static String SIMULATOR_NAME = "scysimulator";

    
    /* data process visualization tool */
    private DataProcessToolPanel dataProcessPanel;

    private ToolBrokerAPI<IMetadataKey> tbi;
    private ISyncSession session = null;
    // how can i get userName & password? + mission name
    private String username = "default_username";
    private String password = "default_password";
    private String mission_name = "mission 1";
    private IActionLogger actionLogger;
    private IDataSyncService datasync;
    
    /* Constructor data ToolImpl panel - blank */
    public FitexPanel() {
        super();
        this.setLayout(new BorderLayout());
        initTBI();
        initActionLogger();
        initDataProcessTool();
        load();
    }

    /* tbi initialization*/
    private void initTBI(){
        //tbi=  new ToolBrokerImpl<IMetadataKey>(username, password);
    }
    /* initialization action logger */
    private void initActionLogger(){
        //actionLogger = tbi.getActionLogger();
        actionLogger = new DevNullActionLogger();
    }

    private void initCollaborationService() throws CollaborationServiceException {
    	// TODO the datasync instance will be soon delivered by the SCY-lab via TBI
    	ConnectionConfiguration config;
        config = new ConnectionConfiguration("scy.collide.info", 5222);
        XMPPConnection  conn = new XMPPConnection(config);
		try {
			conn.connect();
			conn.login("merkel", "merkel");
			conn.addConnectionListener(new ConnectionListener() {
                @Override
				public void reconnectionSuccessful() {}
                @Override
				public void reconnectionFailed(Exception arg0) {}
                @Override
				public void reconnectingIn(int arg0) {}
                @Override
				public void connectionClosedOnError(Exception arg0) {}
                @Override
				public void connectionClosed() {}
			});

			datasync = new DataSyncService(conn);

		} catch (XMPPException e) {
			e.printStackTrace();
		}
    }

    public void joinSession(String mucID){
        try {
            initCollaborationService();
        } catch (CollaborationServiceException ex) {
            Logger.getLogger(FitexPanel.class.getName()).log(Level.SEVERE, null, ex);
        }

        session = datasync.joinSession(mucID, this);
        if(session == null){
            JOptionPane.showMessageDialog(null, "join session error, null");
        }else
            readAllSyncObjects();
    }
    private void readAllSyncObjects(){
        List<ISyncObject> syncObjects = session.getAllSyncObjects();
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
        if(syncObject.getToolname() != null && syncObject.getToolname().equals(SIMULATOR_NAME)){
            if(syncObject.getProperties() != null) {
                String type = syncObject.getProperty(SYNC_OBJECT_TYPE);
                if(type != null){
                    if(type.equals(TYPE_DATASET_HEADER)){
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
        dataProcessPanel = new DataProcessToolPanel(true);
        dataProcessPanel.addActionCopexButton(this);
        add(dataProcessPanel, BorderLayout.CENTER);
//        setSize(DataProcessToolPanel.PANEL_WIDTH, DataProcessToolPanel.PANEL_HEIGHT);
//        setPreferredSize(getSize());
    }

    public void load(){
        dataProcessPanel.loadData();
        setSize(500,300);
    }
    public void newElo(){
        this.dataProcessPanel.newElo();
    }
    /* load ELO into data process tool */
    public void loadELO(String xmlContent){
        this.dataProcessPanel.loadELO(xmlContent);
    }

    /* merge ELO with the current dataset */
    public void mergeELO(String xmlContent){
        this.dataProcessPanel.mergeELO(new JDomStringConversion().stringToXml(xmlContent));
    }

    public Element getPDS(){
        return this.dataProcessPanel.getPDS();
    }
    
    /* import CSV file => EDLO dataset */
    public DataSet importCSVFile(File file){
        if (file  != null)
            return  dataProcessPanel.importCSVFile(file);
        return null;
    }

    @Override
    public void resizeDataToolPanel(int width, int height) {

    }

    @Override
    public void logAction(String type, List<FitexProperty> attribute) {
        // action
        Action action = new Action(type, username);
		action.addContext(ContextConstants.tool, FitexLog.toolName);
		action.addContext(ContextConstants.mission, mission_name);
        for(Iterator<FitexProperty> p = attribute.iterator();p.hasNext();){
            FitexProperty property = p.next();
            if(property.getSubElement() == null)
                action.addAttribute(property.getName(), property.getValue());
            else
                action.addAttribute(property.getName(), property.getValue(), property.getSubElement());
        }
        // log action
        if(actionLogger != null)
            actionLogger.log(action);
    }

    public void stopFitex(){
        this.dataProcessPanel.endTool();
    }

    @Override
    public void syncObjectAdded(ISyncObject syncObject) {
        readSyncObject(syncObject);
    }

    @Override
    public void syncObjectChanged(ISyncObject syncObject) {
        //
    }

    @Override
    public void syncObjectRemoved(ISyncObject syncObject) {
        //
    }
    
}
