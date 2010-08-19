/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tools.mission.springimport;

import eu.scy.client.desktop.scydesktop.utils.jdom.JDomConversionUtils;
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
   private static final String eloSystemRoleName = "eloSystemRole";
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
   private static final String eloLogicalRolesName = "eloLogicalRoles";
   private static final String eloLogicalRoleName = "eloLogicalRole";
   private static final String eloFunctionalRolesName = "eloFunctionalRoles";
   private static final String eloFunctionalRoleName = "eloFunctionalRole";

   private EloToolConfigXmlUtils()
   {
   }

   public static String eloToolConfigsToXml(List<EloToolConfig> eloToolConfigs)
   {
      Element root = new Element(eloToolConfigsName);
      for (EloToolConfig eloToolConfig : eloToolConfigs)
      {
         root.addContent(createEloToolConfigXml(eloToolConfig));
      }
      return new JDomStringConversion().xmlToString(root);
   }

   private static Element createEloToolConfigXml(EloToolConfig eloToolConfig)
   {
      Element element = new Element(eloToolConfigName);
      element.addContent(JDomConversionUtils.createElement(eloTypeName, eloToolConfig.getEloType()));
      element.addContent(JDomConversionUtils.createElement(eloSystemRoleName, eloToolConfig.getEloSystemRole().toString()));
      element.addContent(JDomConversionUtils.createElement(contentCreatorIdName, eloToolConfig.getContentCreatorId()));
      element.addContent(JDomConversionUtils.createElement(topDrawerCreatorIdName, eloToolConfig.getTopDrawerCreatorId()));
      element.addContent(JDomConversionUtils.createElement(rightDrawerCreatorIdName, eloToolConfig.getRightDrawerCreatorId()));
      element.addContent(JDomConversionUtils.createElement(bottomDrawerCreatorIdName, eloToolConfig.getBottomDrawerCreatorId()));
      element.addContent(JDomConversionUtils.createElement(leftDrawerCreatorIdName, eloToolConfig.getLeftDrawerCreatorId()));
      element.addContent(JDomConversionUtils.createElement(isContentCollaborationName, eloToolConfig.isContentCollaboration()));
      element.addContent(JDomConversionUtils.createElement(isTopDrawerCollaborationName, eloToolConfig.isTopDrawerCollaboration()));
      element.addContent(JDomConversionUtils.createElement(isRightDrawerCollaborationName, eloToolConfig.isRightDrawerCollaboration()));
      element.addContent(JDomConversionUtils.createElement(isBottomDrawerCollaborationName, eloToolConfig.isBottomDrawerCollaboration()));
      element.addContent(JDomConversionUtils.createElement(isLeftDrawerCollaborationName, eloToolConfig.isLeftDrawerCollaboration()));
      element.addContent(JDomConversionUtils.createElement(eloLogicalRolesName, eloLogicalRoleName, eloToolConfig.getEloLogicalRoles()));
      element.addContent(JDomConversionUtils.createElement(eloFunctionalRolesName, eloFunctionalRoleName, eloToolConfig.getEloFunctionalRoles()));
      return element;
   }

   public static List<EloToolConfig> eloToolConfigsFromXml(String xml){
      List<EloToolConfig> eloToolConfigs = new ArrayList<EloToolConfig>();
      Element root = new JDomStringConversion().stringToXml(xml);
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

   private static EloToolConfig createEloToolConfig(Element eloToolConfigChild)
   {
      BasicEloToolConfig eloToolConfig = new BasicEloToolConfig();
      eloToolConfig.setEloType(eloToolConfigChild.getChildTextTrim(eloTypeName));
      eloToolConfig.setEloSystemRole(EloSystemRole.valueOf(eloToolConfigChild.getChildTextTrim(eloSystemRoleName)));
      eloToolConfig.setContentCreatorId(eloToolConfigChild.getChildTextTrim(contentCreatorIdName));
      eloToolConfig.setTopDrawerCreatorId(eloToolConfigChild.getChildTextTrim(topDrawerCreatorIdName));
      eloToolConfig.setRightDrawerCreatorId(eloToolConfigChild.getChildTextTrim(rightDrawerCreatorIdName));
      eloToolConfig.setBottomDrawerCreatorId(eloToolConfigChild.getChildTextTrim(bottomDrawerCreatorIdName));
      eloToolConfig.setLeftDrawerCreatorId(eloToolConfigChild.getChildTextTrim(leftDrawerCreatorIdName));
      eloToolConfig.setContentCollaboration(JDomConversionUtils.getBooleanValue(eloToolConfigChild,isContentCollaborationName));
      eloToolConfig.setTopDrawerCollaboration(JDomConversionUtils.getBooleanValue(eloToolConfigChild,isTopDrawerCollaborationName));
      eloToolConfig.setRightDrawerCollaboration(JDomConversionUtils.getBooleanValue(eloToolConfigChild,isRightDrawerCollaborationName));
      eloToolConfig.setBottomDrawerCollaboration(JDomConversionUtils.getBooleanValue(eloToolConfigChild,isBottomDrawerCollaborationName));
      eloToolConfig.setLeftDrawerCollaboration(JDomConversionUtils.getBooleanValue(eloToolConfigChild,isLeftDrawerCollaborationName));
      eloToolConfig.setEloLogicalRoles(JDomConversionUtils.getEnumListValue(EloLogicalRole.class ,eloToolConfigChild,eloLogicalRolesName,eloLogicalRoleName));
      eloToolConfig.setEloFunctionalRoles(JDomConversionUtils.getEnumListValue(EloFunctionalRole.class ,eloToolConfigChild,eloFunctionalRolesName, eloFunctionalRoleName));
      return eloToolConfig;
   }
}
