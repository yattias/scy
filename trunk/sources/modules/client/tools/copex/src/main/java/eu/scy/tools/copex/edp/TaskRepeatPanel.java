/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * TaskRepeatPanel.java
 *
 * Created on 29 avr. 2009, 11:58:07
 */

package eu.scy.tools.copex.edp;

import eu.scy.tools.copex.common.ActionParam;
import eu.scy.tools.copex.common.ActionParamData;
import eu.scy.tools.copex.common.ActionParamMaterial;
import eu.scy.tools.copex.common.ActionParamQuantity;
import eu.scy.tools.copex.common.CopexUnit;
import eu.scy.tools.copex.common.InitialAcquisitionOutput;
import eu.scy.tools.copex.common.InitialActionParam;
import eu.scy.tools.copex.common.InitialManipulationOutput;
import eu.scy.tools.copex.common.InitialOutput;
import eu.scy.tools.copex.common.InitialParamData;
import eu.scy.tools.copex.common.InitialParamMaterial;
import eu.scy.tools.copex.common.InitialParamQuantity;
import eu.scy.tools.copex.common.InitialTreatmentOutput;
import eu.scy.tools.copex.common.Material;
import eu.scy.tools.copex.common.Parameter;
import eu.scy.tools.copex.common.QData;
import eu.scy.tools.copex.common.Quantity;
import eu.scy.tools.copex.common.TaskRepeat;
import eu.scy.tools.copex.common.TaskRepeatParam;
import eu.scy.tools.copex.common.TaskRepeatParamData;
import eu.scy.tools.copex.common.TaskRepeatParamMaterial;
import eu.scy.tools.copex.common.TaskRepeatParamOutputAcquisition;
import eu.scy.tools.copex.common.TaskRepeatParamOutputManipulation;
import eu.scy.tools.copex.common.TaskRepeatParamOutputTreatment;
import eu.scy.tools.copex.common.TaskRepeatParamQuantity;
import eu.scy.tools.copex.common.TaskRepeatValueDataProd;
import eu.scy.tools.copex.common.TaskRepeatValueMaterialProd;
import eu.scy.tools.copex.common.TaskRepeatValueParamData;
import eu.scy.tools.copex.common.TaskRepeatValueParamMaterial;
import eu.scy.tools.copex.common.TaskRepeatValueParamQuantity;
import eu.scy.tools.copex.common.TypeMaterial;
import eu.scy.tools.copex.utilities.ActionCopexButton;
import eu.scy.tools.copex.utilities.ActionSelectParamTaskRepeat;
import eu.scy.tools.copex.utilities.CopexButtonPanel;
import eu.scy.tools.copex.utilities.CopexReturn;
import eu.scy.tools.copex.utilities.CopexUtilities;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * panel repeat task
 * @author Marjolaine
 */
public class TaskRepeatPanel extends javax.swing.JPanel implements ActionCopexButton, ActionSelectParamTaskRepeat {

    private final static int HEIGHT_PANEL_PARAM = 30;
    /* edp*/
    private EdPPanel edP;
    /*liste des parametres */
    private ArrayList<InitialActionParam> listAllParams;
    /* action ou etape */
    private boolean isAction;
    /* mode ajout ou non */
    private boolean modeAdd;
    /* actions */
    private ActionTaskRepeat actionTaskRepeat;

    /* nb repeat */
    private int nbRepeat;
    /* liste des parametres a faire varier */
    private ArrayList<InitialActionParam> listParam;
    /* liste des productions de taches */
    private ArrayList<InitialOutput> listOutput;
    /* liste des valeurs pour chaque itération */
    private ArrayList<ActionParam[]> listValueParam;

    /* task repeat */
    private TaskRepeat taskRepeat;

    /* graphique */
    private JLabel labelTaskRepeat;
    private JSpinner spinnerNbRepeat;
    private JLabel labelTaskRepeat2;
    private JLabel labelModifyParam;
    private JPanel panelParam;
    private JPanel panelRepeatParam;
    private JScrollPane scrollPaneParam;
    private CopexButtonPanel buttonAddParam;
    private CopexButtonPanel buttonRemoveParam;
    private ArrayList<JPanel> listPanels;
    private ArrayList<ArrayList<Object>> listParamPanel;

    /* liste des objets cree dans les iterations precedentes */
    // [indice panel , noRepeat, material]
    private ArrayList<ArrayList<Object>> listMaterialProd;
    private ArrayList<ArrayList<Object>> listDataProd;

    /* liste des param selectionnees */
    private ArrayList<Object> listCbSel;

