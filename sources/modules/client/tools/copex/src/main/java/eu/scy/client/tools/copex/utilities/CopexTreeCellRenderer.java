package eu.scy.client.tools.copex.utilities;

import eu.scy.client.tools.copex.edp.CopexNode;
import eu.scy.client.tools.copex.edp.CopexTree;
import eu.scy.client.tools.copex.edp.MyWhiteBoardPanel;

import eu.scy.client.tools.copex.edp.TaskTreeNode;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.tree.TreeCellRenderer;
import org.jdom.Element;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * objet graphique  qui represente un noeud de l'arbre
 * la structure est la suivante :
 * - Icone pour representer le noeud ou la feuille
 * - Panel
 * Pour composer ces 2 objets dans un seul on dit qu'un CellRenderer n'est autre qu'un
 * JPanel contenant ces 2 objets
 * @author Marjolaine
 */
public class CopexTreeCellRenderer extends JPanel implements  TreeCellRenderer  {
    static public Color BG_COLOR = Color.WHITE;
    static public Color COMMENT_COLOR = Color.LIGHT_GRAY;
    static public Color SELECTED_COLOR = new Color(167, 225, 255);
    static public Color TITLE_NODE_COLOR = new Color(85,112,138);
    static public Font FONT_COMMENT = new Font("Dialog", Font.ITALIC, 10);
    static public Font FONT_NODE = new Font("Dialog", Font.PLAIN, 11);
    static public Font FONT_QUESTION = new Font("Dialog", Font.PLAIN, 11);
    static public Font FONT_NODE_DEFAULT_TEXT = new Font("Dialog", Font.ITALIC, 11);
    static public Font FONT_INTITULE= new Font("Dialog", Font.ITALIC,13);
    /* longueur minimum du texte dans l'arbre, avant declenchement scroll*/
    static public final int TEXT_AREA_MIN_WIDTH = 200;
    static public int HEIGHT_ONE_LINE = 15;
    static public int HEIGHT_ONE_LINE_COMMENT = 14;


    private CopexTree copexTree;

    private JLabel icon;
    private JPanel panelNode;

    private JLabel labelRepeat;
    private JLabel labelNode;
    private JTextArea textNode;
    private JTextArea commentNode;
    private MyWhiteBoardPanel drawPanel;
    protected JLabel taskImageNode;
    private TextTable materialTable;

    private ImageIcon questionIcon;
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


