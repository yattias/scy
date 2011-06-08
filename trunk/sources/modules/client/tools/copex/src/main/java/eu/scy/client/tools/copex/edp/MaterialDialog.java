/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.edp;

import eu.scy.client.tools.copex.common.MaterialStrategy;
import eu.scy.client.tools.copex.common.MaterialUsed;
import eu.scy.client.tools.copex.utilities.ActionAddMaterial;
import eu.scy.client.tools.copex.utilities.ActionMaterial;
import eu.scy.client.tools.copex.utilities.CopexReturn;
import eu.scy.client.tools.copex.utilities.CopexUtilities;
import eu.scy.client.tools.copex.utilities.MyConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

/**
 * material dialog
 * user can choose the material, comment, add material or delete material
 *  listmaterialpanel or newMaterialpanel
 * @author Marjolaine
 */
public class MaterialDialog extends JDialog implements ActionMaterial,ActionAddMaterial, ComponentListener{
    private EdPPanel edP;
    private List<MaterialUsed> listMaterialUsed;
    private List<MaterialUsed> listInitialMaterialUsed;
    private MaterialStrategy materialStrategy;
    private char procRight;

    public static final int panelWidth = 450;
    private static final int panelHeight = 350;
    private boolean modeAdd;
    private JPanel panelMaterial;
    private JPanel panelButtons;
    private JPanel panelCenter;
    private JSeparator sep;

    private JButton buttonOk;
    private JButton buttonCancel;

    private JPanel panelNoMaterial;
    private JLabel labelNoMaterial;
    private ListMaterialPanel listMaterialPanel;
    private NewMaterialPanel newMaterialPanel;

    private ArrayList<MaterialUsed> listMaterialToCreate;
    private ArrayList<MaterialUsed> listMaterialToDelete;
    private ArrayList<MaterialUsed> listMaterialToUpdate;


    public MaterialDialog(EdPPanel edP,char procRight, List<MaterialUsed> listMaterialUsed,  MaterialStrategy materialStrategy) {
        super(edP.getOwnerFrame());
        this.edP = edP;
        this.procRight = procRight;
        this.materialStrategy = materialStrategy;
        this.listMaterialUsed = new ArrayList();
        this.listInitialMaterialUsed =listMaterialUsed;
        for(Iterator<MaterialUsed> e = listMaterialUsed.iterator();e.hasNext();){
            this.listMaterialUsed.add((MaterialUsed)e.next().clone());
        }
        this.listMaterialToCreate = new ArrayList();
        this.listMaterialToDelete = new ArrayList();
        this.listMaterialToUpdate = new ArrayList();
        initGUI();
        
    }

    private void initGUI(){
        setLocationRelativeTo(edP);
        setLocation(edP.getLocationDialog());
        setModal(true);
        setResizable(true);
        setIconImage(edP.getIconDialog());
        setTitle(edP.getBundleString("TITLE_DIALOG_MATERIAL"));
        this.setMinimumSize(new Dimension(panelWidth, panelHeight));
        this.setSize(panelWidth, panelHeight);
        this.setPreferredSize(getSize());
        this.setLayout(new BorderLayout());
        this.add(getPanelButtons(), BorderLayout.SOUTH);
        this.modeAdd = false;
        this.addComponentListener(this);
        setListMaterial();
        if(materialStrategy.canAddMaterial() )
            setCreateMaterial();
        if(procRight == MyConstants.NONE_RIGHT){
            panelButtons.remove(buttonOk);
            buttonOk = null;
            buttonCancel.setText(edP.getBundleString("BUTTON_OK"));
        }
        resizeMaterialDialog();
    }

