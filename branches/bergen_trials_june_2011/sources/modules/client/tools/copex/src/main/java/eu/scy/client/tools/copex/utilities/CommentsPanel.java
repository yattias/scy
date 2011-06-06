/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.utilities;

import eu.scy.client.tools.copex.edp.EdPPanel;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


/**
 * comments panel, display or not the text area comments
 * @author Marjolaine
 */
public class CommentsPanel extends CopexPanelHideShow {
    // ATTRIBUTS
    /* taille */
    private int width;
    /* area comments */
    private JScrollPane scrollPaneComments;
    private JTextArea areaComments;

    /* action de commentaires */
    private ActionComment actionComment;



    // CONSTRUCTOR
    public CommentsPanel(EdPPanel edP, JPanel owner, String titleLabel, int width) {
        super(edP, owner, edP.getBundleString("LABEL_ADD_COMMENT"), true);
        this.width = width ;
        initPanel();
    }


    // METHOD
    private void initPanel(){
        setSize(width, 30);
        setPreferredSize(getSize());
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                resizePanel();
            }
        });
    }

    /**
    * Instancie l'objet ActionComment.
    * @param action ActionComment
    */
    public void addActionComment(ActionComment action){
        this.actionComment=action;
    }

    // OVERRIDE METHODS
    @Override
    protected void setPanelDetailsHide() {
        actionComment.saveComment();
        super.setPanelDetailsHide();
        if (this.panelDetails != null){
            panelDetails.remove(scrollPaneComments);
        }
        scrollPaneComments = null;
        areaComments = null;
        setSize(width, 30);
        setPreferredSize(getSize());
        actionComment.actionComment();
        revalidate();
        repaint();
    }

    @Override
    public void setPanelDetailsShown() {
        if(!isDetails()){
            super.setPanelDetailsShown();
            getPanelDetails().add(getScrollPaneComments());
            panelDetails.setSize(panelDetails.getWidth(), 90);
            setSize(width,120);
            setPreferredSize(getSize());
            actionComment.actionComment();
            actionComment.setComment();
            revalidate();
            repaint();
        }
    }

    private boolean isDetails(){
        return this.scrollPaneComments != null;
    }
    private void resizePanel(){
        if(this.scrollPaneComments != null){
            scrollPaneComments.setSize(this.getWidth(), this.getHeight()-60);
            panelDetails.setSize(this.getWidth(), this.getHeight()-30);
            scrollPaneComments.revalidate();
            panelDetails.revalidate();
            setSize(this.getWidth(), this.getHeight());
            setPreferredSize(getSize());
        }
    }

    @Override
    public void setEnabled(boolean enabled){
        super.setEnabled(enabled);
        if (this.areaComments != null)
            areaComments.setEnabled(enabled);
        setButtonEnabled(enabled);
    }

    private JScrollPane getScrollPaneComments(){
        if (this.scrollPaneComments == null){
            scrollPaneComments = new JScrollPane();
            scrollPaneComments.setViewportView(getAreaComments());
            scrollPaneComments.setBounds(0, 0, width,60 );
        }
        return this.scrollPaneComments ;
    }
    private JTextArea getAreaComments(){
        if (this.areaComments == null){
            areaComments = new JTextArea();
            areaComments.setColumns(20);
            areaComments.setRows(3);
            areaComments.setLineWrap(true);
            areaComments.setWrapStyleWord(true);
            areaComments.addKeyListener(new java.awt.event.KeyAdapter() {
                @Override
                public void keyPressed(java.awt.event.KeyEvent evt) {
                    if (evt.getKeyCode() == KeyEvent.VK_ENTER){
                        actionComment.enterKeyPressed();
                    }
                }
            });
        }
        return areaComments ;
    }

    public void setComments(String text){
        if (text == null || text.length() == 0)
                return;
        if (this.areaComments == null){
            setPanelDetailsShown();
        }
        this.areaComments.setText(text);
    }

    public String getComments(){
        return areaComments == null ?"" : areaComments.getText() ;
    }

    
}
