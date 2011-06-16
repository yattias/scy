/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.common.mission.impl.jdom;

import java.net.URISyntaxException;

import org.jdom.Element;

import eu.scy.common.mission.MissionSpecificationEloContent;
import eu.scy.common.mission.impl.BasicMissionSpecificationEloContent;

/**
 *
 * @author SikkenJ
 */
public class MissionSpecificationEloContentXmlUtils
{

   static final String missionSpecificationName = "missionSpeicification";
   static final String missionMapModelEloUriName = "missionMapModelEloUri";
   static final String eloToolConfigsEloUriName = "eloToolConfigsEloUri";
   static final String templateElosEloUriName = "templateElosEloUri";
   static final String runtimeSettingsEloUriName = "runtimeSettingsEloUri";
   static final String agentModelsEloUriName = "agentModelsEloUri";
   static final String pedagogicalPlanSettingsEloUriName = "pedagogicalPlanSettingsEloUri";
   static final String colorSchemesEloUriName = "colorSchemesEloUri";
   static final String missionDescriptionUriName = "missionDescriptionUri";
   static final String missionIdName = "missionId";
   static final String xhtmlVersionIdName = "xhtmlVersionId";

   private MissionSpecificationEloContentXmlUtils()
   {
   }

   public static String missionSpecificationToXml(MissionSpecificationEloContent missionSpecification)
   {
      Element root = new Element(missionSpecificationName);
      root.addContent(JDomConversionUtils.createElement(missionMapModelEloUriName, missionSpecification.getMissionMapModelEloUri()));
      root.addContent(JDomConversionUtils.createElement(eloToolConfigsEloUriName, missionSpecification.getEloToolConfigsEloUri()));
      root.addContent(JDomConversionUtils.createElement(templateElosEloUriName, missionSpecification.getTemplateElosEloUri()));
      root.addContent(JDomConversionUtils.createElement(runtimeSettingsEloUriName, missionSpecification.getRuntimeSettingsEloUri()));
      root.addContent(JDomConversionUtils.createElement(agentModelsEloUriName, missionSpecification.getAgentModelsEloUri()));
      root.addContent(JDomConversionUtils.createElement(pedagogicalPlanSettingsEloUriName, missionSpecification.getPedagogicalPlanSettingsEloUri()));
      root.addContent(JDomConversionUtils.createElement(colorSchemesEloUriName, missionSpecification.getColorSchemesEloUri()));
      root.addContent(JDomConversionUtils.createElement(missionDescriptionUriName, missionSpecification.getMissionDescriptionUri()));
      root.addContent(JDomConversionUtils.createElement(missionIdName, missionSpecification.getMissionId()));
      root.addContent(JDomConversionUtils.createElement(xhtmlVersionIdName, missionSpecification.getXhtmlVersionId()));
      return new JDomStringConversion().xmlToString(root);
   }

   public static MissionSpecificationEloContent missionSpecificationFromXml(String xml) throws URISyntaxException
   {
      Element root = new JDomStringConversion().stringToXml(xml);
      if (root==null || !missionSpecificationName.equals(root.getName())){
         return null;
      }
      BasicMissionSpecificationEloContent missionSpecification = new BasicMissionSpecificationEloContent();
      missionSpecification.setMissionMapModelEloUri(JDomConversionUtils.getUriValue(root, missionMapModelEloUriName));
      missionSpecification.setEloToolConfigsEloUri(JDomConversionUtils.getUriValue(root, eloToolConfigsEloUriName));
      missionSpecification.setTemplateElosEloUri(JDomConversionUtils.getUriValue(root, templateElosEloUriName));
      missionSpecification.setRuntimeSettingsEloUri(JDomConversionUtils.getUriValue(root, runtimeSettingsEloUriName));
      missionSpecification.setAgentModelsEloUri(JDomConversionUtils.getUriValue(root, agentModelsEloUriName));
      missionSpecification.setPedagogicalPlanSettingsEloUri(JDomConversionUtils.getUriValue(root, pedagogicalPlanSettingsEloUriName));
      missionSpecification.setColorSchemesEloUri(JDomConversionUtils.getUriValue(root, colorSchemesEloUriName));
      missionSpecification.setMissionDescriptionUri(JDomConversionUtils.getUriValue(root, missionDescriptionUriName));
      missionSpecification.setMissionId(root.getChildTextTrim(missionIdName));
      missionSpecification.setXhtmlVersionId(root.getChildTextTrim(xhtmlVersionIdName));
      return missionSpecification;
   }
}
