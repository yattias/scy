/*
 * Created on 16.09.2011
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package eu.scy.agents.groupformation.strategies.features;

import eu.scy.common.scyelo.RooloServices;
import info.collide.sqlspaces.client.TupleSpace;
import roolo.elo.api.IELO;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import roolo.search.ISearchResult;
import roolo.search.MetadataQueryComponent;
import roolo.search.Query;

import java.util.HashSet;
import java.util.List;

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

	protected abstract double[] getFeatures(String user, String mission, IELO elo, IELO userELO);

	protected IELO retrieveEloFromRepository(String user, String eloType) {
		MetadataQueryComponent mcq = new MetadataQueryComponent(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT.getId(),
				eloType);
		Query q = new Query(mcq);
		HashSet<String> allowedUsers = new HashSet<String>();
		allowedUsers.add(this.clean(user));
		q.setIncludedUsers(allowedUsers);
		List<ISearchResult> res = this.repository.getRepository().search(q);
		if (res.isEmpty()) {
			return null;
		}
		IELO elo = this.repository.getRepository().retrieveELO(res.get(0).getUri());
		return elo;
	}

	private String clean(String user) {
		int idx = user.indexOf('@');
		if (idx >= 0) {
			return user.substring(0, idx);
		} else {
			return user;
		}
	}
}
