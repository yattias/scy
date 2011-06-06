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
public class Search_2Icon extends AbstractEloIcon {

public override function clone(): Search_2Icon {
Search_2Icon {
selected: selected
size: size
windowColorScheme: windowColorScheme
}
}

public override function createNode(): Node {

return Group {

			content: [
				SVGPath {
					fill: Color.rgb(0xd2,0xe5,0xf6)
					stroke: null
					content: "M38.31,5.72 C34.69,-0.04 28.63,-0.27 22.13,1.01 C10.83,3.22 12.44,18.61 13.05,19.05 C8.41,23.03 -3.10,31.93 1.80,37.94 C6.78,44.03 17.28,30.07 21.44,25.74 C27.50,26.38 32.67,27.99 36.96,22.35 C40.39,17.83 41.44,10.71 38.31,5.72 Z M35.84,14.49 C35.61,19.91 30.81,23.28 26.14,23.69 C24.19,23.86 21.41,22.41 19.75,24.06 C17.03,26.75 14.33,29.45 11.35,31.82 C10.07,32.84 8.63,33.68 7.15,34.33 C6.72,33.64 6.29,32.97 5.86,32.28 C6.62,31.17 7.47,30.15 8.41,29.22 C11.05,26.43 14.05,23.98 16.83,21.34 C18.21,20.03 17.67,18.16 16.49,17.56 C18.56,10.94 21.42,2.93 29.59,4.73 C33.87,5.68 36.03,10.30 35.84,14.49 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M30.10,3.07 C28.89,2.65 27.64,2.44 26.43,2.44 C21.78,2.44 17.42,5.41 15.82,10.16 C14.74,13.35 15.13,16.69 16.61,19.43 C16.53,19.49 16.44,19.53 16.37,19.60 L3.32,32.14 C2.68,32.76 2.50,33.63 2.92,34.09 L5.72,37.12 C6.14,37.59 7.01,37.46 7.65,36.84 L20.70,24.31 C20.82,24.19 20.91,24.06 20.99,23.93 C21.55,24.24 22.14,24.52 22.76,24.74 C23.97,25.17 25.21,25.38 26.43,25.38 C31.08,25.38 35.42,22.39 37.03,17.66 C39.06,11.68 35.95,5.15 30.10,3.07 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: null
					content: "M34.16,16.64 C33.02,20.01 29.91,22.27 26.43,22.27 C25.52,22.27 24.62,22.11 23.75,21.81 C21.69,21.07 20.02,19.57 19.07,17.56 C18.11,15.55 17.98,13.29 18.70,11.18 C19.84,7.81 22.94,5.55 26.43,5.55 C27.34,5.55 28.23,5.71 29.11,6.01 C31.17,6.74 32.83,8.25 33.78,10.25 C34.74,12.26 34.87,14.53 34.16,16.64 Z "
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
         Search_2Icon{
            windowColorScheme: windowColorScheme
           layoutX: 25
            layoutY: 25
         }
         Search_2Icon{
            windowColorScheme: windowColorScheme
            layoutX: 75
            layoutY: 25
            selected: true
         }
      ]
	}
}
}
