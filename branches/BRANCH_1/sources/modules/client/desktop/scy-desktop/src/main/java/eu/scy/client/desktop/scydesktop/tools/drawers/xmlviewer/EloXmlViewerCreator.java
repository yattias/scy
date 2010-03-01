/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.tools.drawers.xmlviewer;

import eu.scy.client.desktop.scydesktop.elofactory.ScyToolCreator;
import java.awt.Dimension;
import javax.swing.JComponent;

/**
 *
 * @author sikkenj
 */
public class EloXmlViewerCreator implements ScyToolCreator {

   @Override
   public JComponent createScyToolComponent(String eloType, String creatorId, boolean windowContent)
   {
      EloXmlViewer eloXmlViewer = new EloXmlViewer();
      eloXmlViewer.setPreferredSize(new Dimension(200,100));
      return eloXmlViewer;
   }

   @Override
   public boolean supportType(String type)
   {
      return true;
   }

}
