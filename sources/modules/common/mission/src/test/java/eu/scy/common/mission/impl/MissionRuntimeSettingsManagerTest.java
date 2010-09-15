package eu.scy.common.mission.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import eu.scy.common.mission.RuntimeSettingKey;
import eu.scy.common.mission.RuntimeSettingsElo;
import eu.scy.common.mission.RuntimeSettingsManager;
import eu.scy.common.mission.TestRuntimeSettingKeys;
import eu.scy.common.scyelo.RooloServices;

public class MissionRuntimeSettingsManagerTest extends TestRuntimeSettingKeys
{   
   MissionRuntimeSettingsManager missionRuntimeSettingsManager;
   RuntimeSettingsManager specificationRuntimeSettings; 
   RuntimeSettingsManager runtimeRuntimeSettings; 
   
   public MissionRuntimeSettingsManagerTest() throws URISyntaxException{
   }

   @Before
   public void setUp() throws Exception
   {
      RooloServices rooloServices = RooloServicesCreator.createTestRooloServices();
      RuntimeSettingsElo specificationRuntimeSettingsElo = RuntimeSettingsElo.createElo(rooloServices);
      specificationRuntimeSettingsElo.saveAsNewElo();
      RuntimeSettingsElo runtimeRuntimeSettingsElo = RuntimeSettingsElo.createElo(rooloServices);
      runtimeRuntimeSettingsElo.saveAsNewElo();
      Set<URI> specifiactionContentEloUri = new HashSet<URI>();
      missionRuntimeSettingsManager = new MissionRuntimeSettingsManager(
               specificationRuntimeSettingsElo, runtimeRuntimeSettingsElo,
               specifiactionContentEloUri, rooloServices);
      specificationRuntimeSettings = specificationRuntimeSettingsElo.getTypedContent();
      runtimeRuntimeSettings = runtimeRuntimeSettingsElo.getTypedContent();
   }
   
   @Test
   public void testRuntimePartSettings(){
      testGetNull(key1,key2,key11,key12,key111,key112);
      missionRuntimeSettingsManager.addSetting(key1, value1);
      testGetNull(key2);
      testGetValue(value1,key1,key11,key12,key111,key112);
      missionRuntimeSettingsManager.addSetting(key2, value2);
      testGetValue(value1,key1,key11,key12,key111,key112);
      testGetValue(value2,key2);
      missionRuntimeSettingsManager.addSetting(key11, value11);
      testGetValue(value1,key1,key12);
      testGetValue(value2,key2);
      testGetValue(value11,key11,key111,key112);
      missionRuntimeSettingsManager.addSetting(key12, value12);
      testGetValue(value1,key1);
      testGetValue(value2,key2);
      testGetValue(value11,key11,key111,key112);
      testGetValue(value12,key12);
      missionRuntimeSettingsManager.addSetting(key111, value111);
      testGetValue(value1,key1);
      testGetValue(value2,key2);
      testGetValue(value11,key11,key112);
      testGetValue(value12,key12);
      testGetValue(value111,key111);
      missionRuntimeSettingsManager.addSetting(key112, value112);
      assertEquals(value1,missionRuntimeSettingsManager.getSetting(key1));
      assertEquals(value2,missionRuntimeSettingsManager.getSetting(key2));
      assertEquals(value11,missionRuntimeSettingsManager.getSetting(key11));
      assertEquals(value12,missionRuntimeSettingsManager.getSetting(key12));
      assertEquals(value111,missionRuntimeSettingsManager.getSetting(key111));
      assertEquals(value112,missionRuntimeSettingsManager.getSetting(key112));
      missionRuntimeSettingsManager.addSetting(key2,value3);
      assertEquals(value3,missionRuntimeSettingsManager.getSetting(key2));
      // now add some setting the mission specification runtime settings
   }
   
   @Test
   public void testSpecificationPartSettings(){
      testGetNull(key1,key2,key11,key12,key111,key112);
      specificationRuntimeSettings.addSetting(key1, value1);
      testGetNull(key2);
      testGetValue(value1,key1,key11,key12,key111,key112);
      specificationRuntimeSettings.addSetting(key11, value11);
      testGetNull(key2);
      testGetValue(value1,key1,key12);
      testGetValue(value11,key11,key111,key112);
      specificationRuntimeSettings.addSetting(key111, value111);
      testGetNull(key2);
      testGetValue(value1,key1,key12);
      testGetValue(value11,key11,key111,key112);
      testGetValue(value111);
      missionRuntimeSettingsManager.addSetting(key111, value3);
      testGetNull(key2);
      testGetValue(value1,key1,key12);
      testGetValue(value11,key11,key112);
      testGetValue(value3,key111);
      missionRuntimeSettingsManager.addSetting(key11, value4);
      testGetNull(key2);
      testGetValue(value1,key1,key12);
      testGetValue(value11,key112);
      testGetValue(value3,key111);
      testGetValue(value4,key11);
      
   }
   
   private void testGetNull(RuntimeSettingKey... keys){
      for (RuntimeSettingKey key : keys){
         String value = missionRuntimeSettingsManager.getSetting(key);
         assertNull("null expected for key " + key + ", but got " + value,value);
      }
   }

   private void testGetValue(String expected, RuntimeSettingKey... keys){
      for (RuntimeSettingKey key : keys){
         String value = missionRuntimeSettingsManager.getSetting(key);
         assertEquals("error for key " + key,expected,value);
      }
      
   }
}
