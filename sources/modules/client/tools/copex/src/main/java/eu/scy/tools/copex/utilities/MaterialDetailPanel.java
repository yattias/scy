/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.utilities;

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
public class MaterialDetailPanel extends CopexPanelHideShow implements ActionCopexButton {
    private MaterialUsed mUsed;
    private MaterialStrategy materialStrategy;

    private JCheckBox cbUsed;
    private CopexButtonPanel buttonRemove;
    private JLabel labelName;
    private JScrollPane scrollPaneDescription;
    private JTextArea areaDescription;
    private JLabel labelType;
    private JLabel labelType2;
    private JLabel labelParam;
    private JTextArea areaParam;
    private CommentsPanel panelComments;

    private ActionMaterialDetail action;

    /* Images */
    private ImageIcon imgRemove;
    private ImageIcon imgRemoveClic;
    private ImageIcon imgRemoveSurvol;


    public MaterialDetailPanel(EdPPanel edP, JPanel owner, MaterialUsed mUsed, MaterialStrategy materialStrategy) {
        super(edP, owner, "", false);
        this.mUsed = mUsed;
        this.materialStrategy = materialStrategy;
        initGUI();
    }

    private void initGUI(){
        imgRemove = edP.getCopexImage("Bouton-mat_suppr.png");
        imgRemoveClic = edP.getCopexImage("Bouton-mat_suppr_clic.png");
        imgRemoveSurvol = edP.getCopexImage("Bouton-mat_suppr_survol.png");
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
    public void addActionComment(ActionMaterialDetail action){
        this.action=action;
    }

    private JCheckBox getCBUsed(){
        if(cbUsed == null){
            cbUsed = new JCheckBox();
            cbUsed.setName("cbUsed");
            cbUsed.setSelected(mUsed.isUsed());
            cbUsed.setFont(new java.awt.Font("Tahoma", 1, 11));
            cbUsed.setBounds(30,0,20, 20);
        }
        return cbUsed;
    }

    private CopexButtonPanel getButtonRemove(){
        if(buttonRemove == null){
            buttonRemove = new CopexButtonPanel(22, imgRemove.getImage(),imgRemoveSurvol.getImage(), imgRemoveClic.getImage(), imgRemove.getImage() );
            buttonRemove.setBounds(cbUsed.getX()+cbUsed.getWidth()+5,cbUsed.getY(), buttonRemove.getWidth(), buttonRemove.getHeight());
            buttonRemove.addActionCopexButton(this);
        }
        return buttonRemove;
    }

    private JLabel getLabelName(){
        if(labelName == null){
            labelName = new JLabel();
            labelName.setName("labelName");
            labelName.setFont(new java.awt.Font("Tahoma", 1, 11));
            labelName.setText(mUsed.getMaterial().getName());
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
            areaDescription.setText(mUsed.getMaterial().getDescription());
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
        panelDetails.setSize(panelDetails.getWidth(), 110);
        setSize(getWidth(),panelDetails.getHeight()+getPanelTitle().getHeight());
        setPreferredSize(getSize());
        revalidate();
        repaint();
        if(action != null)
            this.action.actionResize();
    }

    @Override
    protected void setPanelDetailsHide() {
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

}
