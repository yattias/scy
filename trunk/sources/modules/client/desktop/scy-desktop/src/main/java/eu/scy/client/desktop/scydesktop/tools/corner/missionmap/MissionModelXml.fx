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
def idName = "id";
def nameName = "name";
def loElosName = "loElos";
def otherElosName = "otherElos";
def activeLasName = "activeLas";
def lassesName = "lasses";
def lasName = "las";
def nextLassesName = "nextLasses";
def anchorsName = "anchors";
def anchorName = "anchor";
def xPosName = "xPos";
def yPosName = "yPos";
def eloUriName = "eloUri";
def iconTypeName = "iconType";
def mainAnchorName = "mainAnchor";
def toolTipName = "toolTip";
def nextAnchorsName = "nextAnchors";
def inputAnchorsName = "inputAnchors";
def resourceElosName = "resourceElos";
def intermediateAnchorsName = "intermediateAnchors";
def intermediateAnchorName = "intermediateAnchor";
def relationsName = "relations";
def relationName = "relation";

def jdomStringConversion = new JDomStringConversion();

public function convertToXml(missionModel:MissionModelFX):String{
   var root = new Element(missionModelName);
   root.addContent(createElement(idName,missionModel.id));
   root.addContent(createElement(nameName,missionModel.name));
   var activeLasId = "";
   if (missionModel.activeLas != null){
      activeLasId = missionModel.activeLas.id;
   }
   root.addContent(createElement(activeLasName,activeLasId));
   root.addContent(createEloUriListXml(loElosName,missionModel.loEloUris));
   var lasses = new Element(lassesName);
   root.addContent(lasses);
   for (las in missionModel.lasses){
      lasses.addContent(createLasXml(las));
   }
   return jdomStringConversion.xmlToString(root);
}

function createElement(name:String, value:String):Element{
   var element = new Element(name);
   element.setText(value);
   return element;
}

function createLasXml(las:Las):Element{
   var lasRoot = new Element(lasName);
   lasRoot.addContent(createElement(idName,las.id));
   lasRoot.addContent(createElement(xPosName, "{las.xPos}"));
   lasRoot.addContent(createElement(yPosName, "{las.yPos}"));
   lasRoot.addContent(createElement(toolTipName, "{las.toolTip}"));
   lasRoot.addContent(createEloUriListXml(loElosName,las.loEloUris));
   lasRoot.addContent(createEloUriListXml(otherElosName,las.otherEloUris));
   lasRoot.addContent(createMissionAnchorXml(mainAnchorName, las.mainAnchor));
   var intermediateAnchorsRoot = new Element(intermediateAnchorsName);
   lasRoot.addContent(intermediateAnchorsRoot);
   for (intermediaAnchor in las.intermediateAnchors){
      intermediateAnchorsRoot.addContent(createMissionAnchorXml(intermediateAnchorName, intermediaAnchor));
   }
   var nextLassesRoot = new Element(nextLassesName);
   lasRoot.addContent(nextLassesRoot);
   for (nextLas in las.nextLasses){
      nextLassesRoot.addContent(createElement(lasName,nextLas.id));
   }

   lasRoot;
}

function createMissionAnchorXml(tagName:String, missionAnchor:MissionAnchorFX):Element{
   var root = new Element(tagName);
   root.addContent(createElement(eloUriName, "{missionAnchor.eloUri.toString()}"));
   root.addContent(createElement(iconTypeName, "{missionAnchor.iconType}"));
   root.addContent(createElement(mainAnchorName, "{missionAnchor.mainAnchor}"));
   root.addContent(createEloUriListXml(loElosName,missionAnchor.loEloUris));
   root.addContent(createAnchorListXml(inputAnchorsName,missionAnchor.inputAnchors));
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
   missionModel.id = root.getChildTextTrim(idName);
   missionModel.name = root.getChildTextTrim(nameName);
   missionModel.loEloUris = createEloUriList(root.getChild(loElosName));
   var lassesMap = new HashMap();
   var anchorsMap = new HashMap();
   var lassesRoot = root.getChild(lassesName);
   if (lassesRoot!=null){
      var lasChildrenRoot = lassesRoot.getChildren(lasName);
      if (lasChildrenRoot!=null){
         for (lasObject in lasChildrenRoot){
            var lasChild = lasObject as Element;
            var las = createLas(lasChild, anchorsMap);
            insert las into missionModel.lasses;
            lassesMap.put(las.id,las);
         }
      }
   }
   var activeLasId = root.getChildTextTrim(activeLasName);
   if (activeLasId!=null){
      missionModel.anchorSelected(lassesMap.get(activeLasId) as Las, null);
   }
   if (sizeof missionModel.lasses > 0){
      fillInMissingLinks(missionModel,lassesMap,anchorsMap,lassesRoot);
   }

   return missionModel;
}

