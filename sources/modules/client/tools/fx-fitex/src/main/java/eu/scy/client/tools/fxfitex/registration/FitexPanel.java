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
import eu.scy.client.common.datasync.IDataSyncService;
import eu.scy.client.common.datasync.ISyncListener;
import eu.scy.client.common.datasync.ISyncSession;
import eu.scy.common.datasync.ISyncObject;
import eu.scy.elo.contenttype.dataset.DataSet;
import eu.scy.elo.contenttype.dataset.DataSetHeader;
import eu.scy.elo.contenttype.dataset.DataSetRow;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import eu.scy.tools.dataProcessTool.dataTool.DataProcessToolPanel;
import eu.scy.tools.dataProcessTool.logger.FitexLog;
import eu.scy.tools.dataProcessTool.logger.FitexProperty;
import eu.scy.tools.dataProcessTool.utilities.ActionDataProcessTool;
import eu.scy.tools.dataProcessTool.utilities.MyFileFilterCSV;
import java.io.File;
//import eu.scy.toolbroker.ToolBrokerImpl;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;

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
    private DataProcessToolPanel dataProcessPanel = null;
    private File lastUsedFileImport = null;

    private ToolBrokerAPI tbi;
    private ISyncSession session = null;
    private String session_name = "sessionName";
    private IActionLogger actionLogger;
    private IDataSyncService datasync;
    private final Logger debugLogger;
    private String toolName;

    /* Constructor data ToolImpl panel - blank */
    public FitexPanel(String toolName) {
        super();
        this.toolName = toolName;
        debugLogger = Logger.getLogger(FitexPanel.class.getName());
        this.setLayout(new BorderLayout());
    }

    public void initFitex(){
        initDataProcessTool();
        load();
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

    public void joinSession(String mucID){
        if(session != null){
            leaveSession(session.getId());
        }
        session = tbi.getDataSyncService().joinSession(mucID, this);
        if (session == null) {
            JOptionPane.showMessageDialog(null, "join session error, null");
        } else
            readAllSyncObjects();
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

    public void readAllSyncObjects(){
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
        debugLogger.log(Level.SEVERE, "readSyncObject...");
        if(syncObject.getToolname() != null && syncObject.getToolname().equals(SIMULATOR_NAME)){
            if(syncObject.getProperties() != null) {
                String type = syncObject.getProperty(SYNC_OBJECT_TYPE);
                if(type != null){
                    if(type.equals(TYPE_DATASET_HEADER)){
                        debugLogger.log(Level.SEVERE, "...of type header");
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
                        debugLogger.log(Level.SEVERE, "...of type row");
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
        setSize(DataProcessToolPanel.PANEL_WIDTH, DataProcessToolPanel.PANEL_HEIGHT);
        setPreferredSize(getSize());
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
        IAction action = new Action();
        action.setType(type);
        if(tbi != null){
            action.setUser(tbi.getLoginUserName());
            action.addContext(ContextConstants.tool, this.toolName);
            action.addContext(ContextConstants.mission, tbi.getMission());
            action.addContext(ContextConstants.session, session_name);
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

    /*public void synchronizeTool(){
        String mucID = JOptionPane.showInputDialog("Enter session ID:", "");
        if (StringUtils.hasText(mucID)){
            joinSession(mucID);
        }
    }*/

    public DataSet importCsvFile() {
        JFileChooser aFileChooser = new JFileChooser();
        aFileChooser.setFileFilter(new MyFileFilterCSV());
	if (lastUsedFileImport != null){
            aFileChooser.setCurrentDirectory(lastUsedFileImport.getParentFile());
            aFileChooser.setSelectedFile(lastUsedFileImport);
        }
	int userResponse = aFileChooser.showOpenDialog(null);
	if (userResponse == JFileChooser.APPROVE_OPTION){
		File file = aFileChooser.getSelectedFile();
                if(!isCSVFile(file)){
                    JOptionPane.showMessageDialog(this ,"Error: the file must be a csv file." , "Error Import csv",JOptionPane.ERROR_MESSAGE);
                    return null;
                }
                lastUsedFileImport = file;
                if(lastUsedFileImport != null)
                    return importCSVFile(file);
	}

        return null;
    }

    public static boolean isCSVFile(File file){
        int id = file.getName().lastIndexOf(".");
        if(id == -1 || id==file.getName().length()-1)
            return false;
        return file.getName().substring(id+1).equals("csv");
    }

   

}
