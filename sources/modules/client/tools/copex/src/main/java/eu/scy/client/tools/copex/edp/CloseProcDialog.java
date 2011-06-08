/*
 * CloseProcDialog.java
 *
 * Created on 1 aoat 2008, 09:08
 */

package eu.scy.client.tools.copex.edp;



import eu.scy.client.tools.copex.main.CopexPanel;
import eu.scy.client.tools.copex.common.LearnerProcedure;
import eu.scy.client.tools.copex.controller.ControllerInterface;
import eu.scy.client.tools.copex.utilities.CopexReturn;
import eu.scy.client.tools.copex.utilities.CopexUtilities;
import eu.scy.client.tools.copex.utilities.MyConstants;
import javax.swing.*;

/**
 * dialog that appears when you click on the cross tab of a proc
 * allows to ask the user if he wants to
 * - close the proc tab
 * - delete definitely the proc
 * @author  Marjolaine
 */
public class CloseProcDialog extends JDialog {

    /* owner */
    private CopexPanel edP;
    /* controller */
    private ControllerInterface controller;
    /* boolean: proc to close (true) or ot delete (false)*/
    private boolean isClose;
    /* proc */
    private LearnerProcedure proc;
 
    public CloseProcDialog(CopexPanel edP, ControllerInterface controller, LearnerProcedure proc) {
        super(edP.getOwnerFrame());
        this.edP = edP;
        this.controller = controller;
        this.proc = proc;
        this.setLocationRelativeTo(edP);
        initComponents();
        setModal(true);
        setLocation(edP.getLocationDialog());
        init();
        setIconImage(edP.getIconDialog());
    }
    
    
    /** Creates new form CloseProcDialog */
    public CloseProcDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }
    
    /* initialization */
    private void init(){
        // parameters in the text for radiobutton
        String s = edP.getBundleString("LABEL_CLOSE_PROC");
        s = CopexUtilities.replace(s,0, proc.getName(edP.getLocale()));
        rbClose.setText(s);
        s = edP.getBundleString("LABEL_SUPPR_PROC");
        s = CopexUtilities.replace(s,0, proc.getName(edP.getLocale()));
        rbSuppr.setText(s);
        if (proc.getRight() == MyConstants.NONE_RIGHT)
            this.rbSuppr.setEnabled(false);
        resizeElements();
        repaint();
    }

    /*
     * allows to resize the elements of the dialog depending of the string leght, (language dependant)
     */
   private void resizeElements(){
       // label question
       this.labelQuestion.setSize(CopexUtilities.lenghtOfString(this.labelQuestion.getText(), getFontMetrics(this.labelQuestion.getFont())), this.labelQuestion.getHeight());
       // rb close
       this.rbClose.setSize(CopexUtilities.lenghtOfString(this.rbClose.getText(), getFontMetrics(this.rbClose.getFont())), this.rbClose.getHeight());
       // rb delete
      this.rbSuppr.setSize(CopexUtilities.lenghtOfString(this.rbSuppr.getText(), getFontMetrics(this.rbSuppr.getFont())), this.rbSuppr.getHeight());
      this.rbSuppr.setPreferredSize(rbSuppr.getSize());
       int maxL = Math.max(rbClose.getWidth(), rbSuppr.getWidth());
       if (maxL + this.rbClose.getX() > getWidth()){
           int newWidth = maxL + this.rbClose.getX() +20;
           setSize(newWidth, getHeight());
       }
       // button Ok
       this.buttonOk.setSize(CopexUtilities.lenghtOfString(this.buttonOk.getText(), getFontMetrics(this.buttonOk.getFont())), this.buttonOk.getHeight());
       // button cancel
       this.buttonCancel.setSize(CopexUtilities.lenghtOfString(this.buttonCancel.getText(), getFontMetrics(this.buttonCancel.getFont())), this.buttonCancel.getHeight());
   }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labelQuestion = new javax.swing.JLabel();
        rbClose = new javax.swing.JRadioButton();
        rbSuppr = new javax.swing.JRadioButton();
        buttonOk = new javax.swing.JButton();
        buttonCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(edP.getBundleString("TITLE_DIALOG_CLOSE_PROC"));
        setModal(true);
        setResizable(false);

        labelQuestion.setFont(new java.awt.Font("Tahoma", 1, 11));
        labelQuestion.setText(edP.getBundleString("LABEL_QUESTION_TODO"));
        labelQuestion.setName("labelQuestion"); // NOI18N

        rbClose.setFont(new java.awt.Font("Tahoma", 1, 11));
        rbClose.setSelected(true);
        rbClose.setText(edP.getBundleString("LABEL_CLOSE_PROC"));
        rbClose.setName("rbClose"); // NOI18N
        rbClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbCloseActionPerformed(evt);
            }
        });

        rbSuppr.setFont(new java.awt.Font("Tahoma", 1, 11));
        rbSuppr.setText(edP.getBundleString("LABEL_SUPPR_PROC"));
        rbSuppr.setName("rbSuppr"); // NOI18N
        rbSuppr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbSupprActionPerformed(evt);
            }
        });

        buttonOk.setText(edP.getBundleString("BUTTON_OK"));
        buttonOk.setName("buttonOk"); // NOI18N
        buttonOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonOkActionPerformed(evt);
            }
        });

        buttonCancel.setText(edP.getBundleString("BUTTON_CANCEL"));
        buttonCancel.setName("buttonCancel"); // NOI18N
        buttonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(labelQuestion, javax.swing.GroupLayout.PREFERRED_SIZE, 318, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(rbSuppr, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(rbClose, javax.swing.GroupLayout.DEFAULT_SIZE, 309, Short.MAX_VALUE)))
                .addContainerGap(20, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addComponent(buttonOk)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 71, Short.MAX_VALUE)
                .addComponent(buttonCancel)
                .addGap(35, 35, 35))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelQuestion)
                .addGap(18, 18, 18)
                .addComponent(rbClose)
                .addGap(18, 18, 18)
                .addComponent(rbSuppr)
                .addGap(40, 40, 40)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonOk)
                    .addComponent(buttonCancel))
                .addContainerGap(41, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void rbCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbCloseActionPerformed
    rbClose.setSelected(true);
    rbSuppr.setSelected(false);
}//GEN-LAST:event_rbCloseActionPerformed

private void rbSupprActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbSupprActionPerformed
    rbClose.setSelected(false);
    rbSuppr.setSelected(true);
}//GEN-LAST:event_rbSupprActionPerformed

private void buttonOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonOkActionPerformed
    // gets data
    this.isClose = rbClose.isSelected();
    if (this.isClose){
        CopexReturn cr = controller.closeProc(this.proc);
        if (cr.isError()){
            edP.displayError(cr, edP.getBundleString("TITLE_DIALOG_ERROR"));
        }
    }else{
        CopexReturn cr = controller.deleteProc(this.proc);
        if (cr.isError()){
            edP.displayError(cr, edP.getBundleString("TITLE_DIALOG_ERROR"));
        }
    }
    this.dispose();
}//GEN-LAST:event_buttonOkActionPerformed

private void buttonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCancelActionPerformed
    this.dispose();
}//GEN-LAST:event_buttonCancelActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                CloseProcDialog dialog = new CloseProcDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton buttonCancel;
    private javax.swing.JButton buttonOk;
    private javax.swing.JLabel labelQuestion;
    private javax.swing.JRadioButton rbClose;
    private javax.swing.JRadioButton rbSuppr;
    // End of variables declaration//GEN-END:variables

}
