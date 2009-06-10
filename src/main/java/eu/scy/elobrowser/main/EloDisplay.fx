/*
 * EloDisplay.fx
 *
 * Created on 6-okt-2008, 19:43:36
 */

package eu.scy.elobrowser.main;

import eu.scy.elobrowser.main.EloDisplay;
import eu.scy.elobrowser.main.Roolo;
import eu.scy.elobrowser.tool.drawing.DrawingNode;
import eu.scy.elobrowser.tool.scysimulator.SimQuestNode;
import eu.scy.elobrowser.tool.drawing.EloDrawingActionWrapper;
import eu.scy.client.tools.scysimulator.EloSimQuestWrapper;
import eu.scy.elobrowser.tool.colemo.ColemoActionWrapper;
import eu.scy.elobrowser.tool.dataProcessTool.EloDataToolWrapper;
import eu.scy.elobrowser.tool.dataProcessTool.DataToolNode;
import eu.scy.elobrowser.tool.textpad.EloTextpadWrapper;
import eu.scy.elobrowser.tool.textpad.TextpadNode;
import eu.scy.elobrowser.ui.SwingMenuItem;
import eu.scy.elobrowser.ui.SwingPopupMenu;
import eu.scy.scywindows.ScyDesktop;
import eu.scy.scywindows.ScyWindow;
import java.util.HashMap;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.ext.swing.SwingButton;
import javafx.ext.swing.SwingComponent;
import javafx.scene.CustomNode;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import roolo.elo.api.IELO;

/**
 * @author sikken
 */

 // place your code here


public class EloDisplay extends CustomNode {
	public var roolo:Roolo;
   public var title = "title" ;
   public var radius = 20;
   public var eloType = "unknown" on replace {
		updateImage()};
	public var elo:IELO;
	public var color = Color.CADETBLUE;
   public var textColor = Color.BLACK;
	public var textBackgroundColor = Color.TRANSPARENT;
   public var fillColor = Color.color(0,0.5,0,0.25);
	public var dragable = true;
	public var windowColor = Color.BLUE;
   var textFont =  Font {
      size: 12}

	var originalX:Number;
	var originalY:Number;
	var windowOpenVisible = false;
	var windowOpenStrokeWidth = 1;
	var windowOpenStrokeColor = bind windowColor;
	var windowOpenSize = 12;
   var closeMouseOverEffect:Effect = Glow{
      level:1}
	public var eloWindow:ScyWindow;

	var openMenuItem = SwingMenuItem{
		label:"Open"
		enabled:false;
		//action:openElo;
			}

	postinit{
		updateImage();
  };

	function updateImage() {
		var eloTypeChar = "?";
		var eloColor = Color.GRAY;
		if (EloDrawingActionWrapper.scyDrawType == eloType){
			eloTypeChar = "D";
			eloColor = Color.GREEN;
			eloWindow.minimumWidth=320;
			eloWindow.minimumHeight=100;
			cache=true;
		}
		else
		if (EloSimQuestWrapper.scyDatasetType == eloType){
			eloTypeChar = "V";
			eloColor = Color.BROWN;
		}
        else
        if (EloSimQuestWrapper.scySimConfigType == eloType){
			eloTypeChar = "C";
			eloColor = Color.GREEN;
		}
		else
		if (EloDataToolWrapper.scyPDSType == eloType){
			eloTypeChar = "S";
			eloColor = Color.GREENYELLOW;
		}
		else
		if (EloTextpadWrapper.scyTextType == eloType){
			eloTypeChar = "T";
			eloColor = Color.YELLOW;
		}
        else
		if (ColemoActionWrapper.scyMappingType == eloType){
			eloTypeChar = "C";
			eloColor = Color.BLUE;
		}

		eloWindow.eloType = eloTypeChar;
		color = eloColor;
	}

	public function clear(){
		eloWindow = null;
	}

