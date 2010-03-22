/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.fxcopex.registration;

import eu.scy.actionlogging.DevNullActionLogger;
import eu.scy.actionlogging.SystemOutActionLogger;
import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.actionlogging.api.IActionLogger;
import eu.scy.actionlogging.Action;
import eu.scy.actionlogging.api.IAction;
import eu.scy.client.desktop.scydesktop.utils.jdom.JDomStringConversion;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import eu.scy.client.tools.copex.common.LearnerProcedure;
import eu.scy.client.tools.copex.edp.CopexPanel;
import eu.scy.client.tools.copex.logger.CopexProperty;
import eu.scy.client.tools.copex.utilities.ActionCopex;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.jdom.Element;
import javax.swing.JPanel;

/**
 *
 * @author Marjolaine
 */
public class ScyCopexPanel extends JPanel implements ActionCopex{
    private CopexPanel copex;
    private ToolBrokerAPI tbi;
    private String session_name = "sessionName";
    private IActionLogger actionLogger;

    private String toolName;

    public ScyCopexPanel(String toolName) {
        super();
        this.toolName = toolName;
        this.setLayout(new BorderLayout());
        
    }

    public void initCopex(){
        // i18n
        Locale locale = Locale.getDefault();
        //locale = new Locale("en", "GB");
        //locale = new Locale("fr", "FR");
        copex = new CopexPanel(true, locale);
        setPreferredSize(new Dimension(550,350));
        copex.addActionCopex(this);
        this.add(this.copex, BorderLayout.CENTER);
        copex.loadData();
        copex.setQuestionDialog();
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
        action.setType(type);
        if(tbi != null){
            action.setUser(tbi.getLoginUserName());
            action.addContext(ContextConstants.tool, this.toolName);
            action.addContext(ContextConstants.mission, tbi.getMission());
            action.addContext(ContextConstants.session, session_name);
        }
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
