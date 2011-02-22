/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.scywindows.moreinfomanager;

import eu.scy.client.desktop.scydesktop.scywindows.MoreInfoToolFactory;
import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.scywindows.ShowMoreInfo;

/**
 * @author SikkenJ
 */
public class TestMoreInfoToolFactory extends MoreInfoToolFactory {

   public override function createMoreInfoTool(showMoreInfo: ShowMoreInfo): Node {
      return TestMoreInfoTool {
         }
   }

}
