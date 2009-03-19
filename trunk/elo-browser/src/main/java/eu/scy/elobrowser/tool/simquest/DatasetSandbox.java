package eu.scy.elobrowser.tool.simquest;

import eu.scy.collaborationservice.CollaborationServiceException;
import eu.scy.collaborationservice.CollaborationServiceFactory;
import eu.scy.collaborationservice.ICollaborationService;
import eu.scy.communications.message.IScyMessage;
import eu.scy.communications.message.impl.ScyMessage;
import java.util.Iterator;
import java.util.Locale;
import roolo.elo.JDomStringConversion;
import roolo.elo.content.dataset.DataSetRow;

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

    void clear() {
        //TODO empty the session
    }

    private void initCollaborationService() throws CollaborationServiceException {
        collaborationService = CollaborationServiceFactory.getCollaborationService(CollaborationServiceFactory.LOCAL_STYLE);
        collaborationService.synchronizeClientState("simulator", SESSIONID);
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
                "1234", //id
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
                "1234", //id
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
