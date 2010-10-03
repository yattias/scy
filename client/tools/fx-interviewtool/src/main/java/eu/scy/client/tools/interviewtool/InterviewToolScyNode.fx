/*
 * InterviewToolNode.fx
 *
 * Created on 8.12.2009, 22:15:57
 */

package eu.scy.client.tools.interviewtool;

import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Group;
import javafx.scene.control.Button;
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
import eu.scy.client.desktop.scydesktop.ScyToolActionLogger;

import java.net.URI;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import eu.scy.client.desktop.scydesktop.utils.jdom.JDomStringConversion;
import roolo.api.search.IQuery;
import roolo.api.search.ISearchResult;
import roolo.cms.repository.mock.BasicMetadataQuery;
import roolo.cms.repository.search.BasicSearchOperations;

import javax.swing.JOptionPane;
import java.util.List;
import javafx.scene.layout.Resizable;

/**
 * @author kaido
 */

/**
* SCY Tool related behaviour of the Interview Tool
*/
public class InterviewToolScyNode extends InterviewToolNode, Resizable, ScyToolFX, EloSaverCallBack {
   def scyInterviewType = "scy/interview";
   def interviewTagName = "interview";
   def questionTagName = "question";
   def topicsTagName = "topics";
   def topicTagName = "topic";
   def indicatorsTagName = "indicators";
   def indicatorTagName = "indicator";
   def indicatorFormulationTagName = "formulation";
   def answersTagName = "answers";
   def answerTagName = "answer";
   def answerOtherNamelyTagName = "other";
   def answerOtherNamelyTrue = "true";
   def answerOtherNamelyFalse = "false";
   def jdomStringConversion = new JDomStringConversion();
   public var eloFactory:IELOFactory;
   public var repository:IRepository;
   public var extensionManager:IExtensionManager;
   public var actionLogger:IActionLogger;
   public var awarenessService:IAwarenessService;
   public var dataSyncService:IDataSyncService;
   public var pedagogicalPlanService:PedagogicalPlanService;
   public var scyWindow:ScyWindow;
   public var metadataTypeManager: IMetadataTypeManager;
   public var toolBrokerAPI:ToolBrokerAPI;
   protected var elo:IELO;
   var technicalFormatKey: IMetadataKey;

   function setLoggerEloUri() {
      var myEloUri:String = (scyWindow.scyToolsList.actionLoggerTool as ScyToolActionLogger).getURI();
      if (myEloUri == null and scyWindow.eloUri != null)
         myEloUri = scyWindow.eloUri.toString();
      interviewLogger.eloUri = myEloUri;
      schemaEditor.setEloUri(myEloUri);
   }

