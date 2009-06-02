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
import eu.scy.tools.copex.common.InitialActionParam;
import eu.scy.tools.copex.utilities.ActionCopexButton;
import eu.scy.tools.copex.utilities.CopexButtonPanel;
import eu.scy.tools.copex.utilities.CopexReturn;
import eu.scy.tools.copex.utilities.CopexUtilities;
import eu.scy.tools.copex.utilities.ParamTableModel;
import eu.scy.tools.copex.utilities.ParamTable;
import eu.scy.tools.copex.utilities.ParamTableModel;
import java.awt.Component;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * panel repeat task
 * @author Marjolaine
 */
public class TaskRepeatPanel extends javax.swing.JPanel implements ActionCopexButton {

    private final static int HEIGHT_PANEL_PARAM = 40;
    /* edp*/
    private EdPPanel edP;
    /*liste des parametres */
    private ArrayList<InitialActionParam> listAllParams;
    /* action ou etape */
    private boolean isAction;
    /* mode edition */
    private boolean editMode;
    /* actions */
    private ActionTaskRepeat actionTaskRepeat;

    /* nb repeat */
    private int nbRepeat;
    /* liset des paramétres à faire varier */
    private ArrayList<InitialActionParam> listParam;
    /* liste des valeurs pour chaque itération */
    private ArrayList<ActionParam[]> listValueParam;

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

    public TaskRepeatPanel(EdPPanel edP, ArrayList<InitialActionParam> listAllParams, boolean isAction) {
        this.edP = edP;
        this.listAllParams = listAllParams;
        this.isAction = isAction;
        this.editMode = false;
        this.nbRepeat = 1;
        this.listParam = new ArrayList();
        this.listValueParam = new ArrayList();
        initComponents();
        initGUI();
    }

    public TaskRepeatPanel(EdPPanel edP, ArrayList<InitialActionParam> listAllParams, boolean isAction, int nbRepeat, ArrayList<InitialActionParam> listParam, ArrayList<ActionParam[]> listValueParam) {
        this.edP = edP;
        this.listAllParams = listAllParams;
        this.isAction = isAction;
        this.nbRepeat = nbRepeat;
        this.listParam = listParam;
        this.listValueParam = listValueParam;
        this.editMode = true;
        initComponents();
        initGUI();
    }

    private void initGUI(){
        setLayout(null);
        this.listPanels = new ArrayList();
        setTaskRepeatLabels();
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
        getPanelParam();
        panelParam.add(getLabelModifyParam());
        if(editMode){

        }else{
            this.panelParam.add(getScrollPaneParam());
            this.panelRepeatParam.add(getPanelActionParam(0));
            panelRepeatParam.revalidate();
        }
        panelParam.add(getButtonAddParam());
        panelParam.add(getButtonRemoveParam());
        this.add(panelParam);
        resizePanel();
    }

    private void updatePanelParam(){
        int nb = this.listPanels.size();
        for (int i=0; i<nb; i++){
            ParamTable table = getTableWithId(i);
            if(table != null){
                // modifcation du nombre de colonnes de la table
                int oldNbC = table.getColumnCount() ;
                int newNbC = nbRepeat;
                if(oldNbC > newNbC){
                    int nbCToRemove = oldNbC - newNbC ;
                    table.getTableModel().removeColumns(nbCToRemove);
                }else{
                    // ajoute des colonnes
                    int nbCToAdd = newNbC - oldNbC ;
                    table.getTableModel().addColumns(nbCToAdd);
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
        panel.setLayout(new FlowLayout());
        panel.add(getCbParam(getListParam(id)));
        panel.add(getLabelValue());
        panel.add(getTableValue());
        panel.setBounds(0, ((id+1)*20+id*HEIGHT_PANEL_PARAM ), 300, HEIGHT_PANEL_PARAM);
        this.listPanels.add(panel);
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
                InitialActionParam p = getSelectedParam(id);
                if(p != null)
                    list.remove(p);
            }
            return list;
        }
    }

    private JComboBox getCbParam(ArrayList<InitialActionParam> listP){
        Vector list = new Vector();
        int nb = listP.size();
        for (int i=0; i<nb; i++){
            list.add(listP.get(i).getParamName());
        }
        JComboBox cb = new JComboBox(list);
        cb.setName("comboBox");
        return cb;
    }

    private JLabel getLabelValue(){
        JLabel label = new JLabel(edP.getBundleString("LABEL_PARAM_VALUE"));
        label.setFont(new java.awt.Font("Tahoma", 1, 11));
        label.setName("labelValue");
        int w = CopexUtilities.lenghtOfString(label.getText(), getFontMetrics(label.getFont()));
        label.setSize(w, 14 );
        return label;
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
            System.out.println("getButtonAddParam : "+y);
            buttonAddParam.setBounds(10, y, buttonAddParam.getWidth(), buttonAddParam.getHeight());
        }
        return buttonAddParam ;
    }

