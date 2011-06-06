/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop;

/**
 *
 * @author sikken
 */
public enum FunctionalTypes
{

   ASSIGMENT("informationAssignment");
   
   private final String id;

   private FunctionalTypes(String id)
   {
      this.id = id;
   }

   public String getId()
   {
      return id;
   }

   public boolean equals(String string)
   {
      return id.equalsIgnoreCase(string);
   }
}
