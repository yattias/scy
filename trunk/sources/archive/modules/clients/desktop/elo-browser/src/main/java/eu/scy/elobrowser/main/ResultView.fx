/*
 * ResultView.fx
 *
 * Created on 6-okt-2008, 19:24:23
 */

package eu.scy.elobrowser.main;

import eu.scy.elobrowser.main.CombinedSearchResult;
import eu.scy.elobrowser.main.EloDisplay;
import eu.scy.elobrowser.main.ResultView;
import eu.scy.elobrowser.main.ResultViewModel;
import eu.scy.elobrowser.main.Roolo;
import eu.scy.elobrowser.model.mapping.DisplayEloMapping;
import eu.scy.elobrowser.model.mapping.DisplayMapping;
import eu.scy.elobrowser.model.mapping.DisplayProperty;
import eu.scy.scywindows.ScyDesktop;
import java.lang.Math;
import java.lang.Object;
import java.lang.System;
import java.net.URI;
import java.util.List;
import java.util.Locale;
import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import roolo.elo.api.IMetadata;

/**
 * @author sikken
 */

public class ResultView extends CustomNode {
	public var roolo: Roolo;
	public var resultViewModel: ResultViewModel;
	public var xOffset = 50;
	public var yOffset = 50;
	public var zOffset = 16;
	public var xSize = 200 on replace {
		placeEloDisplays()
   };
	public var ySize = 200 on replace {
		placeEloDisplays()
   };
	public var zSize = 48;
	public var nrOfElos = 40;
	public var displayEloMappings: List on replace {
		placeEloDisplays()
   };
   
	var eloDisplays: EloDisplay[];
   
	public override function create(): Node {
      Group {
         content: [
            /*			content	Rectangle {
             x: 0,
             y: 0
             width: bind xSize + 2 * xOffset,
             height: bind ySize + 2 * xOffset
             fill: null;
             stroke: Color.BLACK;
             },
             Rectangle
             {
             x: xOffset,
             y: yOffset
             arcHeight: 150
             arcWidth: 150
             width: bind xSize,
             height: bind ySize
             effect: DropShadow {
             radius: 50;
             },
             fill: Color.color(0.5,0.5,0.5,0.1)
             },*/
            for (i in [1..nrOfElos]) {
               var relevance: Number = 1.0 * i / nrOfElos;
               var eloDisplay = EloDisplay {
						title: "elo {i}";
						translateX: calculateX(relevance);
						translateY: calculateY(1 - relevance);
						radius: calculateZ(relevance);
						visible: false;
						roolo: roolo;
               }
	       insert eloDisplay into eloDisplays;
					eloDisplay;
				}
			]
      }
	}

	public function newDisplayEloMappings(displayEloMappings: List) {
		this.displayEloMappings = displayEloMappings;
	}

	public function placeEloDisplays() {
		System.out.println("Nr of eloDisplay to place {displayEloMappings.size()}");
		for (eloDisplay in eloDisplays) {
			eloDisplay.translateX = calculateX(0);
			eloDisplay.translateY = calculateY(0.0);
			eloDisplay.radius = calculateZ(0.0);
			eloDisplay.visible = false;
		}
		var i = 0;
		var displayEloMappingIterator = displayEloMappings.iterator();

      while
      (
		displayEloMappingIterator.hasNext()){
			var displayEloMapping =
         displayEloMappingIterator.next() as DisplayEloMapping;
			System.out.println("placing eloDisplay {i}, {displayEloMapping.getElo().getUri()}");
			var eloDisplay = eloDisplays[i];
			if (eloDisplay == null){
				System.out.println("not enough elo displays, have {i+1}, but need {displayEloMappings.size()}");
            break;
			}
			eloDisplay.elo = displayEloMapping.getElo();
			eloDisplay.eloType = displayEloMapping.getEloType();
			var displayMappingIterator = displayEloMapping.getDisplayMappings().iterator();
         while
         (
			displayMappingIterator.hasNext()){
				var displayMapping =
            displayMappingIterator.next() as DisplayMapping;
				System.out.println("- setting property, {displayMapping}");
				if (displayMapping.getValue() == null)
				continue;
				if (displayMapping.getDisplayProperty() == DisplayProperty.X)
				{
					eloDisplay.translateX = calculateX(displayMapping.getValue());
				} else
				if (displayMapping.getDisplayProperty() == DisplayProperty.Y) {
					eloDisplay.translateY = calculateY(displayMapping.getValue());
				} else
				if (displayMapping.getDisplayProperty() == DisplayProperty.SIZE) {
					eloDisplay.radius = calculateZ(displayMapping.getValue());
				}
			}
			eloDisplay.title = getTitleOutEloMetadata(displayEloMapping.getElo().getMetadata());
			//eloDisplay.title = getTitleFromUri(displayEloMapping.getElo().getUri());
			//         System.out.println("- x:{eloDisplay.translateX}, y:{eloDisplay.translateY}, size:{eloDisplay.radius}, eloType:{eloDisplay.eloType}");
			//		   System.out.println("- image:{eloDisplay.image}, url:{eloDisplay.image.url}, width:{eloDisplay.image.width}, ");
			eloDisplay.visible = true;
         ScyDesktop.getScyDesktop().addScyWindow(eloDisplay.eloWindow);
         ++i;
		}

	}
   