     private CopexButtonPanel getButtonRemoveParam(){
        if(this.buttonRemoveParam == null){
            ImageIcon buttonRemove = edP.getCopexImage("Bouton-onglet_ouverture.png");
            ImageIcon buttonRemoveSurvol = edP.getCopexImage("Bouton-onglet_ouverture_sur.png");
            ImageIcon buttonRemoveClic = edP.getCopexImage("Bouton-onglet_ouverture_cli.png");
            ImageIcon buttonRemoveDisabled = edP.getCopexImage("Bouton-onglet_ouverture_grise.png");
            buttonRemoveParam = new CopexButtonPanel(edP, 28, buttonRemove.getImage(), buttonRemoveSurvol.getImage(), buttonRemoveClic.getImage(), buttonRemoveDisabled.getImage());
            buttonRemoveParam.addActionCopexButton(this);
            buttonRemoveParam.setToolTipText(edP.getBundleString("TOOLTIPTEXT_REMOVE_TASK_REPEAT_PARAM"));
            buttonRemoveParam.setBounds(buttonAddParam.getX()+buttonAddParam.getWidth()+10, buttonAddParam.getY(), buttonRemoveParam.getWidth(), buttonRemoveParam.getHeight());
        }
        return buttonAddParam ;
    }

     private ParamTable getTableValue(){
         Object[] datas = new Object[nbRepeat];
         for (int i=0; i<nbRepeat; i++){
             datas[i] = "";
         }
         ParamTableModel dm = new ParamTableModel(datas);
         ParamTable table = new ParamTable(dm);
         table.setName("table");
         return table;
     }

    private void deletePanelParam(){
        panelParam.removeAll();
        labelModifyParam = null;
        listPanels = new ArrayList();
        remove(panelParam);
        panelParam = null;
        resizePanel();
    }

    private void resizePanel(){
        int width = getWidth();
        int nb = this.listPanels.size();
        int height = 20;
        if(panelParam != null){
            int h1 = nb*HEIGHT_PANEL_PARAM+20*(nb+2);
            int h = h1+buttonAddParam.getHeight()+labelModifyParam.getHeight()+10;
            panelParam.setSize(width, h);
            height += h;

            if(panelRepeatParam != null){
                int w = width+100;
                
                panelRepeatParam.setSize(w, h1);
                for (int i=0; i<listPanels.size(); i++){
                    listPanels.get(i).setSize(w, listPanels.get(i).getHeight());
                }
            }
            scrollPaneParam.setSize(width, h1);
        }
        revalidate();
        setSize(width, height);
        setPreferredSize(getSize());
        if(actionTaskRepeat != null)
            actionTaskRepeat.actionResize();
    }

    

    private InitialActionParam getSelectedParam(int id){
        return (InitialActionParam)getCbWithId(id).getSelectedItem();
    }

    private JComboBox getCbWithId(int id){
        Component[] allC = this.listPanels.get(id).getComponents();
        for (int i=0; i<allC.length; i++){
            if (allC[i].getName().equals("comboBox"))
                return (JComboBox)allC[i];
        }
        return null;
    }
    private ParamTable getTableWithId(int id){
        Component[] allC = this.listPanels.get(id).getComponents();
        for (int i=0; i<allC.length; i++){
            if (allC[i].getName().equals("table"))
                return (ParamTable)allC[i];
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
        throw new UnsupportedOperationException("Not supported yet.");
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

}
