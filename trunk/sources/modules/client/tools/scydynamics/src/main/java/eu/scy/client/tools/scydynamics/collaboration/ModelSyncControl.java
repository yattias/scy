package eu.scy.client.tools.scydynamics.collaboration;

import java.awt.Color;
import java.awt.Point;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Logger;

import colab.um.draw.JdAux;
import colab.um.draw.JdConst;
import colab.um.draw.JdFigure;
import colab.um.draw.JdFlow;
import colab.um.draw.JdHandle;
import colab.um.draw.JdLink;
import colab.um.draw.JdNode;
import colab.um.draw.JdObject;
import colab.um.draw.JdRelation;
import colab.um.draw.JdStock;

import eu.scy.client.common.datasync.DataSyncException;
import eu.scy.client.common.datasync.ISyncListener;
import eu.scy.client.common.datasync.ISyncSession;
import eu.scy.client.tools.scydynamics.editor.ModelEditor;
import eu.scy.client.tools.scydynamics.editor.ModelSelection;
import eu.scy.common.datasync.ISyncObject;
import eu.scy.common.datasync.SyncObject;

public class ModelSyncControl implements ISyncListener {

	private static final String toolname = "scy-dynamics";
	private static final String id = "id";
	private static final String type = "type";
	private static final String label = "label";
	private static final String unit = "unit";
	private static final String expr = "expr";
	private static final String color = "color";
	private static final String figure1 = "figure1";
	private static final String figure2 = "figure2";
	private static final String x1 = "x1";
	private static final String y1 = "y1";
	private static final String x2 = "x2";
	private static final String y2 = "y2";
	private static final String h1x = "h1x";
	private static final String h1y = "h1y";

	private final static Logger DEBUGLOGGER = Logger.getLogger(ModelSyncControl.class.getName());
	private ModelEditor editor;
	private ISyncSession syncSession;
	private Object removeLock = new Object();
	private Object changeLock = new Object();;

	public ModelSyncControl(ModelEditor editor) {
		this.editor = editor;
		System.out.println("***** ModelSyncControl created.");
	}

