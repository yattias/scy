/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.elobrowser.tool.dataProcessTool;

import eu.scy.collaborationservice.CollaborationServiceException;
import eu.scy.collaborationservice.CollaborationServiceFactory;
import eu.scy.collaborationservice.ICollaborationService;
import eu.scy.collaborationservice.event.ICollaborationServiceEvent;
import eu.scy.collaborationservice.event.ICollaborationServiceListener;
import eu.scy.communications.message.IScyMessage;
import eu.scy.elo.contenttype.dataset.DataSetHeader;
import eu.scy.elo.contenttype.dataset.DataSetRow;
import eu.scy.elobrowser.tool.simquest.DatasetSandbox;
import javax.swing.JPanel;
import eu.scy.tools.dataProcessTool.dataTool.*;

import java.awt.BorderLayout;
import java.util.ArrayList;
import org.jdom.Element;
import org.jdom.JDOMException;
import roolo.elo.JDomStringConversion;

/**
 * Entry point of the data process visualization tool,
 * interface between the tool and ToolBrokerAPI
 * @author Marjolaine
 */
public class DataToolPanel extends JPanel implements ICollaborationServiceListener {
    /* object type dataset header*/
    public static final String TYPE_DATASET_HEADER = "datasetheader";
    /* object type dataset row*/
    public static final String TYPE_DATASET_ROW = "datasetrow";

    /* data process visualization tool */
    private MainDataToolPanel dataProcessVisualizationTool;

    /* id dataset creation */
    private long idDataset = -1;

    /* collaboration service*/
    private ICollaborationService collaborationService;
    /* simulator Tool Name */
    private String simulatorName = DatasetSandbox.TOOL_NAME;
    /* SessionID*/
    private  String sessionID = DatasetSandbox.SESSION_ID;
    
    /* Constructor data Tool panel - blank */
    public DataToolPanel() {
        super();
        this.setLayout(new BorderLayout());
        this.dataProcessVisualizationTool = new MainDataToolPanel();
        this.add(this.dataProcessVisualizationTool, BorderLayout.CENTER);
        setSize(MainDataToolPanel.PANEL_WIDTH, MainDataToolPanel.PANEL_HEIGHT);
        setPreferredSize(getSize());
        try{
            initCollaborationService();
        }catch(CollaborationServiceException e){
            //this.dataProcessVisualizationTool.displayError("Error during initializing Collaboration Service : "+e);
            System.out.println("Error during initializing Collaboration Service: "+e);
        }
    }

    /* resize */
    private void resizePanel(){
        System.out.println("resize data tool panel : "+this.getWidth()+", "+this.getHeight());
    }
    
    /* load ELO into data process tool */
    public void loadELO(String xmlContent){
        this.dataProcessVisualizationTool.loadELO(xmlContent);
    }

    public void newElo(){
        this.dataProcessVisualizationTool.createTable("NEW");
    }

    public Element getPDS(){
        return this.dataProcessVisualizationTool.getPDS();
    }

    /* initialize model with header*/
    public void initializeHeader(DataSetHeader header){
        idDataset = this.dataProcessVisualizationTool.createDataset("simquest dataset", header);
        repaint();
    }

    /* update model with datarow*/
    public void updateDataRow(DataSetRow row){
        this.dataProcessVisualizationTool.addData(idDataset, row);
        repaint();
    }

    /* initialization Collaboration Service*/
    private void initCollaborationService() throws CollaborationServiceException {
        collaborationService = CollaborationServiceFactory.getCollaborationService(CollaborationServiceFactory.LOCAL_STYLE);
        ArrayList<IScyMessage> scyMessages = collaborationService.synchronizeClientState(simulatorName, sessionID);
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

    /**/
    private void readMessage(IScyMessage scyMessage){
        if(scyMessage.getToolName() != null && scyMessage.getToolName().equals(simulatorName)){
            String description = scyMessage.getDescription();
            if (scyMessage.getObjectType().equals(TYPE_DATASET_HEADER)){
                try{
                    DataSetHeader header = new DataSetHeader(description);
                    initializeHeader(header);
                }catch(JDOMException ex){
                    //this.dataProcessVisualizationTool.displayError("Error parsing header : "+ex);
                    System.out.println("Error parsing header: "+ex);
                    return;
                }
            }else if (scyMessage.getObjectType().equals(TYPE_DATASET_ROW)){
                try{
                    DataSetRow row = new DataSetRow(new JDomStringConversion().stringToXml(description));
                    updateDataRow(row);
                }catch(JDOMException ex){
                    //this.dataProcessVisualizationTool.displayError("Error parsing row : "+ex);
                    System.out.println("Error parsing row: "+ex);
                    return;
                }
            }
        }
    }
    @Override
    public void handleCollaborationServiceEvent(ICollaborationServiceEvent e) {
        IScyMessage scyMessage = e.getScyMessage();
        readMessage(scyMessage);
    }
    
}
