/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * CopexApplication.java
 *
 * Created on 27 avr. 2009, 15:35:15
 */

package eu.scy.tools.copex.edp;

import java.awt.BorderLayout;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author Marjolaine
 */
public class CopexApplication extends javax.swing.JFrame {
    /* edp panel*/
    private EdPPanel edP;

    private static URL copexURL;
    private static String idUser ;
    private static String mission;
    private static String mo;
    private static String userName;
    private static String firstName;

    
    /** Creates new form CopexApplication */
    public CopexApplication() {
        initComponents();
        initEdP();
    }

    
    /**
     * initialisation de l'applet
     */
    public void initEdP(){
        long dbKeyMission = -1;
        int mode = -1;
        try{
            dbKeyMission = Long.valueOf(mission);
            mode = Integer.valueOf(mo);
        }catch(Throwable t){
            System.out.println(t);
            this.stop();
        }
        edP = new EdPPanel(copexURL, idUser, dbKeyMission, mode, userName, firstName);
        add(edP, BorderLayout.CENTER);
        setSize(EdPPanel.PANEL_WIDTH, EdPPanel.PANEL_HEIGHT);
        
    }

    public void loadEdP(){
        edP.loadData();
        setSize(695,495);
        //repaint();
    }
    public void stop() {
        edP.stop();
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
        if(args == null || args.length  < 5){
//            try {
//                copexURL = new URL("http://copex.imag.fr/espaces/");
//                idUser = "1";
//                mission= "3";
//                mo = "0";
//                userName = "";
//                firstName = "";
//            } catch (MalformedURLException ex) {
//
//            }
        }else{
            // recuperation des parametres de l'application :
            try{
                System.out.println("copex url : "+args[0]);
                copexURL = new URL(args[0]);
                System.out.println("copexURL : "+copexURL.getHost());
            }catch(MalformedURLException e){
                System.out.println("Copex Application : "+e);
                return;
            }
            idUser = args[1];
            mission= args[2];
            mo = args[3];
            userName = args[4];
            firstName = "";
            if(args.length > 5)
                firstName = args[5];
        }
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new CopexApplication().setVisible(true);
//            }
//        });
        CopexApplication copex = new CopexApplication();
        copex.setVisible(true);
        copex.loadEdP();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

}
