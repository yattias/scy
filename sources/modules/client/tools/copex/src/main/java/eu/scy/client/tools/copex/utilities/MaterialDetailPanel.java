/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.utilities;

import eu.scy.client.tools.copex.common.LocalText;
import eu.scy.client.tools.copex.common.MaterialStrategy;
import eu.scy.client.tools.copex.common.MaterialUsed;
import eu.scy.client.tools.copex.edp.EdPPanel;
import java.awt.Font;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author Marjolaine
 */
public class MaterialDetailPanel extends CopexPanelHideShow implements ActionCopexButton {
    private char procRight;
    private MaterialUsed mUsed;
    private MaterialStrategy materialStrategy;
    private String comment;

    private JCheckBox cbUsed;
    private CopexButtonPanel buttonRemove;
    private JLabel labelName;
    private JLabel labelDescription;
    private JScrollPane scrollPaneDescription;
    private JTextArea areaDescription;
    private JLabel labelType1;
    private JLabel labelType2;
    private JLabel labelParam;
    private JTextArea areaParam;
    private JScrollPane scrollPaneParam;
    private JLabel labelComments;
    private JScrollPane scrollPaneComments;
    private JTextArea areaComments;
    private JLabel labelURLDescription;
    private JScrollPane scrollPaneURLDescription;
    private JEditorPane editorURLDescription;

    private ActionMaterialDetail action;

    /* Images */
    private ImageIcon imgRemove;
    private ImageIcon imgRemoveClic;
    private ImageIcon imgRemoveSurvol;
    private ImageIcon imgRemoveGris;

    
    public MaterialDetailPanel(EdPPanel edP, char procRight, JPanel owner, MaterialUsed mUsed, MaterialStrategy materialStrategy) {
        super(edP, owner, "", false);
        this.mUsed = mUsed;
        this.procRight = procRight;
        this.materialStrategy = materialStrategy;
        this.comment = mUsed.getComment(edP.getLocale());
        initGUI();
    }

    private void initGUI(){
        imgRemove = edP.getCopexImage("Bouton-AdT-28_delete.png");
        imgRemoveClic = edP.getCopexImage("Bouton-AdT-28_delete_cli.png");
        imgRemoveSurvol = edP.getCopexImage("Bouton-AdT-28_delete_sur.png");
        imgRemoveGris = edP.getCopexImage("Bouton-AdT-28_delete_gri.png");
        if(materialStrategy.canChooseMaterial())
            getPanelTitle().add(getCBUsed());
        if(mUsed.isUserMaterial())
            getPanelTitle().add(getButtonRemove());
        getPanelTitle().add(getLabelName());
        getPanelTitle().setSize(labelName.getX()+labelName.getWidth()+5,getPanelTitle().getHeight()-5);
        setSize(getWidth(), getPanelTitle().getHeight());
        setPreferredSize(getSize());
    }

    /**
    * Instanciation ActionMaterialDetail.
    * @param action ActionMaterialDetail
    */
    public void addActionMaterialDetail(ActionMaterialDetail action){
        this.action=action;
    }

    private JCheckBox getCBUsed(){
        if(cbUsed == null){
            cbUsed = new JCheckBox();
            cbUsed.setName("cbUsed");
            cbUsed.setSelected(mUsed.isUsed());
            if(mUsed.isAutoUsed()){
                cbUsed.setEnabled(false);
                cbUsed.setToolTipText(edP.getBundleString("TOOLTIPTEXT_NO_SELECT_MATERIAL"));
            }
            if(procRight == MyConstants.NONE_RIGHT){
                cbUsed.setEnabled(false);
            }
            cbUsed.setFont(new java.awt.Font("Tahoma", 1, 11));
            cbUsed.setBounds(30,0,20, 20);
        }
        return cbUsed;
    }

    private CopexButtonPanel getButtonRemove(){
        if(buttonRemove == null){
            buttonRemove = new CopexButtonPanel(imgRemove.getImage(),imgRemoveSurvol.getImage(), imgRemoveClic.getImage(), imgRemoveGris.getImage() );
            buttonRemove.setBounds(cbUsed.getX()+cbUsed.getWidth()+5,cbUsed.getY()+5, buttonRemove.getWidth(), buttonRemove.getHeight());
            buttonRemove.addActionCopexButton(this);
            buttonRemove.setToolTipText(edP.getBundleString("TOOLTIPTEXT_REMOVE_MATERIAL"));
            if(mUsed.isAutoUsed()){
                buttonRemove.setEnabled(false);
            }
        }
        return buttonRemove;
    }

