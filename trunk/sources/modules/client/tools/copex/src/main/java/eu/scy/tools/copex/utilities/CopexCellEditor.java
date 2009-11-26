package eu.scy.tools.copex.utilities;

import eu.scy.tools.copex.edp.CopexNode;
import eu.scy.tools.copex.edp.CopexTree;
import eu.scy.tools.copex.edp.MyWhiteBoardPanel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.EventListenerList;
import javax.swing.tree.TreeCellEditor;
import org.jdom.Element;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * copex tree editor
 * @author Marjolaine
 */
public class CopexCellEditor extends JPanel implements TreeCellEditor{
    private static final Color AREA_BORDER_COLOR = new Color(217,217,217);
    private static final int MIN_HEIGHT_AREA = 70;
    private CopexTree copexTree;
    private JLabel icon;
    private JPanel panelNode;

    private JLabel labelRepeat;
    private JLabel labelNode;
    private JTextArea textNode;
    private JLabel labelComment;
    private JTextArea commentNode;
    private MyWhiteBoardPanel drawPanel;
    protected JLabel taskImageNode;

    private ImageIcon hypothesisIcon;
    private ImageIcon principleIcon;
    private ImageIcon materialIcon;
    private ImageIcon manipulationIcon;
    private ImageIcon datasheetIcon;
    private ImageIcon evaluationIcon;

    private ImageIcon stepIcon;
    private ImageIcon actionIcon;
    private ImageIcon stepWarningIcon;
    private ImageIcon stepReadOnlyIcon;
    private ImageIcon actionReadOnlyIcon;

    private CopexNode node;

    transient private ChangeEvent changeEvent = null;


    public CopexCellEditor(CopexTree copexTree) {
        super();
        listenerList = new EventListenerList();
        this.copexTree = copexTree;
        this.hypothesisIcon = copexTree.getHypothesisImageIcon();
        this.principleIcon = copexTree.getPrincipleImageIcon();
        this.materialIcon = copexTree.getMaterialImageIcon();
        this.manipulationIcon = copexTree.getManipulationImageIcon();
        this.datasheetIcon = copexTree.getDatasheetImageIcon();
        this.evaluationIcon = copexTree.getEvaluationImageIcon();
        this.stepIcon = copexTree.getStepImageIcon();
        this.actionIcon = copexTree.getActionImageIcon();
        this.stepWarningIcon = copexTree.getStepWarningImageIcon();
        this.actionReadOnlyIcon = copexTree.getActionReadOnlyImageIcon();
        this.stepReadOnlyIcon = copexTree.getStepReadOnlyImageIcon();
        // construction panel
        setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
        setBackground(CopexTreeCellRenderer.BG_COLOR);
        setAlignmentY(TOP_ALIGNMENT);
        //icone
        icon = new JLabel() ;
        icon.setSize(38,40);
        icon.setPreferredSize(icon.getSize());
        icon.setAlignmentY(TOP_ALIGNMENT);
        add(icon);
        // label repeat
        this.labelRepeat = new JLabel();
        labelRepeat.setName("labelRepeat");
        labelRepeat.setSize(0, 0);
        labelRepeat.setFont(CopexTreeCellRenderer.FONT_COMMENT);
        labelRepeat.setAlignmentY(TOP_ALIGNMENT);
        add(labelRepeat);
        add(Box.createHorizontalStrut(5));
        // panel node
        this.panelNode = new JPanel();
        //this.panelNode.setLayout(new BoxLayout(panelNode,BoxLayout.Y_AXIS));
        this.panelNode.setLayout(null);
        this.panelNode.setBackground(CopexTreeCellRenderer.BG_COLOR);
        getLabelNode();
        getTextNode();
        getLabelComment();
        getCommentNode();
        this.taskImageNode = new JLabel();
        this.panelNode.add(taskImageNode);
        getDrawPanel();
        add(panelNode);
        // resize
        Dimension d = getPanelPreferredSize() ;
        this.panelNode.setSize(d);
        this.panelNode.setPreferredSize(d);
        Dimension dtot = getPreferredSize() ;
        setSize(dtot);
        setPreferredSize(dtot);
    }

