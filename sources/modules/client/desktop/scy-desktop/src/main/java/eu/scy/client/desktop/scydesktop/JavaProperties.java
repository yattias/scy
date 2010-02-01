/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.StringTokenizer;

/**
 *
 * @author sikken
 */
public class JavaProperties
{

   private static final String spaces = "                                                                                             ";
   private static final String classPathPropName = "java.class.path";
   private static final String libraryPathPropName = "java.library.path";
   private static final String sunBootClassPathPropName = "sun.boot.class.path";
   private static final String sunBootLibraryPathPropName = "sun.boot.library.path";

   /**
    * Writes the value of some system properties accessible to applications to the Java console.
    *
    * @see writePropertiesForApplet()
    */
   public static void writePropertiesForApplication()
   {
      System.out.println("System properties");
      Enumeration<?> enumeration = System.getProperties().propertyNames();
      ArrayList<String> propNames = new ArrayList<String>();
      int maxPropNameLength = 0;
      while (enumeration.hasMoreElements())
      {
         String propName = (String) enumeration.nextElement();
         propNames.add(propName);
         maxPropNameLength = Math.max(maxPropNameLength, propName.length());
      }
      Collections.sort(propNames);
      for (String propName : propNames)
      {
         String label = propName + spaces.substring(0, maxPropNameLength - propName.length());
         if (classPathPropName.equals(propName) || libraryPathPropName.equals(propName)
            || sunBootClassPathPropName.equals(propName)
            || sunBootLibraryPathPropName.equals(propName))
         {
            writeClassPath(label, System.out);
         }
         else
         {
            System.out.println(label + ": " + System.getProperty(propName));
         }
      }
      System.out.println();
   }

   protected static void writeClassPath(String label, PrintStream out)
   {
      String classPath = System.getProperty("java.class.path");
      StringTokenizer tokens = new StringTokenizer(classPath, System.getProperty("path.separator"));
      boolean firstLine = true;
      while (tokens.hasMoreTokens())
      {
         if (firstLine)
         {
            out.println(label + ": " + tokens.nextToken());
            firstLine = false;
         }
         else
         {
            out.println(spaces.substring(0, label.length()) + "  " + tokens.nextToken());
         }
      }
   }

   public static void writeApplicationParameters(String[] args)
   {
      System.out.println("Application parameters");
      for (int i = 0; i < args.length; i++)
      {
         System.out.println("" + i + ": " + args[i]);
      }
      System.out.println("\n");
   }
}
