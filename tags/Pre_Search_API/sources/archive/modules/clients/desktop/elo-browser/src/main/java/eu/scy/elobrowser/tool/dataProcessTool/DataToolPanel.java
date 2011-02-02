/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.elobrowser.tool.dataProcessTool;

import eu.scy.tools.dataProcessTool.dataTool.DataProcessToolPanel ;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import org.jdom.Element;
import eu.scy.collaborationservice.event.ICollaborationServiceEvent;
import eu.scy.collaborationservice.event.ICollaborationServiceListener;
import eu.scy.communications.message.IScyMessage;
import eu.scy.client.tools.scysimulator.DatasetSandbox;
import eu.scy.collaborationservice.CollaborationServiceException;
import eu.scy.collaborationservice.CollaborationServiceFactory;
import eu.scy.collaborationservice.ICollaborationService;
import eu.scy.elo.contenttype.dataset.DataSetHeader;
import eu.scy.elo.contenttype.dataset.DataSetRow;
import eu.scy.elo.contenttype.dataset.DataSet;
import java.util.ArrayList;
import org.jdom.JDOMException;
import roolo.elo.JDomStringConversion;
import eu.scy.elobrowser.main.user.User;
import java.io.File;

/**
 * Entry point of the data process visualization tool,
 * interface between the tool and ToolBrokerAPI
 * @author Marjolaine
 */
public class DataToolPanel extends JPanel  implements ICollaborationServiceListener{
    /* data process visualization tool */
    private DataProcessToolPanel dataProcessPanel;

    /* id elo managed */
    private long idDataset = -1;

    
    /* object type dataset header*/
    public static final String TYPE_DATASET_HEADER = "datasetheader";
    /* object type dataset row*/
    public static final String TYPE_DATASET_ROW = "datasetrow";

    /* collaboration service*/
    private ICollaborationService collaborationService;

    /* simulator Tool Name */
    private String simulatorName = DatasetSandbox.TOOL_NAME;
    /* SessionID*/
    private String sessionID = DatasetSandbox.SESSION_ID;

    /* Constructor data Tool panel - blank */
    public DataToolPanel() {
        super();
        this.setLayout(new BorderLayout());
        initDataProcessTool();
        load();
        try{
            initCollaborationService();
        }catch(CollaborationServiceException e){
            System.out.println("Error while initializing Collaboration Service: "+e);
        }
    }

    private void initDataProcessTool(){
        dataProcessPanel = new DataProcessToolPanel();
        add(dataProcessPanel, BorderLayout.CENTER);
        setSize(DataProcessToolPanel.PANEL_WIDTH, DataProcessToolPanel.PANEL_HEIGHT);
        setPreferredSize(getSize());
    }

    public void load(){
        dataProcessPanel.loadData();
        setSize(500,300);
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

    /* initialization Collaboration Service*/
    private void initCollaborationService() throws CollaborationServiceException {
        collaborationService = CollaborationServiceFactory.getCollaborationService(CollaborationServiceFactory.LOCAL_STYLE);
        ArrayList<IScyMessage> scyMessages = collaborationService.synchronizeClientState(User.instance.getUsername(), simulatorName, sessionID, true);
        // find the header message first
        for (IScyMessage message : scyMessages) {
            if (message.getObjectType().equals(TYPE_DATASET_HEADER)) {
                readMessage(message);
            }
        }
        // add all value messages now
        for (IScyMessage message : scyMessages) {
            if (message.getObjectType().equals(TYPE_DATASET_ROW)) {
                readMessage(message);
            }
        }
        collaborationService.addCollaborationListener(this);
    }

    @Override
    public void handleCollaborationServiceEvent(ICollaborationServiceEvent e) {
        IScyMessage scyMessage = e.getScyMessage();
        readMessage(scyMessage);
    }

    private void readMessage(IScyMessage scyMessage){
        if(scyMessage.getToolName() != null && scyMessage.getToolName().equals(simulatorName)){
            String description = scyMessage.getDescription();
            if (scyMessage.getObjectType().equals(TYPE_DATASET_HEADER)){
                try{
                    DataSetHeader header = new DataSetHeader(description);
                    initializeHeader(header);
                }catch(JDOMException ex){
                    System.out.println("Error parsing header: "+ex);
                    return;
                }
            }else if (scyMessage.getObjectType().equals(TYPE_DATASET_ROW)){
                try{
                    DataSetRow row = new DataSetRow(new JDomStringConversion().stringToXml(description));
                    updateDataRow(row);
                }catch(JDOMException ex){
                    System.out.println("Error parsing row: "+ex);
                    return;
                }
            }
        }
    }

    /* initialize model with header*/
    public void initializeHeader(DataSetHeader header){
        //open a new instance of data processing tool with the header
    }

    /* update model with datarow*/
    public void updateDataRow(DataSetRow row){
        // add data row to the instance of data processing tool
    }

    /* import CSV file => EDLO dataset */
    public DataSet importCSVFile(File file){
        if (file  != null)
            return  dataProcessPanel.importCSVFile(file);
        return null;
    }

}
