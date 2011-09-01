package eu.scy.core.roolo;

import eu.scy.common.scyelo.ScyElo;
import eu.scy.core.roolo.ELOWebSafeTransporter;
import eu.scy.core.roolo.RooloAccessor;
import roolo.api.IExtensionManager;
import roolo.api.IRepository;
import roolo.search.IQueryComponent;
import roolo.search.MetadataQueryComponent;
import roolo.search.IQuery;
import roolo.search.Query;
import roolo.search.ISearchResult;
import roolo.search.SearchOperation;
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
        IQueryComponent missionSpecificationQueryComponent = new MetadataQueryComponent(technicalFormatKey, SearchOperation.EQUALS, "scy/missionspecification");
        IQuery missionSpecificationQuery = new Query(missionSpecificationQueryComponent);
        List result = getELOs(missionSpecificationQuery);
    }

    @Override
    public <ScyElo> List getELOSWithTechnicalFormat(String technicalFormat) {
        IMetadataKey technicalFormatKey = getMetaDataTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT);
        IQueryComponent interviewQueryComponent = new MetadataQueryComponent(technicalFormatKey, SearchOperation.EQUALS, technicalFormat);
        IQuery interviewQuery = new Query(interviewQueryComponent);
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
            ISearchResult searchResult = (ISearchResult) eloSearchResult.get(i);
            //FIXME this wont work anymore!!!
//            IELO elo = searchResult.getELO();
            //and this isnt the good way, but compiling
            ScyElo scyELO = getElo(searchResult.getUri());
            result.add(scyELO);
        }
        return result;
    }




    @Override
    public List<ELOWebSafeTransporter> getWebSafeTransporters(List elo) {
        List <ELOWebSafeTransporter> returnList = new LinkedList<ELOWebSafeTransporter>();
        if(elo.size() > 0 ) {
            if(elo.get(0) instanceof ScyElo) {
                List<ScyElo> elos = new LinkedList<ScyElo>();
                for (int i = 0; i < elo.size(); i++) {
                    ScyElo scyElo = elos.get(i);
                    elos.add(scyElo);
                    ELOWebSafeTransporter transporter = new ELOWebSafeTransporter(scyElo);
                    returnList.add(transporter);
                }
            } else if(elo.get(0) instanceof ISearchResult) {
                for (int i = 0; i < elo.size(); i++) {
                    ISearchResult searchResult = (ISearchResult) elo.get(i);
                    ScyElo eloo = ScyElo.loadLastVersionElo(searchResult.getUri(), this);
                    ELOWebSafeTransporter transporter = new ELOWebSafeTransporter(eloo);
                    returnList.add(transporter);
                }
            }
        }

        return returnList;
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
