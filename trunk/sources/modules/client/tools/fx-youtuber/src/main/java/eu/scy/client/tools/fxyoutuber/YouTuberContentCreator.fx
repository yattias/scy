/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.fxyoutuber;
import javafx.scene.Node;
import javafx.util.Properties;
import eu.scy.client.desktop.scydesktop.elofactory.ScyToolWindowContentCreatorFX;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import java.net.URI;
import roolo.api.IRepository;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataTypeManager;

/**
 * @author pg
 */

public class YouTuberContentCreator extends ScyToolWindowContentCreatorFX {
    override function createScyToolWindowContent():Node {
        //StringLocalizer.associate("eu.scy.client.tools.fxformauthor.resources.FormAuthor", "eu.scy.client.tools.fxformauthor");
        println("createscytoolwindowcontent");
        return YouTuberNode{};
    }

    public var eloFactory:IELOFactory;
    public var metadataTypeManager: IMetadataTypeManager;
    public var repository:IRepository;
    init {
        println("youtuber content creator");
    }


    public override function getScyWindowContent(eloUri: URI, scyWindow:ScyWindow):Node {
            var ytNode = createYouTuberNode(scyWindow);
            return ytNode;
    }
    public override function getScyWindowContentNew(scyWindow:ScyWindow):Node {
            return createYouTuberNode(scyWindow);
    }
    function createYouTuberNode(scyWindow:ScyWindow):YouTuberNode {
        setWindowProperties(scyWindow);
        var props:Properties = new Properties();
        props.put("show.filetoolbar", "false");
        var ytNode:YouTuberNode = YouTuberNode{scyWindow: scyWindow};
        var youTuberRepositoryWrapper:YouTuberRepositoryWrapper = new YouTuberRepositoryWrapper(ytNode);
        youTuberRepositoryWrapper.setRepository(repository);
        youTuberRepositoryWrapper.setMetadataTypeManager(metadataTypeManager);
        youTuberRepositoryWrapper.setEloFactory(eloFactory);
        youTuberRepositoryWrapper.setDocName(scyWindow.title);
        ytNode.setFormAuthorRepositoryWrapper(youTuberRepositoryWrapper);
        return ytNode;
    }

    function setWindowProperties(scyWindow:ScyWindow) {
        scyWindow.minimumWidth = 320;
        scyWindow.minimumHeight = 100;
    }
}
