/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.utilities;

import eu.scy.tools.copex.common.LocalText;
import eu.scy.tools.copex.common.MaterialStrategy;
import eu.scy.tools.copex.common.MaterialUsed;
import eu.scy.tools.copex.edp.EdPPanel;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author Marjolaine
 */
public class MaterialDetailPanel extends CopexPanelHideShow implements ActionCopexButton, ActionComment {
    private MaterialUsed mUsed;
    private MaterialStrategy materialStrategy;
    private String comment;

    private JCheckBox cbUsed;
    private CopexButtonPanel buttonRemove;
    private JLabel labelName;
    private JScrollPane scrollPaneDescription;
    private JTextArea areaDescription;
    private JLabel labelType1;
    private JLabel labelType2;
    private JLabel labelParam;
    private JTextArea areaParam;
    private JScrollPane scrollPaneParam;
    private CommentsPanel panelComments;

    private ActionMaterialDetail action;

    /* Images */
    private ImageIcon imgRemove;
    private ImageIcon imgRemoveClic;
    private ImageIcon imgRemoveSurvol;

    private ImageIcon imgMaterialClose;
    private ImageIcon imgMaterialOpen;


    public MaterialDetailPanel(EdPPanel edP, JPanel owner, MaterialUsed mUsed, MaterialStrategy materialStrategy) {
        super(edP, owner, "", false);
        this.mUsed = mUsed;
        this.materialStrategy = materialStrategy;
        this.comment = mUsed.getComment(edP.getLocale());
        initGUI();
    }

    private void initGUI(){
        imgRemove = edP.getCopexImage("Bouton-onglet_fermeture.png");
        imgRemoveClic = edP.getCopexImage("Bouton-onglet_fermeture_cli.png");
        imgRemoveSurvol = edP.getCopexImage("Bouton-onglet_fermeture_sur.png");
        imgMaterialClose = edP.getCopexImage("Puce-mat_ronde.png");
        imgMaterialOpen = edP.getCopexImage("Puce-mat_ronde_open.png");
        buttonArrow.setImgHide(imgMaterialClose);
        buttonArrow.setImgShow(imgMaterialOpen);
        buttonArrow.revalidate();
        buttonArrow.repaint();
        if(materialStrategy.canChooseMaterial())
            getPanelTitle().add(getCBUsed());
        if(mUsed.isUserMaterial())
            getPanelTitle().add(getButtonRemove());
        getPanelTitle().add(getLabelName());
        getPanelTitle().setSize(labelName.getX()+labelName.getWidth()+5,getPanelTitle().getHeight());
        setSize(getWidth(), getPanelTitle().getHeight());
        setPreferredSize(getSize());
    }

    /**
    * Instancie l'objet ActionMaterialDetail.
    * @param action ActionComment
    */
    public void addActionMaterialDetail(ActionMaterialDetail action){
        this.action=action;
    }

    private JCheckBox getCBUsed(){
        if(cbUsed == null){
            cbUsed = new JCheckBox();
            cbUsed.setName("cbUsed");
            cbUsed.setSelected(mUsed.isUsed());
            if(mUsed.isAutoUsed())
                cbUsed.setEnabled(false);
            cbUsed.setFont(new java.awt.Font("Tahoma", 1, 11));
            cbUsed.setBounds(30,0,20, 20);
        }
        return cbUsed;
    }

    private CopexButtonPanel getButtonRemove(){
        if(buttonRemove == null){
            buttonRemove = new CopexButtonPanel(22, imgRemove.getImage(),imgRemoveSurvol.getImage(), imgRemoveClic.getImage(), imgRemove.getImage() );
            buttonRemove.setBounds(cbUsed.getX()+cbUsed.getWidth()+5,cbUsed.getY()+5, buttonRemove.getWidth(), buttonRemove.getHeight());
            buttonRemove.addActionCopexButton(this);
            buttonRemove.setToolTipText(edP.getBundleString("TOOLTIPTEXT_REMOVE_MATERIAL"));
        }
        return buttonRemove;
    }

    private JLabel getLabelName(){
        if(labelName == null){
            labelName = new JLabel();
            labelName.setName("labelName");
            labelName.setFont(new java.awt.Font("Tahoma", 1, 11));
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
            areaDescription.setEnabled(editable);
        }
        return areaDescription;
    }