    public JLabel getLabelNode(){
        if(this.labelNode == null){
            labelNode = new JLabel();
            labelNode.setName("labelNode");
            labelNode.setFont(CopexTreeCellRenderer.FONT_INTITULE);
            labelNode.setMinimumSize(new Dimension(0,0));
            this.labelNode.setOpaque(true);
            this.panelNode.add(labelNode);
        }
        return labelNode;
    }
     public JLabel getLabelComment(){
        if(this.labelComment == null){
            labelComment = new JLabel();
            labelComment.setName("labelComment");
            labelComment.setFont(CopexTreeCellRenderer.FONT_INTITULE);
            this.labelComment.setOpaque(true);
            this.labelComment.setBackground(CopexTreeCellRenderer.BG_COLOR);
            this.panelNode.add(labelComment);
        }
        return labelNode;
    }
    public JTextArea getTextNode(){
        if (this.textNode == null){
            this.textNode = new JTextArea();
            this.textNode.setEnabled(false);
            this.textNode.setLineWrap(true);
            this.textNode.setWrapStyleWord(true);
            this.textNode.setMinimumSize(new Dimension(CopexTreeCellRenderer.TEXT_AREA_MIN_WIDTH, 10));
            this.textNode.setMaximumSize(new Dimension(1024, 1024));
            this.textNode.setFont(CopexTreeCellRenderer.FONT_NODE);
            this.textNode.setDisabledTextColor(Color.BLACK);
            this.textNode.setOpaque(true);
            this.textNode.addFocusListener(new FocusListener() {

                @Override
                public void focusGained(FocusEvent e) {
                    //
                }

                @Override
                public void focusLost(FocusEvent e) {
                    //validText();
//                   if(commentNode != null)
//                        commentNode.requestFocus();

                }
            });
            this.panelNode.add(textNode);
        }
        return this.textNode;
    }

    private MyWhiteBoardPanel getDrawPanel(){
        if (this.drawPanel == null){
            this.drawPanel = new MyWhiteBoardPanel();
            this.drawPanel.setSize(100,100);
            this.drawPanel.setPreferredSize(getSize());
        }
        return drawPanel ;
    }


