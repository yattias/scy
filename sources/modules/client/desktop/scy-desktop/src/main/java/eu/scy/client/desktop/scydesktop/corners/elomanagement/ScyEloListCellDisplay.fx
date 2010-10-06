/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.corners.elomanagement;

import javafx.scene.control.ListCell;
import eu.scy.client.desktop.scydesktop.elofactory.NewEloCreationRegistry;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.client.desktop.scydesktop.scywindows.WindowStyler;
import javafx.scene.Node;
import javafx.scene.control.Label;
import eu.scy.client.desktop.scydesktop.scywindows.EloIcon;
import javafx.scene.layout.HBox;

/**
 * @author SikkenJ
 */
public mixin class ScyEloListCellDisplay {

   public var newEloCreationRegistry: NewEloCreationRegistry;
   public var windowStyler: WindowStyler;
   def spacing = 5.0;

   public function scyEloCellFactory(): ListCell {
      var listCell: ListCell;
      listCell = ListCell {
            node: bind createListCellContent(listCell.item)
         }
   }

   public function simpleScyEloCellFactory(): ListCell {
      var listCell: ListCell;
      listCell = ListCell {
            node: bind createSimpleListCellContent(listCell.item)
         }
   }

   function createSimpleListCellContent(item: Object): Node {
      def scySearchResult = item as ScySearchResult;
      var eloIcon: EloIcon = scySearchResult.getEloIcon() as EloIcon;
//      eloIcon.visible = scySearchResult!=null;
      println("creating simple cell display for {item}, eloIcon: {eloIcon}");
      HBox {
         spacing: spacing
         content: [
            eloIcon,
            Label {
               text: bind scySearchResult.getScyElo().getTitle()
            }
         ]
      }
   }

   function createListCellContent(item: Object): Node {
      var scySearchResult: ScySearchResult = null;
      if (item instanceof ScySearchResult){
         scySearchResult = item as ScySearchResult;
      }
      var eloIcon: EloIcon = windowStyler.getScyEloIcon(scySearchResult.getScyElo().getUri());
      eloIcon.visible = scySearchResult!=null;
//      println("creating cell display for {item}, eloIcon: {eloIcon}");
      HBox {
         spacing: spacing
         content: [
            eloIcon,
            Label {
               text: bind getResultDisplay(item)
            }
         ]
      }
   }

   function getResultDisplay(item: Object): String {
      if (item instanceof ScySearchResult) {
         def searchResult = item as ScySearchResult;
         return getEloDescription(searchResult.getScyElo());
      }
      if (item instanceof String) {
         return item as String
      }

      return ""
   }

   public function getEloDescription(scyElo: ScyElo): String {
      var authorDisplay = ##"unknown";
      def authors = scyElo.getAuthors();
      if (authors.size() > 0) {
         authorDisplay = "";
         for (author in authors) {
            if (indexof author > 0) {
               authorDisplay = "{authorDisplay}, ";
            }
            authorDisplay = "{authorDisplay}{author}";
         }
      }
      def typeName = newEloCreationRegistry.getEloTypeName(scyElo.getTechnicalFormat());
      return "{authorDisplay}: {scyElo.getTitle()} ({typeName})"
   }

}
