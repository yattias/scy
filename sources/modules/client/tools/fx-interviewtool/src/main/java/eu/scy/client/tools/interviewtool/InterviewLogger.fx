/*
 * InterviewLogger.fx
 *
 * Created on 11.01.2010, 13:20:00
 */

package eu.scy.client.tools.interviewtool;

import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.actionlogging.api.IAction;
import eu.scy.actionlogging.api.IActionLogger;
import eu.scy.actionlogging.Action;

/**
 * @author kaido
 */

public def SHOW_HOME = "start_shown";
public def SHOW_PREPARE = "prepare_main_shown";
public def SHOW_QUESTION = "question_shown";
public def QUESTION_CHANGED = "question_changed";
public def SHOW_TOPICS = "topics_shown";
public def OBJECT_ADD = ":1_added";
public def OBJECT_REMOVE = ":1_removed";
/*
public def OBJECTS_BEFORE_SAVE = ":1s before save";
public def OBJECTS_AFTER_SAVE = ":1s after save";
public def OBJECTS_BEFORE_CANCEL = ":1s before editing canceled";
public def OBJECTS_AFTER_CANCEL = ":1s after editing canceled";
public def OBJECTS_FOCUS_LOST = ":1s before focus lost";
*/
public def SHOW_INDICATORS = "indicators_shown";
public def SHOW_ANSWERS = "answers_shown";
public def OTHER_NAMELY_CLICK = "answer_other_clicked";
public def SHOW_FORMULATION = "formulation_shown";
public def FORMULATION_CHANGED = "formulation_changed";
public def SHOW_STATUS = "status_shown";
public def SHOW_DESIGN = "design_shown";
public def SHOW_CONDUCT = "conduct_shown";
public def SHOW_CONDUCT_PREPARATION = "conduct_preparation_shown";
public def SHOW_CONDUCT_RECOMMENDATIONS = "conduct_recommendation_shown";
//public def OPEN_ELO = "open ELO";
//public def SAVE_ELO = "save ELO";
//public def SAVE_AS_ELO = "save as ELO";
public def ZOOM_TREE_IN = "tree_in_zoomed";
public def ZOOM_TREE_OUT = "tree_out_zoomed";
public def BACK_CLICKED = "back_clicked";
public def HOME_CLICKED = "home_clicked";
public def NEXT_CLICKED = "next_clicked";
public def ZOOM_SCHEMA_IN = "schema_in_zoomed";
public def ZOOM_SCHEMA_OUT = "schema_out_zoomed";
public def ZOOM_GUIDELINES_IN = "guidelines_in_zoomed";
public def ZOOM_GUIDELINES_OUT = "guidelines_out_zoomed";

var COUNT:Integer = 0;

/**
 * Action logging class for Interview Tool
 */
