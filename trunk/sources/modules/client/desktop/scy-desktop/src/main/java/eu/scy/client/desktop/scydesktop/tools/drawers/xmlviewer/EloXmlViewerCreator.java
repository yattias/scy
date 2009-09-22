/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.tools.drawers.xmlviewer;

import eu.scy.client.desktop.scydesktop.elofactory.DrawerContentCreator;
import java.net.URI;
import javax.swing.JComponent;

/**
 *
 * @author sikkenj
 */
public class EloXmlViewerCreator implements DrawerContentCreator {

   @Override
   public JComponent getDrawerContent(URI eloUri)
   {
      EloXmlViewer eloXmlViewer = new EloXmlViewer();
      eloXmlViewer.setEloUri(eloUri);
      return eloXmlViewer;
   }

}
