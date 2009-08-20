/*
 * MaterialLabel.java
 *
 * Created on 11 août 2008, 07:58
 */

package eu.scy.tools.copex.utilities;


import eu.scy.tools.copex.common.Material;
import eu.scy.tools.copex.edp.EdPPanel;
import eu.scy.tools.copex.edp.JustificationMaterialUseDialog;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;


/**
 * represente un label material avec son icone
 * ajoute une case a cocher pour savoir si utilise dans le proc
 * * MBO le 15/12/08 : label devient text area avec une longueur minimum
 * @author  MBO
 */
public class MaterialLabel extends JPanel implements MouseListener{

    // CONSTANTES
    private final static int MIN_WIDTH = 100;
    // ATTRIBUTS
    /* edP*/
    private EdPPanel edP;
    /* panel mere */
    private ListPanel owner;
    /* materiel */
    private Material material;
    /* justification */
    private String justification;
    /* icone */
    private JLabel labelIcon;
    /* texte */
    private JTextArea labelMaterial;
    /* position */
    private int pos;
    /* materiel utilise pour proc*/
    private JCheckBox cbMatUse;
    /* materiel utilise ? */
    private boolean isUsed;
    /* droit */
    private char procRight;
    /* largeur du composant mere */
    private int ownerWidth ;
    
    // CONSTRUCTEURS
    /** Creates new form MaterialLabel */
    public MaterialLabel() {
        initComponents();
    }

    public MaterialLabel(EdPPanel edP, ListPanel owner, Material material, String justification, boolean isUsed, int pos, char procRight, int ownerWidth){
        super();
        this.edP = edP;
        this.owner = owner;
        this.material = material;
        this.pos = pos;
        this.justification = justification;
        this.procRight = procRight;
        this.isUsed = isUsed;
        this.ownerWidth = ownerWidth ;
        initComponents();
        init();
    }
    
    // METHODES
    private void init(){
        this.addMouseListener(this);
        setBackground(Color.WHITE);
        // icone
        labelIcon = new JLabel();
        labelIcon.setIcon(owner.getIconMaterial());
        if (material.isAdvisedLearner())
            labelIcon.setIcon(owner.getIconAdvise());
        // cb permettant de savoir si materiel utilise dans proc 
        cbMatUse = new JCheckBox();
        cbMatUse.setBackground(Color.WHITE);
        if (isUsed)
            cbMatUse.setSelected(true);
        cbMatUse.setText("");
        if (procRight == MyConstants.NONE_RIGHT)
            cbMatUse.setEnabled(false);
         cbMatUse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setMaterialUseForProc(evt);
            }
        });
        // label
        String s = material.toDisplay();
        labelMaterial = new JTextArea(s);
        labelMaterial.setLineWrap(true);
        labelMaterial.setWrapStyleWord(true);
        labelMaterial.setEditable(false);
        labelMaterial.setFont(new Font("Tahoma", Font.PLAIN, 11));
        labelMaterial.setMinimumSize(new Dimension(MIN_WIDTH, 25));
        labelMaterial.setMaximumSize(new Dimension(ownerWidth,1024));
        int w = Math.max(MIN_WIDTH, ownerWidth - 37);
        int h = (int)(((float)((CopexUtilities.lenghtOfString(s, getFontMetrics(labelMaterial.getFont()))) / w)) + 1)*22;
        labelMaterial.setSize(w, h);
        //positionnement 
        labelIcon.setBounds(22, 7, labelIcon.getIcon().getIconWidth(), labelIcon.getIcon().getIconHeight());
        cbMatUse.setBounds(2, 0, 20, 20);
        labelMaterial.setBounds(37, 2, labelMaterial.getWidth(), labelMaterial.getHeight());
        add(cbMatUse);
        add(labelIcon);
        add(labelMaterial);
        setBounds(0, pos, labelMaterial.getWidth()+labelMaterial.getX()+2, h);
        setPreferredSize(new Dimension(labelMaterial.getWidth()+labelMaterial.getX()+2, labelMaterial.getHeight()));
        this.setToolTipText(new DetailMaterialHTMLToolTipText(material).getHtmlMaterial());
    }

    public Material getMaterial() {
        return material;
    }
    
    public void setMaterialUseForProc(ActionEvent evt){
        boolean matUse = cbMatUse.isSelected();
        if(matUse){
            JustificationMaterialUseDialog justificationD = new JustificationMaterialUseDialog(edP, material, justification, true);
            justificationD.setVisible(true);
        }else{
            edP.removeMaterialUse(material, MyConstants.NOT_UNDOREDO);
        }
    }
    /* suppression du materiel utilise  */
    public void removeMaterialUse(){
        this.isUsed = false;
        this.justification = "";
        this.cbMatUse.setSelected(false);
        repaint();
    }
    
    /* modification du materiel utilise  */
    public void updateMaterialUse(String justification){
        this.justification = justification;
        repaint();
    }
    
    /* ajout du materiel utilise  */
    public void addMaterialUse(String justification){
        this.isUsed = false;
        this.justification = justification;
        this.cbMatUse.setSelected(true);
        repaint();
    }

    /* resize, renvoit la nouvelle hauteur */
    public int resizeWidth(int ownerWidth){
        this.ownerWidth = ownerWidth ;
        int w = Math.max(MIN_WIDTH, ownerWidth - 37);
        int h = (int)(((float)((CopexUtilities.lenghtOfString(labelMaterial.getText(), getFontMetrics(labelMaterial.getFont()))) / w) + 1)*22);
       labelMaterial.setSize(w, h);
        setSize(labelMaterial.getWidth()+labelMaterial.getX()+2, h);
        setPreferredSize(new Dimension(labelMaterial.getWidth()+labelMaterial.getX()+2, h));
        return h ;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(null);
    }// </editor-fold>//GEN-END:initComponents

    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2 && cbMatUse.isSelected()){
            JustificationMaterialUseDialog justificationD = new JustificationMaterialUseDialog(edP, material, justification, false);
            justificationD.setVisible(true);
        }
    }

    public void mousePressed(MouseEvent e) {
        
    }

    public void mouseReleased(MouseEvent e) {
        
    }

    public void mouseEntered(MouseEvent e) {
        // affichage detail 
        //owner.showMaterial(material);
    }

    public void mouseExited(MouseEvent e) {
        // fermer l'affichage 
        //owner.hideMaterial();
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

}
