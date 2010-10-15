package eu.scy.scymapper.impl.logging;

import eu.scy.actionlogging.Action;
import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.actionlogging.api.IAction;
import eu.scy.actionlogging.api.IActionLogger;
import eu.scy.client.common.datasync.ISyncSession;
import eu.scy.scymapper.api.diagram.model.*;
import org.apache.log4j.Logger;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

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
    private IDiagramModel diagram;
    private ISyncSession session;
    private String username;
    private String mission = "mission1";
    private String toolname = "scymapper";
    private String eloURI;
    private final static Logger syslog = Logger.getLogger(ConceptMapActionLogger.class);

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
        IAction a = createSCYMapperAction("request_concept_help");
        log(a);        
    }

    public void logRequestRelationHelp() {
        IAction a = createSCYMapperAction("request_relation_help");
        log(a);    
    }

    /**
     * Logs when the user change the name of a node
     *
     * @param node The node model that was changed
     */
    public void logNodeLabelChanged(INodeModel node) {
        IAction a = createSCYMapperAction("node_renamed");
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
        IAction a = createSCYMapperAction("link_renamed");
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
        IAction a = createSCYMapperAction("node_added");
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
        IAction a = createSCYMapperAction("link_added");
        a.addAttribute("id", link.getId());
        a.addAttribute("name", link.getLabel());
        a.addAttribute("from_node", link.getFromNode().getId());
        a.addAttribute("to_node", link.getToNode().getId());

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
        IAction a = createSCYMapperAction("link_removed");
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
        IAction a = createSCYMapperAction("node_removed");
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

}
