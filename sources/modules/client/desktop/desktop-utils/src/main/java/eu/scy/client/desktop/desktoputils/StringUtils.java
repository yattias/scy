/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.desktoputils;

/**
 *
 * @author sikken
 */
public class StringUtils
{

   private StringUtils()
   {
   }

   public static boolean isEmpty(String string)
   {
      return string == null || string.length() == 0;
   }

   public static boolean hasText(String string)
   {
      return !isEmpty(string);
   }

   public static String putInValues(String string, String... values)
   {
      String resultString = string;
      for (int i = 0; i < values.length; i++)
      {
         String replaceValue = values[i];
         if (replaceValue == null)
         {
            replaceValue = "";
         }
         resultString = resultString.replace("%" + i + "%", replaceValue);
      }
      return resultString;
   }
}
