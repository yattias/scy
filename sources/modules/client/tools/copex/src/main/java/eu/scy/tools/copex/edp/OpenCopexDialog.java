/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.edp;

import eu.scy.tools.copex.common.CopexMission;
import eu.scy.tools.copex.common.InitialProcedure;
import eu.scy.tools.copex.common.LearnerProcedure;
import eu.scy.tools.copex.controller.ControllerInterface;
import eu.scy.tools.copex.utilities.CopexReturn;
import eu.scy.tools.copex.utilities.CopexUtilities;
import eu.scy.tools.copex.utilities.MyConstants;
import eu.scy.tools.copex.utilities.MyFileFilterXML;
import java.awt.Cursor;
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
 *
 * @author Marjolaine
 */
public class OpenCopexDialog  extends JDialog  {
    private CopexPanel owner;
    private boolean dbMode;
    private File lastUsedFileOpen = null;
    private File lastUsedFileCopy = null;

    /* controller */
    private ControllerInterface controller;
    /* liste des proc initiaux*/
    private ArrayList<InitialProcedure> listInitialProc;
    /* liste des protocoles de la mission en cours */
    private ArrayList<LearnerProcedure> listProcMission;
    /* liste des missions */
    private ArrayList<CopexMission> listMission;
    /* liste des protocoles de toutes les missions */
    private ArrayList<ArrayList<LearnerProcedure>> listAllProc;
    private boolean onlyOneInitProc;

    private boolean setDefaultProcName = false;

    private JRadioButton rbCreate;
    private JComboBox cbCreateProcInit;
    private JLabel labelCreateName;
    private JTextField fieldName;

    private JRadioButton rbCopy;
    private JComboBox cbProcCopy;
    private JLabel labelFileCopy;
    private JButton buttonExplCopy;
    private JLabel labelCreateNameCopy;
    private JTextField fieldNameCopy;

    private JRadioButton rbOpen;
    private JLabel labelMissionOpen;
    private JComboBox cbMissionOpen;
    private JLabel labelProcOpen;
    private JComboBox cbProcOpen;
    private JLabel labelFileOpen;
    private JButton buttonExplOpen;


    private JButton buttonOk;
    private JButton buttonCancel;

    public OpenCopexDialog(CopexPanel owner, ControllerInterface controller,  File openFile, File copyFile,ArrayList<InitialProcedure> listInitialProc) {
        super();
        this.owner = owner;
        this.controller = controller;
        this.dbMode = false;
        this.lastUsedFileOpen = openFile;
        this.lastUsedFileCopy = copyFile;
        int nb = listInitialProc.size();
        onlyOneInitProc = (nb == 1);
        this.listInitialProc = listInitialProc ;
        initGUI();
        setResizable(false);
        setModal(true);
        setLocationRelativeTo(owner);
    }

    public OpenCopexDialog(CopexPanel owner,ControllerInterface controller, ArrayList<LearnerProcedure> listProcMission, ArrayList<CopexMission> listMission, ArrayList<ArrayList<LearnerProcedure>> listAllProc, ArrayList<InitialProcedure> listInitialProc) {
        super();
        this.owner = owner;
        this.controller = controller;
        this.dbMode = true;
        this.lastUsedFileOpen = null;
        this.lastUsedFileCopy = null;
        int nb = listInitialProc.size();
        onlyOneInitProc = (nb == 1);
        this.listProcMission = listProcMission;
        this.listMission = listMission;
        this.listAllProc = listAllProc;
        this.listInitialProc = listInitialProc ;
        initGUI();
        setResizable(false);
        setModal(true);
        setLocationRelativeTo(owner);
    }

