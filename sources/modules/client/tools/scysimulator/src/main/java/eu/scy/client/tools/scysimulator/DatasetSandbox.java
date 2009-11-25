package eu.scy.client.tools.scysimulator;

import eu.scy.client.common.datasync.DataSyncService;
import eu.scy.client.common.datasync.IDataSyncService;
import eu.scy.client.common.datasync.ISyncListener;
import eu.scy.client.common.datasync.ISyncSession;
import eu.scy.collaborationservice.CollaborationServiceException;
import eu.scy.collaborationservice.CollaborationServiceFactory;
import eu.scy.collaborationservice.ICollaborationService;
import eu.scy.common.datasync.ISyncObject;
import eu.scy.common.datasync.SyncObject;
//import eu.scy.communications.message.IScyMessage;
//import eu.scy.communications.message.impl.ScyMessage;
import eu.scy.elo.contenttype.dataset.DataSetRow;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import roolo.elo.JDomStringConversion;

/**
 *
 * @author lars
 */
public class DatasetSandbox implements ISyncListener {
    private DataCollector datacollector;
    private String TOOL_NAME = "scysimulator";
    private String USER_NAME = "merkel";
	private IDataSyncService datasync;
	ISyncSession currentSession = null;
	private XMPPConnection conn;

    DatasetSandbox(DataCollector datacollector) throws CollaborationServiceException {
        this.datacollector = datacollector;
        initCollaborationService();
    }

    public void clear() {
    	for (ISyncObject syncObject : currentSession.getAllSyncObjects()) {
    		currentSession.removeSyncObject(syncObject);
    	}
    }

    private void initCollaborationService() throws CollaborationServiceException {
    	// TODO the datasync instance will be soon delivered by the SCY-lab via TBI
    	ConnectionConfiguration config;
        config = new ConnectionConfiguration("scy.collide.info", 5222);
		conn = new XMPPConnection(config);
		try {
			conn.connect();
			conn.login("merkel", "merkel");
			conn.addConnectionListener(new ConnectionListener() {		
				public void reconnectionSuccessful() {}
				public void reconnectionFailed(Exception arg0) {}
				public void reconnectingIn(int arg0) {}
				public void connectionClosedOnError(Exception arg0) {}
				public void connectionClosed() {}
			});
			
			datasync = new DataSyncService(conn);		
			
		} catch (XMPPException e) {
			e.printStackTrace();
		}		
    }
    
	public void disconnect() {
		currentSession.removeSyncListener(this);
		conn.disconnect();
	}
    
    public ISyncSession createSession() {
    	try {
    		currentSession = datasync.createSession(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		sendHeaderMessage();
		sendDataRows();
		return currentSession;
    }

    private void sendDataRows() {
        DataSetRow row;
        for (Iterator<DataSetRow> rows = datacollector.getDataSet().getValues().iterator(); rows.hasNext();) {
            row = rows.next();
            sendDataSetRow(row);
        }
    }

    public void sendDataSetRow(DataSetRow row) {
    	ISyncObject syncObject = new SyncObject(this.TOOL_NAME, this.USER_NAME);
    	syncObject.setProperty("type", "datasetrow");
    	syncObject.setProperty("datasetrow", new JDomStringConversion().xmlToString(row.toXML()));
    	currentSession.addSyncObject(syncObject);
    }

    public void sendHeaderMessage() {
    	ISyncObject syncObject = new SyncObject(this.TOOL_NAME, this.USER_NAME);
    	syncObject.setProperty("type", "datasetheader");
    	syncObject.setProperty("datasetheader", new JDomStringConversion().xmlToString(datacollector.getDataSet().getHeader(Locale.ENGLISH).toXML()));
    	currentSession.addSyncObject(syncObject);
    }

	@Override
	public void syncObjectAdded(ISyncObject syncObject) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void syncObjectChanged(ISyncObject syncObject) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void syncObjectRemoved(ISyncObject syncObject) {
		// TODO Auto-generated method stub		
	}

}
