/*
 * HelpDialog.java
 *
 * Created on 24 octobre 2008, 08:26
 */

package eu.scy.tools.copex.edp;

import eu.scy.tools.copex.utilities.CopexUtilities;
import java.awt.Color;
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
        super(edP.getOwnerFrame());
        this.edP = edP;
        initComponents();
        init();
        setIconImage(edP.getIconDialog());
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
        // queslques conseils pour votre protocole :
        this.labelHelp.setText(edP.getBundleString("TEXT_HELP"));
        this.labelHelp.setSize(CopexUtilities.lenghtOfString(this.labelHelp.getText(), getFontMetrics(this.labelHelp.getFont())), this.labelHelp.getHeight());
        this.labelHelp.setBounds(x, 10, this.labelHelp.getWidth(), this.labelHelp.getHeight());
        width = Math.max(width, this.labelHelp.getWidth()+2*x);
        // txt question
        this.textAreaQuestion.setLineWrap(true);
        this.textAreaQuestion.setWrapStyleWord(true);
        this.textAreaQuestion.setText(edP.getBundleString("TEXT_HELP_QUESTION"));
        int l = CopexUtilities.lenghtOfString(this.textAreaQuestion.getText(), getFontMetrics(this.textAreaQuestion.getFont()));
        int h = ((l / txtAreaW) + 2)*15;
        this.jScrollPaneQuestion.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        this.jScrollPaneQuestion.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        this.jScrollPaneQuestion.setBorder(null);
        textAreaQuestion.setBackground(new Color(0,0,0,0));
        textAreaQuestion.setBorder(null);

        this.jScrollPaneQuestion.setSize(txtAreaW, h);
        this.textAreaQuestion.setSize(this.jScrollPaneQuestion.getSize());
        this.jScrollPaneQuestion.setBounds(x+10, this.labelHelp.getY()+this.labelHelp.getHeight()+10, this.jScrollPaneQuestion.getWidth(), this.jScrollPaneQuestion.getHeight());
        // hypothesis
        ImageIcon i = edP.getCopexImage("icone_AdT_hypothese.png");
        this.labelIconHypothesis.setIcon(i);
        this.labelIconHypothesis.setSize(i.getIconWidth(), i.getIconHeight());
        this.labelIconHypothesis.setBounds(x, this.jScrollPaneQuestion.getY()+jScrollPaneQuestion.getHeight()+10, this.labelIconHypothesis.getWidth(), this.labelIconHypothesis.getHeight());;
        this.textAreaHypothesis.setLineWrap(true);
        this.textAreaHypothesis.setWrapStyleWord(true);
        textAreaHypothesis.setBackground(new Color(0,0,0,0));
        textAreaHypothesis.setBorder(null);
        this.textAreaHypothesis.setText(edP.getBundleString("TEXT_HELP_HYPOTHESIS"));
        l = CopexUtilities.lenghtOfString(this.textAreaHypothesis.getText(), getFontMetrics(this.textAreaHypothesis.getFont()));
        h = ((l / txtAreaW) + 2)*15;
        this.jScrollPaneHypothesis.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        this.jScrollPaneHypothesis.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        this.jScrollPaneHypothesis.setBorder(null);
        this.jScrollPaneHypothesis.setSize(txtAreaW, h);
        this.textAreaHypothesis.setSize(this.jScrollPaneHypothesis.getSize());
        this.jScrollPaneHypothesis.setBounds(this.labelIconHypothesis.getX()+this.labelIconHypothesis.getWidth()+5, this.labelIconHypothesis.getY(), this.jScrollPaneHypothesis.getWidth(), this.jScrollPaneHypothesis.getHeight());
        // general  principle
        i = edP.getCopexImage("icone_AdT_principe.png");
        this.labelIconPrinciple.setIcon(i);
        this.labelIconPrinciple.setSize(i.getIconWidth(), i.getIconHeight());
        this.labelIconPrinciple.setBounds(x, this.jScrollPaneHypothesis.getY()+jScrollPaneHypothesis.getHeight()+10, this.labelIconPrinciple.getWidth(), this.labelIconPrinciple.getHeight());
        this.textAreaPrinciple.setLineWrap(true);
        this.textAreaPrinciple.setWrapStyleWord(true);
        textAreaPrinciple.setBackground(new Color(0,0,0,0));
        textAreaPrinciple.setBorder(null);
        this.textAreaPrinciple.setText(edP.getBundleString("TEXT_HELP_GENERAL_PRINCIPLE"));
        l = CopexUtilities.lenghtOfString(this.textAreaPrinciple.getText(), getFontMetrics(this.textAreaPrinciple.getFont()));
        h = ((l / txtAreaW) + 2)*15;
        this.jScrollPanePrinciple.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        this.jScrollPanePrinciple.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        this.jScrollPanePrinciple.setBorder(null);
        this.jScrollPanePrinciple.setSize(txtAreaW, h);
        this.textAreaPrinciple.setSize(this.jScrollPanePrinciple.getSize());
        this.jScrollPanePrinciple.setBounds(this.labelIconPrinciple.getX()+this.labelIconPrinciple.getWidth()+5, this.labelIconPrinciple.getY(), this.jScrollPanePrinciple.getWidth(), this.jScrollPanePrinciple.getHeight());
        // material
        i = edP.getCopexImage("icone_AdT_material.png");
        this.labelIconMaterial.setIcon(i);
        this.labelIconMaterial.setSize(i.getIconWidth(), i.getIconHeight());
        this.labelIconMaterial.setBounds(x, this.jScrollPanePrinciple.getY()+jScrollPanePrinciple.getHeight()+10, this.labelIconMaterial.getWidth(), this.labelIconMaterial.getHeight());
        this.textAreaMaterial.setLineWrap(true);
        this.textAreaMaterial.setWrapStyleWord(true);
        textAreaMaterial.setBackground(new Color(0,0,0,0));
        textAreaMaterial.setBorder(null);
        this.textAreaMaterial.setText(edP.getBundleString("TEXT_HELP_MATERIAL"));
        l = CopexUtilities.lenghtOfString(this.textAreaMaterial.getText(), getFontMetrics(this.textAreaMaterial.getFont()));
        h = ((l / txtAreaW) + 2)*15;
        this.jScrollPaneMaterial.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        this.jScrollPaneMaterial.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        this.jScrollPaneMaterial.setBorder(null);
        this.jScrollPaneMaterial.setSize(txtAreaW, h);
        this.textAreaMaterial.setSize(this.jScrollPaneMaterial.getSize());
        this.jScrollPaneMaterial.setBounds(this.labelIconMaterial.getX()+this.labelIconMaterial.getWidth()+5, this.labelIconMaterial.getY(), this.jScrollPaneMaterial.getWidth(), this.jScrollPaneMaterial.getHeight());
        // manipulation
        i = edP.getCopexImage("icone_AdT_manip.png");
        this.labelIconManipulation.setIcon(i);
        this.labelIconManipulation.setSize(i.getIconWidth(), i.getIconHeight());
        this.labelIconManipulation.setBounds(x, this.jScrollPaneMaterial.getY()+jScrollPaneMaterial.getHeight()+10, this.labelIconManipulation.getWidth(), this.labelIconManipulation.getHeight());
        this.textAreaManipulation.setLineWrap(true);
        this.textAreaManipulation.setWrapStyleWord(true);
        textAreaManipulation.setBackground(new Color(0,0,0,0));
        textAreaManipulation.setBorder(null);
        this.textAreaManipulation.setText(edP.getBundleString("TEXT_HELP_MANIPULATION"));
        l = CopexUtilities.lenghtOfString(this.textAreaManipulation.getText(), getFontMetrics(this.textAreaManipulation.getFont()));
        h = ((l / txtAreaW) + 2)*15;
        this.jScrollPaneManipulation.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        this.jScrollPaneManipulation.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        this.jScrollPaneManipulation.setBorder(null);
        this.jScrollPaneManipulation.setSize(txtAreaW, h);
        this.textAreaManipulation.setSize(this.jScrollPaneManipulation.getSize());
        this.jScrollPaneManipulation.setBounds(this.labelIconManipulation.getX()+this.labelIconManipulation.getWidth()+5, this.labelIconManipulation.getY(), this.jScrollPaneManipulation.getWidth(), this.jScrollPaneManipulation.getHeight());
        // icon action
        i = edP.getCopexImage("Icone-AdT_action.png");
        this.labelIconAction.setIcon(i);
        this.labelIconAction.setSize(i.getIconWidth(), i.getIconHeight());
        this.labelIconAction.setBounds(x, this.jScrollPaneManipulation.getY() + this.jScrollPaneManipulation.getHeight()+10, this.labelIconAction.getWidth(), this.labelIconAction.getHeight());
        this.textAreaAction.setLineWrap(true);
        this.textAreaAction.setWrapStyleWord(true);
        textAreaAction.setBackground(new Color(0,0,0,0));
        textAreaAction.setBorder(null);
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
        i = edP.getCopexImage("Icone-AdT_etape.png");
        this.labelIconStep.setIcon(i);
        this.labelIconStep.setSize(i.getIconWidth(), i.getIconHeight());
        this.labelIconStep.setBounds(x, this.jScrollPaneAction.getY() + this.jScrollPaneAction.getHeight()+10, this.labelIconStep.getWidth(), this.labelIconStep.getHeight());
        this.textAreaStep.setLineWrap(true);
        this.textAreaStep.setWrapStyleWord(true);
        textAreaStep.setBackground(new Color(0,0,0,0));
        textAreaStep.setBorder(null);
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
        textAreaTaskRight.setBackground(new Color(0,0,0,0));
        textAreaTaskRight.setBorder(null);
        this.textAreaTaskRight.setText(edP.getBundleString("TEXT_HELP_PROC_STRUCT"));
        l = CopexUtilities.lenghtOfString(this.textAreaTaskRight.getText(), getFontMetrics(this.textAreaTaskRight.getFont()));
        h = ((l / txtAreaW) + 2)*15;
        this.jScrollPaneRight.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        this.jScrollPaneRight.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        this.jScrollPaneRight.setBorder(null);
        this.jScrollPaneRight.setSize(txtAreaW, h);
        this.textAreaTaskRight.setSize(this.jScrollPaneRight.getSize());
        this.jScrollPaneRight.setBounds(x, this.jScrollPaneStep.getY() + this.jScrollPaneStep.getHeight()+5, this.jScrollPaneRight.getWidth(), this.jScrollPaneRight.getHeight());
        // datasheet
        i = edP.getCopexImage("icone_AdT_datasheet.png");
        this.labelIconDatasheet.setIcon(i);
        this.labelIconDatasheet.setSize(i.getIconWidth(), i.getIconHeight());
        this.labelIconDatasheet.setBounds(x, this.jScrollPaneRight.getY()+jScrollPaneRight.getHeight()+10, this.labelIconDatasheet.getWidth(), this.labelIconDatasheet.getHeight());
        this.textAreaDatasheet.setLineWrap(true);
        this.textAreaDatasheet.setWrapStyleWord(true);
        textAreaDatasheet.setBackground(new Color(0,0,0,0));
        textAreaDatasheet.setBorder(null);
        this.textAreaDatasheet.setText(edP.getBundleString("TEXT_HELP_DATASHEET"));
        l = CopexUtilities.lenghtOfString(this.textAreaDatasheet.getText(), getFontMetrics(this.textAreaDatasheet.getFont()));
        h = ((l / txtAreaW) + 2)*15;
        this.jScrollPaneDatasheet.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        this.jScrollPaneDatasheet.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        this.jScrollPaneDatasheet.setBorder(null);
        this.jScrollPaneDatasheet.setSize(txtAreaW, h);
        this.textAreaDatasheet.setSize(this.jScrollPaneDatasheet.getSize());
        this.jScrollPaneDatasheet.setBounds(this.labelIconDatasheet.getX()+this.labelIconDatasheet.getWidth()+5, this.labelIconDatasheet.getY(), this.jScrollPaneDatasheet.getWidth(), this.jScrollPaneDatasheet.getHeight());
        // evaluation
        i = edP.getCopexImage("icone_AdT_eval.png");
        this.labelIconEvaluation.setIcon(i);
        this.labelIconEvaluation.setSize(i.getIconWidth(), i.getIconHeight());
        this.labelIconEvaluation.setBounds(x, this.jScrollPaneDatasheet.getY()+jScrollPaneDatasheet.getHeight()+10, this.labelIconEvaluation.getWidth(), this.labelIconEvaluation.getHeight());
        this.textAreaEvaluation.setLineWrap(true);
        this.textAreaEvaluation.setWrapStyleWord(true);
        textAreaEvaluation.setBackground(new Color(0,0,0,0));
        textAreaEvaluation.setBorder(null);
        this.textAreaEvaluation.setText(edP.getBundleString("TEXT_HELP_EVALUATION"));
        l = CopexUtilities.lenghtOfString(this.textAreaEvaluation.getText(), getFontMetrics(this.textAreaEvaluation.getFont()));
        h = ((l / txtAreaW) + 2)*15;
        this.jScrollPaneEvaluation.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        this.jScrollPaneEvaluation.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        this.jScrollPaneEvaluation.setBorder(null);
        this.jScrollPaneEvaluation.setSize(txtAreaW, h);
        this.textAreaEvaluation.setSize(this.jScrollPaneEvaluation.getSize());
        this.jScrollPaneEvaluation.setBounds(this.labelIconEvaluation.getX()+this.labelIconEvaluation.getWidth()+5, this.labelIconEvaluation.getY(), this.jScrollPaneEvaluation.getWidth(), this.jScrollPaneEvaluation.getHeight());

        // bouton ouvrir proc
        this.buttonOpenProc.setSize(60+CopexUtilities.lenghtOfString(this.buttonOpenProc.getText(), getFontMetrics(this.buttonOpenProc.getFont())), this.buttonOpenProc.getHeight());
        this.buttonOpenProc.setBounds(30, jScrollPaneEvaluation.getY()+jScrollPaneEvaluation.getHeight()+20, this.buttonOpenProc.getWidth(), this.buttonOpenProc.getHeight());
        // bouton fermer
        this.buttonClose.setSize(60+CopexUtilities.lenghtOfString(this.buttonClose.getText(), getFontMetrics(this.buttonClose.getFont())), this.buttonClose.getHeight());
        this.buttonClose.setBounds(width-50-this.buttonClose.getWidth(), jScrollPaneEvaluation.getY()+jScrollPaneEvaluation.getHeight()+20, this.buttonClose.getWidth(), this.buttonClose.getHeight());
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
        labelIconAction = new javax.swing.JLabel();
        labelIconStep = new javax.swing.JLabel();
        jScrollPaneHypothesis = new javax.swing.JScrollPane();
        textAreaHypothesis = new javax.swing.JTextArea();
        labelIconHypothesis = new javax.swing.JLabel();
        jScrollPanePrinciple = new javax.swing.JScrollPane();
        textAreaPrinciple = new javax.swing.JTextArea();
        jScrollPaneMaterial = new javax.swing.JScrollPane();
        textAreaMaterial = new javax.swing.JTextArea();
        jScrollPaneManipulation = new javax.swing.JScrollPane();
        textAreaManipulation = new javax.swing.JTextArea();
        jScrollPaneDatasheet = new javax.swing.JScrollPane();
        textAreaDatasheet = new javax.swing.JTextArea();
        jScrollPaneEvaluation = new javax.swing.JScrollPane();
        textAreaEvaluation = new javax.swing.JTextArea();
        labelIconPrinciple = new javax.swing.JLabel();
        labelIconMaterial = new javax.swing.JLabel();
        labelIconManipulation = new javax.swing.JLabel();
        labelIconDatasheet = new javax.swing.JLabel();
        labelIconEvaluation = new javax.swing.JLabel();

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
        buttonOpenProc.setBounds(60, 550, 99, 23);

        buttonClose.setText(edP.getBundleString("BUTTON_CLOSE"));
        buttonClose.setMaximumSize(new java.awt.Dimension(300, 23));
        buttonClose.setName("buttonClose"); // NOI18N
        buttonClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCloseActionPerformed(evt);
            }
        });
        getContentPane().add(buttonClose);
        buttonClose.setBounds(230, 550, 99, 23);

        labelHelp.setName("labelHelp"); // NOI18N
        getContentPane().add(labelHelp);
        labelHelp.setBounds(20, 10, 20, 15);

        jScrollPaneQuestion.setName("jScrollPaneQuestion"); // NOI18N

        textAreaQuestion.setBackground(javax.swing.UIManager.getDefaults().getColor("Label.background"));
        textAreaQuestion.setColumns(20);
        textAreaQuestion.setEditable(false);
        textAreaQuestion.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        textAreaQuestion.setLineWrap(true);
        textAreaQuestion.setRows(5);
        textAreaQuestion.setName("textAreaQuestion"); // NOI18N
        textAreaQuestion.setOpaque(false);
        jScrollPaneQuestion.setViewportView(textAreaQuestion);

        getContentPane().add(jScrollPaneQuestion);
        jScrollPaneQuestion.setBounds(60, 30, 300, 40);

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
        jScrollPaneAction.setBounds(60, 280, 300, 40);

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
        jScrollPaneStep.setBounds(60, 330, 300, 40);

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
        jScrollPaneRight.setBounds(60, 380, 300, 40);

        labelIconAction.setName("labelIconAction"); // NOI18N
        getContentPane().add(labelIconAction);
        labelIconAction.setBounds(10, 280, 20, 10);

        labelIconStep.setName("labelIconStep"); // NOI18N
        getContentPane().add(labelIconStep);
        labelIconStep.setBounds(10, 330, 20, 10);

        jScrollPaneHypothesis.setName("jScrollPaneHypothesis"); // NOI18N

        textAreaHypothesis.setBackground(javax.swing.UIManager.getDefaults().getColor("Label.background"));
        textAreaHypothesis.setColumns(20);
        textAreaHypothesis.setEditable(false);
        textAreaHypothesis.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        textAreaHypothesis.setLineWrap(true);
        textAreaHypothesis.setRows(5);
        textAreaHypothesis.setName("textAreaHypothesis"); // NOI18N
        textAreaHypothesis.setOpaque(false);
        jScrollPaneHypothesis.setViewportView(textAreaHypothesis);

        getContentPane().add(jScrollPaneHypothesis);
        jScrollPaneHypothesis.setBounds(60, 80, 300, 40);

        labelIconHypothesis.setName("labelIconHypothesis"); // NOI18N
        getContentPane().add(labelIconHypothesis);
        labelIconHypothesis.setBounds(10, 80, 20, 10);

        jScrollPanePrinciple.setName("jScrollPanePrinciple"); // NOI18N

        textAreaPrinciple.setBackground(javax.swing.UIManager.getDefaults().getColor("Label.background"));
        textAreaPrinciple.setColumns(20);
        textAreaPrinciple.setEditable(false);
        textAreaPrinciple.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        textAreaPrinciple.setLineWrap(true);
        textAreaPrinciple.setRows(5);
        textAreaPrinciple.setName("textAreaPrinciple"); // NOI18N
        jScrollPanePrinciple.setViewportView(textAreaPrinciple);

        getContentPane().add(jScrollPanePrinciple);
        jScrollPanePrinciple.setBounds(60, 130, 300, 40);

        jScrollPaneMaterial.setName("jScrollPaneMaterial"); // NOI18N

        textAreaMaterial.setBackground(javax.swing.UIManager.getDefaults().getColor("Label.background"));
        textAreaMaterial.setColumns(20);
        textAreaMaterial.setEditable(false);
        textAreaMaterial.setFont(new java.awt.Font("Tahoma", 0, 11));
        textAreaMaterial.setLineWrap(true);
        textAreaMaterial.setRows(5);
        textAreaMaterial.setName("textAreaMaterial"); // NOI18N
        jScrollPaneMaterial.setViewportView(textAreaMaterial);

        getContentPane().add(jScrollPaneMaterial);
        jScrollPaneMaterial.setBounds(60, 180, 300, 40);

        jScrollPaneManipulation.setName("jScrollPaneManipulation"); // NOI18N

        textAreaManipulation.setBackground(javax.swing.UIManager.getDefaults().getColor("Label.background"));
        textAreaManipulation.setColumns(20);
        textAreaManipulation.setEditable(false);
        textAreaManipulation.setFont(new java.awt.Font("Tahoma", 0, 11));
        textAreaManipulation.setLineWrap(true);
        textAreaManipulation.setRows(5);
        textAreaManipulation.setName("textAreaManipulation"); // NOI18N
        jScrollPaneManipulation.setViewportView(textAreaManipulation);

        getContentPane().add(jScrollPaneManipulation);
        jScrollPaneManipulation.setBounds(60, 230, 300, 40);

        jScrollPaneDatasheet.setName("jScrollPaneDatasheet"); // NOI18N

        textAreaDatasheet.setBackground(javax.swing.UIManager.getDefaults().getColor("Label.background"));
        textAreaDatasheet.setColumns(20);
        textAreaDatasheet.setEditable(false);
        textAreaDatasheet.setFont(new java.awt.Font("Tahoma", 0, 11));
        textAreaDatasheet.setLineWrap(true);
        textAreaDatasheet.setRows(5);
        textAreaDatasheet.setName("textAreaDatasheet"); // NOI18N
        jScrollPaneDatasheet.setViewportView(textAreaDatasheet);

        getContentPane().add(jScrollPaneDatasheet);
        jScrollPaneDatasheet.setBounds(60, 430, 300, 40);

        jScrollPaneEvaluation.setName("jScrollPaneEvaluation"); // NOI18N

        textAreaEvaluation.setBackground(javax.swing.UIManager.getDefaults().getColor("Label.background"));
        textAreaEvaluation.setColumns(20);
        textAreaEvaluation.setEditable(false);
        textAreaEvaluation.setFont(new java.awt.Font("Tahoma", 0, 11));
        textAreaEvaluation.setLineWrap(true);
        textAreaEvaluation.setRows(5);
        textAreaEvaluation.setName("textAreaEvaluation"); // NOI18N
        jScrollPaneEvaluation.setViewportView(textAreaEvaluation);

        getContentPane().add(jScrollPaneEvaluation);
        jScrollPaneEvaluation.setBounds(60, 480, 300, 40);

        labelIconPrinciple.setName("labelIconPrinciple"); // NOI18N
        getContentPane().add(labelIconPrinciple);
        labelIconPrinciple.setBounds(10, 130, 20, 10);

        labelIconMaterial.setName("labelIconMaterial"); // NOI18N
        getContentPane().add(labelIconMaterial);
        labelIconMaterial.setBounds(10, 180, 20, 10);

        labelIconManipulation.setName("labelIconManipulation"); // NOI18N
        getContentPane().add(labelIconManipulation);
        labelIconManipulation.setBounds(10, 230, 20, 10);

        labelIconDatasheet.setName("labelIconDatasheet"); // NOI18N
        getContentPane().add(labelIconDatasheet);
        labelIconDatasheet.setBounds(10, 430, 20, 10);

        labelIconEvaluation.setName("labelIconEvaluation"); // NOI18N
        getContentPane().add(labelIconEvaluation);
        labelIconEvaluation.setBounds(10, 480, 20, 10);

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
    private javax.swing.JScrollPane jScrollPaneDatasheet;
    private javax.swing.JScrollPane jScrollPaneEvaluation;
    private javax.swing.JScrollPane jScrollPaneHypothesis;
    private javax.swing.JScrollPane jScrollPaneManipulation;
    private javax.swing.JScrollPane jScrollPaneMaterial;
    private javax.swing.JScrollPane jScrollPanePrinciple;
    private javax.swing.JScrollPane jScrollPaneQuestion;
    private javax.swing.JScrollPane jScrollPaneRight;
    private javax.swing.JScrollPane jScrollPaneStep;
    private javax.swing.JLabel labelHelp;
    private javax.swing.JLabel labelIconAction;
    private javax.swing.JLabel labelIconDatasheet;
    private javax.swing.JLabel labelIconEvaluation;
    private javax.swing.JLabel labelIconHypothesis;
    private javax.swing.JLabel labelIconManipulation;
    private javax.swing.JLabel labelIconMaterial;
    private javax.swing.JLabel labelIconPrinciple;
    private javax.swing.JLabel labelIconStep;
    private javax.swing.JTextArea textAreaAction;
    private javax.swing.JTextArea textAreaDatasheet;
    private javax.swing.JTextArea textAreaEvaluation;
    private javax.swing.JTextArea textAreaHypothesis;
    private javax.swing.JTextArea textAreaManipulation;
    private javax.swing.JTextArea textAreaMaterial;
    private javax.swing.JTextArea textAreaPrinciple;
    private javax.swing.JTextArea textAreaQuestion;
    private javax.swing.JTextArea textAreaStep;
    private javax.swing.JTextArea textAreaTaskRight;
    // End of variables declaration//GEN-END:variables

}
