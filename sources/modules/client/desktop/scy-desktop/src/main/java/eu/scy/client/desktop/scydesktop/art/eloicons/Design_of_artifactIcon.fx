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
public class Design_of_artifactIcon extends AbstractEloIcon {

public override function clone(): Design_of_artifactIcon {
Design_of_artifactIcon {
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
					content: "M38.78,26.64 C35.79,24.65 33.43,22.08 30.84,19.64 C28.61,17.53 25.78,16.16 23.37,14.25 C20.77,12.19 18.85,9.38 16.97,6.72 C15.23,4.27 11.87,2.87 9.57,1.02 C8.00,-0.23 6.35,0.18 5.28,1.28 C2.85,0.60 -0.45,3.07 0.67,6.26 C2.66,11.90 3.01,17.91 3.88,23.78 C4.33,26.82 4.66,29.78 4.42,32.86 C4.26,35.04 4.59,36.28 5.88,38.00 C8.27,41.21 15.76,39.90 19.10,39.74 C25.15,39.46 31.26,38.75 37.22,37.69 C40.36,37.13 40.79,33.70 39.30,31.81 C40.60,30.38 40.88,28.03 38.78,26.64 Z M17.88,32.70 C17.48,32.73 13.45,33.07 11.66,32.85 C11.56,25.10 10.94,17.07 9.10,9.47 C10.50,10.53 11.81,11.66 12.87,13.24 C14.61,15.83 16.98,17.97 19.30,20.03 C21.98,22.41 25.16,23.90 27.73,26.44 C29.41,28.10 31.09,29.71 32.95,31.14 C29.86,31.60 26.76,31.94 23.64,32.21 C21.72,32.39 19.80,32.56 17.88,32.70 Z "
				},
				Polygon {
					points: [32.65,32.18,8.77,35.72,4.53,6.24]
					fill: bind windowColorScheme.secondColor
					stroke: bind windowColorScheme.secondColor
					strokeWidth: 2.47
					strokeLineCap: StrokeLineCap.BUTT
				},
				Polygon {
					points: [14.27,20.57,15.69,30.43,23.67,29.25]
					fill: Color.WHITE
					stroke: Color.WHITE
					strokeWidth: 2.3
					strokeLineCap: StrokeLineCap.BUTT
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.81
					strokeLineCap: StrokeLineCap.BUTT
					startX: 22.46
					startY: 24.86
					endX: 28.55
					endY: 24.77
				},
				Polygon {
					points: [36.58,37.38,33.95,34.74,35.81,33.80]
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.53
					strokeLineCap: StrokeLineCap.BUTT
				},
				Polygon {
					points: [15.59,38.38,17.03,33.43,18.08,33.80]
					fill: bind windowColorScheme.mainColor
					stroke: null
				},
				Polyline {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 3.43
					strokeLineCap: StrokeLineCap.BUTT
					points: [34.48,33.79,25.06,14.70,17.61,33.73]
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 2.18
					strokeLineCap: StrokeLineCap.ROUND
					startX: 25.72
					startY: 23.45
					endX: 25.78
					endY: 27.46
				},
				Line {
					fill: null
					stroke: Color.WHITE
					strokeWidth: 0.52
					strokeLineCap: StrokeLineCap.ROUND
					startX: 25.14
					startY: 22.82
					endX: 26.28
					endY: 22.81
				},
				Line {
					fill: null
					stroke: Color.WHITE
					strokeWidth: 0.52
					strokeLineCap: StrokeLineCap.ROUND
					startX: 25.16
					startY: 24.14
					endX: 26.3
					endY: 24.13
				},
				Line {
					fill: null
					stroke: Color.WHITE
					strokeWidth: 0.52
					strokeLineCap: StrokeLineCap.ROUND
					startX: 25.18
					startY: 25.47
					endX: 26.32
					endY: 25.45
				},
				Line {
					fill: null
					stroke: Color.WHITE
					strokeWidth: 0.52
					strokeLineCap: StrokeLineCap.ROUND
					startX: 25.2
					startY: 26.79
					endX: 26.34
					endY: 26.77
				},
				Line {
					fill: null
					stroke: Color.WHITE
					strokeWidth: 0.52
					strokeLineCap: StrokeLineCap.ROUND
					startX: 25.22
					startY: 28.11
					endX: 26.36
					endY: 28.09
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 3.74
					strokeLineCap: StrokeLineCap.ROUND
					startX: 24.84
					startY: 5.08
					endX: 24.84
					endY: 8.2
				},
				Polygon {
					points: [21.94,5.34,9.01,39.31,3.66,37.22,3.98,36.37,16.59,3.25]
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.06
					strokeLineCap: StrokeLineCap.ROUND
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.81
					strokeLineCap: StrokeLineCap.ROUND
					startX: 7.43
					startY: 32.91
					endX: 5.74
					endY: 32.25
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.81
					strokeLineCap: StrokeLineCap.ROUND
					startX: 8.43
					startY: 30.28
					endX: 6.74
					endY: 29.61
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.81
					strokeLineCap: StrokeLineCap.ROUND
					startX: 9.43
					startY: 27.65
					endX: 7.74
					endY: 26.98
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.81
					strokeLineCap: StrokeLineCap.ROUND
					startX: 10.43
					startY: 25.02
					endX: 8.74
					endY: 24.35
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.81
					strokeLineCap: StrokeLineCap.ROUND
					startX: 12.13
					startY: 22.66
					endX: 9.79
					endY: 21.74
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.81
					strokeLineCap: StrokeLineCap.ROUND
					startX: 7.16
					startY: 35.71
					endX: 4.82
					endY: 34.79
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.81
					strokeLineCap: StrokeLineCap.ROUND
					startX: 12.43
					startY: 19.75
					endX: 10.74
					endY: 19.09
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.81
					strokeLineCap: StrokeLineCap.ROUND
					startX: 13.43
					startY: 17.12
					endX: 11.74
					endY: 16.46
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.81
					strokeLineCap: StrokeLineCap.ROUND
					startX: 14.44
					startY: 14.49
					endX: 12.75
					endY: 13.83
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.81
					strokeLineCap: StrokeLineCap.ROUND
					startX: 15.44
					startY: 11.86
					endX: 13.75
					endY: 11.2
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.81
					strokeLineCap: StrokeLineCap.ROUND
					startX: 17.13
					startY: 9.5
					endX: 14.79
					endY: 8.59
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.81
					strokeLineCap: StrokeLineCap.ROUND
					startX: 17.44
					startY: 6.6
					endX: 15.75
					endY: 5.94
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
         Design_of_artifactIcon{
            windowColorScheme: windowColorScheme
           layoutX: 25
            layoutY: 25
         }
         Design_of_artifactIcon{
            windowColorScheme: windowColorScheme
            layoutX: 75
            layoutY: 25
            selected: true
         }
      ]
	}
}
}
