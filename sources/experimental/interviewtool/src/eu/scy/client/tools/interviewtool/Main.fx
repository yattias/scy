package eu.scy.client.tools.interviewtool;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.ClipView;
import javafx.scene.control.ScrollBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.LayoutInfo;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.control.Button;
import javafx.stage.AppletStageExtension;
import javax.swing.tree.TreePath;
import javax.swing.tree.DefaultMutableTreeNode;
import java.util.Enumeration;
import eu.scy.client.tools.interviewtool.interviewtable.*;
import javafx.scene.control.Label;
import javax.jnlp.*;

def normalFont = Font {
    name: "Arial"
    size: 12
}
def treeFont = normalFont;
def labelFont = Font {
    name: "Arial"
    size: 16
}
def variableFont = Font {
    name: "Arial Italic"
    size: 12
}
def titleFont = Font {
    name: "Arial"
    size: 20
}
def INTERVIEW_TOOL_HOME = 1;
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
def INTERVIEW_TOOL_ANALYZE = 13;
def INTERVIEW_TOOL_ANALYZE_NOTINCLUDED = 14;
def stageHeight = 500;
def stageWidth = 600;
def treeWidth = 200;
def upperFrameHeightBase = 200;
def buttonBarHeight = 50;
def lowerFrameHeightBase = stageHeight - upperFrameHeightBase - buttonBarHeight;
def scrollableAreaOffset = 0;
def scrollBarThickness = 12;
def leftFrameWidth = stageWidth - treeWidth - 2*scrollableAreaOffset;
def upperFrameHeight = upperFrameHeightBase - 2*scrollableAreaOffset;
def lowerFrameHeight = lowerFrameHeightBase - 2*scrollableAreaOffset;
def hPadding = 10;
def leftFrameWrappingWidth = leftFrameWidth - 2*hPadding;
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
var upperNodes : Node;
var lowerNodes : Node;
var upperScrollClipView : ClipView = ClipView {
    clipX: bind upperHScroll.value
    clipY: bind upperVScroll.value
    node: bind upperNodes
    pannable: false
    layoutInfo: LayoutInfo {
        width: bind getScrollViewWidth(upperNodes, upperFrameHeight, leftFrameWidth)
        height: bind getScrollViewHeight(upperNodes, upperFrameHeight, leftFrameWidth)
    }
}
var upperVScroll = ScrollBar {
    min: 0
    max: bind getVScrollMaxY(upperNodes, upperFrameHeight, leftFrameWidth)
    value: 0
    vertical: true
    visible: bind getVScrollMaxY(upperNodes, upperFrameHeight, leftFrameWidth)>0
    layoutInfo: LayoutInfo {
        height: bind getVScrollHeight(upperNodes, upperFrameHeight, leftFrameWidth)
    }
}
var upperHBox = HBox {content: [upperScrollClipView, upperVScroll]}
var upperHScroll = ScrollBar {
    min: 0
    max: bind getHScrollMaxX(upperNodes, upperFrameHeight, leftFrameWidth)
    value: 0
    vertical: false
    visible: bind getHScrollMaxX(upperNodes, upperFrameHeight, leftFrameWidth)>0
    layoutInfo: LayoutInfo {
        width: leftFrameWidth
    }
}
var upperVBox = VBox {
    translateX: treeWidth+scrollableAreaOffset
    translateY: scrollableAreaOffset
    content: [upperHBox, upperHScroll]
}
var lowerScrollClipView : ClipView = ClipView {
    clipX: bind lowerHScroll.value
    clipY: bind lowerVScroll.value
    node: bind lowerNodes
    pannable: false
    layoutInfo: LayoutInfo {
        width: bind getScrollViewWidth(lowerNodes, lowerFrameHeight, leftFrameWidth)
        height: bind getScrollViewHeight(lowerNodes, lowerFrameHeight, leftFrameWidth)
    }
}
var lowerVScroll = ScrollBar {
    min: 0
    max: bind getVScrollMaxY(lowerNodes, lowerFrameHeight, leftFrameWidth)
    value: 0
    vertical: true
    visible: bind getVScrollMaxY(lowerNodes, lowerFrameHeight, leftFrameWidth)>0
    layoutInfo: LayoutInfo {
        height: bind getVScrollHeight(lowerNodes, lowerFrameHeight, leftFrameWidth)
    }
}
var lowerHBox = HBox {content: [lowerScrollClipView, lowerVScroll]}
var lowerHScroll = ScrollBar {
    min: 0
    max: bind getHScrollMaxX(lowerNodes, lowerFrameHeight, leftFrameWidth)
    value: 0
    vertical: false
    visible: bind getHScrollMaxX(lowerNodes, lowerFrameHeight, leftFrameWidth)>0
    layoutInfo: LayoutInfo {
        width: leftFrameWidth
    }
}
var lowerVBox = VBox {
    translateX: treeWidth+scrollableAreaOffset
    translateY: upperFrameHeightBase+scrollableAreaOffset
    content: [lowerHBox, lowerHScroll]
}
var topics: InterviewTopic[];
var interviewTree: InterviewTree = makeTree();
function makeTree() : InterviewTree {
    return InterviewTree{
        translateX: 0
        translateY: 0
        width: treeWidth
        height: stageHeight - buttonBarHeight
        font: treeFont
        root: InterviewTreeCell{
            text: "Root"
            cells: [
                InterviewTreeCell{
                    text: "Home"
                    value: INTERVIEW_TOOL_HOME
                }
                InterviewTreeCell{
                    text: "Prepare interview"
                    value: INTERVIEW_TOOL_PREPARE
                    cells:[
                        InterviewTreeCell{
                            text: "Research question"
                            value: INTERVIEW_TOOL_QUESTION
                        }
                        InterviewTreeCell{
                            text: "Topics"
                            value: INTERVIEW_TOOL_TOPICS
                            cells: for (x in [0..sizeof topics-1]) InterviewTreeCell{
                                text: topics[x].topic
                                value: INTERVIEW_TOOL_TOPIC
                                topic: topics[x]
                                topicNo: x+1
                                cells: [
                                    for (y in [0..sizeof topics[x].indicators-1]) [
                                    InterviewTreeCell{
                                        text: "Answers for \"{topics[x].indicators[y].indicator}\""
                                        value: INTERVIEW_TOOL_INDICATOR
                                        topic: topics[x]
                                        topicNo: x+1
                                        indicator: topics[x].indicators[y]
                                        indicatorNo: y+1
                                    }
                                    InterviewTreeCell{
                                        text: "Formulate \"{topics[x].indicators[y].indicator}\""
                                        value: INTERVIEW_TOOL_INDICATOR_FORMULATE
                                        topic: topics[x]
                                        topicNo: x+1
                                        indicator: topics[x].indicators[y]
                                        indicatorNo: y+1
                                    }
                                    InterviewTreeCell{
                                        text: "Status for \"{topics[x].indicators[y].indicator}\""
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
                            text: "Design"
                            value: INTERVIEW_TOOL_DESIGN
                        }
                    ]
                }
                InterviewTreeCell{
                    text: "Conduct interview"
                    value: INTERVIEW_TOOL_CONDUCT
                    cells: [
                        InterviewTreeCell{
                            text: "Preparation is important"
                            value: INTERVIEW_TOOL_CONDUCT_PREPARATION
                        }
                        InterviewTreeCell{
                            text: "Recommendations"
                            value: INTERVIEW_TOOL_CONDUCT_RECOMMENDATIONS
                        }
                    ]
                }
                InterviewTreeCell{
                    text: "Analyze data"
                    value: INTERVIEW_TOOL_ANALYZE
                    cells: [
                        InterviewTreeCell{
                            text: "Not included"
                            value: INTERVIEW_TOOL_ANALYZE_NOTINCLUDED
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
    } else if ((obj as InterviewTreeCell).value==INTERVIEW_TOOL_ANALYZE) {
        showAnalyze();
    } else if ((obj as InterviewTreeCell).value==INTERVIEW_TOOL_ANALYZE_NOTINCLUDED) {
        showAnalyzeNotIncluded();
    }
}
// javafx bug, no refresh :(
function refreshStage() {
    var txt: Text = Text{};
    insert txt into content;
    delete txt from content;
/*
    stage.visible = false;
    stage.visible = true;
*/
/*
    if (stage.height > stageHeight+40)
        stage.height = stage.height-1
    else
        stage.height = stage.height+1;
*/
}
function showHome() : Void {
    upperNodes =
        Text {
            font : titleFont
            wrappingWidth: leftFrameWrappingWidth
            x: hPadding
            y: 30
            content: "This tool helps you to do a good interview.\nThere are three stages. Select one of these below."
        };
    lowerNodes = Group {content: [
        Hyperlink {
            translateX: hPadding
            translateY: 20
            text: "Prepare interview"
            action: function() {
                activateTreeNodeByValue(INTERVIEW_TOOL_PREPARE);
            }
        }
        Hyperlink {
            translateX: hPadding
            translateY: 50
            text: "Conduct interview"
            action: function() {
                activateTreeNodeByValue(INTERVIEW_TOOL_CONDUCT);
            }
        }
        Hyperlink {
            translateX: hPadding
            translateY: 80
            text: "Analyze data"
            action: function() {
                activateTreeNodeByValue(INTERVIEW_TOOL_ANALYZE);
            }
        }
    ]};
    refreshStage();
}
function showPrepare() : Void {
    upperNodes =
        Text {
            font : titleFont
            x: 100
            y: 100
            content: "Prepare the interview"
        };
    lowerNodes = null;
    refreshStage();
}
var question: String;
function showQuestion() {
    upperNodes = 
        Text {
            font : labelFont
            wrappingWidth: leftFrameWrappingWidth
            x: hPadding
            y: 30
            content: "Write down below your research question. This should be a short phrase that states what you want to know about a certain topic. For instance, \"We want to know the average amount of water used by households in Amsterdam.\"\nHere are some criteria a good research question should meet:\n* first criteria\n* second criteria\n* third criteria\n* ..."
        };
    lowerNodes = Group {content: [
        Text {
            font : labelFont
            wrappingWidth: leftFrameWrappingWidth
            x: hPadding
            y: 30
            content: "Research question:"
        }
        InterviewTextArea {
                width: leftFrameWrappingWidth
                height: 100
                translateX: hPadding
                translateY: 50
                font: normalFont
                text: bind question with inverse
            }
    ]};
    refreshStage();
}
var refreshTree: function() =
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
                activateTreeNode(node);
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
    upperNodes = VBox {
        translateX: hPadding
        translateY: 20
        spacing: 5
        content: [
        Text {
            font: normalFont
            wrappingWidth: leftFrameWrappingWidth
            content: "Your research question is:"
        }
        Text {
            font: variableFont
            wrappingWidth: leftFrameWrappingWidth
            content: "\"{question}\""
        }
        Text {
            font: normalFont
            wrappingWidth: leftFrameWrappingWidth
            content: "Now consider what information you need to be able to answer this question. Make a list of topics below."
        }
        ]
    };
    objects = topics;
    lowerNodes = InterviewTableEditor {
        translateX: hPadding
        translateY: 10
        oldObjects: bind objects with inverse;
        classType: "InterviewTopic"
        headerText: "Topics we need information about"
        nextID: bind nextID with inverse
        refreshAction: refreshTopicsAndTree
        width: leftFrameWrappingWidth
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
    topicNo = cell.topicNo;
    upperNodes = VBox {
        translateX: hPadding
        translateY: 20
        spacing: 5
        content: [
        Text {
            font: normalFont
            wrappingWidth: leftFrameWrappingWidth
            content: "Your {cell.topicNo}. variable is:"
        }
        Text {
            font: variableFont
            wrappingWidth: leftFrameWrappingWidth
            content: "\"{cell.topic.topic}\""
        }
        Text {
            font: normalFont
            wrappingWidth: leftFrameWrappingWidth
            content: "Now consider how you can measure this variable. Write down possble indicators below."
        }
        ]
    };
    objects = cell.topic.indicators;
    lowerNodes = InterviewTableEditor {
        translateX: hPadding
        translateY: 10
        oldObjects: bind objects with inverse;
        classType: "InterviewIndicator"
        headerText: "Indicators of \"{cell.topic.topic}\""
        nextID: bind nextID with inverse
        refreshAction: refreshTopicAndTree
        width: leftFrameWrappingWidth
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
    topicNo = cell.topicNo;
    indicatorNo = cell.indicatorNo;
    upperNodes = VBox {
        translateX: hPadding
        translateY: 20
        spacing: 5
        content: [
        Text {
            font: normalFont
            wrappingWidth: leftFrameWrappingWidth
            content: "Your {cell.indicatorNo}. indicator of \"{cell.topic.topic}\" is:"
        }
        Text {
            font: variableFont
            wrappingWidth: leftFrameWrappingWidth
            content: "\"{cell.indicator.indicator}\""
        }
        Text {
            font: normalFont
            wrappingWidth: leftFrameWrappingWidth
            content: "Make a list of the possible answers you might get to this question. Write them down below."
        }
        ]
    };
    objects = cell.indicator.answers;
    namely = cell.indicator.answerIncludeNamely;
    lowerNodes = InterviewTableEditor {
        translateX: hPadding
        translateY: 10
        oldObjects: bind objects with inverse;
        classType: "InterviewAnswer"
        headerText: "Answer options for \"{cell.indicator.indicator}\""
        nextID: bind nextID with inverse
        refreshAction: refreshIndicator
        width: leftFrameWrappingWidth
        height: 150
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
function showIndicatorFormulate(cell: InterviewTreeCell) {
    topicNo = cell.topicNo;
    indicatorNo = cell.indicatorNo;
    formulation = cell.indicator.formulation;
    upperNodes =
        Text {
            font : labelFont
            wrappingWidth: leftFrameWrappingWidth
            x: hPadding
            y: 30
            content: "Now think of a question you could ask someone to reveal the \"{cell.indicator.indicator}\". This is the question you will actually be using in the interview.\nGood interview questions satisfiy the following criteria:\n- ..."
        };
    lowerNodes = Group {content: [
        Label {
            font : labelFont
            width: leftFrameWrappingWidth
            translateX: hPadding
            translateY: 30
            text: "Interview question for \"{cell.indicator.indicator}\""
        }
        InterviewTextArea {
                width: leftFrameWrappingWidth
                height: 100
                translateX: hPadding
                translateY: 50
                font: normalFont
                text: bind formulation with inverse
            }
    ]};
    refreshStage();
}
function showIndicatorStatus(cell: InterviewTreeCell) {
    upperNodes =
        Text {
            font : labelFont
            wrappingWidth: leftFrameWrappingWidth
            x: hPadding
            y: 30
            content: "You have now fully specified the interview questions for \"{cell.indicator.indicator}\"."
        };
    lowerNodes = null;
    refreshStage();
}
function showDesign() {
    upperNodes = VBox {
        translateX: hPadding
        translateY: 20
        spacing: 15
        content: [
        Text {
            font: labelFont
            wrappingWidth: leftFrameWrappingWidth
            content: "Based on your input an interview schema has been created. But before you can use it there are two things you need to do:"
        }
        Text {
            font: labelFont
            wrappingWidth: leftFrameWrappingWidth
            content: "- Sequence the questions\n- Complete the introduction"
        }
        Text {
            font: labelFont
            wrappingWidth: leftFrameWrappingWidth
            content: "Follow the link below to download the draft interview schema and perform the above actions."
        }
        ]
    };
    lowerNodes = VBox {
        translateX: hPadding
        translateY: 20
        spacing: 15
        content: [
        Text {
            font: labelFont
            wrappingWidth: leftFrameWrappingWidth
            content: "Download interview schema"
        }
        Hyperlink {
            text: "Interview schema"
            action: function() {
                var nl = "\n";
                var i: String = "Interview Schema{nl}{nl}";
                i = "{i}Introduction{nl}";
                i = "{i}We are [your name] and [your name] and we are doing an interview for a school project. We are doing this interview because {question}{nl}";
                var tNo: Integer = 0;
                for (topic in topics) {
                    tNo++;
                    i = "{i}{nl}{tNo}. question is about {topic.topic}.{nl}";
                    var iNo: Integer = 0;
                    for (indicator in topic.indicators) {
                        iNo++;
                        i = "{i}{tNo}.{iNo}. {indicator.formulation}? Is that:{nl}";
                        for (answer in indicator.answers) {
                            i = "{i}- {answer.toString()}{nl}";
                        }
                        if (indicator.answerIncludeNamely) {
                            i = "{i}- Other, namely...{nl}";
                        }
                    }
                }
                interviewSchema = i;
                interviewSchemaVisible = true;
//                i = "w=window.open();d=w.document;d.write('<pre>{i}</pre>');d.close();";
//                extension.eval(i);
            }
        }
        ]
    };
    refreshStage();
}
function showConduct() {
    upperNodes =
        Text {
            font : titleFont
            x: 100
            y: 100
            content: "Conduct the interview"
        };
    lowerNodes = null;
    refreshStage();
}
function showConductPreparation() {
    upperNodes = VBox {
        translateX: hPadding
        translateY: 20
        spacing: 15
        content: [
        Text {
            font: labelFont
            wrappingWidth: leftFrameWrappingWidth
            content: "Preparation is key to a successful interview. Make sure you bring all necessary materials to the site, and familiarize yourself with the interview questions, so you can ask them fluently without having to look at the interview schema all the time."
        }
        Text {
            font: labelFont
            wrappingWidth: leftFrameWrappingWidth
            content: "On the next page you’ll find some recommendations for conducting the interview."
        }
        ]
    };
    lowerNodes = null;
    refreshStage();
}
function showConductRecommendations() {
    upperNodes = VBox {
        translateX: hPadding
        translateY: 20
        spacing: 15
        content: [
        Text {
            font: labelFont
            wrappingWidth: leftFrameWrappingWidth
            content: "It is recommended to conduct the interview in pairs with one student asking the questions and the other one writing down the answers. You decide on these roles before the interview starts, and maintain your role to the end. Alternating roles during the interview is not recommended!"
        }
        Text {
            font: labelFont
            wrappingWidth: leftFrameWrappingWidth
            content: "Keep in mind the following guidelines when talking to a respondent:"
        }
        ]
    };
    lowerNodes = VBox {
        translateX: hPadding
        translateY: 20
        spacing: 15
        content: [
        Text {
            font: labelFont
            wrappingWidth: leftFrameWrappingWidth
            content: "Download and print these recommendations"
        }
        Hyperlink {
            text: "Interview guidelines.doc"
            action: function() {
                var bs : BasicService = ServiceManager.lookup("javax.jnlp.BasicService") as BasicService;
                var url : java.net.URL = new java.net.URL("http://bio.edu.ee/scy/tools/interviewtool/Interview_guidelines.doc");
                bs.showDocument(url);
//                extension.showDocument("{FX.getProperty("javafx.application.codebase")}Interview_guidelines.doc", "_BLANK");
            }
        }
        Hyperlink {
            text: "Interview guidelines.pdf"
            action: function() {
                var bs : BasicService = ServiceManager.lookup("javax.jnlp.BasicService") as BasicService;
                var url : java.net.URL = new java.net.URL("http://bio.edu.ee/scy/tools/interviewtool/Interview_guidelines.pdf");
                bs.showDocument(url);
//                extension.showDocument("{FX.getProperty("javafx.application.codebase")}Interview_guidelines.pdf", "_BLANK");
            }
        }
        ]
    };
    refreshStage();
}
function showAnalyze() {
    upperNodes =
        Text {
            font : titleFont
            x: 100
            y: 100
            content: "Analyze data"
        };
    lowerNodes = null;
    refreshStage();
}
function showAnalyzeNotIncluded() {
    upperNodes =
        Text {
            font : labelFont
            wrappingWidth: leftFrameWrappingWidth
            x: hPadding
            y: 30
            content: "Analyze data is not included in the mockup"
        };
    lowerNodes = null;
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
function activateTreeNodeByValue(value: Integer) {
    var e : Enumeration = (interviewTree.model.getRoot() as DefaultMutableTreeNode).preorderEnumeration();
    while (e.hasMoreElements()) {
        var node: DefaultMutableTreeNode = e.nextElement() as DefaultMutableTreeNode;
        if ((node.getUserObject() as InterviewTreeCell).value==value) {
            activateTreeNode(node);
            break;
        }
    }
}
var content: Node[] = [
    Group {content: bind upperVBox},
    Group {content: bind lowerVBox},
    Line {
        startX: 0, startY: stageHeight - buttonBarHeight
        endX: stageWidth, endY: stageHeight - buttonBarHeight
        fill: Color.TRANSPARENT,
        stroke: Color.BLACK
    }
    Line {
        startX: treeWidth, startY: 0
        endX: treeWidth, endY: stageHeight - buttonBarHeight
        fill: Color.TRANSPARENT,
        stroke: Color.BLACK
    }
    Line {
        startX: treeWidth, startY: upperFrameHeightBase
        endX: stageWidth, endY: upperFrameHeightBase
        strokeWidth: 1
        stroke: Color.BLACK
    }
    Group {content: bind interviewTree},
    Rectangle {
        x: 0, y: 0
        width: stageWidth, height: stageHeight
        fill: Color.TRANSPARENT,
        stroke: Color.BLACK
    }
    HBox {
        spacing: 5
        translateY: stageHeight-buttonBarHeight+10
        translateX: 100
        visible: bind not interviewSchemaVisible
        content: [
        Button {
            text: "Zoom tree in/out"
            action: function() {
                if (interviewTree.width==stageWidth)
                    interviewTree.width=treeWidth
                else
                    interviewTree.width=stageWidth;
            }
        }
        Button {
            text: "Back"
            action: function() {
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
            text: "Home"
            action: function() {
                activateTreeNodeByValue(INTERVIEW_TOOL_HOME);
            }
        }
        Button {
            text: "Next"
            action: function() {
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
    }
    InterviewTextArea {
        width: stageWidth
        height: stageHeight-buttonBarHeight
        translateX: 0
        translateY: 0
        font: normalFont
        visible: bind interviewSchemaVisible
        text: bind interviewSchema with inverse
    }
    HBox {
        spacing: 5
        translateY: stageHeight-buttonBarHeight+10
        translateX: 100
        visible: bind interviewSchemaVisible
        content: [
            Button {
                text: "Copy all to clipboard"
                action: function() {
                    var cs : ClipboardService = null;
                    try {
                        cs = ServiceManager.lookup("javax.jnlp.ClipboardService") as ClipboardService;
                    } catch (e : UnavailableServiceException) {
                        cs = null;
                    }
                     if (cs != null) {
                        var ss: java.awt.datatransfer.StringSelection =
                            new java.awt.datatransfer.StringSelection(interviewSchema);
                        cs.setContents(ss);
                    }
                }
            }
            Button {
                text: "Close interview schema"
                action: function() {
                    interviewSchemaVisible = false;
                }
            }
        ]
    }
    ];
var interviewSchemaVisible = false;
var interviewSchema = "";
var extension = AppletStageExtension {}
var stage = Stage {
    width: stageWidth+20
    height: stageHeight+40
    title: "Interview tool"
    scene: Scene {content: bind content}
    extensions: [extension]
}
activateTreeNodeByValue(INTERVIEW_TOOL_HOME);
