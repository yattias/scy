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
public class Get_feedback_newIcon extends AbstractEloIcon {

public override function clone(): Get_feedback_newIcon {
Get_feedback_newIcon {
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
					content: "M7.12,24.86 C7.30,25.02 5.60,27.97 5.31,28.48 C3.86,31.03 1.81,33.17 0.61,35.86 C0.26,36.63 0.30,37.30 0.56,37.83 C0.71,39.18 2.31,40.40 4.10,39.50 C9.35,36.86 14.82,34.60 20.32,32.58 C25.87,30.55 32.48,28.77 36.71,24.39 C39.87,21.11 41.00,15.70 40.27,11.34 C39.44,6.47 32.47,2.58 28.27,1.70 C20.69,0.10 1.73,-1.88 0.70,9.62 C0.26,14.47 1.11,18.48 4.26,22.23 C5.09,23.22 6.16,24.01 7.12,24.86 Z M5.35,11.00 C5.64,4.34 13.98,5.31 18.82,5.31 C25.12,5.32 31.33,6.59 35.19,12.03 C36.96,14.54 35.09,18.63 33.84,20.86 C32.14,23.86 27.75,24.95 24.83,26.28 C19.24,28.83 13.44,30.77 7.78,33.11 C10.57,29.27 13.72,24.58 11.05,21.83 C7.69,18.38 5.11,16.28 5.35,11.00 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M12.59,23.23 L11.52,22.66 C9.45,21.56 4.08,17.94 4.08,14.44 C4.09,9.13 11.50,4.64 20.27,4.64 C29.04,4.64 36.46,9.12 36.46,14.44 C36.46,19.75 29.04,24.24 20.27,24.24 C19.64,24.24 18.98,24.21 18.25,24.15 L17.65,24.11 L10.69,29.90 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M20.27,6.14 C28.09,6.14 34.96,10.02 34.96,14.44 C34.96,18.86 28.09,22.74 20.27,22.74 C19.68,22.74 19.05,22.71 18.37,22.66 L17.15,22.57 L16.22,23.35 L13.46,25.64 L13.70,24.80 L14.36,22.47 L12.22,21.33 C9.90,20.11 5.58,16.83 5.58,14.44 C5.59,10.02 12.45,6.14 20.27,6.14 M20.27,3.14 C10.50,3.14 2.59,8.20 2.58,14.44 Q2.58,19.62 10.81,23.98 L7.92,34.16 L18.14,25.65 C18.84,25.70 19.55,25.74 20.27,25.74 C30.04,25.74 37.96,20.68 37.96,14.44 C37.96,8.20 30.04,3.14 20.27,3.14 Z "
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
         Get_feedback_newIcon{
            windowColorScheme: windowColorScheme
           layoutX: 25
            layoutY: 25
         }
         Get_feedback_newIcon{
            windowColorScheme: windowColorScheme
            layoutX: 75
            layoutY: 25
            selected: true
         }
      ]
	}
}
}
