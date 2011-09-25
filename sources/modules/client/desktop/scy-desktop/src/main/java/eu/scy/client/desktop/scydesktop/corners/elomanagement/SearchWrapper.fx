/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.corners.elomanagement;

import eu.scy.common.scyelo.ScyElo;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import eu.scy.client.desktop.scydesktop.tools.search.EloSearchNode;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindowControl;

/**
 * @author SikkenJ
 */
public class SearchWrapper {

   public-init var scyWindowControl: ScyWindowControl;
   public-init var baseElo: ScyElo;
   public-read var isSaved = false;
   public-read var isEloBased = false;
   public-read var searchWindow: ScyWindow;
   var searchNode: EloSearchNode;

   init {
      createSearchWindow();
   }

   function createSearchWindow(): Void {
      searchWindow = scyWindowControl.addOtherScyWindow("scy/search");
      searchWindow.title = ##"Search";
      searchWindow.allowDrawers = false;
      searchWindow.closedAction = function(window: ScyWindow): Void {
                 if (window.eloUri == null) {
                    scyWindowControl.removeOtherScyWindow(window);
                 } else {
                    window.closedAction = null;
                 }
              }
      setupEloSearchNode();
   }

   function setupEloSearchNode(): Void {
      if (searchWindow == null) {
         return
      }
      def searchNode = searchWindow.scyContent as EloSearchNode;
      if (searchNode == null) {
         FX.deferAction(setupEloSearchNode);
      } else {
         if (baseElo != null) {
            searchNode.searchBasedOnElo(baseElo);
            isEloBased = true;
         }
         searchNode.savedAction = function(window: ScyWindow): Void {
                     isSaved = true;
                    searchWindow = null;
                    window.closedAction = null;
                    window.allowDrawers = true;
                 }
         searchNode.switchedToEloBasedAction = function(window: ScyWindow): Void {
                    isEloBased = true;
                    searchWindow = null;
                 }
      }
   }

}
