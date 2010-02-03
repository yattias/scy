/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * HelpManipulationDialog.java
 *
 * Created on 20 nov. 2009, 11:11:59
 */

package eu.scy.tools.copex.edp;

import eu.scy.tools.copex.utilities.CopexUtilities;
import java.awt.Font;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;

/**
 * help manipulation dialog (double cliking on the manipulation node and task tree empty)
 * @author Marjolaine
 */
public class HelpManipulationDialog extends javax.swing.JDialog {

    private EdPPanel edP;
    private boolean taskProc;
    private boolean questionBefore;

    private JButton buttonOk;

    public HelpManipulationDialog(EdPPanel edP, boolean taskProc, boolean questionBefore) {
        super(edP.getOwnerFrame());
        this.edP = edP;
        this.taskProc = taskProc;
        this.questionBefore = questionBefore;
        initComponents();
        setLocation(edP.getLocationDialog());
        initGUI();
    }


    /** Creates new form HelpManipulationDialog */
    public HelpManipulationDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
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
        setTitle(edP.getBundleString("TITLE_DIALOG_HELP_MANIPULATION"));
        setModal(true);
        setResizable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                HelpManipulationDialog dialog = new HelpManipulationDialog(new javax.swing.JFrame(), true);
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


    private void initGUI(){
        setLayout(null);
        int w = 100;
        int y = 0;
        if(questionBefore){
            JLabel label = new JLabel();
            label.setName("label");
            label.setText(edP.getBundleString("LABEL_HELP_MANIPULATION_QUESTION"));
            label.setFont(new java.awt.Font("Tahoma", Font.PLAIN, 11));
            label.setBounds(10, 10,CopexUtilities.lenghtOfString(label.getText(), getFontMetrics(label.getFont())), 14 );
            getContentPane().add(label);
            w = label.getX()+label.getWidth()+10;
            y = label.getY()+label.getHeight()+20;
        }else{
            // ajout du texte
            String text = this.taskProc ? edP.getBundleString("LABEL_HELP_MANIPULATION_TASK") : edP.getBundleString("LABEL_HELP_MANIPULATION");
            int id = text.indexOf("{");
            String t1 = text.substring(0, id);
            JLabel label1 = new JLabel(t1);
            label1.setFont(new java.awt.Font("Tahoma", Font.PLAIN, 11));
            label1.setBounds(10, 10,CopexUtilities.lenghtOfString(label1.getText(), getFontMetrics(label1.getFont())), 14 );
            getContentPane().add(label1);
            // bouton
            String stepI = taskProc ? "Bouton-AdT-28_task.png" : "Bouton-AdT-28_step.png" ;
            Icon stepIcon = edP.getCopexImage(stepI);
            JLabel labelIconStep = new JLabel();
            labelIconStep.setIcon(stepIcon);
            labelIconStep.setSize(stepIcon.getIconWidth(), stepIcon.getIconHeight());
            labelIconStep.setBounds(label1.getX()+label1.getWidth()+5, label1.getY()-5, labelIconStep.getWidth(), labelIconStep.getHeight());
            getContentPane().add(labelIconStep);
            //si bouton action en +
            int x = labelIconStep.getX()+labelIconStep.getWidth()+5;
            if(!taskProc){
                int id2 = (text.substring(id+3, text.length())).indexOf("{")+text.substring(0,id+3).length();
                String t2 = text.substring(id+3, id2);
                JLabel label2 = new JLabel(t2);
                label2.setFont(new java.awt.Font("Tahoma", 1, 11));
                label2.setBounds(labelIconStep.getX()+labelIconStep.getWidth()+5, 10,CopexUtilities.lenghtOfString(label2.getText(), getFontMetrics(label2.getFont())), 14 );
                getContentPane().add(label2);
                Icon actionIcon = edP.getCopexImage("Bouton-AdT-28_action.png");
                JLabel labelIconAction = new JLabel();
                labelIconAction.setIcon(actionIcon);
                labelIconAction.setSize(actionIcon.getIconWidth(), actionIcon.getIconHeight());
                labelIconAction.setBounds(label2.getX()+label2.getWidth()+5, label1.getY()-5, labelIconAction.getWidth(), labelIconAction.getHeight());
                getContentPane().add(labelIconAction);
                id = id2;
                x = labelIconAction.getX()+labelIconAction.getWidth()+5;
            }
            // fin de la phrase
            String tend = text.substring(id+3, text.length());
            JLabel labelEnd = new JLabel(tend);
            labelEnd.setFont(new java.awt.Font("Tahoma", Font.PLAIN, 11));
            labelEnd.setBounds(x, 10,CopexUtilities.lenghtOfString(labelEnd.getText(), getFontMetrics(labelEnd.getFont())), 14 );
            getContentPane().add(labelEnd);
            // taille de la fenetre
            w = labelEnd.getX()+labelEnd.getWidth()+10;
            y = label1.getY()+label1.getHeight()+20;
        }
        // ajout du bouton ok
        getButtonOk();
        int x = (w-buttonOk.getWidth()) / 2 ;
        this.buttonOk.setBounds(x,y, buttonOk.getWidth(), buttonOk.getHeight());
        setSize(w, buttonOk.getY()+buttonOk.getHeight()+40);
        setPreferredSize(getSize());
        getContentPane().add(buttonOk);
    }

    private void closeDialog(){
        this.dispose();
    }

    private JButton getButtonOk(){
        if(buttonOk == null){
            buttonOk = new JButton();
            buttonOk.setName("buttonOk");
            buttonOk.setText(edP.getBundleString("BUTTON_OK"));
            buttonOk.setSize(60+CopexUtilities.lenghtOfString(this.buttonOk.getText(), getFontMetrics(this.buttonOk.getFont())),23);
            buttonOk.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    closeDialog();
                }
            });
        }
        return buttonOk;
    }
}
