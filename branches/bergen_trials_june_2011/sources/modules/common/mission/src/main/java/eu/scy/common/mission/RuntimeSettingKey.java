package eu.scy.common.mission;

import java.net.URI;

import org.apache.log4j.Logger;

public class RuntimeSettingKey
{
   private static final Logger logger = Logger.getLogger(RuntimeSettingKey.class);
   private final String name;
   private final String lasId;
   private final URI eloUri;

   public RuntimeSettingKey(String name, String lasId, URI eloUri)
   {
      super();
      assert name != null && name.length() > 0;
      this.name = name;
      this.lasId = lasId;
      this.eloUri = eloUri;
   }

   @Override
   public String toString()
   {
      return "RuntimeSettingKey{name=" + name + ",lasId=" + lasId + ",eloUri=" + eloUri + "}";
   }

   @Override
   public boolean equals(Object obj)
   {
      if (obj instanceof RuntimeSettingKey)
      {
         RuntimeSettingKey otherRuntimeSettingKey = (RuntimeSettingKey) obj;
         if (!name.equals(otherRuntimeSettingKey.name))
         {
             logger.info("NAME IS NOT EQAL:" + name + " && " + otherRuntimeSettingKey.name);
            return false;
         }
         if (lasId != null && !objectsEquals(lasId, otherRuntimeSettingKey.lasId))
         {
             logger.info("LAS ID IS NOT EQAL:" + lasId+ " && " + otherRuntimeSettingKey.lasId);
            return false;
         }
         if (eloUri != null && !objectsEquals(eloUri, otherRuntimeSettingKey.eloUri))
         {
             logger.info("eloUri ID IS NOT EQAL:" + eloUri+ " && " + otherRuntimeSettingKey.eloUri);
            return false;
         }
         return true;
      }
      return false;
   }

   private boolean objectsEquals(Object obj1, Object obj2)
   {
      if (obj1 != null)
      {
         return obj1.equals(obj2);
      }
      return obj2 == null;
   }

   @Override
   public int hashCode()
   {
      int code = 17;
      code = 31 * code + name.hashCode();
      code *= 31;
      if (lasId != null)
      {
         code += lasId.hashCode();
      }
      code *= 31;
      if (eloUri != null)
      {
         code += eloUri.hashCode();
      }
      return code;
   }

   public String getName()
   {
      return name;
   }

   public String getLasId()
   {
      return lasId;
   }

   public URI getEloUri()
   {
      return eloUri;
   }

   public boolean isLasIdEmpty()
   {
      return lasId == null || lasId.length() == 0;
   }

   public boolean isEloUriEmpty()
   {
      return eloUri == null;
   }

}
