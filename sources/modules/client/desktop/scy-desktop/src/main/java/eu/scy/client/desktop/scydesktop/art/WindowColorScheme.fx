/*
 * WindowColorScheme.fx
 *
 * Created on 9-apr-2010, 13:43:48
 */

package eu.scy.client.desktop.scydesktop.art;
import javafx.scene.paint.Color;

/**
 * @author sikken
 */

public class WindowColorScheme {
   public var mainColor:Color;
   public var mainColorlLight:Color;
   public var secondColor:Color;
   public var secondColorLight:Color;
   public var backgroundColor:Color;
   public var emptyBackgroundColor:Color;

//   public var titleStartGradientColor:Color;
//   public var titleEndGradientColor:Color;

   public function assign(windowColorScheme: WindowColorScheme):Void{
      mainColor = windowColorScheme.mainColor;
      mainColorlLight = windowColorScheme.mainColorlLight;
      secondColor = windowColorScheme.secondColor;
      secondColorLight = windowColorScheme.secondColorLight;
      backgroundColor = windowColorScheme.backgroundColor;
      emptyBackgroundColor = windowColorScheme.emptyBackgroundColor;

//      titleStartGradientColor = windowColorScheme.titleStartGradientColor;
//      titleEndGradientColor = windowColorScheme.titleEndGradientColor;
   }

   public override function toString():String{
      "mainColor: {mainColor}, secondColor: {secondColor}, backgroundColor: {backgroundColor}"
   }


}

public function getWindowColorScheme(scyColors:ScyColors):WindowColorScheme{
   WindowColorScheme{
      mainColor: Color.web(scyColors.mainColorName)
      mainColorlLight: Color.web(scyColors.mainColorLightName)
      secondColor: Color.web(scyColors.secondColorName)
      secondColorLight: Color.web(scyColors.secondColorLightName)
      backgroundColor: Color.web(scyColors.backgroundColorName)
//      titleStartGradientColor: Color.web(scyColors.titleStartGradientColorName)
//      titleEndGradientColor: Color.web(scyColors.titleEndGradientColorName)
      emptyBackgroundColor: Color.web(scyColors.emptyBackgroundColorName)
   }

}

