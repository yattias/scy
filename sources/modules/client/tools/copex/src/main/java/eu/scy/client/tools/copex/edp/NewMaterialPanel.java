/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.edp;

import eu.scy.client.tools.copex.common.Material;
import eu.scy.client.tools.copex.common.MaterialSourceUser;
import eu.scy.client.tools.copex.common.MaterialUsed;
import eu.scy.client.tools.copex.common.Parameter;
import eu.scy.client.tools.copex.common.TypeMaterial;
import eu.scy.client.tools.copex.utilities.ActionAddMaterial;
import eu.scy.client.tools.copex.utilities.CopexPanelHideShow;
import eu.scy.client.tools.copex.utilities.CopexReturn;
import eu.scy.client.tools.copex.utilities.CopexUtilities;
import eu.scy.client.tools.copex.utilities.MyConstants;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * panel : add/update material
 * @author Marjolaine
 */
public class NewMaterialPanel extends CopexPanelHideShow {
    private JLabel labelName;
    private JTextField fieldName;
    private JLabel labelDescription;
    private JTextArea areaDescription;
    private JScrollPane scrollDescription;
    private JCheckBox cboxUsed;
    private JLabel labelComment;
    private JScrollPane scrollComment;
    private JTextArea areaComment;

    private MaterialUsed mUsed;
    private ActionAddMaterial actionAddMaterial;



   

    public NewMaterialPanel(EdPPanel edP, JPanel owner, String titleLabel) {
        super(edP, owner, titleLabel, true);
        initGUI();
    }

    private void initGUI(){
        this.setLayout(null);
        
    }

    @Override
    protected void setPanelDetailsHide() {
        actionAddMaterial.saveText();
        super.setPanelDetailsHide();
        if (this.panelDetails != null){
            panelDetails.remove(labelName);
            panelDetails.remove(fieldName);
            panelDetails.remove(labelDescription);
            panelDetails.remove(scrollDescription);
            panelDetails.remove(cboxUsed);
            panelDetails.remove(labelComment);
            panelDetails.remove(scrollComment);
        }
        labelName = null;
        fieldName = null;
        labelDescription = null;
        scrollDescription = null;
        areaDescription = null;
        cboxUsed = null;
        labelComment = null;
        scrollComment = null;
        areaComment = null;
        setSize(getWidth(), 30);
        setPreferredSize(getSize());
        actionAddMaterial.actionHideAddMaterial();
        revalidate();
        repaint();
    }

    @Override
    public void setPanelDetailsShown() {
        if(!isDetails()){
            super.setPanelDetailsShown();
            getPanelDetails().add(getLabelName());
            getPanelDetails().add(getLabelDescription());
            getPanelDetails().add(getScrollDescription());
            getPanelDetails().add(getFieldName());
            getPanelDetails().add(getCBoxUsed());
            getPanelDetails().add(getLabelComment());
            getPanelDetails().add(getScrollComment());
            int x = Math.max(fieldName.getX(),scrollDescription.getX());
            x = Math.max(x, scrollComment.getX());
            fieldName.setBounds(x, fieldName.getY(), fieldName.getWidth(), fieldName.getHeight());
            scrollDescription.setBounds(x, scrollDescription.getY(), scrollDescription.getWidth(), scrollDescription.getHeight());
            scrollComment.setBounds(x, scrollComment.getY(), scrollComment.getWidth(), scrollComment.getHeight());
            cboxUsed.setBounds(fieldName.getX()+fieldName.getWidth()+20, cboxUsed.getY(), cboxUsed.getWidth(), cboxUsed.getHeight());
            panelDetails.setSize(panelDetails.getWidth(), scrollComment.getY()+scrollComment.getHeight()+20);
            setSize(getWidth(),panelDetails.getHeight()+getPanelTitle().getHeight());
            setPreferredSize(getSize());
            actionAddMaterial.actionShowAddMaterial();
            actionAddMaterial.setMaterial();
            fieldName.requestFocus();
            revalidate();
            repaint();
        }
    }

