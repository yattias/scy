package eu.scy.client.desktop.scydesktop.feedbackquestion;

import java.net.URI;
import java.util.Date;
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
import eu.scy.client.desktop.desktoputils.XFX;

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
   var actionInProgress = false;
   def spacing = 5.0;
   def titleLabel = Text {
              content: "Feedback"
              font: Font.font("Verdana", FontWeight.BOLD, 12)
              fill: scyWindow.windowColorScheme.mainColor;
           };
   def questionEntryBox = TextBox {
              multiline: true
              selectOnFocus: true
              managed: true
              layoutInfo: LayoutInfo {
                 hfill: true
                 vfill: true
                 hgrow: Priority.ALWAYS
                 vgrow: Priority.ALWAYS
                 minHeight:50
                 maxHeight:106
              }
           };
   def questionsAskedBox = TextBox {
              multiline: true
              editable: false
              managed: true
              layoutInfo: LayoutInfo {
                 hfill: true
                 vfill: true
                 hgrow: Priority.ALWAYS
                 vgrow: Priority.ALWAYS
              }
           };
   def submitButton: Button = Button {
              disable: bind eloUri == null or questionEntryBox.rawText.trim() == "" or actionInProgress
              text: ##"Ask feedback"
              action: function(): Void {
                 actionInProgress = true;
                 XFX.runActionInBackground(function(): Void {
                    askFeedbackQuestion();
                    FX.deferAction(function(): Void {
                       actionInProgress = false;
                    })
                 })
              }
           }
   var mainBox: VBox;
   var identifierKey: IMetadataKey;

   public override function create(): Node {
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
                    questionsAskedBox,
                    questionEntryBox,
                    submitButton
                 ]
              };
   }

   public override function loadedEloChanged(eloUri: URI): Void {
      this.eloUri = eloUri;
      questionEntryBox.text = "";
      questionsAskedBox.text = "";
      if (eloUri != null) {
         XFX.runActionInBackground(eloUriChanged);
      }
   }

   function eloUriChanged(): Void {
      def feedbackElos = findFeedbackElos(eloUri);
      FX.deferAction(function(): Void {
         for (feedbackElo in feedbackElos) {
            addFeedbackQuestionToDisplay(feedbackElo)
         }
      });
   }

   function findFeedbackElos(eloUri: URI): ScyElo[] {
      var feedbackElos: ScyElo[];
      if (eloUri != null) {
         def metadataAllVersions = toolBrokerAPI.getRepository().retrieveMetadataAllVersions(eloUri);
//         Collections.reverse(metadataAllVersions);
         for (metadataObject in metadataAllVersions) {
            def metadata = metadataObject as IMetadata;
            def versionEloUri = metadata.getMetadataValueContainer(identifierKey).getValue() as URI;
            def query = QueryFactory.createFeedbackEloQuery(versionEloUri, technicalType);
            def searchResults = toolBrokerAPI.getRepository().search(query);
            for (searchResult in searchResults) {
               def feedbackElo = ScyElo.loadMetadata(searchResult.getUri(), toolBrokerAPI);
               insert feedbackElo into feedbackElos
            }
         }
      }
      return feedbackElos;
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
      commentElement.setText(questionEntryBox.rawText);
      feedbackElement.addContent(commentElement);
      def fbScyElo = ScyElo.createElo(technicalType, toolBrokerAPI);
      fbScyElo.setTitle(title);
      fbScyElo.setFeedbackOnEloUri(eloUri);
      fbScyElo.getContent().setXmlString(jdomStringConversion.xmlToString(feedbackElement));
      fbScyElo.saveAsNewElo();
      logFeedbackAsked(fbScyElo.getUri().toString());
      FX.deferAction(function(): Void {
         addFeedbackQuestionToDisplay(fbScyElo);
         questionEntryBox.text = ""
      });
   }

   function logFeedbackAsked(eloURI: String): Void {
      def action: IAction = new Action();
      action.setUser(toolBrokerAPI.getLoginUserName());
      action.setType("feedback_asked");
      action.addContext(ContextConstants.tool, "scy-lab");
      action.addContext(ContextConstants.mission, toolBrokerAPI.getMissionRuntimeURI().toString());
      action.addContext(ContextConstants.session, "n/a");
      action.addContext(ContextConstants.eloURI, eloURI);
      toolBrokerAPI.getActionLogger().log(action);
   }

   function addFeedbackQuestionToDisplay(elo: ScyElo): Void {
      def questionDisplay = getQuestionDisplay(elo);
      if (questionsAskedBox.text == "") {
         questionsAskedBox.text = questionDisplay
      } else {
         questionsAskedBox.text = "{questionsAskedBox.text}\n\n{questionDisplay}"
      }
   }

   function getQuestionDisplay(elo: ScyElo): String {
      def contentXml = elo.getContent().getXmlString();
      def feedbackXml = jdomStringConversion.stringToXml(contentXml);
      def date = feedbackXml.getChildTextTrim(calendardateTagName);
      def time = feedbackXml.getChildTextTrim(calendartimeTagName);
      def question = feedbackXml.getChildTextTrim(commentTagName);
      "{date} {time}:\n{question}"
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
