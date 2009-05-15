package nl.utwente.gw.modelling.logging;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.jdom.Content;
import org.jdom.Element;

import colab.um.draw.JdAux;
import colab.um.draw.JdConst;
import colab.um.draw.JdFigure;
import colab.um.draw.JdFlow;
import colab.um.draw.JdFlowCtr;
import colab.um.draw.JdRelation;
import colab.um.draw.JdStock;

public class ActionLogger {

	private ActionLogFileHandler logfilehandler;
	private String filename;
	private Element action;
	private String user;
	private String mission = "mission1";
	private Element attributes;
	private static SimpleDateFormat ISO8601FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

	public ActionLogger(String user) {
		this.user = user;
		logfilehandler = new ActionLogFileHandler(getFileName());
		ISO8601FORMAT.setTimeZone(TimeZone.getDefault());
		System.out.println("ActionLogger. creating logfile at "+getFileName());
	}
	
	private String getFileName() {
		if (this.filename == null) {
			filename = new String(System.getProperties().get("user.home").toString());
			filename = filename.concat(System.getProperties().get("file.separator").toString());
			filename = filename.concat("model-log-"+System.currentTimeMillis()+".xml");
		}
		return filename;
	}
	
	private String getCurrentTimeMillisAsISO8601() {
		String result = ISO8601FORMAT.format(new Date(System.currentTimeMillis()));
		  //convert YYYYMMDDTHH:mm:ss+HH00 into YYYYMMDDTHH:mm:ss+HH:00
		  //- note the added colon for the Timezone
		  result = result.substring(0, result.length()-2)
		    + ":" + result.substring(result.length()-2);
		  return result; 
	}
	
	public void close() {
		logfilehandler.close();
	}
	
	public void addProperty(Element element, String name, String value) {
		Element prop = new Element("property");
		prop.setAttribute("name", name);
		prop.setAttribute("value", value);
		element.addContent(prop);
	}
	
	public void addProperty(Element element, String name, String value, String modelString) {
		Element prop = new Element("property");
		prop.setAttribute("name", name);
		prop.setAttribute("value", value);
		//prop.setText(modelString);
		element.addContent(prop);
	}
	
	public void logAddAction(JdFigure object, String modelString) {
		switch (object.getType()) {
		case JdFigure.AUX:
			action = createBasicAction("add_auxiliary");
			attributes = new Element("attributes");
			addProperty(attributes, "id", object.getID());
			addProperty(attributes, "name", ((JdAux)object).getLabel());
			action.addContent(attributes);
			break;
		case JdFigure.CONSTANT:
			action = createBasicAction("add_constant");
			attributes = new Element("attributes");
			addProperty(attributes, "id", object.getID());
			addProperty(attributes, "name", ((JdConst)object).getLabel());
			action.addContent(attributes);
			break;
		case JdFigure.STOCK:
			action = createBasicAction("add_stock");
			attributes = new Element("attributes");
			addProperty(attributes, "id", object.getID());
			addProperty(attributes, "name", ((JdStock)object).getLabel());
			action.addContent(attributes);
			break;
		case JdFigure.RELATION:
			action = createBasicAction("add_relation");
			attributes = new Element("attributes");
			addProperty(attributes, "id", object.getID());
			addProperty(attributes, "from", ((JdRelation)object).getFigure1().getID());
			// if the "to" object of a relation is the center of a flow,
			// list the parent (flow) here
			if (((JdRelation) object).getFigure2().getType()==JdFigure.FLOWCTR) {
				addProperty(attributes, "to", ((JdFlowCtr)((JdRelation)object).getFigure2()).getParent().getID());
			} else {
				addProperty(attributes, "to", ((JdRelation)object).getFigure2().getID());
			}
			action.addContent(attributes);
			break;
		case JdFigure.FLOW:
			action = createBasicAction("add_flow");
			attributes = new Element("attributes");
			addProperty(attributes, "id", object.getID());
			// the ends of a flow may be null
			if (((JdFlow) object).getFigure1()!=null) {
				addProperty(attributes, "from", ((JdFlow)object).getFigure1().getID());
			}
			if (((JdFlow) object).getFigure2()!=null) {
				addProperty(attributes, "to", ((JdFlow)object).getFigure2().getID());
			}
			action.addContent(attributes);
			break;
		}
		addProperty(attributes, "model", "", modelString);
		logfilehandler.writeAction(action);
		logfilehandler.writeAction(action);
	}
	
