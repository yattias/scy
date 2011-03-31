package eu.scy.client.desktop.scydesktop.feedbackquestion;

import java.net.URI;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;

import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.LayoutInfo;
import javafx.scene.layout.Resizable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import eu.scy.client.desktop.scydesktop.tools.ScyToolFX;
import eu.scy.client.desktop.scydesktop.utils.jdom.JDomStringConversion;
import eu.scy.common.scyelo.QueryFactory;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.toolbrokerapi.ToolBrokerAPI;

import org.apache.log4j.Logger;

import org.jdom.Element;

import roolo.elo.api.IMetadataTypeManager;
import eu.scy.client.desktop.scydesktop.ScyDesktop;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.VBox;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import eu.scy.client.desktop.scydesktop.tools.DrawerUIIndicator;

public class FeedbackQuestionNode extends CustomNode, ScyToolFX, Resizable {

   def logger = Logger.getLogger("eu.scy.client.desktop.scydesktop.feedbackquestion.FeedbackQuestionNode");
   public var toolBrokerAPI: ToolBrokerAPI;
   public var metadataTypeManager: IMetadataTypeManager;
   public var scyDesktop:ScyDesktop;
   public var scyWindow:ScyWindow;
   def technicalType:String = "scy/feedback";
   def title:String = "Feedback ELO";
   def feedbackTagName:String = "feedback";
   def commentTagName:String = "comment";
   def createdbyTagName:String = "createdby";
   def createdbypictureTagName:String = "createdbypicture";
   def calendardateTagName:String = "calendardate";
   def calendartimeTagName:String = "calendartime";
   def jdomStringConversion = new JDomStringConversion();
   var comment:String = "";
   var eloUri: URI;
   var scyElo: ScyElo;
   def spacing = 5.0;
   def titleLabel = Label {
           text: "Ask for Feedback"
           font: Font.font("Verdana", FontWeight.BOLD, 12)
           textFill: scyWindow.windowColorScheme.mainColor;
       };
   var textBox = TextBox {
           text: bind comment with inverse
           multiline: true
           selectOnFocus: true
           columns: 20
       };
   var submitButton:Button = Button {
           text: "Submit"
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
              infoLabel.text = "Your feedback question: \"{textBox.text}\" has been submitted. To see the feedback, click:";
              titleLabel.visible = false;
              textBox.visible = false;
              submitButton.visible = false;
              questionAsked.visible=true;
          }
   }
   var infoLabel = Label {
                      text:"You already asked a question about this ELO. You can ask only one question per ELO. To see question you asked and answers and other information, please click:"
                      textWrap:true
                   };
   var feedbackLink = Hyperlink {
	                 text: "Get feedback"
	                 action: scyDesktop.scyFeedbackGetButton.action
                      };
   var questionAsked = VBox {content: [infoLabel, feedbackLink],visible:false};
   var node = Group {content: [
       titleLabel,
       Group {content: bind textBox},
       Group {content: bind submitButton},
       Group {content: bind questionAsked}
   ]};

   public override function create(): Node {
      var myNode = Group {content: bind node}
      resizeContent();
      FX.deferAction(resizeContent);
      return myNode;
   }

   public override function loadElo(eloUri: URI): Void {
      eloUriChanged(eloUri)
   }

   public override function loadedEloChanged(eloUri: URI): Void {
      eloUriChanged(eloUri)
   }

   function eloUriChanged(eloUri: URI) {
      this.eloUri = eloUri;
      var questionAskedB=true;
      if (not scyDesktop.missionModelFX.getEloUris(false).contains(eloUri)) {
          titleLabel.visible = false;
          textBox.visible = false;
          submitButton.visible = false;
     }
      if (eloUri != null) {
         logger.debug("eloUri is changing, new eloUri: {eloUri.toString()}");
         scyElo = ScyElo.loadMetadata(eloUri, toolBrokerAPI);
         def query = QueryFactory.createFeedbackEloQuery(eloUri, technicalType);
         var searchResults:List = toolBrokerAPI.getRepository().search(query);
         if (searchResults.size()==0) {
             questionAskedB=false;
         } else {
            logger.debug("found {searchResults.size()} feedback ELO-s");
            for (r in searchResults) {
                var result:roolo.search.ISearchResult = r as roolo.search.ISearchResult;
//                not working with current ISearchResult
//                logger.debug("found feedback ELO, eloUri: {result.getUri().toString()}, title: {result.getTitle()}, author: {result.getAuthors().toString()}, createDate: {new Date(result.getDateCreated()).toString()}");
            }
         }
      } else {
         scyElo = null;
      }
      if (questionAskedB) {
          titleLabel.visible = false;
          textBox.visible = false;
          submitButton.visible = false;
          questionAsked.visible = true;
      }
   }

   public override function getDrawerUIIndicator(): DrawerUIIndicator{
      return DrawerUIIndicator.FEEDBACK;
   }

   function resizeContent(): Void{
      textBox.height = height-titleLabel.height-submitButton.height-2*spacing-10;
      textBox.width = width;
      textBox.layoutInfo = LayoutInfo{
            height: height-titleLabel.height-submitButton.height-2*spacing-10
            width: width
      };
      textBox.translateY = titleLabel.height+spacing;
      submitButton.translateY = titleLabel.height+textBox.height+2*spacing;
      infoLabel.width = width;
      infoLabel.layoutInfo = LayoutInfo{
            width: width
      };
   }
   public override var height on replace {
       resizeContent();
   }
   public override var width on replace {
       resizeContent();
   }

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
       return 10;
   }

   public override function getMinWidth() : Number {
       return titleLabel.width+10;
   }

}
