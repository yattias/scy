package eu.scy.client.tools.scydynamics.logging;

import java.awt.Component;
import java.awt.IllegalComponentStateException;

//import colab.all.logging.SQLSpacesLogger;
import colab.um.draw.JdAux;
import colab.um.draw.JdConst;
import colab.um.draw.JdFigure;
import colab.um.draw.JdFlow;
import colab.um.draw.JdFlowCtr;
import colab.um.draw.JdRelation;
import colab.um.draw.JdStock;
import eu.scy.actionlogging.Action;
import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.actionlogging.api.IAction;
import eu.scy.actionlogging.api.IActionLogger;
import java.util.HashMap;
import java.util.List;

public class ModellingLogger implements IModellingLogger {

    private IAction action;
    private IActionLogger logger;
    private String username;
    private String missionRuntimeURI = "mission1";
    private String toolname = "scy-dynamics";
    private String session = "n/a";
    private String eloURI = "n/a";
    public static final String AUXILIARY_ADDED = "auxiliary_added";
    public static final String CONSTANT_ADDED = "constant_added";
    public static final String STOCK_ADDED = "stock_added";
    public static final String RELATION_ADDED = "relation_added";
    public static final String FLOW_ADDED = "flow_added";
    public static final String DATASET_ADDED = "dataset_added";
    public static final String AUXILIARY_DELETED = "auxiliary_deleted";
    public static final String CONSTANT_DELETED = "constant_deleted";
    public static final String STOCK_DELETED = "stock_deleted";
    public static final String RELATION_DELETED = "relation_deleted";
    public static final String FLOW_DELETED = "flow_deleted";
    public static final String DATASET_DELETED = "dataset_deleted";
    public static final String ELEMENT_RENAMED = "element_renamed";
    public static final String SPECIFICATION_CHANGED = "specification_changed";
    public static final String WINDOW_ACTIVATED = "window_activated";
    public static final String MODEL_RAN = "model_ran";
    public static final String GRAPH_VIEWED = "graph_viewed";
    public static final String TABLE_VIEWED = "table_viewed";

    public ModellingLogger(IActionLogger logger, String username, String missionRuntimeURI) {
        this.logger = logger;
        this.username = username;
        this.missionRuntimeURI = missionRuntimeURI;
    }

    public void setEloUri(String newUri) {
        this.eloURI = newUri;
    }

    public void close() {
    	//System.out.println("ModellingLogger.close logger: "+logger);
        //if (logger instanceof SQLSpacesLogger) {
        //	((SQLSpacesLogger)logger).close();
        //}
    }

    public IAction createBasicAction(String type) {
        IAction newAction = new Action();
        newAction.setType(type);
        newAction.setUser(username);
        newAction.addContext(ContextConstants.tool, toolname);
        newAction.addContext(ContextConstants.eloURI, eloURI);
        newAction.addContext(ContextConstants.mission, missionRuntimeURI);
        newAction.addContext(ContextConstants.session, "n/a");
        return newAction;
    }

    /* (non-Javadoc)
     * @see eu.scy.client.tools.scydynamics.logging.IModellingLogger#logAddAction(colab.um.draw.JdFigure, java.lang.String)
     */
    @Override
    public void logAddAction(JdFigure object, String modelString) {
        switch (object.getType()) {
            case JdFigure.AUX:
                action = createBasicAction(AUXILIARY_ADDED);
                action.addAttribute("id", object.getID());
                action.addAttribute("name", ((JdAux) object).getLabel());
                break;
            case JdFigure.CONSTANT:
                action = createBasicAction(CONSTANT_ADDED);
                action.addAttribute("id", object.getID());
                action.addAttribute("name", ((JdConst) object).getLabel());
                break;
            case JdFigure.STOCK:
                action = createBasicAction(STOCK_ADDED);
                action.addAttribute("id", object.getID());
                action.addAttribute("name", ((JdStock) object).getLabel());
                break;
            case JdFigure.RELATION:
                action = createBasicAction(RELATION_ADDED);
                action.addAttribute("id", object.getID());
                action.addAttribute("name", ((JdRelation) object).getLabel());
                action.addAttribute("from", ((JdRelation) object).getFigure1().getID());
                // if the "to" object of a relation is the center of a flow,
                // list the target of the parent (flow) here
                if (((JdRelation) object).getFigure2().getType() == JdFigure.FLOWCTR) {
                    action.addAttribute("to", ((JdFlowCtr) ((JdRelation) object).getFigure2()).getParent().getID());
                } else {
                    action.addAttribute("to", ((JdRelation) object).getFigure2().getID());
                }
                break;
            case JdFigure.FLOW:
                action = createBasicAction(FLOW_ADDED);
                action.addAttribute("id", object.getID());
                action.addAttribute("name", ((JdFlow) object).getLabel());
                // the ends of a flow may be null
                if (((JdFlow) object).getFigure1() != null) {
                    action.addAttribute("from", ((JdFlow) object).getFigure1().getID());
                }
                if (((JdFlow) object).getFigure2() != null) {
                    action.addAttribute("to", ((JdFlow) object).getFigure2().getID());
                }
                break;
            case JdFigure.DATASET:
                action = createBasicAction(DATASET_ADDED);
                action.addAttribute("id", object.getID());
        }
        if (action != null) {
            action.addAttribute("model", modelString);
            logger.log(action);
        }
    }