    private boolean isDetails(){
        return this.scrollDescription != null;
    }

    private void initMaterial(){
        this.fieldName.setText(mUsed.getMaterial().getName(edP.getLocale()));
        this.areaDescription.setText(mUsed.getMaterial().getDescription(edP.getLocale()));
        this.cboxUsed.setSelected(mUsed.isUsed());
        this.areaComment.setText(mUsed.getComment(edP.getLocale()));
    }

    private JLabel getLabelName(){
        if(labelName == null){
            labelName = new JLabel();
            labelName.setName("labelName");
            labelName.setFont(new java.awt.Font("Tahoma", 1, 11));
            labelName.setText(edP.getBundleString("LABEL_MATERIAL_NAME"));
            int width = CopexUtilities.lenghtOfString(this.labelName.getText(), getFontMetrics(this.labelName.getFont()));
            labelName.setBounds(10, 10, width, 14);
        }
        return labelName;
    }
    private JTextField getFieldName(){
        if(fieldName == null){
            fieldName = new JTextField();
            fieldName.setName("fieldName");
            fieldName.setBounds(labelName.getX()+labelName.getWidth()+10, labelName.getY()-5, 220, 27);
        }
        return fieldName;
    }


    private JLabel getLabelDescription(){
        if(labelDescription == null){
            labelDescription = new JLabel();
            labelDescription.setName("labelDescription");
            labelDescription.setFont(new java.awt.Font("Tahoma", 1, 11));
            labelDescription.setText(edP.getBundleString("LABEL_MATERIAL_DESCRIPTION"));
            int width = CopexUtilities.lenghtOfString(this.labelDescription.getText(), getFontMetrics(this.labelDescription.getFont()));
            labelDescription.setBounds(labelName.getX(), labelName.getY()+labelName.getHeight()+20, width, 14);
        }
        return labelDescription;
    }

    private JScrollPane getScrollDescription(){
        if(scrollDescription == null){
            scrollDescription = new JScrollPane(getAreaDescription());
            scrollDescription.setName("scrollDescription");
            scrollDescription.setBounds(labelDescription.getX()+labelDescription.getWidth()+10,labelDescription.getY(), 300,70);
        }
        return scrollDescription;
    }
    private JTextArea getAreaDescription(){
        if (areaDescription == null){
            areaDescription = new JTextArea();
            areaDescription.setName("areaDescription");
            areaDescription.setWrapStyleWord(true);
            areaDescription.setLineWrap(true);
            areaDescription.setColumns(20);
            areaDescription.setRows(4);
            areaDescription.setBounds(labelDescription.getX()+labelDescription.getWidth()+10,labelDescription.getY(), areaDescription.getWidth(), areaDescription.getHeight());
        }
        return areaDescription;
    }

    private JCheckBox getCBoxUsed(){
        if(cboxUsed == null){
            cboxUsed = new JCheckBox();
            cboxUsed.setName("cboxUsed");
            cboxUsed.setFont(new java.awt.Font("Tahoma", 1, 11));
            cboxUsed.setSelected(true);
            cboxUsed.setText(edP.getBundleString("LABEL_MATERIAL_USED"));
            int width = CopexUtilities.lenghtOfString(this.cboxUsed.getText(), getFontMetrics(this.cboxUsed.getFont())) +30;
            cboxUsed.setBounds(fieldName.getX()+fieldName.getWidth()+20, fieldName.getY(), width, 20);
        }
        return cboxUsed;
    }

    private JLabel getLabelComment(){
        if(labelComment == null){
            labelComment = new JLabel();
            labelComment.setName("labelComment");
            labelComment.setFont(new java.awt.Font("Tahoma", 1, 11));
            labelComment.setText(edP.getBundleString("LABEL_COMMENTS"));
            int width = CopexUtilities.lenghtOfString(this.labelComment.getText(), getFontMetrics(this.labelComment.getFont()));
            labelComment.setBounds(labelName.getX(), scrollDescription.getY()+scrollDescription.getHeight()+20, width, 14);
        }
        return labelComment;
    }

