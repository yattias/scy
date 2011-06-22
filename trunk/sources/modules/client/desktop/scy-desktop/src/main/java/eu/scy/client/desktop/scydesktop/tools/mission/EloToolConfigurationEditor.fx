/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tools.mission;

import eu.scy.client.desktop.scydesktop.tools.content.eloImporter.ExampleFileFilter;
import eu.scy.client.desktop.scydesktop.tools.mission.springimport.SpringConfigFileImporter;
import javax.swing.JFileChooser;
import java.lang.String;
import eu.scy.common.mission.impl.jdom.EloToolConfigsEloContentXmlUtils;
import eu.scy.common.mission.MissionEloType;

/**
 * @author SikkenJ
 */

public class EloToolConfigurationEditor extends EloXmlEditor {

   override protected function getEloType(): String {
      MissionEloType.ELO_TOOL_CONFIGURATION.getType()
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
               tbi: toolBrokerAPI
            }
         setContent(springConfigFileImporter.eloToolConfigsXml,springConfigFileImporter.errors);
         language = springConfigFileImporter.language;
         suggestedTitle = springConfigFileImporter.missionTitle;
      }
   }

   override protected function validateXml(xml: String): String {
      var errors = super.validateXml(xml);
      if (errors == null) {
         def eloConfigs = EloToolConfigsEloContentXmlUtils.eloToolConfigsFromXml(xml);
         if (eloConfigs == null) {
            errors = "The xml is not valid for elo tool configurations";
         }
      }
      errors
   }

}
