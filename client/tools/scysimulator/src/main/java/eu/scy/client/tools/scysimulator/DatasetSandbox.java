package eu.scy.client.tools.scysimulator;

import java.util.Iterator;
import java.util.Locale;
import roolo.elo.JDomStringConversion;
import eu.scy.client.common.datasync.DataSyncException;
import eu.scy.client.common.datasync.ISyncListener;
import eu.scy.client.common.datasync.ISyncSession;
import eu.scy.common.datasync.ISyncObject;
import eu.scy.common.datasync.SyncObject;
import eu.scy.elo.contenttype.dataset.DataSetRow;
import eu.scy.toolbrokerapi.ToolBrokerAPI;

/**
 * 
 * @author lars
 */
public class DatasetSandbox implements ISyncListener {

    private DataCollector datacollector;
    private String TOOL_NAME = "scysimulator";
    private String USER_NAME = "scysimulator-user";
    private ISyncSession currentSession = null;
    private ToolBrokerAPI tbi;

    public DatasetSandbox(DataCollector datacollector, String mucID, ToolBrokerAPI tbi) {
        this.datacollector = datacollector;
        this.tbi = tbi;
        try {
			currentSession = tbi.getDataSyncService().joinSession(mucID, this, TOOL_NAME);
		} catch (DataSyncException e) {
			// TODO handle exception appropriately
			e.printStackTrace();
		}
        sendHeaderMessage();
        sendDataRows();
    }

    public void clear() {
        //for (ISyncObject syncObject : currentSession.getAllSyncObjects()) {
        //    currentSession.removeSyncObject(syncObject);
        //}
    }

    public void disconnect() {
        currentSession.removeSyncListener(this);
    }

    public String getSessionID() {
        return currentSession.getId();
    }

    private void sendDataRows() {
        DataSetRow row;
        for (Iterator<DataSetRow> rows = datacollector.getDataSet().getValues().iterator(); rows.hasNext();) {
            row = rows.next();
            sendDataSetRow(row);
        }
    }

    public void sendDataSetRow(DataSetRow row) {
        ISyncObject syncObject = new SyncObject();
        syncObject.setCreator(this.USER_NAME);
        syncObject.setToolname(this.TOOL_NAME);
        syncObject.setProperty("type", "datasetrow");
        syncObject.setProperty("datasetrow", new JDomStringConversion().xmlToString(row.toXML()));
        currentSession.addSyncObject(syncObject);
    }

    public void sendHeaderMessage() {
    	ISyncObject syncObject = new SyncObject();
        syncObject.setCreator(this.USER_NAME);
        syncObject.setToolname(this.TOOL_NAME);
        syncObject.setProperty("type", "datasetheader");
        syncObject.setProperty("datasetheader", new JDomStringConversion().xmlToString(datacollector.getDataSet().getHeader(Locale.ENGLISH).toXML()));
        currentSession.addSyncObject(syncObject);
    }

    /*
     * These ISyncListener methods are empty because SCYSimulator is only sending action,
     * but not reacting on incoming actions / changes.
     */
    @Override
    public void syncObjectAdded(ISyncObject syncObject) {}

    @Override
    public void syncObjectChanged(ISyncObject syncObject) {}

    @Override
    public void syncObjectRemoved(ISyncObject syncObject) {}

}
