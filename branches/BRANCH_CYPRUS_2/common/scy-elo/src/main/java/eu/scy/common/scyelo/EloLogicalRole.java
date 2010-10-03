/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.common.scyelo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author SikkenJ
 */
public enum EloLogicalRole
{
   CONCEPT_MAP("conceptMap"), DATASET("dataset"), STRUCTURED_TEXT("structuredText"), DRAWING(
            "drawing"), PLAIN_TEXT("plainText"), RICH_TEXT("richText"), RICH_TEXT_WITH_IMAGES(
            "richTextWithImages"), PRESENTATION("presentation"), TABLE("table"), INFORMATION(
            "information");

   private final String string;
   private static final Map<String, EloLogicalRole> stringMap = new HashMap<String, EloLogicalRole>();

   static
   {
      for (EloLogicalRole value : EloLogicalRole.values())
      {
         stringMap.put(value.string.toUpperCase(), value);
         stringMap.put(value.name().toUpperCase(), value);
      }
   }

   private EloLogicalRole(String string)
   {
      this.string = string;
   }

   protected static EloLogicalRole myValueOf(String aString)
   {
      if (aString == null || aString.length() == 0)
      {
         return null;
      }
      return stringMap.get(aString.toUpperCase());
   }

   @Override
   public String toString()
   {
      return string;
   }

   /**
    * does a bit smart equals, it is case insensitive and you may use the name or the string
    * representation
    * 
    * @param aString
    * @return
    */
   public boolean equals(String aString)
   {
      return equals(myValueOf(aString));
   }
   
   public static List<EloLogicalRole> convertToEloLogicalRoles(List<String> values)
   {
      if (values == null)
      {
         return null;
      }
      List<EloLogicalRole> eloLogicalRoles = new ArrayList<EloLogicalRole>();
      for (String value : values)
      {
         EloLogicalRole eloLogicalRole = myValueOf(value);
         if (eloLogicalRole != null)
         {
            eloLogicalRoles.add(eloLogicalRole);
         }
         else
         {
            throw new IllegalArgumentException("unknown string representation for EloLogicalRole, "
                     + value);
         }
      }
      return eloLogicalRoles;
   }

}
