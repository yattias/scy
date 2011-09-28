package eu.scy.client.tools.scydynamics.collaboration;

import java.awt.Color;
import java.awt.Point;
import java.util.logging.Logger;

import colab.um.draw.JdAux;
import colab.um.draw.JdConst;
import colab.um.draw.JdFigure;
import colab.um.draw.JdFlow;
import colab.um.draw.JdLink;
import colab.um.draw.JdNode;
import colab.um.draw.JdObject;
import colab.um.draw.JdRelation;
import colab.um.draw.JdStock;

import eu.scy.client.common.datasync.ISyncListener;
import eu.scy.client.common.datasync.ISyncSession;
import eu.scy.client.tools.scydynamics.editor.ModelEditor;
import eu.scy.common.datasync.ISyncObject;
import eu.scy.common.datasync.SyncObject;

public class ModelSyncControl implements ISyncListener {

	private final static Logger DEBUGLOGGER = Logger.getLogger(ModelSyncControl.class.getName());
	private static final String toolname = "scy-dynamics";
	private ModelEditor editor;
	private ISyncSession syncSession;
	private Object removeLock = new Object();

	public ModelSyncControl(ModelEditor editor) {
		this.editor = editor;
		System.out.println("***** ModelSyncControl created.");
	}

	public void setSession(ISyncSession currentSession, boolean writeCurrentStateToServer) {
		System.out.println("***** ModelSyncControl.setSession: "+currentSession);
		DEBUGLOGGER.info("set session: "+currentSession+" / write state: "+writeCurrentStateToServer);
		this.syncSession = currentSession;
		this.syncSession.addSyncListener(this);
		// This is a hack - for some reason the first sync object is not sendt to all clients
		this.syncSession.addSyncObject(new SyncObject());
		
		if (writeCurrentStateToServer) {
			// TODO
//			for (INodeModel node : model.getNodes()) {
//				add(node);
//			}
//			for (ILinkModel link : model.getLinks()) {
//				add(link);
//			}
		}
	}

	public ISyncSession getSession() {
		return this.syncSession;
	}
	
	public void addLink(JdLink aLink) {
		System.out.println("***** ModelSyncControl.addLink.");
		SyncObject syncObject = new SyncObject();
		syncObject.setToolname(toolname);
		syncObject.setCreator(syncSession.getUsername());
		syncObject.setID(aLink.getID());
		syncObject.setProperty("id", aLink.getID());
		syncObject.setProperty("type", aLink.getType()+"");
		syncObject.setProperty("label", aLink.getLabel());		
		syncObject.setProperty("figure1", aLink.getFigure1().getID());
		syncObject.setProperty("figure2", aLink.getFigure2().getID());
		syncObject.setProperty("point1x", aLink.getPoint1().x+"");
		syncObject.setProperty("point1y", aLink.getPoint1().y+"");
		syncObject.setProperty("point2x", aLink.getPoint2().x+"");
		syncObject.setProperty("point2y", aLink.getPoint2().y+"");
		syncSession.addSyncObject(syncObject);
	}
	
	public void addNode(JdNode aNode) {
		System.out.println("***** ModelSyncControl.addNode.");
		SyncObject syncObject = new SyncObject();
		syncObject.setToolname(toolname);
		syncObject.setCreator(syncSession.getUsername());
		syncObject.setID(aNode.getID());
		syncObject.setProperty("id", aNode.getID());
		syncObject.setProperty("type", aNode.getType()+"");
		syncObject.setProperty("unit", aNode.getUnit()+"");		
		syncObject.setProperty("label", aNode.getLabel());
		syncObject.setProperty("color", aNode.getColor().getRGB()+"");
		syncObject.setProperty("x", aNode.getPoint1().x+"");
		syncObject.setProperty("y", aNode.getPoint1().y+"");
		syncObject.setProperty("expr", aNode.getExpr());
		syncSession.addSyncObject(syncObject);
	}
	
	@Override
	public void syncObjectAdded(ISyncObject syncObject) {
		System.out.println("***** ModelSyncControl.syncObjectAdded. ");
		System.out.println("----- syncsession: "+syncSession);
		System.out.println("----- tool: "+syncObject.getToolname());
		System.out.println("----- user: "+syncObject.getCreator()+" (me: "+syncSession.getUsername()+")");
		System.out.println("----- properties: "+syncObject.getProperties());
		if (	(syncSession == null)
				|| (syncObject.getCreator().equals(syncSession.getUsername()))
				|| (!syncObject.getToolname().equals(toolname))) {
			// session doesn't exist or I am the creator of that object or toolname is not correct
			// -> do nothing
			return;
		}
		
		if (syncObject.getProperty("type").equals("3") // constant
				||	syncObject.getProperty("type").equals("2") // aux
				||	syncObject.getProperty("type").equals("1") // stock
		) {
			// it's a node...
			syncNodeAdded(syncObject);
		} else {
			// it's an edge...
			syncEdgeAdded(syncObject);
		}
	}
	
