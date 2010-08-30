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

   public static Element createElement(String tag, URI uri)
   {
      Element element = new Element(tag);
      if (uri != null)
      {
         element.setText(uri.toString());
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

   public static boolean getBooleanValue(Element element)
   {
      return Boolean.parseBoolean(element.getTextTrim());
   }

   public static boolean getBooleanValue(Element element, String childName)
   {
      return Boolean.parseBoolean(element.getChildTextTrim(childName));
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

   public static List<URI> getUriListValue(Element element, String childName) throws URISyntaxException
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

   public static <T extends Enum<T>> List<T> getEnumListValue(Class<T> enumType, Element element, String childName, String tagName)
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
