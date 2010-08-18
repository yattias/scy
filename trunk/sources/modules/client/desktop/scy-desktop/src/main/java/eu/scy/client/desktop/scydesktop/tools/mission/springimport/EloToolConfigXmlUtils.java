/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tools.mission.springimport;

import eu.scy.client.desktop.scydesktop.utils.jdom.JDomCoversionUtils;
import eu.scy.client.desktop.scydesktop.utils.jdom.JDomStringConversion;
import java.util.ArrayList;
import java.util.List;
import org.jdom.Element;

/**
 *
 * @author SikkenJ
 */
public class EloToolConfigXmlUtils
{

   private static final String eloToolConfigsName = "eloToolConfigs";
   private static final String eloToolConfigName = "eloToolConfig";
   private static final String eloTypeName = "eloType";
   private static final String contentCreatorIdName = "contentCreatorId";
   private static final String topDrawerCreatorIdName = "topDrawerCreatorId";
   private static final String rightDrawerCreatorIdName = "rightDrawerCreatorId";
   private static final String bottomDrawerCreatorIdName = "bottomDrawerCreatorId";
   private static final String leftDrawerCreatorIdName = "leftDrawerCreatorId";
   private static final String isContentCollaborationName = "isContentCollaboration";
   private static final String isTopDrawerCollaborationName = "isTopDrawerCollaboration";
   private static final String isRightDrawerCollaborationName = "isRightDrawerCollaboration";
   private static final String isBottomDrawerCollaborationName = "isBottomDrawerCollaboration";
   private static final String isLeftDrawerCollaborationName = "isLeftDrawerCollaboration";
   private static final String logicalNamesName = "logicalNames";
   private static final String logicalNameName = "logicalName";
   private static final String functionalNamesName = "functionalNames";
   private static final String functionalNameName = "functionalName";
   private JDomStringConversion jdomStringConversion = new JDomStringConversion();

   public String eloToolConfigsToXml(List<EloToolConfig> eloToolConfigs)
   {
      Element root = new Element(eloToolConfigsName);
      for (EloToolConfig eloToolConfig : eloToolConfigs)
      {
         root.addContent(createEloToolConfigXml(eloToolConfig));
      }
      return jdomStringConversion.xmlToString(root);
   }

   private Element createEloToolConfigXml(EloToolConfig eloToolConfig)
   {
      Element element = new Element(eloToolConfigName);
      element.addContent(JDomCoversionUtils.createElement(eloTypeName, eloToolConfig.getEloType()));
      element.addContent(JDomCoversionUtils.createElement(contentCreatorIdName, eloToolConfig.getContentCreatorId()));
      element.addContent(JDomCoversionUtils.createElement(topDrawerCreatorIdName, eloToolConfig.getTopDrawerCreatorId()));
      element.addContent(JDomCoversionUtils.createElement(rightDrawerCreatorIdName, eloToolConfig.getRightDrawerCreatorId()));
      element.addContent(JDomCoversionUtils.createElement(bottomDrawerCreatorIdName, eloToolConfig.getBottomDrawerCreatorId()));
      element.addContent(JDomCoversionUtils.createElement(leftDrawerCreatorIdName, eloToolConfig.getLeftDrawerCreatorId()));
      element.addContent(JDomCoversionUtils.createElement(isContentCollaborationName, eloToolConfig.isContentCollaboration()));
      element.addContent(JDomCoversionUtils.createElement(isTopDrawerCollaborationName, eloToolConfig.isTopDrawerCollaboration()));
      element.addContent(JDomCoversionUtils.createElement(isRightDrawerCollaborationName, eloToolConfig.isRightDrawerCollaboration()));
      element.addContent(JDomCoversionUtils.createElement(isBottomDrawerCollaborationName, eloToolConfig.isBottomDrawerCollaboration()));
      element.addContent(JDomCoversionUtils.createElement(isLeftDrawerCollaborationName, eloToolConfig.isLeftDrawerCollaboration()));
      element.addContent(JDomCoversionUtils.createElement(logicalNamesName, logicalNameName, eloToolConfig.getLogicalTypeNames()));
      element.addContent(JDomCoversionUtils.createElement(functionalNamesName, functionalNameName, eloToolConfig.getLogicalTypeNames()));
      return element;
   }

   public List<EloToolConfig> eloToolConfigsFromXml(String xml){
      List<EloToolConfig> eloToolConfigs = new ArrayList<EloToolConfig>();
      Element root = jdomStringConversion.stringToXml(xml);
      if (root==null || !eloToolConfigsName.equals(root.getName())){
         return null;
      }
      @SuppressWarnings("unchecked")
      List<Element> eloToolConfigChildren = root.getChildren(eloToolConfigName);
      if (eloToolConfigChildren!=null){
         for (Element eloToolConfigChild : eloToolConfigChildren){
            eloToolConfigs.add(createEloToolConfig(eloToolConfigChild));
         }
      }
      return eloToolConfigs;
   }

   private EloToolConfig createEloToolConfig(Element eloToolConfigChild)
   {
      BasicEloToolConfig eloToolConfig = new BasicEloToolConfig();
      eloToolConfig.setEloType(eloToolConfigChild.getChildTextTrim(eloTypeName));
      eloToolConfig.setContentCreatorId(eloToolConfigChild.getChildTextTrim(contentCreatorIdName));
      eloToolConfig.setTopDrawerCreatorId(eloToolConfigChild.getChildTextTrim(topDrawerCreatorIdName));
      eloToolConfig.setRightDrawerCreatorId(eloToolConfigChild.getChildTextTrim(rightDrawerCreatorIdName));
      eloToolConfig.setBottomDrawerCreatorId(eloToolConfigChild.getChildTextTrim(bottomDrawerCreatorIdName));
      eloToolConfig.setLeftDrawerCreatorId(eloToolConfigChild.getChildTextTrim(leftDrawerCreatorIdName));
      eloToolConfig.setContentCollaboration(JDomCoversionUtils.getBooleanValue(eloToolConfigChild,isContentCollaborationName));
      eloToolConfig.setTopDrawerCollaboration(JDomCoversionUtils.getBooleanValue(eloToolConfigChild,isTopDrawerCollaborationName));
      eloToolConfig.setRightDrawerCollaboration(JDomCoversionUtils.getBooleanValue(eloToolConfigChild,isRightDrawerCollaborationName));
      eloToolConfig.setBottomDrawerCollaboration(JDomCoversionUtils.getBooleanValue(eloToolConfigChild,isBottomDrawerCollaborationName));
      eloToolConfig.setLeftDrawerCollaboration(JDomCoversionUtils.getBooleanValue(eloToolConfigChild,isLeftDrawerCollaborationName));
      eloToolConfig.setLogicalTypeNames(JDomCoversionUtils.getStringListValue(eloToolConfigChild,logicalNamesName,logicalNameName));
      eloToolConfig.setFunctionalTypeNames(JDomCoversionUtils.getStringListValue(eloToolConfigChild,functionalNamesName, functionalNameName));
      return eloToolConfig;
   }
}
