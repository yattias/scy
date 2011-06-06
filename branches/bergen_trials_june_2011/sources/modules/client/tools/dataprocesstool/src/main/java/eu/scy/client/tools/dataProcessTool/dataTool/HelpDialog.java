/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.dataProcessTool.dataTool;

import eu.scy.client.tools.dataProcessTool.utilities.CopexReturn;
import eu.scy.client.tools.dataProcessTool.utilities.MyUtilities;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

/**
 * short help manual for students
 * @author Marjolaine
 */
public class HelpDialog extends JDialog{
    private FitexToolPanel owner;

    private JPanel panelButton;
    private JButton closeButton;
    private JScrollPane scrollPaneHelp;
    private JEditorPane editorPaneHelp;

    public HelpDialog(FitexToolPanel owner) {
        super();
        this.owner = owner;
        this.setLocationRelativeTo(owner);
        this.setModal(true);
        setIconImage(owner.getIconDialog());
        setLocation(owner.getLocationDialog());
        this.setResizable(true);
        initGUI();
    }

    private void initGUI(){
        setTitle(owner.getBundleString("TITLE_DIALOG_HELP"));
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(500,400));
        setLayout(new BorderLayout());
        getContentPane().add(getScrollPaneHelp(), BorderLayout.CENTER);
        getContentPane().add(getPanelButton(), BorderLayout.SOUTH);
        this.editorPaneHelp.setEditable(false);
        try {
            this.editorPaneHelp.setPage(owner.getHelpManualPage());
        } catch (IOException ex) {
            owner.displayError(new CopexReturn(owner.getBundleString("MSG_ERROR_HELP_MANUAL")+ex, false), owner.getBundleString("TITLE_DIALOG_ERROR"));
        }
    }

    public JScrollPane getScrollPaneHelp() {
        if(scrollPaneHelp == null){
            scrollPaneHelp = new JScrollPane();
            scrollPaneHelp.setName("scrollPaneHelp");
            scrollPaneHelp.setViewportView(getEditorPaneHelp());
        }
        return scrollPaneHelp;
    }

    public JEditorPane getEditorPaneHelp(){
        if(editorPaneHelp == null){
            editorPaneHelp = new JEditorPane();
            editorPaneHelp.setName("editorPaneHelp");
        }
        return editorPaneHelp;
    }
    private JPanel getPanelButton(){
        if(panelButton == null){
            panelButton = new JPanel();
            panelButton.setName("panelButton");
            panelButton.setSize(getWidth(), 80);
            panelButton.setPreferredSize(panelButton.getSize());
            panelButton.setLayout(null);
            panelButton.add(getButtonClose());
        }
        return panelButton;
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