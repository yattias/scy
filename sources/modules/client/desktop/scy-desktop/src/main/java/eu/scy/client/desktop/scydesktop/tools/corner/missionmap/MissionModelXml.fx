/*
 * MissionModelXml.fx
 *
 * Created on 19-dec-2009, 13:42:19
 */
package eu.scy.client.desktop.scydesktop.tools.corner.missionmap;

import eu.scy.client.desktop.scydesktop.utils.jdom.JDomStringConversion;
import java.net.URI;
import java.util.HashMap;
import org.jdom.Element;

/**
 * @author sikken
 */
// place your code here
public class MissionModelXml {
}

def missionModelName = "missionModel";
def activeAnchorName = "activeAnchor";
def anchorsName = "anchors";
def anchorName = "anchor";
def xPosName = "xPos";
def yPosName = "yPos";
def eloUriName = "eloUri";
def mainAnchorName = "mainAnchor";
def nextAnchorsName = "nextAnchors";
def inputAnchorsName = "inputAnchors";
def supportElosName = "supportElos";
def helpElosName = "helpElos";
def relationsName = "relations";
def relationName = "relation";

def jdomStringConversion = new JDomStringConversion();

public function convertToXml(missionModel:MissionModelFX):String{
   var root = new Element(missionModelName);
   var activeAnchorUri = "";
   if (missionModel.activeAnchor != null){
      activeAnchorUri = missionModel.activeAnchor.eloUri.toString();
   }
   root.addContent(createElement(activeAnchorName,activeAnchorUri));
   var anchors = new Element(anchorsName);
   root.addContent(anchors);
   for (anchor in missionModel.anchors){
      anchors.addContent(createMissionAnchorXml(anchor));
   }
   return jdomStringConversion.xmlToString(root);
}

function createElement(name:String, value:String):Element{
   var element = new Element(name);
   element.setText(value);
   return element;
}

function createMissionAnchorXml(missionAnchor:MissionAnchorFX):Element{
   var root = new Element(anchorName);
   root.addContent(createElement(eloUriName, "{missionAnchor.eloUri.toString()}"));
   root.addContent(createElement(xPosName, "{missionAnchor.xPos}"));
   root.addContent(createElement(yPosName, "{missionAnchor.yPos}"));
   root.addContent(createElement(mainAnchorName, "{missionAnchor.mainAnchor}"));
   root.addContent(createAnchorListXml(nextAnchorsName,missionAnchor.nextAnchors));
   root.addContent(createAnchorListXml(inputAnchorsName,missionAnchor.inputAnchors));
   root.addContent(createEloUriListXml(supportElosName,missionAnchor.supportEloUris));
   root.addContent(createEloUriListXml(helpElosName,missionAnchor.helpEloUris));
   root.addContent(createStringListXml(relationsName,missionAnchor.relationNames));
   return root;
}

function createAnchorListXml(tagName:String ,anchors:MissionAnchorFX[] ):Element{
   var anchorList = new Element(tagName);
   for (anchor in anchors) {
      anchorList.addContent(createElement(anchorName, anchor.eloUri.toString()));
   }
   return anchorList;
}

function createEloUriListXml(tagName:String ,uris:URI[] ):Element{
   var uriList = new Element(tagName);
   for (uri in uris) {
      uriList.addContent(createElement(eloUriName, uri.toString()));
   }
   return uriList;
}

function createStringListXml(tagName:String ,strings:String[] ):Element{
   var stringList = new Element(tagName);
   for (string in strings) {
      stringList.addContent(createElement(relationName, string));
   }
   return stringList;
}

public function convertToMissionModel(xml: String): MissionModelFX {
   var missionModel = MissionModelFX {
           };
   var root = jdomStringConversion.stringToXml(xml);
   var missionAnchors = root.getChild(anchorsName);
   var missionAnchorChildren = missionAnchors.getChildren(anchorName);
   var missionAnchorsMap = new HashMap();
   if (missionAnchorChildren!=null){
      // find all anchors
      for (missionAnchorChild in missionAnchorChildren) {
         var missionAnchor = createMissionAnchor(missionAnchorChild as Element);
         missionAnchorsMap.put(missionAnchor.eloUri, missionAnchor);
         insert missionAnchor into missionModel.anchors;
      }
      // fill in anchor links
      for (missionAnchorChild in missionAnchorChildren) {
         var missionAnchorElement = missionAnchorChild as Element;
         var missionAnchorUri = missionAnchorElement.getChildText(eloUriName);
         var missionAnchor = missionAnchorsMap.get(new URI(missionAnchorUri)) as MissionAnchorFX;
         missionAnchor.nextAnchors = createMissionAnchorList(missionAnchorElement.getChild(nextAnchorsName),missionAnchorsMap);
         missionAnchor.inputAnchors = createMissionAnchorList(missionAnchorElement.getChild(inputAnchorsName),missionAnchorsMap);
      }
   }
   var activeAnchorUriName = root.getChildText(activeAnchorName);
   if (activeAnchorUriName != null)
      missionModel.activeAnchor = missionAnchorsMap.get(new URI(activeAnchorUriName)) as MissionAnchorFX;
   return missionModel;
}

function createMissionAnchor(root:Element):MissionAnchorFX{
   var missionAnchor = MissionAnchorFX{
      eloUri: new URI(root.getChildText(eloUriName))
      xPos: java.lang.Float.parseFloat(root.getChildText(xPosName));
      yPos: java.lang.Float.parseFloat(root.getChildText(yPosName));
      mainAnchor:java.lang.Boolean.parseBoolean(root.getChildText(mainAnchorName))
      supportEloUris: createEloUriList(root.getChild(supportElosName))
      helpEloUris: createEloUriList(root.getChild(helpElosName))
      relationNames: createStringList(root.getChild(relationsName))
   }
   return missionAnchor;
}

function createEloUriList(root:Element):URI[]{
   var eloUris:URI[];
   if (root!=null){
      var children = root.getChildren(eloUriName);
      if (children!=null){
         for (child in children) {
            var eloUriChild = child as Element;
            var eloUri = new URI(eloUriChild.getText());
            insert eloUri into eloUris;
         }
      }
   }
   return eloUris;
}

function createStringList(root:Element):String[]{
   var strings:String[];
   if (root!=null){
      var children = root.getChildren(relationName);
      if (children!=null){
         for (child in children) {
            var stringChild = child as Element;
            insert stringChild.getText() into strings;
         }
      }
   }
   return strings;
}

function createMissionAnchorList(root:Element,missionAnchorsMap: HashMap):MissionAnchorFX[]{
   var missionAnchors:MissionAnchorFX[];
   if (root!=null){
      var children = root.getChildren(anchorName);
      if (children!=null){
         for (child in children) {
            var missionAnchorChild = child as Element;
            var missionAnchorEloUri = new URI(missionAnchorChild.getText());
            var missionAnchor = missionAnchorsMap.get(missionAnchorEloUri) as MissionAnchorFX;
            insert missionAnchor into missionAnchors;
         }
      }
   }
   return missionAnchors;
}


