/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.config;

/**
 *
 * @author sikken
 */
public class TypeNameSet
{

   private String type;
   private String name;

   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name.trim();
   }

   public String getType()
   {
      return type;
   }

   public void setType(String type)
   {
      this.type = type.trim();
   }
}
