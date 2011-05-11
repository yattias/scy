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
public class New_template_2Icon extends AbstractEloIcon {

public override function clone(): New_template_2Icon {
New_template_2Icon {
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
					content: "M35.25,26.57 C34.31,24.08 32.58,22.11 31.34,19.86 C33.72,14.82 36.39,9.82 39.03,4.92 C41.33,3.59 40.71,-0.92 37.94,0.47 C37.69,0.59 37.45,0.72 37.20,0.85 C36.70,0.85 36.20,1.06 35.79,1.58 C31.62,3.74 27.54,6.03 23.39,8.33 C20.99,9.67 21.53,9.84 19.90,9.06 C17.96,8.13 16.02,7.17 14.12,6.17 C11.19,4.62 7.96,3.06 5.41,0.93 C4.36,0.05 3.10,0.40 2.27,1.19 C1.22,1.36 0.34,2.19 0.61,3.68 C1.64,9.42 5.43,14.80 8.56,19.62 C8.68,19.80 8.81,19.98 8.93,20.17 C10.22,21.01 10.15,21.60 8.72,21.93 C7.82,23.81 6.75,25.58 5.71,27.38 C4.08,30.21 2.44,33.01 0.79,35.81 C-0.44,37.87 2.45,40.81 4.42,39.11 C8.42,35.68 13.48,33.49 17.92,30.66 C20.16,29.22 26.01,33.13 27.90,34.19 C30.05,35.41 37.05,40.42 37.97,40.12 C38.10,40.08 38.22,40.04 38.35,40.00 C40.33,39.34 40.29,36.44 38.95,35.45 C38.90,35.31 35.85,28.14 35.25,26.57 Z M26.54,28.58 C24.16,27.44 20.60,24.96 17.78,25.29 C15.26,25.58 12.26,28.38 10.08,29.62 C11.54,27.10 14.22,23.83 14.24,21.03 C14.25,18.60 11.52,16.17 10.32,14.23 C9.03,12.14 7.62,9.92 6.56,7.59 C7.77,8.30 9.01,8.98 10.24,9.67 C12.24,10.82 14.29,11.84 16.39,12.79 C17.37,13.24 19.05,14.63 20.26,14.38 C24.24,13.57 28.15,11.33 31.92,9.04 C30.87,11.16 29.80,13.28 28.65,15.36 C26.64,18.99 26.18,20.49 28.74,23.91 C30.29,25.98 31.03,28.57 31.87,31.06 C30.09,30.27 28.34,29.44 26.54,28.58 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M13.12,12.81 L19.10,16.09 L20.55,16.89 L22.01,16.09 L27.96,12.81 L24.68,18.72 L23.87,20.19 L24.68,21.65 L27.97,27.56 L21.99,24.28 L20.54,23.49 L19.09,24.28 L13.12,27.56 L16.40,21.65 L17.22,20.19 L16.40,18.72 Z M35.70,5.11 L20.55,13.44 L5.39,5.11 L13.76,20.19 L5.39,35.26 L20.54,26.93 L35.70,35.26 L27.32,20.19 Z "
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
         New_template_2Icon{
            windowColorScheme: windowColorScheme
           layoutX: 25
            layoutY: 25
         }
         New_template_2Icon{
            windowColorScheme: windowColorScheme
            layoutX: 75
            layoutY: 25
            selected: true
         }
      ]
	}
}
}
