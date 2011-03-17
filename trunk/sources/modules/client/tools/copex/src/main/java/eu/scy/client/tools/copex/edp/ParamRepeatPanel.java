/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.edp;

import eu.scy.client.tools.copex.common.CopexUnit;
import eu.scy.client.tools.copex.common.InitialManipulationOutput;
import eu.scy.client.tools.copex.common.InitialOutput;
import eu.scy.client.tools.copex.common.InitialParamData;
import eu.scy.client.tools.copex.common.InitialParamMaterial;
import eu.scy.client.tools.copex.common.InitialParamQuantity;
import eu.scy.client.tools.copex.common.Material;
import eu.scy.client.tools.copex.common.MaterialSourceAction;
import eu.scy.client.tools.copex.common.Parameter;
import eu.scy.client.tools.copex.common.QData;
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
import eu.scy.client.tools.copex.utilities.ActionSelectParamTaskRepeat;
import eu.scy.client.tools.copex.utilities.CopexReturn;
import eu.scy.client.tools.copex.utilities.CopexUtilities;
import eu.scy.client.tools.copex.utilities.MyConstants;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

/**
 * panel qui contient les valeurs des parametres a repeter
 * @author Marjolaine
 */
public class ParamRepeatPanel extends JPanel{
    private final static int MODE_PARAM_QTT = 0 ;
    private final static int MODE_PARAM_MAT = 1;
    private final static int MODE_PARAM_DATA = 2;
    private final static int MODE_OUT_MAT = 3;
    private final static int MODE_OUT_DATA = 4;
    private final static int MODE_NONE = 5;

    private EdPPanel edP;
    private int index;
    private int nbRepeat;
    private int mode;
    private ArrayList<JPanel> listComponents;

    private InitialParamMaterial initialParamMaterial;
    private InitialParamQuantity initialParamQuantity;
    private InitialParamData initialParamData;
    private InitialManipulationOutput initialManipulationOutput;
    private InitialOutput initialOutput;


    private ArrayList<Material> listParamMaterial;
    private List<CopexUnit> listParamUnit;
    private String quantityName;
    private ArrayList<QData> listParamData;
    private ArrayList<Material> listOutMaterial;
    private List<TypeMaterial> listOutTypeMaterial;
    private CopexUnit outputUnit;

    private ArrayList<Object[]> listMaterialProdRepeat;
    private ArrayList<Object[]> listDataProdRepeat;

    private ActionSelectParamTaskRepeat actionTaskRepeat;


    public ParamRepeatPanel(EdPPanel edP,int index, int nbRepeat) {
        super();
        this.edP = edP;
        this.index = index;
        this.nbRepeat = nbRepeat ;
        this.listComponents = new ArrayList();
        this.mode = MODE_NONE ;
        initGUI();
    }


    private void initGUI(){
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBorder(new LineBorder(Color.BLACK));
    }

    public void addActionSelectParamTaskRepeat(ActionSelectParamTaskRepeat action){
        this.actionTaskRepeat = action;
    }

    
    public void setMode(int mode) {
        this.mode = mode;
        this.initialParamMaterial = null;
        this.initialParamQuantity = null;
        this.initialParamData = null;
        this.initialManipulationOutput = null;
        this.initialOutput = null;
    }

    public InitialParamMaterial getInitialParamMaterial(){
        return this.initialParamMaterial;
    }

    public InitialParamData getInitialParamData() {
        return initialParamData;
    }

    public InitialParamQuantity getInitialParamQuantity() {
        return initialParamQuantity;
    }

    public InitialManipulationOutput getInitialManipulationOutput() {
        return initialManipulationOutput;
    }

    public InitialOutput getInitialOutput() {
        return initialOutput;
    }
    
    // rend disabled tous les elements
    public void disabled(){
        int nb = listComponents.size();
        for (int i=0; i<nb; i++){

        }
    }

    /* nombre de "colonnes" => nbRepeat */
    public int getColumnCount(){
        return this.nbRepeat;
    }

    /* supprime des "colonnes" */
    public void removeColumns(int nb){
        if(listComponents.size() > 0){
            for (int i=0; i<nb; i++){
                int id =nbRepeat-i-1;
                JPanel p = listComponents.get(id);
                if(p != null)
                    this.remove(p);
                listComponents.remove(id);
            }
        }
        this.nbRepeat = nbRepeat - nb;
        resizePanel();
    }

