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
public class Collaboration_deniedIcon extends AbstractEloIcon {

public override function clone(): Collaboration_deniedIcon {
Collaboration_deniedIcon {
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
					content: "M39.65,32.59 C37.51,29.70 34.68,28.15 32.05,25.77 C30.02,23.93 28.15,21.93 26.27,19.94 C30.31,15.58 34.64,11.37 39.01,7.46 C43.01,3.89 36.26,-2.07 32.27,1.49 C28.09,5.23 24.07,9.26 20.19,13.39 C16.06,8.56 12.82,2.55 6.58,0.29 C2.77,-1.09 -1.49,5.89 3.08,8.54 C7.72,11.22 11.06,15.53 14.44,19.71 C12.29,22.26 10.31,24.86 7.58,26.90 C4.62,29.10 1.71,30.48 0.48,34.11 C-0.79,37.85 6.96,41.92 8.43,38.22 C9.47,35.64 12.91,34.15 15.04,32.40 C17.12,30.70 18.81,28.63 20.50,26.56 C22.15,28.32 23.81,30.09 25.55,31.75 C27.84,33.91 31.04,35.49 32.89,37.99 C36.32,42.62 42.84,36.91 39.65,32.59 Z "
				},
				Polygon {
					points: [31.45,40.17,12.45,40.17,12.03,34.24,31.21,33.68]
					fill: bind windowColorScheme.mainColor
					stroke: null
				},
				Line {
					fill: Color.rgb(0x0,0x15,0x79)
					stroke: Color.rgb(0x0,0x15,0x79)
					strokeWidth: 0.33
					startX: 20.88
					startY: 34.78
					endX: 20.64
					endY: 33.32
				},
				Polyline {
					fill: null
					stroke: Color.rgb(0x0,0x15,0x79)
					strokeWidth: 0.25
					strokeLineCap: StrokeLineCap.BUTT
					points: [14.07,15.51]
				},
				Polyline {
					fill: null
					stroke: Color.rgb(0x0,0x15,0x79)
					strokeLineCap: StrokeLineCap.BUTT
					points: [18.11,18.79]
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: Color.WHITE
					strokeWidth: 0.26
					strokeLineCap: StrokeLineCap.BUTT
					content: "M14.69,32.55 L27.73,32.40 L29.48,21.78 L30.06,10.64 Q29.11,10.29 27.43,11.34 Q25.71,12.41 25.90,15.79 L26.22,5.10 Q25.92,3.44 24.38,3.59 Q22.60,3.63 22.71,5.44 Q22.82,7.25 22.77,12.20 Q22.42,12.44 22.24,12.26 Q22.07,12.07 21.97,5.63 Q22.39,3.81 20.46,3.86 Q18.53,3.92 18.86,5.61 L18.91,13.37 Q18.69,13.85 18.46,13.61 Q18.22,13.37 17.93,7.02 C17.90,5.95 17.35,5.18 16.30,5.22 C15.26,5.26 14.62,6.14 14.65,7.21 L15.09,14.32 L15.03,14.35 Q14.77,14.98 14.53,14.48 Q14.30,13.99 13.89,8.52 Q13.89,7.11 12.36,7.26 Q10.83,7.42 10.78,8.61 L11.14,16.74 L11.89,22.28 Z "
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
         Collaboration_deniedIcon{
            windowColorScheme: windowColorScheme
           layoutX: 25
            layoutY: 25
         }
         Collaboration_deniedIcon{
            windowColorScheme: windowColorScheme
            layoutX: 75
            layoutY: 25
            selected: true
         }
      ]
	}
}
}
