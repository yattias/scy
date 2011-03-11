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
public class Alert_messageIcon extends AbstractEloIcon {

public override function clone(): Alert_messageIcon {
Alert_messageIcon {
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
					content: "M38.35,33.21 C36.02,29.48 33.97,25.56 31.95,21.65 C29.78,17.44 27.74,13.15 25.67,8.88 C24.35,6.14 23.69,0.19 20.86,0.19 C19.88,0.19 17.44,0.19 17.33,0.99 C17.32,1.06 17.32,1.12 17.31,1.18 C12.19,12.50 5.63,23.17 0.58,34.46 C0.11,35.50 0.38,36.53 1.06,37.38 C2.41,39.07 4.41,38.71 6.36,38.55 C10.67,38.20 14.92,38.47 19.21,38.88 C23.09,39.25 26.95,39.52 30.84,39.62 C33.51,39.69 36.37,40.58 38.99,40.18 C39.71,40.07 40.36,39.18 40.46,38.48 C40.75,36.42 39.38,34.85 38.35,33.21 Z M22.50,34.38 C19.08,34.10 15.68,33.69 12.25,33.54 C11.58,33.52 7.98,33.98 5.75,34.08 C10.17,24.69 15.46,15.68 19.92,6.28 C20.73,8.02 21.38,10.01 22.02,11.33 C23.83,15.07 25.63,18.81 27.50,22.52 C29.09,25.69 30.76,28.82 32.48,31.91 C33.07,32.96 33.68,33.98 34.30,35.00 C33.58,34.93 32.85,34.87 32.12,34.84 C28.91,34.68 25.71,34.64 22.50,34.38 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.93
					strokeLineCap: StrokeLineCap.BUTT
					content: "M24.93,32.97 C24.93,34.26 24.50,35.32 23.63,36.15 C22.75,36.97 21.62,37.38 20.22,37.38 C18.83,37.38 17.70,36.96 16.82,36.13 C15.94,35.30 15.50,34.24 15.50,32.97 C15.50,31.68 15.94,30.62 16.82,29.79 C17.70,28.97 18.83,28.56 20.22,28.56 C21.60,28.56 22.73,28.97 23.61,29.79 C24.49,30.62 24.93,31.68 24.93,32.97 Z "
				},
				SVGPath {
					fill: Color.rgb(0xff,0x33,0x0)
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.93
					strokeLineCap: StrokeLineCap.BUTT
					content: "M24.95,32.86 C24.95,34.15 24.51,35.22 23.64,36.04 C22.77,36.86 21.63,37.27 20.23,37.27 C18.85,37.27 17.72,36.86 16.84,36.03 C15.96,35.19 15.51,34.14 15.51,32.86 C15.51,31.57 15.96,30.51 16.84,29.68 C17.72,28.86 18.85,28.46 20.23,28.46 C21.61,28.46 22.75,28.86 23.63,29.68 C24.51,30.51 24.95,31.57 24.95,32.86 Z "
				},
				SVGPath {
					fill: Color.rgb(0xff,0x33,0x0)
					stroke: null
					content: "M20.06,26.42 C19.08,26.42 18.43,26.13 18.07,25.53 C17.65,24.84 17.35,23.75 17.17,22.30 L16.15,10.52 C15.96,8.25 15.87,6.59 15.87,5.61 C15.87,4.38 16.27,3.45 17.09,2.77 C17.92,2.08 19.05,1.73 20.45,1.73 C22.10,1.73 23.20,2.16 23.72,2.99 C24.29,3.88 24.58,5.22 24.58,6.96 C24.58,8.02 24.50,9.11 24.36,10.20 L22.99,22.35 C22.84,23.72 22.55,24.77 22.10,25.48 C21.70,26.12 21.03,26.42 20.06,26.42 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M20.45,2.19 C21.93,2.19 22.90,2.54 23.34,3.23 C23.86,4.05 24.12,5.31 24.12,6.96 C24.12,8.00 24.05,9.07 23.90,10.16 L22.53,22.31 C22.40,23.60 22.12,24.59 21.71,25.24 C21.51,25.55 21.13,25.97 20.06,25.97 C18.99,25.97 18.63,25.58 18.46,25.29 C18.08,24.66 17.80,23.63 17.63,22.27 L16.61,10.48 C16.42,8.22 16.32,6.58 16.32,5.61 C16.32,4.52 16.67,3.71 17.38,3.13 C18.14,2.50 19.14,2.19 20.45,2.19 M20.45,1.28 C18.94,1.28 17.72,1.66 16.80,2.42 C15.87,3.18 15.41,4.25 15.41,5.61 C15.41,6.61 15.51,8.26 15.70,10.56 L16.72,22.35 C16.91,23.88 17.23,25.02 17.67,25.76 C18.12,26.51 18.92,26.88 20.06,26.88 C21.19,26.88 22.00,26.50 22.49,25.72 C22.97,24.95 23.29,23.85 23.44,22.40 L24.81,10.26 C24.96,9.15 25.04,8.05 25.04,6.96 C25.04,5.13 24.73,3.72 24.11,2.74 C23.50,1.77 22.27,1.28 20.45,1.28 Z "
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
         Alert_messageIcon{
            windowColorScheme: windowColorScheme
           layoutX: 25
            layoutY: 25
         }
         Alert_messageIcon{
            windowColorScheme: windowColorScheme
            layoutX: 75
            layoutY: 25
            selected: true
         }
      ]
	}
}
}
