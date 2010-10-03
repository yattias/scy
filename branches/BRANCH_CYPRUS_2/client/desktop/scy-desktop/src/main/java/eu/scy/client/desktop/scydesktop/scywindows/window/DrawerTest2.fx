/*
 * DrawerTest2.fx
 *
 * Created on 14-apr-2010, 9:37:46
 */

package eu.scy.client.desktop.scydesktop.scywindows.window;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import eu.scy.client.desktop.scydesktop.art.EloImageInformation;
import eu.scy.client.desktop.scydesktop.art.ImageLoader;
import eu.scy.client.desktop.scydesktop.art.WindowColorScheme;
import eu.scy.client.desktop.scydesktop.imagewindowstyler.ImageEloIcon;
import eu.scy.client.desktop.scydesktop.scywindows.EloIcon;
import eu.scy.client.desktop.scydesktop.uicontrols.test.ruler.ResizableRulerRectangle;
import eu.scy.client.desktop.scydesktop.uicontrols.test.ruler.RulerRectangle;

/**
 * @author sikken
 */

var imageLoader = ImageLoader.getImageLoader();

function loadEloIcon(type: String):EloIcon{
   var name = EloImageInformation.getIconName(type);
   ImageEloIcon{
      activeImage:imageLoader.getImage("{name}_act.png")
      inactiveImage:imageLoader.getImage("{name}_inact.png")
   }
}

var highcontrastColorScheme = WindowColorScheme{
   mainColor:Color.DARKBLUE
   backgroundColor:Color.ORANGE
   titleStartGradientColor:Color.LIGHTBLUE
   titleEndGradientColor:Color.WHITE
   emptyBackgroundColor:Color.WHITE
}
var windowColorScheme = WindowColorScheme{
   mainColor:Color.web("#0042f1")
   backgroundColor:Color.web("#f0f8db")
   titleStartGradientColor:Color.web("#4080f8")
   titleEndGradientColor:Color.WHITE
   emptyBackgroundColor:Color.WHITE
}

windowColorScheme= highcontrastColorScheme;

var openWindow = CombinedWindowElements{
   windowColorScheme:windowColorScheme
   eloIcon:loadEloIcon("scy/mapping")
   title:"Drawer test"
   allowClose:false
   scyContent:ResizableRulerRectangle {
      preferredWidth: 150, preferredHeight: 150
   }
   topDrawerTool:RulerRectangle {
      xSize:100
      ySize:100
   }
   rightDrawerTool:RulerRectangle {
      xSize:120
      ySize:100
   }
   bottomDrawerTool:ResizableRulerRectangle {
      preferredWidth: 100, preferredHeight: 120
   }
   leftDrawerTool:ResizableRulerRectangle {
      preferredWidth: 100, preferredHeight: 100
   }
   layoutX: 150
   layoutY: 150
}

openWindow.openWindow(150,120);


Stage {
    title: "Drawer test"
    scene: Scene {
        width: 400
        height: 400
        content: [
            openWindow
        ]
    }
}