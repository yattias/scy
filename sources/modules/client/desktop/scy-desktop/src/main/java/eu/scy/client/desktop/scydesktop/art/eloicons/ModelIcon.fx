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
public class ModelIcon extends AbstractEloIcon {

public override function clone(): ModelIcon {
ModelIcon {
selected: selected
size: size
windowColorScheme: windowColorScheme
}
}

public override function createNode(): Node {

return Group {

			content: [
				SVGPath {
					fill: bind windowColorScheme.secondColorLight
					stroke: null
					content: "M39.92,28.13 C39.73,27.16 39.10,26.53 38.31,26.19 C39.23,20.16 40.52,14.71 37.00,8.87 C33.80,3.56 27.72,1.27 20.97,3.12 C15.33,4.67 9.83,8.29 8.69,13.48 C7.95,16.82 7.33,18.59 4.16,20.77 C1.64,22.50 0.77,25.05 0.46,27.67 C0.11,30.62 1.78,37.81 7.29,35.28 C9.93,34.06 12.08,32.17 14.29,30.45 C17.03,28.31 16.98,34.02 18.50,35.37 C20.41,37.08 22.55,37.96 25.30,38.36 C28.01,38.77 31.96,39.48 34.71,38.96 C41.20,37.72 40.83,32.69 39.92,28.13 Z M33.37,33.42 C31.42,33.48 29.22,33.16 27.28,32.97 C22.18,32.48 23.38,28.14 20.76,25.36 C18.76,23.23 15.23,23.67 12.70,24.61 C11.44,25.08 10.28,25.95 9.28,26.73 C8.53,27.21 7.83,27.73 7.15,28.28 C7.19,26.79 7.62,25.62 9.33,24.45 C10.93,23.35 12.29,22.30 13.49,20.87 C15.94,17.96 14.20,14.08 17.39,11.23 C18.72,10.05 20.46,9.33 22.18,8.66 C26.64,6.91 29.78,9.23 31.71,12.44 C34.53,17.10 32.01,22.93 31.41,27.81 C31.22,29.40 32.39,30.38 33.80,30.68 C34.08,31.92 34.26,33.39 33.37,33.42 Z "
				},
				Polyline {
					fill: Color.rgb(0x6a,0xb3,0x30)
					stroke: null
					points: [5.93,23.09]
				},
				SVGPath {
					fill: Color.WHITE
					stroke: null
					content: "M35.33,14.89 C35.33,20.11 31.10,24.33 25.88,24.33 C20.67,24.33 16.44,20.11 16.44,14.89 C16.44,9.68 20.67,5.45 25.88,5.45 C31.10,5.45 35.33,9.68 35.33,14.89 Z "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: null
					content: "M15.68,14.15 C15.68,8.57 20.20,4.05 25.78,4.05 C31.36,4.05 35.88,8.57 35.88,14.15 "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M35.71,12.69 C35.71,19.13 31.26,24.34 25.78,24.34 C20.30,24.34 15.85,19.13 15.85,12.69 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 2.13
					content: "M32.06,25.19 C33.46,29.14 34.72,32.89 34.72,33.66 C34.72,35.45 33.69,36.12 32.02,36.12 C30.30,36.12 21.58,36.12 20.46,36.07 C19.45,36.03 17.61,35.29 17.61,33.70 C17.61,31.92 19.11,27.35 19.86,25.18 "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 2.0
					content: "M31.79,24.25 C30.22,19.60 28.48,14.66 28.33,13.88 Q28.06,12.44 28.33,9.14 L23.41,9.14 Q23.79,12.88 23.55,14.02 Q23.28,15.27 20.49,23.07 Q20.34,23.53 20.10,24.24 Z "
				},
				SVGPath {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.88
					strokeLineCap: StrokeLineCap.BUTT
					content: "M5.52,22.93 L16.18,16.34 C15.96,15.51 15.84,14.64 15.84,13.74 C15.84,8.26 20.28,3.81 25.77,3.81 C31.25,3.81 35.69,8.26 35.69,13.74 C35.69,19.22 31.25,23.67 25.77,23.67 C22.56,23.67 19.72,22.15 17.90,19.80 L7.44,25.91 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M17.46,16.47 Q5.86,22.59 6.02,23.22 C6.08,23.46 7.31,25.46 7.60,25.55 C7.60,25.55 7.61,25.55 7.62,25.55 Q8.40,25.55 18.63,18.88 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: null
					content: "M7.05,35.70 C5.23,35.70 4.07,33.89 4.67,31.90 C4.87,31.26 5.49,30.70 5.86,30.11 Q6.55,29.03 7.05,28.10 Q7.66,29.13 8.31,30.11 C8.73,30.75 9.20,31.20 9.41,31.90 C10.01,33.90 9.26,35.70 7.05,35.70 Z "
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
         ModelIcon{
            windowColorScheme: windowColorScheme
           layoutX: 25
            layoutY: 25
         }
         ModelIcon{
            windowColorScheme: windowColorScheme
            layoutX: 75
            layoutY: 25
            selected: true
         }
      ]
	}
}
}
