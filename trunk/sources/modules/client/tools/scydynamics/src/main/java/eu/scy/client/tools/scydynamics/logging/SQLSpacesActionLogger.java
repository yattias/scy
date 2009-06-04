package eu.scy.client.tools.scydynamics.logging;

import java.awt.Component;
import java.awt.IllegalComponentStateException;
import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.TimeZone;
import java.util.UUID;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import colab.um.draw.JdAux;
import colab.um.draw.JdConst;
import colab.um.draw.JdFigure;
import colab.um.draw.JdFlow;
import colab.um.draw.JdFlowCtr;
import colab.um.draw.JdRelation;
import colab.um.draw.JdStock;

public class SQLSpacesActionLogger implements IModellingLogger {

	private ActionLogSQLSpacesHandler logSQLSpacesHandler;
	private Element action;
	private String user;
	private String mission = "mission1";
	private Element attributes;
	private static SimpleDateFormat ISO8601FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ssZ");
	private SAXBuilder saxBuilder = new SAXBuilder();

	public SQLSpacesActionLogger(String user, Properties props) {
		this.user = user;
		logSQLSpacesHandler = new ActionLogSQLSpacesHandler(props.get("sqlspaces.ip").toString(), props.get("sqlspaces.port").toString(), props.get("sqlspaces.space").toString());
		ISO8601FORMAT.setTimeZone(TimeZone.getDefault());
		System.out
				.println("SQLSpacesActionLogger. logging to sqlspaces at "+props.get("sqlspaces.ip"));
	}

