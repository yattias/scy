/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.fxfitex.registration;

import java.awt.BorderLayout;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.jdom.Element;
import org.jdom.JDOMException;
import roolo.elo.JDomStringConversion;
import eu.scy.actionlogging.Action;
import eu.scy.actionlogging.DevNullActionLogger;
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
import eu.scy.toolbroker.ToolBrokerImpl;
import javax.swing.JFileChooser;
import org.springframework.util.StringUtils;


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
    private File lastUsedFileImport = null;

    private ToolBrokerAPI tbi;
    private ISyncSession session = null;
    // how can i get userName & password? + mission name
    private String username = "merkel";
    private String password = "merkel";
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

    public void setTBI(ToolBrokerAPI tbi) {
        this.tbi = tbi;
    }

    /* tbi initialization*/
    private void initTBI(){
        //tbi=  new ToolBrokerImpl(username, password);
    }
    /* initialization action logger */
    private void initActionLogger(){
        if(tbi != null)
            actionLogger = tbi.getActionLogger();
        else
            actionLogger = new DevNullActionLogger();
    }

    private void initCollaborationService() {
        datasync = tbi.getDataSyncService();
    }

    public void joinSession(String mucID){
        initCollaborationService();
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
        action.setUser(username);
		action.addContext(ContextConstants.tool, FitexLog.toolName);
		action.addContext(ContextConstants.mission, mission_name);
        for(Iterator<FitexProperty> p = attribute.iterator();p.hasNext();){
            FitexProperty property = p.next();
            if(property.getSubElement() == null)
                action.addAttribute(property.getName(), property.getValue());
            //else
            	// TODO this is not supported anymore!
                //action.addAttribute(property.getName(), property.getValue(), property.getSubElement());
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

    public void synchronizeTool(){
        String mucID = JOptionPane.showInputDialog("Enter session ID:", "");
        if (StringUtils.hasText(mucID)){
            joinSession(mucID);
        }
    }

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
