/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.tools.fxflyingsaucer;

import eu.scy.client.desktop.scydesktop.scywindows.MoreInfoToolFactory;
import javafx.scene.Node;

/**
 * @author SikkenJ
 */
public class FlyingSaucerMoreInfoToolFactory extends MoreInfoToolFactory {

   public override function createMoreInfoTool(): Node {
      FlyingSaucerMoreInfoTool{}
   }

}
