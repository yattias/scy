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
public class Evaluation_reportIcon extends AbstractEloIcon {

public override function clone(): Evaluation_reportIcon {
Evaluation_reportIcon {
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
					content: "M33.70,8.27 C30.52,4.45 26.95,0.88 21.75,0.27 C16.16,-0.39 10.93,1.32 5.80,3.41 C3.67,4.28 3.35,6.33 4.09,7.96 C4.04,8.05 3.98,8.12 3.93,8.22 C-0.73,17.86 -0.12,26.67 8.30,34.09 C15.60,40.52 25.33,40.92 33.22,35.35 C37.21,32.53 39.91,27.82 40.51,23.04 C41.21,17.50 36.98,12.19 33.70,8.27 Z M28.64,29.88 C24.02,33.72 17.75,32.43 13.36,29.10 C6.51,23.90 7.33,17.47 10.64,10.63 C10.91,10.07 11.01,9.54 10.99,9.05 C16.93,6.87 22.63,6.06 27.27,11.72 C29.41,14.32 31.50,16.72 32.91,19.82 C34.72,23.81 31.47,27.53 28.64,29.88 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: bind windowColorScheme.secondColor
					strokeWidth: 2.98
					strokeLineCap: StrokeLineCap.BUTT
					content: "M31.80,22.30 C31.80,29.55 25.93,35.42 18.69,35.42 C11.44,35.42 5.57,29.55 5.57,22.30 C5.57,15.06 11.44,9.19 18.69,9.19 C25.93,9.19 31.80,15.06 31.80,22.30 Z "
				},
				Polyline {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 5.27
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					points: [7.10,10.72,16.93,30.73,37.99,5.10]
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
         Evaluation_reportIcon{
            windowColorScheme: windowColorScheme
           layoutX: 25
            layoutY: 25
         }
         Evaluation_reportIcon{
            windowColorScheme: windowColorScheme
            layoutX: 75
            layoutY: 25
            selected: true
         }
      ]
	}
}
}
