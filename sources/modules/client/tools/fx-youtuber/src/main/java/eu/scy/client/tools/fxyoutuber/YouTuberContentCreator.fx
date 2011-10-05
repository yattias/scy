/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.fxyoutuber;
import javafx.scene.Node;
import javafx.util.Properties;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import eu.scy.client.desktop.scydesktop.elofactory.ScyToolCreatorFX;
import eu.scy.toolbrokerapi.ToolBrokerAPI;

/**
 * @author pg
 */

public class YouTuberContentCreator extends ScyToolCreatorFX {

    public var toolBrokerAPI:ToolBrokerAPI;

    public override function createScyToolNode(eloType:String, creatorId:String, scyWindow:ScyWindow, windowContent: Boolean):Node {
        setWindowProperties(scyWindow);
        var props:Properties = new Properties();
        props.put("show.filetoolbar", "false");
        var ytNode:YouTuberNode = YouTuberNode{scyDesktop: scyWindow.windowManager.scyDesktop};
        var youTuberRepositoryWrapper:YouTuberRepositoryWrapper = new YouTuberRepositoryWrapper(ytNode);
        youTuberRepositoryWrapper.setRepository(toolBrokerAPI.getRepository());
        youTuberRepositoryWrapper.setMetadataTypeManager(toolBrokerAPI.getMetaDataTypeManager());
        youTuberRepositoryWrapper.setEloFactory(toolBrokerAPI.getELOFactory());
        youTuberRepositoryWrapper.setDocName(scyWindow.title);
        ytNode.setFormAuthorRepositoryWrapper(youTuberRepositoryWrapper);
        return ytNode;
    }

    function setWindowProperties(scyWindow:ScyWindow) {
        scyWindow.minimumWidth = 320;
        scyWindow.minimumHeight = 100;
    }
}
