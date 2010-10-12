/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.fxformauthor;
import eu.scy.client.desktop.scydesktop.elofactory.ScyToolWindowContentCreatorFX;
import javafx.scene.Node;
import javafx.util.StringLocalizer;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import java.net.URI;
import roolo.api.IRepository;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataTypeManager;
import javafx.util.Properties;

/**
 * @author pg
 */

public class FormAuthorContentCreator extends ScyToolWindowContentCreatorFX {
    override function createScyToolWindowContent():Node {
        StringLocalizer.associate("eu.scy.client.tools.fxformauthor.resources.FormAuthor", "eu.scy.client.tools.fxformauthor");
        println("createscytoolwindowcontent");
        return FormAuthorNode{};
    }

    public var eloFactory:IELOFactory;
    public var metadataTypeManager: IMetadataTypeManager;
    public var repository:IRepository;
    init {
        println("formauthor content creator");
    }


    public override function getScyWindowContent(eloUri: URI, scyWindow:ScyWindow):Node {
            var formNode = createFormAuthorNode(scyWindow);
            return formNode;
    }
    public override function getScyWindowContentNew(scyWindow:ScyWindow):Node {
            return createFormAuthorNode(scyWindow);
    }
    function createFormAuthorNode(scyWindow:ScyWindow):FormAuthorNode {
        println("formauithornodebla");
        setWindowProperties(scyWindow);
        var props:Properties = new Properties();
        props.put("show.filetoolbar", "false"); 
        var formNode:FormAuthorNode = FormAuthorNode{scyWindow: scyWindow};
        var formAuthorRepositoryWrapper:FormAuthorRepositoryWrapper = new FormAuthorRepositoryWrapper(formNode);
        formAuthorRepositoryWrapper.setRepository(repository);
        formAuthorRepositoryWrapper.setMetadataTypeManager(metadataTypeManager);
        formAuthorRepositoryWrapper.setEloFactory(eloFactory);
        formAuthorRepositoryWrapper.setDocName(scyWindow.title);
        formNode.setFormAuthorRepositoryWrapper(formAuthorRepositoryWrapper);
        return formNode;
    }

    function setWindowProperties(scyWindow:ScyWindow) {
        scyWindow.minimumWidth = 320;
        scyWindow.minimumHeight = 100;
    }
}
