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
public class Alert_questionIcon extends AbstractEloIcon {

public override function clone(): Alert_questionIcon {
Alert_questionIcon {
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
					content: "M33.94,18.20 C35.61,14.57 35.51,10.06 34.10,6.41 C30.59,-2.74 18.98,0.99 12.23,0.98 C10.68,0.98 9.79,1.89 9.45,3.07 C4.58,5.13 1.38,11.60 3.69,16.98 C4.62,19.13 7.12,20.22 9.21,19.97 C10.84,19.77 12.35,19.26 13.89,18.66 C14.33,18.49 15.44,17.76 16.52,17.12 C16.15,18.49 15.63,19.85 15.30,21.09 C14.62,23.63 14.41,25.95 14.43,28.58 C14.45,31.21 14.91,37.14 17.01,38.96 C19.31,40.95 23.56,40.11 25.99,38.77 C28.87,37.19 30.93,33.35 30.30,29.66 C30.01,27.93 28.83,25.87 29.90,24.24 C31.29,22.13 32.85,20.57 33.94,18.20 Z M24.41,19.49 C22.65,21.55 21.78,24.88 22.52,27.59 C22.97,29.22 22.38,22.48 23.44,17.81 C24.02,15.25 24.69,11.59 22.45,9.64 C20.24,7.73 21.41,7.61 25.04,7.95 C31.58,8.57 26.52,17.04 24.41,19.49 Z "
				},
				SVGPath {
					fill: Color.rgb(0xff,0x33,0x0)
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.99
					strokeLineCap: StrokeLineCap.BUTT
					content: "M23.36,25.71 L16.35,25.71 C16.32,24.63 16.30,24.01 16.30,23.86 C16.30,21.02 17.32,18.40 19.37,16.00 C20.69,14.43 21.55,13.26 21.92,12.49 C22.29,11.72 22.48,11.03 22.48,10.42 C22.48,8.31 21.21,7.26 18.69,7.26 C16.68,7.26 14.88,7.81 13.30,8.92 L11.49,3.02 C14.05,1.51 17.05,0.76 20.45,0.76 C23.61,0.76 26.09,1.54 27.87,3.08 C29.67,4.63 30.56,6.65 30.56,9.13 C30.56,10.38 30.24,11.70 29.61,13.09 C28.98,14.48 27.79,16.15 26.05,18.10 C24.25,20.05 23.36,22.28 23.36,24.81 Z "
				},
				SVGPath {
					fill: Color.rgb(0xff,0x33,0x0)
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.24
					strokeLineCap: StrokeLineCap.BUTT
					content: "M23.43,25.80 L16.43,25.80 C16.40,24.70 16.38,24.08 16.38,23.94 C16.38,21.10 17.40,18.48 19.44,16.08 C20.77,14.51 21.62,13.34 21.99,12.57 C22.36,11.80 22.55,11.10 22.55,10.50 C22.55,8.39 21.29,7.33 18.76,7.33 C16.76,7.33 14.96,7.89 13.37,8.99 L11.56,3.09 C14.13,1.59 17.12,0.84 20.53,0.84 C23.69,0.84 26.17,1.61 27.95,3.16 C29.74,4.71 30.64,6.73 30.64,9.21 C30.64,10.46 30.32,11.78 29.68,13.17 C29.06,14.56 27.87,16.22 26.13,18.17 C24.32,20.12 23.43,22.36 23.43,24.88 Z "
				},
				SVGPath {
					fill: Color.rgb(0xff,0x33,0x0)
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.93
					strokeLineCap: StrokeLineCap.BUTT
					content: "M24.83,32.87 C24.83,34.17 24.39,35.22 23.52,36.05 C22.65,36.88 21.52,37.28 20.11,37.28 C18.73,37.28 17.60,36.87 16.72,36.04 C15.84,35.20 15.39,34.15 15.39,32.87 C15.39,31.58 15.84,30.52 16.72,29.69 C17.60,28.87 18.73,28.46 20.11,28.46 C21.50,28.46 22.63,28.87 23.51,29.69 C24.39,30.52 24.83,31.58 24.83,32.87 Z "
				},
				SVGPath {
					fill: Color.rgb(0xff,0x33,0x0)
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.93
					strokeLineCap: StrokeLineCap.BUTT
					content: "M24.85,32.77 C24.85,34.06 24.41,35.12 23.54,35.94 C22.67,36.77 21.53,37.18 20.13,37.18 C18.75,37.18 17.62,36.76 16.73,35.93 C15.85,35.09 15.41,34.04 15.41,32.77 C15.41,31.47 15.85,30.41 16.73,29.58 C17.62,28.77 18.75,28.36 20.13,28.36 C21.51,28.36 22.64,28.77 23.53,29.58 C24.41,30.41 24.85,31.47 24.85,32.77 Z "
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
         Alert_questionIcon{
            windowColorScheme: windowColorScheme
           layoutX: 25
            layoutY: 25
         }
         Alert_questionIcon{
            windowColorScheme: windowColorScheme
            layoutX: 75
            layoutY: 25
            selected: true
         }
      ]
	}
}
}
