package eu.scy.client.desktop.scydesktop.art.eloicons;

import javafx.scene.Scene;
import javafx.stage.Stage;
import eu.scy.client.desktop.scydesktop.art.ScyColors;
import javafx.scene.Node;
import javafx.scene.Group;
import javafx.scene.shape.*;
import javafx.scene.paint.Color;
import javafx.scene.transform.Transform;
import eu.scy.client.desktop.scydesktop.art.WindowColorScheme;
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
					content: "M31.87,18.65 C30.23,22.32 29.29,26.28 27.71,29.99 C26.98,31.80 26.79,31.93 27.14,30.38 C26.44,29.69 25.80,28.94 25.21,28.13 C23.70,26.19 22.63,23.84 21.64,21.54 C18.30,13.77 15.13,7.88 8.10,3.67 C5.75,2.26 -1.33,5.99 1.99,7.98 C7.55,11.32 10.71,15.89 13.26,22.23 C15.12,26.85 17.47,32.57 21.60,35.21 C24.75,37.23 28.75,37.40 31.71,34.75 C36.05,30.85 36.73,24.27 39.10,18.97 C40.82,15.12 33.06,15.98 31.87,18.65 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M3.11,10.17 C7.09,11.28 9.98,17.49 12.06,20.60 C14.99,24.98 18.02,29.94 22.90,32.34 C26.72,34.22 31.76,32.33 34.69,29.68 C38.74,26.01 37.92,18.43 37.92,13.45 C37.92,12.05 31.92,13.04 31.92,15.06 C31.91,19.11 32.64,25.29 30.24,28.80 C28.11,31.93 24.46,27.49 22.16,24.69 C19.33,21.25 17.06,17.38 14.53,13.74 C12.89,11.38 10.91,8.40 7.98,7.58 C6.40,7.14 1.02,9.59 3.11,10.17 Z "
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
