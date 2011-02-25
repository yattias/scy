/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.fxresultbinder.registration;

import eu.scy.actionlogging.DevNullActionLogger;
import eu.scy.actionlogging.SystemOutActionLogger;
import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.actionlogging.api.IActionLogger;
import eu.scy.actionlogging.Action;
import eu.scy.actionlogging.api.IAction;
import eu.scy.client.desktop.scydesktop.utils.jdom.JDomStringConversion;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.util.logging.Logger;
import org.jdom.Element;
import javax.swing.JPanel;
import eu.scy.client.tools.resultbinder.ResultBinderPanel;
import eu.scy.client.tools.resultbinder.IActionResultBinder;

/**
 *
 * @author Marjolaine
 */
public class ScyResultBinderPanel extends JPanel implements IActionResultBinder{
    private ResultBinderPanel resultBinderPanel;
    private ToolBrokerAPI tbi;
    private String session_name = "n/a";
    private IActionLogger actionLogger;
    private String eloUri = "n/a";
    private static final Logger logger = Logger.getLogger(ScyResultBinderPanel.class.getName());

    private String toolName;


    public ScyResultBinderPanel(String toolName) {
        super();
        this.toolName = toolName;
        this.setLayout(new BorderLayout());
        setPreferredSize(new Dimension(550,350));
        String userName = getUserName();
        this.resultBinderPanel = new ResultBinderPanel(userName);
        resultBinderPanel.addActionResultBinder(this);
        this.add(this.resultBinderPanel, BorderLayout.CENTER);
    }

    private String getUserName(){
        if(tbi == null){
            return "";
        }else{
            return tbi.getLoginUserName();
        }
    }

    public void setEloUri(String eloUri){
        this.eloUri = eloUri;
    }


    public void setTBI(ToolBrokerAPI tbi) {
        this.tbi = tbi;
    }
    
    /* initialization action logger */
    public void initActionLogger(){
        if(tbi != null){
            actionLogger = tbi.getActionLogger();
            //actionLogger = new SystemOutActionLogger();
        }
        else{
            actionLogger = new DevNullActionLogger();
        }
    }

    /* load ELO into result binder */
    public void loadELO(String xmlContent){
        this.resultBinderPanel.loadELO(new JDomStringConversion().stringToXml(xmlContent));
    }

    public void newElo(){
        this.resultBinderPanel.newElo();
    }


    public Element getResultCard(){
        return this.resultBinderPanel.getResultCard();
    }


    /* returns the interface panel for the thumbnail */
    public Container getInterfacePanel(){
        return resultBinderPanel.getInterfacePanel();
    }

    public Dimension getRealSize(){
        return resultBinderPanel.getInterfacePanel().getSize();
    }

    @Override
    public void logAction(String type, String attributeKey, String attributeValue) {
        // action
        IAction action = new Action();
        action.setType(type);
        if(tbi != null){
            action.setUser(tbi.getLoginUserName());
            action.addContext(ContextConstants.tool, this.toolName);
            // generic way now
            //action.addContext(ContextConstants.mission, tbi.getMissionSpecificationURI().toString());
            action.addContext(ContextConstants.session, session_name);
            action.addContext(ContextConstants.eloURI, eloUri);
            action.addAttribute(attributeKey, attributeValue);
        }
        // log action
        if(actionLogger != null)
            actionLogger.log(action);
    }

}
