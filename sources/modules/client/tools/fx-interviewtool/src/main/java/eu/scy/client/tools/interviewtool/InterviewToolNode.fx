/*
 * InterviewToolNode.fx
 *
 * Created on 8.12.2009, 22:15:57
 */

package eu.scy.client.tools.interviewtool;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.layout.ClipView;
import javafx.scene.control.ScrollBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.LayoutInfo;
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
import javafx.scene.control.Label;
import javax.jnlp.*;
import eu.scy.client.desktop.scydesktop.tools.ScyToolFX;
import eu.scy.client.desktop.scydesktop.tools.EloSaverCallBack;
import org.apache.log4j.Logger;
import org.jdom.Element;
import roolo.api.IRepository;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import roolo.api.IRepository;
import roolo.api.IExtensionManager;
import eu.scy.actionlogging.api.IActionLogger;
import eu.scy.awareness.IAwarenessService;
import eu.scy.client.common.datasync.IDataSyncService;
import eu.scy.server.pedagogicalplan.PedagogicalPlanService;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;

import java.net.URI;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import eu.scy.client.desktop.scydesktop.utils.jdom.JDomStringConversion;
import roolo.api.search.IQuery;
import roolo.api.search.ISearchResult;
import roolo.cms.repository.mock.BasicMetadataQuery;
import roolo.cms.repository.search.BasicSearchOperations;

import javafx.scene.layout.Resizable;
import java.awt.Dimension;
import java.net.URL;
import eu.scy.client.common.richtexteditor.RichTextEditor;
import javafx.ext.swing.SwingComponent;
import javafx.geometry.HPos;

/**
 * @author kaido
 */