	public void setSession(ISyncSession currentSession, boolean writeCurrentStateToServer) {
		System.out.println("***** ModelSyncControl.setSession: " + currentSession);
		DEBUGLOGGER.info("set session: " + currentSession + " / write state: " + writeCurrentStateToServer);
		this.syncSession = currentSession;
		this.syncSession.addSyncListener(this);
		// This is a hack - for some reason the first sync object is not sent
		// to all clients
		this.syncSession.addSyncObject(new SyncObject());

		if (writeCurrentStateToServer) {
			for (JdObject object : editor.getModel().getNodes().values()) {
				if (object instanceof JdNode) {
					DEBUGLOGGER.info("adding " + object.getLabel() + "to session.");
					addNode((JdNode) object);
				}
			}
			for (JdLink link: editor.getModel().getLinks()) {
				DEBUGLOGGER.info("adding " + link.getLabel() + "to session.");
				addLink(link);
			}
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
		syncObject.setProperty(id, aLink.getID());
		syncObject.setProperty(type, aLink.getType() + "");
		syncObject.setProperty(label, aLink.getLabel());
		syncObject.setProperty(figure1, aLink.getFigure1().getID());
		if (aLink.getFigure2() != null) {
			syncObject.setProperty(figure2, aLink.getFigure2().getID());
		}
		syncObject.setProperty(x1, aLink.getPoint1().x + "");
		syncObject.setProperty(y1, aLink.getPoint1().y + "");
		syncObject.setProperty(x2, aLink.getPoint2().x + "");
		syncObject.setProperty(y2, aLink.getPoint2().y + "");
		try {
			JdHandle h1 = (JdHandle) aLink.getHandles().get(0);
			syncObject.setProperty(h1x, h1.getPoint().x+"");
			syncObject.setProperty(h1y, h1.getPoint().y+"");
		} catch (Exception ex) {
			System.out.println("ModelSyncControl.addLink(...) caught: "+ex.getMessage());
		}
		syncSession.addSyncObject(syncObject);
	}

	public void addNode(JdNode aNode) {
		System.out.println("***** ModelSyncControl.addNode.");
		SyncObject syncObject = new SyncObject();
		syncObject.setToolname(toolname);
		syncObject.setCreator(syncSession.getUsername());
		syncObject.setID(aNode.getID());
		syncObject.setProperty(id, aNode.getID());
		syncObject.setProperty(type, aNode.getType() + "");
		syncObject.setProperty(unit, aNode.getUnit() + "");
		syncObject.setProperty(label, aNode.getLabel());
		syncObject.setProperty(color, aNode.getColor().getRGB() + "");
		syncObject.setProperty(x1, aNode.getPoint1().x + "");
		syncObject.setProperty(y1, aNode.getPoint1().y + "");
		syncObject.setProperty(x2, aNode.getPoint2().x + "");
		syncObject.setProperty(y2, aNode.getPoint2().y + "");
		syncObject.setProperty(expr, aNode.getExpr());
		System.out.println("---- adding syncobject for object " + aNode.getLabel());
		syncSession.addSyncObject(syncObject);
	}

	public void removeObject(JdFigure object) {
		System.out.println("***** ModelSyncControl.removeNode.");
		if (syncSession != null) {
			syncSession.removeSyncObject(findSyncObjectByJdObjectID(object.getID()));
		}
	}

	private ISyncObject findSyncObjectByJdObjectID(String jdObjectId) {
		try {
			for (ISyncObject syncObject: syncSession.getAllSyncObjects()) {
				if (jdObjectId.equals(syncObject.getProperty(id))) {
					return syncObject;
				}
			}
		} catch (DataSyncException e) {
			DEBUGLOGGER.warning(e.getMessage());
		}
		System.out.println("***** findSyncObjectByJdObjectID: can't find object with id "+jdObjectId+" in session.");
		return null;
	}

	public void changeObject(JdObject object, Point p1, Point p2) {
		System.out.println("***** ModelSyncControl.changeObject(move).");
		if (syncSession != null) {
			ISyncObject syncObject = findSyncObjectByJdObjectID(object.getID());			
			if (syncObject == null) {
				return;
			}
			syncObject.setLastModificator(syncSession.getUsername());
			// to sure, set id (again?)
			syncObject.setProperty(id, object.getID());
			syncObject.setProperty(x1, p1.x + "");
			syncObject.setProperty(y1, p1.y + "");
			syncObject.setProperty(x2, p2.x + "");
			syncObject.setProperty(y2, p2.y + "");
			syncSession.changeSyncObject(syncObject);
			System.out.println("---- changes sent (modificator: "+syncObject.getLastModificator()+").");			
		}
	}

	public void changeObject(JdFigure figure) {
		System.out.println("***** ModelSyncControl.changeObject.");
		if (syncSession != null) {
			ISyncObject syncObject = findSyncObjectByJdObjectID(figure.getID());
			if (syncObject == null) {
				return;
			}
			syncObject.setLastModificator(syncSession.getUsername());
			// to sure, set id (again?)
			syncObject.setProperty(id, figure.getID());
			syncObject.setProperty(label, figure.getProperties().get("label").toString());
			syncObject.setProperty(unit, figure.getProperties().get("unit").toString());
			Color c = Color.black;
			if (figure instanceof JdObject) {
				c = ((JdObject)figure).getLabelColor();
			} else {
				System.out.println("----- color not retrievable, set to black.");
			}
			System.out.println("---- send color: "+c.toString()+" | "+c.getRGB());
			syncObject.setProperty(color, c.getRGB()+"");
			syncObject.setProperty(expr, figure.getProperties().get("expr").toString());
			syncSession.changeSyncObject(syncObject);
			System.out.println("---- changes sent (modificator: "+syncObject.getLastModificator()+").");
		}
	}

	@Override
	public void syncObjectAdded(ISyncObject syncObject) {
		System.out.println("***** ModelSyncControl.syncObjectAdded. ");
		//System.out.println("----- id: " + syncObject.getProperty(id));
		//System.out.println("----- user: " + syncObject.getCreator() + " (me: " + syncSession.getUsername() + ")");
		//System.out.println("----- properties: " + syncObject.getProperties());
		if ((syncSession == null) || (syncObject.getCreator().equals(syncSession.getUsername())) || (!syncObject.getToolname().equals(toolname))) {
			// session doesn't exist or I am the creator of that object or
			// toolname is not correct
			// -> do nothing
			return;
		}

		if (editor.getModel().getObjectOfId(syncObject.getProperty(id))!= null) {
			System.out.println("-----object already exists locally. doing nothing.");
			// TODO
			// do an objectChanged instead?
			return;
		}

		if (syncObject.getProperty(type).equals("3") // constant
				|| syncObject.getProperty(type).equals("2") // aux
				|| syncObject.getProperty(type).equals("1") // stock
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
		if (syncObject.getProperty(type).equals("6")) {
			newLink = new JdFlow(syncObject.getProperty(label));
		} else if (syncObject.getProperty(type).equals("5")) {
			newLink = new JdRelation(syncObject.getProperty(label), true);
		} else {
			System.out.println("***** syncEdgeAdded: unknown link type '" + syncObject.getProperty(type) + "' received. doing nothing.");
			return;
		}
		newLink.setID(syncObject.getProperty(id));
		JdFigure fig1 = editor.getModel().getObjectOfId(syncObject.getProperty(figure1));
		JdFigure fig2 = editor.getModel().getObjectOfId(syncObject.getProperty(figure2));
		if (fig1 == null) {
			System.out.println("***** syncEdgeAdded: couldn't find link node #1. doing nothing.");
			return;
		}
		newLink.setFigure1(fig1);
		
		if (fig2 != null) {
			newLink.setFigure2(fig2);
			newLink.setPoint2(fig2.getCenter());
		}
		int point1x = Integer.parseInt(syncObject.getProperty(x1));
		int point1y = Integer.parseInt(syncObject.getProperty(y2));
		int point2x = Integer.parseInt(syncObject.getProperty(x2));
		int point2y = Integer.parseInt(syncObject.getProperty(y2));
		//newLink.setPoint1(point1x, point1y);
		//newLink.setPoint2(point2x, point2y);
		newLink.setPoint1(fig1.getCenter());
		newLink.setHandles(new Vector<JdHandle>());
		newLink.addHandle(new JdHandle(newLink, newLink.getPoint1(), 1));
		newLink.addHandle(new JdHandle(newLink, newLink.getPoint2(), 2));
		
		// aLink.movePoint(dragPoint, x, y);
		editor.getModel().addObject(newLink, false);
		editor.setModelChanged();
		editor.checkModel();
		editor.updateCanvas();
	}

	private void syncNodeAdded(ISyncObject syncObject) {
		String l = syncObject.getProperty(label);
		int x = Integer.parseInt(syncObject.getProperty(x1));
		int y = Integer.parseInt(syncObject.getProperty(y1));
		JdNode newNode = null;

		if (syncObject.getProperty(type).equals("1")) {
			newNode = new JdStock(l, x, y);
		} else if (syncObject.getProperty(type).equals("2")) {
			newNode = new JdAux(l, x, y);
		} else if (syncObject.getProperty(type).equals("3")) {
			newNode = new JdConst(l, x, y);
		} else {
			DEBUGLOGGER.warning("unknown node type '" + syncObject.getProperty(type) + "' received. doing nothing.");
			return;
		}

		if (newNode != null) {
			newNode.setLabelColor(new Color(Integer.parseInt(syncObject.getProperty(color))));
			//newNode.setColor(new Color(Integer.parseInt(syncObject.getProperty(color))));
			if (syncObject.getProperty(expr)== null) {
				newNode.setExpr("");
			} else {
				newNode.setExpr((String) syncObject.getProperty(expr));
			}
			newNode.setID((String) syncObject.getProperty(id));
			newNode.setUnit((String) syncObject.getProperty(unit));
			editor.saveModel();
			editor.getModel().addObject(newNode, true);
			editor.setModelChanged();
			editor.updateCanvas();
		}
	}

	@Override
	public void syncObjectChanged(ISyncObject syncObject) {
		System.out.println("**** syncObjectChanged. modificator: "+syncObject.getLastModificator()+" | me: "+syncSession.getUsername());
		System.out.println("----- properties: " + syncObject.getProperties());
		if ((syncSession == null) || ((syncSession.getUsername().equals(syncObject.getLastModificator()))) || (!syncObject.getToolname().equals(toolname))) {
			// session doesn't exist or I am the modificator of that object or
			// toolname is not correct
			// -> do nothing
			System.out.println("---- syncObjectChanged: doing nothing.");
			return;
		}
		JdObject object = editor.getModel().getObjectOfId(syncObject.getProperty(id));
		if (object == null) {
			System.out.println("----- syncObjectChanged: couldn't find object with id "+syncObject.getProperty(id)+" in local model. doing nothing.");
			return;
		}
		synchronized(changeLock) {
			System.out.println("----- syncObjectChanged: applying changes to local object "+object.getLabel());
			System.out.println("----- property keys: "+syncObject.getProperties().keySet());
			editor.saveModel();
			String oldName = object.getLabel();
			Hashtable<String, Object> properties = object.getProperties();
			for (String key : syncObject.getProperties().keySet()) {
				if (key.equals("unit")) {
					//((JdNode) object).setUnit(syncObject.getProperties().get(key));
					properties.put("unit", syncObject.getProperties().get(key));
				} else if (key.equals("label")) {
					//object.setLabel(syncObject.getProperties().get(key));
					properties.put("label", syncObject.getProperties().get(key));
				} else if (key.equals("color")) {
					Color newColor = new Color(Integer.parseInt(syncObject.getProperty(key)));
					//((JdNode) object).setColor(newColor);
					System.out.println("----- received color: "+newColor.toString() + " | "+newColor.getRGB());
					//editor.getModel().getObjectOfName(oldName).setLabelColor(newColor);
					object.setLabelColor(newColor);
					//properties.put("color", newColor);
				} else if (key.equals("expr")) {
					if (syncObject.getProperty(expr)== null) {
						properties.put("expr", "");
					} else {
						properties.put("expr", syncObject.getProperty(key));
					}
				} else if (key.equals("x1")) {
					// the object has moved
					Point point1 = new Point(Integer.parseInt(syncObject.getProperty("x1")), Integer.parseInt(syncObject.getProperty("y1")));
					// TODO uncomment
					//ModelSelection selection = new ModelSelection(editor.getModel());
					//selection.add(object);
					//selection.translate(point1.x-object.getPoint1().x, point1.y-object.getPoint1().y);
				}
			}
			properties.put("id", syncObject.getProperty(id));
			System.out.println("----- changing props NOW.");
			editor.setFigureProperties(oldName, properties);
			//object.setProperties(properties);
			editor.updateCanvas();
		}
	}

	@Override
	public void syncObjectRemoved(ISyncObject syncObject) {
		System.out.println("***** ModelSyncControl.syncObjectRemoved.");
		System.out.println("----- user: " + syncObject.getCreator() + " (me: " + syncSession.getUsername() + ")");
		System.out.println("----- properties: " + syncObject.getProperties());
		if ((syncSession == null) || (syncObject.getCreator().equals(syncSession.getUsername())) || (!syncObject.getToolname().equals(toolname))) {
			// session doesn't exist or I am the creator of that object or
			// toolname is not correct
			// -> do nothing
			return;
		}
		synchronized (removeLock) {
			JdObject object = editor.getModel().getObjectOfId(syncObject.getProperty(id));
			if (object == null) {
				System.out.println("--- couldn't find object locally. doing nothing.");
				//DEBUGLOGGER.warning("could find syncObject locally! id: " + syncObject.getID());
				return;
			}
			System.out.println("--- deleting object with label "+object.getLabel());
			editor.saveModel();
			editor.getModel().removeObjectAndRelations(object);
			editor.setModelChanged();
			editor.checkModel();
			editor.updateCanvas();
		}
	}

}
