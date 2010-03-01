package eu.scy.nutpadfx;

import javafx.scene.control.ScrollBar;
import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;
import javafx.scene.input.KeyCode;
import javafx.util.Math;
import javafx.stage.*;
import javafx.ext.swing.*;
import javafx.scene.text.*;
import javafx.scene.effect.*;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.animation.*;
import javafx.scene.*;
import javafx.scene.shape.*;
import javafx.scene.paint.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.*;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import java.util.*;

var height = 300;
var width = 400;

var w: Number = 400 on replace oldw {
    if (oldw != w) {
        println("width resized to {padList.scrollBar.max}");
        println("width resized to {w}");
    }
}
var h: Number = 300 on replace oldh {
    if (oldh != h) {
        println("height resized to {h}");
    }
}

var padListItem : PadItem[];
    var padList = PadView {
        translateX: 5
        translateY: 5
        height: bind h - 35
        width: bind w
        listItem: bind padListItem
    };
    
    
       var index = 0;
       var i;
       
       var nums = [1,2,3,4,5,6,7,8,9,10,11,12,13,14,14,14,51,54,45,45];
       for(i in nums) {
          var pi = PadItem {
              listView: padList
              width: bind w - padList.scrollBar.width - 20
              //width: 180
           };
          pi.setStatus("{i} this is the number hey you  you are");
          insert pi into padListItem;
          index++;
        };
        
var b = Button {
      translateX: 5
      translateY: 5
     text: "Create one"
     action : createNewSession
}

function createNewSession() : Void {
     var pi = PadItem {
                  listView: padList
                  width: bind w - padList.scrollBar.width - 20
          };
          
      pi.setStatus("{i} this is new");
      insert pi into padListItem;
    
}
var stage = Stage {
     
     
     title: "Pad Test"
 	 width:  bind w with inverse
     height: bind h with inverse
     scene: Scene {
 
          fill: Color.RED
         content: Group {
             content: [padList]
             //content:  bind [HBox { content: [b,padList]}]
                       
                     }
                     
                     
                     
                     
     }
}