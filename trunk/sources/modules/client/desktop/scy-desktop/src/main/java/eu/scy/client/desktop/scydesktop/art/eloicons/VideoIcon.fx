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
public class VideoIcon extends AbstractEloIcon {

public override function clone(): VideoIcon {
VideoIcon {
selected: selected
size: size
windowColorScheme: windowColorScheme
}
}

public override function createNode(): Node {

return Group {

			content: [
				SVGPath {
					fill: bind windowColorScheme.secondColorLight
					stroke: null
					content: "M35.71,8.98 C32.84,2.22 28.89,0.76 23.56,1.43 C14.88,-0.41 2.72,-2.46 0.84,11.11 C-1.30,26.58 7.98,34.80 17.70,38.44 C25.25,41.27 36.41,42.44 39.80,30.37 C41.75,23.44 38.17,14.78 35.71,8.98 Z M32.26,30.24 C27.77,34.03 21.26,31.06 16.71,29.14 C10.88,26.68 5.21,20.89 7.29,11.67 C8.34,6.99 15.63,8.02 21.36,9.36 C21.94,9.89 22.67,10.18 23.55,10.03 C23.65,10.02 23.75,10.00 23.86,9.98 C25.49,10.40 26.84,10.77 27.63,10.90 C28.19,10.99 28.67,10.91 29.09,10.71 C30.31,11.99 31.32,16.02 31.92,17.70 C33.17,21.20 35.55,27.47 32.26,30.24 Z "
				},
				Rectangle {
					fill: Color.WHITE
					stroke: null
					x: 5.42
					y: 18.14
					width: 22.0
					height: 11.0
				},
				Polygon {
					points: [30.16,19.93,34.84,16.55,37.45,16.55,37.45,29.59,34.19,29.59,30.08,25.64]
					fill: bind windowColorScheme.mainColor
					stroke: null
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M16.80,8.65 C16.80,11.13 14.79,13.14 12.30,13.14 C9.82,13.14 7.81,11.13 7.81,8.65 C7.81,6.16 9.82,4.15 12.30,4.15 C14.79,4.15 16.80,6.16 16.80,8.65 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M27.86,8.65 C27.86,11.13 25.84,13.14 23.36,13.14 C20.88,13.14 18.86,11.13 18.86,8.65 C18.86,6.16 20.88,4.15 23.36,4.15 C25.84,4.15 27.86,6.16 27.86,8.65 Z "
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 4.8
					strokeLineCap: StrokeLineCap.BUTT
					startX: 5.4
					startY: 27.3
					endX: 5.4
					endY: 18.52
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 4.8
					strokeLineCap: StrokeLineCap.BUTT
					startX: 27.92
					startY: 27.3
					endX: 27.92
					endY: 18.52
				},
				SVGPath {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 6.73
					strokeLineCap: StrokeLineCap.BUTT
					content: "M26.90,27.30 C26.90,28.01 26.44,28.59 25.88,28.59 L7.32,28.59 C6.76,28.59 6.31,28.01 6.31,27.30 "
				},
				SVGPath {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 6.74
					strokeLineCap: StrokeLineCap.BUTT
					content: "M6.32,18.52 C6.32,17.81 6.77,17.23 7.34,17.23 L25.96,17.23 C26.52,17.23 26.97,17.81 26.97,18.52 "
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
         VideoIcon{
            windowColorScheme: windowColorScheme
           layoutX: 25
            layoutY: 25
         }
         VideoIcon{
            windowColorScheme: windowColorScheme
            layoutX: 75
            layoutY: 25
            selected: true
         }
      ]
	}
}
}
