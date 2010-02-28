/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.dataTool;

import eu.scy.tools.dataProcessTool.common.Dataset;
import eu.scy.tools.dataProcessTool.common.Mission;
import eu.scy.tools.dataProcessTool.utilities.CopexReturn;
import eu.scy.tools.dataProcessTool.utilities.MyConstants;
import eu.scy.tools.dataProcessTool.utilities.MyFileFilterCSV;
import eu.scy.tools.dataProcessTool.utilities.MyFileFilterXML;
import eu.scy.tools.dataProcessTool.utilities.MyUtilities;
import java.awt.event.ItemEvent;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/**
 * open data dialog, 4 options :
 * - new ELO
 * - open an ELO ds/pds
 * - import .csv
 * - merge a ds to the current ds
 * @author Marjolaine
 */
public class OpenFitexDialog extends JDialog {
    private DataProcessToolPanel owner;
    private OpenDataAction actionData;
    private boolean dbMode;
    private File lastUsedFileOpen = null;
    private File lastUsedFileImport = null;
    private File lastUsedFileMerge = null;

    private ArrayList<Mission> listMission;
    private ArrayList<ArrayList<Dataset>> listDatasetMission;

    private JRadioButton rbCreate;
    private JLabel labelCreateName;
    private JTextField fieldName;

    private JRadioButton rbOpen;
    private JLabel labelMissionOpen;
    private JComboBox cbMissionOpen;
    private JLabel labelDsOpen;
    private JComboBox cbDsOpen;

    private JRadioButton rbImport;

    private JRadioButton rbMerge;
    private JLabel labelMissionMerge;
    private JComboBox cbMissionMerge;
    private JLabel labelDsMerge;
    private JComboBox cbDsMerge;

    private JButton buttonOk;
    private JButton buttonCancel;


    public OpenFitexDialog(DataProcessToolPanel owner, boolean dbMode, File openFile, File importFile, File mergeFile) {
        super();
        this.owner = owner;
        this.dbMode = dbMode;
        this.lastUsedFileOpen = openFile;
        this.lastUsedFileImport = importFile;
        this.lastUsedFileMerge = mergeFile;
        initGUI();
        setResizable(false);
        setModal(true);
        setLocationRelativeTo(owner);
    }

    private void initGUI(){
        setLayout(null);
        setTitle(owner.getBundleString("TITLE_DIALOG_CREATE_DATASET"));
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        this.add(getRbCreate());
        this.add(getLabeCreateName());
        this.add(getFieldCreateName());

        this.add(getRbOpen());
        if(dbMode){
            this.add(getLabelMissionOpen());
            this.add(getCbMissionOpen());
            this.add(getLabelDsOpen());
            this.add(getCbDsOpen());
        }

        this.add(getRbImport());

        this.add(getRbMerge());
        if(dbMode){
            this.add(getLabelMissionMerge());
            this.add(getCbMissionMerge());
            this.add(getLabelDsMerge());
            this.add(getCbDsMerge());
        }
        // largeur
        int width = fieldName.getX()+fieldName.getWidth()+10;
        int height = 200;
        setSize(width, height);
        this.add(getButtonOk());
        this.add(getButtonCancel());
        height = this.buttonCancel.getY()+this.buttonCancel.getHeight()+40;
        setSize(width, height);
        setPreferredSize(getSize());
        selectRb(1);
    }

