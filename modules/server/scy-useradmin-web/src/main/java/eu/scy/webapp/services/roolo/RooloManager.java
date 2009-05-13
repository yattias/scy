package eu.scy.webapp.services.roolo;

import roolo.elo.api.*;
import roolo.elo.api.metadata.RooloMetadataKeys;
import roolo.elo.metadata.keys.Contribute;
import roolo.api.IRepository;

import javax.xml.bind.annotation.XmlTransient;
import java.util.Locale;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 27.mar.2009
 * Time: 13:48:41
 * To change this template use File | Settings | File Templates.
 */
public class RooloManager {

    @XmlTransient
    private IELOFactory eloFactory;

    @XmlTransient
    private IMetadataTypeManager metadataTypeManager;

    @XmlTransient
    private IRepository repository;

    @XmlTransient
    public IELOFactory getEloFactory() {
        return eloFactory;
    }

    public void setEloFactory(IELOFactory eloFactory) {
        this.eloFactory = eloFactory;
    }

    @XmlTransient
    public IMetadataTypeManager getMetadataTypeManager() {
        return metadataTypeManager;
    }

    public void setMetadataTypeManager(IMetadataTypeManager metadataTypeManager) {
        this.metadataTypeManager = metadataTypeManager;
    }

    @XmlTransient
    public IRepository getRepository() {
        return repository;
    }

    public void setRepository(IRepository repository) {
        this.repository = repository;
    }

    public void saveELO() {
        if(getEloFactory() != null) {
            IELO elo = getEloFactory().createELO();
            elo.setDefaultLanguage(Locale.ENGLISH);
            elo.getMetadata().getMetadataValueContainer(metadataTypeManager.getMetadataKey(RooloMetadataKeys.DATE_CREATED.getId())).setValue(new Long(System.currentTimeMillis()));


            try {
                elo.getMetadata().getMetadataValueContainer(metadataTypeManager.getMetadataKey(RooloMetadataKeys.MISSION.getId())).setValue(new URI("roolo://somewhere/myMission.mission"));
                elo.getMetadata().getMetadataValueContainer(metadataTypeManager.getMetadataKey(RooloMetadataKeys.AUTHOR.getId())).setValue(new Contribute("my vcard", System.currentTimeMillis()));
                elo.getMetadata().getMetadataValueContainer(metadataTypeManager.getMetadataKey(RooloMetadataKeys.TYPE.getId())).setValue("scy/conceptMap");
            } catch (URISyntaxException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

            IContent content = eloFactory.createContent();
            //content.setXmlString(ConceptMapExporter.getDefaultInstance().createXML());
            //content.
            elo.setContent(content);
            IMetadata<IMetadataKey> resultMetadata = getRepository().addELO(elo);
            eloFactory.updateELOWithResult(elo, resultMetadata);

        }

    }

}
