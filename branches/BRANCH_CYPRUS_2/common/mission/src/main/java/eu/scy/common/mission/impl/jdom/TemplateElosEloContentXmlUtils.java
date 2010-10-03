/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.common.mission.impl.jdom;

import java.net.URI;
import java.net.URISyntaxException;

import org.jdom.Element;

import eu.scy.common.mission.TemplateElosEloContent;
import eu.scy.common.mission.impl.BasicTemplateElosEloContent;

/**
 *
 * @author SikkenJ
 */
public class TemplateElosEloContentXmlUtils {
   static final String templateElosEloUrisName = "templateElosEloUris";
   static final String templateElosEloUriName = "templateElosEloUri";

   private TemplateElosEloContentXmlUtils()
   {
   }
   public static String templateElosEloContentToXml(TemplateElosEloContent templateElosEloContent)
   {
      Element root = new Element(templateElosEloUrisName);
      if (templateElosEloContent.getTemplateEloUris()!=null){
      for (URI uri : templateElosEloContent.getTemplateEloUris()){
         root.addContent(JDomConversionUtils.createElement(templateElosEloUriName, uri));
      }
      }
      return new JDomStringConversion().xmlToString(root);
   }

   public static TemplateElosEloContent templateElosEloContentFromXml(String xml) throws URISyntaxException
   {
      Element root = new JDomStringConversion().stringToXml(xml);
      if (root==null || !templateElosEloUrisName.equals(root.getName())){
         return null;
      }
      TemplateElosEloContent templateElosEloContent = new BasicTemplateElosEloContent();
      templateElosEloContent.setTemplateEloUris(JDomConversionUtils.getUriListValue(root, templateElosEloUriName));
      return templateElosEloContent;
   }


}
