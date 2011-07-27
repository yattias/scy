/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.common.mission.impl.jdom;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;

/**
 * 
 * @author SikkenJ
 */
public class JDomConversionUtils
{

   private JDomConversionUtils()
   {
   }

   public static Element createElement(String tag, boolean value)
   {
      Element element = new Element(tag);
      element.setText(Boolean.toString(value));
      return element;
   }

   public static Element createElement(String tag, int value)
   {
      Element element = new Element(tag);
      element.setText(Integer.toString(value));
      return element;
   }

   public static Element createElement(String tag, long value)
   {
      Element element = new Element(tag);
      element.setText(Long.toString(value));
      return element;
   }

   public static Element createElement(String tag, double value)
   {
      Element element = new Element(tag);
      element.setText(Double.toString(value));
      return element;
   }

   public static Element createElement(String tag, URI uri)
   {
      Element element = new Element(tag);
      if (uri != null)
      {
         element.setText(uri.toString());
      }
      return element;
   }

   public static Element createElement(String tag, Object object)
   {
      Element element = new Element(tag);
      if (object != null)
      {
         element.setText(object.toString());
      }
      return element;
   }

   public static <T extends Object> Element createElement(String tags, String tag, List<T> values)
   {
      Element element = new Element(tags);
      if (values != null)
      {
         for (T value : values)
         {
            element.addContent(createElement(tag, (value == null) ? "" : value.toString()));
         }
      }
      return element;
   }

   public static Boolean getBooleanValue(Element element)
   {
      if (element == null)
      {
         return null;
      }
      String value = element.getTextTrim();
      if (isEmpty(value))
      {
         return null;
      }
      return Boolean.parseBoolean(value);
   }

   public static Boolean getBooleanValue(Element element, String childName)
   {
      return getBooleanValue(element.getChild(childName));
   }

   private static boolean isEmpty(String string)
   {
      return string == null || string.length() == 0;
   }

   public static int getIntValue(Element element)
   {
      if (element == null)
      {
         return -1;
      }
      String value = element.getTextTrim();
      if (isEmpty(value))
      {
         return -1;
      }
      return Integer.parseInt(value);
   }

   public static int getIntValue(Element element, String childName)
   {
      return getIntValue(element.getChild(childName));
   }

   public static long getLongValue(Element element)
   {
      if (element == null)
      {
         return -1;
      }
      String value = element.getTextTrim();
      if (isEmpty(value))
      {
         return -1;
      }
      return Long.parseLong(value);
   }

   public static long getLongValue(Element element, String childName)
   {
      return getLongValue(element.getChild(childName));
   }

   public static double getDoubleValue(Element element)
   {
      if (element == null)
      {
         return -1;
      }
      String value = element.getTextTrim();
      if (isEmpty(value))
      {
         return -1;
      }
      return Double.parseDouble(value);
   }

   public static double getDoubleValue(Element element, String childName)
   {
      return getDoubleValue(element.getChild(childName));
   }

   public static URI getUriValue(Element element) throws URISyntaxException
   {
      return getUriValue(element.getTextTrim());
   }

   public static URI getUriValue(Element element, String childName) throws URISyntaxException
   {
      return getUriValue(element.getChildTextTrim(childName));
   }

   public static URI getUriValue(String uriString) throws URISyntaxException
   {
      if (uriString == null)
      {
         return null;
      }
      String trimmedUriString = uriString.trim();
      if (trimmedUriString.length() == 0)
      {
         return null;
      }
      return new URI(trimmedUriString);
   }

   public static <T extends Enum<T>> T getEnumValue(Class<T> enumType, String value)
   {
      if (value == null || value.length() == 0)
      {
         return null;
      }
      return Enum.valueOf(enumType, value.toUpperCase());
   }

   public static <T extends Enum<T>> T getEnumValue(Class<T> enumType, Element element,
            String childName)
   {
      return getEnumValue(enumType, element.getChildTextTrim(childName));
   }

   public static List<String> getStringListValue(Element element, String childName)
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

   public static List<String> getStringListValue(Element element, String childName, String tagName)
   {
      Element child = element.getChild(childName);
      if (child != null)
      {
         return getStringListValue(child, tagName);
      }
      return new ArrayList<String>();
   }

   public static List<URI> getUriListValue(Element element, String childName)
            throws URISyntaxException
   {
      List<URI> uris = new ArrayList<URI>();
      @SuppressWarnings("unchecked")
      List<Element> stringChildren = element.getChildren(childName);
      if (stringChildren != null)
      {
         for (Element stringChild : stringChildren)
         {
            uris.add(getUriValue(stringChild));
         }
      }
      return uris;
   }

   public static <T extends Enum<T>> List<T> getEnumListValue(Class<T> enumType, Element element,
            String childName, String tagName)
   {
      List<String> strings = new ArrayList<String>();
      Element listRoot = element.getChild(childName);
      if (listRoot != null)
      {
         @SuppressWarnings("unchecked")
         List<Element> stringChildren = listRoot.getChildren(tagName);
         if (stringChildren != null)
         {
            for (Element stringChild : stringChildren)
            {
               strings.add(stringChild.getTextTrim());
            }
         }
      }
      return convertToEnums(enumType, strings);
   }

   public static <T extends Enum<T>> List<T> convertToEnums(Class<T> enumType, List<String> values)
   {
      List<T> eloFunctionalRoles = new ArrayList<T>();
      for (String value : values)
      {
         eloFunctionalRoles.add(Enum.valueOf(enumType, value.toUpperCase()));
      }
      return eloFunctionalRoles;
   }
}
