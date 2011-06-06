package eu.scy.nutpadfx;
import javafx.stage.*;
import javafx.ext.swing.*;
import javafx.scene.text.*;
import org.jfxtras.scene.*;
import org.jfxtras.scene.layout.LayoutConstants.*;
import org.jfxtras.scene.layout.Cell;
import org.jfxtras.scene.layout.Grid;
import org.jfxtras.stage.*;
import javafx.scene.effect.*;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.util.Math;
import javafx.animation.*;
import javafx.scene.*;
import javafx.scene.shape.*;
import javafx.scene.paint.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.*;
import org.jfxtras.scene.border.*;
import org.jfxtras.scene.layout.Row;
import javafx.scene.control.ListView;
import javafx.scene.input.*;
import java.util.*;
import java.util.Date;
import java.sql.*;
import java.lang.System;


var stage:Stage;
var popup : JFXDialog;
var textBoxLength = 45;
var toolSessionId:String;
var toolId:String;
var fromm:String;
var to:String;
var content:String;
var event:String;
var expiration:String;

var messageBarText:String;
 
var TOOL_SESSION_ID = "toolSessionId";
var TOOL_ID = "toolId";
var FROM = "from";
var TO = "to";
var CONTENT = "content";
var EVENT = "event";
var PERSISTENCE_ID = "persistenceId";
var EXPIRATION = "expiration";
var TYPE = "DATASYNC";
var TIME_STAMP = "timeStamp";

var bgcolor = Color.BLACK;
var spacer1 =  Text {
    font: Font {
        size: 5
    },
    x: 10,
    y: 30
    content: " "
}

var spacer2 =  Text {
    font: Font {
        size: 5
    },
    x: 10,
    y: 30
    content: " "
}

var spacer3 =  Text {
    font: Font {
        size: 5
    },
    x: 10,
    y: 30
    content: " "
}

var spacer4 =  Text {
    font: Font {
        size: 5
    },
    x: 10,
    y: 30
    content: " "
}

var buttonList = Grid {
   
    rows : [ 
    		
    		row([Cell {
                 content: Button {
                      		text: "Create New Session"
                      		action : createNewSession
                            }
                  fill :  HORIZONTAL
				}]),
			row([Cell {
			      content:  Button {
			                 text: "Get All Sessions"
			                 }
				   fill :  HORIZONTAL
				}]),
			row([Cell {
					content: spacer2
					fill :  HORIZONTAL
						}]),
			row([Cell {
			      content:  Button {
			                 text: "Create Message"
			                }
			      fill :  HORIZONTAL
			}]),
			row([Cell {
				content:  Button {
						   text: "Get All Messages"
						  }
				fill :  HORIZONTAL   
			}]),
		
				row([Cell {
							content:  spacer3
							fill :  HORIZONTAL   
				}]),
				row([Cell {
								content:  Button {
										   text: "Clear Pad"
										   action : clearPad
										  }
								fill :  HORIZONTAL   
				}]),
		]
                    
}

var w: Number = 800 on replace oldw {
    if (oldw != w) {
        println("width resized to {w}");
    }
}
var h: Number = 500 on replace oldh {
    if (oldh != h) {
        println("height resized to {h}");
    }
}

var messageBoxHeight = 100;
var messageBoxes : Group[];
var someRows : Row[];
var statuses : ListItem[];

class ListItem extends CustomNode {
     public var m: HashMap;
     
     override function toString() : String {
         var ts = "{m.get(TIME_STAMP)}-{m.get(EVENT)}-{m.get(TOOL_SESSION_ID)} tool id:{m.get(TOOL_ID)} to:{m.get(TO)} from:{m.get(FROM)} content:{m.get(CONTENT)}";
         return ts;
     }
     
     function toLongString() : String {
        var event = m.get(EVENT); 
        var ts = m.get(TIME_STAMP);
        var sid = m.get(TOOL_SESSION_ID);
        var ti = m.get(TOOL_ID);
        var to = m.get(TO);
        var fromm = m.get(FROM);
        var c = m.get(CONTENT);
        var exp = m.get(EXPIRATION);
        var p = m.get(PERSISTENCE_ID);
     	var ls = "{ts}\n------- {event} -------\nSession ID:{sid}\nTool ID:{ti}\nTo:{to}\nFrom:{fromm}\nContent:{c}\nFrom:{fromm}\nPersistence ID:{p}\nExpiration:{exp}";
     	
     	     	
     	     	//m.get(EVENT)
     	return ls;
     }
     
     
     
     
     
     
     override function create(): Node {
         Group {
           content: null
         }
       }
     
}

var list: ListView = ListView {
        translateX: 0
        layoutInfo: LayoutInfo {
            width: (w - buttonList.width ) - 10
            height: 300
        }
        items : bind statuses
        onMousePressed: function(e:MouseEvent):Void{
               var li: ListItem;
               li = list.selectedItem as ListItem;
               messageBarText = li.toLongString();
        }
        
        onKeyPressed: function(e:KeyEvent) {
                if(e.code == KeyCode.VK_UP) {
                    var li: ListItem;
                    li = list.selectedItem as ListItem;
                    messageBarText = li.toLongString();
                } else if(e.code == KeyCode.VK_DOWN) {
                     var li: ListItem;
                     li = list.selectedItem as ListItem;
                     messageBarText = li.toLongString();
                }
                
        }
       
}

var rectSpace = Rectangle {
	               fill: bind bgcolor
	               width: bind buttonList.width
	               height: bind messageBoxHeight
	               
				};

  
