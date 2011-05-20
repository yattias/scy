/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.dataProcessTool.dataTool;

import eu.scy.client.tools.fitex.main.DataProcessToolPanel;
import eu.scy.client.tools.dataProcessTool.common.Dataset;
import eu.scy.client.tools.dataProcessTool.utilities.DataConstants;
import eu.scy.client.tools.dataProcessTool.utilities.MyUtilities;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JRadioButton;
import org.jdom.Element;

/**
 * ask the user what to do while merging
 * @author Marjolaine
 */
public class MergeDialog extends JDialog implements ActionListener {
    private static final int MIN_WIDTH_MERGE_DIALOG = 350;
    private DataProcessToolPanel owner;

    private Dataset ds1;
    private Dataset ds2;
    private Element elo;
    private boolean isMatrix;

    private JRadioButton rbMerge;
    private JRadioButton rbMergeRow;
    private JRadioButton rbMatrixAddOperation;
    private JRadioButton rbMatrixMultiplyOperation;
    private ButtonGroup rbGroup = null;

    private JButton buttonOk;
    private JButton buttonCancel;
    private String mergeAction = DataConstants.actionMerge;

    public MergeDialog(DataProcessToolPanel owner, Dataset ds1, Dataset ds2, Element elo, boolean isMatrix) {
        super();
        this.owner = owner;
        this.isMatrix = isMatrix;
        this.ds1 = ds1;
        this.ds2 = ds2;
        this.elo = elo;
        initGUI();
        setResizable(false);
        setModal(true);
        setLocationRelativeTo(owner);
    }

    private void initGUI(){
        setTitle(owner.getBundleString("TITLE_DIALOG_MERGE"));
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setLayout(null);
        this.add(getRbMerge());
        this.add(getRbMergeRow());
        rbGroup = new ButtonGroup();
        rbGroup.add(getRbMerge());
        rbGroup.add(getRbMergeRow());
        if(isMatrix){
            this.add(getRbMatrixAddOperation());
            this.add(getRbMatrixMultiplyOperation());
            rbGroup.add(getRbMatrixAddOperation());
            rbGroup.add(getRbMatrixMultiplyOperation());
        }
        int width = Math.max(getRbMerge().getWidth(), getRbMergeRow().getWidth());
        int height = getRbMergeRow().getHeight()+getRbMergeRow().getY();
        if(isMatrix){
            width = Math.max(width, getRbMatrixAddOperation().getWidth());
            width = Math.max(width, getRbMatrixMultiplyOperation().getWidth());
            height = getRbMatrixMultiplyOperation().getHeight()+getRbMatrixMultiplyOperation().getY();
        }
        width = width+getRbMerge().getX()*2;
        width = Math.max(MIN_WIDTH_MERGE_DIALOG, width);
        height += 80;
        setSize(width, height);
        this.add(getButtonOk());
        this.add(getButtonCancel());
        this.rbMerge.setSelected(true);
    }

    private JRadioButton getRbMerge(){
        if(rbMerge == null){
            rbMerge = new JRadioButton();
            rbMerge.setName("rbMerge");
            rbMerge.setFont(new java.awt.Font("Tahoma", 1, 11));
            rbMerge.setText(owner.getBundleString("LABEL_MERGE_DATASET_MERGE"));
            rbMerge.setSize(60+MyUtilities.lenghtOfString(this.rbMerge.getText(), getFontMetrics(this.rbMerge.getFont())), 17);
            rbMerge.setBounds(10, 30, rbMerge.getWidth(), rbMerge.getHeight());
            rbMerge.setSelected(false);
            rbMerge.setActionCommand(DataConstants.actionMerge);
            rbMerge.addActionListener(this);
        }
        return rbMerge;
    }

    private JRadioButton getRbMergeRow(){
        if(rbMergeRow == null){
            rbMergeRow = new JRadioButton();
            rbMergeRow.setName("rbMergeRow");
            rbMergeRow.setFont(new java.awt.Font("Tahoma", 1, 11));
            rbMergeRow.setText(owner.getBundleString("LABEL_MERGE_DATASET_MERGE_ROW"));
            rbMergeRow.setSize(60+MyUtilities.lenghtOfString(this.rbMergeRow.getText(), getFontMetrics(this.rbMergeRow.getFont())), 17);
            rbMergeRow.setBounds(rbMerge.getX(), rbMerge.getY()+rbMerge.getHeight()+10, rbMergeRow.getWidth(), rbMergeRow.getHeight());
            rbMergeRow.setSelected(false);
            rbMergeRow.setActionCommand(DataConstants.actionMergeRow);
            rbMergeRow.addActionListener(this);
        }
        return rbMergeRow;
    }

