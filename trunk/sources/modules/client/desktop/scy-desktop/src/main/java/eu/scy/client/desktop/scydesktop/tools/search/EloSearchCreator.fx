/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.tools.search;
import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.elofactory.ScyToolCreatorFX;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import eu.scy.client.desktop.scydesktop.ScyDesktop;
import eu.scy.client.desktop.scydesktop.tools.search.queryselecters.QuerySelecterFactoryImpl;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import eu.scy.client.desktop.scydesktop.tools.search.queryselecters.LastModifiedQuerySelecterCreator;
import eu.scy.client.desktop.scydesktop.tools.search.queryselecters.TechnicalFormatQuerySelecterCreator;

/**
 * @author SikkenJ
 */

public class EloSearchCreator extends ScyToolCreatorFX {
   public var scyDesktop: ScyDesktop;
   public var toolBrokerAPI:ToolBrokerAPI on replace { setupQuerySelectorFactory() }
   def querySelecterFactory: QuerySelecterFactory = new QuerySelecterFactoryImpl();

   function setupQuerySelectorFactory(){
      if (toolBrokerAPI==null){
         return;
      }
      querySelecterFactory.registerQuerySelecterCreator(new TechnicalFormatQuerySelecterCreator(toolBrokerAPI));
      querySelecterFactory.registerQuerySelecterCreator(new LastModifiedQuerySelecterCreator(toolBrokerAPI));
      querySelecterFactory.registerQuerySelecterCreator(new LastModifiedQuerySelecterCreator(toolBrokerAPI));
   }


   override public function createScyToolNode(eloType: String, creatorId: String, scyWindow: ScyWindow, windowContent: Boolean): Node {
      EloSearchNode {
         scyDesktop: scyDesktop
         window: scyWindow
         querySelecterFactory: querySelecterFactory
      }
   }

}
