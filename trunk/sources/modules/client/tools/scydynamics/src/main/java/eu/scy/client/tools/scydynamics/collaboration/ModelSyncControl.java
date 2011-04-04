package eu.scy.client.tools.scydynamics.collaboration;

import java.util.logging.Logger;

import colab.um.draw.JdNode;
import colab.um.draw.JdStock;

import eu.scy.client.common.datasync.ISyncListener;
import eu.scy.client.common.datasync.ISyncSession;
import eu.scy.client.tools.scydynamics.editor.ModelEditor;
import eu.scy.common.datasync.ISyncObject;
import eu.scy.common.datasync.SyncObject;

public class ModelSyncControl implements ISyncListener {

	private final static Logger DEBUGLOGGER = Logger.getLogger(ModelSyncControl.class.getName());
	private ModelEditor editor;
	private ISyncSession syncSession;

	public ModelSyncControl(ModelEditor editor, ISyncSession syncSession) {
		this.editor = editor;
		this.syncSession = syncSession;
		this.syncSession.addSyncListener(this);
		// This is a hack - for some reason the first sync object is not sendt to all clients
		this.syncSession.addSyncObject(new SyncObject());
	}
	
	public void addNode(JdNode aNode) {
		SyncObject syncObject = new SyncObject();
		syncObject.setToolname("scydynamics");
		syncObject.setProperty("id", aNode.getID());
		syncObject.setProperty("type", aNode.getType()+"");		
		syncObject.setProperty("label", aNode.getLabel());
		syncObject.setProperty("color", aNode.getColor().toString());
		syncObject.setProperty("x", aNode.getPoint1().x+"");
		syncObject.setProperty("y", aNode.getPoint1().y+"");
		syncObject.setProperty("expr", aNode.getExpr());
		syncSession.addSyncObject(syncObject);
	}
	
	@Override
	public void syncObjectAdded(ISyncObject syncObject) {
		if (syncSession == null  || syncObject.getCreator().equals(syncSession.getUsername())) {
			// session doesn't exist or I am the creator of that object
			// -> do nothing
			return;
		}
		
		if (syncObject.getProperty("type").equals("1") // stock
				||	syncObject.getProperty("type").equals("2") // aux
				||	syncObject.getProperty("type").equals("3") // constant				
		) {
			syncNodeAdded(syncObject);
		}
		// TODO syncEdgeEdded
	}
	
	private void syncNodeAdded(ISyncObject syncObject) {
		String label = syncObject.getProperty("label");
		int x = Integer.parseInt(syncObject.getProperty("x"));
		int y = Integer.parseInt(syncObject.getProperty("y"));
		
		if (syncObject.getProperty("type").equals("1")) {
			
			//JdStock newNode = new JdStock(syncObject.getProperty("label"));
		} else if (syncObject.getProperty("type").equals("2")) {
			
		} if (syncObject.getProperty("type").equals("3")) {
			
		} else {
			DEBUGLOGGER.warning("unknown node type '"+syncObject.getProperty("type")+"' received. doing nothing.");
		}
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
