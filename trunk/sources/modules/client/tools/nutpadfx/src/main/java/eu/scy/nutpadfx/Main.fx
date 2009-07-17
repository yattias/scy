package eu.scy.nutpadfx;

import javafx.stage.*;
import javafx.scene.text.*;
import org.jfxtras.scene.layout.LayoutConstants.*;
import org.jfxtras.scene.layout.Cell;
import org.jfxtras.scene.layout.Grid;
import org.jfxtras.stage.*;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
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
import java.lang.System;
import eu.scy.communications.datasync.properties.CommunicationProperties;
import eu.scy.communications.datasync.event.IDataSyncListener;
import eu.scy.communications.message.ISyncMessage;
import eu.scy.communications.message.impl.SyncMessageHelper;
import eu.scy.datasync.client.IDataSyncService;
import eu.scy.toolbroker.ToolBrokerImpl;

import java.text.SimpleDateFormat;
import org.apache.commons.lang.StringUtils;

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
def HARD_CODED_TOOL_NAME = "eu.scy.nutpadfx";
def HARD_CODED_USER_NAME = "obama";
def HARD_CODED_PASSWORD = "obama";

var bgcolor = Color.BLACK;
var props  = new CommunicationProperties();
var stage:Stage;
var popup : JFXDialog;
var textBoxLength = 45;
var toolSessionId:String;
var toolId:String;
var fromm:String;
var to:String;
var content:String;
var event:String;
var expiration = "{props.datasyncMessageDefaultExpiration}";
var persistenceId:String;
var currentSesson: String;

var messageBarText:String;

var tbi = new ToolBrokerImpl();
var dataSyncService: IDataSyncService;
var maps = new HashMap();
var sp1 = Rectangle {
    			   fill: Color.TRANSPARENT
	               height: 10

				};

var sp2 = Rectangle {
    			   fill: Color.TRANSPARENT
	               height: 10

				};
var sp3 = Rectangle {
    			   fill: Color.TRANSPARENT
	               height: 10

				};
