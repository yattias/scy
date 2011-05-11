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
public class Exp_designIcon extends AbstractEloIcon {

public override function clone(): Exp_designIcon {
Exp_designIcon {
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
					content: "M18.18,7.47 C23.71,7.62 29.25,7.60 34.78,7.55 C37.37,7.52 36.92,3.84 34.43,3.86 C27.01,3.93 19.59,3.85 12.16,3.66 C10.26,3.60 7.68,3.23 5.92,4.30 C4.07,5.42 5.01,8.94 4.69,10.53 C3.43,16.90 11.20,17.28 18.95,17.04 C18.53,17.10 18.10,17.16 17.69,17.23 C14.24,17.81 9.11,17.74 6.59,20.45 C6.17,20.91 5.92,21.68 6.22,22.27 C8.02,25.81 11.80,26.26 15.46,26.58 C19.19,26.92 23.60,26.72 27.54,27.52 C27.11,27.77 26.75,27.97 26.60,28.04 C24.81,28.84 23.02,29.63 21.23,30.44 C17.00,32.37 13.04,34.66 8.91,36.77 C6.63,37.94 9.08,40.96 11.18,39.88 C16.22,37.30 21.12,34.59 26.32,32.28 C28.33,31.38 33.86,30.15 34.30,27.55 C34.44,26.69 33.98,25.90 33.20,25.52 C27.02,22.51 19.19,23.60 12.59,22.52 C8.16,21.80 16.67,21.18 16.14,21.26 C18.48,20.88 20.81,20.45 23.16,20.07 C26.97,19.44 30.79,19.14 34.54,18.15 C35.90,17.78 36.33,15.89 35.16,15.03 C32.27,12.92 28.64,13.19 25.18,13.15 C19.98,13.08 14.74,13.52 9.58,12.65 C9.86,12.70 8.50,7.38 8.72,7.36 C11.81,7.06 15.08,7.39 18.18,7.47 Z "
				},
				Rectangle {
					fill: bind windowColorScheme.secondColor
					stroke: bind windowColorScheme.mainColor
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					x: 16.69
					y: 13.05
					width: 8.98
					height: 22.3
				},
				Rectangle {
					fill: bind windowColorScheme.mainColor
					stroke: null
					x: 7.41
					y: 6.64
					width: 27.54
					height: 7.33
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M33.89,7.70 L33.89,12.91 L8.47,12.91 L8.47,7.70 Z M36.01,5.58 L6.35,5.58 L6.35,15.03 L36.01,15.03 Z "
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
         Exp_designIcon{
            windowColorScheme: windowColorScheme
           layoutX: 25
            layoutY: 25
         }
         Exp_designIcon{
            windowColorScheme: windowColorScheme
            layoutX: 75
            layoutY: 25
            selected: true
         }
      ]
	}
}
}
