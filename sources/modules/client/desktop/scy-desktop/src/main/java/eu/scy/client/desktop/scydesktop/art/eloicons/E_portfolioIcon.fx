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
public class E_portfolioIcon extends AbstractEloIcon {

public override function clone(): E_portfolioIcon {
E_portfolioIcon {
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
					content: "M36.17,13.07 C33.30,10.78 29.51,9.42 26.19,8.08 C21.87,6.34 15.91,4.86 12.54,1.61 C10.88,0.02 8.28,1.73 8.49,3.62 C5.01,8.10 1.67,12.14 0.81,18.06 C0.40,20.83 1.33,23.45 2.33,25.97 C3.29,28.38 7.33,33.75 5.72,36.38 C5.13,37.32 5.55,38.37 6.29,39.05 C7.84,40.47 10.47,39.50 12.27,39.29 C17.26,38.72 22.30,38.66 27.32,38.94 C29.62,39.08 33.33,40.14 35.25,38.17 C37.37,35.99 36.48,30.43 37.07,27.64 C37.74,24.42 39.27,21.41 40.01,18.24 C40.53,16.04 37.49,14.12 36.17,13.07 Z M33.14,23.37 C31.75,27.15 32.07,31.15 31.27,34.93 C28.15,35.37 23.43,34.32 20.65,34.33 C18.33,34.33 16.02,34.47 13.70,34.66 C12.82,34.74 11.94,34.85 11.06,34.95 C10.99,32.36 9.56,29.73 8.46,27.47 C6.49,23.39 5.18,19.97 6.43,15.37 C7.27,12.29 9.64,9.63 11.72,7.11 C17.68,11.51 25.34,13.39 32.06,16.07 C33.05,16.60 33.93,17.26 34.71,18.03 C34.67,18.13 34.63,18.24 34.59,18.37 C33.84,19.76 33.70,21.85 33.14,23.37 Z "
				},
				Polygon {
					points: [10.75,34.02,13.08,22.60,34.20,22.60,31.21,34.02]
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: null
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M33.07,23.47 L30.54,33.15 L11.82,33.15 L13.79,23.47 Z M35.33,21.72 L12.36,21.72 L9.68,34.90 L31.89,34.90 Z "
				},
				Polygon {
					points: [7.95,18.17,12.45,7.61,19.28,10.47,19.97,9.70,23.12,11.04,23.21,12.12,31.89,15.76,27.47,26.33]
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: null
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M12.91,8.75 L18.37,11.03 L19.52,11.51 L20.20,10.75 L22.30,11.64 L22.30,11.67 L22.39,12.72 L23.36,13.13 L30.75,16.23 L27.00,25.19 L9.10,17.70 Z M11.99,6.47 L6.81,18.63 L27.94,27.47 L33.03,15.30 L24.04,11.52 L23.94,10.44 L19.94,8.74 L19.66,8.74 L19.05,9.43 Z "
				},
				Polygon {
					points: [9.56,26.73,11.43,15.43,13.03,15.70,13.47,14.91,16.89,15.38,17.24,16.40,32.28,18.86,30.41,30.15]
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: null
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M13.95,15.85 L16.24,16.17 L16.25,16.19 L16.59,17.17 L17.62,17.34 L31.28,19.58 L29.69,29.15 L10.56,26.01 L12.15,16.44 L12.28,16.46 L13.50,16.66 Z M12.99,13.96 L12.57,14.74 L10.71,14.43 L10.71,14.44 L8.56,27.44 L31.13,31.16 L33.28,18.14 L17.90,15.62 L17.54,14.59 Z "
				},
				Polygon {
					points: [9.70,34.30,6.71,22.87,27.87,22.87,30.87,34.30]
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: null
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M27.20,23.74 L29.74,33.43 L10.37,33.43 L7.84,23.74 Z M28.55,22.00 L5.59,22.00 L9.03,35.17 L32.00,35.17 Z "
				},
				Polygon {
					points: [8.61,35.77,11.65,20.85,36.45,20.85,32.56,35.77]
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: null
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M35.33,21.72 L31.89,34.90 L9.68,34.90 L12.36,21.72 Z M37.58,19.98 L35.33,19.98 L12.36,19.98 L10.94,19.98 L10.66,21.38 L7.97,34.55 L7.55,36.64 L9.68,36.64 L31.89,36.64 L33.23,36.64 L33.57,35.34 L37.01,22.16 Z "
				},
				Polygon {
					points: [5.66,19.10,11.53,5.33,18.81,8.38,19.27,7.87,20.11,7.87,24.77,9.84,24.86,10.92,34.17,14.83,28.41,28.61]
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: null
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M11.99,6.47 L19.05,9.43 L19.66,8.74 L19.94,8.74 L23.94,10.44 L24.04,11.52 L33.03,15.30 L27.94,27.47 L6.81,18.63 Z M11.06,4.19 L10.39,5.79 L5.21,17.95 L4.52,19.56 L6.14,20.24 L27.27,29.08 L28.87,29.75 L29.55,28.14 L34.64,15.97 L35.31,14.36 L33.71,13.69 L25.68,10.32 L25.68,10.29 L25.59,9.24 L24.62,8.84 L20.61,7.14 L20.29,7.00 L19.94,7.00 L19.66,7.00 L18.88,7.00 L18.58,7.34 L12.66,4.86 Z "
				},
				Polygon {
					points: [7.55,28.16,9.84,14.37,9.84,13.42,10.85,13.57,12.10,13.78,12.51,13.01,18.20,13.80,18.55,14.85,34.29,17.43,31.85,32.16]
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: null
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M12.99,13.96 L17.54,14.59 L17.90,15.62 L33.28,18.14 L31.13,31.16 L8.56,27.44 L10.71,14.44 L10.71,14.43 L12.57,14.74 Z M12.04,12.07 L11.63,12.82 L10.99,12.71 L8.97,12.38 L8.97,14.30 L6.84,27.16 L6.55,28.88 L8.27,29.16 L30.85,32.88 L32.57,33.16 L32.85,31.44 L35.00,18.43 L35.29,16.71 L33.57,16.42 L19.21,14.07 L19.19,14.02 L18.85,13.01 L17.78,12.86 L13.23,12.23 Z "
				},
				Polygon {
					points: [8.35,36.04,4.46,21.13,29.22,21.13,33.13,36.04]
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: null
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M28.55,22.00 L32.00,35.17 L9.03,35.17 L5.59,22.00 Z M29.89,20.25 L28.55,20.25 L5.59,20.25 L3.33,20.25 L3.90,22.44 L7.34,35.61 L7.68,36.91 L9.03,36.91 L32.00,36.91 L34.26,36.91 L33.69,34.73 L30.23,21.56 Z "
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
         E_portfolioIcon{
            windowColorScheme: windowColorScheme
           layoutX: 25
            layoutY: 25
         }
         E_portfolioIcon{
            windowColorScheme: windowColorScheme
            layoutX: 75
            layoutY: 25
            selected: true
         }
      ]
	}
}
}
