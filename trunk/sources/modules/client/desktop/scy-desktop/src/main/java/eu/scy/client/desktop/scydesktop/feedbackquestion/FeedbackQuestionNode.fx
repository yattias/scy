package eu.scy.client.desktop.scydesktop.feedbackquestion;

import java.net.URI;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;
import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.layout.LayoutInfo;
import javafx.scene.layout.Resizable;
import javafx.scene.control.Button;
import javafx.scene.control.TextBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import eu.scy.client.desktop.scydesktop.tools.ScyToolFX;
import eu.scy.client.desktop.desktoputils.jdom.JDomStringConversion;
import eu.scy.common.scyelo.QueryFactory;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import org.apache.log4j.Logger;
import org.jdom.Element;
import roolo.elo.api.IMetadataTypeManager;
import eu.scy.client.desktop.scydesktop.ScyDesktop;
import javafx.scene.layout.VBox;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import eu.scy.client.desktop.scydesktop.tools.DrawerUIIndicator;
import javafx.scene.text.Text;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.layout.Container;
import javafx.scene.layout.Priority;
import roolo.elo.api.IMetadata;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import java.util.Collections;
import eu.scy.actionlogging.api.IAction;
import eu.scy.actionlogging.Action;
import eu.scy.actionlogging.api.ContextConstants;

public class FeedbackQuestionNode extends CustomNode, ScyToolFX, Resizable {

