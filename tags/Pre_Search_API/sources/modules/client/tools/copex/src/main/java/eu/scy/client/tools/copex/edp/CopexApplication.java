/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * CopexApplication.java
 *
 * Created on 27 avr. 2009, 15:35:15
 */

package eu.scy.client.tools.copex.edp;

import eu.scy.client.tools.copex.common.LearnerProcedure;
import eu.scy.client.tools.copex.logger.CopexProperty;
import eu.scy.client.tools.copex.utilities.ActionCopex;
import java.awt.BorderLayout;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import javax.swing.UIManager;

/**
 * Copex Application for LabBook
 * @author Marjolaine
 */
public class CopexApplication extends javax.swing.JFrame implements ActionCopex{
    /* edp panel*/
    private CopexPanel copex;

    private static URL copexURL;
    private static String idUser ;
    private static String mission;
    private static String group;
    private static String labDoc;
    private static String labDocName;


    
    /** Creates new form CopexApplication */
    public CopexApplication() {
        initComponents();
        initEdP();
    }

    
    /**
     * initialization of the application
     */
    public void initEdP(){
        long dbKeyMission = -1;
        long dbKeyGroup = -1;
        long dbKeyLabDoc = -1;
        try{
            dbKeyMission = Long.valueOf(mission);
            dbKeyGroup = Long.parseLong(group);
            dbKeyLabDoc = Long.parseLong(labDoc);
        }catch(Throwable t){
            // System.out.println(t);
            this.stop();
        }
        // i18n
        Locale locale = Locale.getDefault();
        //locale = new Locale("en", "GB");
        locale = new Locale("fr", "FR");
        copex = new CopexPanel(this,locale, copexURL, idUser, dbKeyMission, dbKeyGroup, dbKeyLabDoc, labDocName);
        copex.addActionCopex(this);
        add(copex, BorderLayout.CENTER);
        setSize(CopexPanel.PANEL_WIDTH, CopexPanel.PANEL_HEIGHT);
        setIconImage(copex.getIconDialog());
    }

    public void loadEdP(){
        copex.loadData();
        copex.setQuestionDialog();
        setSize(695,495);
        //repaint();
    }
    public void stop() {
        copex.stop();
        this.dispose();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("COPEX - Editeur de protocoles");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        stop();
    }//GEN-LAST:event_formWindowClosing

    /**
    * @param args the command line arguments
    */
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
        if(args == null || args.length  < 6){
            try {
                copexURL = new URL("http://localhost/LabBook/pages/");
                idUser = "2";
                mission= "10003";
                group = "1";
                labDoc = "10031";
                labDocName = "Le thé à la française";
//                copexURL = new URL("http://labbook.imag.fr/pages/");
//                idUser = "1";
//                mission= "9";
//                group = "1";
//                labDoc = "515";
//                labDocName = "Le thé à la française";
            } catch (MalformedURLException ex) {

            }
        }else{
            // gets the parameters of the application
            try{
                // System.out.println("copex url : "+args[0]);
                copexURL = new URL(args[0]);
                // System.out.println("copexURL : "+copexURL.getHost());
            }catch(MalformedURLException e){
                // System.out.println("Copex Application : "+e);
                return;
            }
            idUser = args[1];
            mission= args[2];
            group = args[3];
            labDoc = args[4];
            labDocName = args[5];
        }
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                CopexApplication copex = new CopexApplication();
                copex.setVisible(true);
                copex.loadEdP();
            }
        });
        
    }

    @Override
    public void loadHelpProc(LearnerProcedure helpProc) {
        
    }

    @Override
    public void logAction(String type, List<CopexProperty> attribute) {
        copex.logActionInDB(type, attribute);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

}