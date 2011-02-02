package eu.scy.common.mission.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.net.URISyntaxException;

import org.junit.Before;
import org.junit.Test;

import eu.scy.common.mission.RuntimeSettingKey;
import eu.scy.common.mission.TestRuntimeSettingKeys;
import eu.scy.common.mission.impl.jdom.RuntimeSettingsEloContentXmlUtils;

public class BasicRuntimeSettingsEloContentTest extends TestRuntimeSettingKeys
{
   public BasicRuntimeSettingsEloContentTest() throws URISyntaxException
   {
      super();
   }

   private BasicRuntimeSettingsEloContent basicRuntimeSettingsEloContent;

   @Before
   public void setUp() throws Exception
   {
      basicRuntimeSettingsEloContent = new BasicRuntimeSettingsEloContent();
   }

   @Test
   public void testGetSetting(){
      testGetNull(key1,key2,key11,key12,key111,key112);
      basicRuntimeSettingsEloContent.addSetting(key1, value1);
      testGetNull(key2,key11,key12,key111,key112);
      basicRuntimeSettingsEloContent.addSetting(key2, value2);
      testGetNull(key11,key12,key111,key112);
      basicRuntimeSettingsEloContent.addSetting(key11, value11);
      testGetNull(key12,key111,key112);
      basicRuntimeSettingsEloContent.addSetting(key12, value12);
      testGetNull(key111,key112);
      basicRuntimeSettingsEloContent.addSetting(key111, value111);
      testGetNull(key112);
      basicRuntimeSettingsEloContent.addSetting(key112, value112);
      assertEquals(value1,basicRuntimeSettingsEloContent.getSetting(key1));
      assertEquals(value2,basicRuntimeSettingsEloContent.getSetting(key2));
      assertEquals(value11,basicRuntimeSettingsEloContent.getSetting(key11));
      assertEquals(value12,basicRuntimeSettingsEloContent.getSetting(key12));
      assertEquals(value111,basicRuntimeSettingsEloContent.getSetting(key111));
      assertEquals(value112,basicRuntimeSettingsEloContent.getSetting(key112));
      basicRuntimeSettingsEloContent.addSetting(key2,value3);
      assertEquals(value3,basicRuntimeSettingsEloContent.getSetting(key2));
   }
   
   private void testGetNull(RuntimeSettingKey... keys){
      for (RuntimeSettingKey key : keys){
         assertNull(basicRuntimeSettingsEloContent.getSetting(key));
      }
   }
   
   @Test
   public void testOverwrite(){
      assertEquals(0, basicRuntimeSettingsEloContent.getAllSettings().size());
      basicRuntimeSettingsEloContent.addSetting(key1, value1);
      assertEquals(1, basicRuntimeSettingsEloContent.getAllSettings().size());
      assertEquals(value1, basicRuntimeSettingsEloContent.getSetting(key1));
      String xml1 = RuntimeSettingsEloContentXmlUtils.runtimeSettingsToXml(basicRuntimeSettingsEloContent);
      basicRuntimeSettingsEloContent.addSetting(key1, value2);
      assertEquals(1, basicRuntimeSettingsEloContent.getAllSettings().size());
      assertEquals(value2, basicRuntimeSettingsEloContent.getSetting(key1));
      String xml2 = RuntimeSettingsEloContentXmlUtils.runtimeSettingsToXml(basicRuntimeSettingsEloContent);
      assertEquals(xml1.length(), xml2.length());
      basicRuntimeSettingsEloContent.addSetting(key1, value3);
      assertEquals(1, basicRuntimeSettingsEloContent.getAllSettings().size());
      assertEquals(value3, basicRuntimeSettingsEloContent.getSetting(key1));
      String xml3 = RuntimeSettingsEloContentXmlUtils.runtimeSettingsToXml(basicRuntimeSettingsEloContent);
      assertEquals(xml1.length(), xml3.length());
      RuntimeSettingKey copyKey1 = new RuntimeSettingKey(key1.getName(), key1.getLasId(), key1.getEloUri());
      basicRuntimeSettingsEloContent.addSetting(copyKey1, value1);
      assertEquals(1, basicRuntimeSettingsEloContent.getAllSettings().size());
      assertEquals(value1, basicRuntimeSettingsEloContent.getSetting(key1));
      assertEquals(value1, basicRuntimeSettingsEloContent.getSetting(copyKey1));
      String xml4 = RuntimeSettingsEloContentXmlUtils.runtimeSettingsToXml(basicRuntimeSettingsEloContent);
      assertEquals(xml1.length(), xml4.length());
   }

}