    private JPanel getPanelMaterial(){
        if (panelMaterial == null){
            panelMaterial = new JPanel();
            panelMaterial.setName("panelMaterial");
            panelMaterial.setSize(panelWidth, panelHeight-panelButtons.getHeight()-sep.getHeight());
            panelMaterial.setPreferredSize(panelMaterial.getSize());
            panelMaterial.setLayout(new BorderLayout());
        }
        return panelMaterial;
    }
    private JPanel getPanelCenter(){
        if (panelCenter == null){
            panelCenter = new JPanel();
            panelCenter.setName("panelCenter");
            panelCenter.setSize(panelWidth, panelHeight-panelButtons.getHeight());
            panelCenter.setPreferredSize(panelCenter.getSize());
            panelCenter.setLayout(new BorderLayout());
        }
        return panelCenter;
    }

    private JSeparator getSep(){
        if(sep == null){
            sep = new JSeparator(SwingConstants.HORIZONTAL);
            sep.setSize(panelWidth, 5);
            sep.setName("sep");
        }
        return sep;
    }

    private JLabel getLabelNoMaterial(){
        if(labelNoMaterial == null){
            labelNoMaterial = new JLabel();
            labelNoMaterial.setName("labelNoMaterial");
            labelNoMaterial.setFont(new java.awt.Font("Tahoma", Font.ITALIC, 11));
            labelNoMaterial.setText(edP.getBundleString("LABEL_NO_MATERIAL"));
            int w = CopexUtilities.lenghtOfString(this.labelNoMaterial.getText(), getFontMetrics(this.labelNoMaterial.getFont()));
            labelNoMaterial.setBounds(10, 10, w, 14);
        }
        return labelNoMaterial;
    }

    private JPanel getPanelNoMaterial(){
        if(panelNoMaterial == null){
            panelNoMaterial = new JPanel();
            panelNoMaterial.setName("panelNoMaterial");
            panelNoMaterial.setLayout(null);
        }
        return panelNoMaterial;
    }

    private JPanel getPanelButtons(){
        if(panelButtons == null){
            panelButtons = new JPanel();
            panelButtons.setName("panelButtons");
            panelButtons.setLayout(null);
            panelButtons.setSize(panelWidth, 43);
            panelButtons.setPreferredSize(panelButtons.getSize());
            panelButtons.add(getButtonOk());
            panelButtons.add(getButtonCancel());
        }
        return panelButtons;
    }

