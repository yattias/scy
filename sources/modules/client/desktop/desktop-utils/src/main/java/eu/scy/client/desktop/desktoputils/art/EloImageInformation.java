/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.desktoputils.art;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author sikken
 */
public enum EloImageInformation
{
   conceptMap("scy/mapping","concept_map",ScyColors.blue),
   dataset("scy/dataset","dataset",ScyColors.green),
   datasetProcessing("scy/pds","dataset_processing",ScyColors.green),
   document("scy/doc","document",ScyColors.pink),
   drawing("scy/drawing","drawing",ScyColors.blue),
   googleSketchup("scy/skp","drawing",ScyColors.blue),
   image("scy/image","info",ScyColors.magenta),
   interview("scy/interview","interview",ScyColors.orange),
   melo("scy/melo","info",ScyColors.magenta),
   modelEditor("scy/model","model_editor",ScyColors.blue),
   presentation("scy/ppt","presentation",ScyColors.pink),
   richText("scy/rtf","document",ScyColors.pink),
   simulationConfig("scy/simconfig","simulation",ScyColors.orange),
   studentPlanning("scy/studentplanningtool","planning",ScyColors.green),
   text("scy/text","document",ScyColors.pink),
   url("scy/url","info",ScyColors.magenta),
   video("scy/video","info",ScyColors.magenta),
   xproc("scy/xproc","exp_design",ScyColors.orange),
   assignment("special/assignment","assignment",ScyColors.brown),
   generalNew("general/new","new",ScyColors.darkBlue),
   generalSearch("general/search","search",ScyColors.darkBlue),
   generalNavigation("general/navigation","search",ScyColors.darkBlue),
   generalLogo("general/logo","logo_gray",ScyColors.darkGray),
   conceptMap2("ard/concept_map","concept_map2",ScyColors.blue),
   assignment2("ard/assignment","assignment2",ScyColors.magenta),
   choices2("ard/choices","choices2",ScyColors.blue),
   conclusion2("ard/conclusion","conclusion2",ScyColors.pink),
   drawing2("ard/drawing","drawing2",ScyColors.blue),
   house2("ard/house","house2",ScyColors.orange),
   hypothese2("ard/hypothese","hypothese2",ScyColors.pink),
   info2("ard/info","info2",ScyColors.magenta),
   idea2("ard/idea","idea2",ScyColors.blue),
   presentation2("ard/presentation","presentation2",ScyColors.pink),
   report2("ard/report","report2",ScyColors.pink);

   private static final String defaultIconName = "logo_gray";
   private static final ScyColors defaultScyColors = ScyColors.darkGray;
   
   public final String type;
   public final String iconName;
   public final ScyColors scyColors;
   private final static Map<String,String> typeNamesMap = new HashMap<String,String>();
   private final static Map<String,ScyColors> colorStringsMap = new HashMap<String,ScyColors>();

   static{
      for (EloImageInformation technicalType : EloImageInformation.values()){
         typeNamesMap.put(technicalType.type, technicalType.iconName);
         colorStringsMap.put(technicalType.type, technicalType.scyColors);
      }
   }

   private EloImageInformation(String type,String iconName,ScyColors scyColors)
   {
      this.type = type;
      this.iconName = iconName;
      this.scyColors = scyColors;
   }

   public static String getIconName(String type){
      final String name = typeNamesMap.get(type);
      if (name!=null){
         return name;
      }
      return defaultIconName;
   }
   
   public static ScyColors getScyColors(String type){
      final ScyColors scyColors = colorStringsMap.get(type);
      if (scyColors!=null){
         return scyColors;
      }
      return defaultScyColors;
   }
}
