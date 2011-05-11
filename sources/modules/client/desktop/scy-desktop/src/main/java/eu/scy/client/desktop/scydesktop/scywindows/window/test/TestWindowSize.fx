/*
 * TestWindowSize.fx
 *
 * Created on 19-apr-2010, 14:38:27
 */

package eu.scy.client.desktop.scydesktop.scywindows.window.test;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import eu.scy.client.desktop.scydesktop.scywindows.window.StandardScyWindow;
import eu.scy.client.desktop.desktoputils.art.EloImageInformation;
import eu.scy.client.desktop.desktoputils.art.ImageLoader;
import eu.scy.client.desktop.desktoputils.art.WindowColorScheme;
import eu.scy.client.desktop.scydesktop.imagewindowstyler.ImageEloIcon;
import eu.scy.client.desktop.desktoputils.art.EloIcon;
import eu.scy.client.desktop.scydesktop.uicontrols.test.ruler.RulerRectangle;
import eu.scy.client.desktop.scydesktop.uicontrols.test.ruler.ResizableRulerRectangle;
import eu.scy.client.desktop.scydesktop.scywindows.window.MouseBlocker;
import eu.scy.client.desktop.scydesktop.scywindows.window.ProgressOverlay;
import eu.scy.client.desktop.desktoputils.art.ScyColors;

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

var windowColorScheme = WindowColorScheme.getWindowColorScheme(ScyColors.darkGray);

var window1 = StandardScyWindow{
   windowColorScheme:windowColorScheme
   eloIcon:loadEloIcon("scy/mapping")
   title:"Fixed size"
   scyContent:RulerRectangle {
      xSize: 150
      ySize: 150
   }
   topDrawerTool:RulerRectangle {
      xSize: 100
      ySize: 100
   }
   rightDrawerTool:RulerRectangle {
      xSize: 100
      ySize: 100
   }
   bottomDrawerTool:RulerRectangle {
      xSize: 100
      ySize: 100
   }
   leftDrawerTools:RulerRectangle {
      xSize: 100
      ySize: 100
   }
   layoutX:10
   layoutY:10
}
window1.open();

def preferredDrawerWidth = 125;
def preferredDrawerHeight = 150;
var window2 = StandardScyWindow{
   windowColorScheme:windowColorScheme
   eloIcon:loadEloIcon("scy/mapping")
   title:"Variable size"
   scyContent:ResizableRulerRectangle {
      preferredWidth:150
      preferredHeight:150
      minimumWidth:100
      minimumHeight:70
   }
   topDrawerTool:ResizableRulerRectangle{
      preferredWidth:preferredDrawerWidth
      preferredHeight:preferredDrawerHeight
      minimumWidth:100
      minimumHeight:70
   }
   rightDrawerTool:ResizableRulerRectangle{
      preferredWidth:preferredDrawerWidth
      preferredHeight:preferredDrawerHeight
      minimumWidth:100
      minimumHeight:70
   }
   bottomDrawerTool:ResizableRulerRectangle{
      preferredWidth:preferredDrawerWidth
      preferredHeight:preferredDrawerHeight
      minimumWidth:100
      minimumHeight:70
   }
   leftDrawerTools:ResizableRulerRectangle{
      preferredWidth:preferredDrawerWidth
      preferredHeight:preferredDrawerHeight
      minimumWidth:100
      minimumHeight:70
   }

   layoutX:210
   layoutY:10
}
window2.open();

var stage = Stage {
    title: "Test window size"
    scene: Scene {
        width: 400
        height: 400
        fill:Color.LIGHTGRAY
        content: [
            window1,
            window2
        ]
    }
}

MouseBlocker.initMouseBlocker(stage);
ProgressOverlay.initOverlay(stage);
