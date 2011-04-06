/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.corners.elomanagement;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import eu.scy.client.desktop.scydesktop.scywindows.EloIcon;
import javafx.scene.layout.VBox;
import javafx.scene.Group;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.geometry.VPos;
import javafx.util.Math;

/**
 * @author SikkenJ
 */
public class SimpleScySearchResultCellNode extends CustomNode {

   public var scySearchResult: ScySearchResult on replace { newScySearchResult() };
   public var disableRelevanceDisplay = false;
   def titleDisplay = Label {};
   def authorDisplay = Label {};
   def dateDisplay = Label {};
   def spacing = 5.0;
   def relevanceBarWidth = 10.0;
   def relevanceBarHeight = 40.0;
   var relevance: Number;
   def hBox = HBox {
              spacing: spacing
              content: []
              nodeVPos: VPos.CENTER
           }

   public override function create(): Node {
      hBox
   }

   public function getRelevanceNode(relevance: Number) {
      //TODO This has to be done as the relevance might be greater than 1.0 and normalization has to be discussed
      def displayRelevance = Math.min(1.0, Math.max(0.0, relevance));
      def fillColor = Color.hsb(displayRelevance * 120.0, 1.0, 1.0);
      def displayHeight = relevanceBarHeight * displayRelevance;
      def relevanceNode = Group {
                 content: [
                    Rectangle {
                       y: relevanceBarHeight - displayHeight
                       width: relevanceBarWidth
                       height: displayHeight
                       stroke: null
                       fill: fillColor
                    }
                    Rectangle {
                       width: relevanceBarWidth
                       height: relevanceBarHeight
                       stroke: Color.BLACK
                       fill: Color.TRANSPARENT
                    },
                 ]
              }
      if ((not disableRelevanceDisplay) and scySearchResult != null) {
         return relevanceNode;
      }
      return Group { content: [] };
   }

   function newScySearchResult() {
      relevance = scySearchResult.getRelevance();
      titleDisplay.text = scySearchResult.getScyElo().getTitle();
      authorDisplay.text = ScyEloDisplayProperties.getAuthorsText(scySearchResult.getScyElo());
      dateDisplay.text = ScyEloDisplayProperties.getDateString(scySearchResult.getScyElo());
      hBox.content = [
                 getRelevanceNode(relevance),
                 scySearchResult.getEloIcon() as EloIcon,
                 VBox {
                    spacing: -1
                    content: [
                       titleDisplay,
                       authorDisplay,
                       dateDisplay
                    ]
                 }
              ];
   }

}
