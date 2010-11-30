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
        println("formauithornodebla");
        setWindowProperties(scyWindow);
        var props:Properties = new Properties();
        props.put("show.filetoolbar", "false");
        /*
        var formNode:FormAuthorNode = FormAuthorNode{scyWindow: scyWindow};
        var formAuthorRepositoryWrapper:FormAuthorRepositoryWrapper = new FormAuthorRepositoryWrapper(formNode);
        formAuthorRepositoryWrapper.setRepository(repository);
        formAuthorRepositoryWrapper.setMetadataTypeManager(metadataTypeManager);
        formAuthorRepositoryWrapper.setEloFactory(eloFactory);
        formAuthorRepositoryWrapper.setDocName(scyWindow.title);
        formNode.setFormAuthorRepositoryWrapper(formAuthorRepositoryWrapper);
        return formNode;
        */
        return null;
    }

    function setWindowProperties(scyWindow:ScyWindow) {
        scyWindow.minimumWidth = 320;
        scyWindow.minimumHeight = 100;
    }
}
