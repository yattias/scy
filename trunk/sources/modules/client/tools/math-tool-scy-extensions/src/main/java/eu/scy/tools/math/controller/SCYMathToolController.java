package eu.scy.tools.math.controller;

import java.util.logging.Logger;

import eu.scy.actionlogging.Action;
import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.actionlogging.api.IAction;
import eu.scy.actionlogging.api.IActionLogger;
import org.apache.commons.lang.StringUtils;

import eu.scy.tools.math.ui.UIUtils;
import eu.scy.tools.math.ui.panels.ShapeCanvas;

public class SCYMathToolController extends MathToolController {

	protected static Logger log = Logger.getLogger("MathToolController.class"); //$NON-NLS-1$

	
		protected static final String MATH_TOOL_END = "</MathTool>";
		protected static final String MATH_TOOL_START = "<MathTool>";

        protected final static String OPEN = "open file";
        protected final static String SAVE = "save file";

        protected IActionLogger actionLogger;


        protected String username;
        protected String toolname;
        protected String missionname;
        protected String sessionname;
        protected String eloUri;
       
	public SCYMathToolController() {
		super();
	}

        public void setEloUri(String eloUri) {
            this.eloUri = eloUri;
        }

        public void setActionLogger(IActionLogger actionLogger, String username, String toolname,
                                    String missionname, String sessionname) {
            this.actionLogger = actionLogger;
            this.username = username;
            this.toolname = toolname;
            this.sessionname = sessionname;
            this.missionname = missionname;
        }


        public IAction createSaveAction() {
            IAction action = new Action();
            action.setUser(username);
            action.setType("elo_saved");
            action.addContext(ContextConstants.tool, toolname + " math tool saves the day");
            action.addContext(ContextConstants.mission, missionname);
            action.addContext(ContextConstants.session, sessionname);
            if (eloUri != null) {
                action.addContext(ContextConstants.eloURI, eloUri);
            }
            return action;
        }

	public String save() {
		String xml = this.writeXML();
		
		xml = MATH_TOOL_START + xml + MATH_TOOL_END;

//		log.severe("xml: " + xml);
		return xml;
	}
	
//	@Override
	public void open(String xml, boolean showNag) {
		String removeStart = StringUtils.remove(xml, MATH_TOOL_START);
		String removeEnd = StringUtils.remove(removeStart, MATH_TOOL_END);
		super.open(removeEnd, showNag);
	}
	public void logAction(String actionLog) { 
		
	}
}
