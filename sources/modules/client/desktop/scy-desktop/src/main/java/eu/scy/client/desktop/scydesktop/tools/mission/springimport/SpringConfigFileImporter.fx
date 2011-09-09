/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tools.mission.springimport;

import java.lang.IllegalArgumentException;
import org.apache.log4j.Logger;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import eu.scy.common.mission.impl.jdom.EloToolConfigsEloContentXmlUtils;
import eu.scy.common.mission.impl.BasicEloToolConfigsEloContent;
import eu.scy.common.mission.impl.BasicTemplateElosEloContent;
import eu.scy.common.mission.impl.jdom.TemplateElosEloContentXmlUtils;
import eu.scy.common.mission.impl.jdom.MissionModelEloContentXmlUtils;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import java.lang.StringBuilder;
import java.net.URI;
import java.util.Locale;
import eu.scy.common.mission.impl.BasicRuntimeSettingsEloContent;
import eu.scy.common.mission.impl.jdom.RuntimeSettingsEloContentXmlUtils;

/**
 * @author SikkenJ
 */
// place your code here
public class SpringConfigFileImporter {

   def logger = Logger.getLogger(this.getClass());
   public-init var file: String;
   public-init var tbi: ToolBrokerAPI;
   public-read var missionDescriptionUri: URI;
   public-read var colorSchemesEloUri: URI;
   public-read var agentModelsEloUri: URI;
   public-read var missionMapXml: String;
   public-read var eloToolConfigsXml: String;
   public-read var templateElosXml: String;
   public-read var runtimeSettingsEloContentXml: String;
   public-read var missionId: String;
   public-read var missionTitle: String;
   public-read var xhtmlVersionId: String;
   public-read var language: Locale;
   public-read var errors: String;
   var missionConfigInput: MissionConfigInput;
   def missionConfigInputBeanName = "missionConfigInput";

   init {
      missionConfigInput = readSpringConfig();
      missionDescriptionUri = missionConfigInput.getMissionDescriptionUri();
      colorSchemesEloUri = missionConfigInput.getColorSchemesEloUri();
      agentModelsEloUri = missionConfigInput.getAgentModelsEloUri();
      missionId = missionConfigInput.getMissionId();
      missionTitle = missionConfigInput.getMissionTitle();
      xhtmlVersionId = missionConfigInput.getXhtmlVersionId();
      language = missionConfigInput.getLanguage();
      def missionModel = missionConfigInput.getMissionModelEloContent();
      missionMapXml = MissionModelEloContentXmlUtils.missionModelToXml(missionModel);
      def eloToolConfigsEloContent = new BasicEloToolConfigsEloContent();
      eloToolConfigsEloContent.setEloToolConfigs(missionConfigInput.getEloToolConfigs());
      eloToolConfigsXml = EloToolConfigsEloContentXmlUtils.eloToolConfigsToXml(eloToolConfigsEloContent);
      def templateElosEloContent = new BasicTemplateElosEloContent();
      templateElosEloContent.setTemplateEloUris(missionConfigInput.getTemplateEloUris());
      templateElosXml = TemplateElosEloContentXmlUtils.templateElosEloContentToXml(templateElosEloContent);
      def runtimeSettingsEloContent = new BasicRuntimeSettingsEloContent();
      runtimeSettingsEloContent.addSettings(missionConfigInput.getRuntimeSettings());
      runtimeSettingsEloContentXml = RuntimeSettingsEloContentXmlUtils.runtimeSettingsToXml(runtimeSettingsEloContent);
      def builder = new StringBuilder();
      if (missionDescriptionUri==null){
         builder.append("Mission description uri is not defined\n");
      }
      for (error in missionConfigInput.getErrors()){
         builder.append("{error}\n");
      }
      errors = builder.toString();
   }

   function readSpringConfig(): MissionConfigInput {
      def context = new FileSystemXmlApplicationContext(file);
      if (context == null) {
         throw new IllegalArgumentException("failed to load context from file system: {file}");
      }
      var basicMissionConfigInput = context.getBean(missionConfigInputBeanName) as BasicMissionConfigInput;
      basicMissionConfigInput.parseEloConfigs(tbi);
      basicMissionConfigInput;
   }

}