    private JLabel getLabelName(){
        if(labelName == null){
            labelName = new JLabel();
            labelName.setName("labelName");
            labelName.setFont(new Font("Tahoma",Font.PLAIN, 11));
            labelName.setText(mUsed.getMaterial().getName(edP.getLocale()));
            int w = CopexUtilities.lenghtOfString(this.labelName.getText(), getFontMetrics(this.labelName.getFont()));
            int x = 0;
            if(buttonRemove == null){
                if(cbUsed == null){
                    x = 30;
                }else{
                    x = cbUsed.getX()+cbUsed.getWidth()+10;
                }
            }else{
                x = buttonRemove.getX()+buttonRemove.getWidth()+10;
            }
            labelName.setBounds(x, 5, w, 14);
        }
        return labelName;
    }

    private JLabel getLabelDescription(){
        if(labelDescription == null){
            labelDescription = new JLabel();
            labelDescription.setName("labelDescription");
            labelDescription.setFont(new Font("Tahoma",Font.BOLD, 11));
            labelDescription.setText(edP.getBundleString("LABEL_DESCRIPTION"));
            int w = CopexUtilities.lenghtOfString(this.labelDescription.getText(), getFontMetrics(this.labelDescription.getFont()));
            labelDescription.setBounds(20, 0, w, 14);
        }
        return labelDescription;
    }

    private JTextArea getAreaDescription(){
        if(areaDescription == null){
            areaDescription = new JTextArea();
            areaDescription.setName("areaDescription");
            areaDescription.setWrapStyleWord(true);
            areaDescription.setLineWrap(true);
            areaDescription.setColumns(15);
            areaDescription.setRows(3);
            areaDescription.setText(mUsed.getMaterial().getDescription(edP.getLocale()));
            boolean editable = mUsed.isUserMaterial();
            if(procRight == MyConstants.NONE_RIGHT)
                editable = false;
            areaDescription.setEditable(editable);
        }
        return areaDescription;
    }

    private JScrollPane getScrollDescription(){
        if(scrollPaneDescription == null){
            scrollPaneDescription = new JScrollPane(getAreaDescription());
            scrollPaneDescription.setName("scrollPaneDescription");
            scrollPaneDescription.setBounds(labelDescription.getX()+labelDescription.getWidth()+10,0,300,70);
        }
        return scrollPaneDescription;
    }

    private JLabel getLabelURLDescription(){
        if(labelURLDescription == null){
            labelURLDescription = new JLabel();
            labelURLDescription.setName("labelURLDescription");
            labelURLDescription.setFont(new Font("Tahoma",Font.BOLD, 11));
            labelURLDescription.setText(edP.getBundleString("LABEL_URL_DESCRIPTION"));
            int w = CopexUtilities.lenghtOfString(this.labelURLDescription.getText(), getFontMetrics(this.labelURLDescription.getFont()));
            int y =scrollPaneComments.getY()+scrollPaneComments.getHeight()+10;
            labelURLDescription.setBounds(labelDescription.getX(), y, w, 14);
        }
        return labelURLDescription;
    }

