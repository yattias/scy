package eu.scy.common.mission.impl.jdom;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.jdom.Element;

import eu.scy.common.mission.RuntimeSetting;
import eu.scy.common.mission.RuntimeSettingKey;
import eu.scy.common.mission.RuntimeSettingsEloContent;
import eu.scy.common.mission.impl.BasicRuntimeSettingsEloContent;

public class RuntimeSettingsEloContentXmlUtils
{
   private static final String runtimeSettingsName = "runtimeSettings";
   private static final String runtimeSettingName = "runtimeSetting";
   private static final String nameName = "name";
   private static final String lasIdName = "lasId";
   private static final String eloUriName = "eloUri";
   private static final String valueName = "value";

   private RuntimeSettingsEloContentXmlUtils()
   {
   }

   public static String runtimeSettingsToXml(RuntimeSettingsEloContent runtimeSettings)
   {
      Element root = new Element(runtimeSettingsName);
      for (RuntimeSetting runtimeSetting : runtimeSettings.getAllSettings()){
         Element runtimeSettingElement = new Element(runtimeSettingName);
         root.addContent(runtimeSettingElement);
         runtimeSettingElement.addContent(JDomConversionUtils.createElement(nameName,runtimeSetting.getKey().getName()));
         runtimeSettingElement.addContent(JDomConversionUtils.createElement(lasIdName,runtimeSetting.getKey().getLasId()));
         runtimeSettingElement.addContent(JDomConversionUtils.createElement(eloUriName,runtimeSetting.getKey().getEloUri()));
         runtimeSettingElement.addContent(JDomConversionUtils.createElement(valueName,runtimeSetting.getValue()));
      }
      return new JDomStringConversion().xmlToString(root);
   }

   public static RuntimeSettingsEloContent runtimeSettingsFromXml(String xml) throws URISyntaxException
   {
      Element root = new JDomStringConversion().stringToXml(xml);
      if (root==null || !runtimeSettingsName.equals(root.getName())){
         return null;
      }
      BasicRuntimeSettingsEloContent runtimeSettings = new BasicRuntimeSettingsEloContent();
      @SuppressWarnings("unchecked")
      List<Element> runtimeSettingChildren = root.getChildren(runtimeSettingName);
      if (runtimeSettingChildren != null)
      {
         for (Element runtimeSettingChild : runtimeSettingChildren)
         {
            String name = runtimeSettingChild.getChildTextTrim(nameName);
            String lasId = runtimeSettingChild.getChildTextTrim(lasIdName);
            URI eloUri = JDomConversionUtils.getUriValue(runtimeSettingChild, eloUriName);
            String value = runtimeSettingChild.getChildTextTrim(valueName);
            runtimeSettings.addSetting(new RuntimeSettingKey(name,lasId,eloUri), value);
         }
      }
      return runtimeSettings;
   }
}
