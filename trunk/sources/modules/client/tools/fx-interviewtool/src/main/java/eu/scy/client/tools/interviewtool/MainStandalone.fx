/*
 * MainStandalone.fx
 *
 * Created on 1.04.2010, 16:31:03
 */

package eu.scy.client.tools.interviewtool;

/**
 * @author kaido
 */

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.StringLocalizer;
import eu.scy.actionlogging.DevNullActionLogger;

var myLocale : java.util.Locale = new java.util.Locale("en", "US");
java.util.Locale.setDefault(myLocale);
StringLocalizer.associate("eu.scy.client.tools.interviewtool.resources.InterviewTool", "eu.scy.client.tools.interviewtool");
def node:InterviewToolNode = InterviewToolNode{};
node.interviewLogger = InterviewLogger{
   actionLogger: new DevNullActionLogger()
   username: "n/a"
   toolname: "interview"
   missionname: "n/a"
   sessionname: "n/a"
};
var w: Number = 620 on replace {
    if (w<620) {
        node.width = 600;
    } else {
        node.width=w-20;
    }
}
var h: Number = 520 on replace {
    if (h<520) {
        node.height = 500;
    } else {
        node.height=h-20;
    }
}
Stage {
	title : "##Interview tool"
        width: bind w with inverse
        height: bind h with inverse
	scene: Scene {
		content: [ node ]
	}
}
node.activateTreeNodeByValue(node.INTERVIEW_TOOL_HOME);
