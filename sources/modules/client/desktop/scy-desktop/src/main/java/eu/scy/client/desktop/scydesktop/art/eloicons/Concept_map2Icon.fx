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
public class Concept_map2Icon extends AbstractEloIcon {

public override function clone(): Concept_map2Icon {
Concept_map2Icon {
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
					content: "M37.41,25.22 C35.49,24.12 33.23,23.52 30.89,23.10 C32.96,22.58 34.93,21.85 36.64,20.77 C39.09,19.21 39.85,16.67 38.71,14.01 C38.01,12.35 36.60,11.35 34.96,10.69 C35.06,10.67 35.16,10.66 35.25,10.64 C41.59,9.46 39.33,-0.28 33.00,0.89 C26.75,2.05 20.24,1.95 13.93,2.77 C9.93,3.28 3.02,5.01 2.81,10.15 C2.65,13.85 5.04,15.60 7.96,16.53 C3.86,17.45 -0.50,19.36 0.61,24.17 C1.45,27.83 5.37,29.14 8.99,29.88 C6.24,32.08 7.25,37.63 10.85,38.44 C12.17,38.74 13.24,38.77 14.59,38.60 C15.77,38.45 16.66,37.99 17.28,37.34 C21.66,36.28 26.80,36.14 31.04,35.60 C33.77,35.25 37.45,34.83 38.92,32.13 C40.17,29.84 39.90,26.64 37.41,25.22 Z "
				},
				Rectangle {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: null
					x: 20.42
					y: 5.14
					width: 15.0
					height: 16.0
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M36.92,2.96 C34.05,3.04 31.21,3.22 28.33,3.35 C25.39,3.49 22.48,2.92 19.55,2.81 C18.24,2.76 17.94,4.18 18.55,5.01 C18.54,8.48 18.69,11.88 18.42,15.35 C18.27,17.21 17.36,20.05 18.50,21.79 C19.45,23.25 22.05,22.52 23.40,22.46 C27.39,22.27 31.30,22.10 35.22,23.00 C36.13,23.21 36.93,22.48 36.93,21.59 C36.93,15.80 38.13,10.09 38.38,4.30 C38.41,3.54 37.64,2.94 36.92,2.96 Z M21.26,19.79 C20.06,19.87 21.00,17.67 21.09,16.77 C21.18,15.87 21.25,14.98 21.32,14.09 C21.53,11.27 21.39,8.50 21.37,5.70 C23.72,5.89 26.06,6.20 28.44,6.14 C30.80,6.07 33.13,5.91 35.48,5.81 C35.17,10.54 34.34,15.22 34.17,19.96 C29.91,19.25 25.57,19.51 21.26,19.79 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.thirdColor
					stroke: null
					content: "M22.17,33.88 C21.85,31.81 21.57,29.80 21.17,27.75 C20.85,26.14 21.08,23.68 19.58,22.64 C18.23,21.72 16.87,22.41 15.48,22.86 C12.50,23.81 10.12,24.66 6.92,24.42 C4.38,24.24 3.60,28.17 6.17,28.36 C7.96,28.49 9.77,28.57 11.54,28.29 C10.95,28.78 10.35,29.26 9.78,29.78 C7.82,31.56 6.01,33.22 3.80,34.72 C1.68,36.15 3.65,39.64 5.79,38.19 C8.09,36.63 10.17,35.02 12.14,33.06 C13.85,31.37 15.71,30.05 17.28,28.26 C17.36,28.83 17.42,29.29 17.44,29.39 C17.81,30.96 17.93,32.56 18.17,34.15 C18.57,36.69 22.56,36.43 22.17,33.88 Z "
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
         Concept_map2Icon{
            windowColorScheme: windowColorScheme
           layoutX: 25
            layoutY: 25
         }
         Concept_map2Icon{
            windowColorScheme: windowColorScheme
            layoutX: 75
            layoutY: 25
            selected: true
         }
      ]
	}
}
}
