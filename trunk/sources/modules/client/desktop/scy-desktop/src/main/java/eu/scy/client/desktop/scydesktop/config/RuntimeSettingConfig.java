/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.config;

import java.net.URI;

/**
 *
 * @author SikkenJ
 */
public class RuntimeSettingConfig
{

   private String name;
   private String lasId;
   private URI eloUri;
   private String value;

   public URI getEloUri()
   {
      return eloUri;
   }

   public void setEloUri(URI eloUri)
   {
      this.eloUri = eloUri;
   }

   public String getLasId()
   {
      return lasId;
   }

   public void setLasId(String lasId)
   {
      this.lasId = lasId;
   }

   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
   }

   public String getValue()
   {
      return value;
   }

   public void setValue(String value)
   {
      this.value = value;
   }
   
}
