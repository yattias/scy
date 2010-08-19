/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tools.mission;

import javax.swing.JFileChooser;
import eu.scy.client.desktop.scydesktop.tools.content.eloImporter.ExampleFileFilter;
import eu.scy.client.desktop.scydesktop.tools.mission.springimport.SpringConfigFileImporter;
import roolo.elo.api.IELO;
import eu.scy.client.desktop.scydesktop.tools.mission.springimport.BasicMissionSpecification;
import eu.scy.client.desktop.scydesktop.tools.mission.springimport.MissionSpecificationXmlUtils;
import java.io.File;

/**
 * @author sikken
 */
public def eloType = "scy/missionspecification";

public class MissionSpecificationEditor extends EloXmlEditor {

   override protected function getEloType(): String {
      eloType
   }

   override protected function doImport(): Void {
      var fileChooser = new JFileChooser();
      fileChooser.setCurrentDirectory(lastUsedDirectory);
      fileChooser.setFileFilter(new ExampleFileFilter("xml", "Spring mission specification"));
      if (JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog(getParentComponent())) {
         //getting the file from the fileChooser
         lastUsedDirectory = fileChooser.getCurrentDirectory();
         doImport(fileChooser.getSelectedFile());
      }
   }

   function doImport(file: File) {
      var name = file.getName();
      var lastPointIndex = name.lastIndexOf(".");
      if (lastPointIndex >= 0) {
         name = name.substring(0, lastPointIndex);
      }

      var springConfigFileImporter = SpringConfigFileImporter {
            file: file.getAbsolutePath()
            repository: repository
         }
      var missionMapModelElo = saveXmlInElo(MissionMapModelEditor.eloType, name, springConfigFileImporter.missionMapXml);
      var eloToolConfigsElo = saveXmlInElo(EloToolConfigurationEditor.eloType, name, springConfigFileImporter.eloToolConfigsXml);
      var missionSpecification = new BasicMissionSpecification();
      missionSpecification.setMissionMapModelEloUri(missionMapModelElo.getUri());
      missionSpecification.setEloToolConfigsEloUri(eloToolConfigsElo.getUri());
      textBox.text = MissionSpecificationXmlUtils.missionSpecificationToXml(missionSpecification)
   }

   function saveXmlInElo(type: String, name: String, xml: String): IELO {
      var elo = eloFactory.createELO();
      elo.getMetadata().getMetadataValueContainer(technicalFormatKey).setValue(type);
      elo.getMetadata().getMetadataValueContainer(titleKey).setValue(name);
      elo.getContent().setXmlString(xml);
      var metadata = repository.addNewELO(elo);
      eloFactory.updateELOWithResult(elo, metadata);
      elo
   }

   override protected function validateXml(xml: String): String {
      var errors = super.validateXml(xml);
      if (errors == null) {
         def missionSpecification = MissionSpecificationXmlUtils.missionSpecificationFromXml(xml);
         if (missionSpecification == null) {
            errors = "The xml is not valid for mission specification";
         }
      }
      errors
   }

}
