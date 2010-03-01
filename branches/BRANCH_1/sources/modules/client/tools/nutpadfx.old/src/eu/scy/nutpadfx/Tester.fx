package eu.scy.nutpadfx;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.text.Text;
import javafx.scene.effect.*;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import org.jfxtras.scene.shape.Star2;
import javafx.scene.control.Button;
import org.jfxtras.scene.border.*;
import javafx.scene.control.ListView;

var list: ListView = ListView {
        translateX: 110 
        layoutInfo: LayoutInfo {
            width: 200
            height: 200
            maxHeight : 300
        }
         items: [1..10]
       
    }
 
Stage {
   title: "Controls (JavaFX sample)"
   scene: Scene {
    fill: Color.web("#dbe9fd")
    width: 450
    height: 450
    content: [ 
		Flow {
			translateX: 110 
			hgap: 20 
			width: 400  
			content: [ 
				list
			] 
		} 
	]//Scene.content
 }//Scene
}//Stage