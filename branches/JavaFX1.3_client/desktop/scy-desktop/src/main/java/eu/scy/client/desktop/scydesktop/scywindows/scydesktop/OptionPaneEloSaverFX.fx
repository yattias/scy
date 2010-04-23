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
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadata;

import javax.swing.JOptionPane;

import eu.scy.client.desktop.scydesktop.tools.EloSaver;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;

import eu.scy.client.desktop.scydesktop.scywindows.NewTitleGenerator;
import eu.scy.client.desktop.scydesktop.utils.StringUtils;
import eu.scy.client.desktop.scydesktop.tools.EloSaverCallBack;


/**
 * @author sikken
 */

public class OptionPaneEloSaverFX extends EloSaver {

   public var newTitleGenerator: NewTitleGenerator;
   public var myEloChanged: MyEloChanged;
   public var repository: IRepository;
   public var eloFactory: IELOFactory;
   public var titleKey: IMetadataKey;
   public var technicalFormatKey: IMetadataKey;
   public var window:ScyWindow;

   public override function eloSaveAs(elo: IELO, eloSaverCallBack: EloSaverCallBack):Void{
      var forking = elo.getUri()!=null;
      var currentEloTitle = elo.getMetadata().getMetadataValueContainer(titleKey).getValue() as String;
      var suggestedEloTitle = currentEloTitle;
      if (suggestedEloTitle==null){
         suggestedEloTitle = window.title;
      }
      if (forking){
         suggestedEloTitle = "Fork of {suggestedEloTitle}";
      }
      var newEloTitle = JOptionPane.showInputDialog("Enter title:", suggestedEloTitle);
      if (StringUtils.hasText(newEloTitle)){
         elo.getMetadata().getMetadataValueContainer(titleKey).setValue(newEloTitle);
         if (elo.getUri() != null)
         {
            eloFactory.detachELO(elo);
         }
         var newMetadata = repository.addNewELO(elo);
         eloFactory.updateELOWithResult(elo, newMetadata);
         myEloChanged.myEloChanged(elo);
         eloSaverCallBack.eloSaved(elo);
         return;
      }
      eloSaverCallBack.eloSaveCancelled(elo);
   }

   public override function eloUpdate(elo: IELO, eloSaverCallBack: EloSaverCallBack):Void{
      if (elo.getUri() != null) {
         var oldUri = elo.getUri();
         var newMetadata = repository.updateELO(elo);
         eloFactory.updateELOWithResult(elo, newMetadata);
         myEloChanged.myEloChanged(elo);
         eloSaverCallBack.eloSaved(elo);
      }
      else {
         eloSaveAs(elo,eloSaverCallBack);
      }
   }
   public override function otherEloSaveAs(elo: IELO, eloSaverCallBack: EloSaverCallBack):Void{
      var forking = elo.getUri()!=null;
      var currentEloTitle = elo.getMetadata().getMetadataValueContainer(titleKey).getValue() as String;
      var suggestedEloTitle = currentEloTitle;
      if (suggestedEloTitle==null){
         var eloType = elo.getMetadata().getMetadataValueContainer(technicalFormatKey).getValue() as String;
         suggestedEloTitle = newTitleGenerator.generateNewTitleFromType(eloType);
      }
      if (forking){
         suggestedEloTitle = "Fork of {suggestedEloTitle}";
      }
      var newEloTitle = JOptionPane.showInputDialog("Enter title:", suggestedEloTitle);
      if (StringUtils.hasText(newEloTitle)){
         elo.getMetadata().getMetadataValueContainer(titleKey).setValue(newEloTitle);
         var newMetadata:IMetadata;
         if (forking){
            newMetadata = repository.addForkedELO(elo);
         }
         else{
            newMetadata = repository.addNewELO(elo);
         }
         eloFactory.updateELOWithResult(elo, newMetadata);
         myEloChanged.myEloChanged(elo);
         eloSaverCallBack.eloSaved(elo);
      }
      eloSaverCallBack.eloSaveCancelled(elo);
   }


}
