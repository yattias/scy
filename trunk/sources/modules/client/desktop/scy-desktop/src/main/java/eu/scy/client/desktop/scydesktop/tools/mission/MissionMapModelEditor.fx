/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tools.mission;

import java.lang.String;
import eu.scy.client.desktop.scydesktop.tools.content.eloImporter.ExampleFileFilter;
import eu.scy.client.desktop.scydesktop.tools.mission.springimport.SpringConfigFileImporter;
import javax.swing.JFileChooser;
import eu.scy.client.desktop.scydesktop.tools.corner.missionmap.MissionModelXml;
import eu.scy.client.desktop.scydesktop.EloType;

/**
 * @author SikkenJ
 */

public class MissionMapModelEditor extends EloXmlEditor {

   override protected function getEloType(): String {
      EloType.MISSION_MAP_MODEL.getType()
   }

   override protected function doImport(): Void {
      var fileChooser = new JFileChooser();
      fileChooser.setCurrentDirectory(lastUsedDirectory);
      fileChooser.setFileFilter(new ExampleFileFilter("xml", "Spring mission specification"));
      if (JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog(getParentComponent())) {
         //getting the file from the fileChooser
         lastUsedDirectory = fileChooser.getCurrentDirectory();
         var springConfigFileImporter = SpringConfigFileImporter {
               file: fileChooser.getSelectedFile().getAbsolutePath()
               repository: repository
            }
         textBox.text = springConfigFileImporter.missionMapXml;
      }
   }

   override protected function validateXml(xml: String): String {
      var errors = super.validateXml(xml);
      if (errors == null) {
         def missionModel = MissionModelXml.convertToMissionModel(xml);
         if (missionModel == null) {
            errors = "The xml is not valid for mission map model";
         }
      }
      errors
   }
}
