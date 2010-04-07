package eu.scy.core;

import eu.scy.core.model.ELORef;

import java.util.List;

/**
 * @author bjoerge
 * @created 23.feb.2010 18:00:44
 */
public interface ELORefService extends BaseService {

	List<ELORef> getELORefs();

	void save(ELORef eloRef);

	ELORef getELORefById(String id);
}