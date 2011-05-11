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
public class InterviewIcon extends AbstractEloIcon {

public override function clone(): InterviewIcon {
InterviewIcon {
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
					content: "M35.20,4.94 C30.23,2.19 21.57,0.03 15.82,2.11 C15.75,2.10 15.69,2.08 15.60,2.10 C8.78,3.26 0.08,7.53 1.93,16.12 C2.61,19.26 5.49,23.55 8.23,25.21 C10.50,26.58 7.18,30.99 6.30,32.96 C5.59,34.52 4.34,36.41 5.04,37.96 Q5.30,38.52 6.23,38.82 Q12.46,35.83 14.94,33.41 C16.53,31.86 18.35,28.78 21.06,29.26 C22.80,29.56 24.42,29.53 26.19,29.40 C31.79,28.99 38.00,25.95 40.11,20.37 C42.21,14.78 40.48,7.86 35.20,4.94 Z M38.74,18.82 C38.08,22.47 33.52,25.49 30.48,26.57 C27.66,27.58 24.47,27.75 21.53,27.32 C20.71,27.20 19.12,26.70 18.30,27.34 C16.90,28.43 15.84,29.81 14.64,31.11 C12.53,33.40 11.06,35.23 8.30,36.58 C9.00,32.82 10.45,29.22 11.77,25.70 C11.91,25.32 11.68,24.81 11.33,24.63 C8.02,22.97 3.87,18.33 3.73,14.44 C3.52,8.27 9.05,5.48 14.23,4.33 C14.49,4.54 14.86,4.62 15.26,4.40 C20.36,1.57 29.26,4.67 34.26,6.71 C38.76,8.54 39.50,14.59 38.74,18.82 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: bind windowColorScheme.secondColor
					strokeLineCap: StrokeLineCap.BUTT
					content: "M21.42,3.91 C11.85,3.91 4.09,8.99 4.09,15.27 Q4.09,19.31 12.15,24.86 L9.32,35.09 L19.33,26.54 C20.02,26.59 20.71,26.62 21.42,26.62 C30.99,26.62 38.75,21.54 38.75,15.27 C38.75,8.99 30.99,3.91 21.42,3.91 Z "
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
         InterviewIcon{
            windowColorScheme: windowColorScheme
           layoutX: 25
            layoutY: 25
         }
         InterviewIcon{
            windowColorScheme: windowColorScheme
            layoutX: 75
            layoutY: 25
            selected: true
         }
      ]
	}
}
}
