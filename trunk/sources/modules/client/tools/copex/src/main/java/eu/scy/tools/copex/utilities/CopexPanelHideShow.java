/*
 * CopexPanelHideShow.java
 *
 * Created on 28 juillet 2008, 16:02
 */

package eu.scy.tools.copex.utilities;

import eu.scy.tools.copex.edp.EdPPanel;
import java.awt.Dimension;
import javax.swing.*;

/**
 *
 * @author  MBO
 */
public class CopexPanelHideShow extends JPanel {
    // ATTRIBUTS
    static public int HEIGHT_PANEL_HIDE = 30;
    public int HEIGHT_PANEL_SHOWN = 100;
    protected EdPPanel edP;
    protected JPanel  owner;
    protected String titleLabel = "libelle";
    protected JPanel panelDetails = null;
    /* mode */
    private boolean modeHide;
    /* bouton fleche */
    protected ButtonArrowPanel buttonArrow;

    /* ecoute ou non evenemtn souris */
    protected boolean mouseListen;

    public CopexPanelHideShow(EdPPanel edP, JPanel owner, String titleLabel, boolean mouseListen) {
        super();
        this.edP = edP;
        this.owner = owner;
        this.titleLabel = titleLabel;
        this.mouseListen = mouseListen ;
        initComponents();
        initPanel();
    }
    
    
    /** Creates new form CopexPanelHideShow */
    public CopexPanelHideShow() {
        initComponents();
    }

   
    // retourne la position de fin du label
    protected int getLabelEndX(){
        return this.labelTitle.getX()+this.labelTitle.getWidth();
    }
    private void initPanel(){
        modeHide = true;
        HEIGHT_PANEL_SHOWN = owner.getHeight() / 2 - (2*MySeparator.HEIGHT_SEP) ;
        panelDetails = null;
        buttonArrow = new ButtonArrowPanel(edP, this);
        buttonArrow.setBounds(5, 7, buttonArrow.getWidth
                (), buttonArrow.getHeight());
        this.panelTitle.add(buttonArrow);
        this.labelTitle.setSize(CopexUtilities.lenghtOfString(this.labelTitle.getText(), getFontMetrics(this.labelTitle.getFont())), this.labelTitle.getHeight());
        panelTitle.setSize(labelTitle.getWidth()+labelTitle.getX()+5, panelTitle.getHeight());
        setSize(owner.getWidth(), panelTitle.getHeight());
        setPreferredSize(new Dimension(owner.getWidth(), panelTitle.getHeight()));
    }
    protected JPanel getPanelDetails(){
        if (panelDetails == null){
            panelDetails = new JPanel();
            panelDetails.setLayout(null);
            panelDetails.setBounds(0, this.panelTitle.getHeight(), this.getWidth(), HEIGHT_PANEL_SHOWN);
        }
        return panelDetails;
    }
   
    protected JPanel getPanelTitle(){
        return this.panelTitle ;
    }
    /* */
    public void setHide(boolean hide){
        if (hide)
            setPanelDetailsHide();
        else
            setPanelDetailsShown();
        
    }
    /*
     * cache le panel detail
     */
    protected void setPanelDetailsHide(){
        if (panelDetails != null)
            this.remove(panelDetails);
        panelDetails = null;
        buttonArrow.setHide();
        modeHide = true;
        setSize(this.getWidth(), this.panelTitle.getHeight());
    }
    /*
     * montre le panel detail
     */
    protected void setPanelDetailsShown(){
        this.add(getPanelDetails());
        //setSize(this.getWidth(), this.panelTitle.getHeight() +  this.panelDetails.getHeight() );
        setSize(this.getWidth(),HEIGHT_PANEL_SHOWN);
        setPreferredSize(getSize());
        modeHide = false;
        buttonArrow.setShow();
    }
    /* clic sur titre => deploie ou non le panel*/
    protected void labelMousePressed(){
        if(this.mouseListen){
            setHide(!modeHide);
        }
    }
    
    /* resize du panel de detail */
    protected void resizePanelDetails(int newWidth, int newHeight){
        int nw = owner.getWidth();
        int nh = this.panelTitle.getHeight();
        if (this.panelDetails != null){
            nh = newHeight;
        }
        nh = Math.max(nh, this.panelTitle.getHeight());
        Dimension d = new Dimension(nw, nh);
        setSize(d);
        setPreferredSize(d);
        panelTitle.setSize(nw, panelTitle.getHeight());
        if (panelDetails != null){
            panelDetails.setSize(nw, nh - panelTitle.getHeight());
            panelDetails.setPreferredSize(new Dimension(nw, nh - panelTitle.getHeight()));
        }
    }
   /**/
    protected void setButtonEnabled(boolean enabled){
        this.buttonArrow.setButtonEnabled(enabled);
    }

    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelTitle = new javax.swing.JPanel();
        labelTitle = new javax.swing.JLabel();

        setPreferredSize(new java.awt.Dimension(100, 50));
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });
        setLayout(null);

        panelTitle.setName("panelTitle"); // NOI18N
        panelTitle.setLayout(null);

        labelTitle.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labelTitle.setText(this.titleLabel);
        labelTitle.setName("labelTitle"); // NOI18N
        labelTitle.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                labelTitleMousePressed(evt);
            }
        });
        panelTitle.add(labelTitle);
        labelTitle.setBounds(25, 5, 100, 14);

        add(panelTitle);
        panelTitle.setBounds(0, 0, 130, 30);
    }// </editor-fold>//GEN-END:initComponents

private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
    // resize du panel : 
    //resizePanelDetails(owner.getWidth(), 130);
}//GEN-LAST:event_formComponentResized

private void labelTitleMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelTitleMousePressed
    labelMousePressed();
}//GEN-LAST:event_labelTitleMousePressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel labelTitle;
    private javax.swing.JPanel panelTitle;
    // End of variables declaration//GEN-END:variables

}
