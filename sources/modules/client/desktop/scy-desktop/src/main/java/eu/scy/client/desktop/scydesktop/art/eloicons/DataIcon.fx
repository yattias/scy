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
public class DataIcon extends AbstractEloIcon {

public override function clone(): DataIcon {
DataIcon {
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
					content: "M37.55,32.95 C36.34,27.60 37.82,22.90 37.92,17.45 C37.98,13.85 39.45,7.72 37.11,4.58 C34.69,1.33 31.15,1.84 27.53,1.61 C20.61,1.17 13.73,0.35 6.80,0.01 C4.24,-0.11 2.70,1.61 2.31,3.54 C1.37,4.15 0.69,5.16 0.62,6.64 C0.38,11.54 0.60,16.62 0.78,21.52 C0.96,26.22 1.91,30.38 4.36,34.58 C7.12,39.31 11.71,37.38 16.05,37.08 C18.86,36.88 21.79,37.54 24.61,37.39 C27.66,37.22 31.17,36.97 34.11,37.83 C37.07,38.69 38.30,36.28 37.55,32.95 Z M23.35,30.12 C20.52,30.24 17.71,30.53 14.89,30.70 C13.94,30.76 7.05,29.97 5.42,30.20 C4.69,26.87 4.33,22.22 4.23,19.45 C4.09,15.53 4.80,8.34 4.92,4.39 C8.87,4.61 19.29,4.66 23.23,5.02 C26.24,5.28 27.46,9.85 30.63,10.13 C30.56,11.27 30.67,12.80 30.67,13.12 C30.65,15.72 30.55,18.31 30.45,20.91 C30.34,23.71 30.60,26.75 31.04,29.71 C28.47,29.74 25.89,30.00 23.35,30.12 Z "
				},
				Polyline {
					fill: Color.rgb(0x6a,0xb3,0x30)
					stroke: null
					points: [6.86,21.32]
				},
				Polyline {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.2
					points: [5.65,9.29]
				},
				Polyline {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: bind windowColorScheme.secondColor
					strokeWidth: 1.2
					points: [5.65,10.09,5.65,4.89,31.64,4.89,31.64,10.09]
				},
				Rectangle {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: bind windowColorScheme.secondColor
					strokeWidth: 1.2
					x: 5.69
					y: 10.89
					width: 25.92
					height: 18.8
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.secondColor
					strokeWidth: 0.8
					startX: 10.42
					startY: 4.86
					endX: 10.42
					endY: 29.26
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.secondColor
					strokeWidth: 0.8
					startX: 25.64
					startY: 21.44
					endX: 25.64
					endY: 28.75
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.secondColor
					strokeWidth: 0.8
					startX: 6.62
					startY: 21.09
					endX: 30.58
					endY: 21.09
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.secondColor
					strokeWidth: 0.8
					startX: 6.62
					startY: 25.49
					endX: 30.58
					endY: 25.49
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeLineCap: StrokeLineCap.BUTT
					startX: 6.42
					startY: 8.64
					endX: 9.42
					endY: 8.64
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeLineCap: StrokeLineCap.BUTT
					startX: 6.42
					startY: 13.64
					endX: 9.42
					endY: 13.64
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeLineCap: StrokeLineCap.BUTT
					startX: 6.42
					startY: 18.64
					endX: 9.42
					endY: 18.64
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeLineCap: StrokeLineCap.BUTT
					startX: 6.42
					startY: 23.64
					endX: 9.42
					endY: 23.64
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeLineCap: StrokeLineCap.BUTT
					startX: 6.42
					startY: 27.64
					endX: 9.42
					endY: 27.64
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeLineCap: StrokeLineCap.BUTT
					startX: 11.42
					startY: 8.64
					endX: 24.42
					endY: 8.64
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeLineCap: StrokeLineCap.BUTT
					startX: 11.42
					startY: 27.64
					endX: 24.42
					endY: 27.64
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeLineCap: StrokeLineCap.BUTT
					startX: 11.42
					startY: 13.64
					endX: 31.42
					endY: 13.64
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeLineCap: StrokeLineCap.BUTT
					startX: 11.42
					startY: 15.64
					endX: 31.42
					endY: 15.64
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeLineCap: StrokeLineCap.BUTT
					startX: 11.42
					startY: 23.64
					endX: 23.42
					endY: 23.64
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeLineCap: StrokeLineCap.BUTT
					startX: 11.42
					startY: 18.64
					endX: 30.42
					endY: 18.64
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 2.13
					content: "M32.06,24.19 C33.46,28.14 34.72,31.89 34.72,32.66 C34.72,34.45 33.69,35.12 32.02,35.12 C30.30,35.12 21.58,35.12 20.46,35.07 C19.45,35.03 17.61,34.29 17.61,32.70 C17.61,30.92 19.11,26.35 19.86,24.18 "
				},
				SVGPath {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 2.0
					content: "M31.79,23.25 C30.22,18.60 28.48,13.66 28.33,12.88 Q28.06,11.44 28.33,8.14 L23.41,8.14 Q23.79,11.88 23.55,13.02 Q23.28,14.27 20.49,22.07 Q20.34,22.53 20.10,23.24 Z "
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
         DataIcon{
            windowColorScheme: windowColorScheme
           layoutX: 25
            layoutY: 25
         }
         DataIcon{
            windowColorScheme: windowColorScheme
            layoutX: 75
            layoutY: 25
            selected: true
         }
      ]
	}
}
}
