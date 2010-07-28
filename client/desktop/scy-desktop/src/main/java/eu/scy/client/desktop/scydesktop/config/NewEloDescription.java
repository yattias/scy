/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.config;

/**
 *
 * @author sikkenj
 */
public class NewEloDescription {

   private String type;
   private String display;

   public NewEloDescription(String type, String display)
   {
      this.type = type;
      this.display = display;
   }

   public String getDisplay()
   {
      return display;
   }

   public String getType()
   {
      return type;
   }

   @Override
   public String toString()
   {
      return type + "->" + display;
   }


}
