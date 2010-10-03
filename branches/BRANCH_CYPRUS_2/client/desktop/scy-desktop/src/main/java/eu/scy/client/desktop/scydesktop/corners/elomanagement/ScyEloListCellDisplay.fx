/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.corners.elomanagement;

import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import eu.scy.client.desktop.scydesktop.elofactory.NewEloCreationRegistry;
import eu.scy.common.scyelo.ScyElo;

/**
 * @author SikkenJ
 */
public mixin class ScyEloListCellDisplay {

   public var newEloCreationRegistry: NewEloCreationRegistry;

   public function scyEloCellFactory(): ListCell {
      var listCell: ListCell;
      listCell = ListCell {
            node: Label {
               text: bind getResultDisplay(listCell.item)
            }
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
      return "{authorDisplay}:{scyElo.getTitle()} ({typeName})"
   }

}
