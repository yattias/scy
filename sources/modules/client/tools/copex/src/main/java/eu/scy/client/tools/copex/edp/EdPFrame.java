/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * EdPFrame.java
 *
 * Created on 6 avr. 2009, 15:52:59
 */

package eu.scy.client.tools.copex.edp;

import eu.scy.client.tools.copex.common.LearnerProcedure;
import eu.scy.client.tools.copex.logger.CopexProperty;
import eu.scy.client.tools.copex.utilities.ActionCopex;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;
import javax.swing.UIManager;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Locale;
import javax.swing.JOptionPane;

/**
 * test frame edp
 * @author Marjolaine
 */
public class EdPFrame extends javax.swing.JFrame implements ActionCopex, WindowListener{
    private CopexPanel copex;

    /** Creates new form EdPFrame */
    public EdPFrame() {
        super();
        initComponents();
        setMinimumSize(new Dimension(550,350));
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(this);
        initEdP();
    }

    public void load(){
        copex.loadData();
        setSize(550,350);
        copex.setQuestionDialog();
    }
    
    private void initEdP(){
        // i18n
        Locale locale = Locale.getDefault();
        //locale = new Locale("en");
        locale = new Locale("fr");
        //locale = new Locale("de");
        Locale.setDefault(locale);
        copex = new CopexPanel(this,false, locale);
        copex.addActionCopex(this);
        add(copex, BorderLayout.CENTER);
        setSize(CopexPanel.PANEL_WIDTH, CopexPanel.PANEL_HEIGHT);
        setIconImage(copex.getIconDialog());
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
        setTitle("SCYExperimentalDesign COPEX");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        // Initialisation du look and feel
//        try{
//            String myLookAndFeel=UIManager.getSystemLookAndFeelClassName();
//            UIManager.setLookAndFeel(myLookAndFeel);
//        }catch(Exception e){
//            // System.out.println("ERREUR dans l'initialisation du lookAndFeel : "+e) ;
//            //JOptionPane.showMessageDialog(this , "ERREUR ans l'initialisation du lookAndFeel : "+e, "ERROR",JOptionPane.ERROR_MESSAGE);
//        }
        for (UIManager.LookAndFeelInfo laf :UIManager.getInstalledLookAndFeels() ){
            if ("Nimbus".equals(laf.getName())) {
                try {
                    UIManager.setLookAndFeel(laf.getClassName());

                } catch (Exception e) {
                    // System.out.println("ERREUR dans l'initialisation du lookAndFeel : "+e) ;
                }
            }
        }
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                EdPFrame edPFrame =new EdPFrame();
                edPFrame.setVisible(true);
                edPFrame.load();
            }
        });
        
    }

    @Override
    public void loadHelpProc(LearnerProcedure helpProc) {
        
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        int ok = JOptionPane.showConfirmDialog(this, copex.getBundleString("MESSAGE_COPEX_EXIT"), copex.getBundleString("TITLE_DIALOG_EXIT"),JOptionPane.OK_CANCEL_OPTION );
        if(ok == JOptionPane.OK_OPTION){
            this.dispose();
        }
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }

    @Override
    public void logAction(String type, List<CopexProperty> attribute) {
        // nothing
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

}
