package eu.scy.common.scyelo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum Access
{
   DELETED("deleted"), PRIVATE("private")/*, CLASS("class")*/, WORLD("world");
   
   private final String string;
   private static final Map<String,Access> stringMap = new HashMap<String,Access>();
   
   static{
      for (Access value : Access.values()){
         stringMap.put(value.string.toUpperCase(), value);
         stringMap.put(value.name().toUpperCase(), value);
      }
   }
   
   private Access(String string)
   {
      this.string = string;
   }

   protected static Access myValueOf(String aString)
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
    * does a bit smart equals, it is case insensitive and you may use the name or the string representation
    * 
    * @param aString
    * @return
    */
   public boolean equals(String aString)
   {
      if (aString==null || aString.length()==0){
         return false;
      }
      String upperCaseString = aString.toUpperCase();
      Access access = stringMap.get(upperCaseString);
      return equals(access);
   }

   public static List<Access> convertToAcceses(List<String> values)
   {
      if (values == null)
      {
         return null;
      }
      List<Access> accesses = new ArrayList<Access>();
      for (String value : values)
      {
         Access access = myValueOf(value);
         if (access != null)
         {
            accesses.add(access);
         }
         else
         {
            throw new IllegalArgumentException(
                     "unknown string representation for Access, " + value);
         }
      }
      return accesses;
   }



}
