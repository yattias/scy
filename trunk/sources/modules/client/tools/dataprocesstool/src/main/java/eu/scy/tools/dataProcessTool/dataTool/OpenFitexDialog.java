/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.dataTool;

import eu.scy.tools.dataProcessTool.common.Dataset;
import eu.scy.tools.dataProcessTool.common.Mission;
import eu.scy.tools.dataProcessTool.utilities.CopexReturn;
import eu.scy.tools.dataProcessTool.utilities.DataConstants;
import eu.scy.tools.dataProcessTool.utilities.MyFileFilter;
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
 * @author Marjolaine
 */
public class OpenFitexDialog extends JDialog {
    private DataProcessToolPanel owner;
    private OpenDataAction actionData;
    private boolean dbMode;
    private File lastUsedFileOpen = null;

    private ArrayList<Mission> listMission;
    private ArrayList<ArrayList<Dataset>> listDatasetMission;

    private JRadioButton rbCreate;
    private JLabel labelCreateName;
    private JTextField fieldName;

    private JRadioButton rbOpen;
    private JLabel labelOpen;
    private JLabel labelMissionOpen;
    private JComboBox cbMissionOpen;
    private JLabel labelDsOpen;
    private JComboBox cbDsOpen;

    private JButton buttonOk;
    private JButton buttonCancel;


    public OpenFitexDialog(DataProcessToolPanel owner, boolean dbMode, File openFile) {
        super();
        this.owner = owner;
        this.dbMode = dbMode;
        this.lastUsedFileOpen = openFile;
        initGUI();
        setResizable(false);
        setModal(true);
        setLocationRelativeTo(owner);
    }

