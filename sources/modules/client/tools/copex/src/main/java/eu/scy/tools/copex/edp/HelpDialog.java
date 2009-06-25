/*
 * HelpDialog.java
 *
 * Created on 24 octobre 2008, 08:26
 */

package eu.scy.tools.copex.edp;

import eu.scy.tools.copex.utilities.CopexUtilities;
import javax.swing.*;

/**
 * Fenetre d'aide . Se compose d'une partie explication, un bouton fermer
 * et un bouton qui permet d'ouvrir le protocole d'aide
 * @author  MBO
 */
public class HelpDialog extends javax.swing.JDialog {

    // ATTRIBUTS
    private EdPPanel edP;
    
    
   // CONSTRUCTEURS
    /** Creates new form HelpDialog */
    public HelpDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    public HelpDialog(EdPPanel edP) {
        super();
        this.edP = edP;
        initComponents();
        init();
        this.setLocationRelativeTo(edP);
        this.setModal(true);
        setLocation(edP.getLocationDialog());
        this.setResizable(false);
    }
    
    // initialisation du texte de l'aide 
    private void init(){
        int width = 580;
        int x = 10;
        int txtAreaW = 500;
        // queslques conseils pour vote protocole : 
        this.labelHelp.setText(edP.getBundleString("TEXT_HELP"));
        this.labelHelp.setSize(CopexUtilities.lenghtOfString(this.labelHelp.getText(), getFontMetrics(this.labelHelp.getFont())), this.labelHelp.getHeight());
        this.labelHelp.setBounds(x, 10, this.labelHelp.getWidth(), this.labelHelp.getHeight());
        width = Math.max(width, this.labelHelp.getWidth()+2*x);
        // icon question
        ImageIcon i = edP.getCopexImage("Bouton-AdT-28_question.png");
        this.labelIconQuestion.setIcon(i);
        this.labelIconQuestion.setSize(i.getIconWidth(), i.getIconHeight());
        this.labelIconQuestion.setBounds(x, this.labelHelp.getY() + this.labelHelp.getHeight()+15, this.labelIconQuestion.getWidth(), this.labelIconQuestion.getHeight());
        // txt question
        this.textAreaQuestion.setLineWrap(true);
        this.textAreaQuestion.setWrapStyleWord(true);
        this.textAreaQuestion.setText(edP.getBundleString("TEXT_HELP_QUESTION"));
        int l = CopexUtilities.lenghtOfString(this.textAreaQuestion.getText(), getFontMetrics(this.textAreaQuestion.getFont()));
        int h = ((l / txtAreaW) + 2)*15;
        this.jScrollPaneQuestion.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        this.jScrollPaneQuestion.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        this.jScrollPaneQuestion.setBorder(null);
        this.jScrollPaneQuestion.setSize(txtAreaW, h);
        this.textAreaQuestion.setSize(this.jScrollPaneQuestion.getSize());
        this.jScrollPaneQuestion.setBounds(this.labelIconQuestion.getX()+this.labelIconQuestion.getWidth()+5, this.labelIconQuestion.getY(), this.jScrollPaneQuestion.getWidth(), this.jScrollPaneQuestion.getHeight());
        // icon action
        i = edP.getCopexImage("Bouton-AdT-30_action.png");
        this.labelIconAction.setIcon(i);
        this.labelIconAction.setSize(i.getIconWidth(), i.getIconHeight());
        this.labelIconAction.setBounds(x, this.labelIconQuestion.getY() + this.jScrollPaneQuestion.getHeight()+10, this.labelIconAction.getWidth(), this.labelIconAction.getHeight());
        // txt action
        this.textAreaAction.setLineWrap(true);
        this.textAreaAction.setWrapStyleWord(true);
        this.textAreaAction.setText(edP.getBundleString("TEXT_HELP_ACTION"));
        l = CopexUtilities.lenghtOfString(this.textAreaAction.getText(), getFontMetrics(this.textAreaAction.getFont()));
        h = ((l / txtAreaW) + 2)*15;
        this.jScrollPaneAction.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        this.jScrollPaneAction.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        this.jScrollPaneAction.setBorder(null);
        this.jScrollPaneAction.setSize(txtAreaW, h);
        this.textAreaAction.setSize(this.jScrollPaneAction.getSize());
        this.jScrollPaneAction.setBounds(this.labelIconAction.getX()+this.labelIconAction.getWidth()+5, this.labelIconAction.getY(), this.jScrollPaneAction.getWidth(), this.jScrollPaneAction.getHeight());
        // icon step
        i = edP.getCopexImage("Bouton-AdT-30_etape.png");
        this.labelIconStep.setIcon(i);
        this.labelIconStep.setSize(i.getIconWidth(), i.getIconHeight());
        this.labelIconStep.setBounds(x, this.labelIconAction.getY() + this.jScrollPaneAction.getHeight()+10, this.labelIconStep.getWidth(), this.labelIconStep.getHeight());
        // txt step
        this.textAreaStep.setLineWrap(true);
        this.textAreaStep.setWrapStyleWord(true);
        this.textAreaStep.setText(edP.getBundleString("TEXT_HELP_STEP"));
        l = CopexUtilities.lenghtOfString(this.textAreaStep.getText(), getFontMetrics(this.textAreaStep.getFont()));
        h = ((l / txtAreaW) + 2)*15;
        this.jScrollPaneStep.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        this.jScrollPaneStep.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        this.jScrollPaneStep.setBorder(null);
        this.jScrollPaneStep.setSize(txtAreaW, h);
        this.textAreaStep.setSize(this.jScrollPaneStep.getSize());
        this.jScrollPaneStep.setBounds(this.labelIconStep.getX()+this.labelIconStep.getWidth()+5, this.labelIconStep.getY(), this.jScrollPaneStep.getWidth(), this.jScrollPaneStep.getHeight());
        txtAreaW += 30;
        // droits
        this.textAreaTaskRight.setLineWrap(true);
        this.textAreaTaskRight.setWrapStyleWord(true);
        this.textAreaTaskRight.setText(edP.getBundleString("TEXT_HELP_PROC_STRUCT"));
        l = CopexUtilities.lenghtOfString(this.textAreaTaskRight.getText(), getFontMetrics(this.textAreaTaskRight.getFont()));
        h = ((l / txtAreaW) + 2)*15;
        this.jScrollPaneRight.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        this.jScrollPaneRight.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        this.jScrollPaneRight.setBorder(null);
        this.jScrollPaneRight.setSize(txtAreaW, h);
        this.textAreaTaskRight.setSize(this.jScrollPaneRight.getSize());
        this.jScrollPaneRight.setBounds(x, this.jScrollPaneStep.getY() + this.jScrollPaneStep.getHeight()+5, this.jScrollPaneRight.getWidth(), this.jScrollPaneRight.getHeight());
        // 2 espaces
        this.labelSpaces.setText(edP.getBundleString("TEXT_HELP_SPACES"));
        this.labelSpaces.setSize(CopexUtilities.lenghtOfString(this.labelSpaces.getText(), getFontMetrics(this.labelSpaces.getFont())), this.labelSpaces.getHeight());
        this.labelSpaces.setBounds(x, this.jScrollPaneRight.getY() + this.jScrollPaneRight.getHeight()+5, this.labelSpaces.getWidth(), this.labelSpaces.getHeight());
        width = Math.max(width, this.labelSpaces.getWidth()+2*x);
        // material
        this.textAreaMaterial.setLineWrap(true);
        this.textAreaMaterial.setWrapStyleWord(true);
        this.textAreaMaterial.setText(edP.getBundleString("TEXT_HELP_MATERIAL"));
        l = CopexUtilities.lenghtOfString(this.textAreaMaterial.getText(), getFontMetrics(this.textAreaMaterial.getFont()));
        h = ((l / txtAreaW) + 2)*15;
        this.jScrollPaneMaterial.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        this.jScrollPaneMaterial.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        this.jScrollPaneMaterial.setBorder(null);
        this.jScrollPaneMaterial.setSize(txtAreaW, h);
        this.textAreaMaterial.setSize(this.jScrollPaneMaterial.getSize());
        this.jScrollPaneMaterial.setBounds(x, this.labelSpaces.getY() + this.labelSpaces.getHeight()+5, this.jScrollPaneMaterial.getWidth(), this.jScrollPaneMaterial.getHeight());
        // datasheet
        this.textAreaDataSheet.setLineWrap(true);
        this.textAreaDataSheet.setWrapStyleWord(true);
        this.textAreaDataSheet.setText(edP.getBundleString("TEXT_HELP_DATASHEET"));
        l = CopexUtilities.lenghtOfString(this.textAreaDataSheet.getText(), getFontMetrics(this.textAreaDataSheet.getFont()));
        h = ((l / txtAreaW) + 2)*15;
        this.jScrollPaneDataSheet.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        this.jScrollPaneDataSheet.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        this.jScrollPaneDataSheet.setBorder(null);
        this.jScrollPaneDataSheet.setSize(txtAreaW, h);
        this.textAreaDataSheet.setSize(this.jScrollPaneDataSheet.getSize());
        this.jScrollPaneDataSheet.setBounds(x, this.jScrollPaneMaterial.getY() + this.jScrollPaneMaterial.getHeight()+5, this.jScrollPaneDataSheet.getWidth(), this.jScrollPaneDataSheet.getHeight());

        // bouton ouvrir proc
        this.buttonOpenProc.setSize(60+CopexUtilities.lenghtOfString(this.buttonOpenProc.getText(), getFontMetrics(this.buttonOpenProc.getFont())), this.buttonOpenProc.getHeight());
        this.buttonOpenProc.setBounds(30, jScrollPaneDataSheet.getY()+jScrollPaneDataSheet.getHeight()+20, this.buttonOpenProc.getWidth(), this.buttonOpenProc.getHeight());
        // bouton fermer
        this.buttonClose.setSize(60+CopexUtilities.lenghtOfString(this.buttonClose.getText(), getFontMetrics(this.buttonClose.getFont())), this.buttonClose.getHeight());
        this.buttonClose.setBounds(width-50-this.buttonClose.getWidth(), jScrollPaneDataSheet.getY()+jScrollPaneDataSheet.getHeight()+20, this.buttonClose.getWidth(), this.buttonClose.getHeight());
        // taille fenetre
        setSize(width, this.buttonClose.getY()+this.buttonClose.getHeight()+70);
        setPreferredSize(getSize());
    }
    
