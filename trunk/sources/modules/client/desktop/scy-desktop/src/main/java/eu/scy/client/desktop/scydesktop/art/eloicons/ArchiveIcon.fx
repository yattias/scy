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
public class ArchiveIcon extends AbstractEloIcon {

public override function clone(): ArchiveIcon {
ArchiveIcon {
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
					content: "M38.61,3.68 C37.37,-0.35 28.69,0.27 25.77,0.29 C18.50,0.35 11.38,0.89 4.10,0.44 C3.22,0.39 2.54,0.79 2.10,1.37 C1.17,1.81 0.46,2.75 0.70,4.00 C2.02,10.84 1.64,18.02 1.39,24.95 C1.24,29.22 -0.92,36.26 2.61,39.50 C3.84,40.62 6.77,39.70 8.10,39.51 C11.91,38.96 15.65,38.77 19.50,38.76 C25.12,38.73 31.74,40.35 37.02,37.83 C38.64,37.05 39.10,35.09 39.39,33.51 C39.99,30.21 39.43,26.32 39.77,22.92 C40.42,16.40 40.56,10.03 38.61,3.68 Z M35.27,20.83 C35.03,23.37 34.76,25.88 34.65,28.43 C34.49,32.36 35.64,34.13 30.66,34.21 C25.96,34.29 21.27,33.85 16.57,33.92 C14.65,33.95 12.71,34.05 10.80,34.27 C7.72,34.62 5.48,36.37 5.72,32.47 C6.27,23.68 7.06,14.26 5.80,5.43 C9.99,5.63 14.18,5.69 18.38,5.51 C23.13,5.31 27.93,4.75 32.66,5.62 C36.84,6.39 35.55,17.85 35.27,20.83 Z "
				},
				Rectangle {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: null
					x: 8.45
					y: 8.65
					width: 23.52
					height: 22.93
				},
				Rectangle {
					fill: bind windowColorScheme.mainColor
					stroke: null
					x: 8.45
					y: 8.65
					width: 23.52
					height: 22.81
				},
				SVGPath {
					fill: null
					stroke: bind windowColorScheme.emptyBackgroundColor
					strokeWidth: 1.5
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					content: "M22.02,17.47 C22.02,17.70 21.17,17.88 20.11,17.88 C19.06,17.88 18.20,17.70 18.20,17.47 C18.20,17.25 19.06,17.07 20.11,17.07 C21.17,17.07 22.02,17.25 22.02,17.47 Z "
				},
				SVGPath {
					fill: null
					stroke: bind windowColorScheme.emptyBackgroundColor
					strokeWidth: 1.5
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					content: "M22.02,10.14 C22.02,10.36 21.17,10.54 20.11,10.54 C19.06,10.54 18.20,10.36 18.20,10.14 C18.20,9.91 19.06,9.73 20.11,9.73 C21.17,9.73 22.02,9.91 22.02,10.14 Z "
				},
				SVGPath {
					fill: null
					stroke: bind windowColorScheme.emptyBackgroundColor
					strokeWidth: 1.5
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					content: "M22.01,24.30 C22.01,24.52 21.16,24.70 20.11,24.70 C19.06,24.70 18.20,24.52 18.20,24.30 C18.20,24.07 19.06,23.89 20.11,23.89 C21.16,23.89 22.01,24.07 22.01,24.30 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M31.88,8.72 L31.88,31.52 L8.52,31.52 L8.52,8.72 Z M33.38,7.22 L7.02,7.22 L7.02,33.02 L33.38,33.02 Z "
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.emptyBackgroundColor
					strokeWidth: 1.5
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					startX: 7.02
					startY: 23.54
					endX: 33.42
					endY: 23.54
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.emptyBackgroundColor
					strokeWidth: 1.5
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					startX: 7.02
					startY: 16.75
					endX: 33.38
					endY: 16.75
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.emptyBackgroundColor
					strokeWidth: 1.5
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					startX: 7.02
					startY: 9.25
					endX: 33.38
					endY: 9.25
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.emptyBackgroundColor
					strokeWidth: 1.5
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					startX: 7.02
					startY: 30.75
					endX: 33.38
					endY: 30.75
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
         ArchiveIcon{
            windowColorScheme: windowColorScheme
           layoutX: 25
            layoutY: 25
         }
         ArchiveIcon{
            windowColorScheme: windowColorScheme
            layoutX: 75
            layoutY: 25
            selected: true
         }
      ]
	}
}
}
