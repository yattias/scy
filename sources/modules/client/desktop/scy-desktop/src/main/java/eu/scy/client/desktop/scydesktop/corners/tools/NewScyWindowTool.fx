/*
 * NewScyWindowTool.fx
 *
 * Created on 7-jul-2009, 17:01:29
 */

package eu.scy.client.desktop.scydesktop.corners.tools;

import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;

import eu.scy.client.desktop.scydesktop.ScyDesktop;
import javax.swing.JOptionPane;

import javafx.scene.layout.VBox;

import roolo.cms.repository.mock.BasicMetadataQuery;

import roolo.cms.repository.search.BasicSearchOperations;

import roolo.api.IRepository;
import roolo.elo.api.IMetadataKey;

import eu.scy.client.desktop.scydesktop.utils.log4j.Logger;

import java.net.URI;

import java.util.List;

import roolo.api.search.ISearchResult;

import eu.scy.client.desktop.scydesktop.scywindows.ScyWindowControl;

import eu.scy.client.desktop.scydesktop.scywindows.NewTitleGenerator;

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

   init{
      if (scyWindowControl==null){
         scyWindowControl = scyDesktop.scyWindowControl;
      }
   }


//   var newWindowCounter = 0;
   var newButton:Button;
   var loadButton:Button;

   public override function create(): Node {
      return Group {
         content: [
            VBox{
               spacing:5;
               content:[
                  newButton = Button {
                     text: "New"
                     action: createNewScyWindow
                  }
                  loadButton = Button {
                     text: "Search"
                     action: loadElo
                  }
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
            var title = scyDesktop.newTitleGenerator.generateNewTitle(eloType);
            var scyWindow = scyWindowControl.addOtherScyWindow(eloType);
            scyWindow.title = title;
         }
      }
   }

   // TODO, make the checkStatus called automaticly, so that the state is always up to date
   function checkStatus():Boolean{
      var typeNames = scyDesktop.newEloCreationRegistry.getEloTypeNames();
      newButton.disable = typeNames.length==0;
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
      var eloTypeName = getNewEloTypeName("Open ELO");
      if (eloTypeName!=null){
         var eloType = scyDesktop.newEloCreationRegistry.getEloType(eloTypeName);
         if (eloType!=null){
            var typeQuery = new BasicMetadataQuery(technicalFormatKey,BasicSearchOperations.EQUALS,eloType,null);
            var results = repository.search(typeQuery);
            logger.info("Nr of elos found: {results.size()}");
            if (results!=null and results.size()>0){
               var eloUri = askUri(results);
               if (eloUri!=null){
                  scyWindowControl.addOtherScyWindow(eloUri);
               }
            }
            else{
               JOptionPane.showMessageDialog(null, "No ELOs found","Open ELO",JOptionPane.PLAIN_MESSAGE);
            }

         }
      }
   }

   function askUri(searchResults: List):URI{
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
}

