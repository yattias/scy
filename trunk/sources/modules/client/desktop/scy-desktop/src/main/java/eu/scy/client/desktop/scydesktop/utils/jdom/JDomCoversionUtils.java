/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.utils.jdom;

import java.util.ArrayList;
import java.util.List;
import org.jdom.Element;

/**
 *
 * @author SikkenJ
 */
public class JDomCoversionUtils
{

   private JDomCoversionUtils()
   {
   }

   public static Element createElement(String tag, String value)
   {
      Element element = new Element(tag);
      element.setText(value);
      return element;
   }

   public static Element createElement(String tag, boolean value)
   {
      Element element = new Element(tag);
      element.setText(Boolean.toString(value));
      return element;
   }

   public static <T extends Object> Element createElement(String tags, String tag, List<T> values)
   {
      Element element = new Element(tags);
      if (values != null)
      {
         for (T value : values)
         {
            element.addContent(createElement(tag, (value==null) ? "" : value.toString()));
         }
      }
      return element;
   }

   public static boolean getBooleanValue(Element element)
   {
      return Boolean.parseBoolean(element.getTextTrim());
   }

   public static boolean getBooleanValue(Element element, String childName)
   {
      return Boolean.parseBoolean(element.getChildTextTrim(childName));
   }

   public static List<String> getStringListValue(Element element, String childName, String tagName)
   {
      List<String> strings = new ArrayList<String>();
      @SuppressWarnings("unchecked")
      List<Element> stringChildren = element.getChildren(childName);
      if (stringChildren != null)
      {
         for (Element stringChild : stringChildren)
         {
            strings.add(stringChild.getTextTrim());
         }
      }
      return strings;
   }

   public static <T extends Enum<T>> List<T> getEnumListValue(Class<T> enumType, Element element, String childName, String tagName)
   {
      List<String> strings = new ArrayList<String>();
      @SuppressWarnings("unchecked")
      List<Element> stringChildren = element.getChildren(childName);
      if (stringChildren != null)
      {
         for (Element stringChild : stringChildren)
         {
            strings.add(stringChild.getTextTrim());
         }
      }
      return convertToEnums(enumType,strings);
   }

   public static <T extends Enum<T>> List<T> convertToEnums(Class<T> enumType,List<String> values){
      List<T> eloFunctionalRoles = new ArrayList<T>();
      for (String value : values){
         eloFunctionalRoles.add(Enum.valueOf(enumType,value.toUpperCase()));
      }
      return eloFunctionalRoles;
   }
}