var sp4 = Rectangle {
    			   fill: Color.TRANSPARENT
	               height: 10

				};


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
                             action: getAllSessions
			                 }
				   fill :  HORIZONTAL
				}]),
			row([Cell {
					content: sp1
					fill :  HORIZONTAL
						}]),
			row([Cell {
			      content:  Button {
			                 text: "Create Message"
                             action: createMessage
			                }
			      fill :  HORIZONTAL
			}]),
			row([Cell {
				content:  Button {
						   text: "Get All Messages"
                           action: getAllMessages
						  }
				fill :  HORIZONTAL
			}]),

				row([Cell {
							content:  sp2
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

class ListItem  {
     public var m: HashMap;

     override function toString() : String {
         var ts = "{m.get(TIME_STAMP)} ##{m.get(EVENT)}## {m.get(TOOL_SESSION_ID)} tool id:{m.get(TOOL_ID)} to:{m.get(TO)} from:{m.get(FROM)} content:{m.get(CONTENT)}";
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
    			   fill: Color.TRANSPARENT
	               width: bind buttonList.width
	               height: bind messageBoxHeight

				};
function run(){

dataSyncService = tbi.getDataSyncService();
dataSyncService.init(tbi.getConnection(HARD_CODED_USER_NAME, HARD_CODED_PASSWORD));
dataSyncService.addDataSyncListener(IDataSyncListener{
       override function handleDataSyncEvent(e) : Void {
           var syncMessage: ISyncMessage  = e.getSyncMessage();
           var date: java.util.Date = new java.util.Date(System.currentTimeMillis());
           var ts: java.sql.Timestamp = new java.sql.Timestamp(date.getTime());

                 currentSesson = syncMessage.getToolSessionId();

                 toolSessionId = syncMessage.getToolSessionId();
                 toolId = syncMessage.getToolId();
                 fromm = syncMessage.getFrom();
                 to = syncMessage.getTo();
                 content = syncMessage.getContent();
                 event = syncMessage.getEvent();
                 persistenceId = "{syncMessage.getPersistenceId()}";
                 expiration = Long.toString(syncMessage.getExpiration());

                 loadInfoMap();
           if( syncMessage.getEvent().equals(props.clientEventCreateSession) ) {
                println("\n-------- CREATE SESSION --------- {ts} \n{syncMessage.toString()}");

           } else if( syncMessage.getEvent().equals(props.clientEventGetSessions)) {
                var content = syncMessage.getContent();
                println("\n-------- GET SESSIONS --------- {ts}\n{content}");
           } else {
                println("\n-------- new message --------- {ts}\n{syncMessage.toString()}");
           }// if

        }
});


stage = Stage {
    title: "Nutpadfx 3000"
    width:  bind w with inverse
    height: bind h with inverse
    resizable : false;
    scene: Scene {
    	fill: LinearGradient {
    	                startX: 0.0,
    	                startY: 0.0,
    	                endX: 0.5,
    	                endY: 1.0
    	                stops: [
    	                            Stop {
    	                                offset: 0.7,
    	                                color: Color.BLACK
    	                            },
    	                            Stop {
    	                                offset: 1.0,
    	                                color: Color.WHITE
    	                            }
    	                        ]


    	            }
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

}






function clearPad() : Void {
	delete statuses;

	messageBarText = "";
}

function loadInfoMap() : Void {
        var date = new Date(System.currentTimeMillis());

         var format="MM/dd/yyyy HH:mm";
         var f = new SimpleDateFormat ( format ) ;
         var ts = new java.sql.Timestamp(date.getTime());
          


        

        maps.put(TIME_STAMP, f.format ( ts ));
        maps.put(TOOL_SESSION_ID,toolSessionId);
        maps.put(TOOL_ID,toolId);
        maps.put(FROM,fromm);
        maps.put(TO,to);
        maps.put(CONTENT,content);
        maps.put(EVENT,event);
        maps.put(PERSISTENCE_ID,persistenceId);
        maps.put(EXPIRATION,expiration);


        var li = ListItem {
            		m: maps;
        }

        insert li into statuses;
}

function checkSessions() : Void {
     if( StringUtils.trimToNull(toolSessionId) == null) {
        toolSessionId = "no current session available, create session first";
    } else {
        toolSessionId = currentSesson;
    }
    toolId = HARD_CODED_TOOL_NAME;
    fromm = HARD_CODED_USER_NAME;
}

function getAllSessions() : Void {

    event = props.clientEventGetSessions;
    checkSessions();
    showPopup(stage);
}

function getAllMessages() : Void {

    event = props.clientEventQuery;
    checkSessions();
    showPopup(stage);
}


function createMessage() : Void {
    event = props.clientEventCreateData;

    checkSessions();
    //SyncMessageCreateDialog d = new SyncMessageCreateDialog(NutpadDataSyncTestClient.this, HARD_CODED_USER_NAME, HARD_CODED_TOOL_NAME, props.clientEventSynchronize,currentSession);

    showPopup(stage);

}

function joinSession() : Void {
    event = props.clientEventJoinSession;
}

function createNewSession() : Void {
    dataSyncService.createSession(HARD_CODED_TOOL_NAME, HARD_CODED_USER_NAME);
    //event = props.clientEventCreateSession;

    //showPopup(stage);
    println("create new session called");
}

function sendMessage() : Void {

   var sm = SyncMessageHelper.createSyncMessage(toolSessionId, toolId, fromm, to, content, event,null, Long.parseLong(expiration));
   dataSyncService.sendMessage(sm);
   	popup.scene.stage.close();
	popup.owner.close();
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



function cancel() : Void {
    println("closing dialog");
	popup.scene.stage.close();
	popup.owner.close();
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


function showPopup( owner : Stage ) : Void {
    var clr = Color.WHITE;
    popup = JFXDialog {
        title: "XMPP Message Creator"
        style: StageStyle.DECORATED
        modal: false
        resizable : false
        scene: Scene {
            fill: LinearGradient {
                startX: 0.0,
                startY: 0.0,
                endX: 0.5,
                endY: 1.0
                stops: [
                            Stop {
                                offset: 0.5,
                                color: Color.BLACK
                            },
                            Stop {
                                offset: 1.0,
                                color: Color.WHITE
                            }
                        ]

            }
            content:  Grid {
                 rows: [
                     row([Label {
                             textFill: bind clr
                             text: "ToolSessionId:"
                          },
                          TextBox {
                             text: bind toolSessionId with inverse
                             columns: textBoxLength
                             selectOnFocus: true
                         }]),
                     row([Label {
                                textFill: bind clr
                     			text: "ToolId:"
                       	}, TextBox {
                                text: bind toolId with inverse
                                columns: textBoxLength
                                selectOnFocus: true
                            }]),
                     row([Label {
                               textFill: bind clr
                               text: "From:"
                         }, TextBox {
                               text: bind fromm with inverse
                               columns: textBoxLength
                               selectOnFocus: true
                        }]),
                   	row([Label {
                   	           textFill: bind clr
                               text: "To:"
                         }, TextBox {
                               text: bind to with inverse
                               columns: textBoxLength
                               selectOnFocus: true
                         }]),
                    row([Label {
                               textFill: bind clr
                               text: "Content:"
                         }, TextBox {
                               text: bind content with inverse
                               columns: textBoxLength
                               selectOnFocus: true
                         }]),
                    row([Label {
                               textFill: bind clr
                               text: "Event:"
                         }, TextBox {
                               text: bind event with inverse
                               columns: textBoxLength
                         	   selectOnFocus: true
                         }]),
					row([Label {
					     	   textFill: bind clr
							   text: "Expiration:"
						}, TextBox {
							   text: bind expiration with inverse
							   columns: textBoxLength
							   selectOnFocus: true
						}]),

                     row([ sp4,
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
                     		    		}
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