	public void logDeleteAction(JdFigure object, String modelString) {
		switch (object.getType()) {
		case JdFigure.AUX:
			action = createBasicAction("delete_auxiliary");
			attributes = new Element("attributes");
			addProperty(attributes, "id", object.getID());
			action.addContent(attributes);
			break;
		case JdFigure.CONSTANT:
			action = createBasicAction("delete_constant");
			attributes = new Element("attributes");
			addProperty(attributes, "id", object.getID());
			action.addContent(attributes);
			break;
		case JdFigure.STOCK:
			action = createBasicAction("delete_stock");
			attributes = new Element("attributes");
			addProperty(attributes, "id", object.getID());
			action.addContent(attributes);
			break;
		case JdFigure.RELATION:
			action = createBasicAction("delete_relation");
			attributes = new Element("attributes");
			addProperty(attributes, "id", object.getID());
			action.addContent(attributes);
			break;
		case JdFigure.FLOW:
			action = createBasicAction("delete_flow");
			attributes = new Element("attributes");
			addProperty(attributes, "id", object.getID());
			action.addContent(attributes);
			break;
		}
		addProperty(attributes, "model", "", modelString);
		logfilehandler.writeAction(action);
	}
	
	public Element createBasicAction(String type) {
		Element basic = new Element("action");
		basic.setAttribute("id", UUID.randomUUID().toString());
		basic.setAttribute("time", getCurrentTimeMillisAsISO8601());
		basic.setAttribute("type", type);
		basic.setAttribute("user", user);
		
		Element context = new Element("context");
		addProperty(context, "tool", "scydynamics");
		addProperty(context, "mission", mission);
		basic.addContent(context);
		
		return basic;
	}

	public void logRenameAction(String id, String oldName, String newName) {
		action = createBasicAction("rename_element");
		attributes = new Element("attributes");
		addProperty(attributes, "id", id);
		addProperty(attributes, "old", oldName);
		addProperty(attributes, "new", newName);
		action.addContent(attributes);
		logfilehandler.writeAction(action);
	}
	
	public void logChangeSpecification(String id, String expression, String unit) {
		action = createBasicAction("change_specification");
		attributes = new Element("attributes");
		addProperty(attributes, "id", id);
		addProperty(attributes, "expression", expression);
		addProperty(attributes, "unit", unit);
		action.addContent(attributes);
		logfilehandler.writeAction(action);
	}

	public void logSimpleAction(String type) {
		action = createBasicAction(type);
		logfilehandler.writeAction(action);
	}

	public void logInspectVariablesAction(String type, String selectedVariables) {
		action = createBasicAction(type);
		attributes = new Element("attributes");
		addProperty(attributes, "variables", selectedVariables);
		action.addContent(attributes);
		logfilehandler.writeAction(action);
	}

	public void logLoadAction(String modelString) {
		action = createBasicAction("load_model");
		attributes = new Element("attributes");
		addProperty(attributes, "model", "", modelString);
		action.addContent(attributes);
		logfilehandler.writeAction(action);	
	}

	public void logActivateWindow(String window) {
		action = createBasicAction("activate_windows");
		attributes = new Element("attributes");
		addProperty(attributes, "window", window);
		action.addContent(attributes);
		logfilehandler.writeAction(action);	
	}

	public void logActivateWindow(String window, String id) {
		action = createBasicAction("activate_windows");
		attributes = new Element("attributes");
		addProperty(attributes, "window", window);
		addProperty(attributes, "id", id);
		action.addContent(attributes);
		logfilehandler.writeAction(action);			
	}
	
}