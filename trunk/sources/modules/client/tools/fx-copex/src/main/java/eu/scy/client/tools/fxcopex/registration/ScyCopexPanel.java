/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.fxcopex.registration;

import eu.scy.actionlogging.DevNullActionLogger;
import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.actionlogging.api.IActionLogger;
import eu.scy.actionlogging.Action;
import eu.scy.actionlogging.api.IAction;
import eu.scy.toolbroker.ToolBrokerImpl;
import eu.scy.client.desktop.scydesktop.utils.jdom.JDomStringConversion;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import eu.scy.tools.copex.common.LearnerProcedure;
import eu.scy.tools.copex.edp.CopexPanel;
import eu.scy.tools.copex.logger.CopexLog;
import eu.scy.tools.copex.logger.CopexProperty;
import eu.scy.tools.copex.utilities.ActionCopex;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Iterator;
import java.util.List;
import org.jdom.Element;
import javax.swing.JPanel;

/**
 *
 * @author Marjolaine
 */
public class ScyCopexPanel extends JPanel implements ActionCopex{
    private CopexPanel copex;
    private ToolBrokerAPI tbi;
    // how can i get userName & password? + mission name
    private String username = "merkel";
    private String password = "merkel";
    private String mission_name = "mission 1";
    private IActionLogger actionLogger;

    public ScyCopexPanel() {
        super();
        this.setLayout(new BorderLayout());
        initTBI();
        initActionLogger();
        copex = new CopexPanel(true);
        setPreferredSize(new Dimension(550,350));
        copex.addActionCopex(this);
        this.add(this.copex, BorderLayout.CENTER);
        copex.loadData();
        copex.setQuestionDialog();
    }

    /* tbi initialization*/
    private void initTBI(){
       //tbi=  new ToolBrokerImpl(username, password);
    }
    /* initialization action logger */
    private void initActionLogger(){
        if(tbi != null)
            actionLogger = tbi.getActionLogger();
        else
            actionLogger = new DevNullActionLogger();
    }

    /* load ELO into copex */
    public void loadELO(String xmlContent){
        this.copex.loadELO(new JDomStringConversion().stringToXml(xmlContent));
    }

    public void newElo(){
        this.copex.newELO();
    }

    public void controlQuestion(){
        this.copex.setQuestionDialog();
    }

    public Element getExperimentalProcedure(){
        return this.copex.getXProc();
    }

    @Override
    public void loadHelpProc(LearnerProcedure helpProc) {
        
    }

    @Override
    public void logAction(String type, List<CopexProperty> attribute) {
        // action
        IAction action = new Action();
        action.setUser(username);
        action.setType(type);
		action.addContext(ContextConstants.tool, CopexLog.toolName);
		action.addContext(ContextConstants.mission, mission_name);
        for(Iterator<CopexProperty> p = attribute.iterator();p.hasNext();){
            CopexProperty property = p.next();
            if(property.getSubElement() == null) {
                action.addAttribute(property.getName(), property.getValue());
            } else {
                action.addAttribute(property.getName(), property.getValue());
                action.addAttribute(property.getName()+"_sub", property.getSubElement().getValue());
            }
        }
        // log action
        if(actionLogger != null)
            actionLogger.log(action);
    }
}
