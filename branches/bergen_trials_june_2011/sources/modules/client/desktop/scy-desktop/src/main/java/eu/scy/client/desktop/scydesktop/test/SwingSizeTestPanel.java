/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;
import org.apache.log4j.Logger;

/**
 *
 * @author sikkenj
 */
public class SwingSizeTestPanel extends JPanel {

   private final static Logger logger= Logger.getLogger(SwingSizeTestPanel.class);

   @Override
   protected void printComponent(Graphics g)
   {
      super.printComponent(g);
      setBackground(Color.LIGHT_GRAY);
      setForeground(Color.RED);
      g.setColor(getForeground());
      drawSize(g,"actual",getSize(),10,20);
      drawSize(g,"minimum",getMinimumSize(),10,20);
      drawSize(g,"preffered",getPreferredSize(),10,20);
      drawSize(g,"maximum",getMaximumSize(),10,20);
   }

   private void drawSize(Graphics g,String label, Dimension size,int x , int y)
   {
      g.drawString(label + ": " + size.width + "*" + size.height, x, y);
   }

   @Override
   public void resize(int width, int height)
   {
      setMinimumSize(new Dimension(width,height));
      super.resize(width, height);
      logger.info("resize(" + width + "," + height + ")");
   }



}
