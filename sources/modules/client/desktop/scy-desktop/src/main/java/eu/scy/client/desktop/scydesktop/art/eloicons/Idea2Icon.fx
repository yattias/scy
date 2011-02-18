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
public class Idea2Icon extends AbstractEloIcon {

public override function clone(): Idea2Icon {
Idea2Icon {
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
					content: "M38.41,8.82 C36.10,4.65 32.23,2.28 27.53,1.51 C27.17,1.31 26.76,1.14 26.30,1.00 C19.62,-0.95 14.72,0.03 8.82,3.19 C4.47,5.52 -0.36,10.30 0.64,15.07 C1.11,17.33 1.86,19.34 3.27,21.31 C4.05,22.40 9.28,27.46 8.58,28.67 C6.02,33.09 10.91,36.74 14.82,38.73 C19.20,40.97 25.08,40.78 28.10,37.00 C29.34,35.45 29.83,33.65 30.35,31.85 C31.26,28.68 34.95,26.51 37.17,24.04 C41.23,19.53 41.16,13.78 38.41,8.82 Z M23.71,23.20 C21.94,24.93 21.05,25.76 20.36,27.95 C20.26,28.25 20.39,27.77 20.30,28.08 C21.27,25.82 19.46,27.44 19.36,27.45 C20.59,23.55 17.34,22.51 14.77,19.49 C13.11,17.54 10.20,14.95 12.33,12.59 C13.93,10.83 16.65,9.32 19.35,9.13 C20.33,10.00 21.73,10.54 23.53,10.42 C27.42,10.18 29.87,12.87 29.72,16.19 C29.60,18.88 25.61,21.35 23.71,23.20 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.17
					strokeLineCap: StrokeLineCap.ROUND
					strokeLineJoin: StrokeLineJoin.ROUND
					strokeMiterLimit: 4.0
					content: "M20.68,3.54 C14.07,3.54 8.80,8.49 8.80,14.59 Q8.75,19.10 14.89,24.06 Q16.28,25.38 16.21,27.79 L25.06,27.79 Q24.99,25.38 26.38,24.06 Q32.52,19.10 32.47,14.59 C32.47,8.49 27.20,3.54 20.59,3.54 "
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.09
					strokeLineCap: StrokeLineCap.ROUND
					strokeLineJoin: StrokeLineJoin.ROUND
					strokeMiterLimit: 4.0
					startX: 16.75
					startY: 29.54
					endX: 24.73
					endY: 29.54
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.09
					strokeLineCap: StrokeLineCap.ROUND
					strokeLineJoin: StrokeLineJoin.ROUND
					strokeMiterLimit: 4.0
					startX: 16.75
					startY: 31.25
					endX: 24.73
					endY: 31.25
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.09
					strokeLineCap: StrokeLineCap.ROUND
					strokeLineJoin: StrokeLineJoin.ROUND
					strokeMiterLimit: 4.0
					startX: 16.75
					startY: 33.08
					endX: 24.73
					endY: 33.08
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.59
					strokeLineCap: StrokeLineCap.ROUND
					strokeLineJoin: StrokeLineJoin.ROUND
					strokeMiterLimit: 4.0
					content: "M24.80,34.93 Q24.80,35.19 24.63,35.67 Q22.52,37.57 20.62,37.57 Q18.62,37.57 16.71,35.66 Q16.44,35.12 16.44,34.93 Z "
				},
				Polyline {
					fill: null
					stroke: Color.rgb(0xff,0x5f,0xd7)
					strokeLineCap: StrokeLineCap.BUTT
					points: [26.63,6.85]
				},
				SVGPath {
					fill: Color.WHITE
					stroke: null
					content: "M17.88,26.09 Q18.04,24.68 17.48,24.21 C13.52,20.87 11.86,17.68 11.86,14.92 C11.86,10.59 14.13,5.28 21.83,5.28 C24.11,5.28 26.21,6.07 27.85,7.41 C27.96,7.50 28.07,7.59 28.17,7.68 Q20.30,3.90 15.75,9.89 C10.27,17.09 18.73,24.34 18.75,24.37 Q19.21,24.92 19.05,26.05 Z "
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
         Idea2Icon{
            windowColorScheme: windowColorScheme
           layoutX: 25
            layoutY: 25
         }
         Idea2Icon{
            windowColorScheme: windowColorScheme
            layoutX: 75
            layoutY: 25
            selected: true
         }
      ]
	}
}
}