	function setEloContent(scyWindow:ScyWindow){
		if (EloDrawingActionWrapper.scyDrawType == eloType){
			var drawingNode = DrawingNode.createDrawingNode(roolo);
			scyWindow.scyContent = drawingNode;
			drawingNode.loadElo(elo.getUri());
		}else
		if (EloDataToolWrapper.scyPDSType == eloType or  EloSimQuestWrapper.scyDatasetType == eloType){
			// ELO type PDS or DS => open with data process visualization tool
			var dataToolNode = DataToolNode.createDataToolNode(roolo);
			scyWindow.scyContent = dataToolNode;
            dataToolNode.loadElo(elo.getUri());
		}else
		if (EloTextpadWrapper.scyTextType == eloType){
			// ELO type is TEXT => open with textpad tool
			var textpadNode = TextpadNode.createTextpadNode(roolo);
			scyWindow.scyContent = textpadNode;
            textpadNode.loadElo(elo.getUri());
		}else
		if (EloSimQuestWrapper.scySimConfigType == eloType){
			// ELO type is SimConfig => open with Simulator tool
            var simquestNode = SimQuestNode.createSimQuestNode(roolo);
            scyWindow.scyContent = simquestNode;
            simquestNode.loadElo(elo.getUri());
		}
	}

	function hideEloWindow(scyWindow:ScyWindow):Void{
		 scyWindow.hideTo(translateX, translateY);
		windowOpenVisible = true;
	}

	function showEloWindow(scyWindow:ScyWindow):Void{
		 scyWindow.showFrom(translateX, translateY);
		windowOpenVisible = false;
	}

	function closeEloWindow(scyWindow:ScyWindow):Void{
		 scyWindow.closeIt();
	}

	function openEloWindow(){
		if (eloWindow.visible){
			ScyDesktop.getScyDesktop().activateScyWindow(eloWindow);
		}
		else {
			 showEloWindow(eloWindow);
		}
	}

	function openEloXml(){
		var eloXmlWindowId = "{elo.getUri().toString()}.xml";
		var eloXmlWindow = ScyDesktop.getScyDesktop().findScyWindow(eloXmlWindowId);
		if (eloXmlWindow == null){
			var textArea = new JTextArea();
			textArea.setEditable(false);
			textArea.setWrapStyleWord(true);
			textArea.setLineWrap(true);
			var scrollPane = new JScrollPane(textArea);
			var textNode = SwingComponent.wrap(scrollPane);
			eloXmlWindow = ScyWindow{
				id: eloXmlWindowId
				color:Color.color(0.8,0,0)
				title:"XML: {elo.getUri()}"
				scyContent: textNode
				visible:true
				width:300
				height:150
			}
//			eloXmlWindow.closeAction=closeEloWindow;
			ScyDesktop.getScyDesktop().addScyWindow(eloXmlWindow);
			eloXmlWindow.openFrom(translateX, translateY);
			textArea.setText(elo.getXml());
		}
		else {
			ScyDesktop.getScyDesktop().activateScyWindow(eloXmlWindow);
		}
	}

   public override function create(): Node {
		eloWindow = ScyWindow{
			title:bind title;
			color:bind color;
			allowClose:true;
			allowMinimize:true;
			allowResize:true;
			allowRotate:true;
			setScyContent:setEloContent;
		}
		return eloWindow;
	}
}


function run(){
	var eloDisplay:EloDisplay;
	eloDisplay=EloDisplay{
		translateX:50;
		translateY:50;
		radius:30
		title:"testing"
      //eloType:"??"
	}
   Stage {
      title: "EloDisplay test"
      scene: Scene {
         width: 200
         height: 200
         content: [
				eloDisplay
            SwingButton {
               text: "change"
               action: function() {
                  eloDisplay.eloType="scy/drawing";
                  eloDisplay.radius = 48;
						eloDisplay.title = "new title, but very long"
               }
            }
         ]
      }
	}
}