    public TaskRepeatPanel(EdPPanel edP, ArrayList<InitialActionParam> listAllParams, ArrayList<InitialOutput> listOutput, boolean isAction, boolean modeAdd) {
        this.edP = edP;
        this.listAllParams = listAllParams;
        this.listOutput = listOutput ;
        this.isAction = isAction;
        this.modeAdd = modeAdd;
        this.nbRepeat = 1;
        this.listParam = new ArrayList();
        this.listValueParam = new ArrayList();
        this.listCbSel = new ArrayList();
        initComponents();
        initGUI();
    }


    private void initGUI(){
        setLayout(null);
        this.listPanels = new ArrayList();
        this.listParamPanel = new ArrayList();
        this.listMaterialProd = new ArrayList();
        this.listDataProd = new ArrayList();
        setTaskRepeatLabels();
        // cas ou il n'y a pas de parametres
        if(nbRepeat > 1){
            setPanelParam();
        }else
            resizePanel();
    }

     /**
    * Instancie l'objet ActionTaskRepeat.
    * @param action ActionTaskRepeat
    */
    public void addActionTaskRepeat(ActionTaskRepeat action){
        this.actionTaskRepeat=action;
    }

    /*task repeat */
    private JLabel getLabelTaskRepeat(String txt){
        if (this.labelTaskRepeat == null){
            labelTaskRepeat = new JLabel(txt);
            labelTaskRepeat.setFont(new java.awt.Font("Tahoma", 1, 11));
            labelTaskRepeat.setName("labelTaskRepeat");
            int w = CopexUtilities.lenghtOfString(this.labelTaskRepeat.getText(), getFontMetrics(this.labelTaskRepeat.getFont()));
            labelTaskRepeat.setBounds(0, 0,w, 14 );
        }
        return this.labelTaskRepeat;
    }
    private JSpinner getSpinnerNbRepeat(){
        if(this.spinnerNbRepeat == null){
            spinnerNbRepeat = new JSpinner();
            spinnerNbRepeat.setModel(new SpinnerNumberModel(nbRepeat,1,null,1));
            spinnerNbRepeat.setName("spinnerNbRepeat");
            spinnerNbRepeat.setBounds(labelTaskRepeat.getX()+labelTaskRepeat.getWidth()+10, 0, 40, 20);
            spinnerNbRepeat.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent evt) {
                    changeSpinnerValue();
                }
            });
        }
        return this.spinnerNbRepeat ;
    }

    private JLabel getLabelTaskRepeat2(String txt){
        if (this.labelTaskRepeat2 == null){
            labelTaskRepeat2 = new JLabel(txt);
            labelTaskRepeat2.setFont(new java.awt.Font("Tahoma", 1, 11));
            labelTaskRepeat2.setName("labelTaskRepeat2");
            int w = CopexUtilities.lenghtOfString(this.labelTaskRepeat2.getText(), getFontMetrics(this.labelTaskRepeat2.getFont()));
            labelTaskRepeat2.setBounds(this.spinnerNbRepeat.getX()+spinnerNbRepeat.getWidth()+10, 0,w, 14 );
        }
        return this.labelTaskRepeat2;
    }
    private void setTaskRepeatLabels(){
        String txt = edP.getBundleString("LABEL_TASK_REPEAT_STEP");
        if(isAction)
            txt = edP.getBundleString("LABEL_TASK_REPEAT_ACTION");
        int id1 = txt.indexOf("{");
        int id2 = txt.indexOf("}");
        String txt2= null;
        if(id1 != -1){
            if(id2 < (txt.length()-1))
                txt2 = txt.substring(id2+1);
            txt = txt.substring(0, id1);
        }
        this.add(getLabelTaskRepeat(txt));
        this.add(getSpinnerNbRepeat());
        if(txt2 != null && txt2.length() > 0){
            this.add(getLabelTaskRepeat2(txt2));
        }
        revalidate();
    }

    private void setPanelParam(){
        if(!isParam())
            return;
        getPanelParam();
        panelParam.add(getLabelModifyParam());
        this.panelParam.add(getScrollPaneParam());
        this.panelRepeatParam.add(getPanelActionParam(0));
        panelRepeatParam.revalidate();
        panelParam.setSize(300, 150);
        panelParam.add(getButtonAddParam());
        panelParam.add(getButtonRemoveParam());
        panelParam.revalidate();
        updateButtons();
        this.add(panelParam);
        resizePanel();
    }

    private void updatePanelParam(){
        int nb = this.listPanels.size();
        for (int i=0; i<nb; i++){
            ParamRepeatPanel table = getTableWithId(i);
            if(table != null){
                // modifcation du nombre de colonnes de la table
                int oldNbC = table.getColumnCount() ;
                int newNbC = nbRepeat;
                if(oldNbC > newNbC){
                    int nbCToRemove = oldNbC - newNbC ;
                    table.removeColumns(nbCToRemove);
                }else{
                    // ajoute des colonnes
                    int nbCToAdd = newNbC - oldNbC ;
                    table.addColumns(nbCToAdd);
                }
            }
            panelRepeatParam.setSize(table.getWidth()+100, panelRepeatParam.getHeight());
        }
    }

    private JPanel getPanelParam(){
        if (this.panelParam == null){
            panelParam = new JPanel();
            panelParam.setName("panelParam");
            panelParam.setLayout(null);
            panelParam.setBounds(0, labelTaskRepeat.getY()+labelTaskRepeat.getHeight()+20, 300, 100);
        }
        return this.panelParam ;
    }

    private JPanel getPanelRepeatParam(){
        if (this.panelRepeatParam == null){
            panelRepeatParam = new JPanel();
            panelRepeatParam.setName("panelRepeatParam");
            panelRepeatParam.setLayout(null);
            panelRepeatParam.setSize(300,100);
        }
        return this.panelRepeatParam ;
    }
    private JScrollPane getScrollPaneParam(){
        if(this.scrollPaneParam == null){
            scrollPaneParam = new JScrollPane();
            scrollPaneParam.setName("scrollPaneParam");
            scrollPaneParam.setViewportView(getPanelRepeatParam());
            scrollPaneParam.setBounds(0, labelModifyParam.getY()+labelModifyParam.getHeight(), 300, 100);
        }
        return this.scrollPaneParam ;
    }


    private JLabel getLabelModifyParam(){
        if (this.labelModifyParam == null){
            labelModifyParam = new JLabel(edP.getBundleString("LABEL_REPEAT_MODIFY_PARAM"));
            labelModifyParam.setFont(new java.awt.Font("Tahoma", 1, 11));
            labelModifyParam.setName("labelModifyParam");
            int w = CopexUtilities.lenghtOfString(this.labelModifyParam.getText(), getFontMetrics(this.labelModifyParam.getFont()));
            labelModifyParam.setBounds(0, 0,w, 14 );
        }
        return this.labelModifyParam;
    }

    private JPanel getPanelActionParam(int id){
        JPanel panel = new JPanel();
        panel.setLayout(null);
        ArrayList<InitialActionParam> list = getListParam(id) ;
        ArrayList<InitialOutput> listO = getListOutput(id);
        boolean none = id ==0;
        JComboBox cb = getCbParam(list, listO, none) ;
        cb.setSize(80, 20);
        panel.add(cb);
        panel.add(getTableParam(id, cb.getX()+cb.getWidth()+5));
        panel.setBounds(0, id*HEIGHT_PANEL_PARAM , panel.getWidth()+cb.getWidth(), HEIGHT_PANEL_PARAM);
        this.listPanels.add(panel);
        ArrayList<Object> listCb = new ArrayList();
        Object o = null;
        if(none){
            listCb.add(new String(""));
            o = new String("");
        }
        int nb = list.size();
        for (int i=0; i<nb; i++){
            listCb.add(list.get(i));
        }
        if(nb > 0 && o == null)
            o = list.get(0);
        nb = listO.size();
        for (int i=0; i<nb; i++){
            listCb.add(listO.get(i));
        }
        if(nb > 0 && o== null)
             o = listO.get(0);
        this.listParamPanel.add(listCb);
        this.listCbSel.add(o);
        cb.setSelectedIndex(0);
        return panel;
    }

    private ArrayList<InitialActionParam> getListParam(int id){
        if(id == 0)
            return this.listAllParams ;
        else{
            ArrayList<InitialActionParam> list = new ArrayList();
            int nb = this.listAllParams.size();
            for (int i=0; i<nb; i++){
                list.add(this.listAllParams.get(i));
            }
            // supprime les selections precedentes
            for (int i=0; i<id; i++){
                InitialActionParam p = getSelectedParam(i);
                if(p != null)
                    list.remove(p);
            }
            return list;
        }
    }

    private ArrayList<InitialOutput> getListOutput(int id){
        if(id == 0)
            return this.listOutput ;
        else{
            ArrayList<InitialOutput> list = new ArrayList();
            int nb = this.listOutput.size();
            for (int i=0; i<nb; i++){
                list.add(this.listOutput.get(i));
            }
            // supprime les selections precedentes
            for (int i=0; i<id; i++){
                InitialOutput p = getSelectedOutput(i);
                if(p != null)
                    list.remove(p);
            }
            return list;
        }
    }


    private JComboBox getCbParam(ArrayList<InitialActionParam> listP, ArrayList<InitialOutput> listO, boolean none){
        Vector list = new Vector();
        if(none){
            list.add(edP.getBundleString("LABEL_REPEAT_NO_PARAM"));
        }
        int nb = listP.size();
        for (int i=0; i<nb; i++){
            list.add(listP.get(i).getParamName());
        }
        nb = listO.size();
        for (int i=0; i<nb; i++){
            list.add(listO.get(i).getName());
        }
        JComboBox cb = new JComboBox(list);
        cb.setName("comboBox");
        cb.setBounds(5, 10, 70, 20);
        cb.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent evt) {
                changeCbSelection(evt);
            }
        });

        return cb;
    }

    private void changeCbSelection(ItemEvent evt){
        Object source = evt.getSource() ;
        if(source instanceof JComboBox){
            JComboBox cb = (JComboBox)source;
            int id  = getIdComboBox(cb);
            if( id != -1){
                Object oldSel = this.listCbSel.get(id);
                ParamRepeatPanel table = getTableWithId(id);
                boolean none = getSelectedNone(id);
                if (none){
                    table.setNone();
                    actionTaskRepeat.actionSetSelected(oldSel, new String(""));
                }else{
                    InitialActionParam p = getSelectedParam(id);
                    if (p != null){
                        if(p instanceof InitialParamQuantity){
                            ArrayList<CopexUnit> listUnit = ((InitialParamQuantity)p).getListUnit();
                            table.setParamQuantity(listUnit, ((InitialParamQuantity)p).getQuantityName());
                            actionTaskRepeat.actionSetSelected(oldSel,(InitialParamQuantity)p);
                        }else if (p instanceof InitialParamMaterial){
                            ArrayList<Material> materials = edP.getListMaterial(((InitialParamMaterial)p).getTypeMaterial(), ((InitialParamMaterial)p).getTypeMaterial2(), ((InitialParamMaterial)p).isAndTypes(), modeAdd);
                            table.setParamMaterial(materials);
                            actionTaskRepeat.actionSetSelected(oldSel,(InitialParamMaterial)p);
                        }else if (p instanceof InitialParamData){
                            ArrayList<QData> datas = edP.getDataProd( modeAdd);
                            table.setParamData(datas);
                            actionTaskRepeat.actionSetSelected(oldSel,(InitialParamData)p);
                        }
                    }else{
                        InitialOutput o = getSelectedOutput(id);
                        if (o != null){
                            if(o instanceof InitialManipulationOutput){
                                ArrayList<Material> listMat = edP.getMaterialProd(modeAdd);
                                ArrayList<TypeMaterial> listType = ((InitialManipulationOutput)o).getTypeMaterialProd();
                                table.setOutputMaterial(listMat, listType);
                                actionTaskRepeat.actionSetSelected(oldSel,(InitialManipulationOutput)o);
                            }else if (o instanceof InitialAcquisitionOutput || o instanceof InitialTreatmentOutput){
                                CopexUnit unit = null;
                                if (o instanceof InitialAcquisitionOutput){
                                    unit =((InitialAcquisitionOutput)o).getUnitDataProd()[0];
                                    actionTaskRepeat.actionSetSelected(oldSel,(InitialAcquisitionOutput)o);
                                }
                                else if (o instanceof InitialTreatmentOutput){
                                    unit =((InitialTreatmentOutput)o).getUnitDataProd()[0];
                                    actionTaskRepeat.actionSetSelected(oldSel,(InitialTreatmentOutput)o);
                                }
                                table.setOutputData(unit);
                            }
                        }
                    }
                }
            }
            resizePanel();
            updateButtons();
            repaint();
        }
    }

    /* retourne l'indice de la combo en param */
    private int getIdComboBox(JComboBox cb){
        int nb = listPanels.size();
        for (int i=0; i<nb; i++){
            JComboBox c = getCbWithId(i);
            if (c != null && c.equals(cb))
                return i;
        }
        return -1;
    }

   


    private CopexButtonPanel getButtonAddParam(){
        if(this.buttonAddParam == null){
            ImageIcon buttonAdd = edP.getCopexImage("Bouton-onglet_ouverture.png");
            ImageIcon buttonAddSurvol = edP.getCopexImage("Bouton-onglet_ouverture_sur.png");
            ImageIcon buttonAddClic = edP.getCopexImage("Bouton-onglet_ouverture_cli.png");
            ImageIcon buttonAddDisabled = edP.getCopexImage("Bouton-onglet_ouverture_grise.png");
            buttonAddParam = new CopexButtonPanel(edP, 28, buttonAdd.getImage(), buttonAddSurvol.getImage(), buttonAddClic.getImage(), buttonAddDisabled.getImage());
            buttonAddParam.addActionCopexButton(this);
            buttonAddParam.setToolTipText(edP.getBundleString("TOOLTIPTEXT_ADD_TASK_REPEAT_PARAM"));
            int nb = listPanels.size()-1;
            int y = listPanels.get(nb).getY()+listPanels.get(nb).getHeight()+20;
            buttonAddParam.setBounds(10, y, 28, 20);
        }
        return buttonAddParam ;
    }

     private CopexButtonPanel getButtonRemoveParam(){
        if(this.buttonRemoveParam == null){
            ImageIcon buttonRemove = edP.getCopexImage("Bouton-onglet_moins.png");
            ImageIcon buttonRemoveSurvol = edP.getCopexImage("Bouton-onglet_moins_sur.png");
            ImageIcon buttonRemoveClic = edP.getCopexImage("Bouton-onglet_moins_cli.png");
            ImageIcon buttonRemoveDisabled = edP.getCopexImage("Bouton-onglet_moins_grise.png");
            buttonRemoveParam = new CopexButtonPanel(edP, 28, buttonRemove.getImage(), buttonRemoveSurvol.getImage(), buttonRemoveClic.getImage(), buttonRemoveDisabled.getImage());
            buttonRemoveParam.addActionCopexButton(this);
            buttonRemoveParam.setToolTipText(edP.getBundleString("TOOLTIPTEXT_REMOVE_TASK_REPEAT_PARAM"));
            buttonRemoveParam.setBounds(buttonAddParam.getX()+buttonAddParam.getWidth()+5, buttonAddParam.getY(), 28,20);
        }
        return buttonRemoveParam ;
    }

     private ParamRepeatPanel getTableParam(int index, int x){
         ParamRepeatPanel p = new ParamRepeatPanel(index, nbRepeat);
         p.setName("table");
         p.addActionSelectParamTaskRepeat(this);
         p.setBounds(x, 10, p.getWidth(), p.getHeight());
         return p;
     }

    private void deletePanelParam(){
        if(isParam()){
            if(panelRepeatParam != null){
                panelRepeatParam.removeAll();
            }
            panelParam.removeAll();
            labelModifyParam = null;
            listPanels = new ArrayList();
            listParamPanel = new ArrayList();
            remove(panelParam);
            panelParam = null;
            resizePanel();
        }
    }

    
    private void resizePanel(){
        int width = getWidth();
        int nb = this.listPanels.size();
        int height = 20;
        if(panelParam != null){
            int h1 = nb*HEIGHT_PANEL_PARAM;
            int h = scrollPaneParam.getHeight()+buttonAddParam.getHeight()+20+labelModifyParam.getHeight()+10;
            panelParam.setSize(width, h);
            height += h;

            if(panelRepeatParam != null){
                //int w = width+100;
                //panelRepeatParam.setSize(w, h1);
                int maxW = 0;
                for (int i=0; i<listPanels.size(); i++){
                    int pw = listPanels.get(i).getComponent(0).getWidth()+listPanels.get(i).getComponent(1).getWidth()+20;
                    maxW  = Math.max(maxW, pw);
                    listPanels.get(i).setSize(pw, listPanels.get(i).getHeight());
                }
                panelRepeatParam.setSize(maxW+20, h1);
                panelRepeatParam.setPreferredSize(panelRepeatParam.getSize());
                int y = h - buttonAddParam.getHeight() -10;
                buttonAddParam.setBounds(buttonAddParam.getX(), y, buttonAddParam.getWidth(), buttonAddParam.getHeight());
                buttonRemoveParam.setBounds(buttonRemoveParam.getX(), y, buttonRemoveParam.getWidth(), buttonRemoveParam.getHeight());
            }
            scrollPaneParam.setSize(width, scrollPaneParam.getHeight());
        }
        revalidate();
        setSize(width, height);
        setPreferredSize(getSize());
        if(actionTaskRepeat != null)
            actionTaskRepeat.actionResize();
    }

    

    private InitialActionParam getSelectedParam(int id){
        int idP = getCbWithId(id).getSelectedIndex();
        ArrayList<Object> list = listParamPanel.get(id);
        Object o = list.get(idP);
        if(o instanceof InitialActionParam)
            return (InitialActionParam)o;
        return null;
    }

    private InitialOutput getSelectedOutput(int id){
        int idP = getCbWithId(id).getSelectedIndex();
        ArrayList<Object> list = listParamPanel.get(id);
        Object o = list.get(idP);
        if(o instanceof InitialOutput)
            return (InitialOutput)o;
        return null;
    }

    private boolean getSelectedNone(int id){
        int idP = getCbWithId(id).getSelectedIndex();
        ArrayList<Object> list = listParamPanel.get(id);
        Object o = list.get(idP);
        if(o instanceof String)
            return true;
        return false;
    }


    private JComboBox getCbWithId(int id){
        Component[] allC = this.listPanels.get(id).getComponents();
        for (int i=0; i<allC.length; i++){
            if (allC[i].getName().equals("comboBox"))
                return (JComboBox)allC[i];
        }
        return null;
    }
    private ParamRepeatPanel getTableWithId(int id){
        Component[] allC = this.listPanels.get(id).getComponents();
        for (int i=0; i<allC.length; i++){
            if (allC[i].getName().equals("table"))
                return (ParamRepeatPanel)allC[i];
        }
        return null;
    }

    private void changeSpinnerValue(){
        Object o = spinnerNbRepeat.getValue();
        if(!(o instanceof Integer)){
            edP.displayError(new CopexReturn(edP.getBundleString("MSG_ERROR_NB_TASK_REPEAT"), false), edP.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }else{
            int value = (Integer)o;
            if(value < 1){
                edP.displayError(new CopexReturn(edP.getBundleString("MSG_ERROR_NB_TASK_REPEAT"), false), edP.getBundleString("TITLE_DIALOG_ERROR"));
                return;
            }
            int oldR = nbRepeat;
            nbRepeat = value;
            if(value == 1)
                deletePanelParam();
            else{
                if(oldR == 1)
                    setPanelParam();
                else
                    updatePanelParam();
            }
            resizePanel();
            actionTaskRepeat.actionUpdateNbRepeat(nbRepeat);
        }
    }
    /* rend disabled les combo box jusqu'a id non compris*/
    private void disabledCb(int id){
        for (int i=0; i<id; i++){
            getCbWithId(i).setEnabled(false);
        }
        int nb = listPanels.size();
        for(int i=id;i<nb; i++){
            getCbWithId(i).setEnabled(true);
        }
    }

    private void updateButtons(){
        int nb = listPanels.size();
        buttonRemoveParam.setEnabled(nb > 1);
        int nbTot = listAllParams.size();
        buttonAddParam.setEnabled(nb < nbTot);
        if(nb == 1 && getSelectedNone(0))
            buttonAddParam.setEnabled(false);
    }

    private void addParam(){
        int nb = listPanels.size() ;
        this.panelRepeatParam.add(getPanelActionParam(nb));
        disabledCb(nb);
        panelRepeatParam.revalidate();
        updateButtons();
        resizePanel();
    }

    private void removeParam(){
        int nb = listPanels.size();
        if(nb == 0)
            return;
        int id = nb-1;
        panelRepeatParam.remove(listPanels.get(id));
        listPanels.remove(id);
        listCbSel.remove(id);
        listParamPanel.remove(id);
        disabledCb(id-1);
        panelRepeatParam.revalidate();
        updateButtons();
        resizePanel();
        deleteMaterialProd(id);
        deleteDataProd(id);
    }

    /* renvoit true si cas sans param */
    private boolean isParam(){
        return this.listAllParams.size() > 0 || this.listOutput.size() > 0;
    }

    /* retourne le nombre de repetitions */
    public int getNbRepeat(){
        return (Integer)this.spinnerNbRepeat.getValue();
    }

    /* met les valeurs d'une tacke repeat */
    public void setTaskRepeat(TaskRepeat taskRepeat){
        this.taskRepeat = taskRepeat;
        if(taskRepeat == null)
            return;
        this.nbRepeat = taskRepeat.getNbRepeat();
        this.spinnerNbRepeat.setValue(nbRepeat);
        if(nbRepeat > 1){
            ArrayList<TaskRepeatParam> listP = taskRepeat.getListParam();
            int nb = listP.size();
            for (int i=0; i<nb; i++){
                addParam();
                String name = "";
                if (listP.get(i) instanceof TaskRepeatParamData)
                    name = ((TaskRepeatParamData)listP.get(i)).getInitialParamData().getParamName() ;
                else if (listP.get(i) instanceof TaskRepeatParamMaterial)
                    name = ((TaskRepeatParamMaterial)listP.get(i)).getInitialParamMaterial().getParamName() ;
                else if (listP.get(i) instanceof TaskRepeatParamQuantity)
                    name = ((TaskRepeatParamQuantity)listP.get(i)).getInitialParamQuantity().getParamName() ;
                else if (listP.get(i) instanceof TaskRepeatParamOutputAcquisition)
                    name = ((TaskRepeatParamOutputAcquisition)listP.get(i)).getOutput().getName() ;
                else if (listP.get(i) instanceof TaskRepeatParamOutputManipulation)
                    name = ((TaskRepeatParamOutputManipulation)listP.get(i)).getOutput().getName() ;
                else if (listP.get(i) instanceof TaskRepeatParamOutputTreatment)
                    name = ((TaskRepeatParamOutputTreatment)listP.get(i)).getOutput().getName() ;
                getCbWithId(i).setSelectedItem(name);
                getTableWithId(i).setTaskRepeatParam(taskRepeat.getListParam().get(i));
            }
        }
    }

    /*disabled */
    public void setDisabled(){
        this.spinnerNbRepeat.setEnabled(false);
        if(panelParam != null){
            buttonAddParam.setEnabled(false);
            buttonRemoveParam.setEnabled(false);
            int nb = listPanels.size();
            disabledCb(nb);
            disabledTable();
        }
    }

    /* rend disabled les tables */
    private void disabledTable(){
        int nb = listPanels.size();
        for (int i=0; i<nb; i++){
            ParamRepeatPanel table = getTableWithId(i) ;
            if(table != null){
                table.disabled();
            }
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    @Override
    public void actionCopexButtonClic(CopexButtonPanel button) {
        if(button.equals(this.buttonAddParam)){
            addParam();
        }else{
            removeParam();
        }
    }

    @Override
    public void addOutputMaterial(int index, int noRepeat, Material material) {
        System.out.println("ajout d'un materiel : "+material.getName()+" en "+noRepeat);

    }

    // retourne la liste de material prod selon le type d'action
    private ArrayList<Material> getMaterialProd(InitialParamMaterial param){
        ArrayList<Material> list = new ArrayList();
        int nb = listMaterialProd.size();
        for (int i=0; i<nb; i++){
            ArrayList<Object> l = listMaterialProd.get(i);
            Material m = (Material)l.get(2);
            if (param.canAccept(m)){
                list.add(m);
            }
        }
        return list;
    }


    @Override
    public void addOutputData(int index, int noRepeat, QData data) {
       System.out.println("ajout d'un donnees : "+data.getName()+" en "+noRepeat);
    }

    // suppression du material cree dans un panel
    private void deleteMaterialProd(int index){
        int nb = listMaterialProd.size();
        for (int i=nb-1; i>=0; i++){
            ArrayList<Object> list = listMaterialProd.get(i);
            int id = (Integer)list.get(0);
            if (id == index)
                listMaterialProd.remove(i);
        }
    }

    //suppression des data prod dansun panel
    private void deleteDataProd(int index){
        int nb = listDataProd.size();
        for (int i=nb-1; i>=0; i++){
            ArrayList<Object> list = listDataProd.get(i);
            int id = (Integer)list.get(0);
            if (id == index)
                listDataProd.remove(i);
        }
    }

    // renvoit la task repeat ainsi constituee
    public TaskRepeat getTaskRepeat(){
        long dbKey = -1;
        if (taskRepeat != null)
            dbKey = taskRepeat.getDbKey();
        ArrayList<TaskRepeatParam> listP = new ArrayList();
        int nb = listPanels.size() ;
        for (int i=0; i<nb; i++){
            boolean none = getSelectedNone(i);
            if (!none){
                InitialActionParam param = getSelectedParam(i);
                if (param == null){
                    InitialOutput output = getSelectedOutput(i);
                    if(output instanceof InitialManipulationOutput){
                        ArrayList<TaskRepeatValueMaterialProd> listValue= new ArrayList();
                        ParamRepeatPanel valuePanel = getTableWithId(i);
                        if (valuePanel != null){
                            ArrayList<Material> list = valuePanel.getOutputMaterial();
                            int nbV = list.size();
                            for (int k=0; k<nbV; k++){
                                TaskRepeatValueMaterialProd value = new TaskRepeatValueMaterialProd(-1, k, list.get(k));
                                listValue.add(value);
                            }
                        }
                        TaskRepeatParamOutputManipulation out = new TaskRepeatParamOutputManipulation(-1, (InitialManipulationOutput)output, listValue);
                        listP.add(out);
                    }else if (output instanceof InitialAcquisitionOutput){
                        ArrayList<TaskRepeatValueDataProd> listValue= new ArrayList();
                        ParamRepeatPanel valuePanel = getTableWithId(i);
                        if (valuePanel != null){
                            ArrayList<QData> list = valuePanel.getOutputData();
                            int nbV = list.size();
                            for (int k=0; k<nbV; k++){
                                TaskRepeatValueDataProd value = new TaskRepeatValueDataProd(-1, k, list.get(k));
                                listValue.add(value);
                            }
                        }
                        TaskRepeatParamOutputAcquisition out = new TaskRepeatParamOutputAcquisition(-1, (InitialAcquisitionOutput)output, listValue);
                        listP.add(out);
                    }else if (output instanceof InitialTreatmentOutput){
                        ArrayList<TaskRepeatValueDataProd> listValue= new ArrayList();
                        ParamRepeatPanel valuePanel = getTableWithId(i);
                        if (valuePanel != null){
                            ArrayList<QData> list = valuePanel.getOutputData();
                            int nbV = list.size();
                            for (int k=0; k<nbV; k++){
                                TaskRepeatValueDataProd value = new TaskRepeatValueDataProd(-1, k, list.get(k));
                                listValue.add(value);
                            }
                        }
                        TaskRepeatParamOutputTreatment out = new TaskRepeatParamOutputTreatment(-1, (InitialTreatmentOutput)output, listValue);
                        listP.add(out);
                    }
                }else{
                    if (param instanceof InitialParamData){
                        ArrayList<TaskRepeatValueParamData> listValue = new ArrayList();
                        ParamRepeatPanel valuePanel = getTableWithId(i);
                        if (valuePanel != null){
                            ArrayList<QData> list = valuePanel.getParamData();
                            int nbV = list.size();
                            for (int k=0; k<nbV; k++){
                                ActionParamData actionParamData = new ActionParamData(-1,(InitialParamData)param , list.get(k));
                                TaskRepeatValueParamData value = new TaskRepeatValueParamData(-1, k, actionParamData);
                                listValue.add(value);
                            }
                        }
                        TaskRepeatParamData p = new TaskRepeatParamData(-1, (InitialParamData)param, listValue);
                        listP.add(p);
                    }else if (param instanceof InitialParamMaterial){
                        ArrayList<TaskRepeatValueParamMaterial> listValue = new ArrayList();
                        ParamRepeatPanel valuePanel = getTableWithId(i);
                        if (valuePanel != null){
                            ArrayList<Material> list = valuePanel.getParamMaterial();
                            int nbV = list.size();
                            for (int k=0; k<nbV; k++){
                                ActionParamMaterial actionParamMaterial = new ActionParamMaterial(-1,(InitialParamMaterial)param , list.get(k), null);
                                TaskRepeatValueParamMaterial value = new TaskRepeatValueParamMaterial(-1, k, actionParamMaterial);
                                listValue.add(value);
                            }
                        }
                        TaskRepeatParamMaterial p = new TaskRepeatParamMaterial(-1, (InitialParamMaterial)param, listValue);
                        listP.add(p);
                    }else if (param instanceof InitialParamQuantity){
                        ArrayList<TaskRepeatValueParamQuantity> listValue = new ArrayList();
                        ParamRepeatPanel valuePanel = getTableWithId(i);
                        if (valuePanel != null){
                            ArrayList<Parameter> list = valuePanel.getParamQuantity();
                            int nbV = list.size();
                            for (int k=0; k<nbV; k++){
                                ActionParamQuantity actionParamQuantity = new ActionParamQuantity(-1,(InitialParamQuantity)param , list.get(k));
                                TaskRepeatValueParamQuantity value = new TaskRepeatValueParamQuantity(-1, k, actionParamQuantity);
                                listValue.add(value);
                            }
                        }
                        TaskRepeatParamQuantity p = new TaskRepeatParamQuantity(-1, (InitialParamQuantity)param, listValue);
                        listP.add(p);
                    }
                }
            }
        }
        taskRepeat = new TaskRepeat(dbKey, nbRepeat, listP);
        return taskRepeat;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

}
