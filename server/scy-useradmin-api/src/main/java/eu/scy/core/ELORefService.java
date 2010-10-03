package eu.scy.core;

import eu.scy.core.model.ELORef;
import eu.scy.core.model.ScyBase;

import java.util.List;

/**
 * @author bjoerge
 * @created 23.feb.2010 18:00:44
 */
public interface ELORefService extends BaseService {

	List<ELORef> getELORefs();

    List <ELORef> getAllVisibleELORefs();

	void save(ELORef eloRef);

	ELORef getELORefById(String id);

    void delete(ELORef model);
}