    /*ajoute des colonnes */
    public void addColumns(int nb){
        Vector listItem = new Vector();
        int n = 0;
        switch (mode){
            case MODE_PARAM_QTT :
                for (int i=0; i<nb; i++){
                    listItem.add(listParamUnit.get(i).getSymbol(edP.getLocale()));
                }
                for (int i=0; i<nb; i++){
                    addParamQuantity(listItem);
                }
                break;
            case MODE_PARAM_MAT :
                n = listParamMaterial.size();
                for (int i=0; i<n; i++){
                    listItem.add(listParamMaterial.get(i).getName(edP.getLocale()));
                }
                for (int i=0; i<nb; i++){
                    addParamMaterial(listItem);
                }
                break;
            case MODE_PARAM_DATA :
                n = listParamData.size();
                for (int i=0; i<n; i++){
                    listItem.add(listParamData.get(i).getName(edP.getLocale()));
                }
                for (int i=0; i<nb; i++){
                    addParamData(listItem);
                }
                break;
            case MODE_OUT_MAT :
                n = listOutMaterial.size();
                for (int i=0; i<n; i++){
                    listItem.add(listOutMaterial.get(i).getName(edP.getLocale()));
                }
                for (int i=0; i<nb; i++){
                    addOutputMaterial(listItem);
                }
                break;
            case MODE_OUT_DATA :
                for (int i=0; i<nb; i++){
                    addOutputData();
                }
                break;

        }
        this.nbRepeat += nb ;
        resizePanel();
    }

    /*n'affiche rien */
    public void setNone(){
        setMode(MODE_NONE) ;
        this.removeAll();
        this.listComponents = new ArrayList();
        resizePanel();
    }

    /* affichage param quantity : text fields + cb des unites*/
    public void setParamQuantity(InitialParamQuantity initialParamQuantity, List<CopexUnit> listUnit, String quantityName){
        setNone();
        setMode(MODE_PARAM_QTT);
        this.listParamUnit = listUnit ;
        this.initialParamQuantity = initialParamQuantity;
        this.quantityName = quantityName;
        int nb = listUnit.size();
        Vector listItem = new Vector();
        for (int i=0; i<nb; i++){
            listItem.add(listUnit.get(i).getSymbol(edP.getLocale()));
        }
        for (int i=0; i<nbRepeat; i++){
            addParamQuantity(listItem);
        }
        resizePanel();
    }

    private void addParamQuantity(Vector listItem){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        JTextField tf = new JTextField("");
        tf.setSize(60, 25);
        tf.setPreferredSize(new Dimension(60,25));
        panel.add(tf);
        JComboBox cb = new JComboBox(listItem);
        cb.setSize(40, 25);
        panel.add(cb);
        JSeparator sep = new JSeparator(SwingConstants.VERTICAL);
        sep.setBackground(Color.BLACK);
        sep.setSize(5, 25);
        panel.add(sep);
        this.add(panel);
        this.listComponents.add(panel);
    }

    public CopexReturn getParamQuantity(ArrayList v){
        ArrayList<Parameter> list = new ArrayList();
        if (mode == MODE_PARAM_QTT){
            int nb = listComponents.size();
            for (int i=0; i<nb; i++){
                String s = ((JTextField)listComponents.get(i).getComponent(0)).getText();
                double value = 0;
                try{
                    value = Double.parseDouble(s.replace(',', '.'));
                }catch(NumberFormatException e){
                    return new CopexReturn(edP.getBundleString("MSG_ERROR_ACTION_PARAM_VALUE"), false);
                }
                int id = ((JComboBox)listComponents.get(i).getComponent(1)).getSelectedIndex();
                CopexUnit unit = this.listParamUnit.get(id);
                Parameter p = new Parameter(-1,CopexUtilities.getLocalText(quantityName, edP.getLocale()),CopexUtilities.getLocalText("", edP.getLocale()), value ,CopexUtilities.getLocalText("", edP.getLocale()), unit);
                list.add(p);
            }
        }
        v.add(list);
        return new CopexReturn();
    }


