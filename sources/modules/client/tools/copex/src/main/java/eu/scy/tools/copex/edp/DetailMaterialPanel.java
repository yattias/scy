/*
 * DetailMaterialPanel.java
 *
 * Created on 4 aoa�t 2008, 13:17
 */

package eu.scy.tools.copex.edp;


import eu.scy.tools.copex.common.Material;
import eu.scy.tools.copex.common.Parameter;
import eu.scy.tools.copex.utilities.CopexUtilities;
import eu.scy.tools.copex.utilities.RoundedBorder;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

/**
 * panel sui permet de decrire le detail d'un materiel : 
 * nom et types du materiel, description, ses parametres et leurs valeurs.
 * eventuellement un dessin de ce materiel
 * @author  MBO
 */
public class DetailMaterialPanel extends JPanel {
    // CONSTANTES 
    public static final int DETAIL_MATERIAL_WIDTH = 200;
    public static final int DETAIL_MATERIAL_HEIGHT = 110;
    
    // ATTRIBUTS 
    /* materiel a� afficher */
    private Material material;
    /* owner */
    private JPanel ownerPanel;
    private EdPPanel edP;
   
            
    public static Color borderColor = new Color(0, 153, 204) ;
        
        
    // CONSTRUCTEURS 
    /** Creates new form DetailMaterialPanel */
    public DetailMaterialPanel() {
        initComponents();
    }

    public DetailMaterialPanel(Material material, JPanel ownerPanel, EdPPanel edP) {
        this.material = material;
        this.ownerPanel = ownerPanel;
        this.edP = edP;
        initComponents();
        init();
    }

    // METHODES 
    private void init(){
        setSize(new Dimension(Math.max(DETAIL_MATERIAL_WIDTH, ownerPanel.getWidth()-10), DETAIL_MATERIAL_HEIGHT));
        setPreferredSize(getSize());
        setOpaque(true);
        // border
        RoundedBorder rBorder = new RoundedBorder(borderColor, 30, 30);
        setBorder(rBorder);
        // nom du materiel
        if (material == null)
            return;
        this.labelMaterial.setText(material.getName());
        this.labelMaterial.setSize(CopexUtilities.lenghtOfString(this.labelMaterial.getText(), getFontMetrics(this.labelMaterial.getFont())), this.labelMaterial.getHeight());
        // types
        JLabel labelTypeMat = new JLabel(material.getTypeToDisplay());
        labelTypeMat.setFont(new Font("Tahoma", Font.BOLD, 11));
        labelTypeMat.setBounds(10, 30, 150, 14);
        labelTypeMat.setSize(CopexUtilities.lenghtOfString(labelTypeMat.getText(), getFontMetrics(labelTypeMat.getFont())), labelTypeMat.getHeight());
        
        add(labelTypeMat);
        
        // description et parametres
        JTextArea labelDescription = new JTextArea(material.getDescription());
        labelDescription.setLineWrap(true);
        labelDescription.setWrapStyleWord(true);
        labelDescription.setFont(new Font("Tahoma", Font.BOLD, 9));
        //int wi = Math.max(150,labelTypeMat.getWidth());
        int wi = getWidth() - 20 ;
        int lg = CopexUtilities.lenghtOfString(material.getDescription(), getFontMetrics(labelDescription.getFont()));
        
        int hm = (lg / wi + 2)*15 ;
        labelDescription.setBounds(10, 60, wi, Math.max(hm,70));
        
        add(labelDescription);
        ArrayList<Parameter> listP = material.getListParameters();
        int nbP = listP.size();
        int start = labelDescription.getY()+labelDescription.getHeight()+10;
        for (int i=0; i<nbP; i++){
            Parameter p = listP.get(i);
            JLabel labelP = new JLabel("- "+p.getName()+ " / "+p.getValue());
            labelP.setFont(new Font("Tahoma", Font.BOLD, 11));
            labelP.setBounds(10,i*15+start , 150, 14);
            labelP.setSize(CopexUtilities.lenghtOfString(labelP.getText(), getFontMetrics(labelP.getFont())), labelP.getHeight());
            add(labelP);
        }
        /*int w = Math.max(160, labelTypeMat.getWidth()+labelTypeMat.getX()+10);
        w = Math.max(w,labelDescription.getWidth()+labelDescription.getX()+10);*/
        int h = labelDescription.getHeight() + labelDescription.getY()+nbP*15+20;
        h=  Math.max(DETAIL_MATERIAL_HEIGHT, h);
        setSize(getWidth(), h);
        revalidate();
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

        labelMaterial = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setMinimumSize(new java.awt.Dimension(140, 80));
        setLayout(null);

        labelMaterial.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labelMaterial.setText("jLabel1");
        labelMaterial.setName("labelMaterial"); // NOI18N
        add(labelMaterial);
        labelMaterial.setBounds(30, 10, 160, 14);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel labelMaterial;
    // End of variables declaration//GEN-END:variables

}
