/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.fxformauthor;
import javafx.scene.Node;
import javafx.util.StringLocalizer;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import roolo.api.IRepository;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataTypeManager;
import javafx.util.Properties;
import eu.scy.client.desktop.scydesktop.elofactory.ScyToolCreatorFX;
import eu.scy.toolbrokerapi.ToolBrokerAPI;

/**
 * @author pg
 */

public class FormAuthorContentCreator extends ScyToolCreatorFX {
    
    public override function createScyToolNode(eloType:String, creatorId:String, scyWindow:ScyWindow, windowContent: Boolean):Node {
        StringLocalizer.associate("eu.scy.client.tools.fxformauthor.resources.FormAuthor", "eu.scy.client.tools.fxformauthor");
        return createFormAuthorNode(scyWindow);
    }

    public var toolBrokerAPI:ToolBrokerAPI;
    public var eloFactory:IELOFactory;
    public var metadataTypeManager: IMetadataTypeManager;
    public var repository:IRepository;

    function createFormAuthorNode(scyWindow:ScyWindow):FormAuthorNode {
        repository = toolBrokerAPI.getRepository();
        metadataTypeManager = toolBrokerAPI.getMetaDataTypeManager();
        eloFactory = toolBrokerAPI.getELOFactory();
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
