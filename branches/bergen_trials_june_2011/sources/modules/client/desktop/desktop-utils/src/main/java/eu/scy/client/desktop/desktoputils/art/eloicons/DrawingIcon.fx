package eu.scy.client.desktop.desktoputils.art.eloicons;

import javafx.scene.Scene;
import javafx.stage.Stage;
import eu.scy.client.desktop.desktoputils.art.ScyColors;
import javafx.scene.Node;
import javafx.scene.Group;
import javafx.scene.shape.*;
import javafx.scene.paint.Color;
import javafx.scene.transform.Transform;
import eu.scy.client.desktop.desktoputils.art.WindowColorScheme;
/**
 * @author lars
 */
public class DrawingIcon extends AbstractEloIcon {

public override function clone(): DrawingIcon {
DrawingIcon {
selected: selected
size: size
windowColorScheme: windowColorScheme
}
}

public override function createNode(): Node {

return Group {

			content: [
				SVGPath {
					fill: bind windowColorScheme.mainColorLight
					stroke: null
					content: "M32.06,18.74 C30.41,22.42 29.47,26.38 27.90,30.08 C27.17,31.89 26.98,32.02 27.33,30.48 C26.63,29.79 25.98,29.04 25.40,28.22 C23.89,26.28 22.82,23.93 21.83,21.63 C18.48,13.87 15.32,7.98 8.29,3.76 C5.94,2.35 -1.14,6.09 2.17,8.08 C7.73,11.41 10.89,15.99 13.44,22.33 C15.30,26.95 17.66,32.67 21.79,35.31 C24.94,37.32 28.94,37.50 31.90,34.85 C36.24,30.95 36.92,24.36 39.28,19.07 C41.01,15.21 33.25,16.08 32.06,18.74 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M3.30,10.27 C7.27,11.38 10.17,17.58 12.25,20.70 C15.18,25.07 18.21,30.03 23.08,32.43 C26.91,34.32 31.95,32.43 34.88,29.78 C38.93,26.11 38.10,18.53 38.11,13.54 C38.11,12.15 32.11,13.14 32.11,15.15 C32.10,19.21 32.83,25.39 30.43,28.90 C28.30,32.03 24.65,27.59 22.34,24.78 C19.52,21.35 17.25,17.48 14.72,13.84 C13.08,11.48 11.10,8.49 8.16,7.68 C6.59,7.24 1.20,9.69 3.30,10.27 Z "
},
]
}
}
}
function run(){
   def windowColorScheme = WindowColorScheme.getWindowColorScheme(ScyColors.darkGray);
   Stage {
	title: 'MyApp'
	onClose: function () {  }
	scene: Scene {
		width: 200
		height: 200
      fill: Color.YELLOW
		content: [
         DrawingIcon{
            windowColorScheme: windowColorScheme
           layoutX: 25
            layoutY: 25
         }
         DrawingIcon{
            windowColorScheme: windowColorScheme
            layoutX: 75
            layoutY: 25
            selected: true
         }
      ]
	}
}
}
