/*
 * OptionPaneEloSaverFX.fx
 *
 * Created on 1-dec-2009, 17:53:53
 */

package eu.scy.client.desktop.scydesktop.scywindows.scydesktop;

import eu.scy.client.desktop.scydesktop.tools.MyEloChanged;
import roolo.api.IRepository;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataKey;

import javax.swing.JOptionPane;
import org.springframework.util.StringUtils;
import roolo.elo.api.IELO;

import eu.scy.client.desktop.scydesktop.tools.EloSaver;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;

/**
 * @author sikken
 */

public class OptionPaneEloSaverFX extends EloSaver {

   public var myEloChanged: MyEloChanged;
   public var repository: IRepository;
   public var eloFactory: IELOFactory;
   public var titleKey: IMetadataKey;
   public var window:ScyWindow;

   public override function saveElo(elo: IELO, myElo:Boolean):IELO{
      var currentEloTitle = elo.getMetadata().getMetadataValueContainer(titleKey).getValue() as String;
      if (currentEloTitle==null){
         currentEloTitle = window.title;
      }
      var newEloTitle = JOptionPane.showInputDialog("Enter title:", currentEloTitle);
      if (StringUtils.hasText(newEloTitle)){
         elo.getMetadata().getMetadataValueContainer(titleKey).setValue(newEloTitle);
         if (elo.getUri() != null)
         {
            eloFactory.detachELO(elo);
         }
         var newMetadata = repository.addNewELO(elo);
         eloFactory.updateELOWithResult(elo, newMetadata);
         if (myElo)
         {
            myEloChanged.myEloChanged(elo);
         }
         return elo;
      }
      return null;
   }

   public override function updateElo(elo: IELO, myElo:Boolean):IELO{
      if (elo.getUri() != null) {
         var oldUri = elo.getUri();
         var newMetadata = repository.updateELO(elo);
         eloFactory.updateELOWithResult(elo, newMetadata);
         myEloChanged.myEloChanged(elo);
         return elo;
      }
      else {
         return saveElo(elo, myElo);
      }
   }

}
