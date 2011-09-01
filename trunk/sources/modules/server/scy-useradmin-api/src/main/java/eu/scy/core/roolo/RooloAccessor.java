package eu.scy.core.roolo;

import eu.scy.common.scyelo.RooloServices;
import eu.scy.common.scyelo.ScyElo;
import roolo.api.IRepository;
import roolo.elo.api.IMetadataTypeManager;
import roolo.search.ISearchResult;

import java.net.URI;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 20.okt.2010
 * Time: 12:59:39
 * To change this template use File | Settings | File Templates.
 */
public interface RooloAccessor extends RooloServices {
    IRepository getRepository();

    void setRepository(IRepository repository);

    void setMetaDataTypeManager(IMetadataTypeManager metadataTypeManager);

    public <ScyElo> List getELOSWithTechnicalFormat(String technicalFormat);

    public ScyElo getElo(URI uri);

    List <ELOWebSafeTransporter> getWebSafeTransporters(List elo);

    ELOWebSafeTransporter getWebSafeTransporter(ScyElo elo);

}
