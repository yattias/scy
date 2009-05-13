/*
 * Main.fx
 *
 * Created on 4-sep-2008, 15:21:10
 */

package eu.scy.elobrowser.main;

import eu.scy.elobrowser.main.EloSpecWidget;
import eu.scy.elobrowser.main.ResultView;
import eu.scy.elobrowser.main.ResultViewModel;
import eu.scy.elobrowser.main.Roolo;
import java.lang.System;
import javafx.ext.swing.SwingButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;



/**
   * @author sikkenj
 */

 // place your code here
Stage
{
   title: "ELO browser"
   width: 860
   height: 805
   onClose: function()  {
      java.lang.System.exit( 0 ); 
   }
   visible: true
   
   var roolo= Roolo.getRoolo();
   var queryEntryX = EloSpecWidget
   {
      roolo: roolo;
      title: "X axis";
   }
   var queryEntryY = EloSpecWidget
   {
      roolo: roolo;
      title: "Y axis";
   }
   var queryEntryZ = EloSpecWidget
   {
      roolo: roolo;
      title: "Size";
   }
   var resultViewModel = ResultViewModel{};
   var resultView = ResultView
   {
      xSize: 500;
      ySize:500;
   }
   //   var queryEntryXCanvas = Canvas
   //   {
   //      content:[queryEntryX]
   //   }
   //   var queryEntryYCanvas = Canvas
   //   {
   //      content:[queryEntryY]
   //   }
   //   var queryEntryZCanvas = Canvas
   //   {
   //      content:[queryEntryZ]
   //   }
   //   var resultViewCanvas = Canvas
   //   {
   //      content:[resultView]
   //   }

   var searchButton = SwingButton
   {
      text: "Search"
      action: function()  {
         var queryX = queryEntryX.getSearchQuery();
         var resultsX = roolo.repository.search(queryX);
         System.out.println("Query X: {queryX.toString()}\nNr of search results {resultsX.size()}");
         var queryY = queryEntryY.getSearchQuery();
         var resultsY = roolo.repository.search(queryY);
         System.out.println("Query Y: {queryY.toString()}\nNr of search results {resultsY.size()}");
         var queryZ = queryEntryZ.getSearchQuery();
         var resultsZ = roolo.repository.search(queryZ);
         System.out.println("Query Z: {queryZ.toString()}\nNr of search results {resultsZ.size()}");
         var combinedSearchResults =resultViewModel.newSearchResults(resultsX,resultsY,resultsZ);
         resultView.resultsChanged(combinedSearchResults);
//	 searchResultList.searchResults = results;
//	 searchResultList.label = query.toString();


      }
   }

   scene: Scene {
      content: [
         VBox{
             spacing:10;
            content: [
               HBox{
             spacing:10;
               content:[queryEntryX,queryEntryY,queryEntryZ,searchButton]},
               resultView
            ]
         }
//              ComponentView {
//                 component: ClusterPanel {
//                    hcluster: ParallelCluster {
//                       content: [
//                          SequentialCluster {
//                             content: [
//				  queryEntryXCanvas,queryEntryYCanvas,queryEntryZCanvas,searchButton
//                             ]
//                          },
//                          resultViewCanvas
//                       ]
//                    }
//                    vcluster: SequentialCluster {
//                       content: [
//                          ParallelCluster {
//                             content: [
//				  queryEntryXCanvas,queryEntryYCanvas,queryEntryZCanvas,searchButton
//                             ]
//                          },
//                          resultViewCanvas
//                       ]
//                    }
//                 }
//         }
      ]
   }
}