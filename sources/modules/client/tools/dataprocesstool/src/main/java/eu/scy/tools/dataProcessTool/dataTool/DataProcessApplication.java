/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.dataTool;

import eu.scy.tools.dataProcessTool.logger.FitexProperty;
import eu.scy.tools.dataProcessTool.utilities.ActionDataProcessTool;
import java.awt.BorderLayout;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import javax.swing.JFrame;
import javax.swing.UIManager;

/**
 * fitex application labbook
 * @author Marjolaine
 */
public class DataProcessApplication extends JFrame implements  ActionDataProcessTool{
    private DataProcessToolPanel fitexPanel;

    private static URL fitexURL;
    private static String idUser ;
    private static String mission;
    private static String idGroup;
    private static String idLabDoc;
    private static String labDocName;

    

    public DataProcessApplication() {
        super();
        initGUI();
    }

    private void initGUI(){
        this.setLayout(new BorderLayout());
        long dbKeyMission = -1;
        long dbKeyUser = -1;
        long dbKeyGroup = -1;
        long dbKeyLabDoc = -1;
        try{
            dbKeyMission = Long.valueOf(mission);
            dbKeyUser = Long.valueOf(idUser);
            dbKeyGroup = Long.parseLong(idGroup);
            dbKeyLabDoc = Long.parseLong(idLabDoc);
        }catch(Throwable t){
            // System.out.println(t);
            this.stop();
        }
        
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                stop();
            }
        });
        
        fitexPanel = new DataProcessToolPanel(fitexURL, Locale.getDefault(), dbKeyMission,dbKeyUser, dbKeyGroup, dbKeyLabDoc, labDocName);
        fitexPanel.addFitexAction(this);
        add(fitexPanel, BorderLayout.CENTER);
        setSize(DataProcessToolPanel.PANEL_WIDTH, DataProcessToolPanel.PANEL_HEIGHT);
        setTitle(fitexPanel.getBundleString("TITLE_FITEX"));
        setIconImage(fitexPanel.getIconDialog());
    }

    public void load(){
        this.fitexPanel.loadData();
        setTitle(fitexPanel.getBundleString("TITLE_FITEX"));
    }
    public void stop() {
        fitexPanel.endTool();
        this.dispose();
    }

     
    

    @Override
    public void resizeDataToolPanel(int width, int height) {
        this.setSize(width, height);
    }


    public static void main(String args[]) {
        for (UIManager.LookAndFeelInfo laf :UIManager.getInstalledLookAndFeels() ){
            if ("Nimbus".equals(laf.getName())) {
                try {
                    UIManager.setLookAndFeel(laf.getClassName());

                } catch (Exception e) {
                    // System.out.println("ERREUR dans l'initialisation du lookAndFeel : "+e) ;
                }
            }
        }
        if(args == null || args.length != 6){
            try {
                fitexURL = new URL("http://localhost/LabBook/tool_fitex/");
                idUser = "2";
                mission= "1";
                idGroup = "1";
                idLabDoc = "10115";
                labDocName = "my dataset";
//                fitexURL = new URL("http://labbook.imag.fr/pages/");
//                idUser = "3";
//                mission= "5";
//                idGroup = "2";
//                idLabDoc = "628";
//                labDocName = "Modele";
            } catch (MalformedURLException ex) {

            }
        }else{
            // recuperation des parametres de l'application :
            try{
                // System.out.println("fitex url : "+args[0]);
                fitexURL = new URL(args[0]);
                // System.out.println("fitexURL : "+fitexURL.getHost());
            }catch(MalformedURLException e){
                // System.out.println("Fitex Application : "+e);
                return;
            }
            idUser = args[1];
            mission= args[2];
            idGroup = args[3];
            idLabDoc = args[4];
            labDocName = args[5];
        }
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DataProcessApplication fitex = new DataProcessApplication();
                fitex.setVisible(true);
                fitex.load();
            }
        });

    }

    @Override
    public void logAction(String type, List<FitexProperty> attribute) {
        // log in  db
        fitexPanel.logAction(type, attribute);
    }

    
    
}
