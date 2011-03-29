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
public class Get_feedbackIcon extends AbstractEloIcon {

public override function clone(): Get_feedbackIcon {
Get_feedbackIcon {
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
					content: "M7.12,24.56 C7.30,24.73 5.60,27.68 5.31,28.19 C3.86,30.74 1.81,32.88 0.61,35.56 C0.26,36.33 0.30,37.00 0.56,37.53 C0.71,38.89 2.31,40.10 4.10,39.20 C9.35,36.57 14.82,34.30 20.32,32.28 C25.87,30.25 32.48,28.48 36.71,24.10 C39.87,20.81 41.00,15.40 40.27,11.04 C39.44,6.18 32.47,2.28 28.27,1.40 C20.69,-0.19 1.73,-2.17 0.70,9.32 C0.26,14.17 1.11,18.18 4.26,21.93 C5.09,22.92 6.16,23.72 7.12,24.56 Z M5.35,10.71 C5.64,4.04 13.98,5.01 18.82,5.02 C25.12,5.02 31.33,6.29 35.19,11.73 C36.96,14.24 35.09,18.33 33.84,20.56 C32.14,23.56 27.75,24.65 24.83,25.98 C19.24,28.53 13.44,30.47 7.78,32.82 C10.57,28.97 13.72,24.28 11.05,21.53 C7.69,18.09 5.11,15.99 5.35,10.71 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: null
					content: "M12.59,22.93 L11.52,22.36 C9.45,21.26 4.08,17.64 4.08,14.14 C4.09,8.83 11.50,4.34 20.27,4.34 C29.04,4.34 36.46,8.83 36.46,14.14 C36.46,19.45 29.04,23.94 20.27,23.94 C19.64,23.94 18.98,23.91 18.25,23.86 L17.65,23.81 L10.69,29.60 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M20.27,5.84 C28.09,5.84 34.96,9.72 34.96,14.14 C34.96,18.56 28.09,22.44 20.27,22.44 C19.68,22.44 19.05,22.42 18.37,22.36 L17.15,22.27 L16.22,23.05 L13.46,25.35 L13.70,24.51 L14.36,22.17 L12.22,21.04 C9.90,19.81 5.58,16.53 5.58,14.14 C5.59,9.72 12.45,5.84 20.27,5.84 M20.27,2.84 C10.50,2.84 2.59,7.90 2.58,14.14 Q2.58,19.32 10.81,23.69 L7.92,33.86 L18.14,25.35 C18.84,25.41 19.55,25.44 20.27,25.44 C30.04,25.44 37.96,20.38 37.96,14.14 C37.96,7.90 30.04,2.84 20.27,2.84 Z "
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
         Get_feedbackIcon{
            windowColorScheme: windowColorScheme
           layoutX: 25
            layoutY: 25
         }
         Get_feedbackIcon{
            windowColorScheme: windowColorScheme
            layoutX: 75
            layoutY: 25
            selected: true
         }
      ]
	}
}
}
