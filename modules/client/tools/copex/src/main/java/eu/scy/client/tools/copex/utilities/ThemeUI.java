/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.utilities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

/**
 * surcharge du look du tabbedPane
 * @author MBO
 */
public class ThemeUI extends BasicTabbedPaneUI {

    private Color selectedColor = Color.WHITE;
           
    public ThemeUI() {
        super();
    }

    @Override
    protected void paintTabBackground(Graphics g, int tabPlacement,
                                      int tabIndex,
                                      int x, int y, int w, int h, 
                                      boolean isSelected ) {
        g.setColor(Color.WHITE);
        switch(tabPlacement) {
          case LEFT:
              g.fillRect(x+1, y+1, w-1, h-3);
              break;
          case RIGHT:
              g.fillRect(x, y+1, w-2, h-3);
              break;
          case BOTTOM:
              g.fillRect(x+1, y, w-3, h-1);
              break;
          case TOP:
          default:
              g.fillRect(x+1, y+1, w-3, h-1);
        }
    }
   
}
