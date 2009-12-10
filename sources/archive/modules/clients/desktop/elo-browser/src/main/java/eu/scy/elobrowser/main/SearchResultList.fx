/*
 * SearchResultList.fx
 *
 * Created on 1-okt-2008, 21:40:38
 */

package eu.scy.elobrowser.main;

import eu.scy.elobrowser.main.EloSpecWidget;
import eu.scy.elobrowser.main.Roolo;
import eu.scy.elobrowser.main.SearchResultList;
import java.lang.*;
import java.util.Iterator;
import java.util.List;
import javafx.ext.swing.SwingButton;
import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import roolo.api.search.ISearchResult;

/**
 * @author sikken
 */

 // place your code here
public class SearchResultList extends CustomNode {
   public var label = "Search results";
   public var searchResults:List;
   public var searchResultsSeq:ISearchResult[];
   public var xOffset = 0;
   public var yOffset = 20;
   public var yStep = 20;
   
   public override function create(): Node {
      var labelDisplay = Text
      {
         x: xOffset,
         y: yOffset;
         content: bind label;
      }
      //      var searchResultNodes:Node[] = bind createSearchResultDisplays(searchResults);
      var nodes = bind Group
      {
         content: [
	    labelDisplay,
            //	    for (node in searchResultNodes)
            //	    {
            //		node;
            //	    },
            //	    for (node in createSearchResultDisplays(searchResults))
            //	    {
            //		node;
            //	    },
            for (searchResult in searchResultsSeq)  {
               createSearchResultDisplay(searchResult);
            }
         ]
      };
      return nodes;
   }
   
   function createSearchResultDisplays(searchResultList:List):Node[] {
      //System.out.println("createSearchResultDisplay()");
      var nodes:Node[] = [];
      //if (searchResultList!=null)
      {
         var yPos = yOffset;
         var iterator = searchResultList.iterator();
	 while (
         iterator.hasNext()) {
            yPos += 20;
            var searchResult =
            iterator.next() as ISearchResult;
            var searchResultDisplay = Text
	    {
               x: xOffset,
               y: yPos;
               content: "{searchResult.getRelevance()}:{searchResult.getUri()}"
	    }
	    insert  searchResultDisplay into nodes;
            System.out.println("Placed {searchResult.getRelevance()}:{searchResult.getUri()}");
         }
      }
      return nodes;
   }
   
   function createSearchResultDisplay(searchResult:ISearchResult):Node {
      var searchResultDisplay = Text 
      {
         x: xOffset,
         y: yOffset + yStep;
         content: "{searchResult.getRelevance()}:{searchResult.getUri()}"
      }
      return searchResultDisplay;
   }
}

function run() {
      Stage
{
      title: "Test SearchResultList"
      width: 400
      height: 400
      onClose: function() {
         java.lang.System.exit( 0 );

      }
      visible: true

      var roolo= Roolo.getRoolo();
      var queryEntry = EloSpecWidget
   {
         roolo: roolo;
         title: "Query entry";
   }
      var searchResultList = SearchResultList
   {
   }
      var searchButton = SwingButton
   {
         text: "Search"
         action: function()  {
            var query = queryEntry.getSearchQuery();
            var results = roolo.repository.search(query);
            System.out.println("Query: {query.toString()}\nNr of search results {results.size()}");
            searchResultList.searchResults = results;
            searchResultList.label = query.toString();
         }
   }

      scene: Scene {
         content: [
            VBox{
               content: [queryEntry,searchButton,searchResultList]
            }
         ]



      }

//      var queryEntryCanvas = Canvas
//   {
//         content:[queryEntry]
//   }
//      var searchResultListCanvas = Canvas
//   {
//         content:[searchResultList];
//         background:Color.YELLOW;
//         width: 400
//         height:200;
//         hmin:200;
//         vmin:200;
//   }
//
//      scene: Scene {
//         content: [
//            ComponentView {
//               component: ClusterPanel {
//                  hcluster: ParallelCluster {
//                     content: [
//			    queryEntryCanvas,searchButton,searchResultListCanvas
//                     ]
//                  }
//                  vcluster: SequentialCluster {
//                     content: [
//			    queryEntryCanvas,searchButton,searchResultListCanvas
//                     ]
//                  }
//               }
//            }
//         ]
//
//      }
 }
}
