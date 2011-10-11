/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.corners.elomanagement;
import javafx.scene.Node;
import javafx.scene.control.Label;
import eu.scy.client.desktop.scydesktop.tools.corner.missionmap.LastModifiedDisplay;

/**
 * @author SikkenJ
 */

public class ArchivedEloListCellNode extends AbstractArchivedEloListCellNode {

   def archivedDateDisplay = Label {};
   def removedWord = ##"Removed";

   protected override function getDetailsList(): Node[] {
      var detailsList = super.getDetailsList();
      archivedDateDisplay.text = "{removedWord} {LastModifiedDisplay.getLastModifiedDisplay(archivedElo.getArchievedMillis())}";
      insert archivedDateDisplay into detailsList;
      detailsList
   }

}
