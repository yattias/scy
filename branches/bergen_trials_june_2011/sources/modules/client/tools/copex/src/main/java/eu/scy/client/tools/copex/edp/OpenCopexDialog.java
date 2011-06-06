/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.edp;

import eu.scy.client.tools.copex.common.CopexMission;
import eu.scy.client.tools.copex.common.InitialProcedure;
import eu.scy.client.tools.copex.common.LearnerProcedure;
import eu.scy.client.tools.copex.controller.ControllerInterface;
import eu.scy.client.tools.copex.utilities.CopexReturn;
import eu.scy.client.tools.copex.utilities.CopexUtilities;
import eu.scy.client.tools.copex.utilities.MyConstants;
import eu.scy.client.tools.copex.utilities.MyFileFilterXML;
import java.awt.Cursor;
import java.awt.event.ItemEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
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

    /* controller */
    private ControllerInterface controller;
    /* liste des proc initiaux*/
    private List<InitialProcedure> listInitialProc;
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

   
    private JRadioButton rbOpen;
    private JLabel labelOpen;
    private JLabel labelMissionOpen;
    private JComboBox cbMissionOpen;
    private JLabel labelProcOpen;
    private JComboBox cbProcOpen;


    private JButton buttonOk;
    private JButton buttonCancel;

    private boolean canNew;

    public OpenCopexDialog(CopexPanel owner, ControllerInterface controller,  File openFile, List<InitialProcedure> listInitialProc) {
        super(owner.getOwnerFrame());
        this.owner = owner;
        this.controller = controller;
        this.dbMode = false;
        this.canNew = true;
        this.lastUsedFileOpen = openFile;
        int nb = listInitialProc.size();
        onlyOneInitProc = (nb == 1);
        this.listInitialProc = listInitialProc ;
        initGUI();
        setResizable(false);
        setModal(true);
        setLocationRelativeTo(owner);
        setIconImage(owner.getIconDialog());
    }

    public OpenCopexDialog(CopexPanel owner,ControllerInterface controller, ArrayList<CopexMission> listMission, ArrayList<ArrayList<LearnerProcedure>> listAllProc, List<InitialProcedure> listInitialProc) {
        super(owner.getOwnerFrame());
        this.owner = owner;
        this.controller = controller;
        this.dbMode = true;
        this.canNew = false;
        this.lastUsedFileOpen = null;
        int nb = listInitialProc.size();
        onlyOneInitProc = (nb == 1);
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
        if(canNew)
            setTitle(owner.getBundleString("TITLE_DIALOG_ADD_PROC"));
        else
            setTitle(owner.getBundleString("TITLE_DIALOG_OPEN_PROC"));
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        if(canNew){
            this.add(getRbCreate());
            if(!onlyOneInitProc){
                this.add(getCbCreateProcInit());
            }
            this.add(getLabelCreateName());
            this.add(getFieldCreateName());
        }
        if(canNew)
            this.add(getRbOpen());
        else{
            this.add(getLabelOpen());
        }
        if(dbMode){
            this.add(getLabelMissionOpen());
            this.add(getCbMissionOpen());
            this.add(getLabelProcOpen());
            this.add(getCbProcOpen());
            if(cbMissionOpen.getItemCount()>0)
                this.cbMissionOpen.setSelectedIndex(0);
            changeMissionOpen();
        }

        // largeur
        int width = 350;
        if(canNew)
            width = fieldName.getX()+fieldName.getWidth()+10;
        int height = 200;
        setSize(width, height);
        this.add(getButtonOk());
        this.add(getButtonCancel());
        height = this.buttonCancel.getY()+this.buttonCancel.getHeight()+40;
        setSize(width, height);
        setPreferredSize(getSize());
        if(canNew)
            selectRb(1);
        else{
            if(dbMode){
                cbMissionOpen.setEnabled(true);
                cbProcOpen.setEnabled(true);
            }
        }
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
                @Override
                public void itemStateChanged(ItemEvent e) {
                    changeCreateProcInit();
                }
             });
             if (nb > 0){
                cbCreateProcInit.setSelectedIndex(0);
                if(setDefaultProcName)
                    fieldName.setText(listInitialProc.get(0).getName(owner.getLocale()));
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
            fieldName.setBounds(labelCreateName.getX()+labelCreateName.getWidth()+5, labelCreateName.getY()-5, 230,27);
        }
        return fieldName;
    }


     private JLabel getLabelOpen(){
        if(this.labelOpen == null){
            labelOpen = new JLabel();
            labelOpen.setName("labelOpen");
            labelOpen.setText(owner.getBundleString("LABEL_OPEN_PROC"));
            labelOpen.setFont(new java.awt.Font("Tahoma", 1, 11));
            labelOpen.setSize(CopexUtilities.lenghtOfString(this.labelOpen.getText(), getFontMetrics(this.labelOpen.getFont())), 14);
            labelOpen.setBounds(10, 10, labelOpen.getWidth(), labelOpen.getHeight());
        }
        return labelOpen;
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
                    selectRb(2);
                }
            });
            int y= 10;
            if(canNew)
                y = fieldName.getY()+fieldName.getHeight()+10 ;
            rbOpen.setBounds(10,y,rbOpen.getWidth(), rbOpen.getHeight());
        }
        return rbOpen;
    }

      
     private JLabel getLabelMissionOpen(){
         if(this.labelMissionOpen == null){
            labelMissionOpen = new JLabel();
            labelMissionOpen.setName("labelMissionOpen");
            labelMissionOpen.setText(owner.getBundleString("LABEL_MISSION"));
            labelMissionOpen.setFont(new java.awt.Font("Tahoma", 1, 11));
            labelMissionOpen.setSize(CopexUtilities.lenghtOfString(this.labelMissionOpen.getText(), getFontMetrics(this.labelMissionOpen.getFont())), 14);
            int y =10;
            if(rbOpen != null){
                y = rbOpen.getY()+rbOpen.getHeight()+5;
            }else if (labelOpen != null){
                y = labelOpen.getY()+labelOpen.getHeight()+5;
            }
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
                public void itemStateChanged(ItemEvent e) {
                    changeMissionOpen();
                }
             });
             if (listMission.size() > 0)
                cbMissionOpen.setSelectedIndex(0);
             else if(rbOpen != null)
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
            labelProcOpen.setBounds(10, cbMissionOpen.getY()+cbMissionOpen.getHeight()+10, labelProcOpen.getWidth(), labelProcOpen.getHeight());
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
                 y += rbOpen.getY()+rbOpen.getHeight();
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
        return rbCreate == null ||  rbOpen == null;
    }
    private void selectRb(int rb){
        if(rbNull())
            return;
        boolean create = rb==1;
        boolean open = rb==2;
        if(canNew){
            this.rbCreate.setSelected(create);
            fieldName.setEditable(create);
        }
        this.rbOpen.setSelected(open);
        if(dbMode){
            cbMissionOpen.setEnabled(open);
            cbProcOpen.setEnabled(open);
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
                    cbProcOpen.addItem(p.getName(owner.getLocale()));
                }
                if (list.size() > 0)
                    cbProcOpen.setSelectedIndex(0);
            }
        }
        repaint();
    }
   
   
    private void changeCreateProcInit(){
        if(setDefaultProcName){
            int id = this.cbCreateProcInit.getSelectedIndex() ;
            if(id > 0){
                fieldName.setText(listInitialProc.get(id).getName(owner.getLocale()));
            }
       }
    }
    
    private void buttonExplOpen(){
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        JFileChooser aFileChooser = new JFileChooser();
        aFileChooser.setFileFilter(new MyFileFilterXML());
        if (lastUsedFileOpen != null){
            aFileChooser.setCurrentDirectory(lastUsedFileOpen.getParentFile());
            aFileChooser.setSelectedFile(lastUsedFileOpen);
        }
        int userResponse = aFileChooser.showOpenDialog(this);
        if (userResponse == JFileChooser.APPROVE_OPTION){
            File file = aFileChooser.getSelectedFile();
            if(!CopexUtilities.isXMLFile(file)){
                owner.displayError(new CopexReturn(owner.getBundleString("MSG_ERROR_FILE_XML"), false), owner.getBundleString("TITLE_DIALOG_ERROR"));
                return;
            }
            lastUsedFileOpen = file;
            // open a file
            if(lastUsedFileOpen == null){
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                owner.displayError(new CopexReturn(owner.getBundleString("MSG_ERROR_OPEN_PROC"), false), owner.getBundleString("TITLE_DIALOG_ERROR"));
                return;
            }
            owner.openProc(lastUsedFileOpen);
            owner.setQuestionDialog();
            this.dispose();
        }
    }

    private boolean controlLenght(){
        return owner.controlLenght();
    }

    private void buttonOk(){
        // recupere les donnees
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        if (rbCreate != null && this.rbCreate.isSelected()){
            // creation d'un nouveau protocole
            String name = fieldName.getText();
            if (name.length() == 0){
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                String msg = owner.getBundleString("MSG_ERROR_FIELD_NULL");
                msg = CopexUtilities.replace(msg, 0, owner.getBundleString("LABEL_PROC_NAME"));
                owner.displayError(new CopexReturn(msg, false), owner.getBundleString("TITLE_DIALOG_ERROR"));
                return;
            }
            if (controlLenght() && name.length() > MyConstants.MAX_LENGHT_PROC_NAME){
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
            owner.setQuestionDialog();
            return;
        }else if (rbOpen != null && this.rbOpen.isSelected() || !canNew){
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
                owner.setQuestionDialog();
            }else{
                buttonExplOpen();
            }
        }
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        this.dispose();
    }

    private void buttonCancel(){
        this.dispose();
    }


}
