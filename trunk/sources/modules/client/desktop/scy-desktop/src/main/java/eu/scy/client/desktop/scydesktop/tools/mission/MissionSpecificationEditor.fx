/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tools.mission;

import javax.swing.JFileChooser;
import eu.scy.client.desktop.scydesktop.tools.content.eloImporter.ExampleFileFilter;
import eu.scy.client.desktop.scydesktop.tools.mission.springimport.SpringConfigFileImporter;
import eu.scy.common.mission.impl.BasicMissionSpecificationEloContent;
import eu.scy.common.mission.impl.jdom.MissionSpecificationEloContentXmlUtils;
import java.io.File;
import eu.scy.common.mission.MissionEloType;
import eu.scy.common.mission.RuntimeSettingsElo;
import eu.scy.common.scyelo.ScyElo;
import java.util.Locale;
import java.util.ArrayList;
import roolo.elo.api.IELO;

/**
 * @author sikken
 */
public class MissionSpecificationEditor extends EloXmlEditor {

   override protected function getEloType(): String {
      MissionEloType.MISSION_SPECIFICATIOM.getType()
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
      def lastPointIndex = name.lastIndexOf(".");
      if (lastPointIndex >= 0) {
         name = name.substring(0, lastPointIndex);
      }

      def springConfigFileImporter = SpringConfigFileImporter {
                 file: file.getAbsolutePath()
                 tbi: toolBrokerAPI
              }
      def missionMapModelElo = saveXmlInElo(MissionEloType.MISSION_MAP_MODEL.getType(), name, springConfigFileImporter.missionMapXml, springConfigFileImporter.language);
      def eloToolConfigsElo = saveXmlInElo(MissionEloType.ELO_TOOL_CONFIGURATION.getType(), name, springConfigFileImporter.eloToolConfigsXml, null);
      def templateElosElo = saveXmlInElo(MissionEloType.TEMPLATES_ELOS.getType(), name, springConfigFileImporter.templateElosXml, springConfigFileImporter.language);
      def runtimeSettingsElo = RuntimeSettingsElo.createElo(toolBrokerAPI);
      setContentLanguage(runtimeSettingsElo.getElo(), springConfigFileImporter.language);
      runtimeSettingsElo.setTitle(name);
      runtimeSettingsElo.saveAsNewElo();
      def missionSpecification = new BasicMissionSpecificationEloContent();
      missionSpecification.setMissionMapModelEloUri(missionMapModelElo.getUri());
      missionSpecification.setEloToolConfigsEloUri(eloToolConfigsElo.getUri());
      missionSpecification.setTemplateElosEloUri(templateElosElo.getUri());
      missionSpecification.setRuntimeSettingsEloUri(runtimeSettingsElo.getUri());
      missionSpecification.setMissionDescriptionUri(springConfigFileImporter.missionDescriptionUri);
      missionSpecification.setColorSchemesEloUri(springConfigFileImporter.colorSchemesEloUri);
      missionSpecification.setAgentModelsEloUri(springConfigFileImporter.agentModelsEloUri);
      def pedagogicalPlanSettings = ScyElo.createElo(MissionEloType.PADAGOGICAL_PLAN_SETTINGS.getType(),
              toolBrokerAPI);
      pedagogicalPlanSettings.setTitle(missionMapModelElo.getTitle());
      pedagogicalPlanSettings.saveAsNewElo();
      missionSpecification.setPedagogicalPlanSettingsEloUri(pedagogicalPlanSettings.getUri());
      missionSpecification.setMissionId(springConfigFileImporter.missionId);
      missionSpecification.setXhtmlVersionId(springConfigFileImporter.xhtmlVersionId);
      setContent(MissionSpecificationEloContentXmlUtils.missionSpecificationToXml(missionSpecification), springConfigFileImporter.errors);
      language = springConfigFileImporter.language;
      suggestedTitle = springConfigFileImporter.missionTitle;
   }

   function saveXmlInElo(type: String, name: String, xml: String, lang: Locale): ScyElo {
      def scyElo = ScyElo.createElo(type, toolBrokerAPI);
      scyElo.setTitle(name);
      scyElo.getContent().setXmlString(xml);
      setContentLanguage(scyElo.getElo(), lang);
      scyElo.saveAsNewElo();
      scyElo
   }

   override protected function validateXml(xml: String): String {
      var errors = super.validateXml(xml);
      if (errors == null) {
         def missionSpecification = MissionSpecificationEloContentXmlUtils.missionSpecificationFromXml(xml);
         if (missionSpecification == null) {
            errors = "The xml is not valid for mission specification";
         }
      }
      errors
   }

}
