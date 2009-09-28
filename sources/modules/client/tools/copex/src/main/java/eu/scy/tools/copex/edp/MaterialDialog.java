/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.edp;

import eu.scy.tools.copex.common.MaterialStrategy;
import eu.scy.tools.copex.common.MaterialUsed;
import eu.scy.tools.copex.utilities.ActionMaterial;
import eu.scy.tools.copex.utilities.CopexReturn;
import eu.scy.tools.copex.utilities.CopexUtilities;
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * material dialog
 * user can choose the material, comment, add material or delete material
 *  listmaterialpanel or newMaterialpanel
 * @author Marjolaine
 */
public class MaterialDialog extends JDialog implements ActionMaterial{
    private EdPPanel edP;
    private ArrayList<MaterialUsed> listMaterialUsed;
    private MaterialStrategy materialStrategy;

    public static final int panelWidth = 500;
    private static final int panelHeight = 400;
    private boolean modeAdd;
    private JPanel panelMaterial;
    private JPanel panelButtons;

    private JButton buttonOk;
    private JButton buttonCancel;
    private JButton buttonAdd;
    private JButton buttonPrec;

    private ListMaterialPanel listMaterialPanel;
    private NewMaterialPanel newMaterialPanel;


    public MaterialDialog(EdPPanel edP, ArrayList<MaterialUsed> listMaterialUsed, MaterialStrategy materialStrategy) {
        super();
        this.edP = edP;
        this.materialStrategy = materialStrategy;
        this.listMaterialUsed = listMaterialUsed;
        initGUI();
    }

    private void initGUI(){
        setLocationRelativeTo(edP);
        setModal(true);
        setResizable(false);
        this.setSize(panelWidth, panelHeight);
        this.setPreferredSize(getSize());
        this.setLayout(new BorderLayout());
        this.add(getPanelButtons(), BorderLayout.SOUTH);
        this.modeAdd = false;
        setListMaterial();
    }

    private JPanel getPanelMaterial(){
        if (panelMaterial == null){
            panelMaterial = new JPanel();
            panelMaterial.setName("panelMaterial");
            panelMaterial.setSize(panelWidth, panelHeight-panelButtons.getHeight());
            panelMaterial.setPreferredSize(panelMaterial.getSize());
            panelMaterial.setLayout(new BorderLayout());
        }
        return panelMaterial;
    }


    private JPanel getPanelButtons(){
        if(panelButtons == null){
            panelButtons = new JPanel();
            panelButtons.setName("panelButtons");
            panelButtons.setLayout(null);
            panelButtons.setSize(panelWidth, 63);
            panelButtons.setPreferredSize(panelButtons.getSize());
            panelButtons.add(getButtonCancel());
            panelButtons.add(getButtonOk());
            panelButtons.add(getButtonAdd());
            panelButtons.add(getButtonPrec());
        }
        return panelButtons;
    }

