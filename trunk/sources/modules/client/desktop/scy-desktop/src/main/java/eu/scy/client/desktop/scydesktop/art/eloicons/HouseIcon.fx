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
public class HouseIcon extends AbstractEloIcon {

public override function clone(): HouseIcon {
HouseIcon {
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
					x: 10.42
					y: 20.14
					width: 8.0
					height: 8.0
				},
				SVGPath {
					fill: bind windowColorScheme.mainColorLight
					stroke: null
					content: "M38.53,16.52 C38.88,14.79 38.29,12.92 36.24,11.89 C33.80,10.67 31.25,9.69 28.81,8.46 C26.50,7.29 25.08,4.27 23.05,2.63 C19.21,-0.48 14.63,0.98 11.21,3.90 C7.69,6.90 3.29,9.01 1.03,13.20 C-1.63,18.14 1.80,23.97 1.25,29.09 C-0.09,41.60 19.74,38.73 27.34,39.99 C31.76,40.73 36.73,39.83 38.21,35.03 C39.68,30.29 38.27,24.53 39.07,19.57 C39.27,18.37 39.03,17.35 38.53,16.52 Z M29.65,30.37 C29.91,33.21 29.72,31.28 27.43,30.89 C23.52,30.24 19.54,30.28 15.60,29.99 C14.00,29.87 12.42,29.71 10.83,29.48 C9.68,29.32 10.64,27.69 10.65,26.16 C10.68,23.50 7.46,18.17 9.92,16.10 C12.17,14.21 14.68,12.84 16.87,10.81 C18.26,9.52 17.88,9.85 19.01,11.22 C20.10,12.53 21.13,13.82 22.39,14.97 C24.47,16.87 27.41,17.88 30.09,18.99 C29.69,21.78 29.48,24.58 29.43,27.41 C29.41,28.39 29.56,29.39 29.65,30.37 Z "
				},
				Polyline {
					fill: null
					stroke: bind windowColorScheme.emptyBackgroundColor
					strokeWidth: 0.44
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 1.0
					strokeDashArray: [
					1.25,
					]
					points: [10.12,16.67]
				},
				Polygon {
					points: [35.60,36.11,30.08,36.11,30.08,22.82,22.50,22.82,22.50,36.11,7.61,36.11,7.61,15.75,21.60,3.44,35.60,15.75]
					fill: bind windowColorScheme.mainColor
					stroke: null
				},
				Rectangle {
					fill: bind windowColorScheme.mainColor
					stroke: null
					x: 27.06
					y: 4.13
					width: 4.42
					height: 9.17
				},
				Rectangle {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: null
					x: 10.87
					y: 21.48
					width: 7.64
					height: 6.72
				},
				Rectangle {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: null
					x: 22.37
					y: 22.39
					width: 7.64
					height: 13.72
				},
				Polygon {
					points: [4.73,18.28,21.60,3.44,38.48,18.28]
					fill: bind windowColorScheme.thirdColor
					stroke: bind windowColorScheme.emptyBackgroundColor
					strokeWidth: 0.63
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
				},
				Polyline {
					fill: null
					stroke: bind windowColorScheme.emptyBackgroundColor
					strokeWidth: 0.44
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 1.0
					strokeDashArray: [
					1.25,
					]
					points: [10.12,16.67,21.92,6.29,33.72,16.67]
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
         HouseIcon{
            windowColorScheme: windowColorScheme
           layoutX: 25
            layoutY: 25
         }
         HouseIcon{
            windowColorScheme: windowColorScheme
            layoutX: 75
            layoutY: 25
            selected: true
         }
      ]
	}
}
}
