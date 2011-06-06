package eu.scy.client.tools.dataProcessTool.utilities;

import eu.scy.client.tools.dataProcessTool.common.Dataset;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Marjolaine
 */
public class CloseTab extends JPanel implements MouseListener, ActionCopexButton{
    private ActionCloseTab action;
    private Dataset ds;
    private JLabel labelTitle;
    private String title;
    private CopexButtonPanel labelIcon;
    private ImageIcon icon;
    private ImageIcon iconRollOver;
    private ImageIcon iconClic;
    private ImageIcon iconDisabled ;
    private boolean isSelected;
    private String toolTipText;
    private Color bgColor;
    private Color bgSelColor;
    private boolean canClose;

    public CloseTab(Dataset ds, Color bgColor,Color bgSelColor,String title, ImageIcon icon, ImageIcon iconSurvol, ImageIcon iconClic, ImageIcon iconDisabled, String toolTipText, boolean canClose){
        this.ds = ds;
        this.bgColor = bgColor;
        this.bgSelColor = bgSelColor;
        this.title = title;
        this.icon = icon;
        this.iconClic = iconClic;
        this.iconRollOver = iconSurvol;
        this.iconDisabled = iconDisabled ;
        this.toolTipText = toolTipText;
        this.canClose = canClose;
        init();
    }

    private void init(){
        setLayout(null);
        labelTitle = new JLabel(title);
        int l = MyUtilities.lenghtOfString(title, getFontMetrics(labelTitle.getFont()));
        labelTitle.setBounds(0, 0, l+5, 19);
        add(labelTitle);
        labelIcon = new CopexButtonPanel(this.icon.getImage(), this.iconRollOver.getImage(), this.iconClic.getImage(), this.iconDisabled.getImage());
        labelIcon.addActionCopexButton(this);
        Dimension d;
        if (l > 0){
            labelIcon.setBounds(labelTitle.getX()+labelTitle.getWidth()+5, 2, 20, labelTitle.getHeight());
            d = new Dimension(labelTitle.getWidth()+25, 16);
        }else{
            labelIcon.setBounds(0, 2, 20, labelTitle.getHeight());
            d = new Dimension(15, 16);
        }
        labelIcon.setToolTipText(toolTipText);
        if(canClose)
            add(labelIcon);
        setOpaque(false);
        setSize(d);
        setPreferredSize(d);
        setSelected(false);
        this.addMouseListener(this);
    }

    /**
    * Instanciation ActionCloseTab.
    * @param action ActionCloseTab
    */
    public void addActionCloseTab(ActionCloseTab action){
        this.action=action;
    }


    /* set the label selected */
    public void setSelected(boolean selected){
        this.isSelected = selected;
        Color color;
        if (selected){
            color = bgSelColor;
        }else{
            //color = new Color(238, 238, 238);
            color = bgColor;
        }
        setBackground(color);
        labelTitle.setBackground(color);
        labelIcon.setBackground(color);
        repaint();
    }

    /* update the name of the tab */
    public void updateTitle(String name){
        labelTitle.setText(name);
        this.title = name;
        int l = MyUtilities.lenghtOfString(title, getFontMetrics(labelTitle.getFont()));
        this.labelTitle.setSize(l+5, this.labelTitle.getHeight());
        labelIcon.setBounds(labelTitle.getX()+labelTitle.getWidth()+5, 2, 20, labelTitle.getHeight());
        Dimension d = new Dimension(labelTitle.getWidth()+25, 19);
        setSize(d);
        setPreferredSize(d);
        revalidate();
        repaint();
    }

    // set disabled
    public void setDisabled(){
        this.labelIcon.setEnabled(false);
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
      if (this.title != null)
            this.action.setSelectedTab(this);
        if (e.getClickCount() == 2){
            // double-click :
            if (this.title == null || !this.isSelected)
                return;
            this.action.doubleClickTab(this);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }


    @Override
    public void actionCopexButtonClic(CopexButtonPanel button) {
        if(ds == null){
            action.openDialogAddDataset();
        }else{
            action.openDialogCloseDataset(ds);
        }
    }
}
