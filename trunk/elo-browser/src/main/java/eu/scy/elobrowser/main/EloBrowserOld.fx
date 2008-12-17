/*
 * EloBrowser.fx
 *
 * Created on 7-dec-2008, 11:32:07
 */

package eu.scy.elobrowser.main;

import eu.scy.elobrowser.main.EloSpecWidget;
import eu.scy.elobrowser.main.ResultView;
import eu.scy.elobrowser.main.Roolo;
import java.lang.System;
import javafx.ext.swing.SwingButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author sikken
 */

 // place your code here
Stage {
   title: "ELO browser"

   onClose: function()  {
      java.lang.System.exit( 0 );
   }
   visible: true

   var roolo= Roolo.getRoolo();
   var queryEntry1 = EloSpecWidget
   {
      roolo: roolo;
      title: "Query";
   }
   var metadataDisplayMappingWidget = MetadataDisplayMappingWidget{
         roolo: roolo;
   }
   var resultView = ResultView
   {
      xSize: 500;
      ySize:500;
   }
   var searchButton = SwingButton
   {
      text: "Search"
      action: function()  {
         var query1 = queryEntry1.getSearchQuery();
         System.out.println("Query 1: {query1.toString()}");
         var displayEloMappings = roolo.queryToElosDisplay.getDisplayEloMapping(metadataDisplayMappingWidget.getMappingElo(),query1);
         System.out.println("Query 1: {query1.toString()}\nNr of elos {displayEloMappings.size()}");
         resultView.newDisplayEloMappings(displayEloMappings);
      }
   }

   scene: Scene {
      width: 900
      height: 620
      content: [
         HBox{
             translateX:10;
             translateY:10;
            spacing:10;
            content: [
               VBox{
                  spacing:10;
                  content:[queryEntry1,metadataDisplayMappingWidget,searchButton]},
               resultView
            ]
         }
      ]
   }
}