public class InterviewLogger {

public var topic:String;
public var indicator:String;
public-init var actionLogger:IActionLogger;
public-init var username:String;
public-init var toolname:String;
public-init var missionname:String;
public-init var sessionname:String;
public var eloUri:String = null;
def loggerCount:Integer = ++COUNT;
var action : IAction;

function write(action: IAction) {
    actionLogger.log(action);
}
function createBasicAction(type:String):IAction {
    var action:IAction = new Action();
    action.setUser(username);
    action.setType(type);
    //action.addContext(ContextConstants.tool, "{toolname}_{loggerCount}");
	// instances of the logger doesn't need to be identified (e.g. by numbering)
    action.addContext(ContextConstants.tool, "{toolname}");
    action.addContext(ContextConstants.mission, missionname);
    action.addContext(ContextConstants.session, sessionname);
    if (eloUri != null)
        action.addContext(ContextConstants.eloURI, eloUri);
    return action;
}
function initObjectAction(objectType:String,type:String,objects:InterviewObject[]) {
    var i:Integer = 0;
    if (type.equals("ADD")) {
        action = createBasicAction(OBJECT_ADD.replaceAll(":1", objectType));
        action.addAttribute(objectType, objects[0].toString());
    } else if (type.equals("REMOVE")) {
        action = createBasicAction(OBJECT_REMOVE.replaceAll(":1", objectType));
        action.addAttribute(objectType, objects[0].toString());
/*
    } else if (type.equals("BEFORE_SAVE")) {
        action = createBasicAction(OBJECTS_BEFORE_SAVE.replaceAll(":1", objectType));
        for (object in objects) {
            action.addAttribute("{objectType}_{++i}", object.toString());
        }
    } else if (type.equals("AFTER_SAVE")) {
        action = createBasicAction(OBJECTS_AFTER_SAVE.replaceAll(":1", objectType));
        for (object in objects) {
            action.addAttribute("{objectType}_{++i}", object.toString());
        }
    } else if (type.equals("BEFORE_CANCEL")) {
        action = createBasicAction(OBJECTS_BEFORE_CANCEL.replaceAll(":1", objectType));
        for (object in objects) {
            action.addAttribute("{objectType}_{++i}", object.toString());
        }
    } else if (type.equals("AFTER_CANCEL")) {
        action = createBasicAction(OBJECTS_AFTER_CANCEL.replaceAll(":1", objectType));
        for (object in objects) {
            action.addAttribute("{objectType}_{++i}", object.toString());
        }
    } else if (type.equals("FOCUS_LOST")) {
        action = createBasicAction(OBJECTS_FOCUS_LOST.replaceAll(":1", objectType));
        for (object in objects) {
            action.addAttribute("{objectType}_{++i}", object.toString());
        }
*/
    }
}
public function logBasicAction(type:String) {
    action = createBasicAction(type);
    write(action);
}
public function logQuestionChange(oldQuestion:String,newQuestion:String) {
    action = createBasicAction(QUESTION_CHANGED);
    action.addAttribute("oldQuestion", oldQuestion);
    action.addAttribute("newQuestion", newQuestion);
    write(action);
}
/*
public function logCopyInterviewSchemaToClipboard(interviewSchema:String) {
    action = createBasicAction(INTERVIEW_SCHEMA_TO_CLIPBOARD);
    action.addAttribute("interviewSchema", interviewSchema);
    write(action);
}
*/
public function logTopicAction(type:String,objects:InterviewObject[]) {
    initObjectAction("topic",type,objects);
    write(action);
}
public function logShowIndicators(topic:String) {
    action = createBasicAction(SHOW_INDICATORS);
    action.addAttribute("topic", topic);
    write(action);
}
public function logIndicatorAction(type:String,objects:InterviewObject[]) {
    initObjectAction("indicator",type,objects);
    action.addAttribute("topic", topic);
    write(action);
}
public function logShowAnswers(topic:String,indicator:String) {
    action = createBasicAction(SHOW_ANSWERS);
    action.addAttribute("topic", topic);
    action.addAttribute("indicator", indicator);
    write(action);
}
public function logAnswerAction(type:String,objects:InterviewObject[]) {
    initObjectAction("answer",type,objects);
    action.addAttribute("topic", topic);
    action.addAttribute("indicator", indicator);
    write(action);
}
public function logOtherNamelyAction(checked:Boolean) {
    action = createBasicAction(OTHER_NAMELY_CLICK);
    action.addAttribute("other_namely", checked.toString());
    action.addAttribute("topic", topic);
    action.addAttribute("indicator", indicator);
    write(action);
}
public function logShowFormulation(topic:String,indicator:String) {
    action = createBasicAction(SHOW_FORMULATION);
    action.addAttribute("topic", topic);
    action.addAttribute("indicator", indicator);
    write(action);
}
public function logFormulationChange(oldFormulation:String,newFormulation:String) {
    action = createBasicAction(FORMULATION_CHANGED);
    action.addAttribute("oldFormulation", oldFormulation);
    action.addAttribute("newFormulation", newFormulation);
    action.addAttribute("topic", topic);
    action.addAttribute("indicator", indicator);
    write(action);
}
}
