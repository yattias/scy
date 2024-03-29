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
public class Information2Icon extends AbstractEloIcon {

public override function clone(): Information2Icon {
Information2Icon {
selected: selected
size: size
windowColorScheme: windowColorScheme
}
}

public override function createNode(): Node {

return Group {

			content: [
				Rectangle {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: null
					x: 15.42
					y: 2.14
					width: 11.0
					height: 36.0
				},
				SVGPath {
					fill: bind windowColorScheme.mainColorLight
					stroke: null
					content: "M33.96,8.35 C30.41,4.87 25.65,3.40 20.61,2.56 C20.59,2.56 20.57,2.56 20.54,2.55 C19.65,1.03 17.87,-0.13 15.51,0.21 C10.98,0.85 8.13,3.37 7.25,7.42 C6.81,9.48 7.38,11.56 8.07,13.51 C9.28,16.88 8.11,18.76 7.06,22.09 C5.95,25.62 3.80,32.89 6.61,36.25 C7.86,37.76 9.11,38.98 11.24,39.48 C14.12,40.17 16.87,38.73 19.62,39.76 C24.60,41.61 27.50,36.36 28.38,33.00 C28.96,30.77 28.88,28.34 28.96,26.07 C29.09,22.33 31.67,21.77 34.15,19.39 C37.36,16.32 37.06,11.39 33.96,8.35 Z M23.02,16.39 C21.41,18.88 20.06,23.33 19.96,26.16 C19.90,27.75 19.92,29.36 19.68,30.93 C19.65,31.02 19.63,31.10 19.61,31.18 C19.46,31.18 19.30,31.18 19.11,31.18 C17.85,30.99 16.64,31.10 15.39,31.32 C15.00,31.39 14.71,31.47 14.49,31.55 C14.51,31.45 14.52,31.35 14.52,31.23 C14.46,27.75 15.67,24.27 16.81,20.98 C17.57,18.78 18.72,16.23 17.80,13.88 C17.27,12.54 16.06,10.73 16.15,9.46 C16.67,9.91 14.24,10.11 15.17,10.27 C18.14,10.76 16.51,11.44 17.48,14.27 C18.11,16.08 23.72,15.31 23.02,16.39 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M20.03,38.23 C18.54,38.23 17.02,37.26 17.00,35.40 C16.97,33.04 17.26,30.57 17.54,28.18 C17.95,24.68 18.38,21.05 17.81,17.66 C17.67,16.80 17.85,16.04 18.33,15.48 C18.85,14.86 19.68,14.51 20.62,14.51 C22.05,14.51 23.63,15.35 23.94,17.24 C24.53,20.70 24.09,24.37 23.68,27.91 C23.42,30.11 23.15,32.38 23.14,34.55 C23.45,35.47 23.18,36.46 22.37,37.27 C21.73,37.90 20.92,38.23 20.03,38.23 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: null
					content: "M20.62,14.76 C21.97,14.76 23.41,15.58 23.70,17.28 C24.63,22.86 22.92,28.89 22.89,34.54 C23.15,35.36 23.00,36.28 22.20,37.09 C22.18,37.10 22.17,37.12 22.15,37.13 C21.58,37.71 20.80,37.98 20.03,37.98 C18.63,37.98 17.27,37.06 17.25,35.40 C17.17,29.59 19.02,23.37 18.06,17.61 C17.74,15.74 19.13,14.76 20.62,14.76 M20.62,14.26 C19.61,14.26 18.70,14.64 18.14,15.31 C17.77,15.75 17.37,16.52 17.57,17.70 C18.13,21.06 17.70,24.66 17.29,28.15 C17.01,30.55 16.72,33.02 16.75,35.41 C16.77,37.43 18.42,38.48 20.03,38.48 C20.98,38.48 21.86,38.13 22.51,37.48 L22.56,37.43 C23.39,36.60 23.69,35.52 23.39,34.47 C23.41,32.34 23.67,30.10 23.93,27.94 C24.34,24.38 24.78,20.70 24.19,17.19 C23.85,15.18 22.10,14.26 20.62,14.26 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.thirdColor
					stroke: null
					content: "M21.67,12.81 C20.97,12.81 20.28,12.67 19.61,12.37 C17.16,11.31 15.19,8.46 16.29,5.72 C16.83,4.39 17.94,3.40 19.41,2.96 C19.49,2.89 19.64,2.80 19.79,2.72 C20.31,2.46 20.89,2.33 21.50,2.33 C22.92,2.33 24.41,3.07 25.27,4.22 C26.24,5.50 26.66,6.77 26.60,8.22 C26.55,9.44 25.95,11.10 24.75,11.86 C23.75,12.49 22.71,12.81 21.67,12.81 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.thirdColor
					stroke: null
					content: "M21.50,2.58 C22.88,2.58 24.27,3.30 25.08,4.37 C25.96,5.55 26.41,6.75 26.35,8.21 C26.31,9.40 25.71,10.95 24.61,11.65 C23.69,12.23 22.70,12.56 21.67,12.56 C21.03,12.56 20.37,12.43 19.71,12.15 C17.28,11.09 15.50,8.36 16.52,5.82 C17.06,4.47 18.18,3.59 19.48,3.20 C19.62,3.11 19.75,3.02 19.90,2.94 C20.41,2.69 20.95,2.58 21.50,2.58 M21.50,2.08 C20.85,2.08 20.24,2.22 19.68,2.49 C19.53,2.57 19.40,2.65 19.27,2.74 C17.75,3.22 16.61,4.24 16.06,5.63 C14.90,8.51 16.95,11.49 19.51,12.60 C20.21,12.91 20.94,13.06 21.67,13.06 C22.76,13.06 23.84,12.73 24.88,12.07 C26.16,11.26 26.80,9.51 26.85,8.23 C26.91,6.72 26.47,5.40 25.47,4.07 C24.56,2.86 23.00,2.08 21.50,2.08 Z "
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
         Information2Icon{
            windowColorScheme: windowColorScheme
           layoutX: 25
            layoutY: 25
         }
         Information2Icon{
            windowColorScheme: windowColorScheme
            layoutX: 75
            layoutY: 25
            selected: true
         }
      ]
	}
}
}
