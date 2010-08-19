/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tools.mission.springimport;

import eu.scy.client.desktop.scydesktop.utils.jdom.JDomConversionUtils;
import eu.scy.client.desktop.scydesktop.utils.jdom.JDomStringConversion;
import java.net.URI;
import java.net.URISyntaxException;
import org.jdom.Element;

/**
 *
 * @author SikkenJ
 */
public class MissionSpecificationXmlUtils
{

   private static final String missionspecificationName = "missionSpeicification";
   private static final String missionMapModelEloUriName = "missionMapModelEloUri";
   private static final String eloToolConfigsEloUriName = "eloToolConfigsEloUri";

   private MissionSpecificationXmlUtils()
   {
   }

   public static String missionSpecificationToXml(MissionSpecification missionSpecification)
   {
      Element root = new Element(missionspecificationName);
      root.addContent(JDomConversionUtils.createElement(missionMapModelEloUriName, missionSpecification.getMissionMapModelEloUri().toString()));
      root.addContent(JDomConversionUtils.createElement(eloToolConfigsEloUriName, missionSpecification.getEloToolConfigsEloUri().toString()));
      return new JDomStringConversion().xmlToString(root);
   }

   public static MissionSpecification missionSpecificationFromXml(String xml) throws URISyntaxException
   {
      Element root = new JDomStringConversion().stringToXml(xml);
      if (root==null || !missionspecificationName.equals(root.getName())){
         return null;
      }
      BasicMissionSpecification missionSpecification = new BasicMissionSpecification();
      missionSpecification.setMissionMapModelEloUri(new URI(root.getChildTextTrim(missionMapModelEloUriName)));
      missionSpecification.setEloToolConfigsEloUri(new URI(root.getChildTextTrim(eloToolConfigsEloUriName)));
      return missionSpecification;
   }
}
