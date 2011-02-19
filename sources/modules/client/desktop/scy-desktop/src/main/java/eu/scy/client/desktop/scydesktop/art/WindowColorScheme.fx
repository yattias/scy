/*
 * WindowColorScheme.fx
 *
 * Created on 9-apr-2010, 13:43:48
 */

package eu.scy.client.desktop.scydesktop.art;
import javafx.scene.paint.Color;
import eu.scy.common.scyelo.ColorSchemeId;
import eu.scy.common.mission.ColorScheme;
import javafx.util.Math;

/**
 * @author sikken
 */

public class WindowColorScheme {
   public var colorSchemeId: ColorSchemeId;
   public var mainColor:Color;
   public var mainColorLight:Color;
   public var secondColor:Color;
   public var secondColorLight:Color;
   public var thirdColor:Color;
   public var thirdColorLight:Color;
   public var backgroundColor:Color;
   public var emptyBackgroundColor:Color;

   public function assign(windowColorScheme: WindowColorScheme):Void{
      mainColor = windowColorScheme.mainColor;
      mainColorLight = windowColorScheme.mainColorLight;
      secondColor = windowColorScheme.secondColor;
      secondColorLight = windowColorScheme.secondColorLight;
      thirdColor = windowColorScheme.thirdColor;
      thirdColorLight = windowColorScheme.thirdColorLight;
      backgroundColor = windowColorScheme.backgroundColor;
      emptyBackgroundColor = windowColorScheme.emptyBackgroundColor;
   }

   public override function toString():String{
      "mainColor: {mainColor}, secondColor: {secondColor}, backgroundColor: {backgroundColor}"
   }

   public function getColorScheme():ColorScheme{
      def colorScheme = new ColorScheme();
      colorScheme.setColorSchemeId(colorSchemeId);
      colorScheme.setMainColor(fxColorToAwtColor(mainColor));
      colorScheme.setMainColorLight(fxColorToAwtColor(mainColorLight));
      colorScheme.setSecondColor(fxColorToAwtColor(secondColor));
      colorScheme.setSecondColorLight(fxColorToAwtColor(secondColorLight));
      colorScheme.setThirdColor(fxColorToAwtColor(thirdColor));
      colorScheme.setThirdColorLight(fxColorToAwtColor(thirdColorLight));
      colorScheme.setBackgroundColor(fxColorToAwtColor(backgroundColor));
      colorScheme.setEmptyBackgroundColor(fxColorToAwtColor(emptyBackgroundColor));
      return colorScheme;
   }

   public function setColorScheme(colorScheme: ColorScheme):Void{
      colorSchemeId = colorScheme.getColorSchemeId();
      mainColor = awtColorToFxColor(colorScheme.getMainColor());
      mainColorLight = awtColorToFxColor(colorScheme.getMainColorLight());
      secondColor = awtColorToFxColor(colorScheme.getSecondColor());
      secondColorLight = awtColorToFxColor(colorScheme.getSecondColorLight());
      thirdColor = awtColorToFxColor(colorScheme.getThirdColor());
      thirdColorLight = awtColorToFxColor(colorScheme.getThirdColorLight());
      backgroundColor = awtColorToFxColor(colorScheme.getBackgroundColor());
      emptyBackgroundColor = awtColorToFxColor(colorScheme.getEmptyBackgroundColor());
   }

   function fxColorToAwtColor(color:Color):java.awt.Color{
      new java.awt.Color(Math.round(255*color.red),Math.round(255*color.green),Math.round(255*color.blue),Math.round(255*color.opacity))
   }

   function awtColorToFxColor(color:java.awt.Color):Color{
      Color.rgb(color.getRed(), color.getGreen(),color.getBlue(),color.getAlpha()/255.0)
   }
}

public function getWindowColorScheme(scyColors:ScyColors):WindowColorScheme{
   WindowColorScheme{
      mainColor: Color.web(scyColors.mainColorName)
      mainColorLight: Color.web(scyColors.mainColorLightName)
      secondColor: Color.web(scyColors.secondColorName)
      secondColorLight: Color.web(scyColors.secondColorLightName)
      thirdColor: Color.web(scyColors.thirdColorName)
      thirdColorLight: Color.web(scyColors.thirdColorLightName)
      backgroundColor: Color.web(scyColors.backgroundColorName)
      emptyBackgroundColor: Color.web(scyColors.emptyBackgroundColorName)
   }

}

