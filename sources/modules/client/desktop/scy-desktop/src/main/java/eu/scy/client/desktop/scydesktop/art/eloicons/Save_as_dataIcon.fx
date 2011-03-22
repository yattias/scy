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
public class Save_as_dataIcon extends AbstractEloIcon {

public override function clone(): Save_as_dataIcon {
Save_as_dataIcon {
selected: selected
size: size
windowColorScheme: windowColorScheme
}
}

public override function createNode(): Node {

return Group {

			content: [
				Rectangle {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: null
					x: 0.42
					y: 0.14
					width: 40.0
					height: 40.0
				},
				Rectangle {
					fill: bind windowColorScheme.mainColor
					stroke: null
					x: 9.3
					y: 9.14
					width: 22.0
					height: 22.0
				},
				Rectangle {
					fill: null
					stroke: null
					x: 0.3
					y: 0.14
					width: 40.0
					height: 40.0
				},
				Line {
					fill: null
					stroke: Color.rgb(0xff,0x33,0x0)
					strokeWidth: 2.0
					strokeLineCap: StrokeLineCap.BUTT
					startX: 19.42
					startY: 1.14
					endX: 40.42
					endY: 1.14
				},
				Line {
					fill: null
					stroke: Color.rgb(0xff,0x33,0x0)
					strokeWidth: 2.0
					strokeLineCap: StrokeLineCap.BUTT
					startX: 19.42
					startY: 6.14
					endX: 40.42
					endY: 6.14
				},
				Line {
					fill: null
					stroke: Color.rgb(0xff,0x33,0x0)
					strokeWidth: 2.0
					strokeLineCap: StrokeLineCap.BUTT
					startX: 19.42
					startY: 11.14
					endX: 40.42
					endY: 11.14
				},
				Line {
					fill: null
					stroke: Color.rgb(0xff,0x33,0x0)
					strokeWidth: 2.0
					strokeLineCap: StrokeLineCap.BUTT
					startX: 19.42
					startY: 15.14
					endX: 40.42
					endY: 15.14
				},
				Line {
					fill: null
					stroke: Color.rgb(0xff,0x33,0x0)
					strokeWidth: 2.0
					strokeLineCap: StrokeLineCap.BUTT
					startX: 19.42
					startY: 20.14
					endX: 40.42
					endY: 20.14
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
         Save_as_dataIcon{
            windowColorScheme: windowColorScheme
           layoutX: 25
            layoutY: 25
         }
         Save_as_dataIcon{
            windowColorScheme: windowColorScheme
            layoutX: 75
            layoutY: 25
            selected: true
         }
      ]
	}
}
}
