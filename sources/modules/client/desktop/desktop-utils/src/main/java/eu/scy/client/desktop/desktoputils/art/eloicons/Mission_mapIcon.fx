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
public class Mission_mapIcon extends AbstractEloIcon {

public override function clone(): Mission_mapIcon {
Mission_mapIcon {
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
					content: "M21.26,11.54 C21.06,11.00 20.61,10.52 19.95,10.46 C14.37,9.89 8.63,10.30 3.01,9.97 C2.53,9.94 2.06,10.13 1.83,10.57 C1.39,11.39 1.41,12.09 1.46,13.03 C1.60,15.78 1.13,18.58 1.08,21.34 C1.04,23.85 1.08,26.35 1.03,28.86 C1.00,30.08 0.86,30.73 1.61,31.67 C2.02,32.19 2.72,32.24 3.30,32.05 C8.56,30.29 15.00,32.57 20.36,32.57 C20.93,32.57 21.59,32.29 21.67,31.64 C22.00,28.64 21.80,25.49 22.07,22.43 C22.34,19.29 22.25,16.07 22.27,12.92 C22.28,12.23 21.81,11.75 21.26,11.54 Z M3.99,28.94 C3.93,28.48 3.85,28.01 3.86,27.78 C3.90,26.11 3.90,24.60 3.89,22.93 C3.86,19.62 4.39,16.14 4.23,13.14 L5.64,13.14 C7.25,13.14 8.88,13.05 10.48,13.12 C13.46,13.26 16.50,13.06 19.47,13.30 C19.45,16.53 19.47,19.74 19.20,22.95 C19.00,25.18 19.13,27.52 18.99,29.75 C14.11,29.53 8.85,28.12 3.99,28.94 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColorLight
					stroke: null
					content: "M38.69,9.86 C38.66,8.54 38.64,7.22 38.59,5.91 C38.57,5.29 38.82,3.40 38.39,2.77 C37.71,1.75 36.78,1.49 35.51,1.54 C32.39,1.65 29.30,1.72 26.19,2.06 C25.87,2.10 25.60,2.22 25.37,2.39 C24.50,2.14 23.11,2.88 23.16,4.03 C23.38,9.05 23.09,14.26 23.69,19.24 C23.80,20.08 24.67,20.28 25.36,20.19 C29.45,19.68 33.78,19.52 37.81,18.59 C38.43,18.45 38.96,17.92 39.05,17.28 C39.34,14.95 38.74,12.21 38.69,9.86 Z M31.69,4.30 C32.50,4.28 33.32,4.27 34.14,4.27 C36.87,4.26 35.61,3.85 35.61,5.06 C35.61,7.05 35.77,9.06 35.81,11.05 C35.84,12.69 36.20,14.54 36.20,16.23 C33.04,16.84 29.69,17.06 26.47,17.41 C26.07,13.25 26.21,8.93 26.10,4.74 C27.96,4.54 29.82,4.39 31.69,4.30 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColorLight
					stroke: null
					content: "M39.30,30.73 C39.35,28.72 39.60,26.70 39.17,24.71 C39.04,24.07 38.38,23.61 37.77,23.53 C36.08,23.31 34.10,24.03 32.41,24.24 C30.24,24.51 28.05,24.59 25.86,24.48 C25.13,23.59 23.39,23.61 23.38,25.12 C23.37,27.56 23.27,29.99 23.10,32.42 C22.98,34.27 22.44,36.63 23.06,38.44 C24.08,41.39 29.70,40.05 32.05,39.95 C34.02,39.87 35.92,39.87 37.87,40.15 C39.33,40.35 39.44,38.65 39.44,37.70 C39.44,35.38 39.23,33.06 39.30,30.73 Z M25.64,37.31 C25.45,35.80 25.80,34.13 25.90,32.68 C26.04,30.89 26.10,29.09 26.14,27.29 C27.89,27.37 29.63,27.32 31.37,27.21 C32.99,27.10 34.93,26.45 36.66,26.33 C36.78,28.03 36.51,29.83 36.47,31.49 C36.44,32.50 36.47,33.51 36.49,34.52 C36.48,35.17 36.51,35.83 36.57,36.48 C36.49,36.81 36.46,37.04 36.49,37.20 C32.91,36.89 29.19,37.71 25.64,37.31 Z "
				},
				Polyline {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.31
					strokeLineCap: StrokeLineCap.BUTT
					points: [31.49,10.14,11.54,21.60,31.49,32.70]
				},
				Rectangle {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: null
					x: 4.4
					y: 13.06
					width: 14.51
					height: 15.91
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M17.87,14.11 L17.87,27.92 L5.46,27.92 L5.46,14.11 Z M19.97,12.01 L3.35,12.01 L3.35,30.02 L19.97,30.02 Z "
				},
				Rectangle {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: null
					x: 26.61
					y: 4.98
					width: 9.76
					height: 10.33
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M35.39,5.96 L35.39,14.32 L27.59,14.32 L27.59,5.96 Z M37.35,3.99 L25.63,3.99 L25.63,16.29 L37.35,16.29 Z "
				},
				Rectangle {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: null
					x: 26.61
					y: 26.77
					width: 9.76
					height: 10.33
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M35.39,27.76 L35.39,36.12 L27.59,36.12 L27.59,27.76 Z M37.35,25.79 L25.63,25.79 L25.63,38.09 L37.35,38.09 Z "
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
         Mission_mapIcon{
            windowColorScheme: windowColorScheme
           layoutX: 25
            layoutY: 25
         }
         Mission_mapIcon{
            windowColorScheme: windowColorScheme
            layoutX: 75
            layoutY: 25
            selected: true
         }
      ]
	}
}
}
