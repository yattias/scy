package eu.scy.communications;

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
    public String clientEventQuery;
    public String clientEventCreate;
    public String clientEventSynchronize;
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
        clientEventQuery = props.getProperty("client.event.query");
        clientEventCreate = props.getProperty("client.event.create");
        clientEventSynchronize = props.getProperty("client.event.synchronize");       
    }
}