    private JScrollPane getScrollDescription(){
        if(scrollPaneDescription == null){
            scrollPaneDescription = new JScrollPane(getAreaDescription());
            scrollPaneDescription.setName("scrollPaneDescription");
            scrollPaneDescription.setBounds(30,0,300,70);
        }
        return scrollPaneDescription;
    }

    @Override
    protected void setPanelDetailsShown() {
        super.setPanelDetailsShown();
        getPanelDetails().add(getScrollDescription());
        if(!mUsed.isUserMaterial()){
            // affiche type et parametres si besoin
            getPanelDetails().add(getLabelType1());
            getPanelDetails().add(getLabelType2());
            if(mUsed.getMaterial().getListParameters().size()> 0){
                getPanelDetails().add(getLabelParam());
                getPanelDetails().add(getScrollParam());
            }
        }
        getPanelDetails().add(getPanelComment());
        panelDetails.setSize(panelDetails.getWidth(), panelComments.getHeight()+panelComments.getY());
        setSize(getWidth(),panelDetails.getHeight()+getPanelTitle().getHeight());
        setPreferredSize(getSize());
        revalidate();
        repaint();
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

    private CommentsPanel getPanelComment(){
        if(panelComments == null){
            int width=300;
            panelComments = new CommentsPanel(edP, this, edP.getBundleString("LABEL_ADD_COMMENT"), width);
            panelComments.addActionComment(this);
            panelComments.setComments(comment);
            int y =scrollPaneDescription.getY()+scrollPaneDescription.getHeight()+10;
            if(labelType1 != null){
                y = labelType1.getY()+labelType1.getHeight()+10;
            }
            if(areaParam != null){
                y = scrollPaneParam.getY()+scrollPaneParam.getHeight()+10;
            }
            panelComments.setBounds(scrollPaneDescription.getX(), y,width, panelComments.getHeight());
        }
        return panelComments;
    }

    private JLabel getLabelType1(){
        if(labelType1 == null){
            labelType1 = new JLabel();
            labelType1.setName("labelType1");
            labelType1.setText(edP.getBundleString("LABEL_TYPE_MATERIAL"));
            labelType1.setFont(new java.awt.Font("Tahoma", 1, 11));
            int w = CopexUtilities.lenghtOfString(this.labelType1.getText(), getFontMetrics(this.labelType1.getFont()));
            labelType1.setBounds(scrollPaneDescription.getX(), scrollPaneDescription.getY()+scrollPaneDescription.getHeight()+10, w, 14);
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
            labelParam.setBounds(scrollPaneDescription.getX(), labelType1.getY()+labelType1.getHeight()+10, w, 14);
        }
        return labelParam;
    }

    private JScrollPane getScrollParam(){
        if(scrollPaneParam == null){
           scrollPaneParam = new JScrollPane(getAreaParam());
           scrollPaneParam.setName("scrollPaneParam");
           scrollPaneParam.setBounds(labelParam.getX()+labelParam.getWidth()+10,labelParam.getY(),scrollPaneDescription.getWidth()-labelParam.getWidth()-10,50);
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

    @Override
    public void actionComment() {
        panelDetails.setSize(panelDetails.getWidth(), panelComments.getHeight()+panelComments.getY());
        setSize(getWidth(),panelDetails.getHeight()+getPanelTitle().getHeight());
        setPreferredSize(getSize());
        revalidate();
        repaint();
        if(action != null)
            this.action.actionResize();
    }

    @Override
    public void saveComment() {
        this.comment = panelComments.getComments() ;
        mUsed.setComment(new LocalText(comment, edP.getLocale()));
    }

    @Override
    public void setComment() {
        this.panelComments.setComments(this.comment);
    }

    

    public MaterialUsed getMaterialUsed(){
        if(areaDescription != null){
            LocalText text = new LocalText(areaDescription.getText(), edP.getLocale());
            mUsed.getMaterial().setDescription(text);
        }
        if(panelComments != null){
            mUsed.setComment(CopexUtilities.getTextLocal(panelComments.getComments(), edP.getLocale()));
        }
        if(materialStrategy.canChooseMaterial())
            mUsed.setUsed(cbUsed.isSelected());
        return mUsed;
    }

}
