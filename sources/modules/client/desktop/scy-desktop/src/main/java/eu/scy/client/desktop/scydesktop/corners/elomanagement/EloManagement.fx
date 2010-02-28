/*
 * EloManagement.fx
 *
 * Created on 28-feb-2010, 13:49:32
 */
package eu.scy.client.desktop.scydesktop.corners.elomanagement;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.ScyDesktop;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindowControl;
import eu.scy.client.desktop.scydesktop.tooltips.TooltipManager;
import java.util.List;
import org.apache.log4j.Logger;
import roolo.api.IRepository;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataKey;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import eu.scy.client.desktop.scydesktop.scywindows.EloIcon;
import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.ModalDialogBox;
import eu.scy.client.desktop.scydesktop.scywindows.window.CharacterEloIcon;
import javafx.scene.Group;
import javafx.util.Sequences;
import java.net.URI;

/**
 * @author sikken
 */
public class EloManagement extends CustomNode {

   def logger = Logger.getLogger(this.getClass());
   public var scyDesktop: ScyDesktop;
   public var scyWindowControl: ScyWindowControl;
   public var repository: IRepository;
   public var titleKey: IMetadataKey;
   public var technicalFormatKey: IMetadataKey;
   public var eloFactory: IELOFactory;
   public var templateEloUris: List;
   public var tooltipManager: TooltipManager;

   def newFromEloTemplateColor = Color.BLUE;
   def searchColor = Color.BLUE;
   def createBlankEloColor = Color.BLUE;

   def newFromEloTemplateButton =  Button {
      text: "New"
      action: createNewEloFromTemplateAction
   }
   def searchButton =  Button {
      text: "Search"
      action: searchEloAction
   }
   def createBlankEloButton =  Button {
      text: "Create (Dev/Author)"
      action: createNewBlankEloAction
   }

   init {
      if (eloFactory == null) {
         eloFactory = scyDesktop.config.getEloFactory();
      }
      if (scyWindowControl == null) {
         scyWindowControl = scyDesktop.scyWindowControl;
      }
      if (tooltipManager == null) {
         tooltipManager = scyDesktop.tooltipManager;
      }
      if (templateEloUris == null) {
         templateEloUris = scyDesktop.config.getTemplateEloUris();
      }
      //newFromEloTemplateButton.disable = templateEloUris == null or templateEloUris.isEmpty();
      findTemplateEloInformation();
   }

   public override function create(): Node {
      VBox{
         spacing:5;
         content:[
            newFromEloTemplateButton,
            searchButton,
            createBlankEloButton
         ]
      }
   }
   
   var eloTemplateUris:URI[];

   function findTemplateEloInformation(){
      if (templateEloUris != null){
         for (uri in templateEloUris){
            insert uri as URI into eloTemplateUris;
         }
         Sequences.sort(eloTemplateUris);
      }
      newFromEloTemplateButton.disable = sizeof eloTemplateUris == 0;
   }


   function createNewEloFromTemplateAction(): Void{
      var createNewElo = CreateNewElo{
         createAction:createNewEloFromTemplate
         cancelAction:cancelNewElo
      }
      createNewElo.listView.items = eloTemplateUris;
      createNewElo.label.text = "Select ELO template";
      var eloIcon = CharacterEloIcon{
         color:createBlankEloColor;
         iconCharacter:"N"
      }

      createModalDialog(createBlankEloColor,eloIcon,"Create new",createNewElo);
   }

   function createNewEloFromTemplate(createNewElo: CreateNewElo):Void{
      var eloTemplateUri = createNewElo.listView.selectedItem as URI;
      if (eloTemplateUri!=null){
         var newElo = repository.retrieveELO(eloTemplateUri);
         if (newElo!=null){
            var titleContainer = newElo.getMetadata().getMetadataValueContainer(titleKey);
            var templateTitle = titleContainer.getValue() as String;
            titleContainer.setValue(scyDesktop.newTitleGenerator.generateNewTitleFromName(templateTitle));
            var metadata = repository.addForkedELO(newElo);
            eloFactory.updateELOWithResult(newElo,metadata);
            scyWindowControl.addOtherScyWindow(newElo.getUri());
         }
         logger.error("can't find template elo, with uri: {eloTemplateUri}");
      }
      createNewElo.modalDialogBox.close();
   }

   function searchEloAction(): Void{

   }

   function createNewBlankEloAction(): Void{
      var createNewElo = CreateNewElo{
         createAction:createNewBlankElo
         cancelAction:cancelNewElo
      }
      var typeNames = scyDesktop.newEloCreationRegistry.getEloTypeNames();
      createNewElo.listView.items = Sequences.sort(typeNames);
      createNewElo.label.text = "Select ELO type";
      var eloIcon = CharacterEloIcon{
         color:createBlankEloColor;
         iconCharacter:"N"
      }

      createModalDialog(createBlankEloColor,eloIcon,"Create new",createNewElo);
   }

   function createNewBlankElo(createNewElo: CreateNewElo):Void{
      var eloTypeName = createNewElo.listView.selectedItem as String;
      if (eloTypeName!=null){
         var eloType = scyDesktop.newEloCreationRegistry.getEloType(eloTypeName);
         if (eloType!=null){
            var title = scyDesktop.newTitleGenerator.generateNewTitleFromType(eloType);
            var scyWindow = scyWindowControl.addOtherScyWindow(eloType);
            scyWindow.title = title;
         }
      }
      createNewElo.modalDialogBox.close();
   }

   function cancelNewElo(createNewElo: CreateNewElo):Void{
      createNewElo.modalDialogBox.close();
   }


   function createModalDialog(color:Color,eloIcon:EloIcon, title:String,modalDialogNode:ModalDialogNode){
      modalDialogNode.modalDialogBox = ModalDialogBox {
            content: Group{
               content:modalDialogNode.getContentNodes();
            }
            targetScene: scyDesktop.scene
            title: title
            eloIcon: eloIcon
            color: color
         }
   }

}
