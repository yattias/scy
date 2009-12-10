/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.dataTool;

import eu.scy.tools.dataProcessTool.utilities.MyUtilities;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.WindowConstants;

/**
 * short help manual for students
 * @author Marjolaine
 */
public class HelpDialog extends JDialog{
    private FitexToolPanel owner;

    private JButton closeButton;

    public HelpDialog(FitexToolPanel owner) {
        super();
        this.owner = owner;
        this.setModal(true);
        this.setResizable(false);
        setLocation(owner.getLocationDialog());
        initGUI();
    }

    private void initGUI(){
        setTitle(owner.getBundleString("TITLE_DIALOG_HELP"));
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLayout(null);
        setSize(200,100);
        getContentPane().add(getButtonClose());
    }


    private void helpClose(){
        this.dispose();
    }

    private JButton getButtonClose(){
        if(closeButton == null){
            closeButton = new JButton();
            closeButton.setName("closeButton");
            closeButton.setText(owner.getBundleString("BUTTON_CLOSE"));
            closeButton.setSize(60+MyUtilities.lenghtOfString(closeButton.getText(), closeButton.getFontMetrics(closeButton.getFont())), 23);
            closeButton.setBounds((this.getWidth()-closeButton.getWidth()) /2, 20, closeButton.getWidth(), closeButton.getHeight());
            closeButton.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    helpClose();
                }
            });
        }
        return closeButton;
    }
    
}