    /* affichage param materials  combo box*/
    public void setParamMaterial(InitialParamMaterial initialParamMaterial, ArrayList<Material> listMaterial, ArrayList<Object[]> listMaterialProdRepeat){
        setNone();
        this.setMode(MODE_PARAM_MAT);
        this.initialParamMaterial = initialParamMaterial;
        this.listParamMaterial = listMaterial ;
        this.listMaterialProdRepeat = listMaterialProdRepeat;
        Vector listItem = new Vector();
        int nb = listMaterial.size();
        for (int i=0; i<nb; i++){
            listItem.add(listMaterial.get(i).getName(edP.getLocale()));
        }
        nb = listMaterialProdRepeat.size();

        for (int i=0; i<nbRepeat; i++){
            for (int j=0; j<nb; j++){
                int idR = (Integer)(((Object[])listMaterialProdRepeat.get(j))[0]);
                if(idR >= i){
                    listItem.add(((Material)(((Object[])listMaterialProdRepeat.get(j))[1])).getName(edP.getLocale()));
                }
            }
            addParamMaterial(listItem);
        }
        resizePanel();
    }

    private void addParamMaterial(Vector listItem){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        JComboBox cb = new JComboBox(listItem);
        //cb.setSize(60, 20);
        panel.add(cb);
        JSeparator sep = new JSeparator(SwingConstants.VERTICAL);
        sep.setBackground(Color.BLACK);
        sep.setSize(5, 25);
        panel.add(sep);
        this.add(panel);
        this.listComponents.add(panel);
    }

    public void updateMaterial(Object[] o){
        this.listMaterialProdRepeat.add(o);
        int id = (Integer)o[0];
        Material m = (Material)o[1];
        int nb = listComponents.size();
        int start = id+1;
        for (int i=start; i<nb; i++){
            ((JComboBox)listComponents.get(i).getComponent(0)).addItem(m.getName(edP.getLocale()));
        }
        revalidate();
    }

    public ArrayList<Material> getParamMaterial(){
        ArrayList<Material> list = new ArrayList();
        int nbMat = this.listParamMaterial.size();
        int nbMatProd = this.listMaterialProdRepeat.size();
        if (mode == MODE_PARAM_MAT){
            int nb = listComponents.size();
            for (int i=0; i<nb; i++){
                int id = ((JComboBox)listComponents.get(i).getComponent(0)).getSelectedIndex();
                Material m = null;
                if(id < nbMat)
                    m = this.listParamMaterial.get(id);
                else{
                    int q=0;
                    for (int j=0; j<nbMatProd; j++){
                      int idR = (Integer)(((Object[])listMaterialProdRepeat.get(j))[0]);
                      if(idR >= i){
                          if(q == (id-nbMat)){
                              m = (Material)(((Object[])listMaterialProdRepeat.get(j))[1]);
                              break;
                          }
                          q++;

                      }
                    }
                }
                list.add(m);
            }
        }
        return list;
    }

    /* affichage param data  combo box*/
    public void setParamData(InitialParamData initialParamData, ArrayList<QData> listData){
        setNone();
        setMode(MODE_PARAM_DATA);
        this.listParamData = listData ;
        this.initialParamData = initialParamData;
        Vector listItem = new Vector();
        int nb = listData.size();
        for (int i=0; i<nb; i++){
            listItem.add(listData.get(i).getName(edP.getLocale()));
        }
        for (int i=0; i<nbRepeat; i++){
            addParamData(listItem);
        }
        resizePanel();
    }

    private void addParamData(Vector listItem){
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
            JComboBox cb = new JComboBox(listItem);
            cb.setEditable(true);
            //cb.setSize(60, 20);
            panel.add(cb);
            JSeparator sep = new JSeparator(SwingConstants.VERTICAL);
            sep.setBackground(Color.BLACK);
            sep.setSize(5, 25);
            panel.add(sep);
            this.add(panel);
            this.listComponents.add(panel);
    }

    public ArrayList<QData> getParamData(){
        ArrayList<QData> list = new ArrayList();
        if (mode == MODE_PARAM_DATA){
            int nb = listComponents.size();
            for (int i=0; i<nb; i++){
                int id = ((JComboBox)listComponents.get(i).getComponent(0)).getSelectedIndex();
                list.add(this.listParamData.get(id));
            }
        }
        return list;
    }