	private String getCurrentTimeMillisAsISO8601() {
		String result = ISO8601FORMAT.format(new Date(System
				.currentTimeMillis()));
		// convert YYYYMMDDTHH:mm:ss+HH00 into YYYYMMDDTHH:mm:ss+HH:00
		// - note the added colon for the Timezone
		result = result.substring(0, result.length() - 2) + ":"
				+ result.substring(result.length() - 2);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.scy.client.tools.scydynamics.logging.IModellingLogger#close()
	 */
	public void close() {
		logSQLSpacesHandler.close();
	}

	private void addProperty(Element element, String name, String value) {
		Element prop = new Element("property");
		prop.setAttribute("name", name);
		prop.setAttribute("value", value);
		element.addContent(prop);
	}

	private void addProperty(Element element, String name, String value,
			String subElementString) {
		Element prop = new Element("property");
		prop.setAttribute("name", name);
		prop.setAttribute("value", value);
		try {
			Document doc = saxBuilder.build(new StringReader(subElementString));
			doc.getRootElement();
			prop.addContent(doc.getRootElement().detach());
		} catch (JDOMException e) {
			System.out
					.println("ActionLogger.addProperty(). JDomException caught when adding model.");
			// e.printStackTrace();
		} catch (IOException e) {
			System.out
					.println("ActionLogger.addProperty(). IOException caught when adding model.");
			// e.printStackTrace();
		}

		element.addContent(prop);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.scy.client.tools.scydynamics.logging.IModellingLogger#logAddAction
	 * (colab.um.draw.JdFigure, java.lang.String)
	 */
	public void logAddAction(JdFigure object, String modelString) {
		switch (object.getType()) {
		case JdFigure.AUX:
			action = createBasicAction("add_auxiliary");
			attributes = new Element("attributes");
			addProperty(attributes, "id", object.getID());
			addProperty(attributes, "name", ((JdAux) object).getLabel());
			action.addContent(attributes);
			break;
		case JdFigure.CONSTANT:
			action = createBasicAction("add_constant");
			attributes = new Element("attributes");
			addProperty(attributes, "id", object.getID());
			addProperty(attributes, "name", ((JdConst) object).getLabel());
			action.addContent(attributes);
			break;
		case JdFigure.STOCK:
			action = createBasicAction("add_stock");
			attributes = new Element("attributes");
			addProperty(attributes, "id", object.getID());
			addProperty(attributes, "name", ((JdStock) object).getLabel());
			action.addContent(attributes);
			break;
		case JdFigure.RELATION:
			action = createBasicAction("add_relation");
			attributes = new Element("attributes");
			addProperty(attributes, "id", object.getID());
			addProperty(attributes, "from", ((JdRelation) object).getFigure1()
					.getID());
			// if the "to" object of a relation is the center of a flow,
			// list the parent (flow) here
			if (((JdRelation) object).getFigure2().getType() == JdFigure.FLOWCTR) {
				addProperty(attributes, "to",
						((JdFlowCtr) ((JdRelation) object).getFigure2())
								.getParent().getID());
			} else {
				addProperty(attributes, "to", ((JdRelation) object)
						.getFigure2().getID());
			}
			action.addContent(attributes);
			break;
		case JdFigure.FLOW:
			action = createBasicAction("add_flow");
			attributes = new Element("attributes");
			addProperty(attributes, "id", object.getID());
			// the ends of a flow may be null
			if (((JdFlow) object).getFigure1() != null) {
				addProperty(attributes, "from", ((JdFlow) object).getFigure1()
						.getID());
			}
			if (((JdFlow) object).getFigure2() != null) {
				addProperty(attributes, "to", ((JdFlow) object).getFigure2()
						.getID());
			}
			action.addContent(attributes);
			break;
		}
		addProperty(attributes, "model", "", modelString);
		logSQLSpacesHandler.writeAction(action);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.scy.client.tools.scydynamics.logging.IModellingLogger#logDeleteAction
	 * (colab.um.draw.JdFigure, java.lang.String)
	 */
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
		logSQLSpacesHandler.writeAction(action);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.scy.client.tools.scydynamics.logging.IModellingLogger#logRenameAction
	 * (java.lang.String, java.lang.String, java.lang.String)
	 */
	public void logRenameAction(String id, String oldName, String newName) {
		action = createBasicAction("rename_element");
		attributes = new Element("attributes");
		addProperty(attributes, "id", id);
		addProperty(attributes, "old", oldName);
		addProperty(attributes, "new", newName);
		action.addContent(attributes);
		logSQLSpacesHandler.writeAction(action);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeeu.scy.client.tools.scydynamics.logging.IModellingLogger#
	 * logChangeSpecification(java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	public void logChangeSpecification(String id, String expression, String unit) {
		action = createBasicAction("change_specification");
		attributes = new Element("attributes");
		addProperty(attributes, "id", id);
		addProperty(attributes, "expression", expression);
		addProperty(attributes, "unit", unit);
		action.addContent(attributes);
		logSQLSpacesHandler.writeAction(action);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.scy.client.tools.scydynamics.logging.IModellingLogger#logSimpleAction
	 * (java.lang.String)
	 */
	public void logSimpleAction(String type) {
		action = createBasicAction(type);
		logSQLSpacesHandler.writeAction(action);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeeu.scy.client.tools.scydynamics.logging.IModellingLogger#
	 * logInspectVariablesAction(java.lang.String, java.lang.String)
	 */
	public void logInspectVariablesAction(String type, String selectedVariables) {
		action = createBasicAction(type);
		attributes = new Element("attributes");
		addProperty(attributes, "variables", selectedVariables);
		action.addContent(attributes);
		logSQLSpacesHandler.writeAction(action);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.scy.client.tools.scydynamics.logging.IModellingLogger#logLoadAction
	 * (java.lang.String)
	 */
	public void logLoadAction(String modelString) {
		action = createBasicAction("load_model");
		attributes = new Element("attributes");
		addProperty(attributes, "model", "", modelString);
		action.addContent(attributes);
		logSQLSpacesHandler.writeAction(action);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.scy.client.tools.scydynamics.logging.IModellingLogger#logActivateWindow
	 * (java.lang.String, java.lang.String, java.awt.Component)
	 */
	public void logActivateWindow(String window, String id, Component comp) {
		action = createBasicAction("activate_window");
		attributes = new Element("attributes");
		addProperty(attributes, "window", window);
		if (id != null)
			addProperty(attributes, "id", id);
		try {
			addProperty(attributes, "x", comp.getLocationOnScreen().x + "");
			addProperty(attributes, "y", comp.getLocationOnScreen().y + "");
			addProperty(attributes, "w", comp.getWidth() + "");
			addProperty(attributes, "h", comp.getHeight() + "");
		} catch (IllegalComponentStateException ex) {
			addProperty(attributes, "x", "0");
			addProperty(attributes, "y", "0");
			addProperty(attributes, "w", "0");
			addProperty(attributes, "h", "0");
		}
		action.addContent(attributes);
		logSQLSpacesHandler.writeAction(action);
	}

}