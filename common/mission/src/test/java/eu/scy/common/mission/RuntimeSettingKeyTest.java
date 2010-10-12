package eu.scy.common.mission;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Before;
import org.junit.Test;

public class RuntimeSettingKeyTest
{
   private final static String name1 = "name1";
   private final static String name2 = "name2";
   private final static String lasId1 = "las1";
   private final static String lasId2 = "las2";
   private final URI eloUri1 = new URI("roolo//memory/1.test");
   private final URI eloUri2 = new URI("roolo//memory/2.test");

   public RuntimeSettingKeyTest() throws URISyntaxException
   {

   }

   @Before
   public void setUp() throws Exception
   {
   }

   @Test
   public void testEqualsAndHashCode()
   {
      testEqualsAndHashCode(true,new RuntimeSettingKey(name1, null, null), new RuntimeSettingKey(name1, null,
               null));
      testEqualsAndHashCode(true,new RuntimeSettingKey(name1, lasId1, null), new RuntimeSettingKey(name1, lasId1,
               null));
      testEqualsAndHashCode(true,new RuntimeSettingKey(name1, lasId1, eloUri1), new RuntimeSettingKey(name1,
               lasId1, eloUri1));
      testEqualsAndHashCode(false,new RuntimeSettingKey(name1, null, null), new RuntimeSettingKey(name2, null, null));
      testEqualsAndHashCode(false,new RuntimeSettingKey(name1, lasId1, null), new RuntimeSettingKey(name1, lasId2, null));
      testEqualsAndHashCode(false,new RuntimeSettingKey(name1, lasId1, eloUri1), new RuntimeSettingKey(name1, lasId1, eloUri2));
      testEqualsAndHashCode(false,new RuntimeSettingKey(name1, lasId1, eloUri1), new RuntimeSettingKey(name1, lasId2, eloUri1));
      testEqualsAndHashCode(false,new RuntimeSettingKey(name1, lasId1, eloUri1), new RuntimeSettingKey(name2, lasId1, eloUri1));
      testEqualsAndHashCode(false,new RuntimeSettingKey(name1, lasId1, eloUri1), new RuntimeSettingKey(name2, lasId2, eloUri2));
   }
   
   private void testEqualsAndHashCode(boolean equal, RuntimeSettingKey key1, RuntimeSettingKey key2){
      if (equal){
         assertTrue(key1.equals(key2));
         assertEquals(key1.hashCode(),key2.hashCode());
      }
      else{
         assertFalse(key1.equals(key2));
         assertFalse(key1.hashCode()==key2.hashCode());
      }
   }

}
