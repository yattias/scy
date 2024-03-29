/*
 * CopexButtonPanel.java
 *
 * Created on 28 juillet 2008, 08:23
 */

package eu.scy.client.tools.copex.utilities;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.*;

/**
 * menu button
 * @author  marjolaine
 */
public class CopexButtonPanel extends JPanel {

    /* click action */
    private ActionCopexButton actionCopexButton;
    /*
     * background image
     */
    private Image bg;
    /*
     * image
     */
    private Image bgSimple;
    /*
     * image mouseover
     */
    private Image bgSurvol;
    /*
     * image click
     */
    private Image bgClic;
     /*
     * image disabled
     */
    private Image bgDisabled;
    private boolean modeEnabled = true;

    public CopexButtonPanel(Image bgSimple, Image bgSurvol, Image bgClic, Image bgDisabled) {
        super();
        this.bgSimple = bgSimple;
        this.bg = bgSimple;
        this.bgSurvol = bgSurvol;
        this.bgClic = bgClic;
        this.bgDisabled = bgDisabled;
        this.modeEnabled = true;
        initComponents();
        setSize(bgSimple.getWidth(this), bgSimple.getHeight(this));
        setOpaque(false);
        repaint();
    }
    
    
    /** Creates new form CopexButtonPanel */
    public CopexButtonPanel() {
        initComponents();
    }

    /**
    * Instanciation ActionCopexButton.
    * @param action ActionCopexButton
    */
    public void addActionCopexButton(ActionCopexButton action){
        this.actionCopexButton=action;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (bg == null){
            bg = bgSimple;
        }
        boolean d  =g.drawImage(bg, 0, 0, null) ;
        while (!d){
           d =  g.drawImage(bg, 0, 0, null);
        }
    }

    /*
     * set panel enabled
     */
    @Override
    public void setEnabled(boolean b){
        this.modeEnabled = b;
        if (b){
            this.bg = bgSimple;
        }else{
            this.bg = bgDisabled;
        }
        repaint();
    }
    
    /* update the image */
    public void setIcon(Image updateImg){
        this.bgSimple = updateImg;
        this.bg = updateImg;
        repaint();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                formMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                formMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                formMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                formMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 55, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 39, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

private void formMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseEntered
// TODO add your handling code here:
    if (modeEnabled){
        this.bg = bgSurvol;
        repaint();
    }
}//GEN-LAST:event_formMouseEntered

private void formMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseExited
// TODO add your handling code here:
    if (modeEnabled){
        this.bg = bgSimple;
        repaint();
    }
}//GEN-LAST:event_formMouseExited

private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
// TODO add your handling code here:
    if (modeEnabled){
        this.bg = bgClic;
        actionCopexButton.actionCopexButtonClic(this);
        repaint();
    }
    
}//GEN-LAST:event_formMouseClicked

private void formMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseReleased
// TODO add your handling code here:
    if (modeEnabled){
        this.bg = bgSimple;
        repaint();
    }
}//GEN-LAST:event_formMouseReleased

private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed
// TODO add your handling code here:
   /*  if (modeEnabled){
        this.bg = bgClic;
        repaint();
    }*/
}//GEN-LAST:event_formMousePressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

}
