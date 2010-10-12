package eu.scy.common.mission;

public class RuntimeSettingUtils
{

   private RuntimeSettingUtils()
   {
   }

   public static String getStringValue(String value, String defaultValue)
   {
      if (value == null)
      {
         return defaultValue;
      }
      return value;
   }

   public static int getIntValue(String value, int defaultValue)
   {
      if (value == null)
      {
         return defaultValue;
      }
      return Integer.parseInt(value);
   }

   public static float getFloatValue(String value, float defaultValue)
   {
      if (value == null)
      {
         return defaultValue;
      }
      return Float.parseFloat(value);
   }

   public static double getDoubleValue(String value, double defaultValue)
   {
      if (value == null)
      {
         return defaultValue;
      }
      return Double.parseDouble(value);
   }

}