    private JButton getButtonCancel(){
        if(buttonCancel == null){
            buttonCancel = new JButton();
            buttonCancel.setName("buttonCancel");
            buttonCancel.setText(edP.getBundleString("BUTTON_CANCEL"));
            this.buttonCancel.setSize(60+CopexUtilities.lenghtOfString(this.buttonCancel.getText(), getFontMetrics(this.buttonCancel.getFont())), 23);
            this.buttonCancel.setBounds(panelWidth-panelWidth/4-buttonCancel.getWidth(), 10, buttonCancel.getWidth(), buttonCancel.getHeight());
            buttonCancel.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    materialCancel();
                }
            });
        }
        return buttonCancel;
    }

    private JButton getButtonOk(){
        if(buttonOk == null){
            buttonOk = new JButton();
            buttonOk.setName("buttonOk");
            buttonOk.setText(edP.getBundleString("BUTTON_OK"));
            this.buttonOk.setSize(60+CopexUtilities.lenghtOfString(this.buttonOk.getText(), getFontMetrics(this.buttonOk.getFont())), 23);
            this.buttonOk.setBounds(panelWidth/4, 10, buttonOk.getWidth(), buttonOk.getHeight());
            buttonOk.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    materialFinish();
                }
            });
        }
        return buttonOk;
    }

    
    

    // close the frame or return to the list
    private void materialCancel(){
        if(modeAdd)
            this.newMaterialPanel.setPanelDetailsHide();
        else
            this.dispose();
    }

    // valid the dialog
    private void materialFinish(){
        if(modeAdd){
            ArrayList v = new ArrayList();
            CopexReturn cr = newMaterialPanel.getMaterial(v);
            if(cr.isError()){
                edP.displayError(cr, edP.getBundleString("TITLE_DIALOG_ERROR"));
                return;
            }
            MaterialUsed mUsed = (MaterialUsed)v.get(0);
            this.listMaterialUsed.add(mUsed);
            this.listMaterialToCreate.add(mUsed);
            //hideAddMaterial();
            this.newMaterialPanel.setPanelDetailsHide();
        }else{
            validDialog();
        }

    }

    private void updateMaterialUsed(){
        if(listMaterialPanel == null)
            return;
        ArrayList<MaterialUsed> list = listMaterialPanel.getListMaterialUsed();
        int nb = list.size();
        for(int i=0; i<nb; i++){
            MaterialUsed mUsed = list.get(i);
            int id = getIdMaterial(listMaterialToCreate, mUsed);
            if(id !=-1){
                listMaterialToCreate.set(id, mUsed);
            }else{
                id = getIdMaterial(listInitialMaterialUsed, mUsed);
                if(id!=-1){
                    if(isDifferent(mUsed, listInitialMaterialUsed.get(id))){
                        //System.out.println("ajout dans la lsite des maj "+mUsed.getMaterial().getName(edP.getLocale()));
                        listMaterialToUpdate.add(mUsed);
                    }
                }
            }
        }
    }

    private boolean isDifferent(MaterialUsed mUsed1, MaterialUsed mUsed2){
        if(!mUsed1.getComment(edP.getLocale()).equals(mUsed2.getComment(edP.getLocale())))
            return true;
        else if(!isEqualDescription(mUsed1, mUsed2))
            return true;
        else if(mUsed1.isUsed() != mUsed2.isUsed())
            return true;
        return false;
    }

    private boolean isEqualDescription(MaterialUsed mUsed1, MaterialUsed mUsed2){
        String d1 = mUsed1.getMaterial().getDescription(edP.getLocale());
        String d2 = mUsed2.getMaterial().getDescription(edP.getLocale());
        if(d1 == null && d2 == null)
            return true;
        if((d1== null && d2!=null) || (d1 != null && d2 == null) )
            return true;
        if(d1 != null && d2 != null && d1.equals(d2))
            return true;
        return false;
    }
    // valid dialog 
    private void validDialog(){
        updateMaterialUsed();
        boolean isOk = edP.setMaterialUsed(listMaterialToCreate, listMaterialToDelete, listMaterialToUpdate);
        if(isOk)
            this.dispose();
    }
    

    // display the material list
    private void setListMaterial(){
        this.modeAdd = false;
        getPanelCenter();
        panelCenter.add(getSep(), BorderLayout.NORTH);
        panelCenter.add(getPanelMaterial(), BorderLayout.CENTER);
        this.add(getPanelCenter(), BorderLayout.CENTER);
        
        int nbMat = listMaterialUsed.size();
        if(nbMat == 0){
            getPanelNoMaterial().add(getLabelNoMaterial());
            panelMaterial.add(getPanelNoMaterial(), BorderLayout.CENTER);
        }else{
            if(labelNoMaterial != null){
                panelNoMaterial.remove(labelNoMaterial);
                this.remove(panelNoMaterial);
            }
            panelNoMaterial = null;
            labelNoMaterial = null;
            listMaterialPanel = new ListMaterialPanel(edP, procRight,materialStrategy, listMaterialUsed, this.getWidth()-30);
            listMaterialPanel.addActionMaterial(this);
            JScrollPane scroll = new JScrollPane(listMaterialPanel);
            scroll.setName("scroll");
            scroll.setSize(this.getWidth(), this.getHeight()-panelButtons.getHeight());
            scroll.setPreferredSize(scroll.getSize());
            scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            panelMaterial.add(scroll, BorderLayout.CENTER);
//            JScrollPane scrollPaneDescription = new JScrollPane();
//            scrollPaneDescription.setName("scrollPaneDescription");
//            scrollPaneDescription.setViewportView(getTextAreaDescription());
//            panelMaterial.add(scrollPaneDescription, BorderLayout.NORTH);
        }
        panelMaterial.revalidate();
        repaint();
    }

    private JTextArea getTextAreaDescription(){
        JTextArea textAreaDescription = new JTextArea();
        textAreaDescription.setName("textAreaDescription");
        textAreaDescription.setColumns(20);
        textAreaDescription.setRows(2);
        textAreaDescription.setMinimumSize(new Dimension(20, 20));
        textAreaDescription.setLineWrap(true);
        textAreaDescription.setWrapStyleWord(true);
        textAreaDescription.setText(edP.getBundleString("LABEL_MATERIAL_USED_INFO_"+materialStrategy.getCode()));
        textAreaDescription.setEditable(false);
        textAreaDescription.setFont(new Font("Tahoma",Font.ITALIC, 11));
        textAreaDescription.setForeground(Color.BLACK);
        textAreaDescription.setBackground(panelMaterial.getBackground());
        return textAreaDescription;
    }

    // creation material
    private void setCreateMaterial(){
        newMaterialPanel = new NewMaterialPanel(edP, panelMaterial, edP.getBundleString("LABEL_ADD_MATERIAL"));
        newMaterialPanel.addActionAddMaterial(this);
        this.add(newMaterialPanel, BorderLayout.NORTH);
        repaint();
    }

    @Override
    public void actionRemoveMaterial(MaterialUsed mUsed) {
        this.listMaterialUsed.remove(mUsed);
        int id = getIdMaterial(listMaterialToCreate, mUsed);
        if(id==-1){
            listMaterialToDelete.add(mUsed);
        }else{
            listMaterialToCreate.remove(id);
        }
    }

    private int getIdMaterial(List<MaterialUsed> list, MaterialUsed m){
       int nb = list.size();
       for(int i=0; i<nb; i++){
           if(list.get(i).getMaterial().getName(edP.getLocale()).equals(m.getMaterial().getName(edP.getLocale())))
               return i;
       }
       return -1;
    }
    
    private void resizeMaterialDialog(){
        if(listMaterialPanel != null){
            listMaterialPanel.resizeList(getWidth()-30, listMaterialPanel.getHeight());
        }
        if(this.panelButtons != null){
            if(buttonOk != null){
                buttonOk.setBounds(getWidth()/4, buttonOk.getY(), buttonOk.getWidth(), buttonOk.getHeight());
                buttonCancel.setBounds(getWidth()-getWidth()/4-buttonCancel.getWidth(),buttonCancel.getY(), buttonCancel.getWidth(), buttonCancel.getHeight());
            }else{
                buttonCancel.setBounds((getWidth()-buttonCancel.getWidth())/2,buttonCancel.getY(), buttonCancel.getWidth(), buttonCancel.getHeight());
            }
        }
    }


    
    @Override
    public void componentResized(ComponentEvent e) {
        resizeMaterialDialog();
    }

    @Override
    public void componentMoved(ComponentEvent e) {

    }

    @Override
    public void componentShown(ComponentEvent e) {

    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }

    @Override
    public void saveData(MaterialUsed mUsed){
        int id = getIdMaterial(listMaterialToCreate, mUsed);
        if(id !=-1){
            listMaterialToCreate.set(id, mUsed);
        }else{
            id = getIdMaterial(listInitialMaterialUsed, mUsed);
            if(id!=-1){
                 if(isDifferent(mUsed, listInitialMaterialUsed.get(id))){
                      listMaterialToUpdate.add(mUsed);
                 }
            }
        }
    }

    

    @Override
    public void actionHideAddMaterial() {
        if(panelCenter != null)
            panelCenter.removeAll();
        panelCenter = null;
        sep = null;
        if(panelMaterial != null)
            panelMaterial.removeAll();
        panelMaterial = null;
        modeAdd = false;
        setListMaterial();
    }

    @Override
    public void actionShowAddMaterial() {
        this.remove(panelCenter);
        if(panelCenter != null)
            panelCenter.removeAll();
        panelCenter = null;
        sep = null;
        if(panelMaterial != null)
            panelMaterial.removeAll();
        panelMaterial = null;
        modeAdd = true;
        setSize(getWidth(), newMaterialPanel.getHeight()+panelButtons.getHeight()+20);
        repaint();
    }

    @Override
    public void saveText() {

    }

    @Override
    public void setMaterial() {

    }
}
