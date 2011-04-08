/*
 * SCYMapperContentCreator.fx
 *
 * Created on 8-jul-2009, 15:41:12
 */
package eu.scy.client.tools.fxscymapper.registration;

import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import eu.scy.scymapper.impl.SCYMapperPanel;
import eu.scy.scymapper.impl.configuration.SCYMapperToolConfiguration;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.apache.log4j.Logger;
import roolo.elo.api.IELO;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import eu.scy.client.desktop.scydesktop.elofactory.ScyToolCreatorFX;
import eu.scy.client.desktop.scydesktop.hacks.RepositoryWrapper;

public class SCYMapperContentCreator extends ScyToolCreatorFX {

    public var toolBrokerAPI: ToolBrokerAPI;
    public var userName: String;
    var repositoryWrapper;
    def logger = Logger.getLogger("eu.scy.client.tools.fxscymapper.registration.SCYMapperContentCreator");

    function initRepositoryWrapper() {
        if (repositoryWrapper == null) {
            repositoryWrapper = new ScyMapperRepositoryWrapper();
            repositoryWrapper.setRepository(toolBrokerAPI.getRepository());
            repositoryWrapper.setMetadataTypeManager(toolBrokerAPI.getMetaDataTypeManager());
            repositoryWrapper.setEloFactory(toolBrokerAPI.getELOFactory());
        }
    }

    public override function createScyToolNode(eloType:String, creatorId:String, scyWindow:ScyWindow, windowContent: Boolean):Node {
        initRepositoryWrapper();
        def elo = repositoryWrapper.createELO();
        def node = createScyMapperNode(scyWindow, elo);
        (toolBrokerAPI.getRepository() as RepositoryWrapper).addEloSavedListener(node);
        return node;
    }


    function createScyMapperNode(scyWindow: ScyWindow, elo: IELO): SCYMapperNode {
        setWindowProperties(scyWindow);
        var shapesConfig = new ClassPathXmlApplicationContext("eu/scy/scymapper/scymapperToolConfig.xml");

        var configuration = (shapesConfig.getBean("configuration") as SCYMapperToolConfiguration);

        var conceptMap = repositoryWrapper.getELOConceptMap(elo);

        var scymapperPanel = new SCYMapperPanelCollide(conceptMap, configuration);

        logger.debug("THE TOOLBROKERAPI IS: {toolBrokerAPI}");

        scymapperPanel.setToolBroker(toolBrokerAPI, userName);

        return SCYMapperNode {
            scyMapperPanel: scymapperPanel;
            repositoryWrapper: repositoryWrapper;
            currentELO: elo;
            scyWindow: scyWindow;
        }
    }

    function setWindowProperties(scyWindow: ScyWindow) {
        scyWindow.desiredContentWidth = 400;
        scyWindow.desiredContentHeight = 400;
    }

}
