/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.utilities;

import colab.vt.whiteboard.component.WhiteboardPanel;
import eu.scy.tools.copex.edp.CopexTree;
import eu.scy.tools.copex.edp.CopexTreeNode;
import eu.scy.tools.copex.edp.MyWhiteBoardPanel;
import javax.swing.tree.*;
import javax.swing.*;
import java.awt.*;
import javax.swing.border.LineBorder;
import org.jdom.Element;

/**
 * objet graphique  qui représente un noeud de l'arbre
 * la structure est la suivante : 
 * - Icone pour representer le noeud ou la feuille
 * - Panel contenant 2 labels : identifiant du noeud / feuille et commentaires associes
 * Pour composer ces 2 objets dans un seul on dit qu'un CellRenderer n'est autre qu'un 
 * JPanel contenant ces 2 objets
 *  MBO le 06/10/08  : ajout une image associée
 * MBO le 15/12/08 : resize texte
 *  MBO le 22/04/09 : ajout du dessin (drawingTool)
 * @author MBO
 */
public class MyTreeCellRenderer extends JPanel implements  TreeCellRenderer {

    // CONSTANTES
    static private Color BG_COLOR = Color.WHITE;
    static private Color COMMENT_COLOR = Color.LIGHT_GRAY;
    static private Color SELECTED_COLOR = UIManager.getColor("Tree.selectionBackground").brighter().brighter();
    static private Font FONT_COMMENT = new Font("Dialog", Font.ITALIC, 10);
    static private Font FONT_NODE = new Font("Dialog", Font.PLAIN, 11);
    /* longueur minimum du texte dans l'arbre, avant declenchement scroll*/
    static private final int TEXT_AREA_MIN_WIDTH = 200;
    static private int HEIGHT_ONE_LINE = 20;
    static private int HEIGHT_ONE_LINE_COMMENT = 19;
    
    
    // ATTRIBUTS
    private CopexTree tree;
    protected JLabel icon;
    protected JPanel panelNode;
    protected JTextArea labelNode;
    protected JTextArea commentNode;
    protected JLabel taskImageNode;
    protected MyWhiteBoardPanel drawPanel;
    private ImageIcon questionIcon ;
    private ImageIcon stepIcon;
    private ImageIcon actionIcon;
    private ImageIcon stepWarningIcon;
    private ImageIcon stepReadOnlyIcon;
    private ImageIcon actionReadOnlyIcon;
    private CopexTreeNode node;
    private Color bgColor;
    
    // CONSTRUCTEURS
    public MyTreeCellRenderer(CopexTree tree){
        super();
        this.tree = tree;
        this.questionIcon = tree.getQuestionImageIcon();
        this.stepIcon = tree.getStepImageIcon();
        this.actionIcon = tree.getActionImageIcon();
        this.stepWarningIcon = tree.getStepWarningImageIcon();
        this.actionReadOnlyIcon = tree.getActionReadOnlyImageIcon();
        this.stepReadOnlyIcon = tree.getStepReadOnlyImageIcon();
        this.commentNode = null;
        setBackground(BG_COLOR);
        this.bgColor = BG_COLOR; 
        // BoxLayout.X_AXIS : les objets graphiques seront disposés de gauche à droite. 
        setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
        //setLayout(null);
        // création de l'icone qui est affiché sur chaque noeud
        icon = new JLabel() ;
        icon.setSize(25, 22);
        icon.setPreferredSize(icon.getSize());
        icon.setAlignmentY(TOP_ALIGNMENT);
        //icon.setBounds(0, 0, icon.getWidth(), icon.getHeight());
        add(icon);

        // separation icone / texte
        add(Box.createHorizontalStrut(5));

        // creation du panel contenant le texte du noeud
        this.panelNode = new JPanel();
        this.panelNode.setLayout(new BoxLayout(panelNode,BoxLayout.Y_AXIS));
        //this.panelNode.setLayout(new FlowLayout(FlowLayout.LEFT));
        //this.panelNode.setLayout(null);
        this.panelNode.setBackground(BG_COLOR);
       // this.panelNode.add(Box.createVerticalStrut(5));
       
        this.labelNode = new JTextArea();
        this.labelNode.setEnabled(false);
        this.labelNode.setLineWrap(true);
        this.labelNode.setWrapStyleWord(true);
        this.labelNode.setMinimumSize(new Dimension(TEXT_AREA_MIN_WIDTH, 10));
        this.labelNode.setMaximumSize(new Dimension(1024, 1024));
        this.labelNode.setFont(FONT_NODE);
        this.labelNode.setFont(FONT_NODE);
        this.labelNode.setDisabledTextColor(Color.BLACK);
        this.labelNode.setOpaque(true);
        //this.labelNode.setBounds(0, 0, labelNode.getWidth(), labelNode.getHeight());
        this.panelNode.add(labelNode);
         // ajout de l'image 
        this.taskImageNode = new JLabel();
        //this.taskImageNode.setBounds(0, this.labelNode.getHeight(), this.taskImageNode.getWidth(), this.taskImageNode.getHeight());
        this.panelNode.add(taskImageNode);
        getDrawPanel();

        getCommentNode();
        //panelNode.setBounds(icon.getX()+icon.getWidth(), 0, panelNode.getWidth(), panelNode.getHeight());
        panelNode.setAlignmentY(TOP_ALIGNMENT);
        add(panelNode);
        // resize
        Dimension d = getPanelPreferredSize() ;
        this.panelNode.setSize(d);
        this.panelNode.setPreferredSize(d);
        Dimension dtot = getPreferredSize() ;
        setSize(dtot);
        setPreferredSize(dtot);
        
    }
    public JTextArea getCommentNode(){
        if (this.commentNode == null){
            this.commentNode = new JTextArea();
            this.commentNode.setEnabled(false);
            this.commentNode.setLineWrap(true);
            this.commentNode.setWrapStyleWord(true);
            this.commentNode.setMinimumSize(new Dimension(TEXT_AREA_MIN_WIDTH, 10));
            this.commentNode.setMaximumSize(new Dimension(1024, 1024));
            commentNode.setFont(FONT_COMMENT);
            commentNode.setForeground(COMMENT_COLOR);
            //this.commentNode.setOpaque(true);
            //this.commentNode.setBounds(0, this.taskImageNode.getY()+this.taskImageNode.getHeight(), this.commentNode.getWidth(), this.commentNode.getHeight());
            this.panelNode.add(commentNode);
        }
        return this.commentNode;
    }

