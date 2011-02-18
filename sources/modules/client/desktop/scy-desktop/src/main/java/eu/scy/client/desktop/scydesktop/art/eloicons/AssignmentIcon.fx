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
public class AssignmentIcon extends AbstractEloIcon {

public override function clone(): AssignmentIcon {
AssignmentIcon {
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
					content: "M33.44,18.24 C35.11,14.61 35.01,10.11 33.60,6.45 C30.09,-2.69 18.48,1.03 11.73,1.02 C10.18,1.02 9.29,1.93 8.95,3.12 C4.08,5.18 0.88,11.65 3.19,17.02 C4.12,19.17 6.62,20.26 8.71,20.01 C10.34,19.82 11.85,19.30 13.39,18.70 C13.83,18.53 14.94,17.80 16.02,17.17 C15.65,18.54 15.13,19.90 14.80,21.13 C14.12,23.67 13.91,26.00 13.93,28.62 C13.95,31.26 14.41,37.18 16.51,39.00 C18.81,40.99 23.06,40.15 25.49,38.82 C28.37,37.23 30.43,33.39 29.80,29.70 C29.51,27.97 28.33,25.91 29.40,24.28 C30.79,22.17 32.35,20.61 33.44,18.24 Z M23.91,19.54 C22.15,21.59 21.28,24.92 22.02,27.64 C22.47,29.27 21.88,22.52 22.94,17.85 C23.52,15.29 24.19,11.63 21.95,9.69 C19.74,7.77 17.17,8.70 14.83,9.65 C13.44,10.22 13.28,9.21 13.62,8.57 C17.27,8.44 20.91,7.66 24.54,8.00 C31.08,8.62 26.02,17.08 23.91,19.54 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.93
					strokeLineCap: StrokeLineCap.BUTT
					content: "M22.86,25.84 L15.85,25.84 C15.82,24.86 15.80,24.31 15.80,24.18 C15.80,21.64 16.82,19.30 18.87,17.16 C20.19,15.75 21.05,14.71 21.42,14.02 C21.79,13.33 21.98,12.71 21.98,12.17 C21.98,10.29 20.71,9.34 18.19,9.34 C16.18,9.34 14.39,9.84 12.80,10.83 L10.99,5.55 C13.56,4.21 16.55,3.54 19.95,3.54 C23.11,3.54 25.59,4.23 27.37,5.61 C29.17,6.99 30.06,8.80 30.06,11.02 C30.06,12.14 29.74,13.31 29.11,14.56 C28.48,15.79 27.29,17.29 25.55,19.03 C23.75,20.77 22.86,22.77 22.86,25.03 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.17
					strokeLineCap: StrokeLineCap.BUTT
					content: "M22.93,25.91 L15.93,25.91 C15.90,24.93 15.88,24.38 15.88,24.25 C15.88,21.71 16.90,19.37 18.94,17.23 C20.27,15.82 21.12,14.77 21.49,14.09 C21.86,13.40 22.05,12.78 22.05,12.24 C22.05,10.35 20.79,9.41 18.26,9.41 C16.26,9.41 14.46,9.90 12.87,10.89 L11.06,5.62 C13.63,4.28 16.62,3.61 20.03,3.61 C23.19,3.61 25.67,4.30 27.45,5.68 C29.24,7.06 30.14,8.87 30.14,11.08 C30.14,12.20 29.82,13.38 29.18,14.62 C28.56,15.86 27.37,17.35 25.63,19.10 C23.82,20.84 22.93,22.84 22.93,25.09 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.93
					strokeLineCap: StrokeLineCap.BUTT
					content: "M24.45,32.92 C24.45,34.21 24.02,35.27 23.15,36.09 C22.27,36.92 21.14,37.33 19.74,37.33 C18.36,37.33 17.22,36.91 16.34,36.08 C15.46,35.25 15.02,34.19 15.02,32.92 C15.02,31.62 15.46,30.56 16.34,29.74 C17.22,28.92 18.36,28.51 19.74,28.51 C21.12,28.51 22.25,28.92 23.13,29.74 C24.01,30.56 24.45,31.62 24.45,32.92 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.93
					strokeLineCap: StrokeLineCap.BUTT
					content: "M24.47,32.81 C24.47,34.10 24.04,35.16 23.17,35.99 C22.29,36.81 21.16,37.22 19.76,37.22 C18.37,37.22 17.24,36.80 16.36,35.97 C15.48,35.14 15.04,34.09 15.04,32.81 C15.04,31.51 15.48,30.46 16.36,29.63 C17.24,28.81 18.37,28.40 19.76,28.40 C21.14,28.40 22.27,28.81 23.15,29.63 C24.03,30.46 24.47,31.51 24.47,32.81 Z "
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
         AssignmentIcon{
            windowColorScheme: windowColorScheme
           layoutX: 25
            layoutY: 25
         }
         AssignmentIcon{
            windowColorScheme: windowColorScheme
            layoutX: 75
            layoutY: 25
            selected: true
         }
      ]
	}
}
}
