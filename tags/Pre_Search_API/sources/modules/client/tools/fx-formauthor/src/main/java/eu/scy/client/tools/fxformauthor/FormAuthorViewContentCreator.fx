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
import eu.scy.client.tools.fxformauthor.viewer.FormViewer;

/**
 * @author pg
 */

public class FormAuthorViewContentCreator extends ScyToolWindowContentCreatorFX {
    override function createScyToolWindowContent():Node {
        StringLocalizer.associate("eu.scy.client.tools.fxformauthor.resources.FormAuthor", "eu.scy.client.tools.fxformauthor");
        println("createscytoolwindowcontent");
        return FormViewer{};
    }

    public var eloFactory:IELOFactory;
    public var metadataTypeManager: IMetadataTypeManager;
    public var repository:IRepository;
    init {
        println("formauthor content creator");
    }


    public override function getScyWindowContent(eloUri: URI, scyWindow:ScyWindow):Node {
            var formView = createFormViewerNode(scyWindow);
            return formView;
    }
    public override function getScyWindowContentNew(scyWindow:ScyWindow):Node {
            return createFormViewerNode(scyWindow);
    }
    function createFormViewerNode(scyWindow:ScyWindow):FormViewer {
        println("formauithornodebla");
        setWindowProperties(scyWindow);
        var props:Properties = new Properties();
        props.put("show.filetoolbar", "false"); 
        var formView:FormViewer = FormViewer{scyWindow: scyWindow};
        var formAuthorRepositoryWrapper:FormAuthorRepositoryWrapper = new FormAuthorRepositoryWrapper(formView);
        formAuthorRepositoryWrapper.setRepository(repository);
        formAuthorRepositoryWrapper.setMetadataTypeManager(metadataTypeManager);
        formAuthorRepositoryWrapper.setEloFactory(eloFactory);
        formAuthorRepositoryWrapper.setDocName(scyWindow.title);
        formView.setFormAuthorRepositoryWrapper(formAuthorRepositoryWrapper);
        return formView;
    }

    function setWindowProperties(scyWindow:ScyWindow) {
        scyWindow.minimumWidth = 320;
        scyWindow.minimumHeight = 100;
    }
}