    private JRadioButton getRbMatrixAddOperation(){
        if(rbMatrixAddOperation == null){
            rbMatrixAddOperation = new JRadioButton();
            rbMatrixAddOperation.setName("rbMatrixAddOperation");
            rbMatrixAddOperation.setFont(new java.awt.Font("Tahoma", 1, 11));
            rbMatrixAddOperation.setText(owner.getBundleString("LABEL_MERGE_DATASET_MATRIX_ADD_OPERATION"));
            rbMatrixAddOperation.setSize(60+MyUtilities.lenghtOfString(this.rbMatrixAddOperation.getText(), getFontMetrics(this.rbMatrixAddOperation.getFont())), 17);
            rbMatrixAddOperation.setBounds(rbMerge.getX(), rbMergeRow.getY()+rbMergeRow.getHeight()+10, rbMatrixAddOperation.getWidth(), rbMatrixAddOperation.getHeight());
            rbMatrixAddOperation.setSelected(false);
            rbMatrixAddOperation.setActionCommand(DataConstants.actionMatrixAddOperation);
            rbMatrixAddOperation.addActionListener(this);
        }
        return rbMatrixAddOperation;
    }

    private JRadioButton getRbMatrixMultiplyOperation(){
        if(rbMatrixMultiplyOperation == null){
            rbMatrixMultiplyOperation = new JRadioButton();
            rbMatrixMultiplyOperation.setName("rbMatrixMultiplyOperation");
            rbMatrixMultiplyOperation.setFont(new java.awt.Font("Tahoma", 1, 11));
            rbMatrixMultiplyOperation.setText(owner.getBundleString("LABEL_MERGE_DATASET_MATRIX_MULTIPLY_OPERATION"));
            rbMatrixMultiplyOperation.setSize(60+MyUtilities.lenghtOfString(this.rbMatrixMultiplyOperation.getText(), getFontMetrics(this.rbMatrixMultiplyOperation.getFont())), 17);
            rbMatrixMultiplyOperation.setBounds(rbMerge.getX(), rbMatrixAddOperation.getY()+rbMatrixAddOperation.getHeight()+10, rbMatrixMultiplyOperation.getWidth(), rbMatrixMultiplyOperation.getHeight());
            rbMatrixMultiplyOperation.setSelected(false);
            rbMatrixMultiplyOperation.setActionCommand(DataConstants.actionMatrixMultiplyOperation);
            rbMatrixMultiplyOperation.addActionListener(this);
        }
        return rbMatrixMultiplyOperation;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e != null && e.getActionCommand() != null){
            String cmd = e.getActionCommand();
            if(cmd.equals(DataConstants.actionMerge)){
                mergeAction = DataConstants.actionMerge;
            }else if (cmd.equals(DataConstants.actionMergeRow)){
                mergeAction = DataConstants.actionMergeRow;
            }else if (cmd.equals(DataConstants.actionMatrixAddOperation)){
                mergeAction = DataConstants.actionMatrixAddOperation;
            }else if (cmd.equals(DataConstants.actionMatrixMultiplyOperation)){
                mergeAction = DataConstants.actionMatrixMultiplyOperation;
            }
        }
    }

    private JButton getButtonOk(){
         if(buttonOk == null){
             buttonOk = new JButton();
             buttonOk.setName("buttonOk");
             buttonOk.setText(owner.getBundleString("BUTTON_OK"));
             buttonOk.setSize(60+MyUtilities.lenghtOfString(this.buttonOk.getText(), getFontMetrics(this.buttonOk.getFont())), 23);
             int y = isMatrix? rbMatrixMultiplyOperation.getY()+rbMatrixMultiplyOperation.getHeight()+20:rbMergeRow.getY()+rbMergeRow.getHeight()+20;
             buttonOk.setBounds(this.getWidth()/4, y, buttonOk.getWidth(), buttonOk.getHeight());
             buttonOk.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    buttonOk();
                }
            });
         }
         return buttonOk;
     }

    private JButton getButtonCancel(){
         if(buttonCancel == null){
             buttonCancel = new JButton();
             buttonCancel.setName("buttonCancel");
             buttonCancel.setText(owner.getBundleString("BUTTON_CANCEL"));
             buttonCancel.setSize(60+MyUtilities.lenghtOfString(this.buttonCancel.getText(), getFontMetrics(this.buttonCancel.getFont())), 23);
             buttonCancel.setBounds(this.getWidth() - this.getWidth()/4 - this.buttonCancel.getWidth(), buttonOk.getY(), buttonCancel.getWidth(), buttonCancel.getHeight());
             buttonCancel.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    buttonCancel();
                }
            });
         }
         return buttonCancel;
     }

    private void buttonOk(){
        boolean isOk = this.owner.mergeDatasets(ds1, ds2,elo, mergeAction);
        this.dispose();
    }

    private void buttonCancel(){
        this.dispose();
    }

}
