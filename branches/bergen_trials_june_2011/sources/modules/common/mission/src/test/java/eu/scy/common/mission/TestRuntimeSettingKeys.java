package eu.scy.common.mission;

import java.net.URI;
import java.net.URISyntaxException;

public abstract class TestRuntimeSettingKeys
{
   public final String name1 = "name1";
   public final String name2 = "name2";
   public final String lasId1 = "las1";
   public final String lasId2 = "las2";
   public final URI eloUri1 = new URI("roolo//memory/1.test");
   public final URI eloUri2 = new URI("roolo//memory/2.test");
   public final String value1 = "value1";
   public final String value2 = "value2";
   public final String value11 = "value11";
   public final String value12 = "value12";
   public final String value111 = "value111";
   public final String value112 = "value112";
   public final String value3 = "value3";
   public final String value4 = "value4";
   public final String value5 = "value5";
   public final RuntimeSettingKey key1 = new RuntimeSettingKey(name1, null, null);
   public final RuntimeSettingKey key2 = new RuntimeSettingKey(name2, null, null);
   public final RuntimeSettingKey key11 = new RuntimeSettingKey(name1, lasId1, null);
   public final RuntimeSettingKey key12 = new RuntimeSettingKey(name1, lasId2, null);
   public final RuntimeSettingKey key111 = new RuntimeSettingKey(name1, lasId1, eloUri1);
   public final RuntimeSettingKey key112 = new RuntimeSettingKey(name1, lasId1, eloUri2);

   public TestRuntimeSettingKeys()  throws URISyntaxException
   {
   }

}
