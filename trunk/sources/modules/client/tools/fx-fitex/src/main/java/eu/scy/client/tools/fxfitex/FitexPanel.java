/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.fxfitex;

import eu.scy.communications.datasync.event.IDataSyncEvent;
import eu.scy.communications.message.ISyncMessage;
import javax.swing.JPanel;
import eu.scy.tools.dataProcessTool.dataTool.DataProcessToolPanel ;
import java.awt.BorderLayout;
import org.jdom.Element;
import eu.scy.elo.contenttype.dataset.DataSet;
import roolo.elo.JDomStringConversion;
import java.io.File;
import roolo.elo.api.IMetadataKey;
//import eu.scy.toolbroker.ToolBrokerImpl;
import eu.scy.datasync.client.IDataSyncService;
import eu.scy.communications.datasync.event.IDataSyncListener;
import java.util.Date;

/**
 *
 * @author Marjolaine
 */
public class FitexPanel extends JPanel implements IDataSyncListener {
//public class FitexPanel extends JPanel{
    /* data process visualization tool */
    private DataProcessToolPanel dataProcessPanel;
    private IDataSyncService dataSyncService;

    private static final String HARD_CODED_TOOL_NAME = "eu.scy.client.tools.fitex";
    private static final String HARD_CODED_USER_NAME = "obama";
    private static final String HARD_CODED_PASSWORD = "obama";

    /* Constructor data Tool panel - blank */
    public FitexPanel() {
        super();
        this.setLayout(new BorderLayout());
        initDataProcessTool();
        load();
        initializeSynchService();
    }

    /* initialize synch service - collaboration service */
    private void initializeSynchService(){
//        ToolBrokerImpl<IMetadataKey> tbi = new ToolBrokerImpl<IMetadataKey>();
//        dataSyncService = tbi.getDataSyncService();
//        dataSyncService.init(tbi.getConnection(HARD_CODED_USER_NAME, HARD_CODED_PASSWORD));
    }




    private void initDataProcessTool(){
        dataProcessPanel = new DataProcessToolPanel();
        add(dataProcessPanel, BorderLayout.CENTER);
        setSize(DataProcessToolPanel.PANEL_WIDTH, DataProcessToolPanel.PANEL_HEIGHT);
        setPreferredSize(getSize());
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
        Date date = new java.util.Date(System.currentTimeMillis());
        java.sql.Timestamp ts = new java.sql.Timestamp(date.getTime());
    }


}
