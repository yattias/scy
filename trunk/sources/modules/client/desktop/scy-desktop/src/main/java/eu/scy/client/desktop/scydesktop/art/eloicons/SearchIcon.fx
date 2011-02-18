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
public class SearchIcon extends AbstractEloIcon {

public override function clone(): SearchIcon {
SearchIcon {
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
					content: "M35.47,4.97 C32.92,0.83 27.96,1.78 23.86,2.93 C23.72,2.85 23.53,2.79 23.25,2.83 C19.06,3.41 15.07,4.79 12.83,8.58 C11.63,10.62 11.45,14.67 11.35,16.97 C11.30,17.98 11.70,18.83 12.05,19.73 C13.41,23.19 8.08,25.74 6.07,26.99 C2.09,29.47 0.40,32.49 2.17,37.02 C3.57,40.58 8.50,37.12 10.32,35.86 C12.85,34.12 14.99,31.79 17.32,29.80 C19.29,28.12 23.91,30.61 26.47,30.51 C31.22,30.32 36.48,25.20 37.82,20.91 C39.41,15.83 38.22,9.41 35.47,4.97 Z M30.32,27.34 C24.23,30.20 23.71,26.00 18.79,27.31 C15.86,28.10 13.58,31.01 11.42,32.97 C10.07,34.19 8.63,35.25 7.05,36.17 C4.71,36.22 3.53,35.33 3.51,33.51 C3.34,31.40 4.17,29.85 5.98,28.87 C8.46,26.88 11.29,25.66 13.56,23.37 C15.14,21.78 14.18,19.72 13.51,18.01 C12.69,15.92 14.00,12.98 14.28,10.89 C14.76,7.26 17.68,5.55 20.86,4.75 C21.02,5.01 21.36,5.17 21.86,5.03 C23.47,4.56 25.17,3.62 26.80,4.02 C29.86,4.77 34.72,6.89 35.86,11.02 C37.73,17.81 36.55,24.41 30.32,27.34 Z "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: null
					content: "M34.55,15.92 C34.55,21.15 30.31,25.39 25.08,25.39 C19.85,25.39 15.61,21.15 15.61,15.92 C15.61,10.69 19.85,6.45 25.08,6.45 C30.31,6.45 34.55,10.69 34.55,15.92 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M36.19,20.26 C38.37,13.98 35.03,7.11 28.75,4.93 C27.45,4.48 26.12,4.27 24.81,4.27 C19.82,4.27 15.15,7.40 13.42,12.38 C11.25,18.67 14.58,25.53 20.87,27.71 C22.17,28.16 23.50,28.37 24.81,28.37 C29.80,28.37 34.47,25.24 36.19,20.26 Z M24.81,25.11 C23.84,25.11 22.87,24.94 21.94,24.62 C19.72,23.85 17.93,22.27 16.91,20.16 C15.89,18.05 15.74,15.67 16.51,13.45 C17.73,9.91 21.07,7.54 24.81,7.54 C25.79,7.54 26.75,7.70 27.68,8.02 L27.68,8.02 C29.90,8.79 31.69,10.38 32.71,12.48 C33.73,14.60 33.88,16.98 33.11,19.19 C31.88,22.73 28.55,25.11 24.81,25.11 Z "
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 6.14
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					startX: 18.5
					startY: 22.37
					endX: 2.42
					endY: 36.47
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
         SearchIcon{
            windowColorScheme: windowColorScheme
           layoutX: 25
            layoutY: 25
         }
         SearchIcon{
            windowColorScheme: windowColorScheme
            layoutX: 75
            layoutY: 25
            selected: true
         }
      ]
	}
}
}
