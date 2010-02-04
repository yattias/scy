package eu.scy.scymapper.impl.logging;

import eu.scy.actionlogging.Action;
import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.actionlogging.api.IAction;
import eu.scy.actionlogging.api.IActionLogger;
import eu.scy.scymapper.api.diagram.model.ILinkModel;
import eu.scy.scymapper.api.diagram.model.INodeLinkModel;
import eu.scy.scymapper.api.diagram.model.INodeModel;

/**
 * @author bjoerge
 * @created 03.feb.2010 13:13:48
 * <p/>
 * <p/>
 * <p/>
 * create_map	mapid
 * delete_map	mapid
 * add_node	mapid, nodeid
 * modify_node	mapid, nodeid, nodeproperties
 * delete_node	mapid, nodeid
 * add_relation	mapid, relationid
 * modify_relation	mapid, relationid, relationproperties
 * delete_relation	mapid, relationid
 */
public class ConceptMapLogger {
	private IActionLogger logger;
	private String username;
	private String mission = "mission1";
	private String toolname = "scymapper";


	public ConceptMapLogger(IActionLogger logger, String username) {
		this.logger = logger;
		this.username = username;
	}

	/**
	 * Logs when the user change the style of a node
	 *
	 * @param node The node model that was changed
	 */
	public void logNodeStyleChanged(INodeModel node) {
		// TODO
	}

	/**
	 * Logs when the user change the name of a node
	 *
	 * @param node The node model that was changed
	 */
	public void logNodeLabelChanged(INodeModel node) {
		IAction a = createBasicAction("node_rename");
		a.addAttribute("id", node.getId());
		a.addAttribute("new", node.getLabel());
		logger.log(a);
	}

	/**
	 * Logs when the user change the name of a node
	 *
	 * @param link The node model that was changed
	 */
	public void logLinkLabelChanged(ILinkModel link) {
		IAction a = createBasicAction("link_rename");
		a.addAttribute("id", link.getId());
		a.addAttribute("new", link.getLabel());
		logger.log(a);
	}

	/**
	 * Logs when the user change the style of a node
	 *
	 * @param node The node model that was changed
	 */
	public void logNodeAdded(INodeModel node) {
		IAction a = createBasicAction("node_added");
		a.addAttribute("id", node.getId());
		a.addAttribute("new", node.getLabel());
		logger.log(a);
	}

	/**
	 * Logs when the user change the style of a link
	 *
	 * @param link The link model that was changed
	 */
	public void logLinkAdded(INodeLinkModel link) {
		IAction a = createBasicAction("link_added");
		a.addAttribute("id", link.getId());
		a.addAttribute("new", link.getLabel());
		a.addAttribute("from_node", link.getFromNode().getId());
		a.addAttribute("to_node", link.getToNode().getId());
		logger.log(a);
	}

	/**
	 * Logs when the user change the style of a node
	 *
	 * @param node The node model that was changed
	 */
	public void logLinkRemoved(INodeModel node) {
		IAction a = createBasicAction("link_removed");
		a.addAttribute("id", node.getId());
		logger.log(a);
	}

	/**
	 * Logs when the user change the style of a node
	 *
	 * @param node The node model that was changed
	 */
	public void logNodeRemoved(INodeModel node) {

	}

	public IAction createBasicAction(String type) {
		IAction action = new Action();
		action.setType(type);
		action.setUser(username);
		action.addContext(ContextConstants.tool, toolname);
		action.addContext(ContextConstants.mission, mission);
		action.addContext(ContextConstants.session, "n/a");
		return action;
	}
}