    public OpenFitexDialog(DataProcessToolPanel owner, boolean dbMode,ArrayList<Mission> listMission, ArrayList<ArrayList<Dataset>> listDatasetMission){
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

    private boolean canCreate(){
        return !this.dbMode;
    }


    private void initGUI(){
        setLayout(null);
        if(canCreate())
            setTitle(owner.getBundleString("TITLE_DIALOG_CREATE_DATASET"));
        else
            setTitle(owner.getBundleString("TITLE_DIALOG_OPEN_DATASET"));
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        if(canCreate()){
            this.add(getRbCreate());
            this.add(getLabelCreateName());
            this.add(getFieldCreateName());
        }
        if(canCreate())
            this.add(getRbOpen());
        else
            this.add(getLabelOpen());
        if(dbMode){
            this.add(getLabelMissionOpen());
            this.add(getCbMissionOpen());
            this.add(getLabelDsOpen());
            this.add(getCbDsOpen());
        }

        // largeur
        int width = 400;
        if(canCreate())
            width = fieldName.getX()+fieldName.getWidth()+10;
        int height = 200;
        setSize(width, height);
        this.add(getButtonOk());
        this.add(getButtonCancel());
        height = this.buttonCancel.getY()+this.buttonCancel.getHeight()+40;
        setSize(width, height);
        setPreferredSize(getSize());
        selectRb(1);
        if(dbMode){
            changeMissionOpen();
        }
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

    private JLabel getLabelCreateName(){
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
            int y = 10;
            if(canCreate()){
                y = fieldName.getY()+fieldName.getHeight()+10;
            }
            rbOpen.setBounds(10,y,rbOpen.getWidth(), rbOpen.getHeight());
        }
        return rbOpen;
    }

     private JLabel getLabelOpen(){
        if(this.labelOpen == null){
            labelOpen = new JLabel();
            labelOpen.setName("labelOpen");
            labelOpen.setText(owner.getBundleString("LABEL_OPEN_ELO"));
            labelOpen.setFont(new java.awt.Font("Tahoma", 1, 11));
            labelOpen.setSize(MyUtilities.lenghtOfString(this.labelOpen.getText(), getFontMetrics(this.labelOpen.getFont())), 14);
            int y = 10;
            if(canCreate()){
                y = fieldName.getY()+fieldName.getHeight()+10;
            }
            labelOpen.setBounds(10, y, labelOpen.getWidth(), labelOpen.getHeight());
        }
        return labelOpen;
    }

      
     private JLabel getLabelMissionOpen(){
         if(this.labelMissionOpen == null){
            labelMissionOpen = new JLabel();
            labelMissionOpen.setName("labelMissionOpen");
            labelMissionOpen.setText(owner.getBundleString("LABEL_MISSION"));
            labelMissionOpen.setFont(new java.awt.Font("Tahoma", 1, 11));
            labelMissionOpen.setSize(MyUtilities.lenghtOfString(this.labelMissionOpen.getText(), getFontMetrics(this.labelMissionOpen.getFont())), 14);
            int y = 0;
            if(rbOpen == null)
                y = labelOpen.getY()+labelOpen.getHeight()+5;
            else
                y = rbOpen.getY()+rbOpen.getHeight()+5;
            labelMissionOpen.setBounds(10, y, labelMissionOpen.getWidth(), labelMissionOpen.getHeight());
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
                @Override
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

     private JButton getButtonOk(){
         if(buttonOk == null){
             buttonOk = new JButton();
             buttonOk.setName("buttonOk");
             buttonOk.setText(owner.getBundleString("BUTTON_OK"));
             buttonOk.setSize(60+MyUtilities.lenghtOfString(this.buttonOk.getText(), getFontMetrics(this.buttonOk.getFont())), 23);
             int y = 20;
             if(dbMode){
                 y += labelDsOpen.getY()+labelDsOpen.getHeight();
             }else if(rbOpen != null){
                 y += rbOpen.getY()+rbOpen.getHeight();
             }else{
                 y += labelOpen.getY()+labelOpen.getHeight();
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
        return ((canCreate() && rbCreate == null))  || rbOpen == null;
    }
    private void selectRb(int rb){
        if(rbNull())
            return;
        if(!canCreate() && rb ==1)
            rb =2;
        boolean create = rb==1;
        boolean open = rb==2;
        if(canCreate()){
            this.rbCreate.setSelected(create);
            fieldName.setEditable(create);
        }else if(rb == 1){
            open = true;
        }
        this.rbOpen.setSelected(open);
        if(dbMode){
            cbMissionOpen.setEnabled(open);
            cbDsOpen.setEnabled(open);
            if(listMission.size() > 0){
                cbMissionOpen.setSelectedIndex(0);
            }
        }
    }

    
    private void changeMissionOpen(){
        cbDsOpen.removeAllItems();
        int id = cbMissionOpen.getSelectedIndex();
        if (id != -1){
            ArrayList<Dataset> list = this.listDatasetMission.get(id);
            if (list != null){
                // initialisation liste des missions
                int nb = list.size();
                for (int i=0; i<nb; i++){
                    Dataset ds = list.get(i);
                    cbDsOpen.addItem(ds.getName());
                }
                if (nb > 0)
                    cbDsOpen.setSelectedIndex(0);
            }
        }
        repaint();
    }
    private void buttonExplOpen(){
        JFileChooser aFileChooser = new JFileChooser();
        aFileChooser.setFileFilter(new MyFileFilter());
        if (lastUsedFileOpen != null){
            aFileChooser.setCurrentDirectory(lastUsedFileOpen.getParentFile());
            aFileChooser.setSelectedFile(lastUsedFileOpen);
        }
        int userResponse = aFileChooser.showOpenDialog(this);
        if (userResponse == JFileChooser.APPROVE_OPTION){
            File file = aFileChooser.getSelectedFile();
            if(!MyUtilities.isXMLFile(file) && !MyUtilities.isCSVFile(file) && !MyUtilities.isGMBLFile(file)){
                owner.displayError(new CopexReturn(owner.getBundleString("MSG_ERROR_FILE"), false), owner.getBundleString("TITLE_DIALOG_ERROR"));
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
    private void buttonOk(){
        boolean create = false;
        if(canCreate())
                create = this.rbCreate.isSelected();
        if(create){
            String dsName = this.fieldName.getText();
            if (controlLenght() &&  dsName.length() > DataConstants.MAX_LENGHT_DATASET_NAME){
                String msg = owner.getBundleString("MSG_LENGHT_MAX");
                msg  = MyUtilities.replace(msg, 0, owner.getBundleString("LABEL_NEW_NAME_DATASET"));
                msg = MyUtilities.replace(msg, 1, ""+DataConstants.MAX_LENGHT_DATASET_NAME);
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
        boolean open = this.rbOpen == null || this.rbOpen.isSelected();
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
    }

    private void buttonCancel(){
        this.dispose();
    }

    private boolean controlLenght(){
        return owner.controlLenght();
    }
   
}