	public function resultsChanged(combinedSearchResults : CombinedSearchResult[]) {
      //       var combinedSearchResults = resultViewModel.combinedSearchResults;
      System.out.println("Nr of eloDisplay to place {combinedSearchResults.size()}");
		for (j in [0..
         (
      nrOfElos - 1)]) {
			eloDisplays[j].visible = false;
		}
		var i = 0;
		for (combinedSearchResult in combinedSearchResults) {
			var eloDisplay = eloDisplays[i];
			eloDisplay.translateX = calculateX(combinedSearchResult.relevanceX);
			eloDisplay.translateY = calculateY(combinedSearchResult.relevanceY);
			eloDisplay.radius = calculateZ(combinedSearchResult.relevanceZ);
			eloDisplay.title = getTitle(combinedSearchResult.uri);
			eloDisplay.visible = true;
         System.out.println("placing eloDisplay {i}");
	   ++i;
		}
	}

	function calculateX(relevance:Number):Integer {
      return
      calculateSize(xOffset,xSize,relevance);
	}
   
	function calculateY(relevance:Number):Integer {
      return
      calculateSize(yOffset,ySize,relevance);
	}
   
	function calculateZ(relevance:Number):Integer {
      return
      calculateSize(zOffset,zSize,relevance);
	}
   
	function calculateSize(offset:Integer,maxSize:Integer,value:Number) : Integer {
		var useValue = Math.max(0, Math.min(1,value));
		var size = Math.round(offset + useValue * maxSize) as Integer;
      //System.out.println("offset:{offset}, maxSize:{maxSize}, value:{value}, size:{size}");
      return size;
	}

	function getTitle(uri:URI): String {
		var metadata = roolo.repository.retrieveMetadata(uri);
		if (metadata == null)
		{
         System.out.println("there is no metadata for {uri}");
			var elo = roolo.repository.retrieveELO(uri);
			if (elo != null)
			{
				metadata = elo.getMetadata();
			}
	  else
			{
            System.out.println("there is no elo for {uri}");
			}
		}
      return getTitleOutEloMetadata(metadata);
	}
	function getTitleOutEloMetadata(metadata:IMetadata): String {
		var title = null;
		if (metadata != null)
		{
			title = metadata.getMetadataValueContainer(roolo.titleKey).getValue() as String;
			if (title == null)
			title = metadata.getMetadataValueContainer(roolo.titleKey).getValue(Locale.ENGLISH) as String;
		}
		//      if (title == null)
		//      {
		//         // still no title found, apply a quick hack, the title is also in the uri
		//         var uriString = uri.toString();
		//         var lastSlashPos =  uriString.lastIndexOf('/');
		//         var lastPointPos =  uriString.lastIndexOf('.');
		//         title = uriString.substring(lastSlashPos + 1, lastPointPos);
		//      }
		if (title == null)
		title ="<<no title>>";
      return title;
	}
   
	function getTitleFromUri(uri:URI): String {
		var uriString = uri.toString();
		var lastSlashPos = uriString.lastIndexOf('/');
		var lastPointPos = uriString.lastIndexOf('.');
      return uriString.substring(lastSlashPos + 1, lastPointPos);
	}
   
}

function run() {

	Stage {
		title: "ResultView test"
		width: 300
		height: 300
		onClose: function() {
         java.lang.System.exit( 0 );
		}
		visible: true

		scene: Scene {
			content: [
            ResultView{
         }]
		}
 
   }
}
