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
					content: "M38.43,33.15 C36.10,29.43 34.05,25.51 32.03,21.60 C29.86,17.38 27.81,13.10 25.75,8.83 C24.42,6.09 23.77,0.14 20.94,0.14 C19.96,0.14 17.52,0.14 17.41,0.94 C17.40,1.00 17.40,1.07 17.39,1.13 C12.26,12.45 5.70,23.11 0.65,34.41 C0.19,35.45 0.45,36.48 1.13,37.33 C2.49,39.02 4.49,38.65 6.43,38.50 C10.75,38.14 14.99,38.42 19.29,38.83 C23.17,39.20 27.02,39.47 30.92,39.57 C33.59,39.64 36.45,40.53 39.07,40.13 C39.79,40.01 40.43,39.13 40.53,38.43 C40.83,36.37 39.46,34.80 38.43,33.15 Z M22.58,34.33 C19.16,34.05 15.76,33.64 12.33,33.49 C11.66,33.46 8.06,33.93 5.83,34.03 C10.25,24.64 15.53,15.62 20.00,6.23 C20.81,7.97 21.45,9.96 22.09,11.28 C23.90,15.02 25.71,18.76 27.57,22.47 C29.16,25.64 30.83,28.77 32.56,31.86 C33.14,32.90 33.76,33.93 34.37,34.95 C33.65,34.88 32.93,34.82 32.20,34.78 C28.99,34.63 25.79,34.59 22.58,34.33 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.93
					strokeLineCap: StrokeLineCap.BUTT
					content: "M25.01,32.92 C25.01,34.21 24.57,35.27 23.70,36.09 C22.83,36.92 21.70,37.33 20.29,37.33 C18.91,37.33 17.78,36.91 16.90,36.08 C16.02,35.25 15.57,34.19 15.57,32.92 C15.57,31.62 16.02,30.56 16.90,29.74 C17.78,28.92 18.91,28.51 20.29,28.51 C21.67,28.51 22.81,28.92 23.69,29.74 C24.57,30.56 25.01,31.62 25.01,32.92 Z "
				},
				SVGPath {
					fill: Color.rgb(0xff,0x33,0x0)
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.93
					strokeLineCap: StrokeLineCap.BUTT
					content: "M25.03,32.81 C25.03,34.10 24.59,35.16 23.72,35.99 C22.84,36.81 21.71,37.22 20.31,37.22 C18.93,37.22 17.80,36.80 16.91,35.97 C16.03,35.14 15.59,34.09 15.59,32.81 C15.59,31.51 16.03,30.46 16.91,29.63 C17.80,28.81 18.93,28.40 20.31,28.40 C21.69,28.40 22.82,28.81 23.71,29.63 C24.59,30.46 25.03,31.51 25.03,32.81 Z "
				},
				SVGPath {
					fill: Color.rgb(0xff,0x33,0x0)
					stroke: null
					content: "M20.14,26.37 C19.16,26.37 18.50,26.08 18.14,25.47 C17.73,24.79 17.43,23.70 17.25,22.24 L16.23,10.47 C16.04,8.19 15.94,6.54 15.94,5.56 C15.94,4.33 16.34,3.40 17.16,2.72 C18.00,2.03 19.13,1.68 20.52,1.68 C22.17,1.68 23.28,2.10 23.80,2.93 C24.37,3.83 24.66,5.17 24.66,6.91 C24.66,7.97 24.58,9.06 24.44,10.15 L23.06,22.30 C22.92,23.67 22.62,24.72 22.18,25.43 C21.77,26.06 21.11,26.37 20.14,26.37 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M20.52,2.14 C22.01,2.14 22.98,2.49 23.42,3.18 C23.93,4.00 24.20,5.26 24.20,6.91 C24.20,7.95 24.13,9.02 23.98,10.11 L22.61,22.25 C22.47,23.55 22.20,24.54 21.79,25.18 C21.59,25.50 21.20,25.91 20.14,25.91 C19.06,25.91 18.71,25.53 18.54,25.24 C18.16,24.61 17.88,23.58 17.71,22.22 L16.69,10.43 C16.50,8.17 16.40,6.53 16.40,5.56 C16.40,4.47 16.75,3.66 17.46,3.07 C18.22,2.44 19.22,2.14 20.52,2.14 M20.52,1.22 C19.01,1.22 17.80,1.61 16.87,2.37 C15.95,3.13 15.49,4.19 15.49,5.56 C15.49,6.56 15.58,8.21 15.77,10.51 L16.79,22.30 C16.99,23.83 17.30,24.96 17.75,25.71 C18.20,26.46 18.99,26.83 20.14,26.83 C21.27,26.83 22.07,26.44 22.56,25.67 C23.05,24.90 23.37,23.79 23.52,22.35 L24.89,10.21 C25.04,9.09 25.11,7.99 25.11,6.91 C25.11,5.07 24.80,3.67 24.19,2.69 C23.57,1.71 22.35,1.22 20.52,1.22 Z "
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
