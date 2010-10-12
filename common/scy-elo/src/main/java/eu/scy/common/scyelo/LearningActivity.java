package eu.scy.common.scyelo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum LearningActivity
{
   EXPERIMENTATION("experimentation"), DESIGN("design"), REPORT("report");
   
   private final String string;
   private static final Map<String,LearningActivity> stringMap = new HashMap<String,LearningActivity>();
   
   static{
      for (LearningActivity value : LearningActivity.values()){
         stringMap.put(value.string.toUpperCase(), value);
         stringMap.put(value.name().toUpperCase(), value);
      }
   }
   
   private LearningActivity(String string)
   {
      this.string = string;
   }

   protected static LearningActivity myValueOf(String aString)
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
      LearningActivity learningActivity = stringMap.get(upperCaseString);
      return equals(learningActivity);
   }

   public static List<LearningActivity> convertToLearningActivities(List<String> values)
   {
      if (values == null)
      {
         return null;
      }
      List<LearningActivity> learningActivities = new ArrayList<LearningActivity>();
      for (String value : values)
      {
         LearningActivity learningActivity = myValueOf(value);
         if (learningActivity != null)
         {
            learningActivities.add(learningActivity);
         }
         else
         {
            throw new IllegalArgumentException(
                     "unknown string representation for LearningActivity, " + value);
         }
      }
      return learningActivities;
   }


}
