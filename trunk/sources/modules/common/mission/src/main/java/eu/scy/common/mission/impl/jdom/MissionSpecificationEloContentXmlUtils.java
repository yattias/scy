/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.common.mission.impl.jdom;

import eu.scy.common.mission.MissionSpecificationEloContent;
import eu.scy.common.mission.impl.BasicMissionSpecificationEloContent;
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
   static final String templateElosEloUriName = "templateElosEloUri";

   private MissionSpecificationEloContentXmlUtils()
   {
   }

   public static String missionSpecificationToXml(MissionSpecificationEloContent missionSpecification)
   {
      Element root = new Element(missionSpecificationName);
      root.addContent(JDomConversionUtils.createElement(missionMapModelEloUriName, missionSpecification.getMissionMapModelEloUri()));
      root.addContent(JDomConversionUtils.createElement(eloToolConfigsEloUriName, missionSpecification.getEloToolConfigsEloUri()));
      root.addContent(JDomConversionUtils.createElement(templateElosEloUriName, missionSpecification.getTemplateElosEloUri()));
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
      return missionSpecification;
   }
}
