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
public class Conclusion2Icon extends AbstractEloIcon {

public override function clone(): Conclusion2Icon {
Conclusion2Icon {
selected: selected
size: size
windowColorScheme: windowColorScheme
}
}

public override function createNode(): Node {

return Group {

			content: [
				Rectangle {
					fill: Color.WHITE
					stroke: null
					x: 9.42
					y: 12.14
					width: 21.0
					height: 19.0
				},
				SVGPath {
					fill: bind windowColorScheme.mainColorLight
					stroke: null
					content: "M39.97,9.55 C39.86,7.15 38.25,4.91 35.65,4.88 C25.69,4.75 16.33,4.56 6.54,2.25 C2.29,1.25 -0.29,5.93 1.17,8.85 C0.23,10.37 0.04,12.38 1.39,14.18 C3.94,17.57 3.91,22.00 3.46,25.99 C3.13,28.95 1.77,31.77 1.59,34.68 C1.42,37.35 3.54,38.96 5.91,39.35 C9.33,39.92 13.66,38.34 17.15,38.11 C22.69,37.74 28.27,38.86 33.66,40.02 C36.29,40.60 39.59,39.02 39.48,35.90 C39.18,27.14 40.37,18.27 39.97,9.55 Z M13.13,29.47 C12.76,29.50 12.37,29.57 11.96,29.64 C12.42,26.75 12.77,23.91 12.76,20.97 C12.74,18.02 11.85,14.93 10.49,12.15 C17.33,13.25 24.09,13.64 31.04,13.80 C31.00,19.27 30.58,24.77 30.47,30.25 C27.25,29.67 24.00,29.23 20.74,29.06 C18.21,28.92 15.65,29.24 13.13,29.47 Z "
				},
				Line {
					fill: bind windowColorScheme.thirdColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 2.75
					strokeLineCap: StrokeLineCap.ROUND
					strokeLineJoin: StrokeLineJoin.ROUND
					strokeMiterLimit: 4.0
					startX: 20.15
					startY: 4.75
					endX: 20.15
					endY: 34.15
				},
				Line {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 2.75
					strokeLineCap: StrokeLineCap.ROUND
					strokeLineJoin: StrokeLineJoin.ROUND
					strokeMiterLimit: 4.0
					startX: 4.41
					startY: 4.2
					endX: 35.9
					endY: 16.73
				},
				Line {
					fill: bind windowColorScheme.thirdColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.33
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					startX: 8.61
					startY: 11.09
					endX: 8.61
					endY: 5.67
				},
				Line {
					fill: bind windowColorScheme.thirdColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.35
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					startX: 31.92
					startY: 22.31
					endX: 31.92
					endY: 15.97
				},
				Polygon {
					points: [14.06,15.97,3.33,15.97,8.74,11.22]
					fill: bind windowColorScheme.mainColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 2.14
					strokeLineCap: StrokeLineCap.BUTT
					strokeLineJoin: StrokeLineJoin.ROUND
					strokeMiterLimit: 4.0
				},
				Polygon {
					points: [37.32,26.28,26.59,26.28,32.00,21.53]
					fill: bind windowColorScheme.mainColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 2.14
					strokeLineCap: StrokeLineCap.BUTT
					strokeLineJoin: StrokeLineJoin.ROUND
					strokeMiterLimit: 4.0
				},
				Polygon {
					points: [26.92,36.12,13.15,36.12,20.09,31.37]
					fill: bind windowColorScheme.mainColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 2.43
					strokeLineCap: StrokeLineCap.BUTT
					strokeLineJoin: StrokeLineJoin.ROUND
					strokeMiterLimit: 4.0
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
         Conclusion2Icon{
            windowColorScheme: windowColorScheme
           layoutX: 25
            layoutY: 25
         }
         Conclusion2Icon{
            windowColorScheme: windowColorScheme
            layoutX: 75
            layoutY: 25
            selected: true
         }
      ]
	}
}
}
