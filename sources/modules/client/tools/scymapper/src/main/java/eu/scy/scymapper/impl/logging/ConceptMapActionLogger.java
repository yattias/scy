package eu.scy.scymapper.impl.logging;

import org.apache.log4j.Logger;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import eu.scy.actionlogging.Action;
import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.actionlogging.api.IAction;
import eu.scy.actionlogging.api.IActionLogger;
import eu.scy.client.common.datasync.ISyncSession;
import eu.scy.scymapper.api.diagram.model.DiagramElementAdapter;
import eu.scy.scymapper.api.diagram.model.IDiagramModel;
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
public class ConceptMapActionLogger extends DiagramElementAdapter {

    private IActionLogger logger;
    protected IDiagramModel diagram;
    private ISyncSession session;
    protected String username;
    private String mission = "mission1";
    private String toolname = "conceptmap";
    protected String eloURI = "n/a";
    private final static Logger syslog = Logger.getLogger(ConceptMapActionLogger.class);
    public static final String REQUEST_CONCEPT = "concept_help_requested";
    public static final String REQUEST_RELATION = "relation_help_requested";
    public static final String NODE_ADDED = "node_added";
    public static final String LINK_ADDED = "link_added";
    public static final String NODE_RENAMED = "node_renamed";
    public static final String LINK_RENAMED = "link_renamed";
    public static final String LINK_REMOVED = "link_removed";
    public static final String NODE_REMOVED = "node_removed";
    public static final String LINK_FLIPPED = "link_flipped";

    public ConceptMapActionLogger(IActionLogger actionLogger, IDiagramModel diagram, String username) {
        this.logger = actionLogger;
        this.username = username;
        this.setDiagram(diagram);
    }

    public void setDiagram(IDiagramModel diagram) {
        //remove listener from old diagram
        if (diagram != null) {
            diagram.removeDiagramListener(this);
        }
        // set the new diagram
        this.diagram = diagram;
        this.diagram.addDiagramListener(this);
    }

    public void logRequestConceptHelp() {
        IAction a = createSCYMapperAction(REQUEST_CONCEPT);
        log(a);
    }

    public void logRequestRelationHelp() {
        IAction a = createSCYMapperAction(REQUEST_RELATION);
        log(a);
    }

    /**
     * Logs when the user change the name of a node
     *
     * @param node The node model that was changed
     */
    public void logNodeLabelChanged(INodeModel node) {
        IAction a = createSCYMapperAction(NODE_RENAMED);
        a.addAttribute("id", node.getId());
        a.addAttribute("new", node.getLabel());
        log(a);
    }

    /**
     * Logs when the user change the name of a node
     *
     * @param link The node model that was changed
     */
    public void logLinkLabelChanged(ILinkModel link) {
        IAction a = createSCYMapperAction(LINK_RENAMED);
        a.addAttribute("id", link.getId());
        a.addAttribute("new", link.getLabel());
        log(a);
    }

    /**
     * Logs when the user change the style of a node
     *
     * @param node The node model that was changed
     */
    public void logNodeAdded(INodeModel node) {
        IAction a = createSCYMapperAction(NODE_ADDED);
        a.addAttribute("id", node.getId());
        a.addAttribute("name", node.getLabel());

        XStream xstream = new XStream(new DomDriver());
        String xml = xstream.toXML(diagram);
        a.addAttribute("model", xml);

        log(a);
    }

    /**
     * Logs when the user change the style of a link
     *
     * @param link The link model that was changed
     */
    public void logLinkAdded(INodeLinkModel link) {
        IAction a = createSCYMapperAction(LINK_ADDED);
        a.addAttribute("id", link.getId());
        a.addAttribute("name", link.getLabel());
        a.addAttribute("from_node", link.getFromNode().getId());
        a.addAttribute("to_node", link.getToNode().getId());

        XStream xstream = new XStream(new DomDriver());
        String xml = xstream.toXML(diagram);
        a.addAttribute("model", xml);

        log(a);
    }
    public void logLinkFlipped(INodeLinkModel simpleLink) {
        IAction a = createSCYMapperAction(LINK_FLIPPED);
        a.addAttribute("id", simpleLink.getId());
        a.addAttribute("name", simpleLink.getLabel());
        a.addAttribute("from_node", simpleLink.getFromNode().getId());
        a.addAttribute("to_node", simpleLink.getToNode().getId());
        XStream xstream = new XStream(new DomDriver());
        String xml = xstream.toXML(diagram);
        a.addAttribute("model", xml);

        log(a);
    }

    /**
     * Logs when the user change the style of a node
     *
     * @param link The node model that was changed
     */
    public void logLinkRemoved(INodeLinkModel link) {
        IAction a = createSCYMapperAction(LINK_REMOVED);
        a.addAttribute("id", link.getId());

        XStream xstream = new XStream(new DomDriver());
        String xml = xstream.toXML(diagram);
        a.addAttribute("model", xml);

        log(a);
    }

    /**
     * Logs when the user change the style of a node
     *
     * @param node The node model that was changed
     */
    public void logNodeRemoved(INodeModel node) {
        IAction a = createSCYMapperAction(NODE_REMOVED);
        a.addAttribute("id", node.getId());

        XStream xstream = new XStream(new DomDriver());
        String xml = xstream.toXML(diagram);
        a.addAttribute("model", xml);

        log(a);
    }

    public IAction createSCYMapperAction(String type) {
        IAction action = new Action();
        action.setType(type);
        action.setUser(username);
        action.addContext(ContextConstants.tool, toolname);
        action.addContext(ContextConstants.mission, mission);
        action.addContext(ContextConstants.session, session == null ? "N/A" : session.getId());
        action.addContext(ContextConstants.eloURI, eloURI == null ? "N/A" : eloURI);
        return action;
    }

    public void log(IAction action) {
        syslog.debug("LOGGING ACTION: " + action);
        logger.log(action);
    }

    @Override
    public void labelChanged(INodeModel node) {
        logNodeLabelChanged(node);
    }

    @Override
    public void labelChanged(ILinkModel link) {
        logLinkLabelChanged(link);
    }

    @Override
    public void nodeAdded(INodeModel n, boolean focused) {
        logNodeAdded(n);
        n.addListener(this);
    }

    @Override
    public void nodeRemoved(INodeModel node) {
        logNodeRemoved(node);
        node.removeListener(this);
    }

    @Override
    public void linkAdded(ILinkModel link, boolean focused) {
        if (link instanceof INodeLinkModel) {
            logLinkAdded((INodeLinkModel) link);
            link.addListener(this);
        }
    }

    @Override
    public void linkRemoved(ILinkModel link) {
        if (link instanceof INodeLinkModel) {
            logLinkRemoved((INodeLinkModel) link);
            link.removeListener(this);
        }
    }

    public void setSession(ISyncSession session) {
        this.session = session;
    }

    public void setEloURI(String eloURI) {
        this.eloURI = eloURI;
    }

    @Override
    public void linkFlipped(ILinkModel linkModel) {
        if (linkModel instanceof INodeLinkModel){
            logLinkFlipped((INodeLinkModel)linkModel);
        }
    }




}