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
public class Collaboration_invitationIcon extends AbstractEloIcon {

public override function clone(): Collaboration_invitationIcon {
Collaboration_invitationIcon {
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
					content: "M30.75,6.39 C23.42,1.47 11.77,2.76 3.49,2.97 C0.55,3.05 0.28,7.95 3.42,7.87 C9.67,7.71 15.92,7.53 22.10,8.58 C29.01,9.76 32.76,13.92 34.42,20.54 C35.15,23.49 35.35,28.73 32.62,30.80 C30.51,32.40 26.38,32.20 23.83,32.34 C17.37,32.68 10.23,33.36 4.34,30.22 C1.70,28.80 -0.74,33.17 1.91,34.58 C9.39,38.58 18.82,37.73 27.01,37.01 C34.81,36.33 39.24,32.65 39.44,24.60 C39.63,17.41 36.79,10.45 30.75,6.39 Z "
				},
				Polygon {
					points: [0.42,8.47,0.42,27.47,6.20,27.89,6.83,8.71]
					fill: bind windowColorScheme.mainColor
					stroke: null
				},
				Line {
					fill: Color.rgb(0x0,0x15,0x79)
					stroke: Color.rgb(0x0,0x15,0x79)
					strokeWidth: 0.33
					startX: 5.51
					startY: 20.04
					endX: 6.98
					endY: 20.29
				},
				Polyline {
					fill: null
					stroke: Color.rgb(0x0,0x15,0x79)
					strokeWidth: 0.25
					strokeLineCap: StrokeLineCap.BUTT
					points: [24.78,25.86]
				},
				Polyline {
					fill: null
					stroke: Color.rgb(0x0,0x15,0x79)
					strokeLineCap: StrokeLineCap.BUTT
					points: [21.50,22.82]
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: Color.WHITE
					strokeWidth: 0.26
					strokeLineCap: StrokeLineCap.BUTT
					content: "M7.74,25.23 L7.90,12.19 L18.51,10.45 L29.66,9.86 Q30.01,10.82 28.96,12.50 Q27.89,14.21 24.51,14.03 L35.20,13.70 Q36.86,14.01 36.70,15.55 Q36.67,17.32 34.86,17.22 Q33.04,17.11 28.10,17.15 Q27.85,17.51 28.04,17.68 Q28.23,17.86 34.67,17.96 Q36.49,17.54 36.43,19.47 Q36.38,21.39 34.69,21.07 L26.93,21.02 Q26.45,21.23 26.69,21.47 Q26.93,21.70 33.28,22.00 C34.35,22.03 35.12,22.57 35.08,23.62 C35.03,24.67 34.15,25.31 33.08,25.28 L25.98,24.84 L25.95,24.89 Q25.32,25.16 25.81,25.39 Q26.31,25.63 31.78,26.04 Q33.19,26.03 33.03,27.56 Q32.88,29.10 31.69,29.14 L23.55,28.79 L18.02,28.04 Z "
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
         Collaboration_invitationIcon{
            windowColorScheme: windowColorScheme
           layoutX: 25
            layoutY: 25
         }
         Collaboration_invitationIcon{
            windowColorScheme: windowColorScheme
            layoutX: 75
            layoutY: 25
            selected: true
         }
      ]
	}
}
}