    private JScrollPane getScrollComment(){
        if(scrollComment == null){
            scrollComment = new JScrollPane(getAreaComment());
            scrollComment.setName("scrollComment");
            scrollComment.setBounds(labelComment.getX()+labelComment.getWidth()+10,labelComment.getY(), 300,70);
        }
        return scrollComment;
    }
    private JTextArea getAreaComment(){
        if (areaComment == null){
            areaComment = new JTextArea();
            areaComment.setName("areaComment");
            areaComment.setWrapStyleWord(true);
            areaComment.setLineWrap(true);
            areaComment.setColumns(20);
            areaComment.setRows(4);
            areaComment.setBounds(labelComment.getX()+labelComment.getWidth()+10,labelComment.getY(), areaComment.getWidth(), areaComment.getHeight());
        }
        return areaComment;
    }

    /**
    * Instancie l'objet ActionAddMAterial.
    * @param action ActionAddMAterial
    */
    public void addActionAddMaterial(ActionAddMaterial action){
        this.actionAddMaterial=action;
    }


    public CopexReturn getMaterial(ArrayList v){
        // nom
        String name = fieldName.getText();
        if (controlLenght() && name.length() > MyConstants.MAX_LENGHT_MATERIAL_NAME){
            String msg = edP.getBundleString("MSG_LENGHT_MAX");
            msg  = CopexUtilities.replace(msg, 0, edP.getBundleString("LABEL_MATERIAL_NAME"));
            msg = CopexUtilities.replace(msg, 1, ""+MyConstants.MAX_LENGHT_MATERIAL_NAME);
            return new CopexReturn(msg, false);
        }
        if (name.length() == 0){
            String msg = edP.getBundleString("MSG_ERROR_FIELD_NULL");
            msg  = CopexUtilities.replace(msg, 0, edP.getBundleString("LABEL_MATERIAL_NAME"));
            return new CopexReturn(msg ,false);
        }
        // description
        String description = areaDescription.getText();
        if (controlLenght() && description.length() > MyConstants.MAX_LENGHT_MATERIAL_DESCRIPTION){
            String msg = edP.getBundleString("MSG_LENGHT_MAX");
            msg  = CopexUtilities.replace(msg, 0, edP.getBundleString("LABEL_MATERIAL_DESCRIPTION"));
            msg = CopexUtilities.replace(msg, 1, ""+MyConstants.MAX_LENGHT_MATERIAL_DESCRIPTION);
            return new CopexReturn(msg, false);
        }
        // liste des types => type inconnu par defaut
        List<TypeMaterial> listType = new LinkedList();
        listType.add(edP.getDefaultMaterialType());
        // liste des parametres
        List<Parameter> listParameters = new LinkedList();
        // creation du material
        Material m = new Material(CopexUtilities.getLocalText(name, edP.getLocale()), CopexUtilities.getLocalText(description, edP.getLocale()), null, listType, listParameters, new MaterialSourceUser());
        // used
        boolean used = this.cboxUsed.isSelected();
        // comments
        String comments = areaComment.getText();
        if (controlLenght() && comments.length() > MyConstants.MAX_LENGHT_MATERIAL_COMMENTS){
            String msg = edP.getBundleString("MSG_LENGHT_MAX");
            msg  = CopexUtilities.replace(msg, 0, edP.getBundleString("LABEL_ADD_COMMENT"));
            msg = CopexUtilities.replace(msg, 1, ""+MyConstants.MAX_LENGHT_MATERIAL_COMMENTS);
            return new CopexReturn(msg, false);
        }
        // creation du materiel utilise
        MaterialUsed materialUsed = new MaterialUsed(m, CopexUtilities.getLocalText(comments, edP.getLocale()), used);
        v.add(materialUsed);
        return new CopexReturn();
    }

    private boolean controlLenght(){
        return edP.controlLenght();
    }

    
}
