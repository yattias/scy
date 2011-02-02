package eu.scy.core.roolo;

import eu.scy.common.scyelo.ScyElo;
import eu.scy.core.roolo.ELOWebSafeTransporter;
import eu.scy.core.roolo.RooloAccessor;
import org.roolo.search.BasicMetadataQuery;
import org.roolo.search.BasicSearchOperations;
import org.roolo.search.SearchResult;
import roolo.api.IExtensionManager;
import roolo.api.IRepository;
import roolo.api.search.IQuery;
import roolo.elo.api.IELO;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;

import java.net.URI;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 20.okt.2010
 * Time: 12:59:58
 * To change this template use File | Settings | File Templates.
 */
public class RooloAccessorImpl implements RooloAccessor {

    private IRepository repository;
    private IMetadataTypeManager metaDataTypeManager;
    private IExtensionManager extensionManager;
    private IELOFactory eloFactory;

    public void findMissionSpecifications() {
        final IMetadataKey technicalFormatKey = getMetaDataTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT);
        IQuery missionSpecificationQuery = new BasicMetadataQuery(technicalFormatKey, BasicSearchOperations.EQUALS, "scy/missionspecification");
        List result = getELOs(missionSpecificationQuery);
    }

    @Override
    public <ScyElo> List getELOSWithTechnicalFormat(String technicalFormat) {
        IMetadataKey technicalFormatKey = getMetaDataTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT);
        IQuery interviewQuery = new BasicMetadataQuery(technicalFormatKey, BasicSearchOperations.EQUALS, technicalFormat);
        List result = getELOs(interviewQuery);

        return result;
    }

    @Override
    public ScyElo getElo(URI uri) {
        ScyElo elo =ScyElo.loadElo(uri, this);
        // System.out.println("LOADED ELO: " + elo.getTitle() + " (" + elo.getUri() + ")");
        return   elo;
    }

    protected List getELOs(IQuery interviewQuery) {
        List eloSearchResult = getRepository().search(interviewQuery);
        List result = new LinkedList();
        for (int i = 0; i < eloSearchResult.size(); i++) {
            SearchResult searchResult = (SearchResult) eloSearchResult.get(i);
            IELO elo = searchResult.getELO();
            ScyElo scyELO = getElo(elo.getUri());
            result.add(scyELO);
        }
        return result;
    }

    @Override
     public List <ELOWebSafeTransporter> getWebSafeTransporters(List <ScyElo> missionSpecificationElos) {
        List transporters = new LinkedList();
        for (int i = 0; i < missionSpecificationElos.size(); i++) {
            transporters.add(new ELOWebSafeTransporter(missionSpecificationElos.get(i)));
        }
        return transporters;
    }

    @Override
    public ELOWebSafeTransporter getWebSafeTransporter(ScyElo missionSpecificationElo) {
        ELOWebSafeTransporter transporter = new ELOWebSafeTransporter(missionSpecificationElo);
        return transporter;

    }


    @Override
    public IRepository getRepository() {
        return repository;
    }

    @Override
    public IMetadataTypeManager getMetaDataTypeManager() {
        return metaDataTypeManager;
    }

    public void setMetaDataTypeManager(IMetadataTypeManager metaDataTypeManager) {
        this.metaDataTypeManager = metaDataTypeManager;
    }

    @Override
    public IExtensionManager getExtensionManager() {
        return extensionManager;
    }

    public void setExtensionManager(IExtensionManager extensionManager) {
        this.extensionManager = extensionManager;
    }

    @Override
    public IELOFactory getELOFactory() {
        return eloFactory;
    }

    public void setEloFactory(IELOFactory eloFactory) {
        this.eloFactory = eloFactory;
    }

    @Override
    public void setRepository(IRepository repository) {
        this.repository = repository;
    }


}
