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
import eu.scy.client.desktop.scydesktop.tools.ScyToolFX;
import eu.scy.client.desktop.scydesktop.tools.EloSaverCallBack;
import org.jdom.Element;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import roolo.api.IRepository;
import roolo.api.IExtensionManager;
import eu.scy.actionlogging.api.IActionLogger;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import eu.scy.client.desktop.scydesktop.ScyToolActionLogger;

import java.net.URI;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import eu.scy.client.desktop.scydesktop.utils.jdom.JDomStringConversion;
import roolo.search.IQuery;
import roolo.search.Query;
import roolo.search.ISearchResult;

import javax.swing.JOptionPane;
import java.util.List;
import javafx.scene.layout.Resizable;
import roolo.search.MetadataQueryComponent;
import roolo.search.IQueryComponent;
import roolo.search.SearchOperation;
import java.awt.image.BufferedImage;
import java.awt.Dimension;
import javafx.geometry.Bounds;
import java.awt.Graphics2D;
import javafx.scene.layout.Container;
import javafx.reflect.FXLocal;
import javafx.geometry.BoundingBox;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import eu.scy.client.desktop.scydesktop.tools.TitleBarButton;
import eu.scy.client.desktop.scydesktop.tools.TitleBarButtonManager;

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
   public var scyWindow:ScyWindow;
   public var metadataTypeManager: IMetadataTypeManager;
   public var toolBrokerAPI:ToolBrokerAPI;
   protected var elo:IELO;
   var technicalFormatKey: IMetadataKey;
   // interval in milliseconds after what typed text is wrote
   // into action log - should be configurable from authoring tools
   public var interviewSchemaTypingLogIntervalMs = 30000;
   def saveTitleBarButton = TitleBarButton {
              actionId: "save"
              iconType: "save"
              action: doSaveElo
              tooltip: "save ELO"
           }
   def saveAsTitleBarButton = TitleBarButton {
              actionId: "saveAs"
              iconType: "save_as"
              action: doSaveAsElo
              tooltip: "save copy of ELO"
           }

   function setLoggerEloUri() {
      var myEloUri:String = (scyWindow.scyToolsList.actionLoggerTool as ScyToolActionLogger).getURI();
      if (myEloUri == null and scyWindow.eloUri != null)
         myEloUri = scyWindow.eloUri.toString();
      interviewLogger.eloUri = myEloUri;
      schemaEditor.setEloUri(myEloUri);
   }

   public override function initialize(windowContent:Boolean):Void{
       repository = toolBrokerAPI.getRepository();
       metadataTypeManager = toolBrokerAPI.getMetaDataTypeManager();
       eloFactory = toolBrokerAPI.getELOFactory();
       extensionManager = toolBrokerAPI.getExtensionManager();
       actionLogger = toolBrokerAPI.getActionLogger();

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

   public override function setTitleBarButtonManager(titleBarButtonManager: TitleBarButtonManager, windowContent: Boolean): Void {
      if (windowContent) {
         titleBarButtonManager.titleBarButtons = [
                    saveTitleBarButton,
                    saveAsTitleBarButton
                 ]
      }
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
      var metadataQuery:IQueryComponent = new MetadataQueryComponent(technicalFormatKey,SearchOperation.EQUALS, scyInterviewType);
      var query:IQuery = new Query(metadataQuery);
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
      schemaEditor.setTypingLogIntervalMs(interviewSchemaTypingLogIntervalMs);
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
      if (elo != null) {
         def oldContentXml = elo.getContent().getXmlString();
         def newContentXml = getElo().getContent().getXmlString();
         if (oldContentXml == newContentXml) {
            // nothing changed
            return;
         }
      }
      doSaveElo();
    }

    public function nodeToImage(node : Node, bounds : Bounds) : BufferedImage {
        def context = FXLocal.getContext();
        def nodeClass = context.findClass("javafx.scene.Node");
        def getFXNode = nodeClass.getFunction("impl_getPGNode");
        var g2:Graphics2D;
        if(node instanceof Container) {
            (node as Resizable).width = bounds.width;
            (node as Resizable).height = bounds.height;
            (node as Container).layout();
        } else if(node instanceof Resizable) {
            (node as Resizable).width = bounds.width;
            (node as Resizable).height = bounds.height;
        }
        def nodeBounds = node.layoutBounds;
        def sgNode = (getFXNode.invoke(context.mirrorOf(node))
                as FXLocal.ObjectValue).asObject();
        def g2dClass = (context.findClass("java.awt.Graphics2D")
                as FXLocal.ClassType).getJavaImplementationClass();
        def boundsClass = (context.findClass("com.sun.javafx.geom.Bounds2D")
                as FXLocal.ClassType).getJavaImplementationClass();
        def affineClass = (context.findClass("com.sun.javafx.geom.transform.BaseTransform")
                as FXLocal.ClassType).getJavaImplementationClass();
        def getBounds = sgNode.getClass().getMethod("getContentBounds",
                boundsClass, affineClass);
        def bounds2D = getBounds.invoke(sgNode, new com.sun.javafx.geom.Bounds2D(),
                new com.sun.javafx.geom.transform.Affine2D());
        var paintMethod = sgNode.getClass().getMethod("render", g2dClass,
                boundsClass, affineClass);
        def bufferedImage = new java.awt.image.BufferedImage(
                nodeBounds.width, nodeBounds.height,
                java.awt.image.BufferedImage.TYPE_INT_ARGB);
        g2 = (bufferedImage.getGraphics() as Graphics2D);
        g2.setPaint(java.awt.Color.WHITE);
        g2.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
        paintMethod.invoke(sgNode, g2, bounds2D,
                new com.sun.javafx.geom.transform.Affine2D());
        g2.dispose();
        return bufferedImage;
    }

    public function scaleImage(bufferedImage : BufferedImage, originalSize:Dimension,
            targetSize:Dimension) : BufferedImage {
        var bi:BufferedImage = bufferedImage;
        var factor:Double;
        if ((originalSize.width - targetSize.width) < (originalSize.height - targetSize.height)) {
            factor = 1.0 * targetSize.height / originalSize.height;
        } else {
            factor = 1.0 * targetSize.width / originalSize.width;
        }
        var scale:AffineTransform = AffineTransform.getScaleInstance(factor, factor);
        var op:AffineTransformOp = new AffineTransformOp(scale, AffineTransformOp.TYPE_BILINEAR);
        bi = op.filter(bi, null);
        return bi;
    }

    public override function getThumbnail(width: Integer, height: Integer): BufferedImage {
        return scaleImage(
                    nodeToImage(this, BoundingBox {
                        width: this.layoutBounds.width
                        height: this.layoutBounds.height}),
                    new Dimension(this.layoutBounds.width, this.layoutBounds.height),
                    new Dimension(width, height)
               );
    }

}