    // ouverture du proc d'aide 
    private void openHelpProc(){
        edP.displayHelpProc();
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

        buttonOpenProc = new javax.swing.JButton();
        buttonClose = new javax.swing.JButton();
        labelHelp = new javax.swing.JLabel();
        jScrollPaneQuestion = new javax.swing.JScrollPane();
        textAreaQuestion = new javax.swing.JTextArea();
        jScrollPaneAction = new javax.swing.JScrollPane();
        textAreaAction = new javax.swing.JTextArea();
        jScrollPaneStep = new javax.swing.JScrollPane();
        textAreaStep = new javax.swing.JTextArea();
        jScrollPaneRight = new javax.swing.JScrollPane();
        textAreaTaskRight = new javax.swing.JTextArea();
        labelSpaces = new javax.swing.JLabel();
        jScrollPaneMaterial = new javax.swing.JScrollPane();
        textAreaMaterial = new javax.swing.JTextArea();
        labelIconQuestion = new javax.swing.JLabel();
        labelIconAction = new javax.swing.JLabel();
        labelIconStep = new javax.swing.JLabel();
        jScrollPaneDataSheet = new javax.swing.JScrollPane();
        textAreaDataSheet = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(edP.getBundleString("TITLE_DIALOG_HELP")+" ("+edP.getVersion()+")");
        setMinimumSize(new java.awt.Dimension(400, 350));
        setModal(true);
        getContentPane().setLayout(null);

        buttonOpenProc.setText(edP.getBundleString("BUTTON_OPEN_PROC_HELP"));
        buttonOpenProc.setMaximumSize(new java.awt.Dimension(200, 23));
        buttonOpenProc.setName("buttonOpenProc"); // NOI18N
        buttonOpenProc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonOpenProcActionPerformed(evt);
            }
        });
        getContentPane().add(buttonOpenProc);
        buttonOpenProc.setBounds(60, 440, 99, 23);

        buttonClose.setText(edP.getBundleString("BUTTON_CLOSE"));
        buttonClose.setMaximumSize(new java.awt.Dimension(300, 23));
        buttonClose.setName("buttonClose"); // NOI18N
        buttonClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCloseActionPerformed(evt);
            }
        });
        getContentPane().add(buttonClose);
        buttonClose.setBounds(230, 440, 99, 23);

        labelHelp.setText("jLabel1");
        labelHelp.setName("labelHelp"); // NOI18N
        getContentPane().add(labelHelp);
        labelHelp.setBounds(20, 10, 34, 14);

        jScrollPaneQuestion.setName("jScrollPaneQuestion"); // NOI18N

        textAreaQuestion.setBackground(javax.swing.UIManager.getDefaults().getColor("Label.background"));
        textAreaQuestion.setColumns(20);
        textAreaQuestion.setEditable(false);
        textAreaQuestion.setFont(new java.awt.Font("Tahoma", 0, 11));
        textAreaQuestion.setLineWrap(true);
        textAreaQuestion.setRows(5);
        textAreaQuestion.setName("textAreaQuestion"); // NOI18N
        jScrollPaneQuestion.setViewportView(textAreaQuestion);

        getContentPane().add(jScrollPaneQuestion);
        jScrollPaneQuestion.setBounds(60, 30, 300, 60);

        jScrollPaneAction.setName("jScrollPaneAction"); // NOI18N

        textAreaAction.setBackground(javax.swing.UIManager.getDefaults().getColor("Label.background"));
        textAreaAction.setColumns(20);
        textAreaAction.setEditable(false);
        textAreaAction.setFont(new java.awt.Font("Tahoma", 0, 11));
        textAreaAction.setLineWrap(true);
        textAreaAction.setRows(5);
        textAreaAction.setName("textAreaAction"); // NOI18N
        jScrollPaneAction.setViewportView(textAreaAction);

        getContentPane().add(jScrollPaneAction);
        jScrollPaneAction.setBounds(60, 100, 300, 60);

        jScrollPaneStep.setName("jScrollPaneStep"); // NOI18N

        textAreaStep.setBackground(javax.swing.UIManager.getDefaults().getColor("Label.background"));
        textAreaStep.setColumns(20);
        textAreaStep.setEditable(false);
        textAreaStep.setFont(new java.awt.Font("Tahoma", 0, 11));
        textAreaStep.setLineWrap(true);
        textAreaStep.setRows(5);
        textAreaStep.setName("textAreaStep"); // NOI18N
        jScrollPaneStep.setViewportView(textAreaStep);

        getContentPane().add(jScrollPaneStep);
        jScrollPaneStep.setBounds(60, 170, 300, 60);

        jScrollPaneRight.setName("jScrollPaneRight"); // NOI18N

        textAreaTaskRight.setBackground(javax.swing.UIManager.getDefaults().getColor("Label.background"));
        textAreaTaskRight.setColumns(20);
        textAreaTaskRight.setEditable(false);
        textAreaTaskRight.setFont(new java.awt.Font("Tahoma", 0, 11));
        textAreaTaskRight.setLineWrap(true);
        textAreaTaskRight.setRows(5);
        textAreaTaskRight.setName("textAreaTaskRight"); // NOI18N
        jScrollPaneRight.setViewportView(textAreaTaskRight);

        getContentPane().add(jScrollPaneRight);
        jScrollPaneRight.setBounds(60, 240, 300, 60);

        labelSpaces.setText("jLabel1");
        labelSpaces.setName("labelSpaces"); // NOI18N
        getContentPane().add(labelSpaces);
        labelSpaces.setBounds(10, 320, 34, 14);

        jScrollPaneMaterial.setName("jScrollPaneMaterial"); // NOI18N

        textAreaMaterial.setBackground(javax.swing.UIManager.getDefaults().getColor("Label.background"));
        textAreaMaterial.setColumns(20);
        textAreaMaterial.setEditable(false);
        textAreaMaterial.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        textAreaMaterial.setLineWrap(true);
        textAreaMaterial.setRows(5);
        textAreaMaterial.setName("textAreaMaterial"); // NOI18N
        jScrollPaneMaterial.setViewportView(textAreaMaterial);

        getContentPane().add(jScrollPaneMaterial);
        jScrollPaneMaterial.setBounds(60, 340, 300, 40);

        labelIconQuestion.setName("labelIconQuestion"); // NOI18N
        getContentPane().add(labelIconQuestion);
        labelIconQuestion.setBounds(20, 30, 0, 0);

        labelIconAction.setName("labelIconAction"); // NOI18N
        getContentPane().add(labelIconAction);
        labelIconAction.setBounds(10, 100, 0, 0);

        labelIconStep.setName("labelIconStep"); // NOI18N
        getContentPane().add(labelIconStep);
        labelIconStep.setBounds(10, 170, 0, 0);

        jScrollPaneDataSheet.setName("jScrollPaneDataSheet"); // NOI18N

        textAreaDataSheet.setBackground(javax.swing.UIManager.getDefaults().getColor("Label.background"));
        textAreaDataSheet.setColumns(20);
        textAreaDataSheet.setEditable(false);
        textAreaDataSheet.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        textAreaDataSheet.setLineWrap(true);
        textAreaDataSheet.setRows(5);
        textAreaDataSheet.setName("textAreaDataSheet"); // NOI18N
        jScrollPaneDataSheet.setViewportView(textAreaDataSheet);

        getContentPane().add(jScrollPaneDataSheet);
        jScrollPaneDataSheet.setBounds(60, 390, 300, 40);

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void buttonCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCloseActionPerformed
    edP.closeHelpDialog();
    this.dispose();
}//GEN-LAST:event_buttonCloseActionPerformed

