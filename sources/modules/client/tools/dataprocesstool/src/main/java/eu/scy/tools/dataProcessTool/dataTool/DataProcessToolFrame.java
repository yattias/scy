/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.dataTool;

import eu.scy.tools.dataProcessTool.utilities.ActionDataProcessTool;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.UIManager;
/**
 *
 * @author Marjolaine
 */
public class DataProcessToolFrame extends JFrame implements ActionDataProcessTool{
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
        try{
            String myLookAndFeel=UIManager.getSystemLookAndFeelClassName();
            UIManager.setLookAndFeel(myLookAndFeel);
        }catch(Exception e){
            System.out.println("ERREUR dans l'initialisation du lookAndFeel : "+e) ;
            //JOptionPane.showMessageDialog(this , "ERREUR ans l'initialisation du lookAndFeel : "+e, "ERROR",JOptionPane.ERROR_MESSAGE);
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
        setSize(500, 335);
    }

    private void initDataProcessTool(){
        dataProcessPanel = new DataProcessToolPanel(false);
        setTitle("FITEX "+dataProcessPanel.getVersion());
        dataProcessPanel.addActionCopexButton(this);
        add(dataProcessPanel, BorderLayout.CENTER);
        setSize(DataProcessToolPanel.PANEL_WIDTH, DataProcessToolPanel.PANEL_HEIGHT);
        setPreferredSize(getSize());
    }

    private void initGUI(){
        setMinimumSize(new Dimension(500, 330));
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("FITEX ");
        setLayout(new BorderLayout());
    }

   

    
    @Override
    public void resizeDataToolPanel(int width, int height) {
        this.setSize(width, height);
    }


}
