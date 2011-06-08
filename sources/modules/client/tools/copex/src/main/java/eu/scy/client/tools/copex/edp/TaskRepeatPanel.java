/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * TaskRepeatPanel.java
 *
 * Created on 29 avr. 2009, 11:58:07
 */

package eu.scy.client.tools.copex.edp;

import eu.scy.client.tools.copex.common.ActionParam;
import eu.scy.client.tools.copex.common.ActionParamData;
import eu.scy.client.tools.copex.common.ActionParamMaterial;
import eu.scy.client.tools.copex.common.ActionParamQuantity;
import eu.scy.client.tools.copex.common.CopexUnit;
import eu.scy.client.tools.copex.common.InitialAcquisitionOutput;
import eu.scy.client.tools.copex.common.InitialActionParam;
import eu.scy.client.tools.copex.common.InitialManipulationOutput;
import eu.scy.client.tools.copex.common.InitialOutput;
import eu.scy.client.tools.copex.common.InitialParamData;
import eu.scy.client.tools.copex.common.InitialParamMaterial;
import eu.scy.client.tools.copex.common.InitialParamQuantity;
import eu.scy.client.tools.copex.common.InitialTreatmentOutput;
import eu.scy.client.tools.copex.common.Material;
import eu.scy.client.tools.copex.common.Parameter;
import eu.scy.client.tools.copex.common.QData;
import eu.scy.client.tools.copex.common.TaskRepeat;
import eu.scy.client.tools.copex.common.TaskRepeatParam;
import eu.scy.client.tools.copex.common.TaskRepeatParamData;
import eu.scy.client.tools.copex.common.TaskRepeatParamMaterial;
import eu.scy.client.tools.copex.common.TaskRepeatParamOutputAcquisition;
import eu.scy.client.tools.copex.common.TaskRepeatParamOutputManipulation;
import eu.scy.client.tools.copex.common.TaskRepeatParamOutputTreatment;
import eu.scy.client.tools.copex.common.TaskRepeatParamQuantity;
import eu.scy.client.tools.copex.common.TaskRepeatValueDataProd;
import eu.scy.client.tools.copex.common.TaskRepeatValueMaterialProd;
import eu.scy.client.tools.copex.common.TaskRepeatValueParamData;
import eu.scy.client.tools.copex.common.TaskRepeatValueParamMaterial;
import eu.scy.client.tools.copex.common.TaskRepeatValueParamQuantity;
import eu.scy.client.tools.copex.common.TypeMaterial;
import eu.scy.client.tools.copex.utilities.ActionCopexButton;
import eu.scy.client.tools.copex.utilities.ActionSelectParamTaskRepeat;
import eu.scy.client.tools.copex.utilities.CopexButtonPanel;
import eu.scy.client.tools.copex.utilities.CopexReturn;
import eu.scy.client.tools.copex.utilities.CopexUtilities;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * panel repeat task
 * @author Marjolaine
 */
public class TaskRepeatPanel extends javax.swing.JPanel implements ActionCopexButton, ActionSelectParamTaskRepeat {

    private final static int HEIGHT_PANEL_PARAM = 35;
    /* owner*/
    private EdPPanel edP;
    /* parameters list */
    private ArrayList<InitialActionParam> listAllParams;
    /* action or step */
    private boolean isAction;
    /* mode add or not */
    private boolean modeAdd;
    /* actions */
    private ActionTaskRepeat actionTaskRepeat;

    /* nb repeat */
    private int nbRepeat;
    /* list param to modify */
    private ArrayList<InitialActionParam> listParam;
    /* list of prod. for tasks */
    private ArrayList<InitialOutput> listOutput;
    /* list of values for each iteration */
    private ArrayList<ActionParam[]> listValueParam;

    private ArrayList<Long> listDbKeyActionParam;
    private ArrayList<Long> listDbKeyActionOutput;

    /* task repeat */
    private TaskRepeat taskRepeat;

    /* gui */
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

    /* list of objects created in the previous iterations  */
    // [noRepeat, material, indice panel]
    private ArrayList<Object[]> listMaterialProdRepeat;
    private ArrayList<Object[]> listDataProdRepeat;

    /* list of selected params  */
    private ArrayList<Object> listCbSel;

