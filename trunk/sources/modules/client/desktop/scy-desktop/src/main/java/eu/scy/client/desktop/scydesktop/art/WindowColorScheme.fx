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
   public var backgroundColor:Color;
   public var titleStartGradientColor:Color;
   public var titleEndGradientColor:Color;
   public var emptyBackgroundColor:Color;
}

public function getWindowColorScheme(scyColors:ScyColors):WindowColorScheme{
   WindowColorScheme{
      mainColor: Color.web(scyColors.mainColorName)
      backgroundColor: Color.web(scyColors.backgroundColorName)
      titleStartGradientColor: Color.web(scyColors.titleStartGradientColorName)
      titleEndGradientColor: Color.web(scyColors.titleEndGradientColorName)
      emptyBackgroundColor: Color.web(scyColors.emptyBackgroundColorName)
   }

}

