/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.fxfitex;

import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.actionlogging.api.IActionLogger;
import eu.scy.actionlogging.logger.Action;
import eu.scy.communications.datasync.event.IDataSyncEvent;
import eu.scy.communications.message.ISyncMessage;
import eu.scy.communications.datasync.event.IDataSyncListener;
import eu.scy.toolbroker.ToolBrokerImpl;
import eu.scy.tools.dataProcessTool.dataTool.DataProcessToolPanel ;
import eu.scy.tools.dataProcessTool.logger.FitexProperty;
import eu.scy.tools.dataProcessTool.logger.FitexLog;
import eu.scy.tools.dataProcessTool.utilities.ActionDataProcessTool;
import eu.scy.elo.contenttype.dataset.DataSet;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import roolo.elo.JDomStringConversion;
import java.util.List;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import org.jdom.Element;
import java.io.File;
import java.util.Iterator;

/**
 *
 * @author Marjolaine
 */
public class FitexPanel extends JPanel implements ActionDataProcessTool, IDataSyncListener{
    /* data process visualization tool */
    private DataProcessToolPanel dataProcessPanel;

    private ToolBrokerAPI tbi;
    // how can i get userName & password? + mission name
    private String username = "default_username";
    private String password = "default_password";
    private String mission_name = "mission 1";
    private IActionLogger actionLogger;
    
    /* Constructor data ToolImpl panel - blank */
    public FitexPanel() {
        super();
        this.setLayout(new BorderLayout());
        initTBI();
        initActionLogger();
        initDataProcessTool();
        load();
    }

    /* tbi initialization*/
    private void initTBI(){
        tbi=  new ToolBrokerImpl();
    }
    /* initialization action logger */
    private void initActionLogger(){
        actionLogger = tbi.getActionLogger();
    }

    private void initDataProcessTool(){
        dataProcessPanel = new DataProcessToolPanel(true);
        dataProcessPanel.addActionCopexButton(this);
        add(dataProcessPanel, BorderLayout.CENTER);
//        setSize(DataProcessToolPanel.PANEL_WIDTH, DataProcessToolPanel.PANEL_HEIGHT);
//        setPreferredSize(getSize());
    }

    public void load(){
        dataProcessPanel.loadData();
        setSize(500,300);
    }
    public void newElo(){
        this.dataProcessPanel.newElo();
    }
    /* load ELO into data process tool */
    public void loadELO(String xmlContent){
        this.dataProcessPanel.loadELO(xmlContent);
    }

    /* merge ELO with the current dataset */
    public void mergeELO(String xmlContent){
        this.dataProcessPanel.mergeELO(new JDomStringConversion().stringToXml(xmlContent));
    }

    public Element getPDS(){
        return this.dataProcessPanel.getPDS();
    }
    
    /* import CSV file => EDLO dataset */
    public DataSet importCSVFile(File file){
        if (file  != null)
            return  dataProcessPanel.importCSVFile(file);
        return null;
    }

    @Override
    public void handleDataSyncEvent(IDataSyncEvent e) {
        ISyncMessage syncMessage = e.getSyncMessage();
    }

    @Override
    public void resizeDataToolPanel(int width, int height) {

    }

    @Override
    public void logAction(String type, List<FitexProperty> attribute) {
        // action
        Action action = new Action(type, username);
		action.addContext(ContextConstants.tool, FitexLog.toolName);
		action.addContext(ContextConstants.mission, mission_name);
        for(Iterator<FitexProperty> p = attribute.iterator();p.hasNext();){
            FitexProperty property = p.next();
            if(property.getSubElement() == null)
                action.addAttribute(property.getName(), property.getValue());
            else
                action.addAttribute(property.getName(), property.getValue(), property.getSubElement());
        }
        // log action
//        if(actionLogger != null)
//            actionLogger.log(action);
    }

    public void stopFitex(){
        this.dataProcessPanel.endTool();
    }
    
}
