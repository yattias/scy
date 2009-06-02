/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.edp;

import javax.swing.JLayeredPane;

/**
 * thread qui permet l'affichage du detail d'un materiel 
 * @author MBO
 */
public class DisplayMaterialThread extends Thread{

    // CONSTANTES
    /* delai d'affichage du materiel */
    public static final int DELAY_DISPLAY = 3;
    
    // ATTRIBUTS
    /* */
    private JLayeredPane layeredPane;
    private DetailMaterialPanel detailMaterialPanel;

    
    // CONSTRUCTEURS
    public DisplayMaterialThread(JLayeredPane layeredPane, DetailMaterialPanel detailMaterialPanel) {
        super();
        this.layeredPane = layeredPane;
        this.detailMaterialPanel = detailMaterialPanel;
    }
    
    
    // DEROULEMENT DU THREAD 
    @Override
    public void run(){
        try{
            layeredPane.moveToFront(detailMaterialPanel);
            layeredPane.revalidate();
            layeredPane.repaint();
            sleep(DELAY_DISPLAY * 1000);
            layeredPane.remove(detailMaterialPanel);
            this.detailMaterialPanel = null;
            layeredPane.revalidate();
            layeredPane.repaint();
            this.interrupt();
        }catch(InterruptedException e){
        }
    }
}
