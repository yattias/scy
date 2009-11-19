/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.fxcopex;

import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.actionlogging.api.IActionLogger;
import eu.scy.actionlogging.logger.Action;
import eu.scy.toolbroker.ToolBrokerImpl;
import eu.scy.client.desktop.scydesktop.utils.jdom.JDomStringConversion;
import eu.scy.tools.copex.common.LearnerProcedure;
import eu.scy.tools.copex.edp.CopexPanel;
import eu.scy.tools.copex.logger.CopexLog;
import eu.scy.tools.copex.logger.CopexProperty;
import eu.scy.tools.copex.utilities.ActionCopex;
import java.awt.BorderLayout;
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
    private ToolBrokerImpl tbi;
    // how can i get userName & password? + mission name
    private String username = "default_username";
    private String password = "default_password";
    private String mission_name = "mission 1";
    private IActionLogger actionLogger;

    public ScyCopexPanel() {
        super();
        this.setLayout(new BorderLayout());
        //initTBI();
        //initActionLogger();
        copex = new CopexPanel(true);
        copex.addActionCopex(this);
        this.add(this.copex, BorderLayout.CENTER);
        copex.loadData();
    }

    /* tbi initialization*/
    private void initTBI(){
        //tbi=  new ToolBrokerImpl();
    }
    /* initialization action logger */
    private void initActionLogger(){
        //actionLogger = tbi.getActionLogger();
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
        Action action = new Action(type, username);
		action.addContext(ContextConstants.tool, CopexLog.toolName);
		action.addContext(ContextConstants.mission, mission_name);
        for(Iterator<CopexProperty> p = attribute.iterator();p.hasNext();){
            CopexProperty property = p.next();
            if(property.getSubElement() == null)
                action.addAttribute(property.getName(), property.getValue());
            else
                action.addAttribute(property.getName(), property.getValue(), property.getSubElement());
        }
        // log action
//        if(actionLogger != null)
//            actionLogger.log(action);
    }
}