    public TaskRepeatPanel(EdPPanel edP, ArrayList<InitialActionParam> listAllParams,ArrayList<Long> listDbKeyActionParam, ArrayList<InitialOutput> listOutput, ArrayList<Long> listDbKeyActionOutput, boolean isAction, boolean modeAdd) {
        this.edP = edP;
        this.listAllParams = listAllParams;
        this.listDbKeyActionParam = listDbKeyActionParam;
        this.listOutput = listOutput ;
        this.listDbKeyActionOutput = listDbKeyActionOutput;
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
        this.listMaterialProdRepeat = new ArrayList();
        this.listDataProdRepeat = new ArrayList();
        setTaskRepeatLabels();
        // case no param
        resizePanel();
    }

   /**
    *  ActionTaskRepeat.
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
            labelTaskRepeat.setBounds(0, 5,w, 14 );
        }
        return this.labelTaskRepeat;
    }

    private JSpinner getSpinnerNbRepeat(){
        if(this.spinnerNbRepeat == null){
            spinnerNbRepeat = new JSpinner();
            spinnerNbRepeat.setModel(new SpinnerNumberModel(nbRepeat,1,null,1));
            spinnerNbRepeat.setName("spinnerNbRepeat");
            spinnerNbRepeat.setBounds(labelTaskRepeat.getX()+labelTaskRepeat.getWidth()+10, 0, 40, 25);
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
            labelTaskRepeat2.setBounds(this.spinnerNbRepeat.getX()+spinnerNbRepeat.getWidth()+10, 5,w, 14 );
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
    }

    private void updatePanelParam(){
        int nb = this.listPanels.size();
        for (int i=0; i<nb; i++){
            ParamRepeatPanel table = getTableWithId(i);
            if(table != null){
                // modification of the nb columns in the table
                int oldNbC = table.getColumnCount() ;
                int newNbC = nbRepeat;
                if(oldNbC > newNbC){
                    int nbCToRemove = oldNbC - newNbC ;
                    table.removeColumns(nbCToRemove);
                }else{
                    // add columns
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
        changeCbSelection(cb);
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
            // remove previous selections
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
            // remove previsous selections
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
            list.add(listP.get(i).getParamName(edP.getLocale()));
        }
        nb = listO.size();
        for (int i=0; i<nb; i++){
            list.add(listO.get(i).getName(edP.getLocale()));
        }
        JComboBox cb = new JComboBox(list);
        cb.setName("comboBox");
        cb.setBounds(5, 10, 70, 20);
        cb.addItemListener(new ItemListener() {
            @Override
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
            changeCbSelection(cb);
        }
    }

    private void changeCbSelection(JComboBox cb){
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
                        List<CopexUnit> listUnit = ((InitialParamQuantity)p).getListUnit();
                        table.setParamQuantity((InitialParamQuantity)p, listUnit, ((InitialParamQuantity)p).getQuantityName(edP.getLocale()));
                        actionTaskRepeat.actionSetSelected(oldSel,(InitialParamQuantity)p);
                    }else if (p instanceof InitialParamMaterial){
                        ArrayList<Material> materials = edP.getListMaterial(((InitialParamMaterial)p).getTypeMaterial(), ((InitialParamMaterial)p).getTypeMaterial2(), ((InitialParamMaterial)p).isAndTypes(), modeAdd);
                        ArrayList<Object[]> listMaterialProdDuringRepeat = getListMaterialProdRepeat((InitialParamMaterial)p);
                        table.setParamMaterial((InitialParamMaterial)p, materials, listMaterialProdDuringRepeat);
                        actionTaskRepeat.actionSetSelected(oldSel,(InitialParamMaterial)p);
                    }else if (p instanceof InitialParamData){
                        ArrayList<QData> datas = edP.getDataProd( modeAdd);
                        table.setParamData((InitialParamData)p, datas);
                        actionTaskRepeat.actionSetSelected(oldSel,(InitialParamData)p);
                    }
                }else{
                    InitialOutput o = getSelectedOutput(id);
                    if (o != null){
                        if(o instanceof InitialManipulationOutput){
                            ArrayList<Material> listMat = edP.getMaterialProd(modeAdd);
                            ArrayList<TypeMaterial> listType = ((InitialManipulationOutput)o).getTypeMaterialProd();
                            ArrayList<Object[]> listMatRepeat = getListMaterialProdRepeat((InitialManipulationOutput)o);
                            table.setOutputMaterial((InitialManipulationOutput)o, listMat, listType, listMatRepeat);
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
                            table.setOutputData((InitialOutput)o, unit);
                        }
                    }
                }
            }
        }
        resizePanel();
        updateButtons();
        repaint();
    }

    /* returns index of the combo */
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
            buttonAddParam = new CopexButtonPanel(buttonAdd.getImage(), buttonAddSurvol.getImage(), buttonAddClic.getImage(), buttonAddDisabled.getImage());
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
            buttonRemoveParam = new CopexButtonPanel(buttonRemove.getImage(), buttonRemoveSurvol.getImage(), buttonRemoveClic.getImage(), buttonRemoveDisabled.getImage());
            buttonRemoveParam.addActionCopexButton(this);
            buttonRemoveParam.setToolTipText(edP.getBundleString("TOOLTIPTEXT_REMOVE_TASK_REPEAT_PARAM"));
            buttonRemoveParam.setBounds(buttonAddParam.getX()+buttonAddParam.getWidth()+5, buttonAddParam.getY(), 28,20);
        }
        return buttonRemoveParam ;
    }

     private ParamRepeatPanel getTableParam(int index, int x){
         ParamRepeatPanel p = new ParamRepeatPanel(edP,index, nbRepeat);
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
            if(panelParam != null){
                panelParam.removeAll();
                remove(panelParam);
            }
            labelModifyParam = null;
            listPanels = new ArrayList();
            listParamPanel = new ArrayList();
            panelParam = null;
            resizePanel();
        }
    }

    
    private void resizePanel(){
        int width = getWidth();
        int nb = this.listPanels.size();
        int height = 25;
        if(panelParam != null && buttonAddParam !=null){
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
        repaint();
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
            changeSpinnerValue(value);
        }
    }

    private void changeSpinnerValue(int value){
        if(value < 1){
            edP.displayError(new CopexReturn(edP.getBundleString("MSG_ERROR_NB_TASK_REPEAT"), false), edP.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        int oldR = nbRepeat;
        if(oldR==value)
            return;
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
    /* set disabled all combo box until id excluding */
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
        if(buttonAddParam== null || buttonRemoveParam ==null)
            return;
        int nb = listPanels.size();
        buttonRemoveParam.setEnabled(nb > 1);
        int nbTot = listAllParams.size()+listOutput.size();
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
    }

    /* returns true if without param  */
    private boolean isParam(){
        return this.listAllParams.size() > 0 || this.listOutput.size() > 0;
    }

    /* returns the nb of repetions  */
    public int getNbRepeat(){
        return (Integer)this.spinnerNbRepeat.getValue();
    }

    /* set value of a task repeat */
    public void setTaskRepeat(TaskRepeat taskRepeat){
        this.taskRepeat = taskRepeat;
        if(taskRepeat == null)
            return;
        //this.nbRepeat = taskRepeat.getNbRepeat();
        changeSpinnerValue(taskRepeat.getNbRepeat());
        this.nbRepeat = taskRepeat.getNbRepeat();
        this.spinnerNbRepeat.setValue(nbRepeat);
        if(nbRepeat > 1){
            ArrayList<TaskRepeatParam> listP = taskRepeat.getListParam();
            int nb = listP.size();
            for (int i=0; i<nb; i++){
                if(i > 0)
                    addParam();
                String name = "";
                if (listP.get(i) instanceof TaskRepeatParamData)
                    name = ((TaskRepeatParamData)listP.get(i)).getInitialParamData().getParamName(edP.getLocale()) ;
                else if (listP.get(i) instanceof TaskRepeatParamMaterial)
                    name = ((TaskRepeatParamMaterial)listP.get(i)).getInitialParamMaterial().getParamName(edP.getLocale()) ;
                else if (listP.get(i) instanceof TaskRepeatParamQuantity)
                    name = ((TaskRepeatParamQuantity)listP.get(i)).getInitialParamQuantity().getParamName(edP.getLocale()) ;
                else if (listP.get(i) instanceof TaskRepeatParamOutputAcquisition)
                    name = ((TaskRepeatParamOutputAcquisition)listP.get(i)).getOutput().getName(edP.getLocale()) ;
                else if (listP.get(i) instanceof TaskRepeatParamOutputManipulation)
                    name = ((TaskRepeatParamOutputManipulation)listP.get(i)).getOutput().getName(edP.getLocale()) ;
                else if (listP.get(i) instanceof TaskRepeatParamOutputTreatment)
                    name = ((TaskRepeatParamOutputTreatment)listP.get(i)).getOutput().getName(edP.getLocale()) ;
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

    /* set disabled tables */
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
        Object[] o = new Object[3];
        o[0] = noRepeat;
        o[1] = material;
        o[2] = index;
        this.listMaterialProdRepeat.add(o);
        updateListsMaterial(o);
    }

   

    @Override
    public void addOutputData(int index, int noRepeat, QData data) {
       Object[] o = new Object[3];
        o[0] = noRepeat;
        o[1] = data;
        o[2] = index;
        this.listDataProdRepeat.add(o);
        updateListsData(o);
    }

    private int getIdInitialParam(InitialActionParam param){
        return listParam.indexOf(param);
    }

    private int getIdInitialOutput(InitialOutput output){
        return listOutput.indexOf(output);
    }


    /* return the corresponding action th the selected parameter, -1 if it's an action */
    private long getCorrespondingAction(InitialActionParam param, InitialOutput output){
        if(listDbKeyActionParam == null && this.listDbKeyActionOutput == null )
            return -1;
        if(param != null){
            int id = getIdInitialParam(param);
            if(id != -1){
                return listDbKeyActionParam.get(id);
            }else
                return -1;
        }
        if(output != null){
            int id = getIdInitialOutput(output);
            if(id != -1){
                return listDbKeyActionOutput.get(id);
            }else
                return -1;
        }
        return -1;
    }

    // returns the repeat task
    public CopexReturn  getTaskRepeat(ArrayList v){
        long dbKey = -1;
        if (taskRepeat != null)
            dbKey = taskRepeat.getDbKey();
        ArrayList<TaskRepeatParam> listP = new ArrayList();
        int nb = listPanels.size() ;
        String msg = "";
        for (int i=0; i<nb; i++){
            boolean none = getSelectedNone(i);
            if (!none){
                InitialActionParam param = getSelectedParam(i);
                InitialOutput output = getSelectedOutput(i);
                long dbKeyAction = getCorrespondingAction(param, output);
                if (param == null){
                    if(output instanceof InitialManipulationOutput){
                        ArrayList<TaskRepeatValueMaterialProd> listValue= new ArrayList();
                        ParamRepeatPanel valuePanel = getTableWithId(i);
                        if (valuePanel != null){
                            ArrayList v2 = new ArrayList();
                            CopexReturn cr = valuePanel.getOutputMaterial(v2);
                            if (cr.isError()){
                                msg += cr.getText();
                            }else{
                                ArrayList<Material> list = (ArrayList<Material>)v2.get(0);
                                int nbV = list.size();
                                for (int k=0; k<nbV; k++){
                                    TaskRepeatValueMaterialProd value = new TaskRepeatValueMaterialProd(-1, k, list.get(k));
                                    listValue.add(value);
                                }
                            }
                        }
                        TaskRepeatParamOutputManipulation out = new TaskRepeatParamOutputManipulation(-1, dbKeyAction, (InitialManipulationOutput)output, listValue);
                        listP.add(out);
                    }else if (output instanceof InitialAcquisitionOutput){
                        ArrayList<TaskRepeatValueDataProd> listValue= new ArrayList();
                        ParamRepeatPanel valuePanel = getTableWithId(i);
                        if (valuePanel != null){
                            ArrayList v2 = new ArrayList();
                            CopexReturn cr = valuePanel.getOutputData(v2);
                            if(cr.isError()){
                                msg += cr.getText();
                            }else{
                                ArrayList<QData> list = (ArrayList<QData>)v2.get(0);
                                int nbV = list.size();
                                for (int k=0; k<nbV; k++){
                                    TaskRepeatValueDataProd value = new TaskRepeatValueDataProd(-1, k, list.get(k));
                                    listValue.add(value);
                                }
                            }
                        }
                        TaskRepeatParamOutputAcquisition out = new TaskRepeatParamOutputAcquisition(-1,dbKeyAction,  (InitialAcquisitionOutput)output, listValue);
                        listP.add(out);
                    }else if (output instanceof InitialTreatmentOutput){
                        ArrayList<TaskRepeatValueDataProd> listValue= new ArrayList();
                        ParamRepeatPanel valuePanel = getTableWithId(i);
                        if (valuePanel != null){
                            ArrayList v2 = new ArrayList();
                            CopexReturn cr = valuePanel.getOutputData(v2);
                            if(cr.isError()){
                                msg += cr.getText();
                            }else{
                                ArrayList<QData> list = (ArrayList<QData>)v2.get(0);
                                int nbV = list.size();
                                for (int k=0; k<nbV; k++){
                                    TaskRepeatValueDataProd value = new TaskRepeatValueDataProd(-1, k, list.get(k));
                                    listValue.add(value);
                                }
                            }
                        }
                        TaskRepeatParamOutputTreatment out = new TaskRepeatParamOutputTreatment(-1, dbKeyAction, (InitialTreatmentOutput)output, listValue);
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
                        TaskRepeatParamData p = new TaskRepeatParamData(-1,dbKeyAction,  (InitialParamData)param, listValue);
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
                        TaskRepeatParamMaterial p = new TaskRepeatParamMaterial(-1, dbKeyAction, (InitialParamMaterial)param, listValue);
                        listP.add(p);
                    }else if (param instanceof InitialParamQuantity){
                        ArrayList<TaskRepeatValueParamQuantity> listValue = new ArrayList();
                        ParamRepeatPanel valuePanel = getTableWithId(i);
                        if (valuePanel != null){
                            ArrayList v2 = new ArrayList();
                            CopexReturn cr  = valuePanel.getParamQuantity(v2);
                            if(cr.isError()){
                                msg += cr.getText();
                            }else{
                                ArrayList<Parameter> list = (ArrayList<Parameter>)v2.get(0);
                                int nbV = list.size();
                                for (int k=0; k<nbV; k++){
                                    ActionParamQuantity actionParamQuantity = new ActionParamQuantity(-1,(InitialParamQuantity)param , list.get(k));
                                    TaskRepeatValueParamQuantity value = new TaskRepeatValueParamQuantity(-1, k, actionParamQuantity);
                                    listValue.add(value);
                                }
                            }
                        }
                        TaskRepeatParamQuantity p = new TaskRepeatParamQuantity(-1, dbKeyAction, (InitialParamQuantity)param, listValue);
                        listP.add(p);
                    }
                }
            }
        }
        if(msg.equals("")){
            taskRepeat = new TaskRepeat(dbKey, nbRepeat, listP);
            v.add(taskRepeat);
            return new CopexReturn();
        }else{
            return new CopexReturn(msg, false);
        }
    }

    /* returns the list of material prod during the repetion */
    private ArrayList<Object[]> getListMaterialProdRepeat(InitialParamMaterial p){
        ArrayList<Object[]> list = new ArrayList();
        int nb = this.listMaterialProdRepeat.size();
        for (int i=0; i<nb; i++){
            Material m = (Material)this.listMaterialProdRepeat.get(i)[1];
            if(p.canAccept(m)){
                list.add(this.listMaterialProdRepeat.get(i));
            }
        }
        return list;
    }

    /* returns the list of material prod during the repetition */
    private ArrayList<Object[]> getListMaterialProdRepeat(InitialManipulationOutput o){
        ArrayList<Object[]> list = new ArrayList();
        int nb = this.listMaterialProdRepeat.size();
        for (int i=0; i<nb; i++){
            Material m = (Material)this.listMaterialProdRepeat.get(i)[1];
            if(o.canAccept(m)){
                list.add(this.listMaterialProdRepeat.get(i));
            }
        }
        return list;
    }

    /* update the list of new material */
    private void updateListsMaterial(Object[] o){
        int nb = this.listPanels.size();
        for (int i=0; i<nb; i++){
            ParamRepeatPanel table = getTableWithId(i);
            if(table != null){
                if(table.getInitialParamMaterial() != null){
                    Material m  = (Material)o[1];
                    if(table.getInitialParamMaterial().canAccept(m))
                        table.updateMaterial(o);
                }else if(table.getInitialManipulationOutput() != null){
                    Material m  = (Material)o[1];
                    if(table.getInitialManipulationOutput().canAccept(m))
                        table.updateMaterialOutput(o);
                }
            }
        }
    }

    /* update the list for a new data  */
    private void updateListsData(Object[] o){
        int nb = this.listPanels.size();
//        for (int i=0; i<nb; i++){
//            ParamRepeatPanel table = getTableWithId(i);
//            if(table != null){
//                if(table.getInitialParamData() != null){
//                    QData d  = (QData)o[1];
//                    if(table.getInitialParamData().canAccept(d))
//                        table.updateData(o);
//                }else if(table.getInitialManipulationOutput() != null){
//                    Material m  = (Material)o[1];
//                    if(table.getInitialManipulationOutput().canAccept(m))
//                        table.updateMaterialOutput(o);
//                }
//            }
//        }
    }



    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

}
