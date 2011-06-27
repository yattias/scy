/*
 * InterviewToolNode.fx
 *
 * Created on 8.12.2009, 22:15:57
 */

package eu.scy.client.tools.interviewtool;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Group;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.control.Button;
import javax.swing.tree.TreePath;
import javax.swing.tree.DefaultMutableTreeNode;
import java.util.Enumeration;
import eu.scy.client.tools.interviewtool.interviewtable.*;
import org.apache.log4j.Logger;
import javafx.scene.layout.Resizable;
import java.awt.Dimension;
import java.net.URL;
import eu.scy.client.common.richtexteditor.RichTextEditor;
import javafx.ext.swing.SwingComponent;
import javafx.geometry.HPos;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import java.util.ResourceBundle;
import java.lang.Exception;
import javafx.scene.layout.LayoutInfo;

/**
 * @author kaido
 */

/**
* Base class of the Interview Tool. Contains main logic of the Interview Tool.
*/
public class InterviewToolNode extends CustomNode, Resizable {
def normalFont = Font {
    name: "Arial"
    size: 14
}
def treeFont = normalFont;
def labelFont = normalFont;
protected def buttonFont = normalFont;
public def INTERVIEW_TOOL_HOME = 1;
def INTERVIEW_TOOL_PREPARE = 2;
def INTERVIEW_TOOL_QUESTION = 3;
def INTERVIEW_TOOL_TOPICS = 4;
def INTERVIEW_TOOL_TOPIC = 5;
def INTERVIEW_TOOL_INDICATOR = 6;
def INTERVIEW_TOOL_INDICATOR_FORMULATE = 7;
def INTERVIEW_TOOL_INDICATOR_STATUS = 8;
def INTERVIEW_TOOL_DESIGN = 9;
def INTERVIEW_TOOL_CONDUCT = 10;
def INTERVIEW_TOOL_CONDUCT_PREPARATION = 11;
def INTERVIEW_TOOL_CONDUCT_RECOMMENDATIONS = 12;
protected def rightWidth = 400;
def rightBottomHeight = 250;
def buttonBarHeight = 50;
def lowerNodesHeight = rightBottomHeight - buttonBarHeight;
def hPadding = 10;
def vPadding = hPadding;
def toolBottomOffset = 10;
protected var parentHeightOffset = 50;
protected def logger = Logger.getLogger("eu.scy.client.tools.interviewtool.InterviewToolNode");
var numbers:ResourceBundle = ResourceBundle.getBundle("eu.scy.client.tools.interviewtool.resources.InterviewToolNode");
var interviewStrings:InterviewStrings = InterviewStrings{};
public var interviewLogger: InterviewLogger;
var log = true;
//var guidePane : InterviewGuides = InterviewGuides{width:rightWidth, height:height-rightBottomHeight};
var guidePane : InterviewGuides = InterviewGuides{layoutInfo: LayoutInfo { width:rightWidth, height:height-rightBottomHeight }};
var lowerNodes : Node;
protected var treeMaximized : Boolean = false;
protected var schemaMaximized : Boolean = false;
protected var guidelinesMaximized : Boolean = false;
protected var topics: InterviewTopic[];
protected var interviewTree: InterviewTree = makeTree();
function makeTree() : InterviewTree {
    return InterviewTree{
        translateX: 0
        translateY: 0
//        width: width - rightWidth
//        height: height
        layoutInfo:LayoutInfo{width: width - rightWidth, height: height}
        font: treeFont
        root: InterviewTreeCell{
            text: "Root"
            cells: [
                InterviewTreeCell{
                    text: ##"Home"
                    value: INTERVIEW_TOOL_HOME
                }
                InterviewTreeCell{
                    text: ##"Prepare the interview"
                    value: INTERVIEW_TOOL_PREPARE
                    cells:[
                        InterviewTreeCell{
                            text: ##"Research question"
                            value: INTERVIEW_TOOL_QUESTION
                        }
                        InterviewTreeCell{
                            text: ##"Topics"
                            value: INTERVIEW_TOOL_TOPICS
                            cells: for (x in [0..sizeof topics-1]) InterviewTreeCell{
                                text: topics[x].topic
                                value: INTERVIEW_TOOL_TOPIC
                                topic: topics[x]
                                topicNo: x+1
                                cells: [
                                    for (y in [0..sizeof topics[x].indicators-1]) [
                                    InterviewTreeCell{
                                        text: "{interviewStrings.indicatorAnswers} \"{topics[x].indicators[y].indicator}\""
                                        value: INTERVIEW_TOOL_INDICATOR
                                        topic: topics[x]
                                        topicNo: x+1
                                        indicator: topics[x].indicators[y]
                                        indicatorNo: y+1
                                    }
                                    InterviewTreeCell{
                                        text: "{interviewStrings.indicatorFormulate} \"{topics[x].indicators[y].indicator}\""
                                        value: INTERVIEW_TOOL_INDICATOR_FORMULATE
                                        topic: topics[x]
                                        topicNo: x+1
                                        indicator: topics[x].indicators[y]
                                        indicatorNo: y+1
                                    }
                                    InterviewTreeCell{
                                        text: "{interviewStrings.indicatorStatus} \"{topics[x].indicators[y].indicator}\""
                                        value: INTERVIEW_TOOL_INDICATOR_STATUS
                                        topic: topics[x]
                                        topicNo: x+1
                                        indicator: topics[x].indicators[y]
                                        indicatorNo: y+1
                                    }
                                    ]
                                ]
                            }
                        }
                        InterviewTreeCell{
                            text: ##"Design"
                            value: INTERVIEW_TOOL_DESIGN
                        }
                    ]
                }
                InterviewTreeCell{
                    text: ##"Conduct the interview"
                    value: INTERVIEW_TOOL_CONDUCT
                    cells: [
                        InterviewTreeCell{
                            text: ##"Preparation is important"
                            value: INTERVIEW_TOOL_CONDUCT_PREPARATION
                        }
                        InterviewTreeCell{
                            text: ##"Recommendations"
                            value: INTERVIEW_TOOL_CONDUCT_RECOMMENDATIONS
                        }
                    ]
                }
            ]
        }
        action: function(obj: Object):Void {
            interviewTreeClick(obj);
        }
    }
};
function interviewTreeClick(obj: Object):Void {
    if ((obj as InterviewTreeCell).value==INTERVIEW_TOOL_HOME) {
        showHome();
    } else if ((obj as InterviewTreeCell).value==INTERVIEW_TOOL_PREPARE) {
        showPrepare();
    } else if ((obj as InterviewTreeCell).value==INTERVIEW_TOOL_QUESTION) {
        showQuestion();
    } else if ((obj as InterviewTreeCell).value==INTERVIEW_TOOL_TOPICS) {
        showTopics();
    } else if ((obj as InterviewTreeCell).value==INTERVIEW_TOOL_TOPIC) {
        showTopic((obj as InterviewTreeCell));
    } else if ((obj as InterviewTreeCell).value==INTERVIEW_TOOL_INDICATOR) {
        showIndicator((obj as InterviewTreeCell));
    } else if ((obj as InterviewTreeCell).value==INTERVIEW_TOOL_INDICATOR_FORMULATE) {
        showIndicatorFormulate((obj as InterviewTreeCell));
    } else if ((obj as InterviewTreeCell).value==INTERVIEW_TOOL_INDICATOR_STATUS) {
        showIndicatorStatus((obj as InterviewTreeCell));
    } else if ((obj as InterviewTreeCell).value==INTERVIEW_TOOL_DESIGN) {
        showDesign();
    } else if ((obj as InterviewTreeCell).value==INTERVIEW_TOOL_CONDUCT) {
        showConduct();
    } else if ((obj as InterviewTreeCell).value==INTERVIEW_TOOL_CONDUCT_PREPARATION) {
        showConductPreparation();
    } else if ((obj as InterviewTreeCell).value==INTERVIEW_TOOL_CONDUCT_RECOMMENDATIONS) {
        showConductRecommendations();
    }
}
// javafx bug, no refresh :(
function refreshStage() {
    var txt: Text = Text{};
    insert txt into content;
    delete txt from content;
    resizeContent();
}
function showHome() : Void {
    interviewLogger.logBasicAction(InterviewLogger.SHOW_HOME);
    guidePane.setText(interviewStrings.guideHome);
    lowerNodes = null;
    refreshStage();
}
function showPrepare() : Void {
    interviewLogger.logBasicAction(InterviewLogger.SHOW_PREPARE);
    guidePane.setText(##"Prepare the interview");
    lowerNodes = null;
    refreshStage();
}
public var question: String;
var questionBefore: String;
var questionAfter: String on replace {if (not questionBefore.equals(questionAfter)) {interviewLogger.logQuestionChange(questionBefore,questionAfter);}};
function showQuestion() {
    interviewLogger.logBasicAction(InterviewLogger.SHOW_QUESTION);
    guidePane.setTextFromFile(interviewStrings.guideQuestionFileName);
    lowerNodes = Group {content: [
        Text {
            font : labelFont
            wrappingWidth: rightWidth-2*hPadding
            x: hPadding
            y: 30
            content: ##"Research question"
        }
        InterviewTextArea {
//                width: rightWidth-2*hPadding
//                height: 150
                layoutInfo: LayoutInfo{width: rightWidth-2*hPadding, height: 150}
                translateX: hPadding
                translateY: 50
                font: normalFont
                text: bind question with inverse
                textBefore: bind questionBefore with inverse
                textAfter: bind questionAfter with inverse
            }
    ]};
    refreshStage();
}
protected var refreshTree: function() =
    function() {
        var selectedValue: InterviewTreeCell = interviewTree.selectedValue as InterviewTreeCell;
        interviewTree = makeTree();
        var e : Enumeration = (interviewTree.model.getRoot() as DefaultMutableTreeNode).preorderEnumeration();
        var parentNode: DefaultMutableTreeNode = null;
        var found = false;
        while (e.hasMoreElements()) {
            var node: DefaultMutableTreeNode = e.nextElement() as DefaultMutableTreeNode;
            if (selectedValue.value==INTERVIEW_TOOL_TOPICS and
                selectedValue.value==(node.getUserObject() as InterviewTreeCell).value
                or
                selectedValue.value==INTERVIEW_TOOL_TOPIC and
                selectedValue.value==(node.getUserObject() as InterviewTreeCell).value and
                selectedValue.topic.id==(node.getUserObject() as InterviewTreeCell).topic.id
                or
                selectedValue.value==INTERVIEW_TOOL_INDICATOR and
                selectedValue.value==(node.getUserObject() as InterviewTreeCell).value and
                selectedValue.topic.id==(node.getUserObject() as InterviewTreeCell).topic.id and
                selectedValue.indicator.id==(node.getUserObject() as InterviewTreeCell).indicator.id) {
                log = false;
                activateTreeNode(node);
                log = true;
                found = true;
                break;
            }
            if (selectedValue.value==INTERVIEW_TOOL_TOPIC and
                (node.getUserObject() as InterviewTreeCell).value==INTERVIEW_TOOL_TOPICS
                or
                selectedValue.value==INTERVIEW_TOOL_INDICATOR and
                (node.getUserObject() as InterviewTreeCell).value==INTERVIEW_TOOL_TOPIC and
                (node.getUserObject() as InterviewTreeCell).topic.id==selectedValue.topic.id) {
                parentNode = node;
                }
        }
        if (not found and parentNode != null) {
            activateTreeNode(parentNode);
        }
    }
var refreshTopicsAndTree: function() =
    function() {
        topics = objects as InterviewTopic[];
        refreshTree();
    };
var nextID: Integer = 1;
var objects: InterviewObject[];
function showTopics() : Void {
    if (log)
        interviewLogger.logBasicAction(InterviewLogger.SHOW_TOPICS);
    guidePane.setTextFromFile(interviewStrings.guideTopicsFileName,
        ["__QUESTION__"], [question]);
    objects = topics;
    lowerNodes = InterviewTableEditor {
        translateX: hPadding
        translateY: 10
        oldObjects: bind objects with inverse;
        classType: "InterviewTopic"
        headerText: ##"Topics we need information about"
        nextID: bind nextID with inverse
        refreshAction: refreshTopicsAndTree
        logAction: interviewLogger.logTopicAction
        width: rightWidth - 2*hPadding
        height: 150
//        layoutInfo: LayoutInfo{width: rightWidth - 2*hPadding,height: 150}
        font: normalFont
        headerFont: labelFont
    }
    refreshStage();
}
var refreshTopicAndTree: function() =
    function() {
        topics[topicNo-1].indicators = objects as InterviewIndicator[];
        refreshTree();
    };
var topicNo: Integer;
var topicNoStr: String;
function showTopic(cell: InterviewTreeCell) {
    if (log)
        interviewLogger.logShowIndicators(cell.topic.topic);
    topicNo = cell.topicNo;
    try {
        topicNoStr = numbers.getString(topicNo.toString());
    } catch (e: Exception) {
        topicNoStr = "{topicNo.toString()}.";
    }
    guidePane.setTextFromFile(interviewStrings.guideIndicatorsFileName,
        ["__TOPIC_NO__","__TOPIC__"],
        [topicNoStr,cell.topic.topic]);
    objects = cell.topic.indicators;
    interviewLogger.topic = cell.topic.topic;
    lowerNodes = InterviewTableEditor {
        translateX: hPadding
        translateY: 10
        oldObjects: bind objects with inverse;
        classType: "InterviewIndicator"
        headerText: "{interviewStrings.indicatorsOf} \"{cell.topic.topic}\""
        nextID: bind nextID with inverse
        refreshAction: refreshTopicAndTree
        logAction: interviewLogger.logIndicatorAction
        width: rightWidth - 2*hPadding
        height: 150
//        layoutInfo: LayoutInfo{width: rightWidth - 2*hPadding,height: 150}
        font: normalFont
        headerFont: labelFont
    }
    refreshStage();
}
var refreshIndicator: function() =
    function() {
        topics[topicNo-1].indicators[indicatorNo-1].answers = (objects as InterviewAnswer[]);
        topics[topicNo-1].indicators[indicatorNo-1].answerIncludeNamely = namely;
    };
var indicatorNo: Integer;
var indicatorNoStr: String;
var namely: Boolean;
function showIndicator(cell: InterviewTreeCell) {
    if (log)
        interviewLogger.logShowAnswers(cell.topic.topic,cell.indicator.indicator);
    topicNo = cell.topicNo;
    indicatorNo = cell.indicatorNo;
    try {
        indicatorNoStr = numbers.getString(indicatorNo.toString());
    } catch (e: Exception) {
        indicatorNoStr = "{indicatorNo.toString()}.";
    }
    guidePane.setTextFromFile(interviewStrings.guideAnswersFileName,
        ["__INDICATOR_NO__","__TOPIC__","__INDICATOR__"],
        [indicatorNoStr,cell.topic.topic,cell.indicator.indicator]);
    objects = cell.indicator.answers;
    namely = cell.indicator.answerIncludeNamely;
    interviewLogger.topic = cell.topic.topic;
    interviewLogger.indicator = cell.indicator.indicator;
    lowerNodes = InterviewTableEditor {
        translateX: hPadding
        translateY: 10
        oldObjects: bind objects with inverse;
        classType: "InterviewAnswer"
        headerText: "{interviewStrings.answerOptions} \"{cell.indicator.indicator}\""
        nextID: bind nextID with inverse
        refreshAction: refreshIndicator
        logAction: interviewLogger.logAnswerAction
        logNamelyAction: interviewLogger.logOtherNamelyAction
        width: rightWidth - 2*hPadding
        height: 120
//        layoutInfo: LayoutInfo{width: rightWidth - 2*hPadding,height: 120}
        font: normalFont
        headerFont: labelFont
        namelyShow: true
        namelyChecked: bind namely with inverse
    }
    refreshStage();
}
var formulation: String on replace {
        topics[topicNo-1].indicators[indicatorNo-1].formulation = formulation;
    };
var formulationBefore: String;
var formulationAfter: String on replace {
    if (not formulationBefore.equals(formulationAfter)) {
        interviewLogger.logFormulationChange(formulationBefore,formulationAfter);
    }
};
function showIndicatorFormulate(cell: InterviewTreeCell) {
    interviewLogger.logShowFormulation(cell.topic.topic,cell.indicator.indicator);
    topicNo = cell.topicNo;
    indicatorNo = cell.indicatorNo;
    formulation = cell.indicator.formulation;
    guidePane.setTextFromFile(interviewStrings.guideFormulationFileName,
        ["__INDICATOR__"], [cell.indicator.indicator]);
    lowerNodes = Group {content: [
        Text {
            font : labelFont
            wrappingWidth: rightWidth-2*hPadding
            x: hPadding
            y: 30
            content: "{interviewStrings.indicatorFormulation} \"{cell.indicator.indicator}\""
        }
        InterviewTextArea {
//                width: rightWidth - 2*hPadding
//                height: 150
                layoutInfo: LayoutInfo{width: rightWidth - 2*hPadding,height: 150}
                translateX: hPadding
                translateY: 50
                font: normalFont
                text: bind formulation with inverse
                textBefore: bind formulationBefore with inverse
                textAfter: bind formulationAfter with inverse
            }
    ]};
    refreshStage();
}
function showIndicatorStatus(cell: InterviewTreeCell) {
    interviewLogger.logBasicAction(interviewLogger.SHOW_STATUS);
    guidePane.setText("{interviewStrings.guideIndicatorStatusBeginning} \"{cell.indicator.indicator}\".");
    lowerNodes = null;
    refreshStage();
}
protected def schemaEditor:RichTextEditor = new RichTextEditor(false, false, true);
var wrappedSchemaEditor:SwingComponent;
def zoomInImage:Image = Image {url: "{__DIR__}resources/Button_zoom_in.png"};
def zoomOutImage:Image = Image {url: "{__DIR__}resources/Button_zoom_out.png"};
function zoomEditorIn(editor:SwingComponent, button:ImageView) {
    lowerNodes.translateX=0;
    lowerNodes.translateY=0;
    editor.width = width;
    editor.height = height - parentHeightOffset - toolBottomOffset;
    editor.layoutInfo = LayoutInfo{width: width,
        height: height - parentHeightOffset - toolBottomOffset}
    editor.translateX = 0;
    editor.translateY = 0;
    button.translateX = width-3-23;
    button.translateY = 3;
    button.visible = true;
    interviewTree.visible = false;
}
function zoomEditorOut(editor:SwingComponent, button:ImageView) {
    lowerNodes.translateX = width - rightWidth;
    lowerNodes.translateY = height - rightBottomHeight - parentHeightOffset - toolBottomOffset;
    editor.width = rightWidth-2*hPadding;
    editor.height = lowerNodesHeight-2*vPadding;
    editor.layoutInfo = LayoutInfo{width: rightWidth-2*hPadding,
        height: lowerNodesHeight-2*vPadding}
    editor.translateX = hPadding;
    editor.translateY = vPadding;
    button.translateX = rightWidth-hPadding-3-23;
    button.translateY = vPadding+3;
    button.visible = true;
    interviewTree.visible = true;
};
def schemaZoomInButton:ImageView = ImageView {
                image:zoomInImage,
                translateX:rightWidth-hPadding-3-23,
                translateY:vPadding+3,
                onMouseReleased:function(e:MouseEvent) {
                    zoomSchemaIn();
                    schemaZoomOutButton.disable = false;
                }
};
def schemaZoomOutButton:ImageView = ImageView {
                disable: true,
                image:zoomOutImage,
                translateX:rightWidth-hPadding-3-23,
                translateY:vPadding+3,
                onMouseReleased:function(e:MouseEvent) {
                    zoomSchemaOut();
                }
};
def zoomSchemaOut: function() =
    function() {
        schemaZoomOutButton.visible = false;
        zoomEditorOut(wrappedSchemaEditor,schemaZoomInButton);
        schemaMaximized = false;
        drawNormalWindow();
        interviewLogger.logBasicAction(InterviewLogger.ZOOM_SCHEMA_OUT);
    };
def zoomSchemaIn: function() =
    function() {
        schemaZoomInButton.visible = false;
        zoomEditorIn(wrappedSchemaEditor,schemaZoomOutButton);
        schemaMaximized = true;
        interviewLogger.logBasicAction(InterviewLogger.ZOOM_SCHEMA_IN);
    };
protected function getAuthors(anonUser:String, andStr:String) {
    return "[{anonUser}] {andStr} [{anonUser}]";
}
function showDesign() {
    interviewLogger.logBasicAction(interviewLogger.SHOW_DESIGN);
    guidePane.setTextFromFile(interviewStrings.guideDesignFileName);
    var nl = "\n";
    var stInterviewSchema = ##"Interview Schema";
    var stIntroduction = ##"Introduction";
    var stWeAre = ##"We are";
    var stYourName = ##"your name";
    var stAnd = ##"and";
    var name:String = getAuthors(stYourName, stAnd);
    var stDoingInterview = ##"and we are doing an interview for a school project. We are doing this interview because";
    var stQuestionAbout = ##"question is about";
    var stIsThat = ##"Is that";
    var stOtherNamely = ##"Other, namely...";
    var i: String = "{stInterviewSchema}{nl}{nl}";
    i = "{i}{stIntroduction}{nl}";
    i = "{i}{stWeAre} {name} {stDoingInterview} {question}{nl}";
    var tNo: Integer = 0;
    for (topic in topics) {
        tNo++;
        i = "{i}{nl}{tNo}. {stQuestionAbout} {topic.topic}.{nl}";
        var iNo: Integer = 0;
        for (indicator in topic.indicators) {
            iNo++;
            i = "{i}{tNo}.{iNo}. {indicator.formulation}? {stIsThat}:{nl}";
            for (answer in indicator.answers) {
                i = "{i}- {answer.toString()}{nl}";
            }
            if (indicator.answerIncludeNamely) {
                i = "{i}- {stOtherNamely}{nl}";
            }
        }
    }
    schemaEditor.setPlainText(i);
    var size = new Dimension(rightWidth-2*hPadding,lowerNodesHeight-2*vPadding);
    schemaEditor.setPreferredSize(size);
    schemaEditor.setSize(size);
    wrappedSchemaEditor = SwingComponent.wrap(schemaEditor);
    wrappedSchemaEditor.translateX = hPadding;
    wrappedSchemaEditor.translateY = vPadding;
    // delete added due to JavaFX garbage collection problems
    // this problem caused warning described in
    // http://stuartmarks.wordpress.com/2009/10/12/that-infernal-scene-graph-warning-message/
    if (wrappedSchemaEditor.parent != null) {
        delete wrappedSchemaEditor from (wrappedSchemaEditor.parent as Group).content;
    }
    if (schemaZoomInButton.parent != null) {
        delete schemaZoomInButton from (schemaZoomInButton.parent as Group).content;
    }
    if (schemaZoomOutButton.parent != null) {
        delete schemaZoomOutButton from (schemaZoomOutButton.parent as Group).content;
    }
    lowerNodes = Group {
        content: [
            wrappedSchemaEditor,
            schemaZoomOutButton,
            schemaZoomInButton
        ]
    };
    refreshStage();
}
function showConduct() {
    interviewLogger.logBasicAction(interviewLogger.SHOW_CONDUCT);
    guidePane.setText(##"Conduct the interview");
    lowerNodes = null;
    refreshStage();
}
function showConductPreparation() {
    interviewLogger.logBasicAction(interviewLogger.SHOW_CONDUCT_PREPARATION);
    guidePane.setTextFromFile(interviewStrings.guideConduct1FileName);
    lowerNodes = null;
    refreshStage();
}
def guidelinesEditor:RichTextEditor = new RichTextEditor(true, false, true);
var wrappedGuidelinesEditor:SwingComponent;
def guidelinesZoomInButton:ImageView = ImageView {
                image:zoomInImage,
                translateX:rightWidth-hPadding-3-23,
                translateY:vPadding+3,
                onMouseReleased:function(e:MouseEvent) {
                    zoomGuidelinesIn();
                    guidelinesZoomOutButton.disable = false;
                }
};
def guidelinesZoomOutButton:ImageView = ImageView {
                disable: true,
                image:zoomOutImage,
                translateX:rightWidth-hPadding-3-23,
                translateY:vPadding+3,
                onMouseReleased:function(e:MouseEvent) {
                    zoomGuidelinesOut();
                }
};
def zoomGuidelinesOut: function() =
    function() {
        guidelinesZoomOutButton.visible = false;
        zoomEditorOut(wrappedGuidelinesEditor,guidelinesZoomInButton);
        guidelinesMaximized = false;
        drawNormalWindow();
        interviewLogger.logBasicAction(InterviewLogger.ZOOM_GUIDELINES_OUT);
    };
def zoomGuidelinesIn: function() =
    function() {
        guidelinesZoomInButton.visible = false;
        zoomEditorIn(wrappedGuidelinesEditor,guidelinesZoomOutButton);
        guidelinesMaximized = true;
        interviewLogger.logBasicAction(InterviewLogger.ZOOM_GUIDELINES_IN);
    };
function showConductRecommendations() {
    interviewLogger.logBasicAction(interviewLogger.SHOW_CONDUCT_RECOMMENDATIONS);
    guidePane.setTextFromFile(interviewStrings.guideConduct2FileName);
    var url:URL = getClass().getResource("resources/{interviewStrings.guidelinesFileName}");
    guidelinesEditor.setTextFromUrl(url);
    var size = new Dimension(rightWidth-2*hPadding,lowerNodesHeight-2*vPadding);
    guidelinesEditor.setPreferredSize(size);
    guidelinesEditor.setSize(size);

    wrappedGuidelinesEditor = SwingComponent.wrap(guidelinesEditor);
    wrappedGuidelinesEditor.translateX = hPadding;
    wrappedGuidelinesEditor.translateY = vPadding;
    // delete added due to JavaFX garbage collection problems
    // this problem caused warning described in
    // http://stuartmarks.wordpress.com/2009/10/12/that-infernal-scene-graph-warning-message/
    if (wrappedGuidelinesEditor.parent != null) {
        delete wrappedGuidelinesEditor from (wrappedGuidelinesEditor.parent as Group).content;
    }
    if (guidelinesZoomInButton.parent != null) {
        delete guidelinesZoomInButton from (guidelinesZoomInButton.parent as Group).content;
    }
    if (guidelinesZoomOutButton.parent != null) {
        delete guidelinesZoomOutButton from (guidelinesZoomOutButton.parent as Group).content;
    }
    lowerNodes = Group {
        content: [
            wrappedGuidelinesEditor,
            guidelinesZoomOutButton,
            guidelinesZoomInButton
        ]
    };
    refreshStage();
}
function activateTreeNode(node: DefaultMutableTreeNode) {
    var nodes = interviewTree.model.getPathToRoot(node);
    var treePath: TreePath = new TreePath(nodes);
    interviewTree.tree.setSelectionPath(treePath);
    interviewTree.tree.scrollPathToVisible(treePath);
    interviewTree.selectedValue = node.getUserObject();
    interviewTreeClick(node.getUserObject());
}
public function activateTreeNodeByValue(value: Integer) {
    var e : Enumeration = (interviewTree.model.getRoot() as DefaultMutableTreeNode).preorderEnumeration();
    while (e.hasMoreElements()) {
        var node: DefaultMutableTreeNode = e.nextElement() as DefaultMutableTreeNode;
        if ((node.getUserObject() as InterviewTreeCell).value==value) {
            activateTreeNode(node);
            break;
        }
    }
}
protected var content: Node[] = [
    guidePane,
    Rectangle {
        x: bind width-rightWidth+3,
        y: bind height - rightBottomHeight - parentHeightOffset - toolBottomOffset + 3
        width: rightWidth-6, height: rightBottomHeight-6
//        layoutInfo: LayoutInfo{width: rightWidth-6, height: rightBottomHeight-6}
        fill: Color.TRANSPARENT,
        stroke: Color.GREY
    },
    HBox {
        spacing: 5
        hpos: HPos.RIGHT
        translateY: bind height - parentHeightOffset - 30 - toolBottomOffset
        translateX: bind width - rightWidth + 10
        width: rightWidth-20
//        layoutInfo: LayoutInfo{width: rightWidth-20}
        content: [
        Button {
            text: ##"Back"
            font: buttonFont
            action: function() {
                interviewLogger.logBasicAction(InterviewLogger.BACK_CLICKED);
                var e : Enumeration = (interviewTree.model.getRoot() as DefaultMutableTreeNode).preorderEnumeration();
                var prevNode: DefaultMutableTreeNode = null;
                // skip root
                if (e.hasMoreElements()) {
                    e.nextElement();
                }
                while (e.hasMoreElements()) {
                    var node: DefaultMutableTreeNode = e.nextElement() as DefaultMutableTreeNode;
                    if (interviewTree.selectedValue == node.getUserObject()) {
                        if (prevNode != null) {
                            activateTreeNode(prevNode);
                        }
                        break;
                    }
                    prevNode = node;
                }
            }
        }
        Button {
            text: ##"Home"
            font: buttonFont
            action: function() {
                interviewLogger.logBasicAction(InterviewLogger.HOME_CLICKED);
                activateTreeNodeByValue(INTERVIEW_TOOL_HOME);
            }
        }
        Button {
            text: ##"Next"
            font: buttonFont
            action: function() {
                interviewLogger.logBasicAction(InterviewLogger.NEXT_CLICKED);
                var e : Enumeration = (interviewTree.model.getRoot() as DefaultMutableTreeNode).preorderEnumeration();
                var found = false;
                while (e.hasMoreElements()) {
                    var node: DefaultMutableTreeNode = e.nextElement() as DefaultMutableTreeNode;
                    if (found) {
                        activateTreeNode(node);
                        break;
                    }
                    if (interviewTree.selectedValue == node.getUserObject()) {
                        found = true;
                    }
                }
            }
        }
        ]
    },
    Group {content: bind lowerNodes},
    Group {content: bind interviewTree}
    ];

   public override function getPrefHeight(width: Number) : Number{
      if (height<getMinHeight())
          return getMinHeight();
      return height;
   }
   public override function getPrefWidth(height: Number) : Number{
      if (width<getMinWidth())
          return getMinWidth();
      return width;
   }
   public override function getMinHeight() : Number {
      return 500;
   }
   public override function getMinWidth() : Number {
      return 600;
   }
   public override var width on replace {resizeContent()};
   public override var height on replace {resizeContent()};
   function drawNormalWindow() {
      interviewTree.width = width - rightWidth;
      interviewTree.height = height - parentHeightOffset - toolBottomOffset;
      interviewTree.layoutInfo = LayoutInfo{
            height: height - parentHeightOffset - toolBottomOffset
            width: width - rightWidth};
      guidePane.height = height - rightBottomHeight - parentHeightOffset - toolBottomOffset;
      guidePane.layoutInfo = LayoutInfo{
            height: height - rightBottomHeight - parentHeightOffset - toolBottomOffset
            width: rightWidth};
      guidePane.translateX = width - rightWidth;
      lowerNodes.translateX = width - rightWidth;
      lowerNodes.translateY = height - rightBottomHeight - parentHeightOffset - toolBottomOffset;
   }

   protected function resizeContent(){
      if (treeMaximized) {
          interviewTree.width = width;
          interviewTree.height = height - parentHeightOffset - toolBottomOffset;
          interviewTree.layoutInfo = LayoutInfo{
                height: height - parentHeightOffset - toolBottomOffset
                width: width};
      } else if (schemaMaximized) {
          zoomSchemaIn();
      } else if (guidelinesMaximized) {
          zoomGuidelinesIn();
      } else {
          drawNormalWindow();
      }
   }
   protected def treeZoomButton:Button =
    Button {
       text: ##"Zoom tree in/out"
       font: buttonFont
       action: function() {
            if (not schemaMaximized and not guidelinesMaximized) {
                if (treeMaximized) {
                    treeMaximized = false;
                    resizeContent();
                    interviewLogger.logBasicAction(InterviewLogger.ZOOM_TREE_OUT);
                } else {
                    treeMaximized = true;
                    resizeContent();
                    interviewLogger.logBasicAction(InterviewLogger.ZOOM_TREE_IN);
                }
            }
       }
    }
   public override function create(): Node {
      var node:Node = Group{content: bind content};
      var buttons:Node =
          HBox{
             translateX:10;
             spacing:10;
             content:[
                treeZoomButton
             ]
          }
      return Group {
         blocksMouse:true;
         content: [
            VBox{
               translateY:10;
               spacing:10;
               content:[
                  buttons,
                  node
               ]
            }
         ]
      };
   }

}