    /* (non-Javadoc)
     * @see eu.scy.client.tools.scydynamics.logging.IModellingLogger#logDeleteAction(colab.um.draw.JdFigure, java.lang.String)
     */
    public void logDeleteAction(JdFigure object, String modelString) {
        switch (object.getType()) {
            case JdFigure.AUX:
                action = createBasicAction(AUXILIARY_DELETED);
                action.addAttribute("id", object.getID());
                break;
            case JdFigure.CONSTANT:
                action = createBasicAction(CONSTANT_DELETED);
                action.addAttribute("id", object.getID());
                break;
            case JdFigure.STOCK:
                action = createBasicAction(STOCK_DELETED);
                action.addAttribute("id", object.getID());
                break;
            case JdFigure.RELATION:
                action = createBasicAction(RELATION_DELETED);
                action.addAttribute("id", object.getID());
                action.addAttribute("from", ((JdRelation) object).getFigure1().getID());

                // if figure2 is null, then a link is deleted that has not really been drawn,
                // so, do nothing then
                if (((JdRelation) object).getFigure2() == null) {
                    return;
                }

                // if the "to" object of a relation is the center of a flow,
                // list the target of the parent (flow) here
                if (((JdRelation) object).getFigure2().getType() == JdFigure.FLOWCTR) {
                    action.addAttribute("to", ((JdFlowCtr) ((JdRelation) object).getFigure2()).getParent().getID());
                } else {
                    action.addAttribute("to", ((JdRelation) object).getFigure2().getID());
                }
                break;
            case JdFigure.FLOW:
                action = createBasicAction(FLOW_DELETED);
                action.addAttribute("id", object.getID());
                // the ends of a flow may be null
                if (((JdFlow) object).getFigure1() != null) {
                    action.addAttribute("from", ((JdFlow) object).getFigure1().getID());
                }
                if (((JdFlow) object).getFigure2() != null) {
                    action.addAttribute("to", ((JdFlow) object).getFigure2().getID());
                }
                break;
            case JdFigure.DATASET:
                action = createBasicAction(DATASET_DELETED);
                action.addAttribute("id", object.getID());
        }
        if (action != null) {
            action.addAttribute("model", modelString);
            logger.log(action);
        }
    }

    /* (non-Javadoc)
     * @see eu.scy.client.tools.scydynamics.logging.IModellingLogger#logRenameAction(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void logRenameAction(String id, String oldName, String newName) {
        action = createBasicAction(ELEMENT_RENAMED);
        action.addAttribute("id", id);
        action.addAttribute("old", oldName);
        action.addAttribute("new", newName);
        logger.log(action);
    }

    /* (non-Javadoc)
     * @see eu.scy.client.tools.scydynamics.logging.IModellingLogger#logChangeSpecification(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void logChangeSpecification(String id, String name, String expression, String unit, String modelString) {
        action = createBasicAction(SPECIFICATION_CHANGED);
        action.addAttribute("id", id);
        action.addAttribute("name", name);
        action.addAttribute("expression", expression);
        action.addAttribute("unit", unit);
        action.addAttribute("model", modelString);
        logger.log(action);
    }

    /* (non-Javadoc)
     * @see eu.scy.client.tools.scydynamics.logging.IModellingLogger#logSimpleAction(java.lang.String)
     */
    @Override
    public void logSimpleAction(String type) {
        logger.log(createBasicAction(type));
    }

    /* (non-Javadoc)
     * @see eu.scy.client.tools.scydynamics.logging.IModellingLogger#logSimpleAction(java.lang.String)
     */
    @Override
    public void logSimpleAction(String type, String modelString) {
        action = createBasicAction(type);
        action.addAttribute("model", modelString);
        logger.log(action);
    }

    /* (non-Javadoc)
     * @see eu.scy.client.tools.scydynamics.logging.IModellingLogger#logInspectVariablesAction(java.lang.String, java.lang.String)
     */
    @Override
    public void logInspectVariablesAction(String type, String variablesIDs) {
        action = createBasicAction(type);
        action.addAttribute("selected_variables", variablesIDs);
        logger.log(action);
    }

    /* (non-Javadoc)
     * @see eu.scy.client.tools.scydynamics.logging.IModellingLogger#logLoadAction(java.lang.String)
     */
//	public void logLoadAction(String modelString) {
//		action = createBasicAction("load_model");
//		action.addAttribute("model", modelString);
//		logger.log(action);
//	}

    /* (non-Javadoc)
     * @see eu.scy.client.tools.scydynamics.logging.IModellingLogger#logActivateWindow(java.lang.String, java.lang.String, java.awt.Component)
     */
    @Override
    public void logActivateWindow(String window, String id, Component comp) {
        action = createBasicAction(WINDOW_ACTIVATED);
        action.addAttribute("window", window);
        if (id != null) {
            action.addAttribute("id", id);
        }
        try {
            action.addAttribute("x", comp.getLocationOnScreen().x + "");
            action.addAttribute("y", comp.getLocationOnScreen().y + "");
            action.addAttribute("w", comp.getWidth() + "");
            action.addAttribute("h", comp.getHeight() + "");
        } catch (IllegalComponentStateException ex) {
            action.addAttribute("x", "0");
            action.addAttribute("y", "0");
            action.addAttribute("w", "0");
            action.addAttribute("h", "0");
        }
        logger.log(action);
    }

    @Override
    public void logModelRan(String modelString, String injectedVariables) {
        action = createBasicAction(MODEL_RAN);
        action.addAttribute("model", modelString);
        action.addAttribute("injected_values", injectedVariables);
        logger.log(action);
    }

}