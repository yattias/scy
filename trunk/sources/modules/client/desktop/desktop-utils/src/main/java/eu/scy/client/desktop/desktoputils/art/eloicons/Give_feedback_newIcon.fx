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
public class Give_feedback_newIcon extends AbstractEloIcon {

public override function clone(): Give_feedback_newIcon {
Give_feedback_newIcon {
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
					content: "M33.72,15.19 C33.54,15.03 35.25,12.08 35.54,11.57 C36.98,9.02 39.03,6.88 40.23,4.20 C40.58,3.43 40.54,2.76 40.28,2.22 C40.13,0.87 38.53,-0.34 36.74,0.56 C31.49,3.19 26.02,5.45 20.52,7.47 C14.97,9.51 8.36,11.28 4.13,15.66 C0.97,18.94 -0.16,24.35 0.57,28.72 C1.40,33.58 8.37,37.48 12.57,38.36 C20.15,39.95 39.11,41.93 40.14,30.43 C40.58,25.58 39.73,21.58 36.58,17.82 C35.75,16.84 34.68,16.04 33.72,15.19 Z M35.50,29.05 C35.20,35.72 26.86,34.74 22.02,34.74 C15.72,34.73 9.51,33.47 5.65,28.02 C3.88,25.51 5.75,21.42 7.00,19.20 C8.70,16.19 13.09,15.11 16.01,13.77 C21.60,11.23 27.40,9.29 33.06,6.94 C30.27,10.78 27.12,15.48 29.79,18.22 C33.15,21.67 35.73,23.77 35.50,29.05 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: null
					content: "M20.57,35.42 C11.80,35.42 4.38,30.93 4.38,25.62 C4.38,20.30 11.80,15.82 20.57,15.82 C21.20,15.82 21.86,15.84 22.59,15.90 L23.19,15.94 L30.15,10.15 L28.25,16.83 L29.33,17.40 C31.39,18.50 36.76,22.12 36.76,25.61 C36.75,30.93 29.34,35.42 20.57,35.42 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M27.38,14.41 L27.14,15.25 L26.49,17.58 L28.62,18.72 C31.30,20.15 35.26,23.33 35.26,25.61 C35.25,30.04 28.39,33.92 20.57,33.92 C12.75,33.92 5.88,30.04 5.88,25.62 C5.88,21.19 12.75,17.32 20.57,17.32 C21.16,17.32 21.79,17.34 22.47,17.39 L23.69,17.49 L24.62,16.71 Z M32.92,5.89 L22.70,14.40 C22.00,14.35 21.29,14.32 20.57,14.32 C10.80,14.32 2.88,19.38 2.88,25.62 C2.88,31.86 10.80,36.92 20.57,36.92 C30.34,36.92 38.25,31.86 38.26,25.62 Q38.26,20.43 30.03,16.07 Z "
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
         Give_feedback_newIcon{
            windowColorScheme: windowColorScheme
           layoutX: 25
            layoutY: 25
         }
         Give_feedback_newIcon{
            windowColorScheme: windowColorScheme
            layoutX: 75
            layoutY: 25
            selected: true
         }
      ]
	}
}
}