    @Override
    public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row) {
        if(value instanceof CopexNode)
            node = (CopexNode)value;
        // icon
        if(value instanceof CopexNode && ((CopexNode)value).isQuestion()){
            icon.setSize(0,0);
            icon.setPreferredSize(icon.getSize());
        }else{
            icon.setSize(38,40);
            icon.setPreferredSize(icon.getSize());
        }
        if(value instanceof CopexNode && ((CopexNode)value).isHypothesis())
            icon.setIcon(hypothesisIcon);
        else if(value instanceof CopexNode && ((CopexNode)value).isGeneralPrinciple())
            icon.setIcon(principleIcon);
        else if(value instanceof CopexNode && ((CopexNode)value).isMaterial())
            icon.setIcon(materialIcon);
        else if(value instanceof CopexNode && ((CopexNode)value).isManipulation())
            icon.setIcon(manipulationIcon);
        else if(value instanceof CopexNode && ((CopexNode)value).isDatasheet())
            icon.setIcon(datasheetIcon);
        else if(value instanceof CopexNode && ((CopexNode)value).isEvaluation())
            icon.setIcon(evaluationIcon);
        
        // labelNode :
        String txtLabel = ((CopexTree)tree).getIntituleValue(value, true);
        labelNode.setText(txtLabel);
        labelNode.setSize(CopexUtilities.lenghtOfString(labelNode.getText(), labelNode.getFontMetrics(labelNode.getFont())), 14);
        labelNode.setBounds(0, 5, labelNode.getWidth(), labelNode.getHeight());
        if(txtLabel.equals("")){
            labelNode.setSize(0,0);
            labelNode.setBounds(0,0,0,0);
        }
        // panel
        String text = ((CopexTree)tree).getDescriptionValue(value);
        if(text == null){
            if(textNode != null)
                this.panelNode.remove(textNode);
            textNode = null;
        }else{
            getTextNode();
            if(value instanceof CopexNode && ((CopexNode)value).isQuestion())
                this.textNode.setFont(CopexTreeCellRenderer.FONT_QUESTION);
            else
                this.textNode.setFont(CopexTreeCellRenderer.FONT_NODE);
            boolean editable = ((CopexTree)tree).isEditableValue(value);
            textNode.setText(text);
            textNode.setEditable(editable);
            textNode.setEnabled(editable);
            int textWidth = CopexUtilities.lenghtOfString(text, getFontMetrics(textNode.getFont()))+5;
            int nbL = textNode.getLineCount() ;
            int widthTree = ((CopexTree)tree).getTextWidth(value, row) - icon.getWidth();
            boolean oneLine = false;
            if (textWidth < widthTree){
                widthTree = textWidth ;
                oneLine = true;
            }
            int w = Math.max(widthTree, CopexTreeCellRenderer.TEXT_AREA_MIN_WIDTH);
            int heightText = (int)((((float)(textWidth / w))+1)*CopexTreeCellRenderer.HEIGHT_ONE_LINE) ;
            if (oneLine)
                heightText = CopexTreeCellRenderer.HEIGHT_ONE_LINE;
            heightText = Math.max(nbL*CopexTreeCellRenderer.HEIGHT_ONE_LINE, heightText);
            nbL = CopexTreeCellRenderer.getNbLine(text, w, getFontMetrics(textNode.getFont()));
            heightText = nbL*CopexTreeCellRenderer.HEIGHT_ONE_LINE;
            if(editable){
                w = ((CopexTree)tree).getTextWidth(value, row) - icon.getWidth();
                heightText = Math.max(MIN_HEIGHT_AREA, heightText);
                textNode.setBorder(BorderFactory.createLineBorder(AREA_BORDER_COLOR));
            }else
                textNode.setBorder(null);
            textNode.setSize(w, heightText);
            textNode.setBounds(0, labelNode.getHeight()+labelNode.getY(), textNode.getWidth(), textNode.getHeight());
            String toolTipText = ((CopexTree)tree).getToolTipTextValue(value);
            textNode.setToolTipText(toolTipText);
        }
        // comments
        String comment = ((CopexTree)tree).getCommentValue(value);
        getCommentNode();
        commentNode.setText(comment);
        //if (comment == null || comment.length() == 0){
        if (comment == null){
            this.panelNode.remove(commentNode);
            commentNode = null;
        }else{
            boolean editable = ((CopexTree)tree).isEditableValue(value);
            if(editable){
                getLabelComment();
                labelComment.setText(((CopexTree)tree).getCommentLabelText());
                labelComment.setSize(CopexUtilities.lenghtOfString(labelComment.getText(), getFontMetrics(labelComment.getFont())),14);
                labelComment.setBounds(0, this.textNode.getY()+this.textNode.getHeight(), labelComment.getWidth(), labelComment.getHeight());
                commentNode.setFont(CopexTreeCellRenderer.FONT_NODE);
                commentNode.setForeground(Color.BLACK);
            }else{
                this.panelNode.remove(labelComment);
                labelComment = null;
                commentNode.setFont(CopexTreeCellRenderer.FONT_COMMENT);
                commentNode.setForeground(CopexTreeCellRenderer.COMMENT_COLOR);
            }
            commentNode.setEditable(editable);
            commentNode.setEnabled(editable);
            int widthTree = ((CopexTree)tree).getTextWidth(value, row) - icon.getWidth();
            // taille du text area des commentaires
            int nbLC = commentNode.getLineCount() ;
            int commentTextWidth = CopexUtilities.lenghtOfString(comment, getFontMetrics(commentNode.getFont()))+5;
            boolean oneLineC = false;
            if (commentTextWidth < widthTree){
                widthTree = commentTextWidth ;
                oneLineC = true;
            }
            int cw = Math.max(widthTree, CopexTreeCellRenderer.TEXT_AREA_MIN_WIDTH);
            int heightComment = (int)((((float)(commentTextWidth / cw))+1)*CopexTreeCellRenderer.HEIGHT_ONE_LINE_COMMENT) ;
            if (oneLineC)
                heightComment = CopexTreeCellRenderer.HEIGHT_ONE_LINE_COMMENT;
            heightComment = Math.max(nbLC*CopexTreeCellRenderer.HEIGHT_ONE_LINE_COMMENT, heightComment);
            commentNode.setSize(cw, heightComment);
            nbLC = CopexTreeCellRenderer.getNbLine(comment, cw, getFontMetrics(commentNode.getFont()));
            commentNode.setSize(cw, nbLC*CopexTreeCellRenderer.HEIGHT_ONE_LINE_COMMENT);
            if(editable){
                cw = ((CopexTree)tree).getTextWidth(value, row) - icon.getWidth();
                heightComment = Math.max(MIN_HEIGHT_AREA, heightComment);
                commentNode.setBorder(BorderFactory.createLineBorder(AREA_BORDER_COLOR));
                commentNode.setSize(cw, heightComment);
            }else
                commentNode.setBorder(null);
        }
        if (commentNode != null){
            int posy = this.textNode.getY()+this.textNode.getHeight();
            if(labelComment != null)
                posy = this.labelComment.getY()+labelComment.getHeight();
            this.commentNode.setBounds(0, posy, this.commentNode.getWidth(), this.commentNode.getHeight());
        }
        ImageIcon taskImage = ((CopexTree)tree).getImageValue(value);
        if (taskImage != null){
            this.taskImageNode.setIcon(taskImage);
            this.taskImageNode.setSize(taskImage.getIconWidth(), taskImage.getIconHeight());
        }else {
            this.taskImageNode.setIcon(null);
            this.taskImageNode.setSize(0,0);
        }
        Element taskDrawElement = ((CopexTree)tree).getTaskDraw(value);
        if(taskDrawElement != null && taskDrawElement.getChildren() != null && taskDrawElement.getChildren().size() > 0){
            panelNode.add(getDrawPanel()) ;
            drawPanel.getWhiteBoardPanel().setContentStatus(taskDrawElement);
        }else{
            if(drawPanel != null){
                panelNode.remove(drawPanel);
                drawPanel = null;
            }
        }
        // panel node
        if (isSelected){
            labelNode.setBackground(CopexTreeCellRenderer.SELECTED_COLOR);
            if(labelNode.getWidth() == 0 && textNode != null){
                //cas de la question 
                //textNode.setBackground(CopexTreeCellRenderer.SELECTED_COLOR);
            }
        }else{
            labelNode.setBackground(CopexTreeCellRenderer.BG_COLOR);
            if(textNode != null)
                textNode.setBackground(CopexTreeCellRenderer.BG_COLOR);
        }
        // resize
        Dimension d = getPanelPreferredSize() ;
        this.panelNode.setSize(d);
        this.panelNode.setPreferredSize(d);
        Dimension dtot = getPreferredSize() ;
        setSize(dtot);
        setPreferredSize(dtot);
        return this;
    }

    @Override
    public Object getCellEditorValue() {
        return this;
    }

    @Override
    public boolean isCellEditable(EventObject anEvent) {
        boolean isEditable = copexTree.isPathEditable(null);
        if(isEditable){
            if(anEvent instanceof MouseEvent){
                return ((MouseEvent)anEvent).getClickCount()==2;
            }
            return isEditable;
        }
        return isEditable;
    }

    @Override
    public boolean shouldSelectCell(EventObject anEvent) {
        return true;
    }

    @Override
    public boolean stopCellEditing() {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==CellEditorListener.class) {
            // Lazily create the event:
            if (changeEvent == null)
                changeEvent = new ChangeEvent(this);
            ((CellEditorListener)listeners[i+1]).editingStopped(changeEvent);
            }	       
        }
        return true;
    }

    @Override
    public void cancelCellEditing() {
        validText();
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==CellEditorListener.class) {
            // Lazily create the event:
            if (changeEvent == null)
                changeEvent = new ChangeEvent(this);
            ((CellEditorListener)listeners[i+1]).editingCanceled(changeEvent);
            }
        }
    }

    @Override
    public void addCellEditorListener(CellEditorListener l) {
        listenerList.add(CellEditorListener.class, l);
    }

    @Override
    public void removeCellEditorListener(CellEditorListener l) {
        listenerList.remove(CellEditorListener.class, l);
    }

    public JTextArea getCommentNode(){
        if (this.commentNode == null){
            this.commentNode = new JTextArea();
            this.commentNode.setEnabled(false);
            this.commentNode.setLineWrap(true);
            this.commentNode.setWrapStyleWord(true);
            this.commentNode.setMinimumSize(new Dimension(0, 0));
            this.commentNode.setMaximumSize(new Dimension(1024, 1024));
            commentNode.setFont(CopexTreeCellRenderer.FONT_COMMENT);
            commentNode.setForeground(CopexTreeCellRenderer.COMMENT_COLOR);
            //this.commentNode.setOpaque(true);
            //this.commentNode.setBounds(0, this.taskImageNode.getY()+this.taskImageNode.getHeight(), this.commentNode.getWidth(), this.commentNode.getHeight());
            this.panelNode.add(commentNode);
            
        }
        return this.commentNode;
    }
    
    @Override
    public Dimension getPreferredSize() {
        Dimension iconD = icon.getSize();
        Dimension repeatD = labelRepeat.getSize();
        Dimension textD = panelNode.getSize();

        int height = iconD.height < textD.height ?
					textD.height : iconD.height;
        return new Dimension(iconD.width +repeatD.width+ textD.width+8, height);

    }

    public Dimension getPanelPreferredSize() {
        Dimension labelNodeD = labelNode.getSize();
        Dimension labelCommentD  = new Dimension(0,0);
        if(labelComment != null)
            labelCommentD = labelComment.getSize();
        Dimension commentNodeD = new Dimension(0, 0);
        if (this.commentNode != null)
            commentNodeD = commentNode.getSize();
        Dimension taskImageD = taskImageNode.getSize();
        Dimension taskDrawD = new Dimension(0,0);
        if (this.drawPanel != null)
            taskDrawD = drawPanel.getSize();
        Dimension textNodeD = new Dimension(0,0);
        if(this.textNode != null){
            textNodeD = this.textNode.getSize();
        }
        int height = labelNodeD.height + textNodeD.height+labelCommentD.height+commentNodeD.height+5 + taskImageD.height + taskDrawD.height;
        //int height = labelNodeD.height + commentNodeD.height+5;

        int width = labelNodeD.width < commentNodeD.width ? commentNodeD.width : labelNodeD.width;
         width = width < labelCommentD.width ? labelCommentD.width : width ;
        width = width < taskImageD.width ? taskImageD.width : width ;
        width = width < textNodeD.width ? textNodeD.width : width ;
        return new Dimension(width, height);

    }
    public void saveText(){
        this.copexTree.saveNodeText(node, textNode.getText());
    }
    public void validText(){
        String c = "";
        if(commentNode != null)
            c = commentNode.getText();
        this.copexTree.setNodeText(node, textNode.getText(),c);
    }

    

}