    def logger = Logger.getLogger("eu.scy.client.desktop.scydesktop.feedbackquestion.FeedbackQuestionNode");
    public var toolBrokerAPI: ToolBrokerAPI on replace {
                identifierKey = toolBrokerAPI.getMetaDataTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.IDENTIFIER);
            };
    public var scyDesktop: ScyDesktop;
    public var scyWindow: ScyWindow;
    def technicalType: String = "scy/feedback";
    def title: String = "Feedback ELO";
    def feedbackTagName: String = "feedbackelo";
    def commentTagName: String = "comment";
    def createdbyTagName: String = "createdby";
    def createdbypictureTagName: String = "createdbypicture";
    def calendardateTagName: String = "calendardate";
    def calendartimeTagName: String = "calendartime";
    def jdomStringConversion = new JDomStringConversion();
    var eloUri: URI;
    def spacing = 5.0;
    def titleLabel = Text {
                content: "Feedback"
                font: Font.font("Verdana", FontWeight.BOLD, 12)
                fill: scyWindow.windowColorScheme.mainColor;
            };
    var textBox = TextBox {
                multiline: true
                selectOnFocus: true
                managed: true
                layoutInfo: LayoutInfo {
                    hfill: true
                    vfill: true
                    hgrow: Priority.ALWAYS
                    vgrow: Priority.ALWAYS
                }
            };
    var submitButton: Button = Button {
                disable: bind eloUri == null or textBox.rawText.trim() == ""
            }
    var mainBox: VBox;
    var identifierKey: IMetadataKey;

    public override function create(): Node {
        setFeedbackNotAsked();
        mainBox = VBox {
                    spacing: spacing
                    nodeHPos: HPos.CENTER
                    padding: Insets {
                        top: spacing
                        right: spacing
                        bottom: spacing
                        left: spacing
                    }
                    managed: false
                    content: [
                        titleLabel,
                        textBox,
                        submitButton
                    ]
                };
    }

    public override function loadedEloChanged(eloUri: URI): Void {
        eloUriChanged(eloUri)
    }

    function eloUriChanged(eloUri: URI) {
        this.eloUri = eloUri;
        def feedbackElo = findFeedbackElo(eloUri);
        if (feedbackElo != null) {
            if (feedbackElo.getContent().getXmlString()!=null){
               setFeedbackAsked(getQuestionDisplay(feedbackElo));
            } else {
               logger.warn("unexpected null for feedbackElo.getContent().getXmlString(), eloUri: {eloUri}");
               setFeedbackNotAsked();
            }
        } else {
            setFeedbackNotAsked();
        }
    }

    function findFeedbackElo(eloUri: URI): ScyElo {
        if (eloUri != null) {
            def metadataAllVersions = toolBrokerAPI.getRepository().retrieveMetadataAllVersions(eloUri);
            Collections.reverse(metadataAllVersions);
            for (metadataObject in metadataAllVersions) {
                def metadata = metadataObject as IMetadata;
                def feedbackElo = findFeedbackEloVersion(metadata.getMetadataValueContainer(identifierKey).getValue() as URI);
                if (feedbackElo != null) {
                    return feedbackElo;
                }
            }
        }
        return null;
    }

    function findFeedbackEloVersion(eloUri: URI): ScyElo {
        def query = QueryFactory.createFeedbackEloQuery(eloUri, technicalType);
        var searchResults: List = toolBrokerAPI.getRepository().search(query);
        if (searchResults.size() > 0) {
            logger.debug("found {searchResults.size()} feedback ELO-s");
            for (r in searchResults) {
                def result: roolo.search.ISearchResult = r as roolo.search.ISearchResult;
                def feedbackElo = ScyElo.loadMetadata(result.getUri(), toolBrokerAPI);
                if (feedbackElo.getFeedbackOnEloUri() == eloUri) {
                    return feedbackElo;
                }
            }
        }
        return null;
    }

    function setFeedbackAsked(questionDisplay: String) {
        textBox.text = questionDisplay;
        textBox.editable = false;
        submitButton.action = scyDesktop.scyFeedbackGetButton.action;
        submitButton.text = "View feedback";
    }

    function setFeedbackNotAsked() {
        textBox.editable = true;
        textBox.text = "";
        submitButton.action = askFeedbackQuestion;
        submitButton.text = "Ask feedback";
    }

    function askFeedbackQuestion(): Void {
        if (eloUri == null) {
            javafx.stage.Alert.inform("No ELO loaded.");
            return;
        }
        def feedbackElement = new Element(feedbackTagName);
        def createdbyElement = new Element(createdbyTagName);
        createdbyElement.setText(toolBrokerAPI.getLoginUserName());
        feedbackElement.addContent(createdbyElement);
        def createdbypictureElement = new Element(createdbypictureTagName);
        feedbackElement.addContent(createdbypictureElement);
        def datetime = new Date();
        def dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        def calendardateElement = new Element(calendardateTagName);
        calendardateElement.setText(dateFormat.format(datetime));
        feedbackElement.addContent(calendardateElement);
        def timeFormat = new SimpleDateFormat("HH:mm");
        def calendartimeElement = new Element(calendartimeTagName);
        calendartimeElement.setText(timeFormat.format(datetime));
        feedbackElement.addContent(calendartimeElement);
        def commentElement = new Element(commentTagName);
        commentElement.setText(textBox.rawText);
        feedbackElement.addContent(commentElement);
        def fbScyElo = ScyElo.createElo(technicalType, toolBrokerAPI);
        fbScyElo.setTitle(title);
        fbScyElo.setFeedbackOnEloUri(eloUri);
        fbScyElo.getContent().setXmlString(jdomStringConversion.xmlToString(feedbackElement));
        fbScyElo.saveAsNewElo();
        logFeedbackAsked(fbScyElo.getUri().toString());
        setFeedbackAsked(getQuestionDisplay(fbScyElo));
    }

    function logFeedbackAsked(eloURI : String) : Void {
        def action: IAction = new Action();
        action.setUser(toolBrokerAPI.getLoginUserName());
        action.setType("feedback_asked");
        action.addContext(ContextConstants.tool, "scy-lab");
        action.addContext(ContextConstants.mission, toolBrokerAPI.getMissionRuntimeURI().toString());
        action.addContext(ContextConstants.session, "n/a");
        action.addContext(ContextConstants.eloURI, eloURI);
        toolBrokerAPI.getActionLogger().log(action);
    }

    function getQuestionDisplay(elo: ScyElo): String {
        def contentXml = elo.getContent().getXmlString();
        def feedbackXml = jdomStringConversion.stringToXml(contentXml);
        def date = feedbackXml.getChildTextTrim(calendardateTagName);
        def time = feedbackXml.getChildTextTrim(calendartimeTagName);
        def question = feedbackXml.getChildTextTrim(commentTagName);
        "You have asked for feedback.\nOn{date} at {time}\nQuestion:\n{question}"
    }

    public override function getDrawerUIIndicator(): DrawerUIIndicator {
        return DrawerUIIndicator.FEEDBACK;
    }

    public override var height on replace {
                resizeContent();
            }
    public override var width on replace {
                resizeContent();
            }

    function resizeContent(): Void {
        Container.resizeNode(mainBox, width, height);
    }

    public override function getPrefHeight(width: Number): Number {
        if (height < getMinHeight())
            return getMinHeight();
        return height;
    }

    public override function getPrefWidth(height: Number): Number {
        if (width < getMinWidth())
            return getMinWidth();
        return width;
    }

    public override function getMinHeight(): Number {
        return 10;
    }

    public override function getMinWidth(): Number {
        return titleLabel.boundsInParent.width + 50;
    }

}
