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
public class Data_syncIcon extends AbstractEloIcon {

public override function clone(): Data_syncIcon {
Data_syncIcon {
selected: selected
size: size
windowColorScheme: windowColorScheme
}
}

public override function createNode(): Node {

return Group {

			content: [
				Rectangle {
					fill: bind windowColorScheme.mainColorLight
					stroke: null
					x: 0.42
					y: 0.14
					width: 40.0
					height: 40.0
				},
				Rectangle {
					fill: null
					stroke: null
					x: 0.42
					y: 0.14
					width: 40.0
					height: 40.0
				},
				SVGPath {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 2.0
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					content: "M9.56,19.29 Q8.72,20.03 7.91,21.08 C5.44,24.16 2.97,30.01 21.49,28.01 C40.57,25.94 31.56,35.68 30.66,35.67 "
				},
				Rectangle {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 2.0
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					x: 27.83
					y: 33.74
					width: 10.25
					height: 5.4
				},
				Rectangle {
					transforms: [Transform.rotate( 10.86, 7.60, 4.45)]
					fill: bind windowColorScheme.mainColor
					stroke: null
					x: 6.69
					y: 0.81
					width: 1.82
					height: 7.28
				},
				Rectangle {
					transforms: [Transform.rotate( 10.84, 10.89, 5.08)]
					fill: bind windowColorScheme.mainColor
					stroke: null
					x: 9.98
					y: 1.44
					width: 1.82
					height: 7.28
				},
				Rectangle {
					transforms: [Transform.rotate( 10.86, 14.25, 5.73)]
					fill: bind windowColorScheme.mainColor
					stroke: null
					x: 13.34
					y: 2.09
					width: 1.82
					height: 7.28
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M14.49,16.20 C13.90,19.26 10.64,22.94 7.58,22.35 L7.57,22.35 C4.51,21.76 2.84,17.13 3.42,14.08 L4.26,9.73 C4.84,6.68 4.09,6.74 10.34,7.94 L10.34,7.94 C14.87,8.81 15.95,8.58 15.37,11.63 Z "
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
         Data_syncIcon{
            windowColorScheme: windowColorScheme
           layoutX: 25
            layoutY: 25
         }
         Data_syncIcon{
            windowColorScheme: windowColorScheme
            layoutX: 75
            layoutY: 25
            selected: true
         }
      ]
	}
}
}