     private void initGUI(){
        setLayout(null);
        setTitle(owner.getBundleString("TITLE_DIALOG_ADD_PROC"));
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        this.add(getRbCreate());
        if(!onlyOneInitProc){
            this.add(getCbCreateProcInit());
        }
        this.add(getLabelCreateName());
        this.add(getFieldCreateName());

        this.add(getRbCopy());
        if(dbMode){
            this.add(getCbProcCopy());
        }else{
            this.add(getButtonExplCopy());
            this.add(getLabelFileCopy());
        }
        this.add(getLabelCreateNameCopy());
        this.add(getFieldNameCopy());

        this.add(getRbOpen());
        if(dbMode){
            this.add(getLabelMissionOpen());
            this.add(getCbMissionOpen());
            this.add(getLabelProcOpen());
            this.add(getCbProcOpen());
        }else{
            this.add(getButtonExplOpen());
            this.add(getLabelFileOpen());
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

     private JRadioButton getRbCreate(){
        if(rbCreate == null){
            rbCreate = new JRadioButton();
            rbCreate.setName("rbCreate");
            rbCreate.setText(owner.getBundleString("LABEL_ADD_NEW_PROC_INIT"));
            if (onlyOneInitProc){
                rbCreate.setText(owner.getBundleString("LABEL_ADD_NEW_PROC"));
            }
            rbCreate.setSelected(true);
            rbCreate.setFont(new java.awt.Font("Tahoma", 1, 11));
            rbCreate.setSize(60+CopexUtilities.lenghtOfString(this.rbCreate.getText(), getFontMetrics(this.rbCreate.getFont())), 23);
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

     private JComboBox getCbCreateProcInit(){
         if(this.cbCreateProcInit == null){
             cbCreateProcInit = new JComboBox();
             cbCreateProcInit.setName("cbCreateProcInit");
             int nb = this.listInitialProc.size();
             for (int i=0; i<nb; i++){
                InitialProcedure initProc = listInitialProc.get(i);
                cbCreateProcInit.addItem(initProc.getCode());
             }
             cbCreateProcInit.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    changeCreateProcInit();
                }
             });
             if (nb > 0){
                cbCreateProcInit.setSelectedIndex(0);
                if(setDefaultProcName)
                    fieldName.setText(listInitialProc.get(0).getName());
            }
             cbCreateProcInit.setBounds(rbCreate.getX()+rbCreate.getWidth()+5, rbCreate.getY(), 100,20);
         }
         return cbCreateProcInit;
     }

    private JLabel getLabelCreateName(){
        if(this.labelCreateName == null){
            labelCreateName = new JLabel();
            labelCreateName.setName("labelCreateName");
            labelCreateName.setText(owner.getBundleString("LABEL_PROC_NAME"));
            labelCreateName.setFont(new java.awt.Font("Tahoma", 1, 11));
            labelCreateName.setSize(CopexUtilities.lenghtOfString(this.labelCreateName.getText(), getFontMetrics(this.labelCreateName.getFont())), 14);
            labelCreateName.setBounds(10, rbCreate.getY()+rbCreate.getHeight()+5, labelCreateName.getWidth(), labelCreateName.getHeight());
        }
        return labelCreateName;
    }

    private JTextField getFieldCreateName(){
        if(fieldName == null){
            fieldName = new JTextField();
            fieldName.setName("fieldName");
            fieldName.setBounds(labelCreateName.getX()+labelCreateName.getWidth()+5, labelCreateName.getY()-3, 230,20);
        }
        return fieldName;
    }

    private JRadioButton getRbCopy(){
        if(rbCopy == null){
            rbCopy = new JRadioButton();
            rbCopy.setName("rbCopy");
            rbCopy.setText(owner.getBundleString("LABEL_ADD_NEW_PROC_COPY"));
            rbCopy.setSelected(true);
            rbCopy.setFont(new java.awt.Font("Tahoma", 1, 11));
            rbCopy.setSize(60+CopexUtilities.lenghtOfString(this.rbCopy.getText(), getFontMetrics(this.rbCopy.getFont())), 23);
            rbCopy.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    selectRb(2);
                }
            });
            rbCopy.setBounds(10,fieldName.getY()+fieldName.getHeight()+10,rbCopy.getWidth(), rbCopy.getHeight());
        }
        return rbCopy;
    }
    private JComboBox getCbProcCopy(){
        if(this.cbProcCopy == null){
             cbProcCopy = new JComboBox();
             cbProcCopy.setName("cbProcCopy");
             int n = listProcMission.size();
             for (int i=0; i<n; i++){
                 cbProcCopy.addItem(listProcMission.get(i).getName());
             }
             cbProcCopy.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    changeProcCopy();
                }
             });
             if (listProcMission.size() > 0)
                cbProcCopy.setSelectedIndex(0);
             else
                rbCopy.setEnabled(false);
             cbProcCopy.setBounds(rbCopy.getX()+rbCopy.getWidth()+5, rbCopy.getY(), 100,20);
        }
        return cbProcCopy;
    }
    private JButton getButtonExplCopy(){
        if(buttonExplCopy == null){
             buttonExplCopy = new JButton();
             buttonExplCopy.setName("buttonExplCopy");
             buttonExplCopy.setIcon(owner.getCopexImage("Button-Expl.png"));
             buttonExplCopy.setBounds(10, rbCopy.getY()+rbCopy.getHeight()+5, 22,22);
             buttonExplCopy.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    buttonExplCopy();
                }
            });
         }
         return buttonExplCopy;
    }
    private JLabel getLabelFileCopy(){
        if(labelFileCopy == null){
             labelFileCopy = new JLabel();
             labelFileCopy.setName("labelFileCopy");
             if(lastUsedFileCopy != null)
                 labelFileCopy.setText(lastUsedFileCopy.getPath());
             labelFileCopy.setFont(new java.awt.Font("Tahoma", 2, 11));
             labelFileCopy.setBounds(buttonExplCopy.getX()+buttonExplCopy.getWidth()+5,buttonExplCopy.getY()+4, 280,14);
         }
         return labelFileCopy;
    }

    private JLabel getLabelCreateNameCopy(){
        if(this.labelCreateNameCopy == null){
            labelCreateNameCopy = new JLabel();
            labelCreateNameCopy.setName("labelCreateName");
            labelCreateNameCopy.setText(owner.getBundleString("LABEL_PROC_NAME"));
            labelCreateNameCopy.setFont(new java.awt.Font("Tahoma", 1, 11));
            labelCreateNameCopy.setSize(CopexUtilities.lenghtOfString(this.labelCreateNameCopy.getText(), getFontMetrics(this.labelCreateNameCopy.getFont())), 14);
            int y = rbCopy.getY()+rbCopy.getHeight()+5;
            if(!dbMode)
                y = labelFileCopy.getY()+labelFileCopy.getHeight()+5;
            labelCreateNameCopy.setBounds(10,y , labelCreateNameCopy.getWidth(), labelCreateNameCopy.getHeight());
        }
        return labelCreateNameCopy;
    }

    private JTextField getFieldNameCopy(){
        if(fieldNameCopy == null){
            fieldNameCopy = new JTextField();
            fieldNameCopy.setName("fieldNameCopy");
            fieldNameCopy.setBounds(labelCreateNameCopy.getX()+labelCreateNameCopy.getWidth()+5, labelCreateNameCopy.getY()-3, 230,20);
        }
        return fieldNameCopy;
    }
     private JRadioButton getRbOpen(){
        if(rbOpen == null){
            rbOpen = new JRadioButton();
            rbOpen.setName("rbOpen");
            rbOpen.setText(owner.getBundleString("LABEL_OPEN_PROC"));
            rbOpen.setSelected(false);
            rbOpen.setFont(new java.awt.Font("Tahoma", 1, 11));
            rbOpen.setSize(60+CopexUtilities.lenghtOfString(this.rbOpen.getText(), getFontMetrics(this.rbOpen.getFont())), 23);
            rbOpen.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    selectRb(3);
                }
            });
            rbOpen.setBounds(10,fieldNameCopy.getY()+fieldNameCopy.getHeight()+10,rbOpen.getWidth(), rbOpen.getHeight());
        }
        return rbOpen;
    }

      private JButton getButtonExplOpen(){
         if(buttonExplOpen == null){
             buttonExplOpen = new JButton();
             buttonExplOpen.setName("buttonExplOpen");
             buttonExplOpen.setIcon(owner.getCopexImage("Button-Expl.png"));
             buttonExplOpen.setBounds(10, rbOpen.getY()+rbOpen.getHeight()+5, 22,22);
             buttonExplOpen.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    buttonExplOpen();
                }
            });
         }
         return buttonExplOpen;
     }

     private JLabel getLabelFileOpen(){
         if(labelFileOpen == null){
             labelFileOpen = new JLabel();
             labelFileOpen.setName("labelFileOpen");
             if(lastUsedFileOpen != null)
                 labelFileOpen.setText(lastUsedFileOpen.getPath());
             labelFileOpen.setFont(new java.awt.Font("Tahoma", 2, 11));
             labelFileOpen.setBounds(buttonExplOpen.getX()+buttonExplOpen.getWidth()+5,buttonExplOpen.getY()+4, 280,14);
         }
         return labelFileOpen;
     }


     private JLabel getLabelMissionOpen(){
         if(this.labelMissionOpen == null){
            labelMissionOpen = new JLabel();
            labelMissionOpen.setName("labelMissionOpen");
            labelMissionOpen.setText(owner.getBundleString("LABEL_MISSION"));
            labelMissionOpen.setFont(new java.awt.Font("Tahoma", 1, 11));
            labelMissionOpen.setSize(CopexUtilities.lenghtOfString(this.labelMissionOpen.getText(), getFontMetrics(this.labelMissionOpen.getFont())), 14);
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
             if (listMission.size() > 0)
                cbMissionOpen.setSelectedIndex(0);
             else
                rbOpen.setEnabled(false);
             cbMissionOpen.setBounds(labelMissionOpen.getX()+labelMissionOpen.getWidth()+5, labelMissionOpen.getY()-3, 100,20);
        }
        return cbMissionOpen;
     }

     private JLabel getLabelProcOpen(){
         if(this.labelProcOpen == null){
            labelProcOpen = new JLabel();
            labelProcOpen.setName("labelProcOpen");
            labelProcOpen.setText(owner.getBundleString("LABEL_PROC"));
            labelProcOpen.setFont(new java.awt.Font("Tahoma", 1, 11));
            labelProcOpen.setSize(CopexUtilities.lenghtOfString(this.labelProcOpen.getText(), getFontMetrics(this.labelProcOpen.getFont())), 14);
            labelProcOpen.setBounds(10, labelMissionOpen.getY()+labelMissionOpen.getHeight()+5, labelProcOpen.getWidth(), labelProcOpen.getHeight());
        }
        return labelProcOpen;
     }
     private JComboBox getCbProcOpen(){
         if(this.cbProcOpen == null){
             cbProcOpen = new JComboBox();
             cbProcOpen.setName("cbProcOpen");
             cbProcOpen.setBounds(labelProcOpen.getX()+labelProcOpen.getWidth()+5, labelProcOpen.getY()-3, 100,20);
        }
        return cbProcOpen;
     }


     

     private JButton getButtonOk(){
         if(buttonOk == null){
             buttonOk = new JButton();
             buttonOk.setName("buttonOk");
             buttonOk.setText(owner.getBundleString("BUTTON_OK"));
             buttonOk.setSize(60+CopexUtilities.lenghtOfString(this.buttonOk.getText(), getFontMetrics(this.buttonOk.getFont())), 23);
             int y = 20;
             if(dbMode){
                 y += labelProcOpen.getY()+labelProcOpen.getHeight();
             }else{
                 y += labelFileOpen.getY()+labelFileOpen.getHeight();
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
             buttonCancel.setSize(60+CopexUtilities.lenghtOfString(this.buttonCancel.getText(), getFontMetrics(this.buttonCancel.getFont())), 23);
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
        return rbCreate == null || rbCopy == null || rbOpen == null;
    }
    private void selectRb(int rb){
        if(rbNull())
            return;
        boolean create = rb==1;
        boolean copy = rb==2;
        boolean open = rb==3;
        this.rbCreate.setSelected(create);
        fieldName.setEditable(create);
        this.rbCopy.setSelected(copy);
        if(dbMode)
            this.cbProcCopy.setEnabled(copy);
        else
            this.buttonExplCopy.setEnabled(copy);
        fieldNameCopy.setEditable(copy);
        this.rbOpen.setSelected(open);
        if(dbMode){
            cbMissionOpen.setEnabled(open);
            cbProcOpen.setEnabled(open);
        }else{
            buttonExplOpen.setEnabled(open);
        }
    }


    private void changeMissionOpen(){
        // mise a jour de la liste des protocoles en fonction de la mission choisie
        cbProcOpen.removeAllItems();
        int id = cbMissionOpen.getSelectedIndex();
        if (id != -1){
            ArrayList<LearnerProcedure> list = this.listAllProc.get(id);
            if (list != null){
                // initialisation liste des missions
                int nb = list.size();
                for (int i=0; i<nb; i++){
                    LearnerProcedure p = list.get(i);
                    cbProcOpen.addItem(p.getName());
                }
                if (list.size() > 0)
                    cbProcOpen.setSelectedIndex(0);
            }
        }
        repaint();
    }
   
    private void changeProcCopy(){

    }

    private void changeCreateProcInit(){
        if(setDefaultProcName){
            int id = this.cbCreateProcInit.getSelectedIndex() ;
            if(id > 0){
                fieldName.setText(listInitialProc.get(id).getName());
            }
       }
    }
    
    private void buttonExplOpen(){
        JFileChooser aFileChooser = new JFileChooser();
        aFileChooser.setFileFilter(new MyFileFilterXML());
        if (lastUsedFileOpen != null)
            aFileChooser.setCurrentDirectory(lastUsedFileOpen.getParentFile());
        int userResponse = aFileChooser.showOpenDialog(this);
        if (userResponse == JFileChooser.APPROVE_OPTION){
            File file = aFileChooser.getSelectedFile();
            if(!CopexUtilities.isXMLFile(file)){
                owner.displayError(new CopexReturn(owner.getBundleString("MSG_ERROR_FILE_XML"), false), owner.getBundleString("TITLE_DIALOG_ERROR"));
                return;
            }
            lastUsedFileOpen = file;
            labelFileOpen.setText(file.getPath());
        }
    }
    private void buttonExplCopy(){
        JFileChooser aFileChooser = new JFileChooser();
        aFileChooser.setFileFilter(new MyFileFilterXML());
        if (lastUsedFileCopy != null)
            aFileChooser.setCurrentDirectory(lastUsedFileCopy.getParentFile());
        int userResponse = aFileChooser.showOpenDialog(this);
        if (userResponse == JFileChooser.APPROVE_OPTION){
            File file = aFileChooser.getSelectedFile();
            if(!CopexUtilities.isXMLFile(file)){
                owner.displayError(new CopexReturn(owner.getBundleString("MSG_ERROR_FILE_XML"), false), owner.getBundleString("TITLE_DIALOG_ERROR"));
                return;
            }
            lastUsedFileCopy = file;
            labelFileCopy.setText(file.getPath());
        }
    }

    
    private void buttonOk(){
        // recupere les donnees
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        if (this.rbCreate.isSelected()){
            // creation d'un nouveau protocole
            String name = fieldName.getText();
            if (name.length() == 0){
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                String msg = owner.getBundleString("MSG_ERROR_FIELD_NULL");
                msg = CopexUtilities.replace(msg, 0, owner.getBundleString("LABEL_PROC_NAME"));
                owner.displayError(new CopexReturn(msg, false), owner.getBundleString("TITLE_DIALOG_ERROR"));
                return;
            }
            if (name.length() > MyConstants.MAX_LENGHT_PROC_NAME){
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                String msg = owner.getBundleString("MSG_LENGHT_MAX");
                msg  = CopexUtilities.replace(msg, 0, owner.getBundleString("LABEL_PROC_NAME"));
                msg = CopexUtilities.replace(msg, 1, ""+MyConstants.MAX_LENGHT_PROC_NAME);
                owner.displayError(new CopexReturn(msg, false), owner.getBundleString("TITLE_DIALOG_ERROR"));
                return;
            }
            // recupere le proc initial
            InitialProcedure initProc = listInitialProc.get(0);
            if(!onlyOneInitProc){
                int id = this.cbCreateProcInit.getSelectedIndex() ;
                if (id == -1){
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    owner.displayError(new CopexReturn(owner.getBundleString("MSG_ERROR_OPEN_PROC"), false), owner.getBundleString("TITLE_DIALOG_ERROR"));
                    return;
                }
                initProc = this.listInitialProc.get(id);
            }
            CopexReturn cr = this.controller.createProc(name, initProc);
            if (cr.isError()){
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                owner.displayError(cr, owner.getBundleString("TITLE_DIALOG_ERROR"));
                return;
            }
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            this.dispose();
            owner.openQuestionDialog();
            return;
        }else if (this.rbCopy.isSelected()){
            // copie d'un protocole existant  dans la mission
            // nom de la copie
            String name = fieldNameCopy.getText();
            if (name.length() == 0){
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                String msg = owner.getBundleString("MSG_ERROR_FIELD_NULL");
                msg = CopexUtilities.replace(msg, 0, owner.getBundleString("LABEL_PROC_NAME"));
                owner.displayError(new CopexReturn(msg, false), owner.getBundleString("TITLE_DIALOG_ERROR"));
                return;
            }
            if (name.length() > MyConstants.MAX_LENGHT_PROC_NAME){
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                String msg = owner.getBundleString("MSG_LENGHT_MAX");
                msg  = CopexUtilities.replace(msg, 0, owner.getBundleString("LABEL_PROC_NAME"));
                msg = CopexUtilities.replace(msg, 1, ""+MyConstants.MAX_LENGHT_PROC_NAME);
                owner.displayError(new CopexReturn(msg, false), owner.getBundleString("TITLE_DIALOG_ERROR"));
                return;
            }
            // on recupere le protocole a copier
            if(dbMode){
                int id = this.cbProcCopy.getSelectedIndex();
                if (id == -1){
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    owner.displayError(new CopexReturn(owner.getBundleString("MSG_ERROR_COPY_PROC"), false), owner.getBundleString("TITLE_DIALOG_ERROR"));
                    return;
                }
                LearnerProcedure procToCopy = listProcMission.get(id);
                if (procToCopy == null){
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    owner.displayError(new CopexReturn(owner.getBundleString("MSG_ERROR_COPY_PROC"), false), owner.getBundleString("TITLE_DIALOG_ERROR"));
                    return;
                }
                CopexReturn cr = this.controller.copyProc(name, procToCopy);
                if (cr.isError()){
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    owner.displayError(cr, owner.getBundleString("TITLE_DIALOG_ERROR"));
                    return;
                }
            }else{
                if(lastUsedFileCopy == null){
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    owner.displayError(new CopexReturn(owner.getBundleString("MSG_ERROR_COPY_PROC"), false), owner.getBundleString("TITLE_DIALOG_ERROR"));
                    return;
                }
                owner.copyProc(name, lastUsedFileCopy);
            }
        }else if (this.rbOpen.isSelected()){
            if(dbMode){
                // on recupere la mission et le protocole selectionne
                int idM = this.cbMissionOpen.getSelectedIndex();
                if (idM == -1){
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    owner.displayError(new CopexReturn(owner.getBundleString("MSG_ERROR_OPEN_PROC"), false), owner.getBundleString("TITLE_DIALOG_ERROR"));
                    return;
                }
                CopexMission mission = listMission.get(idM);
                if (mission == null){
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    owner.displayError(new CopexReturn(owner.getBundleString("MSG_ERROR_OPEN_PROC"), false), owner.getBundleString("TITLE_DIALOG_ERROR"));
                    return;
                }
                int idP = this.cbProcOpen.getSelectedIndex();
                if (idP == -1){
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    owner.displayError(new CopexReturn(owner.getBundleString("MSG_ERROR_OPEN_PROC"), false), owner.getBundleString("TITLE_DIALOG_ERROR"));
                    return;
                }
                LearnerProcedure procToOpen = listAllProc.get(idM).get(idP);
                if (procToOpen == null){
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    owner.displayError(new CopexReturn(owner.getBundleString("MSG_ERROR_OPEN_PROC"), false), owner.getBundleString("TITLE_DIALOG_ERROR"));
                    return;
                }
                CopexReturn cr = this.controller.openProc(mission, procToOpen);
                if (cr.isError()){
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    owner.displayError(cr, owner.getBundleString("TITLE_DIALOG_ERROR"));
                    return;
                }
            }else{
                // open a file
                if(lastUsedFileOpen == null){
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    owner.displayError(new CopexReturn(owner.getBundleString("MSG_ERROR_OPEN_PROC"), false), owner.getBundleString("TITLE_DIALOG_ERROR"));
                    return;
                }
                owner.openProc(lastUsedFileOpen);
            }
        }
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        this.dispose();
    }

    private void buttonCancel(){
        this.dispose();
    }


}
