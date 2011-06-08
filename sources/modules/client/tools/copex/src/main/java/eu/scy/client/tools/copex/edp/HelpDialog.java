/*
 * HelpDialog.java
 *
 * Created on 24 octobre 2008, 08:26
 */

package eu.scy.client.tools.copex.edp;

import eu.scy.client.tools.copex.utilities.CopexReturn;
import eu.scy.client.tools.copex.utilities.CopexUtilities;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * Help dialog: there is an explication part, a close button and a button to open the help proc
 * @author  Marjolaine
 */
public class HelpDialog extends javax.swing.JDialog {

    private EdPPanel edP;
    private JScrollPane scrollPaneHelp;
    private JEditorPane editorPaneHelp;
    private JPanel panelButton;
    private JButton buttonClose;
    private JButton buttonOpenProc;
    
    
    /** Creates new form HelpDialog */
    public HelpDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    public HelpDialog(EdPPanel edP) {
        super(edP.getOwnerFrame());
        this.edP = edP;
        initComponents();
        setIconImage(edP.getIconDialog());
        this.setLocationRelativeTo(edP);
        this.setModal(true);
        setLocation(edP.getLocationDialog());
        this.setResizable(true);
        init();
    }

    public HelpDialog(EdPPanel edP, EdPPanel helpPanel){
        super(edP.getOwnerFrame());
        this.edP = edP;
        initComponents();
        setIconImage(edP.getIconDialog());
        this.setLocationRelativeTo(edP);
        this.setModal(true);
        setLocation(edP.getLocationDialog());
        this.setResizable(true);
        initHelpProc(helpPanel);
    }
    
    // initialization of the help text
    private void init(){
        setMinimumSize(new Dimension(500,400));
        setLayout(new BorderLayout());
        getContentPane().add(getScrollPaneHelp(), BorderLayout.CENTER);
        getContentPane().add(getPanelButton(), BorderLayout.SOUTH);
        this.editorPaneHelp.setEditable(false);
        try {
            this.editorPaneHelp.setPage(edP.getHelpManualPage());
        } catch (IOException ex) {
            edP.displayError(new CopexReturn(edP.getBundleString("MSG_ERROR_HELP_MANUAL")+ex, false), edP.getBundleString("TITLE_DIALOG_ERROR"));
        }

        this.panelButton.setSize(getWidth(), 60);
        // button open proc
        this.buttonOpenProc.setSize(60+CopexUtilities.lenghtOfString(this.buttonOpenProc.getText(), getFontMetrics(this.buttonOpenProc.getFont())), this.buttonOpenProc.getHeight());
        this.buttonOpenProc.setBounds(30, 20, this.buttonOpenProc.getWidth(), this.buttonOpenProc.getHeight());
        // button close
        this.buttonClose.setSize(60+CopexUtilities.lenghtOfString(this.buttonClose.getText(), getFontMetrics(this.buttonClose.getFont())), this.buttonClose.getHeight());
        this.buttonClose.setBounds(getWidth()-50-this.buttonClose.getWidth(), buttonOpenProc.getY(), this.buttonClose.getWidth(), this.buttonClose.getHeight());
    }

    private void initHelpProc(EdPPanel helpPanel){
        setLayout(new BorderLayout());
        getContentPane().add(helpPanel, BorderLayout.CENTER);
        getContentPane().add(getPanelButton(), BorderLayout.SOUTH);
        this.panelButton.setSize(getWidth(), 60);
        panelButton.remove(buttonOpenProc);
        buttonOpenProc = null;
        // button close
        this.buttonClose.setSize(60+CopexUtilities.lenghtOfString(this.buttonClose.getText(), getFontMetrics(this.buttonClose.getFont())), this.buttonClose.getHeight());
        this.buttonClose.setBounds((getWidth()-this.buttonClose.getWidth())/2, 20, this.buttonClose.getWidth(), this.buttonClose.getHeight());
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
            panelButton.add(getButtonOpenProc());
            panelButton.add(getButtonClose());
        }
        return panelButton;
    }

    private JButton getButtonOpenProc(){
        if(buttonOpenProc == null){
            buttonOpenProc = new JButton();
            buttonOpenProc.setName("buttonOpenProc");
            buttonOpenProc.setText(edP.getBundleString("BUTTON_OPEN_PROC_HELP"));
            buttonOpenProc.setSize(60+CopexUtilities.lenghtOfString(buttonOpenProc.getText(), buttonOpenProc.getFontMetrics(buttonOpenProc.getFont())), 23);
            buttonOpenProc.setBounds(30, 10, buttonOpenProc.getWidth(), buttonOpenProc.getHeight());
            buttonOpenProc.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                     openHelpProc();
                }
            });
        }
        return buttonOpenProc;
    }

    private JButton getButtonClose(){
        if(buttonClose == null){
            buttonClose = new JButton();
            buttonClose.setName("buttonClose");
            buttonClose.setText(edP.getBundleString("BUTTON_CLOSE"));
            buttonClose.setSize(60+CopexUtilities.lenghtOfString(buttonClose.getText(), buttonClose.getFontMetrics(buttonClose.getFont())), 23);
            buttonClose.setBounds(getWidth()-50-buttonClose.getWidth(), 10, buttonClose.getWidth(), buttonClose.getHeight());
            buttonClose.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                     closeHelpProc();
                }
            });
        }
        return buttonClose;
    }


    // open the help proc
    private void openHelpProc(){
        edP.displayHelpProc();
        this.dispose();
    }

    private void closeHelpProc(){
        edP.closeHelpDialog();
        this.dispose();
    }

    private void resizeButtonPanel(){
        if(scrollPaneHelp != null){
            this.scrollPaneHelp.setSize(scrollPaneHelp.getWidth(), getHeight()-100);
            scrollPaneHelp.setPreferredSize(scrollPaneHelp.getSize());
            scrollPaneHelp.revalidate();
            this.buttonClose.setBounds(getWidth()-50-this.buttonClose.getWidth(), buttonClose.getY(), this.buttonClose.getWidth(), this.buttonClose.getHeight());
        }else{
            this.buttonClose.setBounds((getWidth()-this.buttonClose.getWidth())/2, buttonClose.getY(), this.buttonClose.getWidth(), this.buttonClose.getHeight());
        }
        this.panelButton.setSize(getWidth(), panelButton.getHeight());
        this.panelButton.setPreferredSize(panelButton.getSize());
        
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(edP.getBundleString("TITLE_DIALOG_HELP")+" ("+edP.getVersion()+")");
        setMinimumSize(new java.awt.Dimension(400, 350));
        setModal(true);
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        resizeButtonPanel();
    }//GEN-LAST:event_formComponentResized

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                HelpDialog dialog = new HelpDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

}
