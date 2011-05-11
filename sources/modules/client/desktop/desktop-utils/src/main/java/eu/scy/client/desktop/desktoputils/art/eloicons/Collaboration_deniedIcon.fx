package eu.scy.client.desktop.desktoputils.art.eloicons;

import javafx.scene.Scene;
import javafx.stage.Stage;
import eu.scy.client.desktop.desktoputils.art.ScyColors;
import javafx.scene.Node;
import javafx.scene.Group;
import javafx.scene.shape.*;
import javafx.scene.paint.Color;
import javafx.scene.transform.Transform;
import eu.scy.client.desktop.desktoputils.art.WindowColorScheme;
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
					content: "M39.80,32.57 C37.66,29.68 34.83,28.12 32.20,25.74 C30.17,23.90 28.30,21.91 26.42,19.92 C30.46,15.56 34.79,11.34 39.16,7.44 C43.17,3.86 36.42,-2.09 32.43,1.47 C28.24,5.21 24.22,9.24 20.34,13.36 C16.21,8.54 12.97,2.53 6.73,0.27 C2.92,-1.11 -1.34,5.87 3.23,8.51 C7.87,11.19 11.21,15.50 14.60,19.68 C12.44,22.23 10.46,24.84 7.73,26.87 C4.77,29.07 1.87,30.45 0.63,34.09 C-0.64,37.83 7.11,41.89 8.59,38.19 C9.62,35.61 13.06,34.12 15.20,32.37 C17.27,30.67 18.96,28.60 20.65,26.53 C22.30,28.30 23.96,30.07 25.71,31.72 C27.99,33.89 31.19,35.46 33.04,37.96 C36.47,42.60 42.99,36.88 39.80,32.57 Z "
				},
				Polygon {
					points: [31.60,40.14,12.60,40.14,12.18,34.22,31.36,33.66]
					fill: bind windowColorScheme.mainColor
					stroke: null
				},
				Line {
					fill: Color.rgb(0x0,0x15,0x79)
					stroke: Color.rgb(0x0,0x15,0x79)
					strokeWidth: 0.33
					startX: 21.03
					startY: 34.76
					endX: 20.79
					endY: 33.3
				},
				Polyline {
					fill: null
					stroke: Color.rgb(0x0,0x15,0x79)
					strokeWidth: 0.25
					strokeLineCap: StrokeLineCap.BUTT
					points: [14.22,15.49]
				},
				Polyline {
					fill: null
					stroke: Color.rgb(0x0,0x15,0x79)
					strokeLineCap: StrokeLineCap.BUTT
					points: [18.26,18.77]
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: bind windowColorScheme.emptyBackgroundColor
					strokeWidth: 0.26
					strokeLineCap: StrokeLineCap.BUTT
					content: "M14.85,32.53 L27.88,32.38 L29.63,21.76 L30.21,10.61 Q29.26,10.26 27.58,11.31 Q25.86,12.38 26.05,15.77 L26.37,5.07 Q26.07,3.42 24.53,3.57 Q22.75,3.60 22.86,5.42 Q22.97,7.23 22.92,12.17 Q22.57,12.42 22.39,12.23 Q22.22,12.05 22.12,5.60 Q22.54,3.78 20.61,3.84 Q18.68,3.89 19.01,5.58 L19.06,13.35 Q18.84,13.82 18.61,13.58 Q18.38,13.34 18.08,7.00 C18.05,5.93 17.50,5.15 16.46,5.20 C15.41,5.24 14.77,6.12 14.80,7.19 L15.24,14.29 L15.19,14.32 Q14.92,14.95 14.68,14.46 Q14.45,13.96 14.04,8.49 Q14.04,7.08 12.51,7.24 Q10.98,7.39 10.93,8.58 L11.29,16.72 L12.04,22.25 Z "
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
