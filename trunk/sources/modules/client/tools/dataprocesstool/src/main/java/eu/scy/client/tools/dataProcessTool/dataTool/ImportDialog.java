/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.dataProcessTool.dataTool;

import eu.scy.client.tools.fitex.main.DataProcessToolPanel;
import eu.scy.client.tools.dataProcessTool.common.Dataset;
import eu.scy.client.tools.dataProcessTool.common.Mission;
import eu.scy.client.tools.dataProcessTool.utilities.CopexReturn;
import eu.scy.client.tools.dataProcessTool.utilities.MyFileFilterCSV_GMBL;
import eu.scy.client.tools.dataProcessTool.utilities.MyFileFilterXML;
import eu.scy.client.tools.dataProcessTool.utilities.MyUtilities;
import java.awt.event.ItemEvent;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JRadioButton;

/**
 * import data dialog :
 * - import data from a csv file
 * - merge a dataset with the current dataset
 * @author Marjolaine
 */
public class ImportDialog extends JDialog{

    private DataProcessToolPanel owner;
    private OpenDataAction actionData;
    private boolean dbMode;
    private File lastUsedFileImport = null;
    private File lastUsedFileMerge = null;

    private ArrayList<Mission> listMission;
    private ArrayList<ArrayList<Dataset>> listDatasetMission;


    private JRadioButton rbImport;

    private JRadioButton rbMerge;
    private JLabel labelMissionMerge;
    private JComboBox cbMissionMerge;
    private JLabel labelDsMerge;
    private JComboBox cbDsMerge;

    private JButton buttonOk;
    private JButton buttonCancel;

    public ImportDialog(DataProcessToolPanel owner, boolean dbMode, File importFile, File mergeFile) {
        super();
        this.owner = owner;
        this.dbMode = dbMode;
        this.lastUsedFileImport = importFile;
        this.lastUsedFileMerge = mergeFile;
        initGUI();
        setResizable(false);
        setModal(true);
        setLocationRelativeTo(owner);
    }

    public ImportDialog(DataProcessToolPanel owner, boolean dbMode,ArrayList<Mission> listMission, ArrayList<ArrayList<Dataset>> listDatasetMission){
        super();
        this.owner = owner;
        this.dbMode = dbMode;
        this.listMission = listMission;
        this.listDatasetMission = listDatasetMission;
        initGUI();
        setResizable(false);
        setModal(true);
        setLocationRelativeTo(owner);
    }

    private void initGUI(){
        setLayout(null);
        setTitle(owner.getBundleString("TITLE_DIALOG_IMPORT"));
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        
        this.add(getRbImport());

        this.add(getRbMerge());
        if(dbMode){
            this.add(getLabelMissionMerge());
            this.add(getCbMissionMerge());
            this.add(getLabelDsMerge());
            this.add(getCbDsMerge());
        }
        // width
        int width = 400;
        int height = 200;
        setSize(width, height);
        this.add(getButtonOk());
        this.add(getButtonCancel());
        height = this.buttonCancel.getY()+this.buttonCancel.getHeight()+40;
        setSize(width, height);
        setPreferredSize(getSize());
        selectRb(1);
        if(dbMode){
            changeMissionMerge();
        }
    }

    public void addOpenFitexAction(OpenDataAction action){
        this.actionData = action;
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
                    selectRb(1);
                }
            });
            rbImport.setBounds(10,10,rbImport.getWidth(), rbImport.getHeight());
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
                    selectRb(2);
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
                @Override
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

     private boolean rbNull(){
        return (rbImport == null || rbMerge == null );
    }

    private void selectRb(int rb){
        if(rbNull())
            return;
        boolean importCsv = rb==1;
        boolean merge = rb==2;
        this.rbImport.setSelected(importCsv);
        this.rbMerge.setSelected(merge);
        if(dbMode){
            cbMissionMerge.setEnabled(merge);
            cbDsMerge.setEnabled(merge);
            if(listMission.size() > 0)
                cbMissionMerge.setSelectedIndex(0);
        }
    }

    private void changeMissionMerge(){
        cbDsMerge.removeAllItems();
        int id = cbMissionMerge.getSelectedIndex();
        if (id != -1){
            ArrayList<Dataset> list = this.listDatasetMission.get(id);
            if (list != null){
                // initialisation liste des missions
                int nb = list.size();
                for (int i=0; i<nb; i++){
                    Dataset ds = list.get(i);
                    cbDsMerge.addItem(ds.getName());
                }
                if (list.size() > 0)
                    cbDsMerge.setSelectedIndex(0);
            }
        }
        repaint();
    }

    private void buttonExplImport(){
        JFileChooser aFileChooser = new JFileChooser();
        aFileChooser.setFileFilter(new MyFileFilterCSV_GMBL());
        if (lastUsedFileImport != null){
            aFileChooser.setCurrentDirectory(lastUsedFileImport.getParentFile());
            aFileChooser.setSelectedFile(lastUsedFileImport);
        }
        int userResponse = aFileChooser.showOpenDialog(this);
        if (userResponse == JFileChooser.APPROVE_OPTION){
            File file = aFileChooser.getSelectedFile();
            //if(!MyUtilities.isCSVFile(file)){
            if( !MyUtilities.isCSVFile(file) && !MyUtilities.isGMBLFile(file)){
                owner.displayError(new CopexReturn(owner.getBundleString("MSG_ERROR_FILE"), false), owner.getBundleString("TITLE_DIALOG_ERROR"));
                return;
            }
            lastUsedFileImport = file;
            if(lastUsedFileImport == null){
                owner.displayError(new CopexReturn(owner.getBundleString("MSG_ERROR_IMPORT_DATASET") ,false), owner.getBundleString("TITLE_DIALOG_ERROR"));
                return;
            }
            actionData.importELO(lastUsedFileImport, false);
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
