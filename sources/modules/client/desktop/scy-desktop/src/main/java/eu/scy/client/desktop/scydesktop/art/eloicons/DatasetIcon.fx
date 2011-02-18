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
public class DatasetIcon extends AbstractEloIcon {

public override function clone(): DatasetIcon {
DatasetIcon {
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
					content: "M38.59,36.09 C37.60,30.16 37.45,23.86 37.64,17.87 C37.77,13.68 38.50,8.88 37.64,4.74 C37.41,3.64 36.38,3.18 35.36,3.52 C31.08,4.94 24.80,3.90 20.34,3.51 C15.08,3.05 10.05,1.90 4.74,1.96 C2.79,1.98 2.34,4.82 4.02,5.16 C4.43,11.87 3.39,19.06 3.01,25.75 C2.82,29.17 0.50,34.30 3.50,36.94 C4.60,37.92 7.18,37.48 8.46,37.49 C11.54,37.51 14.67,38.07 17.75,38.30 C24.16,38.79 30.83,38.37 37.24,37.95 C38.12,37.89 38.72,36.90 38.59,36.09 Z M5.55,34.19 C5.86,34.20 7.11,11.74 7.20,5.26 C12.20,5.52 17.09,6.49 22.09,6.93 C25.97,7.27 30.66,7.76 34.69,6.99 C35.25,11.71 34.37,16.97 34.31,21.61 C34.25,25.96 34.51,30.47 35.12,34.82 C25.17,35.32 15.49,34.55 5.55,34.19 Z "
				},
				Rectangle {
					fill: bind windowColorScheme.mainColor
					stroke: null
					x: 7.57
					y: 6.35
					width: 27.57
					height: 27.57
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M34.23,7.27 L34.23,33.00 L8.49,33.00 L8.49,7.27 Z M36.07,5.43 L6.65,5.43 L6.65,34.84 L36.07,34.84 Z "
				},
				Rectangle {
					fill: Color.WHITE
					stroke: null
					x: 28.69
					y: 27.52
					width: 7.35
					height: 7.31
				},
				Rectangle {
					fill: Color.WHITE
					stroke: null
					x: 28.69
					y: 16.69
					width: 7.35
					height: 7.31
				},
				Rectangle {
					fill: Color.WHITE
					stroke: null
					x: 28.69
					y: 5.54
					width: 7.35
					height: 7.4
				},
				Rectangle {
					fill: Color.WHITE
					stroke: null
					x: 17.7
					y: 5.46
					width: 7.37
					height: 7.46
				},
				Rectangle {
					fill: Color.WHITE
					stroke: null
					x: 6.55
					y: 5.46
					width: 7.37
					height: 7.46
				},
				Rectangle {
					fill: Color.WHITE
					stroke: null
					x: 17.7
					y: 16.66
					width: 7.37
					height: 7.37
				},
				Rectangle {
					fill: Color.WHITE
					stroke: null
					x: 6.55
					y: 16.66
					width: 7.37
					height: 7.37
				},
				Rectangle {
					fill: Color.WHITE
					stroke: null
					x: 17.7
					y: 27.58
					width: 7.37
					height: 7.37
				},
				Rectangle {
					fill: Color.WHITE
					stroke: null
					x: 6.55
					y: 27.58
					width: 7.37
					height: 7.37
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M34.23,7.27 L34.23,33.00 L8.49,33.00 L8.49,7.27 Z M36.06,5.43 L6.65,5.43 L6.65,34.84 L36.06,34.84 Z "
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
         DatasetIcon{
            windowColorScheme: windowColorScheme
           layoutX: 25
            layoutY: 25
         }
         DatasetIcon{
            windowColorScheme: windowColorScheme
            layoutX: 75
            layoutY: 25
            selected: true
         }
      ]
	}
}
}
