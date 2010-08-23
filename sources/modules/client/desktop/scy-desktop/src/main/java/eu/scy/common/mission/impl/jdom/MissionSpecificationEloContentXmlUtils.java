/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.common.mission.impl.jdom;

import eu.scy.common.mission.MissionSpecificationEloContent;
import eu.scy.common.mission.impl.BasicMissionSpecificationEloContent;
import java.net.URI;
import java.net.URISyntaxException;
import org.jdom.Element;

/**
 *
 * @author SikkenJ
 */
public class MissionSpecificationEloContentXmlUtils
{

   static final String missionSpecificationName = "missionSpeicification";
   static final String missionMapModelEloUriName = "missionMapModelEloUri";
   static final String eloToolConfigsEloUriName = "eloToolConfigsEloUri";

   private MissionSpecificationEloContentXmlUtils()
   {
   }

   public static String missionSpecificationToXml(MissionSpecificationEloContent missionSpecification)
   {
      Element root = new Element(missionSpecificationName);
      root.addContent(JDomConversionUtils.createElement(missionMapModelEloUriName, missionSpecification.getMissionMapModelEloUri().toString()));
      root.addContent(JDomConversionUtils.createElement(eloToolConfigsEloUriName, missionSpecification.getEloToolConfigsEloUri().toString()));
      return new JDomStringConversion().xmlToString(root);
   }

   public static MissionSpecificationEloContent missionSpecificationFromXml(String xml) throws URISyntaxException
   {
      Element root = new JDomStringConversion().stringToXml(xml);
      if (root==null || !missionSpecificationName.equals(root.getName())){
         return null;
      }
      BasicMissionSpecificationEloContent missionSpecification = new BasicMissionSpecificationEloContent();
      missionSpecification.setMissionMapModelEloUri(new URI(root.getChildTextTrim(missionMapModelEloUriName)));
      missionSpecification.setEloToolConfigsEloUri(new URI(root.getChildTextTrim(eloToolConfigsEloUriName)));
      return missionSpecification;
   }
}
