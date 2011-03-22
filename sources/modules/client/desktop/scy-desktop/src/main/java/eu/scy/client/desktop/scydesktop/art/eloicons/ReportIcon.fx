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
public class ReportIcon extends AbstractEloIcon {

public override function clone(): ReportIcon {
ReportIcon {
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
					content: "M40.14,9.78 C39.96,9.12 39.43,8.59 39.06,7.84 C36.30,2.14 30.42,1.96 24.49,1.10 C18.80,0.28 10.42,-0.34 5.26,2.37 C0.21,5.01 1.43,10.49 1.48,14.95 C1.56,21.26 1.94,27.39 3.11,33.64 C4.02,38.51 7.56,39.65 11.90,39.57 C17.89,39.44 23.95,40.89 29.91,40.41 C34.92,40.00 37.84,37.43 38.46,32.95 C38.82,30.31 37.70,27.31 37.69,24.63 C37.68,20.79 38.71,17.21 40.29,13.68 C40.96,12.17 40.80,10.84 40.14,9.78 Z M23.30,37.02 C21.38,36.87 17.78,37.14 15.86,37.08 C15.59,37.07 7.80,37.27 6.55,37.33 C6.77,31.47 5.99,16.12 5.73,10.27 C5.65,8.32 6.14,3.32 9.80,3.20 C11.71,3.14 22.01,4.07 23.92,4.14 C27.36,4.27 29.32,5.16 31.98,6.95 C38.73,12.02 34.13,31.66 34.86,37.77 C34.81,37.76 27.09,37.21 27.05,37.20 C24.82,37.07 25.52,37.18 23.30,37.02 Z "
				},
				Rectangle {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.32
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					x: 7.92
					y: 4.14
					width: 25.6
					height: 32.56
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 3.97
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					startX: 10.72
					startY: 8.26
					endX: 20.72
					endY: 8.26
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 2.65
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					startX: 10.72
					startY: 13.4
					endX: 30.9
					endY: 13.4
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.secondColor
					strokeWidth: 2.65
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					startX: 10.72
					startY: 18.25
					endX: 30.9
					endY: 18.25
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 2.65
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					startX: 10.72
					startY: 23.09
					endX: 30.9
					endY: 23.09
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.secondColor
					strokeWidth: 2.65
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					startX: 10.72
					startY: 27.94
					endX: 30.9
					endY: 27.94
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 2.65
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					startX: 10.72
					startY: 32.79
					endX: 30.9
					endY: 32.79
				},
				Rectangle {
					fill: bind windowColorScheme.mainColorLight
					stroke: null
					x: 29.13
					y: 3.14
					width: 5.66
					height: 5.45
				},
				Polygon {
					points: [33.16,9.07,28.64,9.07,28.64,4.56]
					fill: bind windowColorScheme.mainColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.72
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.7
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					startX: 28.9
					startY: 3.87
					endX: 33.78
					endY: 8.82
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
         ReportIcon{
            windowColorScheme: windowColorScheme
           layoutX: 25
            layoutY: 25
         }
         ReportIcon{
            windowColorScheme: windowColorScheme
            layoutX: 75
            layoutY: 25
            selected: true
         }
      ]
	}
}
}
