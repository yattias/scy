/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.dataTool;

import eu.scy.tools.dataProcessTool.logger.FitexProperty;
import eu.scy.tools.dataProcessTool.utilities.ActionDataProcessTool;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;
import java.util.Locale;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
/**
 *
 * @author Marjolaine
 */
public class DataProcessToolFrame extends JFrame implements ActionDataProcessTool, WindowListener{

    private DataProcessToolPanel dataProcessPanel;
    public DataProcessToolFrame() {
        super();
        initGUI();
        initDataProcessTool();
    }

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
                DataProcessToolFrame dataFrame =new DataProcessToolFrame();
                dataFrame.setVisible(true);
                dataFrame.load();
            }
        });

    }


    public void load(){
        dataProcessPanel.loadData();
        setSize(660, 355);
    }

    private void initDataProcessTool(){
        // i18n
        Locale locale = Locale.getDefault();
        //locale = new Locale("en");
        //locale = new Locale("fr");
        //locale = new Locale("de");
        Locale.setDefault(locale);
        dataProcessPanel = new DataProcessToolPanel(false, locale);
        setTitle("SCYDataViewer Fitex "+dataProcessPanel.getVersion());
        setIconImage(dataProcessPanel.getIconDialog());
        dataProcessPanel.addFitexAction(this);
        add(dataProcessPanel, BorderLayout.CENTER);
        setSize(DataProcessToolPanel.PANEL_WIDTH, DataProcessToolPanel.PANEL_HEIGHT);
        setPreferredSize(getSize());
    }

    private void initGUI(){
        this.addWindowListener(this);
        setMinimumSize(new Dimension(615, 400));
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("SCYDataViewer ");
        setLayout(new BorderLayout());
    }

    
    @Override
    public void resizeDataToolPanel(int width, int height) {
        this.setSize(width, height);
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        int ok = JOptionPane.showConfirmDialog(this, dataProcessPanel.getBundleString("MESSAGE_FITEX_EXIT"), dataProcessPanel.getBundleString("TITLE_DIALOG_EXIT"),JOptionPane.OK_CANCEL_OPTION );
        if(ok == JOptionPane.OK_OPTION){
            dataProcessPanel.endTool();
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
    public void logAction(String type, List<FitexProperty> attribute) {
        
    }


}
