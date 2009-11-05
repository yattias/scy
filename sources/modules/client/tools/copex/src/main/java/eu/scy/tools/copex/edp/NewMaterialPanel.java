/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.edp;

import eu.scy.tools.copex.common.Material;
import eu.scy.tools.copex.common.MaterialUsed;
import eu.scy.tools.copex.common.Parameter;
import eu.scy.tools.copex.common.TypeMaterial;
import eu.scy.tools.copex.utilities.ActionComment;
import eu.scy.tools.copex.utilities.CommentsPanel;
import eu.scy.tools.copex.utilities.CopexReturn;
import eu.scy.tools.copex.utilities.CopexUtilities;
import eu.scy.tools.copex.utilities.MyConstants;
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
public class NewMaterialPanel extends JPanel implements ActionComment{
    private EdPPanel edP;

    private boolean modeAdd;
    private JLabel labelName;
    private JTextField fieldName;
    private JLabel labelDescription;
    private JTextArea areaDescription;
    private JScrollPane scrollDescription;
    private JCheckBox cboxUsed;
    private CommentsPanel panelComments;

    private MaterialUsed mUsed;
    private String comment;

    public NewMaterialPanel(EdPPanel edP) {
        super();
        this.edP = edP;
        initGUI();
    }

    public NewMaterialPanel(EdPPanel edP, MaterialUsed mUsed){
        super();
        this.edP = edP;
        this.modeAdd = false;
        this.mUsed = mUsed;
        initGUI();
        initMaterial();
    }

    private void initGUI(){
        this.setLayout(null);
        this.add(getLabelName());
        this.add(getLabelDescription());
        this.add(getScrollDescription());
        this.add(getFieldName());
        this.add(getCBoxUsed());
        this.add(getPanelComment());
    }

    private void initMaterial(){
        this.fieldName.setText(mUsed.getMaterial().getName(edP.getLocale()));
        this.areaDescription.setText(mUsed.getMaterial().getDescription(edP.getLocale()));
        this.cboxUsed.setSelected(mUsed.isUsed());
        this.panelComments.setComments(mUsed.getComment(edP.getLocale()));
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
            fieldName.setBounds(scrollDescription.getX(), labelName.getY(), 150, 20);
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

    private CommentsPanel getPanelComment(){
        if(panelComments == null){
            int width=300;
            panelComments = new CommentsPanel(edP, this, edP.getBundleString("LABEL_ADD_COMMENT"), width);
            panelComments.addActionComment(this);
            panelComments.setBounds(labelName.getX(), scrollDescription.getY()+scrollDescription.getHeight()+10,width, panelComments.getHeight());
        }
        return panelComments;
    }

    @Override
    public void actionComment() {

    }

    @Override
    public void saveComment() {
        this.comment = panelComments.getComments() ;
    }

    @Override
    public void setComment() {
        this.panelComments.setComments(this.comment);
    }

    public CopexReturn getMaterial(ArrayList v){
        // nom
        String name = fieldName.getText();
        if (name.length() > MyConstants.MAX_LENGHT_MATERIAL_NAME){
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
        if (description.length() > MyConstants.MAX_LENGHT_MATERIAL_DESCRIPTION){
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
        Material m = new Material(CopexUtilities.getLocalText(name, edP.getLocale()), CopexUtilities.getLocalText(description, edP.getLocale()), listType, listParameters);
        // used
        boolean used = this.cboxUsed.isSelected();
        // comments
        String comments = panelComments.getComments();
        if (comments.length() > MyConstants.MAX_LENGHT_MATERIAL_COMMENTS){
            String msg = edP.getBundleString("MSG_LENGHT_MAX");
            msg  = CopexUtilities.replace(msg, 0, edP.getBundleString("LABEL_ADD_COMMENT"));
            msg = CopexUtilities.replace(msg, 1, ""+MyConstants.MAX_LENGHT_MATERIAL_COMMENTS);
            return new CopexReturn(msg, false);
        }
        // creation du materiel utilise
        MaterialUsed materialUsed = new MaterialUsed(m, CopexUtilities.getLocalText(comments, edP.getLocale()), used, true);
        v.add(materialUsed);
        return new CopexReturn();
    }

    
}
