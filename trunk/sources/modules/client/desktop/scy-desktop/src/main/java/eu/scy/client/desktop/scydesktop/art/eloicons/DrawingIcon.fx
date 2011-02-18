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
					content: "M31.68,18.66 C30.04,22.33 29.10,26.29 27.52,30.00 C26.80,31.81 26.61,31.94 26.95,30.40 C26.25,29.71 25.61,28.96 25.03,28.14 C23.51,26.20 22.45,23.85 21.46,21.55 C18.11,13.79 14.94,7.89 7.92,3.68 C5.57,2.27 -1.51,6.01 1.80,8.00 C7.36,11.33 10.52,15.90 13.07,22.24 C14.93,26.87 17.28,32.59 21.41,35.23 C24.56,37.24 28.57,37.42 31.52,34.76 C35.86,30.87 36.54,24.28 38.91,18.98 C40.63,15.13 32.87,15.99 31.68,18.66 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M2.93,10.19 C6.90,11.29 9.80,17.50 11.88,20.61 C14.80,24.99 17.83,29.95 22.71,32.35 C26.53,34.23 31.57,32.34 34.50,29.69 C38.55,26.02 37.73,18.44 37.73,13.46 C37.74,12.06 31.73,13.06 31.73,15.07 C31.73,19.12 32.45,25.30 30.06,28.82 C27.92,31.94 24.27,27.50 21.97,24.70 C19.15,21.27 16.88,17.40 14.34,13.75 C12.70,11.39 10.72,8.41 7.79,7.59 C6.21,7.16 0.83,9.60 2.93,10.19 Z "
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
