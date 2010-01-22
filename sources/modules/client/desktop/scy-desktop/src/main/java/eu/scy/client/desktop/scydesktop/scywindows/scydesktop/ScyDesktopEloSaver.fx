/*
 * ScyDesktopEloSaver.fx
 *
 * Created on 18-jan-2010, 10:47:41
 */

package eu.scy.client.desktop.scydesktop.scywindows.scydesktop;

import roolo.api.IRepository;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadata;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;

import eu.scy.client.desktop.scydesktop.tools.EloSaver;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;

import eu.scy.client.desktop.scydesktop.scywindows.NewTitleGenerator;
import eu.scy.client.desktop.scydesktop.tools.MyEloChanged;
import javafx.scene.Group;
import eu.scy.client.desktop.scydesktop.config.DisplayNames;
import java.util.List;
import eu.scy.client.desktop.scydesktop.config.Config;

import javafx.ext.swing.SwingComponent;

import eu.scy.client.desktop.scydesktop.ScyRooloMetadataKeyIds;
import eu.scy.client.desktop.scydesktop.tools.EloSaverCallBack;
import eu.scy.client.desktop.scydesktop.scywindows.WindowStyler;
import javafx.scene.paint.Color;
import eu.scy.client.desktop.scydesktop.scywindows.EloIcon;

/**
 * @author sikken
 */

class EloSaveAsActionHandler extends EloSaveAsActionListener{
   var elo:IELO;
   var eloSaveAsPanel:EloSaveAsPanel;
   var myEloChanged: MyEloChanged;
   var modalDialogBox:ModalDialogBox;
   var eloSaverCallBack: EloSaverCallBack;

   public override function eloSaved():Void{
      elo.getMetadata().getMetadataValueContainer(titleKey).setValue(eloSaveAsPanel.getTitle());
      elo.getMetadata().getMetadataValueContainer(descriptionKey).setValue(eloSaveAsPanel.getDescription());
      if (logicalTypeKey!=null){
         elo.getMetadata().getMetadataValueContainer(logicalTypeKey).setValue(eloSaveAsPanel.getLogicalType());
      }
      if (functionalTypeKey!=null){
         elo.getMetadata().getMetadataValueContainer(functionalTypeKey).setValue(eloSaveAsPanel.getFunctionalType());
      }
      if (elo.getUri() != null)
      {
         eloFactory.detachELO(elo);
      }
      var newMetadata = repository.addNewELO(elo);
      eloFactory.updateELOWithResult(elo, newMetadata);
      myEloChanged.myEloChanged(elo);
      eloSaverCallBack.eloSaved(elo);
      modalDialogBox.close();

   }

   public override function eloSaveCancelled():Void{
      eloSaverCallBack.eloSaveCancelled(elo);
      modalDialogBox.close();
   }

}


public class ScyDesktopEloSaver extends EloSaver {

   public var newTitleGenerator: NewTitleGenerator;
   public var myEloChanged: MyEloChanged;
   public var repository: IRepository;
   public var eloFactory: IELOFactory;
   public var titleKey: IMetadataKey;
   public var technicalFormatKey: IMetadataKey;
   public var window:ScyWindow;
   public var config:Config;
   public var windowStyler:WindowStyler;

   def logicalTypeDisplayNames = config.getLogicalTypeDisplayNames();
   def functionalTypeDisplayNames = config.getFunctionalTypeDisplayNames();

   def descriptionKey = config.getMetadataTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.DESCRIPTION);
//   def logicalTypeKey = config.getMetadataTypeManager().getMetadataKey(ScyRooloMetadataKeyIds.LOGICAL_TYPE);
//   def functionalTypeKey = config.getMetadataTypeManager().getMetadataKey(ScyRooloMetadataKeyIds.FUNCTIONAL_TYPE);
   var logicalTypeKey: IMetadataKey;
   var functionalTypeKey: IMetadataKey;

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
      showEloSaveAsPanel(elo,suggestedEloTitle,eloSaverCallBack);
   }

   function showEloSaveAsPanel(elo:IELO, suggestedEloTitle:String, eloSaverCallBack: EloSaverCallBack):Void{
      var eloSaveAsPanel = new EloSaveAsPanel();
      eloSaveAsPanel.setEloConfig(config.getEloConfig(window.eloType));
      eloSaveAsPanel.setLogicalTypeDisplayNames(logicalTypeDisplayNames);
      eloSaveAsPanel.setFunctinalTypeDisplayNames(functionalTypeDisplayNames);

      eloSaveAsPanel.setTitle(suggestedEloTitle);
      if (elo.getMetadata().metadataKeyExists(descriptionKey)){
         eloSaveAsPanel.setDescription(elo.getMetadata().getMetadataValueContainer(descriptionKey).getValue() as String);
      }
      if (logicalTypeKey!=null){
         if (elo.getMetadata().metadataKeyExists(logicalTypeKey)){
            eloSaveAsPanel.setLogicalType(elo.getMetadata().getMetadataValueContainer(logicalTypeKey).getValue() as String);
         }
      }
      if (functionalTypeKey!=null){
         if (elo.getMetadata().metadataKeyExists(functionalTypeKey)){
            eloSaveAsPanel.setFunctionalType(elo.getMetadata().getMetadataValueContainer(functionalTypeKey).getValue() as String);
         }
      }
      var eloIcon:EloIcon;
      var color:Color;
      if (window.eloUri!=null){
         eloIcon = windowStyler.getScyEloIcon(window.eloUri);
         color = windowStyler.getScyColor(window.eloUri);
      }
      else{
         eloIcon = windowStyler.getScyEloIcon(window.eloType);
         color = windowStyler.getScyColor(window.eloType);
      }


      var modalDialogBox = ModalDialogBox{
         content: Group{
               content: SwingComponent.wrap(eloSaveAsPanel);
            }
         targetScene:window.scene
         title:"Enter ELO specification"
         eloIcon:eloIcon
         color:color
      }
      eloSaveAsPanel.setEloSaveAsActionListener(EloSaveAsActionHandler{
            elo:elo;
            eloSaveAsPanel:eloSaveAsPanel
            myEloChanged:myEloChanged
            modalDialogBox:modalDialogBox
            eloSaverCallBack:eloSaverCallBack
         });
   }


   function getTypeDisplayNameList(typeNames:List, displayNames:DisplayNames):String[]{
      if (typeNames==null){
         return [];
      }
      for (type in typeNames){
         displayNames.getDisplayName(type as String);
      }
   }


   public override function eloUpdate(elo: IELO, eloSaverCallBack: EloSaverCallBack):Void{
      if (elo.getUri() != null) {
         var oldUri = elo.getUri();
         var newMetadata = repository.updateELO(elo);
         eloFactory.updateELOWithResult(elo, newMetadata);
         myEloChanged.myEloChanged(elo);
         //return elo;
      }
      else {
         eloSaveAs(elo, eloSaverCallBack);
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
      showEloSaveAsPanel(elo,suggestedEloTitle,eloSaverCallBack);

//      var newEloTitle = JOptionPane.showInputDialog("Enter title:", suggestedEloTitle);
//      if (StringUtils.hasText(newEloTitle)){
//         elo.getMetadata().getMetadataValueContainer(titleKey).setValue(newEloTitle);
//         var newMetadata:IMetadata;
//         if (forking){
//            newMetadata = repository.addForkedELO(elo);
//         }
//         else{
//            newMetadata = repository.addNewELO(elo);
//         }
//         eloFactory.updateELOWithResult(elo, newMetadata);
//         myEloChanged.myEloChanged(elo);
//         //return elo;
//      }
//      //return null;
   }

}
