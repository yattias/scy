/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.common.mission.impl.jdom;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;

import eu.scy.common.mission.EloSystemRole;
import eu.scy.common.mission.EloToolConfig;
import eu.scy.common.mission.EloToolConfigsEloContent;
import eu.scy.common.mission.impl.BasicEloToolConfig;
import eu.scy.common.mission.impl.BasicEloToolConfigsEloContent;
import eu.scy.common.scyelo.EloFunctionalRole;
import eu.scy.common.scyelo.EloLogicalRole;

/**
 *
 * @author SikkenJ
 */
public class EloToolConfigsEloContentXmlUtils
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
   private static final String isContentStaticName = "isContentStatic";
   private static final String eloLogicalRolesName = "eloLogicalRoles";
   private static final String eloLogicalRoleName = "eloLogicalRole";
   private static final String eloFunctionalRolesName = "eloFunctionalRoles";
   private static final String eloFunctionalRoleName = "eloFunctionalRole";

   private EloToolConfigsEloContentXmlUtils()
   {
   }

   public static String eloToolConfigsToXml(EloToolConfigsEloContent eloToolConfigs)
   {
      Element root = new Element(eloToolConfigsName);
      for (EloToolConfig eloToolConfig : eloToolConfigs.getEloToolConfigs())
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
      element.addContent(JDomConversionUtils.createElement(isContentStaticName, eloToolConfig.isContentStatic()));
      element.addContent(JDomConversionUtils.createElement(eloLogicalRolesName, eloLogicalRoleName, eloToolConfig.getEloLogicalRoles()));
      element.addContent(JDomConversionUtils.createElement(eloFunctionalRolesName, eloFunctionalRoleName, eloToolConfig.getEloFunctionalRoles()));
      return element;
   }

   public static EloToolConfigsEloContent eloToolConfigsFromXml(String xml){
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
      BasicEloToolConfigsEloContent eloToolConfigsEloContent = new BasicEloToolConfigsEloContent();
      eloToolConfigsEloContent.setEloToolConfigs(eloToolConfigs);
      return eloToolConfigsEloContent;
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
      eloToolConfig.setContentStatic(JDomConversionUtils.getBooleanValue(eloToolConfigChild,isContentStaticName));
      eloToolConfig.setEloLogicalRoles(EloLogicalRole.convertToEloLogicalRoles(JDomConversionUtils.getStringListValue(eloToolConfigChild,eloLogicalRolesName,eloLogicalRoleName)));
      eloToolConfig.setEloFunctionalRoles(EloFunctionalRole.convertToEloFunctionalRoles(JDomConversionUtils.getStringListValue(eloToolConfigChild,eloFunctionalRolesName, eloFunctionalRoleName)));
      return eloToolConfig;
   }
}
