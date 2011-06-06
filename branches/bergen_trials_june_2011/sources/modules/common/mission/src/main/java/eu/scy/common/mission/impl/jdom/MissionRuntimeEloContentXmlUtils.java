/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.common.mission.impl.jdom;

import java.net.URISyntaxException;

import org.jdom.Element;

import eu.scy.common.mission.MissionRuntimeEloContent;
import eu.scy.common.mission.impl.BasicMissionRuntimeEloContent;

/**
 *
 * @author SikkenJ
 */
public class MissionRuntimeEloContentXmlUtils {

   static final String missionRuntimeEloName = "missionRuntimeElo";
   static final String missionSpecificationEloUriName = "missionSpecificationEloUri";
   static final String ePortfolioEloUriName = "ePortfolioEloUri";

   private MissionRuntimeEloContentXmlUtils()
   {
   }

   public static String missionRuntimeToXml(MissionRuntimeEloContent missionRuntime)
   {
      Element root = new Element(missionRuntimeEloName);
      root.addContent(JDomConversionUtils.createElement(missionSpecificationEloUriName, missionRuntime.getMissionSpecificationEloUri()));
      root.addContent(JDomConversionUtils.createElement(MissionSpecificationEloContentXmlUtils.missionMapModelEloUriName, missionRuntime.getMissionMapModelEloUri()));
      root.addContent(JDomConversionUtils.createElement(MissionSpecificationEloContentXmlUtils.eloToolConfigsEloUriName, missionRuntime.getEloToolConfigsEloUri()));
      root.addContent(JDomConversionUtils.createElement(MissionSpecificationEloContentXmlUtils.templateElosEloUriName, missionRuntime.getTemplateElosEloUri()));
      root.addContent(JDomConversionUtils.createElement(MissionSpecificationEloContentXmlUtils.runtimeSettingsEloUriName, missionRuntime.getRuntimeSettingsEloUri()));
      root.addContent(JDomConversionUtils.createElement(ePortfolioEloUriName, missionRuntime.getEPortfolioEloUri()));
      root.addContent(JDomConversionUtils.createElement(MissionSpecificationEloContentXmlUtils.pedagogicalPlanSettingsEloUriName, missionRuntime.getPedagogicalPlanSettingsEloUri()));
      root.addContent(JDomConversionUtils.createElement(MissionSpecificationEloContentXmlUtils.colorSchemesEloUriName, missionRuntime.getColorSchemesEloUri()));
      return new JDomStringConversion().xmlToString(root);
   }

   public static MissionRuntimeEloContent missionRuntimeFromXml(String xml) throws URISyntaxException
   {
      Element root = new JDomStringConversion().stringToXml(xml);
      if (root==null || !missionRuntimeEloName.equals(root.getName())){
         return null;
      }
      BasicMissionRuntimeEloContent missionRuntimeElo = new BasicMissionRuntimeEloContent();
      missionRuntimeElo.setMissionSpecificationEloUri(JDomConversionUtils.getUriValue(root,missionSpecificationEloUriName));
      missionRuntimeElo.setMissionMapModelEloUri(JDomConversionUtils.getUriValue(root,MissionSpecificationEloContentXmlUtils.missionMapModelEloUriName));
      missionRuntimeElo.setEloToolConfigsEloUri(JDomConversionUtils.getUriValue(root,MissionSpecificationEloContentXmlUtils.eloToolConfigsEloUriName));
      missionRuntimeElo.setTemplateElosEloUri(JDomConversionUtils.getUriValue(root,MissionSpecificationEloContentXmlUtils.templateElosEloUriName));
      missionRuntimeElo.setRuntimeSettingsEloUri(JDomConversionUtils.getUriValue(root,MissionSpecificationEloContentXmlUtils.runtimeSettingsEloUriName));
      missionRuntimeElo.setEPortfolioEloUri(JDomConversionUtils.getUriValue(root,ePortfolioEloUriName));
      missionRuntimeElo.setPedagogicalPlanSettingsEloUri(JDomConversionUtils.getUriValue(root,MissionSpecificationEloContentXmlUtils.pedagogicalPlanSettingsEloUriName));
      missionRuntimeElo.setColorSchemesEloUri(JDomConversionUtils.getUriValue(root,MissionSpecificationEloContentXmlUtils.colorSchemesEloUriName));
      return missionRuntimeElo;
   }

}