function createLas(root:Element, anchorsMap: HashMap):Las{
   var las= Las{
      id: root.getChildTextTrim(idName)
      xPos: java.lang.Float.parseFloat(root.getChildTextTrim(xPosName));
      yPos: java.lang.Float.parseFloat(root.getChildTextTrim(yPosName));
      toolTip: root.getChildTextTrim(toolTipName)
      loEloUris: createEloUriList(root.getChild(loElosName))
      otherEloUris: createEloUriList(root.getChild(otherElosName))
      mainAnchor:createMissionAnchor(root.getChild(mainAnchorName))
   }
   las.mainAnchor.las = las;
   anchorsMap.put(las.mainAnchor.eloUri, las.mainAnchor);
   var intermediateAnchorsRoot = root.getChild(intermediateAnchorsName);
   var intermediateAnchorslist = intermediateAnchorsRoot.getChildren(intermediateAnchorName);
   if (intermediateAnchorslist!=null){
      for (intermediateAnchorObject in intermediateAnchorslist){
         var intermediateAnchorRoot = intermediateAnchorObject as Element;
         var intermediateAnchor = createMissionAnchor(intermediateAnchorRoot);
         insert intermediateAnchor into las.intermediateAnchors;
         intermediateAnchor.las = las;
         anchorsMap.put(intermediateAnchor.eloUri, intermediateAnchor);
      }
   }
   las;
}


function createMissionAnchor(root:Element):MissionAnchorFX{
   var missionAnchor = MissionAnchorFX{
      eloUri: new URI(root.getChildText(eloUriName))
      iconType: root.getChildText(iconTypeName)
      mainAnchor:java.lang.Boolean.parseBoolean(root.getChildText(mainAnchorName))
      loEloUris: createEloUriList(root.getChild(loElosName))
      relationNames: createStringList(root.getChild(relationsName),relationsName)
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

function createStringList(root:Element, tagName: String):String[]{
   var strings:String[];
   if (root!=null){
      var children = root.getChildren(tagName);
      if (children!=null){
         for (child in children) {
            var stringChild = child as Element;
            insert stringChild.getText() into strings;
         }
      }
   }
   return strings;
}

function fillInMissingLinks(missionModel:MissionModelFX, lassesMap: HashMap, anchorsMap: HashMap, lassesRoot:Element){
   var lasChildrenRoot = lassesRoot.getChildren(lasName);
   for (lasObject in lasChildrenRoot){
      var lasChild = lasObject as Element;
      var lasId = lasChild.getChildTextTrim(idName);
      var las = lassesMap.get(lasId) as Las;
      var nextLassesIds = createStringList(lasChild.getChild(nextLassesName),lasName);
      for (nextLasId in nextLassesIds){
         insert lassesMap.get(nextLasId) as Las into las.nextLasses;
      }
      // TODO scan input links of anchors
   }
}

function fillInMissingAnchorLinks(anchorRoot:Element, anchorsMap: HashMap){
   var anchor = anchorsMap.get(new URI(anchorRoot.getChildTextTrim(eloUriName))) as MissionAnchorFX;
   var inputAnchorsUriStrings = createStringList(anchorRoot.getChild(inputAnchorsName),anchorsName);
   for (inputAnchorUriString in inputAnchorsUriStrings){
      var inputAnchor = anchorsMap.get(new URI(inputAnchorUriString)) as MissionAnchorFX;
      insert inputAnchor into anchor.inputAnchors;
   }
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


