/*
 * SCYMapperContentCreator.fx
 *
 * Created on 8-jul-2009, 15:41:12
 */

package eu.scy.client.tools.fxscymapper.registration;

import eu.scy.client.desktop.scydesktop.elofactory.WindowContentCreatorFX;

import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import java.net.URI;

import roolo.api.IRepository;
import roolo.elo.api.IMetadataTypeManager;

import eu.scy.scymapper.impl.SCYMapperPanel;

import eu.scy.scymapper.impl.configuration.SCYMapperToolConfiguration;


import org.springframework.context.support.ClassPathXmlApplicationContext;
import roolo.elo.api.IELOFactory;


import roolo.elo.api.IELO;
import eu.scy.client.common.datasync.IDataSyncService;
import eu.scy.toolbrokerapi.ToolBrokerAPI;

public class SCYMapperContentCreator extends WindowContentCreatorFX {
    public-init var eloFactory:IELOFactory;
    public-init var metadataTypeManager: IMetadataTypeManager;
    public-init var repository:IRepository;
    public-init var toolBroker:ToolBrokerAPI;

    var repositoryWrapper;

    function initRepositoryWrapper() {
        if (repositoryWrapper == null) {
            repositoryWrapper = new ScyMapperRepositoryWrapper();
            repositoryWrapper.setRepository(repository);
            repositoryWrapper.setMetadataTypeManager(metadataTypeManager);
            repositoryWrapper.setEloFactory(eloFactory);
        }
    }


    public override function getScyWindowContent(eloUri:URI, scyWindow:ScyWindow):Node{
        initRepositoryWrapper();
        var elo = repositoryWrapper.loadELO(eloUri);
        var scyMapperNode = createScyMapperNode(scyWindow, elo);
        return scyMapperNode;
    }

    public override function getScyWindowContentNew(scyWindow:ScyWindow): Node{
        initRepositoryWrapper();
        var elo = repositoryWrapper.createELO();
        return createScyMapperNode(scyWindow, elo);
    }

    function createScyMapperNode(scyWindow:ScyWindow, elo:IELO): SCYMapperNode{
        setWindowProperties(scyWindow);
        var shapesConfig = new ClassPathXmlApplicationContext("eu/scy/scymapper/shapesConfig.xml");

        var configuration = (shapesConfig.getBean("configuration")  as SCYMapperToolConfiguration);

        var conceptMap = repositoryWrapper.getELOConceptMap(elo);

        var scymapperPanel= new SCYMapperPanel(conceptMap, configuration);

        scymapperPanel.setToolBroker(toolBroker);

        return SCYMapperNode{
            scyMapperPanel:scymapperPanel;
            repositoryWrapper:repositoryWrapper;
            currentELO:elo;
            scyWindow: scyWindow;
        }
   }

   function setWindowProperties(scyWindow:ScyWindow){
      scyWindow.minimumWidth = 320;
      scyWindow.minimumHeight = 100;
   }


}
