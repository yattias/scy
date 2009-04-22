/*
 * MissionModelXml.fx
 *
 * Created on 31-mrt-2009, 13:49:18
 */

package eu.scy.elobrowser.tool.missionmap;

import eu.scy.elobrowser.tool.missionmap.Anchor;
import eu.scy.elobrowser.tool.missionmap.MissionModel;
import java.net.URI;
import java.util.HashMap;
import org.jdom.Element;
import roolo.elo.JDomStringConversion;

/**
 * @author sikkenj
 */

public class MissionModelXml {

}

def missionModelName = "missionModel";
def activeAnchorName = "activeAnchor";
def anchorsName = "anchors";
def anchorName = "anchor";
def xPosName = "xPos";
def yPosName = "yPos";
def eloUriName = "eloUri";
def nextAnchorsName = "nextAnchors";
def relationsName = "relations";
def relationName = "relation";

def jdomStringConversion = new JDomStringConversion();

public function convertToXml(missionModel:MissionModel):String{
    var root = new Element(missionModelName);
    var activeAnchorUri = "";
    if (missionModel.activeAnchor!=null) activeAnchorUri = missionModel.activeAnchor.eloUri.toString();
    root.addContent(createElement(activeAnchorName,activeAnchorUri));
    var anchors = new Element(anchorsName);
    root.addContent(anchors);
    for (anchor in missionModel.anchors){
        anchors.addContent(createAnchorXml(anchor));
    }
    return jdomStringConversion.xmlToString(root);
}

function createElement(name:String, value:String):Element{
   var element = new Element(name);
   element.setText(value);
   return element;
}

function createAnchorXml(anchor:Anchor):Element{
    var root = new Element(anchorName);
    root.addContent(createElement(xPosName, "{anchor.xPos}"));
    root.addContent(createElement(yPosName, "{anchor.yPos}"));
    root.addContent(createElement(eloUriName, "{anchor.eloUri.toString()}"));
    var nextAnchors = new Element(nextAnchorsName);
    root.addContent(nextAnchors);
    for (nextAnchor in anchor.nextAnchors){
        nextAnchors.addContent(createElement(anchorName,nextAnchor.eloUri.toString()));
    }
    var relations = new Element(relationsName);
    root.addContent(relations);
    for (relation in anchor.relationNames){
        relations.addContent(createElement(relationName,relation));
    }
    return root;
}

public function convertToMissionModel(xml:String):MissionModel{
    var missionModel = MissionModel{};
    var root = jdomStringConversion.stringToXml(xml);
    var anchors = root.getChild(anchorsName);
    var anchorChildren = anchors.getChildren(anchorName);
    // find all anchors
    var anchorsMap = new HashMap();
    for (anchorChild in anchorChildren){
        var anchor = createAnchor(anchorChild as Element);
        anchorsMap.put(anchor.eloUri, anchor);
        insert anchor into missionModel.anchors;
    }
    // fill in nextAnchors
    for (anchorChild in anchorChildren){
        var anchorElement = anchorChild as Element;
        var anchorUriName = anchorElement.getChildText(eloUriName);
        var anchor = anchorsMap.get(new URI(anchorUriName)) as Anchor;
        var nextAnchors = anchorElement.getChild(nextAnchorsName);
        var nextAnchorChildren = nextAnchors.getChildren(anchorName);
        for (nextAnchorChild in nextAnchorChildren){
            var nextAnchorUriName = (nextAnchorChild as Element).getText();
            var nextAnchor = anchorsMap.get(new URI(nextAnchorUriName)) as Anchor;
            if (nextAnchor!=null) insert nextAnchor into anchor.nextAnchors;
        }
    }
    var activeAnchorUriName = root.getChildText(activeAnchorName);
    if (activeAnchorUriName!=null) missionModel.activeAnchor = anchorsMap.get(new URI(activeAnchorUriName)) as Anchor;
    return missionModel;
}

function createAnchor(anchorRoot:Element):Anchor{
    var anchor = Anchor{
        xPos: java.lang.Integer.parseInt(anchorRoot.getChildText(xPosName));
        yPos: java.lang.Integer.parseInt(anchorRoot.getChildText(yPosName));
        eloUri: new URI(anchorRoot.getChildText(eloUriName))
    }
    var relations = anchorRoot.getChild(relationsName);
    var relationChildren = relations.getChildren(relationName);
    for (relationChild in relationChildren){
        insert (relationChild as Element).getText() into anchor.relationNames;
    }
    return anchor;
}


