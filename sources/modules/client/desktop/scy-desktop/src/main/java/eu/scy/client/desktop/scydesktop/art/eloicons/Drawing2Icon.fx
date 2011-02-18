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
public class Drawing2Icon extends AbstractEloIcon {

public override function clone(): Drawing2Icon {
Drawing2Icon {
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
					content: "M10.02,6.06 C16.93,0.76 27.26,2.05 33.22,8.80 C38.98,15.31 44.32,24.88 35.85,31.18 C32.63,33.58 29.58,35.95 25.48,36.02 C26.15,37.44 25.90,39.11 24.03,39.82 C14.30,43.52 2.09,35.22 1.23,24.75 C0.64,17.48 4.43,10.35 10.02,6.06 Z M23.26,17.59 C24.22,18.31 24.75,19.49 24.23,20.95 C23.73,22.35 22.21,25.52 23.25,26.99 C24.78,29.18 28.74,27.32 30.04,26.02 C32.89,23.21 28.50,19.83 25.85,18.54 C25.24,18.24 24.28,17.85 23.26,17.59 Z M10.65,15.18 C8.53,18.49 6.52,23.44 8.69,27.40 C10.56,30.83 15.12,34.73 19.31,34.23 C17.61,33.17 16.12,31.73 14.98,30.14 C10.34,23.66 10.37,11.93 19.92,10.83 C22.23,10.57 24.67,10.97 26.99,11.84 C25.91,10.93 24.70,10.21 23.34,9.86 C18.03,8.53 13.45,10.79 10.65,15.18 Z "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.01
					strokeLineCap: StrokeLineCap.ROUND
					strokeLineJoin: StrokeLineJoin.ROUND
					strokeMiterLimit: 4.0
					content: "M5.34,32.81 Q7.09,25.43 7.47,25.12 Q8.31,24.25 9.20,24.59 C12.29,25.79 9.15,26.98 10.88,28.98 C11.54,29.74 12.26,30.33 14.90,31.59 Q16.14,32.18 15.03,32.54 L7.84,35.33 "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.67
					strokeLineCap: StrokeLineCap.ROUND
					strokeLineJoin: StrokeLineJoin.ROUND
					strokeMiterLimit: 4.0
					content: "M35.15,12.83 L27.09,4.84 Q28.10,3.02 28.93,2.99 C29.75,2.96 36.63,10.27 36.98,10.98 Q37.34,11.69 35.15,12.83 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.35
					strokeLineCap: StrokeLineCap.ROUND
					strokeLineJoin: StrokeLineJoin.ROUND
					strokeMiterLimit: 4.0
					content: "M8.28,32.45 Q9.15,33.21 9.10,34.70 L5.75,36.07 L5.66,36.09 L5.55,36.07 L5.45,36.09 L5.34,36.08 L5.23,36.06 L5.16,36.03 L5.08,35.95 L5.03,35.89 L4.90,35.74 L4.85,35.52 L4.85,35.28 L4.86,35.06 L5.91,31.26 Q7.41,31.21 8.28,32.45 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.99
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					content: "M16.71,31.47 C16.04,31.42 17.22,30.86 15.04,29.74 C12.86,28.62 11.37,28.83 12.02,26.27 C12.82,23.11 9.15,23.51 8.82,23.13 Q8.50,22.75 26.23,5.99 L34.04,13.75 Q17.38,31.51 16.71,31.47 Z "
				},
				Polygon {
					points: [26.12,5.29,26.66,4.86,34.96,13.15,34.58,13.64]
					fill: Color.WHITE
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
         Drawing2Icon{
            windowColorScheme: windowColorScheme
           layoutX: 25
            layoutY: 25
         }
         Drawing2Icon{
            windowColorScheme: windowColorScheme
            layoutX: 75
            layoutY: 25
            selected: true
         }
      ]
	}
}
}
