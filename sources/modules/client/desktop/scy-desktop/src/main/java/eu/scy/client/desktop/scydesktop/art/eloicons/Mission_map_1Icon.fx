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
public class Mission_map_1Icon extends AbstractEloIcon {

public override function clone(): Mission_map_1Icon {
Mission_map_1Icon {
selected: selected
size: size
windowColorScheme: windowColorScheme
}
}

public override function createNode(): Node {

return Group {

			content: [
				SVGPath {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: null
					content: "M7.53,9.24 Q18.73,2.83 24.34,4.61 C29.94,6.39 35.44,9.73 36.21,18.80 C36.98,27.87 28.90,38.01 25.96,37.66 C23.03,37.30 7.76,33.70 7.22,31.48 C6.67,29.25 0.71,12.75 7.53,9.24 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColorLight
					stroke: null
					content: "M39.36,19.96 C39.56,18.25 40.64,10.27 40.26,7.97 C39.65,4.34 37.32,3.55 34.63,1.65 C34.49,1.51 34.32,1.41 34.13,1.32 C33.64,1.03 33.10,0.97 32.59,1.08 C24.19,0.60 15.97,1.23 8.00,4.32 C6.05,5.07 2.31,6.87 2.66,9.69 C3.29,14.79 0.96,18.72 0.45,23.72 C-0.31,31.14 6.35,35.41 12.13,37.46 C19.12,39.94 30.19,41.51 37.14,38.31 C38.15,37.85 40.55,32.79 39.86,30.93 C39.32,29.47 39.10,22.24 39.36,19.96 Z M15.76,33.78 C8.54,31.96 3.70,27.56 6.00,19.39 C6.60,17.28 7.84,15.04 7.78,12.78 C7.76,12.09 7.10,9.75 7.70,9.22 C8.82,8.22 11.16,7.74 12.55,7.29 C18.82,5.24 25.39,5.11 31.88,5.51 C34.34,7.28 35.70,8.31 34.68,11.97 C33.76,15.28 34.78,19.09 34.42,22.53 C34.33,23.45 34.45,24.51 34.80,25.35 C35.96,28.16 33.99,28.83 34.59,30.91 C35.19,33.02 34.35,32.18 33.22,34.10 C33.03,34.43 32.98,34.77 32.99,35.12 C27.60,36.33 20.64,35.01 15.76,33.78 Z "
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
         Mission_map_1Icon{
            windowColorScheme: windowColorScheme
           layoutX: 25
            layoutY: 25
         }
         Mission_map_1Icon{
            windowColorScheme: windowColorScheme
            layoutX: 75
            layoutY: 25
            selected: true
         }
      ]
	}
}
}
