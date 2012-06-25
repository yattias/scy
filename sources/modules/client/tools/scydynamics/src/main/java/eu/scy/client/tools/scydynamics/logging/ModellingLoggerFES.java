package eu.scy.client.tools.scydynamics.logging;

import java.util.logging.Logger;

import colab.um.draw.JdAux;
import colab.um.draw.JdConst;
import colab.um.draw.JdFigure;
import colab.um.draw.JdFlow;
import colab.um.draw.JdFlowCtr;
import colab.um.draw.JdRelation;
import colab.um.draw.JdStock;
import eu.scy.actionlogging.api.IActionLogger;

public class ModellingLoggerFES extends ModellingLogger {

	@SuppressWarnings("unused")
	private final static Logger debugLogger = Logger.getLogger(ModellingLoggerFES.class.getName());
    public static final String MODEL_LOADED = "model_loaded";
    public static final String FEEDBACK_REQUESTED = "feedback_requested";
	public static final String TERM_NOT_RECOGNIZED = "term_not_recognized";
	public static final String TERM_NOT_RECOGNIZED_PROPOSALS = "term_not_recognized__proposals_given";
	
    public ModellingLoggerFES(IActionLogger logger, String username, String missionRuntimeURI) {
        super(logger, username, missionRuntimeURI);
    }

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
//        if (action != null) {
//            action.addAttribute("model", modelString);
//            logger.log(action);
//        }
        logger.log(action);
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
//        if (action != null) {
//            action.addAttribute("model", modelString);
//            logger.log(action);
//        }
        logger.log(action);
    }

	@Override
	public void logLoadAction(String filename, String modelString) {
		action = createBasicAction(MODEL_LOADED);
		action.addAttribute("model", modelString);
		action.addAttribute("filename", filename);
		logger.log(action);
	}

	@Override
	public void logFeedbackRequested(String modelString, String data) {
		action = createBasicAction(FEEDBACK_REQUESTED);
		action.addAttribute("model", modelString);
		action.addAttribute("feedback", data);
		logger.log(action);	
	}
	
	@Override
	public void logTermNotRecognized(String term) {
		action = createBasicAction(TERM_NOT_RECOGNIZED);
		action.addAttribute("term", term);
		logger.log(action);	
	}
	
	@Override
	public void logTermNotRecognizedProposals(String term) {
		action = createBasicAction(TERM_NOT_RECOGNIZED_PROPOSALS);
		action.addAttribute("term", term);
		logger.log(action);	
	}

}