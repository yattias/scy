/*
 * Created on 16.09.2011
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package eu.scy.agents.groupformation.strategies.features;

import info.collide.sqlspaces.client.TupleSpace;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import eu.scy.common.scyelo.RooloServices;

import roolo.api.IRepository;
import roolo.elo.api.IELO;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import roolo.search.ISearchResult;
import roolo.search.MetadataQueryComponent;
import roolo.search.Query;

public abstract class AbstractFeatureExtractor implements FeatureExtractor {

    protected TupleSpace commandSpace;
    protected RooloServices repository;
    

    @Override
    public void setCommandSpace(TupleSpace commandSpace) {
        this.commandSpace = commandSpace;
    }

    @Override
    public void setRepository(RooloServices repository) {
        this.repository = repository;      
    }

    @Override
    public TupleSpace getCommandSpace() {
        return this.commandSpace;
    }

    @Override
    public abstract boolean canRun(IELO elo);


    protected abstract double[] getFeatures(String user, String mission, IELO elo,
                                     IELO userELO);

    protected IELO retrieveEloFromRepository(String user, String eloType) {
        MetadataQueryComponent mcq = new MetadataQueryComponent(
                                                                CoreRooloMetadataKeyIds.TECHNICAL_FORMAT.getId(),
                                                                eloType);
        Query q = new Query(mcq);
        HashSet<String> allowedUsers = new HashSet<String>();
        allowedUsers.add(user);
        q.setIncludedUsers(allowedUsers);
        List<ISearchResult> res = repository.getRepository().search(q);
        IELO elo = repository.getRepository().retrieveELO(res.get(0).getUri());
        return elo;
    }

}
