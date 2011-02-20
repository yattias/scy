/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.art;

import eu.scy.client.desktop.scydesktop.art.images.FXDIconParser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author SikkenJ
 */
public class JavaFxdEloIconLoader
{

   HashMap<String, String> replaceMap = new HashMap<String, String>();

   public JavaFxdEloIconLoader(File file, File targetDir)
   {
//	File file = new File("..\\..\\src\\main\\java\\eu\\scy\\client\\desktop\\scydesktop\\art\\images\\content.fxd");
      ArrayList<String> group = null;
      BufferedReader reader = null;
      ArrayList<String> names = new ArrayList<String>();

      // very dark blue
      replaceMap.put("Color.rgb(0x26,0x2f,0x66)", "bind windowColorScheme.mainColor");
      // very dark blue
      replaceMap.put("Color.rgb(0x1a,0x1e,0x5d)", "bind windowColorScheme.mainColor");
      // dark blue
      replaceMap.put("Color.rgb(0x68,0x4d,0x9a)", "bind windowColorScheme.mainColor");
      // dark blue
      replaceMap.put("Color.rgb(0x0,0x51,0xf4)", "bind windowColorScheme.mainColor");
      // dark blue
      replaceMap.put("Color.rgb(0xe9,0x5a,0xc)", "bind windowColorScheme.mainColor");
      // light blue
      replaceMap.put("Color.rgb(0xb5,0xd5,0xf0)", "bind windowColorScheme.mainColorLight");
      // light blue
      replaceMap.put("Color.rgb(0xd3,0xe6,0xf6)", "bind windowColorScheme.mainColorLight");
      // green
      replaceMap.put("Color.rgb(0x0,0xb2,0x59)", "bind windowColorScheme.secondColor");
      // light green
      replaceMap.put("Color.rgb(0xcc,0xf0,0xde)", "bind windowColorScheme.secondColorLight");
      // dark orange/red
      replaceMap.put("Color.rgb(0xff,0x40,0x0)", "bind windowColorScheme.thirdColor");
      // pink
      replaceMap.put("Color.rgb(0xff,0x0,0x97)", "bind windowColorScheme.thirdColor");
      // light red
      replaceMap.put("Color.rgb(0xf2,0xd5,0xc7)", "bind windowColorScheme.thirdColorLight");
      // light orange
      replaceMap.put("Color.rgb(0xff,0xea,0x9e)", "bind windowColorScheme.thirdColorLight");

      // medium grey
      // replaceMap.put("Color.rgb(0x9a,0x99,0x99)", "bind windowColorScheme.thirdColor");
      // dark grey
      // replaceMap.put("Color.rgb(0x47,0x46,0x46)", "bind windowColorScheme.thirdColor");
      // light grey
      // replaceMap.put("Color.rgb(0xf1,0xf1,0xef)", "bind windowColorScheme.thirdColor");


      //	// green
//	replaceMap.put("Color.rgb(0x6a,0xb3,0x30)", "bind windowColorScheme.secondColor");
//	// pink
//	replaceMap.put("Color.rgb(0xff,0x0,0x97)", "bind windowColorScheme.thirdColor");
//	// light pink
//	replaceMap.put("Color.rgb(0xff,0x5f,0xd7)", "bind windowColorScheme.thirdColorLight");
//
//	// medium grey
//	replaceMap.put("Color.rgb(0x9a,0x99,0x99)", "bind windowColorScheme.thirdColor");
//	// dark grey
//	replaceMap.put("Color.rgb(0x47,0x46,0x46)", "bind windowColorScheme.thirdColor");
//	// light grey
//	replaceMap.put("Color.rgb(0xf1,0xf1,0xef)", "bind windowColorScheme.thirdColor");
//

      try
      {
         reader = new BufferedReader(new FileReader(file));
         String text = null;
         String name = null;

         while ((text = reader.readLine()) != null)
         {
            if (text.contains("Group {"))
            {
               text = reader.readLine();
               group = new ArrayList<String>();
               name = text.substring(8, text.length() - 1);
               name = name.replace(" ", "_");
               name = name.replace("-", "_");
               name = name.substring(0, 1).toUpperCase() + name.substring(1);
               System.out.println("parsing: " + name);
               while (!(text.contains("]") && text.contains("},")))
               {
                  group.add(text);
                  text = reader.readLine();
               }
               group.add("},");
               if (!name.startsWith("_"))
               {
                  createClass(group, name, targetDir);
                  names.add(name);
               }
            }
         }
         createFactory(names,targetDir);
      }
      catch (FileNotFoundException e)
      {
         e.printStackTrace();
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
      finally
      {
         try
         {
            if (reader != null)
            {
               reader.close();
            }
         }
         catch (IOException e)
         {
            e.printStackTrace();
         }
      }
   }

   public static void main(String[] args)
   {
      FXDIconParser fXDIconParser = new FXDIconParser();
   }

   private void createFactory(ArrayList<String> names, File targetDir)
   {
      FileWriter outFile;
      try
      {
         outFile = new FileWriter(new File(targetDir,"EloIconFactory.fx"));
         PrintWriter out = new PrintWriter(outFile);
         out.println("package eu.scy.client.desktop.scydesktop.art.eloicons;");
         out.println("");
         out.println("import eu.scy.client.desktop.scydesktop.art.javafx.LogoEloIcon;");
         out.println("import eu.scy.client.desktop.scydesktop.scywindows.EloIcon;");
         out.println("import eu.scy.client.desktop.scydesktop.art.WindowColorScheme;");
         out.println("import eu.scy.client.desktop.scydesktop.art.ScyColors;");
         out.println("/**");
         out.println("* @author lars");
         out.println("*/");
         out.println();
         out.println("public class EloIconFactory {");
         out.println(" def windowColorScheme = WindowColorScheme.getWindowColorScheme(ScyColors.darkGray);");
         out.println();

         out.print("def names = [");
         String allNames = new String();
         for (String name : names)
         {
            allNames = allNames.concat("\"" + name + "\",");
         }
         out.print(allNames.substring(0, allNames.length() - 1));
         out.println("];");
         out.println();

         out.println("public function getNames(): String[] {");
         out.println("return names;");
         out.println("}");
         out.println();

         out.println("public function createEloIcon(name: String): EloIcon {");
         out.println("if (name.equalsIgnoreCase(\"dummy\")) {");
         out.println("return null;");
         for (String name : names)
         {
            out.println("} else if (name.equalsIgnoreCase(\"" + name + "\")) {");
            out.println("return " + name + "Icon{windowColorScheme:windowColorScheme};");
         }
         out.println("} else {");
         out.println("return new LogoEloIcon();");
         out.println("}");
         out.println("}");
         out.println("}");
         out.close();
      }
      catch (IOException ex)
      {
         Logger.getLogger(FXDIconParser.class.getName()).log(Level.SEVERE, null, ex);
      }
   }

   private void createClass(ArrayList<String> group, String name, File targetDir)
   {
      try
      {
         FileWriter outFile = new FileWriter(new File(targetDir,name + "Icon.fx"));
         PrintWriter out = new PrintWriter(outFile);
         writeHeader(out, name);
         for (String s : group)
         {
            if (s.contains("id:") || s.contains("visible:"))
            {
               // doing nothing, skip these lines
            }
            else
            {
               for (String find : replaceMap.keySet())
               {
                  if (s.contains(find))
                  {
                     System.out.println("replacing '" + find + "' with '" + replaceMap.get(find) + "'.");
                     s = s.replace(find, replaceMap.get(find));
                  }
               }
               out.println(s);
            }
         }
         writeFooter(out, name);
         out.close();
         System.out.println("*** " + name + " finished. ***");
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
   }

   private void writeHeader(PrintWriter out, String name)
   {
      out.println("package eu.scy.client.desktop.scydesktop.art.eloicons;");
      out.println();
      out.println("import javafx.scene.Scene;");
      out.println("import javafx.stage.Stage;");
      out.println("import eu.scy.client.desktop.scydesktop.art.ScyColors;");
      out.println("import javafx.scene.Node;");
      out.println("import javafx.scene.Group;");
      out.println("import javafx.scene.shape.*;");
      out.println("import javafx.scene.paint.Color;");
      out.println("import javafx.scene.transform.Transform;");
      out.println("import eu.scy.client.desktop.scydesktop.art.WindowColorScheme;");
      out.println("/**");
      out.println(" * @author lars");
      out.println(" */");
      out.println("public class " + name + "Icon extends AbstractEloIcon {");
      out.println();
      out.println("public override function clone(): " + name + "Icon {");
      out.println(name + "Icon {");
      out.println("selected: selected");
      out.println("size: size");
      out.println("windowColorScheme: windowColorScheme");
      out.println("}");
      out.println("}");
      out.println();
      out.println("public override function createNode(): Node {");
      out.println();
      out.println("return Group {");
      out.println();
   }

   private void writeFooter(PrintWriter out, String name)
   {
      out.println("]");
      out.println("}");
      out.println("}");
      out.println("}");
      out.println("function run(){");
      out.println("   def windowColorScheme = WindowColorScheme.getWindowColorScheme(ScyColors.darkGray);");
      out.println("   Stage {");
      out.println("	title: 'MyApp'");
      out.println("	onClose: function () {  }");
      out.println("	scene: Scene {");
      out.println("		width: 200");
      out.println("		height: 200");
      out.println("      fill: Color.YELLOW");
      out.println("		content: [");
      out.println("         " + name + "Icon{");
      out.println("            windowColorScheme: windowColorScheme");
      out.println("           layoutX: 25");
      out.println("            layoutY: 25");
      out.println("         }");
      out.println("         " + name + "Icon{");
      out.println("            windowColorScheme: windowColorScheme");
      out.println("            layoutX: 75");
      out.println("            layoutY: 25");
      out.println("            selected: true");
      out.println("         }");

      out.println("      ]");
      out.println("	}");
      out.println("}");
      out.println("}");
   }
}
