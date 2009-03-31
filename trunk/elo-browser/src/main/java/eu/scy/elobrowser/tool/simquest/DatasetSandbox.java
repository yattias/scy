package eu.scy.elobrowser.tool.simquest;

import eu.scy.collaborationservice.CollaborationServiceException;
import eu.scy.collaborationservice.CollaborationServiceFactory;
import eu.scy.collaborationservice.ICollaborationService;
import eu.scy.communications.message.IScyMessage;
import eu.scy.communications.message.impl.ScyMessage;
import eu.scy.elo.contenttype.dataset.DataSetRow;
import java.util.Iterator;
import java.util.Locale;
import roolo.elo.JDomStringConversion;

/**
 *
 * @author lars
 */
public class DatasetSandbox {
    private DataCollector datacollector;
    private ICollaborationService collaborationService;
    public static String SESSION_ID = "datasetsandbox";
    public static String TOOL_NAME = "simulator";
    public static String USER_NAME = "scy_user";


    DatasetSandbox(DataCollector datacollector) throws CollaborationServiceException {
        this.datacollector = datacollector;
        initCollaborationService();
        clear();
        sendHeaderMessage();
        sendDataRows();
    }

    public void clear() {
        collaborationService.cleanSession(SESSION_ID);
    }

    private void initCollaborationService() throws CollaborationServiceException {
        collaborationService = CollaborationServiceFactory.getCollaborationService(CollaborationServiceFactory.LOCAL_STYLE);
        collaborationService.synchronizeClientState(USER_NAME, TOOL_NAME, SESSION_ID, false);
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
                TOOL_NAME, //toolName
                //UUID.randomUUID().toString(), //id
                "1234", //id
                "datasetrow", //objectType
                "some name", //name
                new JDomStringConversion().xmlToString(row.toXML()), //description
                null, // to
                null, // from
                "datasetsandbox", // message purpose
                0,  // expiration time
                SESSION_ID); // session

        send(datarowmessage);
    }

    private void sendHeaderMessage() {
        IScyMessage headermessage = ScyMessage.createScyMessage(
                "lars@simulator",  //username
                TOOL_NAME, //toolName
                //UUID.randomUUID().toString(), //id
                "1234", //id
                "datasetheader", //objectType
                "some name", //name
                new JDomStringConversion().xmlToString(datacollector.getDataSet().getHeader(Locale.ENGLISH).toXML()), //description
                null, // to
                null, // from
                "datasetsandbox", // message purpose
                0,  // expiration time
                SESSION_ID); // session

        send(headermessage);
    }

}
