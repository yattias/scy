/*
 * NewJavaFXEmpty.fx
 *
 * Created on 23.jan.2009, 14:08:29
 */

package eu.scy.elobrowser.tool.colemo;
import eu.scy.colemo.client.ColemoPanel;
import eu.scy.elobrowser.main.Roolo;
import eu.scy.elobrowser.tool.colemo.ColemoActionWrapper;
import eu.scy.elobrowser.tool.colemo.ColemoNode;
import eu.scy.elobrowser.ui.CommandText;
import eu.scy.scywindows.ScyWindow;
import java.awt.Dimension;
import java.lang.Object;
import javafx.ext.swing.SwingComponent;
import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URI;


public class ColemoNode extends CustomNode {

    //var whiteboardPanel:WhiteboardPanel;
   var colemoPanel : ColemoPanel;
   var colemoActionWrapper:ColemoActionWrapper;
	public var scyWindow:ScyWindow on replace {
		setScyWindowTitle()};

        
   public function loadElo(uri:URI){
       colemoActionWrapper.loadElo(uri);
       setScyWindowTitle();
   }

	function setScyWindowTitle(){
		if (scyWindow == null)  return;
		scyWindow.title = "SCYMapper";
	}

   public override function create(): Node {
      return Group {
         blocksMouse:true;
         content: [
            VBox{
               translateY:5;
               spacing:5;
               content:[
                  SwingComponent.wrap(colemoPanel)
               ]
            }
         ]
      };
   }
}


	public function createColemoNode(roolo:Roolo):ColemoNode{
		var colemoPanel= new ColemoPanel();

		colemoPanel.setPreferredSize(new Dimension(2000,2000));
		var colemoActionWrapper= new ColemoActionWrapper(colemoPanel);
        
		colemoActionWrapper.setRepository(roolo.repository);
        colemoActionWrapper.setEloFactory(roolo.eloFactory);
		colemoActionWrapper.setMetadataTypeManager(roolo.metadataTypeManager);
		//colemoActionWrapper.setEloFactory(roolo.eloFactory);
		return ColemoNode{
			colemoPanel:colemoPanel;
			colemoActionWrapper:colemoActionWrapper;
		}
	}

	public function createColemoWindow(roolo:Roolo):ScyWindow{
		return  createColemoWindow(ColemoNode.createColemoNode(roolo));
	}

	public function createColemoWindow(colemoNode:ColemoNode):ScyWindow{
        var colemoWindow = ColemoWindow{
			color:Color.YELLOWGREEN
			title:"Colemo-app"
			scyContent: colemoNode
			minimumWidth:320;
			minimumHeight:100;
			//width: 320;
			height: 250;
			cache:true;
      }
		colemoNode.scyWindow = colemoWindow;
		colemoWindow.width = 320;
		return colemoWindow;
	}

function run(){
   Stage {
		title: "Drawing node test"
		width: 250
		height: 250
		scene: Scene {
			content:ColemoNode{
         }

		}
	}



}

/**
 * @author Henrik
 */

// place your code here
