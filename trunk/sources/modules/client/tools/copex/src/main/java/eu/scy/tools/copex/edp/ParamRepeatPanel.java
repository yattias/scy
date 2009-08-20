/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.edp;

import eu.scy.tools.copex.common.CopexUnit;
import eu.scy.tools.copex.common.Material;
import eu.scy.tools.copex.common.Parameter;
import eu.scy.tools.copex.common.QData;
import eu.scy.tools.copex.common.Quantity;
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
import eu.scy.tools.copex.utilities.ActionSelectParamTaskRepeat;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
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


    private int index;
    private int nbRepeat;
    private int mode;
    private ArrayList<JPanel> listComponents;

    private ArrayList<Material> listParamMaterial;
    private ArrayList<CopexUnit> listParamUnit;
    private String quantityName;
    private ArrayList<QData> listParamData;
    private ArrayList<Material> listOutMaterial;
    private ArrayList<TypeMaterial> listOutTypeMaterial;
    private CopexUnit outputUnit;

    private ActionSelectParamTaskRepeat actionTaskRepeat;


    public ParamRepeatPanel(int index, int nbRepeat) {
        super();
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
                    listItem.add(listParamUnit.get(i).getSymbol());
                }
                for (int i=0; i<nb; i++){
                    addParamQuantity(listItem);
                }
                break;
            case MODE_PARAM_MAT :
                n = listParamMaterial.size();
                for (int i=0; i<n; i++){
                    listItem.add(listParamMaterial.get(i).getName());
                }
                for (int i=0; i<nb; i++){
                    addParamMaterial(listItem);
                }
                break;
            case MODE_PARAM_DATA :
                n = listParamData.size();
                for (int i=0; i<n; i++){
                    listItem.add(listParamData.get(i).getName());
                }
                for (int i=0; i<nb; i++){
                    addParamData(listItem);
                }
                break;
            case MODE_OUT_MAT :
                n = listOutMaterial.size();
                for (int i=0; i<n; i++){
                    listItem.add(listOutMaterial.get(i).getName());
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
    public void setParamQuantity(ArrayList<CopexUnit> listUnit, String quantityName){
        setNone();
        setMode(MODE_PARAM_QTT);
        this.listParamUnit = listUnit ;
        this.quantityName = quantityName;
        int nb = listUnit.size();
        Vector listItem = new Vector();
        for (int i=0; i<nb; i++){
            listItem.add(listUnit.get(i).getSymbol());
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
        tf.setSize(60, 20);
        tf.setPreferredSize(new Dimension(60,20));
        panel.add(tf);
        JComboBox cb = new JComboBox(listItem);
        cb.setSize(40, 20);
        panel.add(cb);
        JSeparator sep = new JSeparator(SwingConstants.VERTICAL);
        sep.setBackground(Color.BLACK);
        sep.setSize(5, 20);
        panel.add(sep);
        this.add(panel);
        this.listComponents.add(panel);
    }

    public ArrayList<Parameter> getParamQuantity(){
        ArrayList<Parameter> list = new ArrayList();
        if (mode == MODE_PARAM_QTT){
            int nb = listComponents.size();
            for (int i=0; i<nb; i++){
                String s = ((JTextField)listComponents.get(i).getComponent(0)).getText();
                double value = 0;
                try{
                    value = Double.parseDouble(s);
                }catch(NumberFormatException e){
                    //TODO ERROR
                }
                int id = ((JComboBox)listComponents.get(i).getComponent(1)).getSelectedIndex();
                CopexUnit unit = this.listParamUnit.get(id);
                Parameter p = new Parameter(-1, quantityName, "", value, "", unit);
                list.add(p);
            }
        }
        return list;
    }


    /* affichage param materials  combo box*/
    public void setParamMaterial(ArrayList<Material> listMaterial){
        setNone();
        this.setMode(MODE_PARAM_MAT);
        this.listParamMaterial = listMaterial ;
        Vector listItem = new Vector();
        int nb = listMaterial.size();
        for (int i=0; i<nb; i++){
            listItem.add(listMaterial.get(i).getName());
        }
        for (int i=0; i<nbRepeat; i++){
            addParamMaterial(listItem);
        }
        resizePanel();
    }

    private void addParamMaterial(Vector listItem){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        JComboBox cb = new JComboBox(listItem);
        cb.setSize(60, 20);
        panel.add(cb);
        JSeparator sep = new JSeparator(SwingConstants.VERTICAL);
        sep.setBackground(Color.BLACK);
        sep.setSize(5, 20);
        sep.setMaximumSize(getSize());
         panel.add(sep);
         this.add(panel);
         this.listComponents.add(panel);
    }

    public ArrayList<Material> getParamMaterial(){
        ArrayList<Material> list = new ArrayList();
        if (mode == MODE_PARAM_MAT){
            int nb = listComponents.size();
            for (int i=0; i<nb; i++){
                int id = ((JComboBox)listComponents.get(i).getComponent(0)).getSelectedIndex();
                list.add(this.listParamMaterial.get(id));
            }
        }
        return list;
    }

    /* affichage param data  combo box*/
    public void setParamData(ArrayList<QData> listData){
        setNone();
        setMode(MODE_PARAM_DATA);
        this.listParamData = listData ;
        Vector listItem = new Vector();
        int nb = listData.size();
        for (int i=0; i<nb; i++){
            listItem.add(listData.get(i).getName());
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
            cb.setSize(60, 20);
            panel.add(cb);
            JSeparator sep = new JSeparator(SwingConstants.VERTICAL);
            sep.setBackground(Color.BLACK);
            sep.setSize(5, 20);
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
    public void setOutputMaterial(ArrayList<Material> listOutput, ArrayList<TypeMaterial> listType){
        setNone();
        this.setMode(MODE_OUT_MAT);
        this.listOutMaterial = listOutput;
        this.listOutTypeMaterial = listType;
        Vector listItem = new Vector();
        int nb = listOutput.size();
        for (int i=0; i<nb; i++){
            listItem.add(listOutput.get(i).getName());
        }
        for (int i=0; i<nbRepeat; i++){
            addOutputMaterial(listItem);
        }
        resizePanel();
    }

    private void addOutputMaterial(Vector listItem){
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
            JComboBox cb = new JComboBox(listItem);
            cb.setSize(60, 20);
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
            sep.setSize(5, 20);
            panel.add(sep);
            this.add(panel);
            this.listComponents.add(panel);
    }

    public ArrayList<Material> getOutputMaterial(){
        ArrayList<Material> list = new ArrayList();
        if( mode == MODE_OUT_MAT){
            int nb = listComponents.size();
            for (int i=0; i<nb; i++){
                String s = (String)((JComboBox)listComponents.get(i).getComponent(0)).getSelectedItem();
                Material m = getMaterialOutput(s);
                if (m == null){
                    ArrayList<Parameter> listParameters = new ArrayList();
                    m = new Material(s, s, listOutTypeMaterial, listParameters, false);
                }
                list.add(m);
            }
        }
        return list;
    }


    private void changeCbOutputMaterialSelection(ItemEvent evt){
        String matName = (String)((JComboBox)evt.getSource()).getSelectedItem();
        int idCb = getComboBoxId((JComboBox)evt.getSource());
        if(idCb == -1)
            return;
        if(idCb >-1 && actionTaskRepeat != null && !isMaterialOutput(matName)){
            // liste des parametres
            ArrayList<Parameter> listParameters = new ArrayList();
            Material m = new Material(matName, matName, listOutTypeMaterial, listParameters, false);
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
            if (listOutMaterial.get(i).getName().equals(name))
                return true;
        }
        return false;
    }
    private Material getMaterialOutput(String name){
        int nb = listOutMaterial.size();
        for (int i=0; i<nb; i++){
            if (listOutMaterial.get(i).getName().equals(name))
                return listOutMaterial.get(i);
        }
        return null;
    }

    /* affichage output data : textfied */
    public void setOutputData(CopexUnit unit){
        setNone();
        this.outputUnit = unit;
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
        field.setSize(60, 20);
        field.setPreferredSize(new Dimension(60,20));
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

    public ArrayList<QData> getOutputData(){
        ArrayList<QData> list = new ArrayList();
        if (mode == MODE_OUT_DATA){
            int nb = listComponents.size();
            for (int i=0; i<nb; i++){
                String s = ((JTextField)listComponents.get(i).getComponent(0)).getText();
                QData data = new QData(-1, s, "", 0, "", outputUnit);
                list.add(data);
            }
        }
        return list;
    }


    private void changeOutputData(ActionEvent evt){
        String name = ((JTextField)evt.getSource()).getText();
        int idF = getFieldId((JTextField)evt.getSource());
        if (idF != -1  && actionTaskRepeat != null){
            QData data = new QData(-1, name, "", 0, "", outputUnit);
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
                width+= tab[j].getWidth();
            }
        }
        setSize(width, 20);
        setPreferredSize(getSize());
        revalidate();
        repaint();
    }

    public void setTaskRepeatParam(TaskRepeatParam param){
        if(param instanceof TaskRepeatParamData && mode == MODE_PARAM_DATA){
            ArrayList<TaskRepeatValueParamData> listValue = ((TaskRepeatParamData)param).getListValue();
            int nb = listValue.size();
            for (int i=0; i<nb; i++){
                ((JComboBox)(this.listComponents.get(i).getComponent(0))).setSelectedItem(listValue.get(i).getActionParamData().getData().getName());
            }
        }else if (param instanceof TaskRepeatParamMaterial && mode == MODE_PARAM_MAT){
            ArrayList<TaskRepeatValueParamMaterial> listValue = ((TaskRepeatParamMaterial)param).getListValue();
            int nb = listValue.size();
            for (int i=0; i<nb; i++){
                ((JComboBox)(this.listComponents.get(i).getComponent(0))).setSelectedItem(listValue.get(i).getActionParamMaterial().getMaterial().getName());
            }
        }else if (param instanceof TaskRepeatParamQuantity && mode == MODE_PARAM_QTT){
            ArrayList<TaskRepeatValueParamQuantity> listValue = ((TaskRepeatParamQuantity)param).getListValue();
            int nb = listValue.size();
            for (int i=0; i<nb; i++){
                ((JTextField)(this.listComponents.get(i).getComponent(0))).setText(""+listValue.get(i).getActionParamQuantity().getParameter().getValue());
                ((JComboBox)(this.listComponents.get(i).getComponent(1))).setSelectedItem(listValue.get(i).getActionParamQuantity().getParameter().getUnit().getSymbol());
            }
        }else if (param instanceof TaskRepeatParamOutputAcquisition && mode == MODE_OUT_DATA){
            ArrayList<TaskRepeatValueDataProd> listValue = ((TaskRepeatParamOutputAcquisition)param).getListValue();
            int nb = listValue.size();
            for (int i=0; i<nb; i++){
                ((JTextField)(this.listComponents.get(i).getComponent(0))).setText(""+listValue.get(i).getData().getName());

            }
        }else if (param instanceof TaskRepeatParamOutputManipulation && mode == MODE_OUT_DATA){
            ArrayList<TaskRepeatValueMaterialProd> listValue = ((TaskRepeatParamOutputManipulation)param).getListValue();
            int nb = listValue.size();
            for (int i=0; i<nb; i++){
                ((JComboBox)(this.listComponents.get(i).getComponent(0))).setSelectedItem(listValue.get(i).getMaterial().getName());
            }
        }else if (param instanceof TaskRepeatParamOutputTreatment && mode == MODE_OUT_DATA){
            ArrayList<TaskRepeatValueDataProd> listValue = ((TaskRepeatParamOutputTreatment)param).getListValue();
            int nb = listValue.size();
            for (int i=0; i<nb; i++){
                ((JTextField)(this.listComponents.get(i).getComponent(0))).setText(""+listValue.get(i).getData().getName());
            }
        }
    }
    
}
