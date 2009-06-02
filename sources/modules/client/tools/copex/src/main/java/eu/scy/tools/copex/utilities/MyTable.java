/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.utilities;

import eu.scy.tools.copex.edp.EdPPanel;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.InputMap;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.UIManager;

/**
 * represente table pour les données DataSheet
 * @author MBO
 */
public class MyTable extends JTable {
    // CONSTANTES
    /* largeur min d'une colonne */
    private static final int MIN_WIDTH_COL = 60;
    /* model */
    private MyTableModel tableModel;
    /* editeur cellule */
    private MyTableEditor anEditor;
    
    //Va à la cellule suivante jusqu'au bout de la ligne puis à la ligne suivante etc...
    	final Action CelluleSuivante = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				//Pour ne pas qu'il aille au delà de la taille du tableau
				if (getSelectedRow() < getRowCount()){
					//Arrivé en fin de ligne il va à la première colonne de la ligne suivante
					if(getSelectedColumn() == getColumnCount()-1)					
						changeSelection(getSelectedRow()+1,0,false,false);					
					else//il va vers la colonne suivante
						changeSelection(getSelectedRow(),getSelectedColumn()+1,false,false);
				}
			}
		};
    	
                
    
    public MyTable(MyTableModel tableModel) {
        super(tableModel);
        this.tableModel = tableModel;
        setTableHeader(null);
        // EN FAIT ON AURAIT PU UTTILISER LE TABLE HEADER MAIS CECI SE FAIT A CONTRE COUP
        MyTableRenderer myRenderer = new MyTableRenderer();
        myRenderer.setHorizontalAlignment(JLabel.CENTER);
        this.setDefaultRenderer(Object.class, myRenderer);
        this.anEditor = new MyTableEditor();
        setCellEditor(anEditor);
        setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        resizeColumn();
        alterActionEnter();
            
    }

    /* gestion de la touche entree sur une cellule */
    private void alterActionEnter(){
        //Enleve le comportement de la touche entrée
        InputMap inputMap = (javax.swing.InputMap)UIManager.get("Table.ancestorInputMap");
        inputMap.remove(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0));
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0),"ENTREE");
    	getActionMap().put("ENTREE", CelluleSuivante);
 
    	//L'ecouteur pour editer la cellule
        addKeyListener(new KeyAdapter(){
            @Override
             public void keyPressed(KeyEvent e){
                  if (e.getKeyCode() == KeyEvent.VK_ENTER){
                       editCellAt(getSelectedRow(),getSelectedColumn()+1);                                              
                  }
             }
        });    
    }
    
    
                
    @Override
    public boolean isCellEditable(int row, int column) {
        if (!EdPPanel.CAN_EDIT_DATASHEET)
            return false ;
        return (row == 0) ;
    }

    

    /* resize column */
    public void resizeColumn(){
        int widthTot = 0;
        for (int j = 0 ; j < tableModel.getColumnCount() ; j++){
            int max = 0;
            for (int i = 0 ; i < tableModel.getRowCount() ; i++){
                FontMetrics fm = getFontMetrics(getFont());
               int taille = 0;
               if (this.getValueAt(i, j) != null){
                   String s = "";
                   if (this.getValueAt(i, j) instanceof String)
                       s = (String)this.getValueAt(i, j) ;
                   else if (this.getValueAt(i, j) instanceof Double){
                       s = Double.toString((Double)this.getValueAt(i, j)) ;
                   }else if (this.getValueAt(i, j) instanceof Integer){
                       s = Integer.toString((Integer)this.getValueAt(i, j)) ;
                   }
                   taille = fm.stringWidth(s);
               }
               max = Math.max(taille, max);
            }

           max = Math.max(MIN_WIDTH_COL, max);
           getColumnModel().getColumn(j).setPreferredWidth(max+10);
           widthTot += (max+10);
        }
         int height = this.getRowCount() * getRowHeight() ;
         setSize(widthTot, height);
         setPreferredSize(getSize());
         repaint();

    }

    /* retourne la largeur d'une colonne */
    public int getColumnWidth(int noCol){
        return getColumnModel().getColumn(noCol).getWidth();
    }
    
    

}