    public void addOpenFitexAction(OpenDataAction action){
        this.actionData = action;
    }
    private JRadioButton getRbCreate(){
        if(rbCreate == null){
            rbCreate = new JRadioButton();
            rbCreate.setName("rbCreate");
            rbCreate.setText(owner.getBundleString("LABEL_NEW_ELO"));
            rbCreate.setSelected(true);
            rbCreate.setFont(new java.awt.Font("Tahoma", 1, 11));
            rbCreate.setSize(60+MyUtilities.lenghtOfString(this.rbCreate.getText(), getFontMetrics(this.rbCreate.getFont())), 23);
            rbCreate.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    selectRb(1);
                }
            });
            rbCreate.setBounds(10,10,rbCreate.getWidth(), rbCreate.getHeight());
        }
        return rbCreate;
    }

    private JLabel getLabeCreateName(){
        if(this.labelCreateName == null){
            labelCreateName = new JLabel();
            labelCreateName.setName("labelCreateName");
            labelCreateName.setText(owner.getBundleString("LABEL_NEW_NAME_DATASET"));
            labelCreateName.setFont(new java.awt.Font("Tahoma", 1, 11));
            labelCreateName.setSize(MyUtilities.lenghtOfString(this.labelCreateName.getText(), getFontMetrics(this.labelCreateName.getFont())), 14);
            labelCreateName.setBounds(10, rbCreate.getY()+rbCreate.getHeight()+5, labelCreateName.getWidth(), labelCreateName.getHeight());
        }
        return labelCreateName;
    }

    private JTextField getFieldCreateName(){
        if(fieldName == null){
            fieldName = new JTextField();
            fieldName.setName("fieldName");
            fieldName.setBounds(labelCreateName.getX()+labelCreateName.getWidth()+5, labelCreateName.getY()-3, 230,(int)fieldName.getPreferredSize().getHeight());
        }
        return fieldName;
    }

     private JRadioButton getRbOpen(){
        if(rbOpen == null){
            rbOpen = new JRadioButton();
            rbOpen.setName("rbOpen");
            rbOpen.setText(owner.getBundleString("LABEL_OPEN_ELO"));
            rbOpen.setSelected(false);
            rbOpen.setFont(new java.awt.Font("Tahoma", 1, 11));
            rbOpen.setSize(60+MyUtilities.lenghtOfString(this.rbOpen.getText(), getFontMetrics(this.rbOpen.getFont())), 20);
            rbOpen.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    selectRb(2);
                }
            });
            rbOpen.setBounds(10,fieldName.getY()+fieldName.getHeight()+10,rbOpen.getWidth(), rbOpen.getHeight());
        }
        return rbOpen;
    }

      
     private JLabel getLabelMissionOpen(){
         if(this.labelMissionOpen == null){
            labelMissionOpen = new JLabel();
            labelMissionOpen.setName("labelMissionOpen");
            labelMissionOpen.setText(owner.getBundleString("LABEL_MISSION"));
            labelMissionOpen.setFont(new java.awt.Font("Tahoma", 1, 11));
            labelMissionOpen.setSize(MyUtilities.lenghtOfString(this.labelMissionOpen.getText(), getFontMetrics(this.labelMissionOpen.getFont())), 14);
            labelMissionOpen.setBounds(10, rbOpen.getY()+rbOpen.getHeight()+5, labelMissionOpen.getWidth(), labelMissionOpen.getHeight());
        }
        return labelMissionOpen;
     }
     private JComboBox getCbMissionOpen(){
         if(this.cbMissionOpen == null){
             cbMissionOpen = new JComboBox();
             cbMissionOpen.setName("cbMissionOpen");
             int n = listMission.size();
             for (int i=0; i<n; i++){
                 cbMissionOpen.addItem(listMission.get(i).getCode());
             }
             cbMissionOpen.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    changeMissionOpen();
                }
             });
             cbMissionOpen.setBounds(labelMissionOpen.getX()+labelMissionOpen.getWidth()+5, labelMissionOpen.getY()-3, 100,20);
        }
        return cbMissionOpen;
     }

     private JLabel getLabelDsOpen(){
         if(this.labelDsOpen == null){
            labelDsOpen = new JLabel();
            labelDsOpen.setName("labelDsOpen");
            labelDsOpen.setText(owner.getBundleString("LABEL_DATASET"));
            labelDsOpen.setFont(new java.awt.Font("Tahoma", 1, 11));
            labelDsOpen.setSize(MyUtilities.lenghtOfString(this.labelDsOpen.getText(), getFontMetrics(this.labelDsOpen.getFont())), 14);
            labelDsOpen.setBounds(10, labelMissionOpen.getY()+labelMissionOpen.getHeight()+5, labelDsOpen.getWidth(), labelDsOpen.getHeight());
        }
        return labelDsOpen;
     }
     private JComboBox getCbDsOpen(){
         if(this.cbDsOpen == null){
             cbDsOpen = new JComboBox();
             cbDsOpen.setName("cbDsOpen");
             cbDsOpen.setBounds(labelDsOpen.getX()+labelDsOpen.getWidth()+5, labelDsOpen.getY()-3, 100,20);
        }
        return cbDsOpen;
     }


     private JRadioButton getRbImport(){
        if(rbImport == null){
            rbImport = new JRadioButton();
            rbImport.setName("rbImport");
            rbImport.setText(owner.getBundleString("LABEL_IMPORT_ELO"));
            rbImport.setSelected(false);
            rbImport.setFont(new java.awt.Font("Tahoma", 1, 11));
            rbImport.setSize(60+MyUtilities.lenghtOfString(this.rbImport.getText(), getFontMetrics(this.rbImport.getFont())), 23);
            rbImport.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    selectRb(3);
                }
            });
            int y = 0;
            if(dbMode){
                y = cbDsOpen.getY()+cbDsOpen.getHeight()+10;
            }else{
                y = rbOpen.getY()+rbOpen.getHeight()+10;
            }
            rbImport.setBounds(10,y,rbImport.getWidth(), rbImport.getHeight());
        }
        return rbImport;
    }

     

     private JRadioButton getRbMerge(){
        if(rbMerge == null){
            rbMerge = new JRadioButton();
            rbMerge.setName("rbMerge");
            rbMerge.setText(owner.getBundleString("LABEL_MERGE_ELO"));
            rbMerge.setSelected(false);
            rbMerge.setFont(new java.awt.Font("Tahoma", 1, 11));
            rbMerge.setSize(60+MyUtilities.lenghtOfString(this.rbMerge.getText(), getFontMetrics(this.rbMerge.getFont())), 23);
            rbMerge.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    selectRb(4);
                }
            });
            rbMerge.setBounds(10,rbImport.getY()+rbImport.getHeight()+10,rbMerge.getWidth(), rbMerge.getHeight());
        }
        return rbMerge;
    }

    

     private JLabel getLabelMissionMerge(){
         if(this.labelMissionMerge == null){
            labelMissionMerge = new JLabel();
            labelMissionMerge.setName("labelMissionMerge");
            labelMissionMerge.setText(owner.getBundleString("LABEL_MISSION"));
            labelMissionMerge.setFont(new java.awt.Font("Tahoma", 1, 11));
            labelMissionMerge.setSize(MyUtilities.lenghtOfString(this.labelMissionMerge.getText(), getFontMetrics(this.labelMissionMerge.getFont())), 14);
            labelMissionMerge.setBounds(10, rbMerge.getY()+rbMerge.getHeight()+5, labelMissionMerge.getWidth(), labelMissionMerge.getHeight());
        }
        return labelMissionMerge;
     }
     private JComboBox getCbMissionMerge(){
         if(this.cbMissionMerge == null){
             cbMissionMerge = new JComboBox();
             cbMissionMerge.setName("cbMissionMerge");
             int n = listMission.size();
             for (int i=0; i<n; i++){
                 cbMissionMerge.addItem(listMission.get(i).getCode());
             }
             cbMissionMerge.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    changeMissionMerge();
                }
             });
             cbMissionMerge.setBounds(labelMissionMerge.getX()+labelMissionMerge.getWidth()+5, labelMissionMerge.getY()-3, 100,20);
        }
        return cbMissionMerge;
     }

     private JLabel getLabelDsMerge(){
         if(this.labelDsMerge == null){
            labelDsMerge = new JLabel();
            labelDsMerge.setName("labelDsMerge");
            labelDsMerge.setText(owner.getBundleString("LABEL_DATASET"));
            labelDsMerge.setFont(new java.awt.Font("Tahoma", 1, 11));
            labelDsMerge.setSize(MyUtilities.lenghtOfString(this.labelDsMerge.getText(), getFontMetrics(this.labelDsMerge.getFont())), 14);
            labelDsMerge.setBounds(10, labelMissionMerge.getY()+labelMissionMerge.getHeight()+5, labelDsMerge.getWidth(), labelDsMerge.getHeight());
        }
        return labelDsMerge;
     }
     private JComboBox getCbDsMerge(){
         if(this.cbDsMerge == null){
             cbDsMerge = new JComboBox();
             cbDsMerge.setName("cbDsMerge");
             cbDsMerge.setBounds(labelDsMerge.getX()+labelDsMerge.getWidth()+5, labelDsMerge.getY()-3, 100,20);
        }
        return cbDsMerge;
     }


     private JButton getButtonOk(){
         if(buttonOk == null){
             buttonOk = new JButton();
             buttonOk.setName("buttonOk");
             buttonOk.setText(owner.getBundleString("BUTTON_OK"));
             buttonOk.setSize(60+MyUtilities.lenghtOfString(this.buttonOk.getText(), getFontMetrics(this.buttonOk.getFont())), 23);
             int y = 20;
             if(dbMode){
                 y += labelDsMerge.getY()+labelDsMerge.getHeight();
             }else{
                 y += rbMerge.getY()+rbMerge.getHeight();
             }
             buttonOk.setBounds(this.getWidth()/4, y, buttonOk.getWidth(), buttonOk.getHeight());
             buttonOk.addActionListener(new java.awt.event.ActionListener() {
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
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    buttonCancel();
                }
            });
         }
         return buttonCancel;
     }

    private boolean rbNull(){
        return rbCreate == null || rbImport == null || rbMerge == null || rbOpen == null;
    }
    private void selectRb(int rb){
        if(rbNull())
            return;
        boolean create = rb==1;
        boolean open = rb==2;
        boolean importCsv = rb==3;
        boolean merge = rb==4;
        this.rbCreate.setSelected(create);
        fieldName.setEditable(create);
        this.rbOpen.setSelected(open);
        if(dbMode){
            cbMissionOpen.setEnabled(open);
            cbDsOpen.setEnabled(open);
        }
        this.rbImport.setSelected(importCsv);
        this.rbMerge.setSelected(merge);
        if(dbMode){
            cbMissionMerge.setEnabled(merge);
            cbDsMerge.setEnabled(merge);
        }
    }

    
    private void changeMissionOpen(){
        
    }
    private void changeMissionMerge(){

    }
    private void buttonExplOpen(){
        JFileChooser aFileChooser = new JFileChooser();
        aFileChooser.setFileFilter(new MyFileFilterXML());
        if (lastUsedFileOpen != null){
            aFileChooser.setCurrentDirectory(lastUsedFileOpen.getParentFile());
            aFileChooser.setSelectedFile(lastUsedFileOpen);
        }
        int userResponse = aFileChooser.showOpenDialog(this);
        if (userResponse == JFileChooser.APPROVE_OPTION){
            File file = aFileChooser.getSelectedFile();
            if(!MyUtilities.isXMLFile(file)){
                owner.displayError(new CopexReturn(owner.getBundleString("MSG_ERROR_FILE_XML"), false), owner.getBundleString("TITLE_DIALOG_ERROR"));
                return;
            }
            lastUsedFileOpen = file;
            if(lastUsedFileOpen == null){
                owner.displayError(new CopexReturn(owner.getBundleString("MSG_ERROR_OPEN_DATASET") ,false), owner.getBundleString("TITLE_DIALOG_ERROR"));
                return;
            }
            actionData.openELO(lastUsedFileOpen);
            this.dispose();
            return;
        }
    }
    private void buttonExplImport(){
        JFileChooser aFileChooser = new JFileChooser();
        aFileChooser.setFileFilter(new MyFileFilterCSV());
        if (lastUsedFileImport != null){
            aFileChooser.setCurrentDirectory(lastUsedFileImport.getParentFile());
            aFileChooser.setSelectedFile(lastUsedFileImport);
        }
        int userResponse = aFileChooser.showOpenDialog(this);
        if (userResponse == JFileChooser.APPROVE_OPTION){
            File file = aFileChooser.getSelectedFile();
            if(!MyUtilities.isCSVFile(file)){
                owner.displayError(new CopexReturn(owner.getBundleString("MSG_ERROR_FILE_CSV"), false), owner.getBundleString("TITLE_DIALOG_ERROR"));
                return;
            }
            lastUsedFileImport = file;
            if(lastUsedFileImport == null){
                owner.displayError(new CopexReturn(owner.getBundleString("MSG_ERROR_IMPORT_DATASET") ,false), owner.getBundleString("TITLE_DIALOG_ERROR"));
                return;
            }
            actionData.importELO(lastUsedFileImport);
            this.dispose();
            return;
        }
    }
    private void buttonExplMerge(){
        JFileChooser aFileChooser = new JFileChooser();
        aFileChooser.setFileFilter(new MyFileFilterXML());
        if (lastUsedFileMerge != null){
            aFileChooser.setCurrentDirectory(lastUsedFileMerge.getParentFile());
            aFileChooser.setSelectedFile(lastUsedFileMerge);
        }
        int userResponse = aFileChooser.showOpenDialog(this);
        if (userResponse == JFileChooser.APPROVE_OPTION){
            File file = aFileChooser.getSelectedFile();
            if(!MyUtilities.isXMLFile(file)){
                owner.displayError(new CopexReturn(owner.getBundleString("MSG_ERROR_FILE_XML"), false), owner.getBundleString("TITLE_DIALOG_ERROR"));
                return;
            }
            lastUsedFileMerge = file;
            if(lastUsedFileMerge == null){
                owner.displayError(new CopexReturn(owner.getBundleString("MSG_ERROR_MERGE_DATASET") ,false), owner.getBundleString("TITLE_DIALOG_ERROR"));
                return;
            }
            actionData.mergeELO(lastUsedFileMerge);
            this.dispose();
            return;
        }
    }

    private void buttonOk(){
        boolean create = this.rbCreate.isSelected();
        if(create){
            String dsName = this.fieldName.getText();
            if (dsName.length() > MyConstants.MAX_LENGHT_DATASET_NAME){
                String msg = owner.getBundleString("MSG_LENGHT_MAX");
                msg  = MyUtilities.replace(msg, 0, owner.getBundleString("LABEL_NEW_NAME_DATASET"));
                msg = MyUtilities.replace(msg, 1, ""+MyConstants.MAX_LENGHT_DATASET_NAME);
                owner.displayError(new CopexReturn(msg, false), owner.getBundleString("TITLE_DIALOG_ERROR"));
                return;
            }
            if (dsName.length() == 0){
                String msg = owner.getBundleString("MSG_ERROR_FIELD_NULL");
                msg  = MyUtilities.replace(msg, 0, owner.getBundleString("LABEL_NEW_NAME_DATASET"));
                owner.displayError(new CopexReturn(msg ,false), owner.getBundleString("TITLE_DIALOG_ERROR"));
                return;
            }
            actionData.newElo(dsName);
            this.dispose();
            return;
        }
        boolean open = this.rbOpen.isSelected();
        if(open){
            if(dbMode){
                int idM = cbMissionOpen.getSelectedIndex() ;
                Mission m = listMission.get(idM);
                Dataset ds = listDatasetMission.get(idM).get(cbDsOpen.getSelectedIndex());
                actionData.openDataset(m,ds);
                this.dispose();
                return;
            }else{
                buttonExplOpen();
                return;
            }
        }
        boolean importCsv = this.rbImport.isSelected();
        if(importCsv){
            buttonExplImport();
            return;
        }
        boolean merge = this.rbMerge.isSelected();
        if(merge){
            if(dbMode){
                int idM = cbMissionMerge.getSelectedIndex() ;
                Mission m = listMission.get(idM);
                Dataset ds = listDatasetMission.get(idM).get(cbDsMerge.getSelectedIndex());
                actionData.mergeDataset(m,ds);
                this.dispose();
                return;
            }else{
                buttonExplMerge();
                return;
            }
        }

    }

    private void buttonCancel(){
        this.dispose();
    }
   
}
