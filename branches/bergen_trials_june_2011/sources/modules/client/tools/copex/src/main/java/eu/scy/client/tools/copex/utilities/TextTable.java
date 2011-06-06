package eu.scy.client.tools.copex.utilities;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.util.List;
import javax.swing.JTable;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Marjolaine
 */
public class TextTable extends JTable{
    private List<String> textList;
    private TextTableModel textModel;
    private TextTableRenderer textRenderer;
    private int nbCol;

    public TextTable(List<String> textList, int nbCol){
        super();
        this.textList = textList;
        this.nbCol = nbCol;
        this.textModel = new TextTableModel(textList, nbCol);
        this.textRenderer = new TextTableRenderer();
        this.setTableHeader(null);
        this.setModel(this.textModel);
        this.setDefaultRenderer(Object.class, textRenderer);
        this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        this.setShowGrid(false);
        this.setIntercellSpacing(new Dimension(0,0));
        this.setBackground(Color.WHITE);
    }

    public void setTextList(List<String> textList){
        this.textList = textList;
        textModel.setTextList(textList);
        resize();
    }

    public void setNbCol(int nbCol){
        this.nbCol = nbCol;
        textModel.setNbCol(nbCol);
        resize();
    }

    public void resize(){
        int widthTot = 0;
        for (int j = 0 ; j < this.textModel.getColumnCount() ; j++){
            int max = 0;
            for (int i = 0 ; i < this.textModel.getRowCount() ; i++){
                FontMetrics fm = getFontMetrics(TextTableRenderer.dataFont);
                int taille = 0;
                if (this.getValueAt(i, j) != null){
                    String s = "";
                    if (this.getValueAt(i, j) instanceof String)
                        s = (String)this.getValueAt(i, j) ;
                    taille = fm.stringWidth(s)+10;
                }
                max = Math.max(taille, max);
            }
            getColumnModel().getColumn(j).setPreferredWidth(max);
            getColumnModel().getColumn(j).setWidth(max);
            getColumnModel().getColumn(j).setMinWidth(max);
            widthTot += (max);
        }
         int height = (this.textModel.getRowCount())*getRowHeight();
         setSize(widthTot, height);
         setPreferredSize(getSize());
         repaint();
    }

}