	private void syncEdgeAdded(ISyncObject syncObject) {
		JdLink newLink = null;
		if (syncObject.getProperty("type").equals("6")) {						
			newLink = new JdFlow(syncObject.getProperty("label"));
			//((JdFlow)newLink).
		} else if (syncObject.getProperty("type").equals("5")) {
			newLink = new JdRelation(syncObject.getProperty("label"), true);
		} else {
			DEBUGLOGGER.warning("unknown link type '"+syncObject.getProperty("type")+"' received. doing nothing.");
			return;
		}
		newLink.setID(syncObject.getID());
		JdFigure figure1 = editor.getModel().getObjectOfId(syncObject.getProperty("figure1"));
		JdFigure figure2 = editor.getModel().getObjectOfId(syncObject.getProperty("figure2"));
		if (figure1 == null || figure2 == null) {
			DEBUGLOGGER.warning("couldn't find the link nodes. doing nothing.");
			return;
		}
		newLink.setFigure1(figure1);
		newLink.setFigure2(figure2);
        
		int point1x = Integer.parseInt(syncObject.getProperty("point1x"));
		int point1y = Integer.parseInt(syncObject.getProperty("point1y"));
		int point2x = Integer.parseInt(syncObject.getProperty("point2x"));
		int point2y = Integer.parseInt(syncObject.getProperty("point2y"));
		newLink.setPoints(point1x, point1y, point2x, point2y);
		newLink.clearHandles();
		
		//newLink.setPoint1(x, y);
        //aLink.movePoint(dragPoint, x, y);
        editor.getModel().addObject(newLink, false);
        editor.setModelChanged();
        editor.checkModel();
        editor.updateCanvas();
	}
	
	private void syncNodeAdded(ISyncObject syncObject) {
		String label = syncObject.getProperty("label");
		int x = Integer.parseInt(syncObject.getProperty("x"));
		int y = Integer.parseInt(syncObject.getProperty("y"));
		JdNode newNode = null;

		if (syncObject.getProperty("type").equals("1")) {						
			newNode = new JdStock(label, x, y);
		} else if (syncObject.getProperty("type").equals("2")) {
			newNode = new JdAux(label, x, y);
		} else if (syncObject.getProperty("type").equals("3")) {
			newNode = new JdConst(label, x, y);
		} else {
			DEBUGLOGGER.warning("unknown node type '"+syncObject.getProperty("type")+"' received. doing nothing.");
			return;
		}
		
		if (newNode != null) {
			newNode.setColor(new Color(Integer.parseInt(syncObject.getProperty("color"))));
			newNode.setExpr((String)syncObject.getProperty("expr"));
			newNode.setID((String)syncObject.getProperty("id"));
			newNode.setUnit((String)syncObject.getProperty("unit"));
			editor.saveModel();
			editor.getModel().addObject(newNode, true);
			editor.setModelChanged();
			editor.updateCanvas();
		}
	}

	@Override
	public void syncObjectChanged(ISyncObject syncObject) {
		if (	(syncSession == null)
				|| (syncObject.getCreator().equals(syncSession.getUsername()))
				|| (!syncObject.getToolname().equals(toolname))) {
			// session doesn't exist or I am the creator of that object or toolname is not correct
			// -> do nothing
			return;
		}
		JdObject object = editor.getModel().getObjectOfId(syncObject.getID());
		if (object == null) {
			DEBUGLOGGER.warning("could find syncObject locally! id: "+syncObject.getID());
			return;
		}
		
		for (String key: syncObject.getProperties().keySet()) {
			if (key.equals("unit")) {
				((JdNode)object).setUnit(syncObject.getProperties().get(key));
			} else if (key.equals("label")) {
				object.setLabel(syncObject.getProperties().get(key));
			} else if (key.equals("color")) {
				((JdNode)object).setColor(new Color(Integer.parseInt(syncObject.getProperty(key))));
			} else if (key.equals("expr")) {
				((JdNode)object).setExpr(syncObject.getProperty(key));
			} else if (key.equals("x")) {
				Point point = new Point(Integer.parseInt(syncObject.getProperty("x")), Integer.parseInt(syncObject.getProperty("y")));
				// TODO
				//((JdNode)object).setExpr(syncObject.getProperty(key));
			}
		}
		editor.saveModel();
		editor.setModelChanged();
		editor.updateCanvas();
	}

	@Override
	public void syncObjectRemoved(ISyncObject syncObject) {
		System.out.println("***** ModelSyncControl.syncObjectRemoved.");
		System.out.println("----- syncsession: "+syncSession);
		System.out.println("----- tool: "+syncObject.getToolname());
		System.out.println("----- user: "+syncObject.getCreator()+" (me: "+syncSession.getUsername()+")");
		System.out.println("----- properties: "+syncObject.getProperties());
		if (	(syncSession == null)
				|| (syncObject.getCreator().equals(syncSession.getUsername()))
				|| (!syncObject.getToolname().equals(toolname))) {
			// session doesn't exist or I am the creator of that object or toolname is not correct
			// -> do nothing
			return;
		}
		synchronized (removeLock) {
			JdObject object = editor.getModel().getObjectOfId(syncObject.getID());
			if (object == null) {
				System.out.println("++++ couldn't find object locally.");
				DEBUGLOGGER.warning("could find syncObject locally! id: "+syncObject.getID());
				return;
			}
			editor.saveModel();
			editor.getModel().removeObjectAndRelations(object);
			editor.setModelChanged();
			editor.checkModel();
			editor.updateCanvas();
		}
	}

	public void removeObject(JdFigure object) {
		System.out.println("***** ModelSyncControl.removeNode.");
		if (syncSession != null) {
			SyncObject syncObject = new SyncObject();
			syncObject.setToolname(toolname);
			syncObject.setCreator(syncSession.getUsername());
			syncObject.setID(object.getID());
			syncSession.removeSyncObject(syncObject);
		}
	}

}
