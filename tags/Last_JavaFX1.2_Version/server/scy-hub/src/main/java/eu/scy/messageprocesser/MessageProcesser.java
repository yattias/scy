package eu.scy.messageprocesser;

import java.util.ArrayList;
import java.util.List;

import org.xmpp.packet.Message;

/**
 * Processes a message and routes it to the correct service.
 * 
 * @author anthonjp
 *
 */
public class MessageProcesser {
	
	List<IRelayMessagerListener> relayListeners = new ArrayList<IRelayMessagerListener>();
//	IActivityCoordintorModule activityCoordinator;
//	IDataSyncModule dataSyncModule;
	
	public MessageProcesser() {
		initModules();
	}

	private void initModules() {
//		activityCoordinator = ActivityCoordintorModuleFactory.getActivityCoordinator(ActivityCoordintorModuleFactory.HIBERNATE);
//		dataSyncModule = DataSyncModuleFactory.getDataSync(DataSyncModuleFactory.SQLSPACES);
	} 
	
	public void addRelayListener(IRelayMessagerListener relayListener){
		relayListeners.add(relayListener);
	}
	
	public void processMessage(Message messageToProcess) {
		
	}
}
