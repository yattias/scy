/*
 * NewScyWindowTool.fx
 *
 * Created on 7-jul-2009, 17:01:29
 */

package eu.scy.client.desktop.scydesktop.corners.tools;

import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;

import eu.scy.client.desktop.scydesktop.ScyDesktop;
import javax.swing.JOptionPane;

import javafx.scene.layout.VBox;


import roolo.api.IRepository;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IELOFactory;
import roolo.search.ISearchResult;
import roolo.search.SearchOperation;
import roolo.search.Query;
import roolo.search.IQuery;
import roolo.search.IQueryComponent;
import roolo.search.MetadataQueryComponent;
import eu.scy.client.desktop.desktoputils.log4j.Logger;

import java.net.URI;

import java.util.List;

import eu.scy.client.desktop.scydesktop.scywindows.ScyWindowControl;

import eu.scy.client.desktop.scydesktop.tooltips.TooltipManager;
import javafx.scene.control.Button;

/**
 * @author sikkenj
 */

// place your code here

public class NewScyWindowTool extends CustomNode {
   def logger = Logger.getLogger(this.getClass());

   public var scyDesktop: ScyDesktop;
   public var scyWindowControl: ScyWindowControl;
   public var repository:IRepository;
   public var titleKey : IMetadataKey;
   public var technicalFormatKey : IMetadataKey;
   public var eloFactory:IELOFactory;
   public var templateEloUris:List;
   public var tooltipManager: TooltipManager;

   init{
      if (eloFactory==null){
         eloFactory = scyDesktop.config.getEloFactory();
      }
      if (scyWindowControl==null){
         scyWindowControl = scyDesktop.scyWindowControl;
      }
      if (tooltipManager==null){
         tooltipManager = scyDesktop.tooltipManager;
      }
      if (templateEloUris==null){
         templateEloUris = scyDesktop.config.getTemplateEloUris();
      }
      newButton.disable = templateEloUris==null or templateEloUris.isEmpty();
   }


//   var newWindowCounter = 0;
   var newButton:Button;
   var loadButton:Button;
   var createButton:Button;

   public override function create(): Node {
      newButton = Button {
         text: "New"
//         tooltip:"Fork from template"
         action: createNewScyWindowFromTemplate
 //        disable:templateEloUris==null or templateEloUris.isEmpty()
      }
      loadButton = Button {
         text: "Search"
         action: loadElo
      }
      createButton = Button {
         text: "Create (Dev/Author)"
//         tooltip:"Create new\nOnly for developers and authors"
         action: createNewScyWindow
      }
//      tooltipManager.registerNode(newButton, newButton);
//      tooltipManager.registerNode(loadButton, loadButton);
//      tooltipManager.registerNode(createButton, createButton);
      return Group {
         content: [
            VBox{
               spacing:5;
               content:[
                  newButton,
                  loadButton,
                  createButton
               ]
            }

         ]
      };
   }

   function createNewScyWindow(){
      if (not checkStatus()) return;
      var eloTypeName = getNewEloTypeName("Create new ELO");
      if (eloTypeName!=null){
         var eloType = scyDesktop.newEloCreationRegistry.getEloType(eloTypeName);
         if (eloType!=null){
            var title = scyDesktop.newTitleGenerator.generateNewTitleFromType(eloType);
            var scyWindow = scyWindowControl.addOtherScyWindow(eloType);
            scyWindow.title = title;
         }
      }
   }

   // TODO, make the checkStatus called automaticly, so that the state is always up to date
   function checkStatus():Boolean{
      var typeNames = scyDesktop.newEloCreationRegistry.getEloTypeNames();
      createButton.disable = typeNames.length==0;
      loadButton.disable = typeNames.length==0;
      return typeNames.length>0;
   }


   function getNewEloTypeName(dialogTitle:String):String{
      var typeNames = scyDesktop.newEloCreationRegistry.getEloTypeNames();
      if (typeNames.length>0){
         var choice = JOptionPane.showInputDialog(null, "Select type", dialogTitle, JOptionPane.QUESTION_MESSAGE, null, typeNames, null) as String;
         return choice;
      }
      return null;
   }

   function loadElo(){
      if (not checkStatus()) return;
      var eloTypeName = getNewEloTypeName("Search for ELOs");
      if (eloTypeName!=null){
         def eloType = scyDesktop.newEloCreationRegistry.getEloType(eloTypeName);
         if (eloType!=null){
            def typeQueryComponent:IQueryComponent = new MetadataQueryComponent(technicalFormatKey,SearchOperation.EQUALS,eloType);
            def typeQuery:IQuery = new Query(typeQueryComponent);
            var results = repository.search(typeQuery);
            logger.info("Nr of elos found: {results.size()}");
            if (results!=null and results.size()>0){
               var eloUri = askUriFromSearchResults(results);
               if (eloUri!=null){
                  scyWindowControl.addOtherScyWindow(eloUri);
                  scyWindowControl.makeMainScyWindow(eloUri);
               }
            }
            else{
               JOptionPane.showMessageDialog(null, "No ELOs found","Search for ELOs",JOptionPane.PLAIN_MESSAGE);
            }
         }
      }
   }

   function askUriFromSearchResults(searchResults: List):URI{
      var displayUris:String[];
      for (result in searchResults){
         var searchResult = result as ISearchResult;
         insert "{searchResult.getUri()}" into displayUris;
      }
      var selectedUri = JOptionPane.showInputDialog(null,"Select elo to open","Open ELO",
         JOptionPane.QUESTION_MESSAGE,null,displayUris,null);
      if (selectedUri!=null){
         return new URI(selectedUri as String);
      }
      return null;
   }

   function askUri(uris: List):URI{
      var displayUris:String[];
      for (uri in uris){
         insert "{uri as URI}" into displayUris;
      }
      var selectedUri = JOptionPane.showInputDialog(null,"Select elo to copy","Create new ELO",
         JOptionPane.QUESTION_MESSAGE,null,displayUris,null);
      if (selectedUri!=null){
         return new URI(selectedUri as String);
      }
      return null;
   }

   function createNewScyWindowFromTemplate(){
      var eloUri = askUri(templateEloUris);
      if (eloUri!=null){
         var newElo = repository.retrieveELO(eloUri);
         if (newElo!=null){
            var titleContainer = newElo.getMetadata().getMetadataValueContainer(titleKey);
            var templateTitle = titleContainer.getValue() as String;
            titleContainer.setValue(scyDesktop.newTitleGenerator.generateNewTitleFromName(templateTitle));
            var metadata = repository.addForkedELO(newElo);
            eloFactory.updateELOWithResult(newElo,metadata);
            scyWindowControl.addOtherScyWindow(newElo.getUri());
         }
         logger.error("can't find template elo, with uri: {eloUri}");
      }
   }

}

