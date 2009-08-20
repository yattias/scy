/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.utilities;

import eu.scy.tools.dataProcessTool.dataTool.CopexGraph;
import eu.scy.tools.dataProcessTool.dataTool.DataProcessToolPanel;
import eu.scy.tools.dataProcessTool.dataTool.VisualTabbedPane;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;

/**
 * onglet contenant la croix pour fermeture
 * @author MBO
 */
public class CloseTab extends JPanel implements MouseListener, ActionCopexButton {

    // ATTRIBUTS
    
    
    private DataProcessToolPanel owner;
    private CopexGraph graph;
    private VisualTabbedPane tabbedPane;
    private JLabel labelTitle;
    private String title;
    private CopexButtonPanel labelIcon;
    private ImageIcon icon;
    private ImageIcon iconRollOver;
    private ImageIcon iconClic;
    private ImageIcon iconDisabled ;
    private boolean isSelected;
    
    
    // CONSTRUCTEURS
    public CloseTab(DataProcessToolPanel owner,  VisualTabbedPane tabbedPane, CopexGraph graph, String title, ImageIcon icon, ImageIcon iconSurvol, ImageIcon iconClic, ImageIcon iconDisabled){
        this.owner = owner;
        this.tabbedPane = tabbedPane;
        this.graph = graph;
        this.title = title;
        this.icon = icon;
        this.iconClic = iconClic;
        this.iconRollOver = iconSurvol;
        this.iconDisabled = iconDisabled ;
        init();
    }
    // GETTER AND SETTER
    public CopexButtonPanel getButtonClose(){
        return this.labelIcon;
    }
    // METHODES
    private void init(){
        //setBackground(Color.WHITE);
        //setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
        setLayout(null);
         //setAlignmentY(CENTER_ALIGNMENT);
        labelTitle = new JLabel(title);
        int l = MyUtilities.lenghtOfString(title, getFontMetrics(labelTitle.getFont()));
        labelTitle.setBounds(0, 0, l+5, 19);
        add(labelTitle);
        //add(Box.createHorizontalStrut(5));
        labelIcon = new CopexButtonPanel(20, this.icon.getImage(), this.iconRollOver.getImage(), this.iconClic.getImage(), this.iconDisabled.getImage());
        labelIcon.addActionCopexButton(this);
        //labelIcon.setBackground(Color.WHITE);
        Dimension d;
        if (l > 0){
            labelIcon.setBounds(labelTitle.getX()+labelTitle.getWidth()+5, 2, 20, labelTitle.getHeight());
            d = new Dimension(labelTitle.getWidth()+25, 16);
        }else{
            labelIcon.setBounds(0, 2, 20, labelTitle.getHeight());
            d = new Dimension(15, 16);
        }
        add(labelIcon);
        setSize(d);
        setPreferredSize(d);
        setSelected(false);
        this.addMouseListener(this);
    }
    
    /* met le label selectionne */
    public void setSelected(boolean selected){
        this.isSelected = selected;
        Color color;
        if (selected){
            color = Color.WHITE;
        }else{
            //color = new Color(238, 238, 238);
            color = DataProcessToolPanel.backgroundColor;
        }
        setBackground(color);
        labelIcon.setBackground(color);
        repaint();
    }
    
   

    // rend disabled
    public void setDisabled(){
        this.labelIcon.setEnabled(false);
        repaint();
    }
    
    public void mouseClicked(MouseEvent e) {
        if (this.title != null)
            this.tabbedPane.setSelected(this);
        if (e.getClickCount() == 2){
            // double-clic : 
            if (this.title == null || !this.isSelected)
                return;
            owner.doubleClickTab(tabbedPane, this);
        }
    }

    public void mousePressed(MouseEvent e) {
        
    }

    public void mouseReleased(MouseEvent e) {
        
    }

    public void mouseEntered(MouseEvent e) {
        
    }

    public void mouseExited(MouseEvent e) {
        
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    /* mise a  jour du nom du protocole */
    public void updateTitle(String name){
        labelTitle.setText(name);
        this.title = name;
        int l = MyUtilities.lenghtOfString(title, getFontMetrics(labelTitle.getFont()));
        this.labelTitle.setSize(l+5, this.labelTitle.getHeight());
        labelIcon.setBounds(labelTitle.getX()+labelTitle.getWidth()+5, 2, 20, labelTitle.getHeight());
        Dimension d = new Dimension(labelTitle.getWidth()+25, 15);
        setSize(d);
        setPreferredSize(d);
        revalidate();
        repaint();
    }

    @Override
    public void actionCopexButtonClic(CopexButtonPanel button) {
        if(graph == null){
            owner.openDialogCreateVisual();
        }else{
            owner.closeGraph(graph.getVisualization());
        }
    }
}
