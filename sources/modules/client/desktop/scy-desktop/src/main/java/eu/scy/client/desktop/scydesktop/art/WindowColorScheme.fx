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
   public var thirdColor:Color;
   public var thirdColorLight:Color;
   public var backgroundColor:Color;
   public var emptyBackgroundColor:Color;

   public function assign(windowColorScheme: WindowColorScheme):Void{
      mainColor = windowColorScheme.mainColor;
      mainColorlLight = windowColorScheme.mainColorlLight;
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


}

public function getWindowColorScheme(scyColors:ScyColors):WindowColorScheme{
   WindowColorScheme{
      mainColor: Color.web(scyColors.mainColorName)
      mainColorlLight: Color.web(scyColors.mainColorLightName)
      secondColor: Color.web(scyColors.secondColorName)
      secondColorLight: Color.web(scyColors.secondColorLightName)
      thirdColor: Color.web(scyColors.thirdColorName)
      thirdColorLight: Color.web(scyColors.thirdColorLightName)
      backgroundColor: Color.web(scyColors.backgroundColorName)
      emptyBackgroundColor: Color.web(scyColors.emptyBackgroundColorName)
   }

}

