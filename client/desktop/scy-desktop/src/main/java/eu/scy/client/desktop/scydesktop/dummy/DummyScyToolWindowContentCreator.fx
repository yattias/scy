/*
 * DummyScyToolWindowContentCreator.fx
 *
 * Created on 30-nov-2009, 12:14:41
 */

package eu.scy.client.desktop.scydesktop.dummy;

import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.elofactory.ScyToolWindowContentCreatorFX;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import java.net.URI;
import roolo.api.IRepository;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataTypeManager;

/**
 * @author sikken
 */

// place your code here

public class DummyScyToolWindowContentCreator extends ScyToolWindowContentCreatorFX {
//   public var eloFactory:IELOFactory;
//   public var metadataTypeManager: IMetadataTypeManager;
//   public var repository:IRepository;

   public override function createScyToolWindowContent():Node{
      return DummyScyToolWindowContent{
      }
   }
}
