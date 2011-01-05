package eu.scy.common.mission.impl.jdom;

import java.net.URISyntaxException;

import org.jdom.Element;

import eu.scy.common.mission.EPortfolioEloContent;
import eu.scy.common.mission.impl.BasicEPortfolioEloContent;

public class EPortfolioEloContentXmlUtils
{
   static final String templateElosEloUrisName = "templateElosEloUris";
   static final String templateElosEloUriName = "templateElosEloUri";

   private EPortfolioEloContentXmlUtils()
   {
   }
   public static String ePortfolioEloContentToXml(EPortfolioEloContent templateElosEloContent)
   {
      Element root = new Element(templateElosEloUrisName);
//      if (templateElosEloContent.getTemplateEloUris()!=null){
//      for (URI uri : templateElosEloContent.getTemplateEloUris()){
//         root.addContent(JDomConversionUtils.createElement(templateElosEloUriName, uri));
//      }
//      }
      return new JDomStringConversion().xmlToString(root);
   }

   public static EPortfolioEloContent ePortfolioEloContentFromXml(String xml) throws URISyntaxException
   {
      Element root = new JDomStringConversion().stringToXml(xml);
      if (root==null || !templateElosEloUrisName.equals(root.getName())){
         return null;
      }
      EPortfolioEloContent ePortfolioEloContent = new BasicEPortfolioEloContent();
//      ePortfolioEloContent.setTemplateEloUris(JDomConversionUtils.getUriListValue(root, templateElosEloUriName));
      return ePortfolioEloContent;
   }



}
