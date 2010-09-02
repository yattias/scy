/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.common.mission.impl.jdom;

import eu.scy.common.mission.MissionRuntimeEloContent;
import eu.scy.common.mission.impl.BasicMissionRuntimeEloContent;
import java.net.URISyntaxException;
import org.jdom.Element;

/**
 *
 * @author SikkenJ
 */
public class MissionRuntimeEloContentXmlUtils {

   static final String missionRuntimeEloName = "missionRuntimeElo";
   static final String missionSpecificationEloUriName = "missionSpecificationEloUri";

   private MissionRuntimeEloContentXmlUtils()
   {
   }

   public static String missionRuntimeToXml(MissionRuntimeEloContent missionSpecification)
   {
      Element root = new Element(missionRuntimeEloName);
      root.addContent(JDomConversionUtils.createElement(missionSpecificationEloUriName, missionSpecification.getMissionSpecificationEloUri()));
      root.addContent(JDomConversionUtils.createElement(MissionSpecificationEloContentXmlUtils.missionMapModelEloUriName, missionSpecification.getMissionMapModelEloUri()));
      root.addContent(JDomConversionUtils.createElement(MissionSpecificationEloContentXmlUtils.eloToolConfigsEloUriName, missionSpecification.getEloToolConfigsEloUri()));
      root.addContent(JDomConversionUtils.createElement(MissionSpecificationEloContentXmlUtils.templateElosEloUriName, missionSpecification.getTemplateElosEloUri()));
      root.addContent(JDomConversionUtils.createElement(MissionSpecificationEloContentXmlUtils.runtimeSettingsEloUriName, missionSpecification.getRuntimeSettingsEloUri()));
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
      return missionRuntimeElo;
   }

}