private void buttonOpenProcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonOpenProcActionPerformed
    openHelpProc();
}//GEN-LAST:event_buttonOpenProcActionPerformed

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
    private javax.swing.JButton buttonClose;
    private javax.swing.JButton buttonOpenProc;
    private javax.swing.JScrollPane jScrollPaneAction;
    private javax.swing.JScrollPane jScrollPaneDataSheet;
    private javax.swing.JScrollPane jScrollPaneMaterial;
    private javax.swing.JScrollPane jScrollPaneQuestion;
    private javax.swing.JScrollPane jScrollPaneRight;
    private javax.swing.JScrollPane jScrollPaneStep;
    private javax.swing.JLabel labelHelp;
    private javax.swing.JLabel labelIconAction;
    private javax.swing.JLabel labelIconQuestion;
    private javax.swing.JLabel labelIconStep;
    private javax.swing.JLabel labelSpaces;
    private javax.swing.JTextArea textAreaAction;
    private javax.swing.JTextArea textAreaDataSheet;
    private javax.swing.JTextArea textAreaMaterial;
    private javax.swing.JTextArea textAreaQuestion;
    private javax.swing.JTextArea textAreaStep;
    private javax.swing.JTextArea textAreaTaskRight;
    // End of variables declaration//GEN-END:variables

}
