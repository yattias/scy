/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.common.mission.impl.jdom;

import eu.scy.common.mission.MissionRuntimeEloContent;
import eu.scy.common.mission.impl.BasicMissionRuntimeEloContent;
import java.net.URI;
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
      root.addContent(JDomConversionUtils.createElement(missionSpecificationEloUriName, missionSpecification.getMissionSpecificationEloUri().toString()));
      root.addContent(JDomConversionUtils.createElement(MissionSpecificationEloContentXmlUtils.missionMapModelEloUriName, missionSpecification.getMissionMapModelEloUri().toString()));
      root.addContent(JDomConversionUtils.createElement(MissionSpecificationEloContentXmlUtils.eloToolConfigsEloUriName, missionSpecification.getEloToolConfigsEloUri().toString()));
      return new JDomStringConversion().xmlToString(root);
   }

   public static MissionRuntimeEloContent missionRuntimeFromXml(String xml) throws URISyntaxException
   {
      Element root = new JDomStringConversion().stringToXml(xml);
      if (root==null || !missionRuntimeEloName.equals(root.getName())){
         return null;
      }
      BasicMissionRuntimeEloContent missionRuntimeElo = new BasicMissionRuntimeEloContent();
      missionRuntimeElo.setMissionSpecificationEloUri(new URI(root.getChildTextTrim(missionSpecificationEloUriName)));
      missionRuntimeElo.setMissionMapModelEloUri(new URI(root.getChildTextTrim(MissionSpecificationEloContentXmlUtils.missionMapModelEloUriName)));
      missionRuntimeElo.setEloToolConfigsEloUri(new URI(root.getChildTextTrim(MissionSpecificationEloContentXmlUtils.eloToolConfigsEloUriName)));
      return missionRuntimeElo;
   }

}