    private JButton getButtonCancel(){
        if(buttonCancel == null){
            buttonCancel = new JButton();
            buttonCancel.setName("buttonCancel");
            buttonCancel.setText(edP.getBundleString("BUTTON_CANCEL"));
            this.buttonCancel.setSize(60+CopexUtilities.lenghtOfString(this.buttonCancel.getText(), getFontMetrics(this.buttonCancel.getFont())), 23);
            this.buttonCancel.setBounds(panelWidth-20-buttonCancel.getWidth(), 20, buttonCancel.getWidth(), buttonCancel.getHeight());
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
            this.buttonOk.setBounds(buttonCancel.getX() - buttonOk.getWidth()-10, 20, buttonOk.getWidth(), buttonOk.getHeight());
            buttonOk.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    materialFinish();
                }
            });
        }
        return buttonOk;
    }

    private JButton getButtonAdd(){
        if(buttonAdd == null){
            buttonAdd = new JButton();
            buttonAdd.setName("buttonAdd");
            buttonAdd.setText(edP.getBundleString("BUTTON_ADD"));
            this.buttonAdd.setSize(60+CopexUtilities.lenghtOfString(this.buttonAdd.getText(), getFontMetrics(this.buttonAdd.getFont())), 23);
            this.buttonAdd.setBounds(buttonOk.getX() - buttonAdd.getWidth()-30, 20, buttonAdd.getWidth(), buttonAdd.getHeight());
            this.buttonAdd.setEnabled(materialStrategy.canAddMaterial());
            buttonAdd.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    materialAdd();
                }
            });
        }
        return buttonAdd;
    }

    private JButton getButtonPrec(){
        if(buttonPrec == null){
            buttonPrec = new JButton();
            buttonPrec.setName("buttonPrec");
            buttonPrec.setText(edP.getBundleString("BUTTON_PREC"));
            this.buttonPrec.setSize(60+CopexUtilities.lenghtOfString(this.buttonPrec.getText(), getFontMetrics(this.buttonPrec.getFont())), 23);
            this.buttonPrec.setBounds(20, 20, buttonPrec.getWidth(), buttonPrec.getHeight());
            this.buttonPrec.setEnabled(true);
            buttonPrec.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    materialPrec();
                }
            });
        }
        return buttonPrec;
    }

    // fermeture de la fenetre ou retour a la liste
    private void materialCancel(){
        if(modeAdd)
            setListMaterial();
        else
            this.dispose();
    }

    // validation de la fentre
    private void materialFinish(){
        
    }

    // ajout de material
    private void materialAdd(){
        setCreateMaterial();
    }

    // retour à la liste
    private void materialPrec(){
        if(newMaterialPanel != null){
            ArrayList v = new ArrayList();
            CopexReturn cr = newMaterialPanel.getMaterial(v);
            if(cr.isError()){
                edP.displayError(cr, edP.getBundleString("TITLE_DIALOG_ERROR"));
                return;
            }
            MaterialUsed mUsed = (MaterialUsed)v.get(0);
            this.listMaterialUsed.add(mUsed);
        }
        setListMaterial();
    }

    // affichage de  la liste du material
    private void setListMaterial(){
        this.modeAdd = false;
        this.buttonPrec.setEnabled(false);
        this.buttonAdd.setEnabled(true);
        setTitle(edP.getBundleString("TITLE_DIALOG_MATERIAL_"+materialStrategy.getCode()));
        if(panelMaterial != null){
            panelMaterial.removeAll();
            newMaterialPanel = null;
        }else{
            this.add(getPanelMaterial(), BorderLayout.CENTER);
        }
        listMaterialPanel = new ListMaterialPanel(edP, materialStrategy, listMaterialUsed);
        listMaterialPanel.addActionMaterial(this);
        JScrollPane scroll = new JScrollPane(listMaterialPanel);
        scroll.setName("scroll");
        scroll.setSize(panelWidth, panelHeight-panelButtons.getHeight());
        panelMaterial.add(scroll, BorderLayout.CENTER);
        panelMaterial.revalidate();
        repaint();
    }

    // affichage de creation de material
    private void setCreateMaterial(){
        this.modeAdd = true;
        this.buttonPrec.setEnabled(true);
        this.buttonAdd.setEnabled(false);
        setTitle(edP.getBundleString("TITLE_DIALOG_MATERIAL_CREATE"));
        if(panelMaterial != null){
            panelMaterial.removeAll();
            listMaterialPanel = null;
        }
        newMaterialPanel = new NewMaterialPanel(edP);
        panelMaterial.add(newMaterialPanel, BorderLayout.CENTER);
        panelMaterial.revalidate();
        repaint();
    }

    @Override
    public void actionRemoveMaterial(MaterialUsed mUsed) {
        this.listMaterialUsed.remove(mUsed);
    }

    @Override
    public void actionUpdateMaterial(MaterialUsed mUsed) {
        int id = -1;
        int nb = this.listMaterialUsed.size();
        for (int i=0; i<nb; i++){
            if (mUsed.getMaterial().getName().equals(this.listMaterialUsed.get(i).getMaterial().getName())){
                id = i;
                break;
            }
        }
        if(id != -1){
            this.listMaterialUsed.set(id, mUsed);
        }
    }
}
