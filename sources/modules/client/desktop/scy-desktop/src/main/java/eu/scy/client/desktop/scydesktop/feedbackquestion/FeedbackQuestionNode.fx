package eu.scy.client.desktop.scydesktop.feedbackquestion;

import java.net.URI;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextBox;
import javafx.scene.text.Font;

import eu.scy.client.desktop.scydesktop.tools.ScyToolFX;
import eu.scy.client.desktop.scydesktop.utils.jdom.JDomStringConversion;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.toolbrokerapi.ToolBrokerAPI;

import org.apache.log4j.Logger;

import org.jdom.Element;

import roolo.elo.api.IMetadataTypeManager;

import eu.scy.common.scyelo.QueryFactory;

public class FeedbackQuestionNode extends CustomNode, ScyToolFX {

   def logger = Logger.getLogger("eu.scy.client.desktop.scydesktop.feedbackquestion.FeedbackQuestionNode");
   public var toolBrokerAPI: ToolBrokerAPI;
   public var metadataTypeManager: IMetadataTypeManager;
   def technicalType:String = "scy/feedback";
   def title:String = "Feedback ELO";
   def feedbackTagName:String = "feedback";
   def commentTagName:String = "comment";
   def createdbyTagName:String = "createdby";
   def createdbypictureTagName:String = "createdbypicture";
   def calendardateTagName:String = "calendardate";
   def calendartimeTagName:String = "calendartime";
   def jdomStringConversion = new JDomStringConversion();
   def spacing = 5.0;
   var comment:String = "";
   var eloUri: URI;
   var scyElo: ScyElo;
   var submitButton:Button = Button {
               text: "Submit"
               disable: true
               action: function() {
                  if (eloUri==null) {
                      javafx.stage.Alert.inform("ELO not in Roolo.");
                      return;
                  }
                  var feedbackElement = new Element(feedbackTagName);
                  var createdbyElement = new Element(createdbyTagName);
                  createdbyElement.setText(toolBrokerAPI.getLoginUserName());
                  feedbackElement.addContent(createdbyElement);
                  var createdbypictureElement = new Element(createdbypictureTagName);
                  feedbackElement.addContent(createdbypictureElement);
                  def datetime = new Date();
                  def dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                  var calendardateElement = new Element(calendardateTagName);
                  calendardateElement.setText(dateFormat.format(datetime));
                  feedbackElement.addContent(calendardateElement);
                  def timeFormat = new SimpleDateFormat("HH:mm");
                  var calendartimeElement = new Element(calendartimeTagName);
                  calendartimeElement.setText(timeFormat.format(datetime));
                  feedbackElement.addContent(calendartimeElement);
                  var commentElement = new Element(commentTagName);
                  commentElement.setText(comment);
                  feedbackElement.addContent(commentElement);
                  def fbScyElo = ScyElo.createElo(technicalType, toolBrokerAPI);
                  fbScyElo.setTitle(title);
                  fbScyElo.setFeedbackOnEloUri(eloUri);
                  fbScyElo.getContent().setXmlString(jdomStringConversion.xmlToString(feedbackElement));
                  fbScyElo.saveAsNewElo();
                  submitButton.disable = true;
              }
   }

   public override function create(): Node {
      VBox {
         spacing: spacing
         content: [
            Label {
               text: "Ask for Feedback"
               font: Font{size:18}
            }
            TextBox {
                text: bind comment with inverse
                columns: 10
                lines: 20
                multiline: true
                selectOnFocus: true
            }
            submitButton
         ]
      }
   }

   public override function loadElo(eloUri: URI): Void {
      eloUriChanged(eloUri)
   }

   public override function loadedEloChanged(eloUri: URI): Void {
      eloUriChanged(eloUri)
   }

   function eloUriChanged(eloUri: URI) {
      this.eloUri = eloUri;
      submitButton.disable=true;
      if (eloUri != null) {
         logger.debug("eloUri is changing, new eloUri: {eloUri.toString()}");
         scyElo = ScyElo.loadMetadata(eloUri, toolBrokerAPI);
         def query = QueryFactory.createFeedbackEloQuery(eloUri, technicalType);
         var searchResults:List = toolBrokerAPI.getRepository().search(query);
         if (searchResults.size()==0) {
             submitButton.disable=false;
         } else {
            logger.debug("found {searchResults.size()} feedback ELO-s");
            for (r in searchResults) {
                var result:roolo.search.ISearchResult = r as roolo.search.ISearchResult;
                logger.debug("found feedback ELO, eloUri: {result.getUri().toString()}, title: {result.getTitle()}, author: {result.getAuthors().toString()}, createDate: {new Date(result.getDateCreated()).toString()}");
            }
         }
      } else {
         scyElo = null;
      }
   }

}
