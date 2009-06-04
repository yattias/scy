package eu.scy.client.tools.scydynamics.logging;

import java.util.List;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class ActionLogSQLSpacesHandler {

	private TupleSpace ts;
	private Field idField;
	private Field xmlField;
	private Field userField;
	private Field typeField;
	private Field timeField;
	private Tuple actionTuple;
	private Field toolField;
	private Field missionField;

	public ActionLogSQLSpacesHandler(String ip, String port, String space) {
		// creating / connecting to a space
		try {
			ts = new TupleSpace(ip, Integer.parseInt(port), space);
		} catch (TupleSpaceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void writeAction(Element action) {
		if (ts != null) {
		//if (false) {
			idField = new Field(action.getAttributeValue("id"));
			timeField = new Field(action.getAttributeValue("time"));
			typeField = new Field(action.getAttributeValue("type"));
			userField = new Field(action.getAttributeValue("user"));
			for (Element elem: ((List<Element>)action.getChild("context").getChildren("property"))) {
				if (elem.getAttributeValue("name").equals("tool")) {
					toolField = new Field(elem.getAttributeValue("value"));
				} else if (elem.getAttributeValue("name").equals("mission")) {
					missionField = new Field(elem.getAttributeValue("value"));
				}
			}
			xmlField = new Field(new XMLOutputter(Format.getPrettyFormat()).outputString(action)+"");
			actionTuple = new Tuple(idField, timeField, typeField, userField, toolField, missionField, xmlField);
			try {
				ts.write(actionTuple);
			} catch (TupleSpaceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void close() {
		try {
			ts.disconnect();
		} catch (TupleSpaceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
