package eu.scy.tools.fitex.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * distance panel
 * @author Marjolaine
 */
public class DistancePanel extends JPanel{
    private FitexPanel fitex;
    private int id;
    private JLabel labelDist;
    private JPanel panelDistBlue;
    private JPanel panelDistGreen;
    private JPanel panelDistBlack;
    private JLabel labelKBlue;
    private JLabel labelKGreen;
    private JLabel labelKBlack;

    public DistancePanel(FitexPanel fitex, int id) {
        this.fitex = fitex;
        this.id = id;
        initGUI();
    }


    private void initGUI(){
        setName("panelDist");
        setMaximumSize(new Dimension(32767, 25));
        setMinimumSize(new Dimension(50, 25));
        setLayout(new FlowLayout(FlowLayout.LEFT));
        add(getLabelDist());
        add(getPanelDistBlue());
        add(getPanelDistGreen());
        add(getPanelDistBlack());
    }

    
    private JLabel getLabelDist(){
        if(labelDist == null){
            labelDist = new JLabel();
            labelDist.setName("labelDist");
            labelDist.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
            labelDist.setText(fitex.getBundleString("LABEL_DISTANCE_"+id));
        }
        return labelDist;
    }

    private JPanel getPanelDistBlue(){
        if(panelDistBlue == null){
            panelDistBlue = new JPanel();
            panelDistBlue.setName("panelDistBlue");
            panelDistBlue.setBorder(javax.swing.BorderFactory.createEtchedBorder());
            panelDistBlue.setMinimumSize(new java.awt.Dimension(60, 20));
            panelDistBlue.setPreferredSize(new java.awt.Dimension(60, 20));
            javax.swing.GroupLayout panelDistBlueLayout = new javax.swing.GroupLayout(panelDistBlue);
            panelDistBlue.setLayout(panelDistBlueLayout);
            getLabelKBlue();
            panelDistBlueLayout.setHorizontalGroup(
                panelDistBlueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelDistBlueLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(labelKBlue)
                    .addContainerGap(34, Short.MAX_VALUE))
            );
            panelDistBlueLayout.setVerticalGroup(
                panelDistBlueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelDistBlueLayout.createSequentialGroup()
                    .addComponent(labelKBlue)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
        }
        return panelDistBlue;
    }

    private JLabel getLabelKBlue(){
        if (labelKBlue == null){
            labelKBlue = new JLabel();
            labelKBlue.setName("labelKBlue");
            labelKBlue.setForeground(java.awt.Color.BLUE);
            labelKBlue.setText("...");
        }
        return labelKBlue ;
    }

    private JPanel getPanelDistGreen(){
        if(panelDistGreen == null){
            panelDistGreen = new JPanel();
            panelDistGreen.setName("panelDistGreen");
            panelDistGreen.setBorder(javax.swing.BorderFactory.createEtchedBorder());
            panelDistGreen.setMinimumSize(new java.awt.Dimension(60, 20));
            panelDistGreen.setPreferredSize(new java.awt.Dimension(60, 20));
            javax.swing.GroupLayout panelDistGreenLayout = new javax.swing.GroupLayout(panelDistGreen);
            panelDistGreen.setLayout(panelDistGreenLayout);
            getLabelKGreen();
            panelDistGreenLayout.setHorizontalGroup(
                panelDistGreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelDistGreenLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(labelKGreen)
                    .addContainerGap(34, Short.MAX_VALUE))
            );
            panelDistGreenLayout.setVerticalGroup(
                panelDistGreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelDistGreenLayout.createSequentialGroup()
                    .addComponent(labelKGreen)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
        }
        return panelDistGreen;
    }

    private JLabel getLabelKGreen(){
        if (labelKGreen == null){
            labelKGreen = new JLabel();
            labelKGreen.setName("labelKGreen");
            labelKGreen.setForeground(new java.awt.Color(51, 153, 0));
            labelKGreen.setText("...");
        }
        return labelKGreen ;
    }

    private JPanel getPanelDistBlack(){
        if(panelDistBlack == null){
            panelDistBlack = new JPanel();
            panelDistBlack.setName("panelDistBlack");
            panelDistBlack.setBorder(javax.swing.BorderFactory.createEtchedBorder());
            panelDistBlack.setMinimumSize(new java.awt.Dimension(60, 20));
            panelDistBlack.setPreferredSize(new java.awt.Dimension(60, 20));
            javax.swing.GroupLayout panelDistBlackLayout = new javax.swing.GroupLayout(panelDistBlack);
            panelDistBlack.setLayout(panelDistBlackLayout);
            getLabelKBlack();
            panelDistBlackLayout.setHorizontalGroup(
                panelDistBlackLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelDistBlackLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(labelKBlack)
                    .addContainerGap(34, Short.MAX_VALUE))
            );
            panelDistBlackLayout.setVerticalGroup(
                panelDistBlackLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelDistBlackLayout.createSequentialGroup()
                    .addComponent(labelKBlack)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
        }
        return panelDistBlack;
    }

    private JLabel getLabelKBlack(){
        if (labelKBlack == null){
            labelKBlack = new JLabel();
            labelKBlack.setName("labelKBlack");
            labelKBlack.setText("...");
        }
        return labelKBlack ;
    }

    public void setText(Color coul, String txt){
        JLabel jLab = null ;
        if (coul == Color.BLUE) jLab = labelKBlue ;
        else if (coul == FitexPanel.DARK_GREEN ) jLab = labelKGreen ;
        else jLab = labelKBlack ;
        if(jLab != null){
            jLab.setText(txt);
        }
    }

}
