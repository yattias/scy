/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author SikkenJ
 */
public enum LoginType
{

   LOCAL("local"), LOCAL_MULTI_USER("localMultiUser"), REMOTE("remote");
   private final String[] representations;
   private static final Map<String, LoginType> stringMap = new HashMap<String, LoginType>();

   static
   {
      for (LoginType value : LoginType.values())
      {
         for (String representation : value.representations)
         {
            stringMap.put(representation.toLowerCase(), value);
         }
      }
   }

   private LoginType(String... representations)
   {
      this.representations = representations;
   }

   public static LoginType convertToLoginType(final String representation)
   {
      final LoginType loginType = stringMap.get(representation.toLowerCase());
      if (loginType != null)
      {
         return loginType;
      }
      throw new IllegalArgumentException("can't find the login type for " + representation);
   }
}
