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
import eu.scy.common.scyelo.ScyElo;
import java.util.Locale;
import eu.scy.common.mission.MissionSpecificationElo;
import eu.scy.common.mission.MissionSpecificationEloContent;
import java.net.URI;

/**
 * @author sikken
 */
public class MissionSpecificationEditor extends EloXmlEditor {

   var missionMapModelElo: ScyElo;
   var eloToolConfigsElo: ScyElo;
   var templateElosElo: ScyElo;
   var runtimeSettingsElo: ScyElo;
   var pedagogicalPlanSettings: ScyElo;
   var missionSpecification: MissionSpecificationEloContent;

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
      def springConfigFileImporter = SpringConfigFileImporter {
                 file: file.getAbsolutePath()
                 tbi: toolBrokerAPI
              }
      missionId = springConfigFileImporter.missionId;
      if (springConfigFileImporter.missionTitle != null) {
         suggestedTitle = springConfigFileImporter.missionTitle;
      } else {
         suggestedTitle = getTitleFromFile(file);
      }
      language = springConfigFileImporter.language;
      if (elo.getUri() == null) {
         createNewMissionSpecElos(springConfigFileImporter);
      } else {
         overwriteExistingMissionSpecElos(springConfigFileImporter);
      }
      missionSpecification.setMissionMapModelEloUri(missionMapModelElo.getUri());
      missionSpecification.setEloToolConfigsEloUri(eloToolConfigsElo.getUri());
      missionSpecification.setTemplateElosEloUri(templateElosElo.getUri());
      missionSpecification.setRuntimeSettingsEloUri(runtimeSettingsElo.getUri());
      missionSpecification.setMissionDescriptionUri(springConfigFileImporter.missionDescriptionUri);
      missionSpecification.setColorSchemesEloUri(springConfigFileImporter.colorSchemesEloUri);
      missionSpecification.setAgentModelsEloUri(springConfigFileImporter.agentModelsEloUri);
      missionSpecification.setPedagogicalPlanSettingsEloUri(pedagogicalPlanSettings.getUri());
      missionSpecification.setMissionId(springConfigFileImporter.missionId);
      missionSpecification.setXhtmlVersionId(springConfigFileImporter.xhtmlVersionId);
      setContent(MissionSpecificationEloContentXmlUtils.missionSpecificationToXml(missionSpecification), springConfigFileImporter.errors);
   }

   function getTitleFromFile(file: File): String {
      var name = file.getName();
      def lastPointIndex = name.lastIndexOf(".");
      if (lastPointIndex >= 0) {
         name = name.substring(0, lastPointIndex);
      }
      name
   }

   function createNewMissionSpecElos(springConfigFileImporter: SpringConfigFileImporter): Void {
      missionMapModelElo = saveXmlInElo(MissionEloType.MISSION_MAP_MODEL.getType(), springConfigFileImporter.missionMapXml, springConfigFileImporter.language);
      eloToolConfigsElo = saveXmlInElo(MissionEloType.ELO_TOOL_CONFIGURATION.getType(), springConfigFileImporter.eloToolConfigsXml, null);
      templateElosElo = saveXmlInElo(MissionEloType.TEMPLATES_ELOS.getType(), springConfigFileImporter.templateElosXml, springConfigFileImporter.language);
      runtimeSettingsElo = saveXmlInElo(MissionEloType.RUNTIME_SETTINGS.getType(), springConfigFileImporter.runtimeSettingsEloContentXml, springConfigFileImporter.language);
      pedagogicalPlanSettings = saveXmlInElo(MissionEloType.PADAGOGICAL_PLAN_SETTINGS.getType(), "", springConfigFileImporter.language);
      missionSpecification = new BasicMissionSpecificationEloContent();
   }

   function saveXmlInElo(type: String, xml: String, lang: Locale): ScyElo {
      def scyElo = ScyElo.createElo(type, toolBrokerAPI);
      scyElo.getContent().setXmlString(xml);
      setContentLanguage(scyElo.getElo(), lang);
      setTitleMissionIdAndTemplate(scyElo);
      scyElo.saveAsNewElo();
      scyElo
   }

   function setTitleMissionIdAndTemplate(scyElo: ScyElo): Void {
      scyElo.setTitle(suggestedTitle, if (language != null) language else Locale.getDefault());
      scyElo.setTemplate(true);
      scyElo.setMissionId(missionId);
   }

   function overwriteExistingMissionSpecElos(springConfigFileImporter: SpringConfigFileImporter): Void {
      def currentMissionSpecificationElo = new MissionSpecificationElo(elo, toolBrokerAPI);
      missionSpecification = currentMissionSpecificationElo.getTypedContent();
      missionMapModelElo = updateXmlInElo(missionSpecification.getMissionMapModelEloUri(), springConfigFileImporter.missionMapXml, springConfigFileImporter.language);
      eloToolConfigsElo = updateXmlInElo(missionSpecification.getEloToolConfigsEloUri(), springConfigFileImporter.eloToolConfigsXml, null);
      templateElosElo = updateXmlInElo(missionSpecification.getTemplateElosEloUri(), springConfigFileImporter.templateElosXml, springConfigFileImporter.language);
      runtimeSettingsElo = updateXmlInElo(missionSpecification.getRuntimeSettingsEloUri(), springConfigFileImporter.runtimeSettingsEloContentXml, springConfigFileImporter.language);
      pedagogicalPlanSettings = updateXmlInElo(missionSpecification.getPedagogicalPlanSettingsEloUri(), "", springConfigFileImporter.language);
   }

   function updateXmlInElo(uri: URI, xml: String, lang: Locale): ScyElo {
      def scyElo = ScyElo.loadElo(uri, toolBrokerAPI);
      scyElo.getContent().setXmlString(xml);
      setContentLanguage(scyElo.getElo(), lang);
      setTitleMissionIdAndTemplate(scyElo);
      scyElo.updateElo();
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