    public CopexTreeCellRenderer(final CopexTree copexTree) {
        super();
        this.copexTree = copexTree;
        // recuperation des images
        this.questionIcon = copexTree.getQuestionImageIcon();
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
        setBackground(BG_COLOR);
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
        labelRepeat.setFont(FONT_COMMENT);
        labelRepeat.setAlignmentY(TOP_ALIGNMENT);
        add(labelRepeat);
        // separation icone / texte
        add(Box.createHorizontalStrut(5));
        // panel node
        this.panelNode = new JPanel();
        //this.panelNode.setLayout(new BoxLayout(panelNode,BoxLayout.Y_AXIS));
        this.panelNode.setLayout(null);
        this.panelNode.setBackground(BG_COLOR);
        getLabelNode();
        getTextNode();
        getMaterialTable();
        getCommentNode();
        // ajout de l'image
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

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        this.setToolTipText(null);
        // icon
//        if(value instanceof CopexNode && ((CopexNode)value).isQuestion()){
//            icon.setSize(0,0);
        if(value instanceof TaskTreeNode){
            icon.setSize(25, 22);
        }else{
            icon.setSize(38,40);
        }
        icon.setPreferredSize(icon.getSize());
        if(value instanceof CopexNode && ((CopexNode)value).isQuestion())
            icon.setIcon(questionIcon);
        else if(value instanceof CopexNode && ((CopexNode)value).isHypothesis())
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
        // ordre de priorite : interdit, warning, lock
        else if (value instanceof TaskTreeNode && ((TaskTreeNode)value).isDisabled() && ((TaskTreeNode)value).isAction()){
            icon.setIcon(actionReadOnlyIcon);
            this.setToolTipText(((CopexTree)tree).getToolTipText((TaskTreeNode)value));
        }else if (value instanceof TaskTreeNode && ((TaskTreeNode)value).isAction()) {
            icon.setIcon(actionIcon);
        } else if (value instanceof TaskTreeNode && ((TaskTreeNode)value).isStepAlone() && !((CopexTree)tree).isTaskProc()){
            icon.setIcon(stepWarningIcon);
            String tooltiptextR = "";
            if (((TaskTreeNode)value).isDisabled()){
                tooltiptextR = " <br/>"+((CopexTree)tree).getToolTipText((TaskTreeNode)value);
            }
            this.setToolTipText("<html>"+((CopexTree)tree).getBundleString("TOOLTIPTEXT_STEP_LEAF")+tooltiptextR+"</html>");
        }else if (value instanceof TaskTreeNode && ((TaskTreeNode)value).isDisabled() && ((TaskTreeNode)value).isStep()){
            icon.setIcon(stepReadOnlyIcon);
           this.setToolTipText(((CopexTree)tree).getToolTipText((TaskTreeNode)value));
        }else if (value instanceof TaskTreeNode && ((TaskTreeNode)value).isStep())
            icon.setIcon(stepIcon);
        // labelNode :
        String txtLabel = ((CopexTree)tree).getIntituleValue(value);
        labelNode.setText(txtLabel);
        labelNode.setSize(CopexUtilities.lenghtOfString(labelNode.getText(), labelNode.getFontMetrics(labelNode.getFont())), 14);
        labelNode.setForeground(TITLE_NODE_COLOR);
        labelNode.setBounds(0, 5, labelNode.getWidth(), labelNode.getHeight());
        if(txtLabel.equals("")){
            labelNode.setSize(0,0);
            labelNode.setBounds(0,0,0,0);
        }
        //label Repeat
        int nbRepeat = ((CopexTree)tree).getNbRepeat(value);
        if(nbRepeat > 1){
            labelRepeat.setText("("+nbRepeat+"*)");
            labelRepeat.setSize(34, 14);
        }else{
            labelRepeat.setText("");
            labelRepeat.setSize(0,0);
        }
        // panel
        String text = ((CopexTree)tree).getDescriptionValue(value);
        if(text == null){
            if(textNode != null){
                this.panelNode.remove(textNode);
            }
            textNode = null;
            List<String> listMaterial = ((CopexTree)tree).getMaterialValue(value);
            if(listMaterial != null && listMaterial.size() > 0){
                if(materialTable == null)
                    getMaterialTable();
                materialTable.setTextList(listMaterial);
                materialTable.setNbCol(4);
                materialTable.setBounds(0, labelNode.getHeight()+labelNode.getY(),materialTable.getWidth(), materialTable.getHeight());
            }else{
                if(materialTable != null){
                    panelNode.remove(materialTable);
                }
                materialTable = null;
            }

        }else{
            if(materialTable != null){
                panelNode.remove(materialTable);
            }
            materialTable = null;
            int hl = HEIGHT_ONE_LINE;
            String defaultText = ((CopexTree)tree).getDefaultDescriptionValue(value);
            getTextNode();
            if(text.length() == 0 && defaultText != null){
                text = defaultText;
                this.textNode.setFont(FONT_NODE_DEFAULT_TEXT);
            }else{
                if(value instanceof CopexNode && ((CopexNode)value).isQuestion()){
                    this.textNode.setFont(FONT_QUESTION);
                    //hl = 17;
                }else
                    this.textNode.setFont(FONT_NODE);
            }
            boolean editable = ((CopexTree)tree).isEditableValue(value);
            //setEnabled(tree.isEnabled());
            textNode.setText(text);
            textNode.setEditable(editable);
            textNode.setEnabled(true);
            int textWidth = CopexUtilities.lenghtOfString(text, textNode.getFontMetrics(textNode.getFont()))+5;
            int nbL = textNode.getLineCount() ;
            int widthTree = ((CopexTree)tree).getTextWidth(value, row) - icon.getWidth();
            boolean oneLine = false;
            //System.out.println("widthTree, textWidth "+widthTree+", "+textWidth);
            if (textWidth < widthTree){
                widthTree = textWidth ;
                oneLine = true;
            }
            //System.out.println("oneLine "+oneLine);
            int w = Math.max(widthTree, TEXT_AREA_MIN_WIDTH);
            int heightText = (int)((((float)(textWidth / w))+1)*hl) ;
            if (oneLine)
                heightText = hl;
            heightText = Math.max(nbL*hl, heightText);
            textNode.setSize(w, heightText);
            //System.out.println("heightText "+heightText);
            nbL = getNbLine(text, w-5, textNode.getFontMetrics(textNode.getFont()));
            //System.out.println("nbL "+nbL);
            //textNode.setSize(w, nbL*hl+5);
            //textNode.setSize(w, nbL*hl);
            textNode.setBounds(0, labelNode.getHeight()+labelNode.getY(), textNode.getWidth(), textNode.getHeight());
        }
        // comments
        String comment = ((CopexTree)tree).getCommentValue(value);
        getCommentNode();
        commentNode.setText(comment);
        if (comment == null || comment.length() == 0){
            this.panelNode.remove(commentNode);
            commentNode = null;
        }else{
            int widthTree = ((CopexTree)tree).getTextWidth(value, row) - icon.getWidth();
            // taille du text area des commentaires
            int nbLC = commentNode.getLineCount() ;
            int commentTextWidth = CopexUtilities.lenghtOfString(comment, commentNode.getFontMetrics(commentNode.getFont()))+5;
            boolean oneLineC = false;
            if (commentTextWidth < widthTree){
                widthTree = commentTextWidth ;
                oneLineC = true;
            }
            int cw = Math.max(widthTree, TEXT_AREA_MIN_WIDTH);
            int heightComment = (int)((((float)(commentTextWidth / cw))+1)*HEIGHT_ONE_LINE_COMMENT) ;
            if (oneLineC)
                heightComment = HEIGHT_ONE_LINE_COMMENT;
            heightComment = Math.max(nbLC*HEIGHT_ONE_LINE_COMMENT, heightComment);
            commentNode.setSize(cw, heightComment);
            nbLC = getNbLine(comment, cw-5, commentNode.getFontMetrics(commentNode.getFont()));
            commentNode.setSize(cw, nbLC*HEIGHT_ONE_LINE_COMMENT+5);
        }
        if (commentNode != null){
            this.commentNode.setBounds(0, this.textNode.getY()+this.textNode.getHeight(), this.commentNode.getWidth(), this.commentNode.getHeight());
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
            if(drawPanel != null){
                drawPanel.removeAll();
                panelNode.remove(drawPanel);
                drawPanel = null;
            }
            panelNode.add(getDrawPanel()) ;
            drawPanel.getWhiteBoardPanel().setContentStatus(taskDrawElement);
            Rectangle2D r = drawPanel.getWhiteBoardPanel().getEnclosingScreenRectangle();
            int y = 0;
            if(textNode != null)
                y = textNode.getY()+textNode.getHeight();
            if(commentNode != null)
                y = commentNode.getY()+commentNode.getHeight();
            drawPanel.setBounds(0, y, (int)(r.getBounds().getWidth()+r.getBounds().getX())+30, (int)(r.getBounds().getHeight()+r.getBounds().getY()) + 30);
            drawPanel.repaint();
        }else{
            if(drawPanel != null){
                drawPanel.removeAll();
                panelNode.remove(drawPanel);
                drawPanel = null;
            }
        }
        // selection
        if (selected){
            labelNode.setBackground(SELECTED_COLOR);
            if(labelNode.getWidth() == 0 && textNode != null){
                //cas de la question
                textNode.setBackground(SELECTED_COLOR);
            }
        }else{
            labelNode.setBackground(new Color(0,0,0,0));
            labelNode.setBackground(BG_COLOR);
            if(textNode != null){
                textNode.setBackground(BG_COLOR);
                textNode.setBackground(new Color(0,0,0,0));
            }
        }
        //drag & drop
        if(value instanceof TaskTreeNode &&( (TaskTreeNode) value ).isMouseover()){
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

    public static  int getNbLine(String t, int width, FontMetrics fm){
        String text = new String(t);
        //int nbL = (int)(((float)(CopexUtilities.lenghtOfString(t, fm) / width))+1);
        int nbL = 1;
        int id1 = 0;
        int id2 = text.indexOf("\n");
        while(id2 != -1 && id2> id1){
            String s = text.substring(id1, id2);
            nbL += (int)(((float)(CopexUtilities.lenghtOfString(s, fm) / width))+1);
            text = text.substring(id2+1);
            id2 = text.indexOf("\n");
        }
        if(id2 != -1){
            nbL += (int)(((float)(CopexUtilities.lenghtOfString(text.substring(id2, text.length()), fm) / width))+1);
        }
        if(nbL == 1 ){
            nbL = (int)(((float)(CopexUtilities.lenghtOfString(t, fm) / width))+1);
        }
        return nbL;
    }
    public JLabel getLabelNode(){
        if(this.labelNode == null){
            labelNode = new JLabel();
            labelNode.setName("labelNode");
            labelNode.setFont(FONT_INTITULE);
            labelNode.setMinimumSize(new Dimension(0,0));
            this.labelNode.setOpaque(true);
            this.panelNode.add(labelNode);
        }
        return labelNode;
    }

    public JTextArea getTextNode(){
        if (this.textNode == null){
            this.textNode = new JTextArea();
            this.textNode.setEnabled(true);
            this.textNode.setLineWrap(true);
            this.textNode.setWrapStyleWord(true);
            this.textNode.setMinimumSize(new Dimension(0,0));
            this.textNode.setMaximumSize(new Dimension(2147483647, 2147483647));
            this.textNode.setFont(FONT_NODE);
            this.textNode.setOpaque(false);
            textNode.setBackground(new Color(0,0,0,0));
            this.textNode.setDisabledTextColor(Color.BLACK);
            this.textNode.setBackground(BG_COLOR);
            this.textNode.setBorder(null);
            this.panelNode.add(textNode);
        }
        return this.textNode;
    }

    public TextTable getMaterialTable(){
        if(materialTable == null){
            materialTable = new TextTable(new LinkedList(), 4);
            materialTable.setName("materialTable");
            this.panelNode.add(materialTable);
        }
        return this.materialTable;
    }

    
    public JTextArea getCommentNode(){
        if (this.commentNode == null){
            this.commentNode = new JTextArea();
            this.commentNode.setEnabled(false);
            this.commentNode.setLineWrap(true);
            this.commentNode.setWrapStyleWord(true);
            this.commentNode.setMinimumSize(new Dimension(0, 0));
            this.commentNode.setMaximumSize(new Dimension(2147483647, 2147483647));
            commentNode.setFont(FONT_COMMENT);
            commentNode.setForeground(COMMENT_COLOR);
            this.commentNode.setBorder(null);
            this.commentNode.setBackground(BG_COLOR);
            commentNode.setBackground(new Color(0,0,0,0));
            this.commentNode.setOpaque(false);
            //this.commentNode.setBounds(0, this.taskImageNode.getY()+this.taskImageNode.getHeight(), this.commentNode.getWidth(), this.commentNode.getHeight());
            this.panelNode.add(commentNode);
        }
        return this.commentNode;
    }

    private MyWhiteBoardPanel getDrawPanel(){
        if (this.drawPanel == null){
            this.drawPanel = new MyWhiteBoardPanel();
            //this.drawPanel.setSize(100,100);
            this.drawPanel.setPreferredSize(getSize());
        }
        return drawPanel ;
    }
    

    @Override
    public Dimension getPreferredSize() {
        Dimension iconD = icon.getSize();
        Dimension repeatD = labelRepeat.getSize();
        Dimension textD = getPanelPreferredSize();
        int height = iconD.height < textD.height ?
					textD.height : iconD.height;
        return new Dimension(iconD.width +repeatD.width+ textD.width+8, height);
    }

    public Dimension getPanelPreferredSize() {
        Dimension labelNodeD = labelNode.getSize();
        Dimension commentNodeD = new Dimension(0, 0);
        if (this.commentNode != null)
            commentNodeD = commentNode.getSize();
        Dimension textNodeD = new Dimension(0,0);
        if(this.textNode != null){
            textNodeD = this.textNode.getSize();
        }
        Dimension taskImageD = taskImageNode.getSize();
        Dimension taskDrawD = new Dimension(0,0);
        if (this.drawPanel != null)
            taskDrawD = drawPanel.getSize();
        Dimension materialTableD = new Dimension(0,0);
        if(this.materialTable != null){
            materialTableD = this.materialTable.getSize();
        }
        int height = labelNodeD.height + textNodeD.height+materialTableD.height + commentNodeD.height+5 + taskImageD.height + taskDrawD.height;
        //int height = labelNodeD.height + commentNodeD.height+5;

        int width = labelNodeD.width < commentNodeD.width ? commentNodeD.width : labelNodeD.width;
        width = width < taskImageD.width ? taskImageD.width : width ;
        width = width < taskDrawD.width ? taskDrawD.width : width ;
        width = width < textNodeD.width ? textNodeD.width : width ;
        width = width < materialTableD.width ? materialTableD.width : width ;
        return new Dimension(width, height);
    }

    

}
