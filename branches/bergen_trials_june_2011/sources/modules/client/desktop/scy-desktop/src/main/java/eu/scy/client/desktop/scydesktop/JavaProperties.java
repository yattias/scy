/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop;

import java.io.File;
import java.io.PrintWriter;
import java.security.AccessControlException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;

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
   private static final SimpleDateFormat modifiedFormat = new SimpleDateFormat("yyyy.MM.dd, HH:mm:ss");

   /**
    * Writes the value of some system properties accessible to applications to the Java console.
    *
    * @see writePropertiesForApplet()
    */
   public static void writePropertiesForApplication(PrintWriter out)
   {
      out.println("System properties");
      ArrayList<String> propNames = new ArrayList<String>();
      int maxPropNameLength = 0;
      Enumeration<?> enumeration;
      try
      {
         enumeration = System.getProperties().propertyNames();
      }
      catch (AccessControlException e)
      {
         Vector<String> pathNames = new Vector<String>();
         pathNames.add(classPathPropName);
         pathNames.add(libraryPathPropName);
         pathNames.add(sunBootClassPathPropName);
         pathNames.add(sunBootLibraryPathPropName);
         enumeration = pathNames.elements();
      }
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
            writeClassPath(label, propName, out);

         }
         else
         {
            out.println(label + ": " + System.getProperty(propName));
         }
      }
      out.println();
   }

   protected static void writeClassPath(String label, String propName, PrintWriter out)
   {
      String classPath;
      try
      {
         classPath = System.getProperty(propName);
      }
      catch (AccessControlException e)
      {
         classPath = "access denied";
      }
      if (classPath == null)
      {
         classPath = "";
      }
      StringTokenizer tokens = new StringTokenizer(classPath, System.getProperty("path.separator"));
      boolean firstLine = true;
      while (tokens.hasMoreTokens())
      {
         String fileName = tokens.nextToken();
         String fileModified;
         try {
             fileModified = getFileLastModified(fileName);
         } catch (AccessControlException e) {
             fileModified = "access denied";
         }
         if (firstLine)
         {
            out.println(label + ": " + fileName + " (" + fileModified + ")");
            firstLine = false;
         }
         else
         {
            out.println(spaces.substring(0, label.length()) + "  " + fileName + " (" + fileModified + ")");
         }
      }
   }

   private static String getFileLastModified(String fileName)
   {
      File file = new File(fileName);
      if (file.exists())
      {
         return modifiedFormat.format(new Date(file.lastModified()));
      }
      return "does not exists";
   }

   public static void writeApplicationParameters(String[] args, PrintWriter out)
   {
      out.println("Application parameters");
      for (int i = 0; i < args.length; i++)
      {
         out.println("" + i + ": " + args[i]);
      }
      out.println("\n");
   }
}
