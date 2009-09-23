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

    public String sqlSpacesServerHost;
    public int sqlSpacesServerPort;
    public String sqlSpacesServerSpaceDatasync;

    public String datasyncServerHost;
    public String datasyncServerPort;
    public String datasyncMessageHub;
    public String datasyncMessageHubAddress;
    public long datasyncMessageDefaultExpiration;
    
    public String datasyncExternalComponentHost;
    public int datasyncExternalComponentPort;
    public String datasyncExternalComponentSecretKey;

    public String clientEventQuery;
    public String clientEventCreateSession;
    public String clientEventCreateData;
    public String clientEventSynchronize;
    public String clientEventGetSessions;
    public String clientEventJoinSession;

    public Properties props = new Properties();
    
    
    /*
     * This class is deprecated and should not be used anymore.
     */
    private CommunicationProperties() {
    	System.out.println("CommunicationProperties initializing...");
        try {
            props.load(CommunicationProperties.class.getResourceAsStream("communication.properties"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        sqlSpacesServerHost = props.getProperty("sqlspaces.server.host");
        System.out.println("sqlspaces.server.host = "+sqlSpacesServerHost);
        
        sqlSpacesServerPort = Integer.parseInt(props.getProperty("sqlspaces.server.port"));
        sqlSpacesServerSpaceDatasync = props.getProperty("sqlspaces.server.space.datasync");

        datasyncServerHost = props.getProperty("datasync.server.host");
        System.out.println("datasync.server.host = "+datasyncServerHost);
        datasyncServerPort = props.getProperty("datasync.server.port");
        datasyncMessageHub = props.getProperty("datasync.messageHub");
        datasyncMessageHubAddress = datasyncMessageHub + "." + datasyncServerHost;
        datasyncMessageDefaultExpiration = Long.parseLong(props.getProperty("datasync.message.default.expiration"));
        
        datasyncExternalComponentHost = props.getProperty("datasync.server.externalComponent.host");
        System.out.println("datasync.server.externalComponent.host = "+datasyncExternalComponentHost);
        datasyncExternalComponentPort = Integer.parseInt(props.getProperty("datasync.server.externalComponent.port"));
        datasyncExternalComponentSecretKey = props.getProperty("datasync.server.externalComponent.secretKey");

        clientEventQuery = props.getProperty("client.event.query");
        clientEventCreateSession = props.getProperty("client.event.create.session");
        clientEventCreateData = props.getProperty("client.event.create.data");
        clientEventSynchronize = props.getProperty("client.event.synchronize");    
        clientEventGetSessions = props.getProperty("client.event.get.sessions");
        clientEventJoinSession = props.getProperty("client.event.join.session");
    }
}