    private MyWhiteBoardPanel getDrawPanel(){
        if (this.drawPanel == null){
            this.drawPanel = new MyWhiteBoardPanel();
            this.drawPanel.setSize(100,100);
            this.drawPanel.setPreferredSize(getSize());
        }
        return drawPanel ;
    }
    
    
    
    // IMPLEMENTS INTERFACE 
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
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
            //drawPanel.getWhiteBoardPanel().setContentStatus(taskDrawElement);
        }else{
            if(drawPanel != null){
                panelNode.remove(drawPanel);
                drawPanel = null;
            }
        }
        if (selected){
            labelNode.setBackground(SELECTED_COLOR);
        }else
            labelNode.setBackground(BG_COLOR);
        // icones : 
        this.setToolTipText(null);
        // ordre de priorité : interdit, warning, lock
        if (value instanceof CopexTreeNode && ( (CopexTreeNode) value ).isRoot() ){
            icon.setIcon(questionIcon);
        }else if (value instanceof CopexTreeNode && ((CopexTreeNode)value).isDisabled() && ((CopexTreeNode)value).isAction()){
            icon.setIcon(actionReadOnlyIcon);
            this.setToolTipText(((CopexTree)tree).getToolTipText((CopexTreeNode)value));
        }else if (value instanceof CopexTreeNode && ((CopexTreeNode)value).isAction()) {
            icon.setIcon(actionIcon);
        } else if (value instanceof CopexTreeNode && ((CopexTreeNode)value).isStepAlone()){
            icon.setIcon(stepWarningIcon);
            String tooltiptextR = "";
            if (((CopexTreeNode)value).isDisabled()){
                tooltiptextR = " <br/>"+((CopexTree)tree).getToolTipText((CopexTreeNode)value);
            }
            this.setToolTipText("<html>"+((CopexTree)tree).getBundleString("TOOLTIPTEXT_STEP_LEAF")+tooltiptextR+"</html>");
        }else if (value instanceof CopexTreeNode && ((CopexTreeNode)value).isDisabled() && ((CopexTreeNode)value).isStep()){
            icon.setIcon(stepReadOnlyIcon);
           this.setToolTipText(((CopexTree)tree).getToolTipText((CopexTreeNode)value));
        }else if (value instanceof CopexTreeNode && ((CopexTreeNode)value).isStep())
            icon.setIcon(stepIcon);
        else if (value instanceof CopexTreeNode && ((CopexTreeNode)value).isQuestion())
            icon.setIcon(questionIcon);
        
        //String textNode = tree.convertValueToText(value, selected, expanded, leaf, row, hasFocus);
        String textNode = ((CopexTree)tree).getDescriptionValue(value);
        setEnabled(tree.isEnabled());
        labelNode.setText(textNode);
        int textWidth = CopexUtilities.lenghtOfString(textNode, getFontMetrics(labelNode.getFont()))+5;
        int nbL = labelNode.getLineCount() ;
        int widthTree = ((CopexTree)tree).getTextWidth(value, row) - icon.getWidth();
        boolean oneLine = false;
        if (textWidth < widthTree){
            widthTree = textWidth ;
            oneLine = true;
        }
        int w = Math.max(widthTree, TEXT_AREA_MIN_WIDTH);
        int heightText = (int)((((float)(textWidth / w))+1)*HEIGHT_ONE_LINE) ;
        if (oneLine)
            heightText = HEIGHT_ONE_LINE;
        heightText = Math.max(nbL*HEIGHT_ONE_LINE, heightText);
        labelNode.setSize(w, heightText);
        
        String comment = ((CopexTree)tree).getCommentValue(value);
        getCommentNode();
        commentNode.setText(comment);
        if (comment == null || comment.length() == 0){
            this.panelNode.remove(commentNode);
            commentNode = null;
        }else{
            widthTree = ((CopexTree)tree).getTextWidth(value, row) - icon.getWidth();
            // taille du text area des commentaires
            int nbLC = commentNode.getLineCount() ;
            int commentTextWidth = CopexUtilities.lenghtOfString(comment, getFontMetrics(commentNode.getFont()))+5;
            boolean oneLineC = false;
            if (commentTextWidth < widthTree){
                widthTree = commentTextWidth ;
                oneLineC = true;
            }
            int cw = Math.max(widthTree, TEXT_AREA_MIN_WIDTH);
            int heightComment = (int)((((float)(commentTextWidth / cw))+1)*HEIGHT_ONE_LINE_COMMENT) ;
            if (oneLineC)
                heightText = HEIGHT_ONE_LINE_COMMENT;
            heightComment = Math.max(nbLC*HEIGHT_ONE_LINE_COMMENT, heightComment);

            
            commentNode.setSize(cw, heightComment);
        }
        this.labelNode.setBounds(0, 0, this.labelNode.getWidth(), this.labelNode.getHeight());
        if(drawPanel != null){
            this.drawPanel.setBounds(0, this.labelNode.getHeight(), this.drawPanel.getWidth(), this.drawPanel.getHeight());
            this.taskImageNode.setBounds(0, this.drawPanel.getHeight(), this.taskImageNode.getWidth(), this.taskImageNode.getHeight());
        }else
            this.taskImageNode.setBounds(0, this.labelNode.getHeight(), this.taskImageNode.getWidth(), this.taskImageNode.getHeight());
        if (commentNode != null){
            this.commentNode.setBounds(0, this.taskImageNode.getY()+this.taskImageNode.getHeight(), this.commentNode.getWidth(), this.commentNode.getHeight());
        }
        if(value instanceof CopexTreeNode &&( (CopexTreeNode) value ).isMouseover()){
            //RoundedBorder border = new RoundedBorder(new Color(0,153, 255), 1, 1);
            //LineBorder border = new LineBorder(new Color(0,153, 255), 1, true);
            Color borderColor = new Color(160,160,160);
            if (UIManager.getDefaults().get("Tree.dropLineColor") instanceof Color)
                borderColor = (Color)UIManager.getDefaults().get("Tree.dropLineColor");
            LineBorder border = new LineBorder(borderColor, 3, true);
            UIManager.getDefaults().get("Tree.DropLineColor");
            this.setBorder(border);
        }else
            this.setBorder(null);
        // resize
        Dimension d = getPanelPreferredSize() ;
        this.panelNode.setSize(d);
        this.panelNode.setPreferredSize(d);
        Dimension dtot = getPreferredSize() ;
        setSize(dtot);
        setPreferredSize(dtot);
        return this;
    }

    // OPERATIONS
    /*
     * redimmensionne suivant ce qui est affiché
     */
    @Override
    public Dimension getPreferredSize() {
        Dimension iconD = icon.getSize();
        Dimension textD = panelNode.getSize();
	
        int height = iconD.height < textD.height ?
					textD.height : iconD.height;
        
        return new Dimension(iconD.width + textD.width+8, height);
        
    }
    
    public Dimension getPanelPreferredSize() {
        Dimension labelNodeD = labelNode.getSize();
        Dimension commentNodeD = new Dimension(0, 0);
        if (this.commentNode != null)
            commentNodeD = commentNode.getSize();
        Dimension taskImageD = taskImageNode.getSize();
        Dimension taskDrawD = new Dimension(0,0);
        if (this.drawPanel != null)
            taskDrawD = drawPanel.getSize();
	
        int height = labelNodeD.height + commentNodeD.height+5 + taskImageD.height + taskDrawD.height;
        //int height = labelNodeD.height + commentNodeD.height+5;
					
        int width = labelNodeD.width < commentNodeD.width ? commentNodeD.width : labelNodeD.width;
        width = width < taskImageD.width ? taskImageD.width : width ;
        return new Dimension(width, height);
        
    }

    
   
}
