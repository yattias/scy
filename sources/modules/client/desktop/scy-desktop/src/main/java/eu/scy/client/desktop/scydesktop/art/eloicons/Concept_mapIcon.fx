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
public class Concept_mapIcon extends AbstractEloIcon {

public override function clone(): Concept_mapIcon {
Concept_mapIcon {
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
					content: "M35.64,10.26 C35.64,10.21 35.65,10.17 35.65,10.11 C35.59,8.15 35.63,9.75 35.64,10.26 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColorLight
					stroke: null
					content: "M35.64,10.26 C35.64,10.28 35.64,10.29 35.64,10.31 C35.64,10.46 35.64,10.41 35.64,10.26 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColorLight
					stroke: null
					content: "M27.65,18.90 C24.14,18.48 20.61,18.16 17.09,17.81 C16.66,17.77 16.20,17.73 15.73,17.70 C18.81,17.26 21.88,16.81 24.95,16.30 C28.32,15.74 35.60,15.39 35.64,10.31 C35.63,10.18 35.63,9.93 35.61,9.45 C35.58,8.60 35.24,7.59 34.60,7.03 C32.59,5.29 30.44,4.29 28.24,2.90 C23.36,-0.18 15.61,0.71 10.12,0.43 C5.88,0.22 6.11,7.22 10.33,7.43 C15.32,7.68 20.61,7.47 25.27,9.26 C15.64,10.66 -1.94,10.44 0.67,19.80 C1.99,24.51 11.40,24.17 14.80,24.55 C16.98,24.79 19.15,25.00 21.33,25.22 C18.21,25.80 15.08,26.31 11.97,26.94 C8.87,27.58 3.54,29.23 3.22,32.88 C0.56,38.61 8.91,39.80 12.76,40.14 C16.45,40.46 16.74,35.19 13.97,33.58 C15.50,33.28 17.09,33.11 18.60,32.84 C25.06,31.71 33.21,31.13 39.11,27.96 C40.19,27.38 40.67,26.05 40.70,24.85 C40.85,19.12 31.11,19.32 27.65,18.90 Z "
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.secondColor
					strokeWidth: 4.45
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					startX: 13.38
					startY: 27.3
					endX: 3.94
					endY: 36.71
				},
				Rectangle {
					fill: bind windowColorScheme.mainColor
					stroke: null
					x: 21.94
					y: 4.18
					width: 14.43
					height: 14.36
				},
				Polygon {
					points: [17.39,34.49,21.53,19.06,6.09,23.20]
					fill: bind windowColorScheme.secondColor
					stroke: null
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
         Concept_mapIcon{
            windowColorScheme: windowColorScheme
           layoutX: 25
            layoutY: 25
         }
         Concept_mapIcon{
            windowColorScheme: windowColorScheme
            layoutX: 75
            layoutY: 25
            selected: true
         }
      ]
	}
}
}
