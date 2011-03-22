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
					content: "M30.75,6.58 C23.42,1.66 11.77,2.96 3.49,3.17 C0.55,3.24 0.28,8.15 3.42,8.07 C9.67,7.91 15.92,7.72 22.10,8.78 C29.01,9.95 32.76,14.12 34.42,20.74 C35.15,23.69 35.35,28.93 32.62,30.99 C30.51,32.59 26.38,32.40 23.83,32.53 C17.37,32.87 10.23,33.56 4.34,30.41 C1.70,29.00 -0.74,33.37 1.91,34.78 C9.39,38.78 18.82,37.92 27.01,37.21 C34.81,36.53 39.24,32.84 39.44,24.80 C39.63,17.61 36.79,10.64 30.75,6.58 Z "
				},
				Polygon {
					points: [0.42,8.67,0.42,27.67,6.20,28.09,6.83,8.91]
					fill: bind windowColorScheme.mainColor
					stroke: null
				},
				Line {
					fill: Color.rgb(0x0,0x15,0x79)
					stroke: Color.rgb(0x0,0x15,0x79)
					strokeWidth: 0.33
					startX: 5.51
					startY: 20.24
					endX: 6.98
					endY: 20.48
				},
				Polyline {
					fill: null
					stroke: Color.rgb(0x0,0x15,0x79)
					strokeWidth: 0.25
					strokeLineCap: StrokeLineCap.BUTT
					points: [24.78,26.05]
				},
				Polyline {
					fill: null
					stroke: Color.rgb(0x0,0x15,0x79)
					strokeLineCap: StrokeLineCap.BUTT
					points: [21.50,23.02]
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: bind windowColorScheme.emptyBackgroundColor
					strokeWidth: 0.26
					strokeLineCap: StrokeLineCap.BUTT
					content: "M7.74,25.43 L7.90,12.39 L18.51,10.65 L29.66,10.06 Q30.01,11.02 28.96,12.69 Q27.89,14.41 24.51,14.23 L35.20,13.90 Q36.86,14.20 36.70,15.74 Q36.67,17.52 34.86,17.41 Q33.04,17.30 28.10,17.35 Q27.85,17.70 28.04,17.88 Q28.23,18.05 34.67,18.15 Q36.49,17.73 36.43,19.66 Q36.38,21.59 34.69,21.26 L26.93,21.21 Q26.45,21.43 26.69,21.67 Q26.93,21.90 33.28,22.19 C34.35,22.22 35.12,22.77 35.08,23.82 C35.03,24.87 34.15,25.50 33.08,25.47 L25.98,25.04 L25.95,25.09 Q25.32,25.35 25.81,25.59 Q26.31,25.83 31.78,26.23 Q33.19,26.23 33.03,27.76 Q32.88,29.29 31.69,29.34 L23.55,28.98 L18.02,28.23 Z "
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
