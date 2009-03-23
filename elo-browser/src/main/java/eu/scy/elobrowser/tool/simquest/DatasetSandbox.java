package eu.scy.elobrowser.tool.simquest;

import eu.scy.collaborationservice.CollaborationServiceException;
import eu.scy.collaborationservice.CollaborationServiceFactory;
import eu.scy.collaborationservice.ICollaborationService;
import eu.scy.communications.message.IScyMessage;
import eu.scy.communications.message.impl.ScyMessage;
import eu.scy.elo.contenttype.dataset.DataSetRow;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import roolo.elo.JDomStringConversion;

/**
 *
 * @author lars
 */
public class DatasetSandbox {
    private DataCollector datacollector;
    private ICollaborationService collaborationService;
    private static String SESSIONID = "42";

    DatasetSandbox(DataCollector datacollector) throws CollaborationServiceException {
        this.datacollector = datacollector;
        initCollaborationService();
        clear();
        sendHeaderMessage();
        sendDataRows();
    }

    public void clear() {
        List<IScyMessage> messages = collaborationService.synchronizeClientState("simulator", SESSIONID);
        for(IScyMessage message : messages) {
            System.out.println("DatasetSandbax deleting: "+message.getId());
            try {
                collaborationService.delete(message.getId());
            } catch (CollaborationServiceException ex) {
                Logger.getLogger(DatasetSandbox.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void initCollaborationService() throws CollaborationServiceException {
        collaborationService = CollaborationServiceFactory.getCollaborationService(CollaborationServiceFactory.LOCAL_STYLE);
    }

    private void send(IScyMessage message) {
        System.out.println("DatasetSandbox sending:\n"+message.toString());
        try {
            collaborationService.create(message);
        } catch (CollaborationServiceException ex) {
            ex.printStackTrace();
        }
    }

    private void sendDataRows() {
        DataSetRow row;
        for (Iterator<DataSetRow> rows = datacollector.getDataSet().getValues().iterator(); rows.hasNext();) {
            row = rows.next();
            sendDataSetRow(row);
        }
    }

    public void sendDataSetRow(DataSetRow row) {
        IScyMessage datarowmessage = ScyMessage.createScyMessage(
                "lars@simulator",  //username
                "simulator", //toolName
                UUID.randomUUID().toString(), //id
                "datasetrow", //objectType
                "some name", //name
                new JDomStringConversion().xmlToString(row.toXML()), //description
                null, // to
                null, // from
                "dataset-sandbox", // message purpose
                0,  // expiration time
                SESSIONID); // session

        send(datarowmessage);
    }

    private void sendHeaderMessage() {
        IScyMessage headermessage = ScyMessage.createScyMessage(
                "lars@simulator",  //username
                "simulator", //toolName
                UUID.randomUUID().toString(), //id
                "datasetheader", //objectType
                "some name", //name
                new JDomStringConversion().xmlToString(datacollector.getDataSet().getHeader(Locale.ENGLISH).toXML()), //description
                null, // to
                null, // from
                "dataset-sandbox", // message purpose
                0,  // expiration time
                SESSIONID); // session

        send(headermessage);
    }

}