public class InterviewToolNode extends CustomNode, Resizable {
def normalFont = Font {
    name: "Arial"
    size: 14
}
def treeFont = normalFont;
def labelFont = normalFont;
protected def buttonFont = normalFont;
protected def INTERVIEW_TOOL_HOME = 1;
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
protected var parentHeightOffset = 0;
protected def stageHeight = 500;
protected def stageWidth = 600;
def scrollBarThickness = 12;
protected def logger = Logger.getLogger("eu.scy.client.tools.interviewtool.InterviewToolNode");
protected var interviewLogger: InterviewLogger;
var log = true;
function getScrollViewWidth(node: Node, height: Number, width: Number) : Number {
    if (node.layoutBounds.maxY>height)
        return width-scrollBarThickness;
    return width;
}
function getScrollViewHeight(node: Node, height: Number, width: Number) : Number {
    if (node.layoutBounds.maxX>width)
        return height-scrollBarThickness;
    return height;
}
function getHScrollMaxX(node: Node, height: Number, width: Number) : Number {
    if (node.layoutBounds.maxX-getScrollViewWidth(node, height, width)<0 )
        return 0;
    return node.layoutBounds.maxX-getScrollViewWidth(node, height, width);
}
function getVScrollMaxY(node: Node, height: Number, width: Number) : Number {
    if (node.layoutBounds.maxY-getScrollViewHeight(node, height, width)<0 )
        return 0;
    return node.layoutBounds.maxY-getScrollViewHeight(node, height, width);
}
function getVScrollHeight(node: Node, height: Number, width: Number) : Number {
    if (getHScrollMaxX(node, height, width)==0)
        return height;
    return height-scrollBarThickness;
}
var guidePane : InterviewGuides = InterviewGuides{width:rightWidth, height:height-rightBottomHeight};
var upperNodes : Node;
var lowerNodes : Node;
var lowerScrollClipView : ClipView = ClipView {
    clipX: bind lowerHScroll.value
    clipY: bind lowerVScroll.value
    node: bind lowerNodes
    pannable: false
    layoutInfo: LayoutInfo {
        width: bind getScrollViewWidth(lowerNodes, rightBottomHeight, rightWidth)
        height: bind getScrollViewHeight(lowerNodes, rightBottomHeight, rightWidth)
    }
}
var lowerVScroll = ScrollBar {
    min: 0
    max: bind getVScrollMaxY(lowerNodes, rightBottomHeight, rightWidth)
    value: 0
    vertical: true
    visible: bind getVScrollMaxY(lowerNodes, rightBottomHeight, rightWidth)>0
    layoutInfo: LayoutInfo {
        height: bind getVScrollHeight(lowerNodes, rightBottomHeight, rightWidth)
    }
}
var lowerHBox = HBox {content: [lowerScrollClipView, lowerVScroll]}
var lowerHScroll = ScrollBar {
    min: 0
    max: bind getHScrollMaxX(lowerNodes, rightBottomHeight, rightWidth)
    value: 0
    vertical: false
    visible: bind getHScrollMaxX(lowerNodes, rightBottomHeight, rightWidth)>0
    layoutInfo: LayoutInfo {
        width: rightWidth
    }
}
var lowerVBox = VBox {
    translateX: width - rightWidth
    translateY: height - rightBottomHeight - parentHeightOffset
    content: [lowerHBox, lowerHScroll]
}
protected var topics: InterviewTopic[];
protected var interviewTree: InterviewTree = makeTree();
function makeTree() : InterviewTree {
    return InterviewTree{
        translateX: 0
        translateY: 0
        width: width - rightWidth
        height: height
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
var interviewStrings:InterviewStrings = InterviewStrings{};
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
protected var question: String;
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
                width: rightWidth-2*hPadding
                height: 150
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
function showTopic(cell: InterviewTreeCell) {
    if (log)
        interviewLogger.logShowIndicators(cell.topic.topic);
    topicNo = cell.topicNo;
    guidePane.setTextFromFile(interviewStrings.guideIndicatorsFileName,
        ["__TOPIC_NO__","__TOPIC__"], [topicNo.toString(),cell.topic.topic]);
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
var namely: Boolean;
function showIndicator(cell: InterviewTreeCell) {
    if (log)
        interviewLogger.logShowAnswers(cell.topic.topic,cell.indicator.indicator);
    topicNo = cell.topicNo;
    indicatorNo = cell.indicatorNo;
    guidePane.setTextFromFile(interviewStrings.guideAnswersFileName,
        ["__INDICATOR_NO__","__TOPIC__","__INDICATOR__"],
        [indicatorNo.toString(),cell.topic.topic,cell.indicator.indicator]);
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
                width: rightWidth - 2*hPadding
                height: 150
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
def schemaEditor:RichTextEditor = new RichTextEditor();
function showDesign() {
    interviewLogger.logBasicAction(interviewLogger.SHOW_DESIGN);
    guidePane.setTextFromFile(interviewStrings.guideDesignFileName);
    var nl = "\n";
    var stInterviewSchema = ##"Interview Schema";
    var stIntroduction = ##"Introduction";
    var stWeAre = ##"We are";
    var stAnd = ##"and";
    var stDoingInterview = ##"and we are doing an interview for a school project. We are doing this interview because";
    var stQuestionAbout = ##"question is about";
    var stIsThat = ##"Is that";
    var stOtherNamely = ##"Other, namely...";
    var i: String = "{stInterviewSchema}{nl}{nl}";
    i = "{i}{stIntroduction}{nl}";
    i = "{i}{stWeAre} [your name] {stAnd} [your name] {stDoingInterview} {question}{nl}";
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
    var wrappedSchemaEditor:SwingComponent = SwingComponent.wrap(schemaEditor);
    wrappedSchemaEditor.translateX = hPadding;
    wrappedSchemaEditor.translateY = vPadding;
    lowerNodes = wrappedSchemaEditor;
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
def guidelinesEditor:RichTextEditor = new RichTextEditor(true);
function showConductRecommendations() {
    interviewLogger.logBasicAction(interviewLogger.SHOW_CONDUCT_RECOMMENDATIONS);
    guidePane.setTextFromFile(interviewStrings.guideConduct2FileName);
    var url:URL = getClass().getResource("resources/{interviewStrings.guidelinesFileName}");
    guidelinesEditor.setTextFromUrl(url);
    var size = new Dimension(rightWidth-2*hPadding,lowerNodesHeight-2*vPadding);
    guidelinesEditor.setPreferredSize(size);
    guidelinesEditor.setSize(size);
    var wrappedGuidelinesEditor:SwingComponent = SwingComponent.wrap(guidelinesEditor);
    wrappedGuidelinesEditor.translateX = hPadding;
    wrappedGuidelinesEditor.translateY = vPadding;
    lowerNodes = wrappedGuidelinesEditor;
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
protected function activateTreeNodeByValue(value: Integer) {
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
        fill: Color.TRANSPARENT,
        stroke: Color.GREY
    },
    Group {content: bind lowerVBox},
    HBox {
        spacing: 5
        hpos: HPos.RIGHT
        translateY: bind height - parentHeightOffset - 30 - toolBottomOffset
        translateX: bind width - rightWidth + 10
        width: rightWidth-20
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
    Group {content: bind interviewTree}
    ];

   public override function getPrefHeight(width: Number) : Number{
      return getMinHeight();
   }
   public override function getPrefWidth(height: Number) : Number{
      return getMinWidth();
   }
   public override function getMinHeight() : Number {
      return stageHeight;
   }
   public override function getMinWidth() : Number {
      return stageWidth;
   }
   public override var width on replace {resizeContent()};
   public override var height on replace {resizeContent()};
   function resizeContent(){
      interviewTree.width = width - rightWidth;
      interviewTree.height = height - parentHeightOffset - toolBottomOffset;
      guidePane.height = height - rightBottomHeight - parentHeightOffset - toolBottomOffset;
      guidePane.translateX = width - rightWidth;
      lowerVBox.translateX = width - rightWidth;
      lowerVBox.translateY = height - rightBottomHeight - parentHeightOffset - toolBottomOffset;
   }
   public override function create(): Node {
      return Group{content: bind content}
   }

}