    private JEditorPane getEditorURLDescription(){
        if(editorURLDescription== null){
            editorURLDescription = new JEditorPane();
            editorURLDescription.setName("editorURLDescription");
            editorURLDescription.setEditable(false);
            try {
                editorURLDescription.setPage(mUsed.getMaterial().getURLDescription());
            } catch (IOException ex) {
                Logger.getLogger(MaterialDetailPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return editorURLDescription;
    }

    private JScrollPane getScrollURLDescription(){
        if(scrollPaneURLDescription == null){
            scrollPaneURLDescription = new JScrollPane();
            scrollPaneURLDescription.setName("scrollPaneURLDescription");
            scrollPaneURLDescription.setBounds(labelURLDescription.getX()+labelURLDescription.getWidth()+10,labelURLDescription.getY(),300,300);
            scrollPaneURLDescription.setViewportView(getEditorURLDescription());
        }
        return scrollPaneURLDescription;
    }

    private JLabel getLabelComments(){
        if(labelComments == null){
            labelComments = new JLabel();
            labelComments.setName("labelComments");
            labelComments.setFont(new Font("Tahoma",Font.BOLD, 11));
            labelComments.setText(edP.getBundleString("LABEL_COMMENTS"));
            int w = CopexUtilities.lenghtOfString(this.labelComments.getText(), getFontMetrics(this.labelComments.getFont()));
            int y =scrollPaneDescription.getY()+scrollPaneDescription.getHeight()+10;
            if(labelType1 != null){
                y = labelType1.getY()+labelType1.getHeight()+10;
            }
            if(areaParam != null){
                y = scrollPaneParam.getY()+scrollPaneParam.getHeight()+10;
            }
            labelComments.setBounds(labelDescription.getX(), y, w, 14);
        }
        return labelComments;
    }

    private JTextArea getAreaComments(){
        if(areaComments == null){
            areaComments = new JTextArea();
            areaComments.setName("areaComments");
            areaComments.setWrapStyleWord(true);
            areaComments.setLineWrap(true);
            areaComments.setColumns(15);
            areaComments.setRows(3);
            areaComments.setText(mUsed.getComment(edP.getLocale()));
            if(procRight == MyConstants.NONE_RIGHT)
                areaComments.setEditable(false);

        }
        return areaComments;
    }

    private JScrollPane getScrollComments(){
        if(scrollPaneComments == null){
            scrollPaneComments = new JScrollPane(getAreaComments());
            scrollPaneComments.setName("scrollPaneComments");
            scrollPaneComments.setBounds(labelComments.getX()+labelComments.getWidth()+10,labelComments.getY(),300,70);
        }
        return scrollPaneComments;
    }

    @Override
    protected void setPanelDetailsShown() {
        super.setPanelDetailsShown();
        getPanelDetails().add(getLabelDescription());
        getPanelDetails().add(getScrollDescription());
        
        if(!mUsed.isUserMaterial()){
            getPanelDetails().add(getLabelType1());
            getPanelDetails().add(getLabelType2());
            if(mUsed.getMaterial().getListParameters().size()> 0){
                getPanelDetails().add(getLabelParam());
                getPanelDetails().add(getScrollParam());
            }
        }
        getPanelDetails().add(getLabelComments());
        getPanelDetails().add(getScrollComments());
        int h = scrollPaneComments.getHeight()+scrollPaneComments.getY();
        if(mUsed.getMaterial().hasMoreInfo()){
            getPanelDetails().add(getLabelURLDescription());
            getPanelDetails().add(getScrollURLDescription());
            h = scrollPaneURLDescription.getHeight()+scrollPaneURLDescription.getY();
        }
        int x = Math.max(scrollPaneDescription.getX(), scrollPaneComments.getX());
        if(mUsed.getMaterial().hasMoreInfo()){
            x = Math.max(x, scrollPaneURLDescription.getX());
        }
        if(labelType2 != null){
            x = Math.max(x, labelType2.getX());
        }
        if(scrollPaneParam != null){
            x = Math.max(x, scrollPaneParam.getX());
        }
        scrollPaneDescription.setBounds(x, scrollPaneDescription.getY(), scrollPaneDescription.getWidth(), scrollPaneDescription.getHeight());
        scrollPaneComments.setBounds(x, scrollPaneComments.getY(), scrollPaneComments.getWidth(), scrollPaneComments.getHeight());
        if(mUsed.getMaterial().hasMoreInfo()){
            scrollPaneURLDescription.setBounds(x, scrollPaneURLDescription.getY(), scrollPaneURLDescription.getWidth(), scrollPaneURLDescription.getHeight());
        }
        if(labelType2 != null){
            labelType2.setBounds(x, labelType2.getY(), labelType2.getWidth(), labelType2.getHeight());
        }
        if(scrollPaneParam != null){
            scrollPaneParam.setBounds(x, scrollPaneParam.getY(), scrollPaneParam.getWidth(), scrollPaneParam.getHeight());
        }
        panelDetails.setSize(getPanelDetails().getWidth(), h);
        setSize(getWidth(),panelDetails.getHeight()+getPanelTitle().getHeight());
        resizePanelWidth(getWidth());
        if(action != null)
            this.action.actionResize();
    }

    @Override
    protected void setPanelDetailsHide() {
        action.saveData(getMaterialUsed());
        super.setPanelDetailsHide();
        if(panelDetails != null ){
            if(scrollPaneDescription != null){
                panelDetails.remove(scrollPaneDescription);
            }
        }
        scrollPaneDescription = null;
        areaDescription = null;
        scrollPaneURLDescription = null;
        editorURLDescription = null;
        setSize(getWidth(), getPanelTitle().getHeight());
        setPreferredSize(getSize());
        revalidate();
        repaint();
        if(action != null)
            this.action.actionResize();
    }

    @Override
    public void actionCopexButtonClic(CopexButtonPanel button) {
        if(action != null)
            action.actionRemoveMaterial(mUsed);
    }

   

    private JLabel getLabelType1(){
        if(labelType1 == null){
            labelType1 = new JLabel();
            labelType1.setName("labelType1");
            labelType1.setText(edP.getBundleString("LABEL_TYPE_MATERIAL"));
            labelType1.setFont(new java.awt.Font("Tahoma", 1, 11));
            int w = CopexUtilities.lenghtOfString(this.labelType1.getText(), getFontMetrics(this.labelType1.getFont()));
            labelType1.setBounds(labelDescription.getX(), scrollPaneDescription.getY()+scrollPaneDescription.getHeight()+10, w, 14);
        }
        return labelType1;
    }

    private JLabel getLabelType2(){
        if(labelType2 == null){
            labelType2 = new JLabel();
            labelType2.setName("labelType2");
            labelType2.setText(mUsed.getMaterial().getTypeToDisplay(edP.getLocale()));
            int w = CopexUtilities.lenghtOfString(this.labelType2.getText(), getFontMetrics(this.labelType2.getFont()));
            labelType2.setBounds(labelType1.getX()+labelType1.getWidth()+10, labelType1.getY(), w, 14);
        }
        return labelType2;
    }

    private JLabel getLabelParam(){
        if(labelParam == null){
            labelParam = new JLabel();
            labelParam.setName("labelParam");
            labelParam.setText(edP.getBundleString("LABEL_PARAMETERS_MATERIAL"));
            labelParam.setFont(new java.awt.Font("Tahoma", 1, 11));
            int w = CopexUtilities.lenghtOfString(this.labelParam.getText(), getFontMetrics(this.labelParam.getFont()));
            labelParam.setBounds(labelDescription.getX(), labelType1.getY()+labelType1.getHeight()+10, w, 14);
        }
        return labelParam;
    }

    private JScrollPane getScrollParam(){
        if(scrollPaneParam == null){
           scrollPaneParam = new JScrollPane(getAreaParam());
           scrollPaneParam.setName("scrollPaneParam");
           scrollPaneParam.setBounds(labelParam.getX()+labelParam.getWidth()+10,labelParam.getY(),300,70);
        }
        return scrollPaneParam;
    }

    private JTextArea getAreaParam(){
        if(areaParam == null){
            areaParam = new JTextArea();
            areaParam.setName("areaParam");
            areaParam.setWrapStyleWord(true);
            areaParam.setLineWrap(true);
            areaParam.setColumns(15);
            areaParam.setRows(3);
            areaParam.setText(mUsed.getMaterial().getParametersToDisplay(edP.getLocale()));
            areaParam.setEnabled(false);
            areaParam.setEditable(false);
        }
        return areaParam;
    }

    

    public MaterialUsed getMaterialUsed(){
        if(areaDescription != null){
            LocalText text = new LocalText(areaDescription.getText(), edP.getLocale());
           // mUsed.getMaterial().setDescription(text);
             mUsed.getMaterial().setDescription(areaDescription.getText());
        }
        if(areaComments != null){
            //mUsed.setComment(CopexUtilities.getTextLocal(areaComments.getText(), edP.getLocale()));
            mUsed.setComment(areaComments.getText());
        }
        if(materialStrategy.canChooseMaterial())
            mUsed.setUsed(cbUsed.isSelected());
        return mUsed;
    }

    public void resizePanelWidth(int width){
        if(scrollPaneDescription != null){
            int w = width - scrollPaneDescription.getX() - 10;
            scrollPaneDescription.setSize(w, scrollPaneDescription.getHeight());
        }
        if(scrollPaneComments != null){
            int w = width - scrollPaneComments.getX() - 10;
            scrollPaneComments.setSize(w, scrollPaneComments.getHeight());
        }
        if(scrollPaneParam != null){
            int w = width - scrollPaneParam.getX() - 10;
            scrollPaneParam.setSize(w, scrollPaneParam.getHeight());
        }
        if(scrollPaneURLDescription != null){
            int w = width - scrollPaneURLDescription.getX() - 10;
            scrollPaneURLDescription.setSize(w, scrollPaneURLDescription.getHeight());
        }
        getPanelDetails().setSize(width, panelDetails.getHeight());
        setSize(width,getHeight());
        setPreferredSize(getSize());
        revalidate();
        repaint();
    }

}