stage = Stage {
    title: "Nutpadfx 3000"
    width:  bind w with inverse
    height: bind h with inverse
    resizable : false;
    scene: Scene {
    	fill: bind bgcolor 
        content: Grid {
            
           
            rows : [
            	row([buttonList, Cell{ 
            					    content:  list
            					  hgrow: ALWAYS
            					   fill: BOTH
            		  }]),
           		row( [rectSpace,Cell{ hspan: 1
           				   content: makeMessageBox("this is it")
           				   hgrow: ALWAYS
           				   fill: BOTH
           				  }] )]
           				  
        }
        
    }
}





function showPopup( owner : Stage ) : Void {
    popup = JFXDialog {
        title: "XMPP Message Creator"
        style: StageStyle.DECORATED
        modal: true
        scene: Scene {
            content:  Grid {
                 rows: [
                     row([Label {
                             text: "ToolSessionId:"
                          }, 
                          TextBox {
                             text: bind toolSessionId with inverse
                             columns: textBoxLength
                             selectOnFocus: true
                         }]),
                     row([Label {
                     			text: "ToolId:"
                       	}, TextBox {
                                text: bind toolId with inverse
                                columns: textBoxLength
                                selectOnFocus: true
                            }]),
                     row([Label {
                               text: "From:"
                         }, TextBox {
                               text: bind fromm with inverse
                               columns: textBoxLength
                               selectOnFocus: true
                        }]),
                   	row([Label {
                               text: "To:"
                         }, TextBox {
                               text: bind to with inverse
                               columns: textBoxLength
                               selectOnFocus: true
                         }]),  
                    row([Label {
                               text: "Content:"
                         }, TextBox {
                               text: bind content with inverse
                               columns: textBoxLength
                               selectOnFocus: true
                         }]),   
                    row([Label {
                               text: "Event:"
                         }, TextBox {
                               text: bind event with inverse
                               columns: textBoxLength
                         	   selectOnFocus: true
                         }]),   
					row([Label {
							   text: "Expiration:"
						}, TextBox {
							   text: bind expiration with inverse
							   columns: textBoxLength
							   selectOnFocus: true
						}]),   
                                                                                                     
                     row([ spacer4,
                     	    Cell {
                     	    hpos : HPos.RIGHT
                     		content: Flow {
                     		    vertical : false
                     		    hgap : 2
                     		    vgap : 2
                     		    hpos : HPos.RIGHT
                     		    nodeHPos : HPos.RIGHT
                     		    content: [
                     		    		Button {
                     		    		    	text: "Send"
                     		    		        action : sendMessage
                     		    		},
                     		    		Button {
                     		    		        text: "Cancel"
                     		    		        action : cancel
                     		    		}
                     		    		
                     		    		]
                     			}
                     	    }
                     		
                     	])
                 ]
            }
        }
    }
    
}


function clearPad() : Void {
	delete statuses;
	
	messageBarText = "";
}

function createNewSession() : Void {
    println("button called");
    
    var maps = new HashMap();
    
    var date = new Date(System.currentTimeMillis());
    var ts = new java.sql.Timestamp(date.getTime());
    
    maps.put(TIME_STAMP, ts);
    maps.put(TOOL_SESSION_ID,"dfdfdfdfd.-dfdfdfdfd ");
    maps.put(TOOL_ID,"toolsid");
    maps.put(FROM,"frosm");
    maps.put(TO,"tssssso");
    maps.put(CONTENT,"content");
    maps.put(EVENT,"event");
    maps.put(PERSISTENCE_ID,"paersitence");
    maps.put(EXPIRATION,"exap");
    
    
    var li = ListItem {
        		m: maps;
    }

    insert li into statuses;
    //addToStatuses("create session", maps)
     //var g = makeMessageBox("this is a new message");
     //insert g into messageBoxes;
     //makeRows();
    	//println(messageBoxes);
    showPopup(stage);

}

function addToStatuses( eventType: String, toPrintMap : HashMap ) : Void {
   var c = toPrintMap.values();
 
   var status:String;
  
   var it = toPrintMap.keySet().iterator(); 
   println(toPrintMap.toString());
   var event = "{eventType} -- ";  
   
   var buffer:java.lang.StringBuffer;
   buffer = new java.lang.StringBuffer();
   buffer.append(event);
   while(it.hasNext()) {  
        var key = it.next() as String;  
        var v = toPrintMap.get(key) as String;
        
        if( key != "event" ) {
            
            buffer.append(key).append(": ").append(v).append(" ");
           
        	//status.concat(key.toString()).concat(": ").concat(v.toString()).concat("\n");  
        }
	   
   	}  
 	
 	 println(buffer.toString());
   	//insert buffer.toString() into statuses;
   	
}

function sendMessage() : Void {
   
}

function cancel() : Void {
    println(toolSessionId);
	popup.getWindow().setVisible(false);
	popup.close();
	
}


function makeMessageBox( message : String ) : Group {
	var group = Group {
	       var border: Border;
	       content: [
	           Rectangle {
	               
	               width:  bind list.width - 3
	               height: 165
	               fill:  LinearGradient {
	                    startX : 0.0
	                    startY : 0.0
	                    endX : 0.5
	                    endY : 1.0
	                    proportional: true
	                    stops : [ Stop {
	                        offset : 0.0
	                        color : Color.ORANGE
	                    }, Stop {
	                        offset : 1.0
	                        color : Color.WHITE
	                    }]
	                }
	                stroke: Color.YELLOW
	                arcWidth: 20  
	                arcHeight: 20
				}
	       , Text {
	           font: Font {
	               size: 11
	           },
	           x: 10,
	           y: 20
	           
	           content: bind messageBarText
	       }]
	}
	
	

	return group;

}










