package eu.scy.communications.datasync.properties;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;


/**
 * Holds properties that are accessed throughout scy, both tool and server side.
 * 
 * @author thomasd
 *
 */
public class CommunicationProperties {

    public String datasyncServerHost;
    public String datasyncServerPort;
    public String datasyncMessageHub;
    public String datasyncMessageHubAddress;
    public long datasyncMessageDefaultExpiration;

    public String clientEventQuery;
    public String clientEventCreateSession;
    public String clientEventCreateData;
    public String clientEventSynchronize;
    public String clientEventGetSessions;
    public Properties props = new Properties();
    
    
    public CommunicationProperties() {
        try {
            props.load(CommunicationProperties.class.getResourceAsStream("communication.properties"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        datasyncServerHost = props.getProperty("datasync.server.host");
        datasyncServerPort = props.getProperty("datasync.server.port");
        datasyncMessageHub = props.getProperty("datasync.messagehub");
        datasyncMessageHubAddress = datasyncMessageHub + "." + datasyncServerHost;
        datasyncMessageDefaultExpiration = Long.parseLong(props.getProperty("datasync.message.default.expiration"));
        
        clientEventQuery = props.getProperty("client.event.query");
        clientEventCreateSession = props.getProperty("client.event.create.session");
        clientEventCreateData = props.getProperty("client.event.create.data");
        clientEventSynchronize = props.getProperty("client.event.synchronize");    
        clientEventGetSessions = props.getProperty("client.event.get.sessions");
    }
}
