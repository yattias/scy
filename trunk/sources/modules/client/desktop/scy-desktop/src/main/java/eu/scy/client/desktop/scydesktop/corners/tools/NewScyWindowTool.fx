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

import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import eu.scy.client.desktop.scydesktop.scywindows.window.StandardScyWindow;

import javafx.scene.layout.VBox;

import roolo.cms.repository.mock.BasicMetadataQuery;

import roolo.cms.repository.search.BasicSearchOperations;

import roolo.api.IRepository;
import roolo.elo.api.IMetadataKey;

import org.apache.log4j.Logger;

import java.net.URI;

import java.util.List;

import roolo.api.search.ISearchResult;

/**
 * @author sikkenj
 */

// place your code here

var logger = Logger.getLogger("eu.scy.client.desktop.scydesktop.corners.tools.NewScyWindowTool");

public class NewScyWindowTool extends CustomNode {

   public var scyDesktop: ScyDesktop;
   public var repository:IRepository;
   public var titleKey : IMetadataKey;
   public var technicalFormatKey : IMetadataKey;

   var newWindowCounter = 0;
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
                     text: "Load"
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
            var title = "new {eloTypeName} {++newWindowCounter}";
            createScyWindow(null,eloType,title);
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

   function createScyWindow(eloUri:URI,eloType:String,title:String){
      var window:ScyWindow = StandardScyWindow{
         title:title
         eloUri:eloUri;
         eloType:eloType;
//         id:"new://{title}"
         allowClose: true;
         allowResize: true;
         allowRotate: true;
         allowMinimize: true;
         cache:true;
      }
      scyDesktop.addScyWindow(window);
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
                  scyDesktop.addScyWindow(eloUri);
//                  var eloMetadata = repository.retrieveMetadata(eloUri);
//                  var title = eloMetadata.getMetadataValueContainer(titleKey).getValue() as String;
//                  var realEloType = eloMetadata.getMetadataValueContainer(technicalFormatKey).getValue() as String;
//                  createScyWindow(eloUri,realEloType,title);
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



//   function getNewEloType():String{
//      var typeNames = scyDesktop.newEloCreationRegistry.getEloTypeNames();
//      if (typeNames.length>0){
//         var choice = JOptionPane.showInputDialog(null, "Select type", "Create new ELO", JOptionPane.QUESTION_MESSAGE, null, typeNames, null) as String;
//         if (choice!=null){
//            return scyDesktop.newEloCreationRegistry.getEloType(choice);
//         }
//      }
//      return null;
//   }

}

