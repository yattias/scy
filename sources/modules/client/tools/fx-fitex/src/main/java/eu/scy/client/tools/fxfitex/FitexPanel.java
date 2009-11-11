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
import eu.scy.communications.datasync.event.IDataSyncListener;

/**
 *
 * @author Marjolaine
 */
public class FitexPanel extends JPanel implements IDataSyncListener{
//public class FitexPanel extends JPanel{
    /* data process visualization tool */
    private DataProcessToolPanel dataProcessPanel;
    
    /* Constructor data ToolImpl panel - blank */
    public FitexPanel() {
        super();
        this.setLayout(new BorderLayout());
        initDataProcessTool();
        load();
    }

    

    private void initDataProcessTool(){
        dataProcessPanel = new DataProcessToolPanel(true);
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

    
}
