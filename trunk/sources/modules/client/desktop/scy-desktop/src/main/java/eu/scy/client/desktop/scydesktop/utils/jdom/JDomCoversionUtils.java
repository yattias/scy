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

   public static Element createElement(String tags, String tag, List<String> values)
   {
      Element element = new Element(tags);
      if (values != null)
      {
         for (String value : values)
         {
            element.addContent(createElement(tag, value));
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
}