   public override function initialize(windowContent:Boolean):Void{
      var username:String = toolBrokerAPI.getLoginUserName();
      var toolname:String = "interviewtool";
      var missionname:String = toolBrokerAPI.getMission();
      var sessionname:String = "n/a";
      technicalFormatKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT);
      interviewLogger = InterviewLogger{
         actionLogger: actionLogger
         username: username
         toolname: toolname
         missionname: missionname
         sessionname: sessionname
      };
      schemaEditor.setRichTextEditorLogger(actionLogger,
         username, toolname, missionname, sessionname, "interview schema");
      setLoggerEloUri();
   }

   public override function loadElo(uri:URI){
      doLoadElo(uri);
   }

   function doLoadElo(eloUri:URI) {
      logger.info("Trying to load elo {eloUri}");
      var newElo = repository.retrieveELO(eloUri);
      if (newElo != null) {
         eloContentXmlToInterview(newElo.getContent().getXmlString());
         logger.info("elo interview loaded");
         elo = newElo;
      }
      setLoggerEloUri();
   }

   function openElo() {
      var query:IQuery = new BasicMetadataQuery(technicalFormatKey,
         BasicSearchOperations.EQUALS, scyInterviewType, null);
      var searchResults:List = repository.search(query);
      var interviewUris:URI[];
      for (searchResult in searchResults)
         insert (searchResult as ISearchResult).getUri() into interviewUris;
      var interviewUri:URI = JOptionPane.showInputDialog(null, ##"Select interview",
      ##"Select interview", JOptionPane.QUESTION_MESSAGE, null, interviewUris, null) as URI;
      if (interviewUri != null) {
         loadElo(interviewUri);
      }
   }

   function doSaveElo(){
      elo.getContent().setXmlString(interviewToEloContentXml());
      eloSaver.eloUpdate(getElo(),this);
   }

   function doSaveAsElo(){
     eloSaver.eloSaveAs(getElo(),this);
   }

    override public function eloSaveCancelled (elo : IELO) : Void {
    }

    override public function eloSaved (elo : IELO) : Void {
        this.elo = elo;
        setLoggerEloUri();
    }

   function getElo():IELO{
      if (elo==null){
         elo = eloFactory.createELO();
         elo.getMetadata().getMetadataValueContainer(technicalFormatKey).setValue(scyInterviewType);
      }
      elo.getContent().setXmlString(interviewToEloContentXml());
      return elo;
   }

   function interviewToEloContentXml():String{
      var interviewElement = new Element(interviewTagName);
      var questionElement = new Element(questionTagName);
      questionElement.setText(question);
      interviewElement.addContent(questionElement);
      var topicsElement = new Element(topicsTagName);
      for (topic in topics) {
         var topicElement = new Element(topicTagName);
         var topicTopicElement = new Element(topicTagName);
         topicTopicElement.setText(topic.topic);
         topicElement.addContent(topicTopicElement);
         var indicatorsElement = new Element(indicatorsTagName);
         for (indicator in topic.indicators) {
            var indicatorElement = new Element(indicatorTagName);
            var indicatorIndicatorElement = new Element(indicatorTagName);
            indicatorIndicatorElement.setText(indicator.indicator);
            indicatorElement.addContent(indicatorIndicatorElement);
            var indicatorFormulationElement = new Element(indicatorFormulationTagName);
            indicatorFormulationElement.setText(indicator.formulation);
            indicatorElement.addContent(indicatorFormulationElement);
            var indicatorOtherElement = new Element(answerOtherNamelyTagName);
            if (indicator.answerIncludeNamely)
                indicatorOtherElement.setText(answerOtherNamelyTrue)
            else
                indicatorOtherElement.setText(answerOtherNamelyFalse);
            indicatorElement.addContent(indicatorOtherElement);
            var answersElement = new Element(answersTagName);
            for (answer in indicator.answers) {
               var answerElement = new Element(answerTagName);
               answerElement.setText(answer.toString());
               answersElement.addContent(answerElement);
            }
            indicatorElement.addContent(answersElement);
            indicatorsElement.addContent(indicatorElement);
         }
         topicElement.addContent(indicatorsElement);
         topicsElement.addContent(topicElement);
      }
      interviewElement.addContent(topicsElement);
      return jdomStringConversion.xmlToString(interviewElement);
   }
   function eloContentXmlToInterview(text:String) {
      var interviewElement=jdomStringConversion.stringToXml(text);
      if (interviewTagName != interviewElement.getName()){
         logger.error("wrong tag name, expected {interviewTagName}, but got {interviewElement.getName()}");
      }
      var questionElement = interviewElement.getChild(questionTagName);
      question = questionElement.getTextTrim();
      topics = [];
      for (topicElement in interviewElement.getChild(topicsTagName).getChildren(topicTagName)) {
         var topic = InterviewTopic{};
         topic.topic = (topicElement as Element).getChild(topicTagName).getTextTrim();
         for (indicatorElement in (topicElement as Element).getChild(indicatorsTagName).getChildren(indicatorTagName)) {
            var indicator = InterviewIndicator{};
            indicator.indicator = (indicatorElement as Element).getChild(indicatorTagName).getTextTrim();
            indicator.formulation = (indicatorElement as Element).getChild(indicatorFormulationTagName).getTextTrim();
            var otherNamely = (indicatorElement as Element).getChild(answerOtherNamelyTagName).getTextTrim();
            if (otherNamely == answerOtherNamelyTrue)
                indicator.answerIncludeNamely = true
            else
                indicator.answerIncludeNamely = false;
            for (answerElement in (indicatorElement as Element).getChild(answersTagName).getChildren(answerTagName)) {
                var answer = InterviewAnswer{};
                answer.answer = (answerElement as Element).getTextTrim();
                insert answer into indicator.answers;
             }
             insert indicator into topic.indicators;
         }
         insert topic into topics;
      }
      refreshTree();
      activateTreeNodeByValue(INTERVIEW_TOOL_HOME);
   }
   override function postInitialize():Void {
      activateTreeNodeByValue(INTERVIEW_TOOL_HOME);
   }
   public override function create(): Node {
      var node:Node = Group{content: bind content};
      var buttons:Node =
          HBox{
             translateX:10;
             spacing:10;
             content:[
                /* 100301 Jakob, commented out open button, it should not be in the tool
                Button {
                   text: ##"Open"
                   font: buttonFont
                   action: function() {
                       openElo();
                   }
                }
                */
                Button {
                   text: ##"Save"
                   font: buttonFont
                   action: function() {
                      doSaveElo();
                   }
                }
                Button {
                   text: ##"Save as"
                   font: buttonFont
                   action: function() {
                        doSaveAsElo();
                   }
                }
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
protected override function getAuthors(anonUser:String, andStr:String) {
/*
    if (elo != null) {
logger.debug(CoreRooloMetadataKeyIds.AUTHOR);
logger.debug(elo.getMetadata().getMetadataValueContainer(metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.AUTHOR)).toString());
logger.debug(elo.getMetadata().getMetadataValueContainer(metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.AUTHOR)).getValue().toString());
        name = elo.getMetadata().getMetadataValueContainer(metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.AUTHOR)).getValue().toString();
    }
*/
    return  "{toolBrokerAPI.getLoginUserName()} {andStr} [{anonUser}]";
}


   public override function onQuit() {
       schemaEditor.insertedTextToActionLog();
   }

}
