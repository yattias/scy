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
 
 
/**
 * @author anthonjp
 */

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
										  }
								fill :  HORIZONTAL   
				}]),
		]
                    
}
var width = 640;
var messageBoxHeight = 225;
var messageBoxes : Group[];
var someRows : Row[];
var messageList = Grid {
   
    rows : bind makeRows()
		
		
}



stage = Stage {
    title: "Nutpadfx"
    scene: ResizableScene {
        fill: Color.BLACK
        width: width
        height: 480
        content: Grid {
            
            rows : row([buttonList,messageList])
        }
        
    }
}

function makeRows() : Row[] {
    
    	//insert makeMessageBox( "first one" ) into messageBoxes;
    	
    	for (g in messageBoxes) {
        		    insert row([Cell {
        		     								content:  g
        		     								fill :  HORIZONTAL   
        		     								hgrow : ALWAYS
        		     				}]) 
        		     into someRows
        		}
    
    
    
    return someRows;
}

function makeMessageBox( message : String ) : Group {
	var group = Group {
	       var border: Border;
	       content: [
	           Rectangle {
	               
	               width: bind stage.scene.width - 160
	               height: bind messageBoxHeight
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
	               size: 15
	           },
	           x: 10,
	           y: 20
	           
	           content: message
	       }]
	}
	
	

	return group;

}

function showPopup( owner : Stage ) : Void {
    var radius = 200;
    
    popup = JFXDialog {
        title: "XMPP Message Creator"
        style: StageStyle.DECORATED
        owner : stage
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
           fill: Color.TRANSPARENT
        }
    }
    
}

function createNewSession() : Void {
    println("button called");
     var g = makeMessageBox("this is a new message");
     insert g into messageBoxes;
     makeRows();
    	println(messageBoxes);
    //showPopup(stage);

}

function sendMessage() : Void {
   
}

function cancel() : Void {
    println(toolSessionId);
	popup.getWindow().setVisible(false);
	
}









