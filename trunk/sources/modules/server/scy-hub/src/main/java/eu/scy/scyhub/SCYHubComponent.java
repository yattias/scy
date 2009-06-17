package eu.scy.scyhub;

import java.util.Date;

import org.xmpp.component.Component;
import org.xmpp.component.ComponentManager;
import org.xmpp.component.ComponentManagerFactory;
import org.xmpp.packet.JID;
import org.xmpp.packet.Message;
import org.xmpp.packet.Packet;
import org.xmpp.packet.PacketExtension;

import eu.scy.datasync.api.DataSyncException;
import eu.scy.datasync.api.IDataSyncModule;
import eu.scy.datasync.api.ISyncMessage;
import eu.scy.datasync.api.event.IDataSyncEvent;
import eu.scy.datasync.api.event.IDataSyncListener;
import eu.scy.datasync.api.session.IDataSyncSession;
import eu.scy.datasync.extension.DataSyncPacketExtension;
import eu.scy.datasync.impl.factory.DataSyncModuleFactory;

/**
 * SCYHub is the central communicator, processes packets and routes them to the correct 
 * places
 *
 * @author anthonjp
 */
public class SCYHubComponent implements Component {

    private IDataSyncModule dataSyncModule;
	private IDataSyncSession dataSyncSession;

	public String getName() {
        return "SCY HUB";
    }

    public String getDescription() {
        return "SCY Hub Component";
    }

	/**
	 * process the packet and route the it to the correct place 
	 */
    public void processPacket(Packet packet) {
        System.out.println("Received package:"+packet.toXML());
        // Only process Message packets
        if (packet instanceof Message) {
            // Get the requested station to obtain it's weather information
            Message message = (Message) packet;
            
            DataSyncPacketExtension dataSyncPacketExtension = (DataSyncPacketExtension) message.getExtension(DataSyncPacketExtension.ELEMENT_NAME, DataSyncPacketExtension.NAMESPACE);
            
            if( dataSyncPacketExtension instanceof DataSyncPacketExtension ) {
            	
            	DataSyncPacketExtension dsp = (DataSyncPacketExtension)dataSyncPacketExtension;
            	 try {
                     // pass syncMessage to DataSyncModule for storing
                     dataSyncModule.create(dsp.toPojo());
                 } catch (DataSyncException e1) {
                     e1.printStackTrace();
                 }
            	
            }
            
            
//            ISyncMessage syncMessage = SyncMessage.createSyncMessage("12", "toolid", "bob", "something something", "EVENT", null, SyncMessage.DEFAULT_MESSAGE_EXPIRATION_TIME);
//           
//            DataSyncPacketExtension dsp = new DataSyncPacketExtension(syncMessage);
//            
//            Message newMessage = new Message();
//            
//            newMessage.setTo("scyhub.imediamac09.uio.no");
//            newMessage.addExtension(dsp);
//            
//            try {
//				ComponentManagerFactory.getComponentManager().sendPacket(this, newMessage);
//			} catch (ComponentException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
            
            // Send the request and get the weather information
////            Metar metar = Weather.getMetar(station, 5000);
//
//            // Build the answer
//            Message reply = new Message();
//            reply.setTo(message.getFrom());
//            reply.setFrom(message.getTo());
//            reply.setType(message.getType());
//            reply.setThread(message.getThread());
//
//            // Append the discovered information if something was found
//            if (metar != null) {
//                StringBuilder sb = new StringBuilder();
//                sb.append("station id : " + metar.getStationID());
//                sb.append("\rwind dir   : " + metar.getWindDirection() + " degrees");
//                sb.append("\rwind speed : " + metar.getWindSpeedInMPH() + " mph, " +
//                                                     metar.getWindSpeedInKnots() + " knots");
//                if (!metar.getVisibilityLessThan()) {
//                    sb.append("\rvisibility : " + metar.getVisibility() + " mile(s)");
//                } else {
//                    sb.append("\rvisibility : < " + metar.getVisibility() + " mile(s)");
//                }
//
//                sb.append("\rpressure   : " + metar.getPressure() + " in Hg");
//                sb.append("\rtemperaturePrecise: " +
//                                   metar.getTemperaturePreciseInCelsius() + " C, " +
//                                   metar.getTemperaturePreciseInFahrenheit() + " F");
//                sb.append("\rtemperature: " +
//                                   metar.getTemperatureInCelsius() + " C, " +
//                                   metar.getTemperatureInFahrenheit() + " F");
//                sb.append("\rtemperatureMostPrecise: " +
//                                   metar.getTemperatureMostPreciseInCelsius() + " C, " +
//                                   metar.getTemperatureMostPreciseInFahrenheit() + " F");
//                reply.setBody(sb.toString());
//            }
//            else {
//                // Answer that the requested station id does not exist
//                reply.setBody("Unknown station ID");
//            }
//
//            // Send the response to the sender of the request
//            try {
//                ComponentManagerFactory.getComponentManager().sendPacket(this, reply);
//            } catch (ComponentException e) {
//                ComponentManagerFactory.getComponentManager().getLog().error(e);
//            }
        }
    }

    public void initialize(JID jid, ComponentManager componentManager) {
    	System.out.println("SCYHubComponent.initialize()");
    	initModules();
    }

    private void initModules() {
		//data sync
    	  try {
              
              dataSyncModule = DataSyncModuleFactory.getDataSyncModule(DataSyncModuleFactory.LOCAL_STYLE);
              // add listner in order to get callbacks on stuff that's happening
              dataSyncModule.addDataSyncListener(new IDataSyncListener(){
			
				@Override
				public void handleDataSyncEvent(IDataSyncEvent e) {
					// ComponentManagerFactory.getComponentManager().sendPacket(this, packet)
				      ISyncMessage syncMessage = e.getSyncMessage();
				      if (syncMessage.getFrom() != null) {            
				            Date date = new java.util.Date(System.currentTimeMillis());
				            java.sql.Timestamp ts = new java.sql.Timestamp(date.getTime());
				            System.out.println("SCYHubComponent.initModules()");
				      } 
					
				}
			});
              //create new session
              String HARD_CODED_TOOL_NAME = "eu.scy.scyhub";
              String HARD_CODED_USER_NAME = "thomasd";
              dataSyncSession = dataSyncModule.createSession(HARD_CODED_TOOL_NAME, HARD_CODED_USER_NAME);
          } catch (DataSyncException e) {
              e.printStackTrace();
          }        
	}

	public void start() {
    	System.out.println("SCYHubComponent.start()");
    }

    public void shutdown() {
    	System.out.println("SCYHubComponent.shutdown()");
    }
}