    /* affichage output material : combobox*/
    public void setOutputMaterial(InitialManipulationOutput initialManipulationOutput, ArrayList<Material> listOutput, ArrayList<TypeMaterial> listType, ArrayList<Object[]> listMaterialProdRepeat){
        setNone();
        this.setMode(MODE_OUT_MAT);
        this.listOutMaterial = listOutput;
        this.initialManipulationOutput = initialManipulationOutput;
        this.listOutTypeMaterial = listType;
        this.listMaterialProdRepeat = listMaterialProdRepeat;
        Vector listItem = new Vector();
        int nb = listOutput.size();
        for (int i=0; i<nb; i++){
            listItem.add(listOutput.get(i).getName(edP.getLocale()));
        }
        for (int i=0; i<nbRepeat; i++){
            for (int j=0; j<nb; j++){
                int idR = (Integer)(((Object[])listMaterialProdRepeat.get(j))[0]);
                if(idR >= i){
                    listItem.add(((Material)(((Object[])listMaterialProdRepeat.get(j))[1])).getName(edP.getLocale()));
                }
            }
            addOutputMaterial(listItem);
        }
        resizePanel();
    }

    private void addOutputMaterial(Vector listItem){
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
            JComboBox cb = new JComboBox(listItem);
            //cb.setSize(60, 20);
            cb.setEditable(true);
            cb.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent evt) {
                    changeCbOutputMaterialSelection(evt);
                }
            });
            panel.add(cb);
            JSeparator sep = new JSeparator(SwingConstants.VERTICAL);
            sep.setBackground(Color.BLACK);
            sep.setSize(5, 25);
            panel.add(sep);
            this.add(panel);
            this.listComponents.add(panel);
    }

    public void updateMaterialOutput(Object[] o){
        this.listMaterialProdRepeat.add(o);
        int id = (Integer)o[0];
        Material m = (Material)o[1];
        int nb = listComponents.size();
        int start = id+1;
        for (int i=start; i<nb; i++){
            ((JComboBox)listComponents.get(i).getComponent(0)).addItem(m.getName(edP.getLocale()));
        }
        revalidate();
    }

    public CopexReturn getOutputMaterial(ArrayList v){
        ArrayList<Material> list = new ArrayList();
        if( mode == MODE_OUT_MAT){
            int nb = listComponents.size();
            for (int i=0; i<nb; i++){
                String s = (String)((JComboBox)listComponents.get(i).getComponent(0)).getSelectedItem();
                if (s == null || s.length() == 0){
                    String msg = edP.getBundleString("MSG_ERROR_FIELD_NULL");
                    msg  = CopexUtilities.replace(msg, 0, initialManipulationOutput.getName(edP.getLocale()));
                    return new CopexReturn(msg ,false);
                }
                if (s.length() > MyConstants.MAX_LENGHT_MATERIAL_NAME){
                    String msg = edP.getBundleString("MSG_LENGHT_MAX");
                    msg  = CopexUtilities.replace(msg, 0, initialManipulationOutput.getName(edP.getLocale()));
                    msg = CopexUtilities.replace(msg, 1, ""+MyConstants.MAX_LENGHT_MATERIAL_NAME);
                    return new CopexReturn(msg, false);
                }

                Material m = getMaterialOutput(s);
                if (m == null){
                    List<Parameter> listParameters = new LinkedList();
                    m = new Material(CopexUtilities.getLocalText(s, edP.getLocale()), CopexUtilities.getLocalText(s, edP.getLocale()), null,  listOutTypeMaterial, listParameters, new MaterialSourceAction(-1));
                }
                list.add(m);
            }
        }
        v.add(list);
        return new CopexReturn();
    }


    private void changeCbOutputMaterialSelection(ItemEvent evt){
        String matName = (String)((JComboBox)evt.getSource()).getSelectedItem();
        int idCb = getComboBoxId((JComboBox)evt.getSource());
        if(idCb == -1)
            return;
        if(idCb >-1 && actionTaskRepeat != null && !isMaterialOutput(matName)){
            // liste des parametres
            List<Parameter> listParameters = new LinkedList();
            Material m = new Material(CopexUtilities.getLocalText(matName, edP.getLocale()), CopexUtilities.getLocalText(matName, edP.getLocale()), null, listOutTypeMaterial, listParameters, new MaterialSourceAction(-1));
            actionTaskRepeat.addOutputMaterial(index,idCb, m);
        }
    }

    private int getComboBoxId(JComboBox cb){
        int nb = listComponents.size();
        for (int i=0; i<nb; i++){
            if (listComponents.get(i).getComponent(0) instanceof JComboBox && ((JComboBox)listComponents.get(i).getComponent(0)).equals(cb)){
                return i;

            }
        }
        return -1;
    }
    private int getFieldId(JTextField f){
        int nb = listComponents.size();
        for (int i=0; i<nb; i++){
            if (listComponents.get(i).getComponent(0) instanceof JTextField && ((JTextField)listComponents.get(i).getComponent(0)).equals(f)){
                return i;

            }
        }
        return -1;
    }

    private boolean isMaterialOutput(String name){
        int nb = listOutMaterial.size();
        for (int i=0; i<nb; i++){
            if (listOutMaterial.get(i).getName(edP.getLocale()).equals(name))
                return true;
        }
        return false;
    }
    private Material getMaterialOutput(String name){
        int nb = listOutMaterial.size();
        for (int i=0; i<nb; i++){
            if (listOutMaterial.get(i).getName(edP.getLocale()).equals(name))
                return listOutMaterial.get(i);
        }
        nb = this.listMaterialProdRepeat.size();
        for (int i=0; i<nb; i++){
            if(((Material)listMaterialProdRepeat.get(i)[1]).getName(edP.getLocale()).equals(name))
                return (Material)listMaterialProdRepeat.get(i)[1];
        }
        return null;
    }

    /* affichage output data : textfied */
    public void setOutputData(InitialOutput initialOutput, CopexUnit unit){
        setNone();
        this.outputUnit = unit;
        this.initialOutput = initialOutput;
        setMode(MODE_OUT_DATA);
        for (int i=0; i<nbRepeat; i++){
            addOutputData();
        }
        resizePanel();
    }

    private void addOutputData(){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        JTextField field = new JTextField();
        field.setSize(60, 25);
        field.setPreferredSize(new Dimension(60,25));
        panel.add(field);
        field.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                 changeOutputData(e);
            }
            });
        JSeparator sep = new JSeparator(SwingConstants.VERTICAL);
        sep.setBackground(Color.BLACK);
        sep.setSize(5, field.getHeight());
        panel.add(sep);
        this.add(panel);
        this.listComponents.add(panel);
    }

    public CopexReturn getOutputData(ArrayList v){
        ArrayList<QData> list = new ArrayList();
        if (mode == MODE_OUT_DATA){
            int nb = listComponents.size();
            for (int i=0; i<nb; i++){
                String s = ((JTextField)listComponents.get(i).getComponent(0)).getText();
                if (s.length() == 0){
                    String msg = edP.getBundleString("MSG_ERROR_FIELD_NULL");
                    msg  = CopexUtilities.replace(msg, 0, initialOutput.getName(edP.getLocale()));
                    return new CopexReturn(msg ,false);
                }
                if (s.length() > MyConstants.MAX_LENGHT_QUANTITY_NAME){
                    String msg = edP.getBundleString("MSG_LENGHT_MAX");
                    msg  = CopexUtilities.replace(msg, 0, initialOutput.getName(edP.getLocale()));
                    msg = CopexUtilities.replace(msg, 1, ""+MyConstants.MAX_LENGHT_QUANTITY_NAME);
                    return new CopexReturn(msg, false);
                }
                QData data = new QData(-1,CopexUtilities.getLocalText(s, edP.getLocale()), CopexUtilities.getLocalText("", edP.getLocale()), 0, CopexUtilities.getLocalText(s, edP.getLocale()), outputUnit);
                list.add(data);
            }
        }
        v.add(list);
        return new CopexReturn();
    }


    private void changeOutputData(ActionEvent evt){
        String name = ((JTextField)evt.getSource()).getText();
        int idF = getFieldId((JTextField)evt.getSource());
        if (idF != -1  && actionTaskRepeat != null){
            QData data = new QData(-1, CopexUtilities.getLocalText(name, edP.getLocale()), CopexUtilities.getLocalText("", edP.getLocale()), 0, CopexUtilities.getLocalText(name, edP.getLocale()), outputUnit);
            actionTaskRepeat.addOutputData(index, idF, data);
        }
    }



    private void resizePanel(){
        int width = 0;
        int nb = listComponents.size();
        for (int i=0; i<nb; i++){
            JPanel p = listComponents.get(i);
            Component[] tab = p.getComponents();
            for (int j=0; j<tab.length; j++){
                width+= tab[j].getPreferredSize().getWidth();
            }
        }
        setSize(width, 25);
        setPreferredSize(getSize());
        revalidate();
        repaint();
    }

    public void setTaskRepeatParam(TaskRepeatParam param){
        NumberFormat numberFormat = NumberFormat.getNumberInstance(getLocale());
        numberFormat.setMaximumFractionDigits(Integer.MAX_VALUE);
        numberFormat.setGroupingUsed(false);
        if(param instanceof TaskRepeatParamData && mode == MODE_PARAM_DATA){
            ArrayList<TaskRepeatValueParamData> listValue = ((TaskRepeatParamData)param).getListValue();
            int nb = listValue.size();
            for (int i=0; i<nb; i++){
                ((JComboBox)(this.listComponents.get(i).getComponent(0))).setSelectedItem(listValue.get(i).getActionParamData().getData().getName(edP.getLocale()));
            }
        }else if (param instanceof TaskRepeatParamMaterial && mode == MODE_PARAM_MAT){
            ArrayList<TaskRepeatValueParamMaterial> listValue = ((TaskRepeatParamMaterial)param).getListValue();
            int nb = listValue.size();
            for (int i=0; i<nb; i++){
                ((JComboBox)(this.listComponents.get(i).getComponent(0))).setSelectedItem(listValue.get(i).getActionParamMaterial().getMaterial().getName(edP.getLocale()));
            }
        }else if (param instanceof TaskRepeatParamQuantity && mode == MODE_PARAM_QTT){
            ArrayList<TaskRepeatValueParamQuantity> listValue = ((TaskRepeatParamQuantity)param).getListValue();
            int nb = listValue.size();
            for (int i=0; i<nb; i++){
                ((JTextField)(this.listComponents.get(i).getComponent(0))).setText(numberFormat.format(listValue.get(i).getActionParamQuantity().getParameter().getValue()));
                ((JComboBox)(this.listComponents.get(i).getComponent(1))).setSelectedItem(listValue.get(i).getActionParamQuantity().getParameter().getUnit().getSymbol(edP.getLocale()));
            }
        }else if (param instanceof TaskRepeatParamOutputAcquisition && mode == MODE_OUT_DATA){
            ArrayList<TaskRepeatValueDataProd> listValue = ((TaskRepeatParamOutputAcquisition)param).getListValue();
            int nb = listValue.size();
            for (int i=0; i<nb; i++){
                ((JTextField)(this.listComponents.get(i).getComponent(0))).setText(""+listValue.get(i).getData().getName(edP.getLocale()));

            }
        }else if (param instanceof TaskRepeatParamOutputManipulation && mode == MODE_OUT_MAT){
            ArrayList<TaskRepeatValueMaterialProd> listValue = ((TaskRepeatParamOutputManipulation)param).getListValue();
            int nb = listValue.size();
            for (int i=0; i<nb; i++){
                ((JComboBox)(this.listComponents.get(i).getComponent(0))).setSelectedItem(listValue.get(i).getMaterial().getName(edP.getLocale()));
            }
        }else if (param instanceof TaskRepeatParamOutputTreatment && mode == MODE_OUT_DATA){
            ArrayList<TaskRepeatValueDataProd> listValue = ((TaskRepeatParamOutputTreatment)param).getListValue();
            int nb = listValue.size();
            for (int i=0; i<nb; i++){
                ((JTextField)(this.listComponents.get(i).getComponent(0))).setText(""+listValue.get(i).getData().getName(edP.getLocale()));
            }
        }
    }
    
}
