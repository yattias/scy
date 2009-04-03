/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.elobrowser.tool.colemo;

import eu.scy.colemo.client.*;
import java.net.URI;
import roolo.api.IRepository;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataTypeManager;


/**
 *
 * @author Henrik
 */
public class ColemoActionWrapper {

    public static final String scyMappingType = "scy/mapping";

    private ColemoPanel colemoPanel;
    private IRepository repository;
    private IELOFactory eloFactory;
    private IMetadataTypeManager metadataTypeManager;
    
    ColemoActionWrapper(ColemoPanel colemoPanel) {
        this.colemoPanel = colemoPanel;
    }

    public void loadElo(URI eloUri) {
        ApplicationController.getDefaultInstance().loadElo(eloUri);
    }

    public void connect(){
        System.out.println("Connecting!");
        
    }

    public void cleanUp() {
        colemoPanel.cleanUp();
    }

    public void createNewConcept() {
        colemoPanel.addNewConcept(null, "c");
    }

    public IRepository getRepository() {
        return repository;
    }

    public void setRepository(IRepository repository) {
        this.repository = repository;
        
        ApplicationController.getDefaultInstance().setRepository(repository);

    }

    public IELOFactory getEloFactory() {
        return eloFactory;
    }

    public void setEloFactory(IELOFactory eloFactory) {
        this.eloFactory = eloFactory;
        ApplicationController.getDefaultInstance().setEloFactory(eloFactory);
    }

    public IMetadataTypeManager getMetadataTypeManager() {
        return metadataTypeManager;
    }

    public void setMetadataTypeManager(IMetadataTypeManager metadataTypeManager) {
        this.metadataTypeManager = metadataTypeManager;
        ApplicationController.getDefaultInstance().setMetadataTypeManager(metadataTypeManager);
    }

    

    



   
}
