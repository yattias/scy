/*
 * DataToolNode.fx
 *
 * Created on 12-jan-2008, 21:46:32
 */

package eu.scy.elobrowser.tool.copex;

import eu.scy.elobrowser.main.Roolo;
import eu.scy.elobrowser.ui.CommandText;
import eu.scy.scywindows.ScyWindow;
import java.lang.Object;
import java.net.URI;
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
import javafx.scene.layout.Resizable;

/**
 * @author marjolaine bodin
 */

 // place your code here
public class CopexNode extends CustomNode{
   

   var copexPanel:ScyCopexPanel;
   var eloCopexWrapper:EloCopexWrapper;
	public var scyWindow:ScyWindow on replace {
		setScyWindowTitle()};

   public function loadElo(uri:URI){
      eloCopexWrapper.loadElo(uri);
	  setScyWindowTitle();
   }

	function setScyWindowTitle(){
		if (scyWindow == null)
		return;
		scyWindow.title = "Experimental procedure: {eloCopexWrapper.getDocName()}";
	}

   public override function create(): Node {
      return Group {
         blocksMouse:true;
         content: [
            VBox{
               translateY:5;
               spacing:5;
               content:[
                  HBox{
                     translateX:5;
                     spacing:5;
                     content:[
                        CommandText{
                           label:"New"
                           clickAction:function( e: MouseEvent ):Void {
                              eloCopexWrapper.newDataProcessAction();
										setScyWindowTitle();
                           }
                        }
                        CommandText{
                           label:"Load"
                           clickAction:function( e: MouseEvent ):Void {
                              eloCopexWrapper.loadDataProcessAction();
										setScyWindowTitle();
                           }
                        }
                        CommandText{
                           label:"Save"
                           clickAction:function( e: MouseEvent ):Void {
                              eloCopexWrapper.saveDataProcessAction();
										setScyWindowTitle();
                           }
                        }
                        CommandText{
                           label:"Save as"
                           clickAction:function( e: MouseEvent ):Void {
                              eloCopexWrapper.saveAsDataProcessAction();
										setScyWindowTitle();
                           }
                        }
                     ]
                  }
                  SwingComponent.wrap(copexPanel)
               ]
            }
         ]
      };
   }
}


	public function createCopexNode(roolo:Roolo):CopexNode{
		var copexPanel= new ScyCopexPanel();
		var eloCopexWrapper= new EloCopexWrapper(copexPanel);
		eloCopexWrapper.setRepository(roolo.repository);
		eloCopexWrapper.setMetadataTypeManager(roolo.metadataTypeManager);
		eloCopexWrapper.setEloFactory(roolo.eloFactory);
		return CopexNode{
			copexPanel:copexPanel;
			eloCopexWrapper:eloCopexWrapper;
		}
	}

	public function createCopexWindow(roolo:Roolo):ScyWindow{
		return
		createCopexWindow(CopexNode.createCopexNode(roolo));
	}

	public function createCopexWindow(copexNode:CopexNode):ScyWindow{
		var copexWindow = ScyWindow{
			color:Color.CHARTREUSE;
			title:"Experiment Design Tool"
			scyContent: copexNode
			minimumWidth:320;
			minimumHeight:100;
			cache:true;
      }
        copexWindow.openWindow(850, 650);
		copexNode.scyWindow = copexWindow;
		return copexWindow;
	}

function run(){
   Stage {
		title: "Experiment design tool test"
		width: 250
		height: 250
		scene: Scene {
			content:CopexNode{
         }

		}
	}

}
