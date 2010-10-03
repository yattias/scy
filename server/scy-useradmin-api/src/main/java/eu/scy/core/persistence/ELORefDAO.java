package eu.scy.core.persistence;

import eu.scy.core.model.ELORef;
import eu.scy.core.model.ScyBase;

import java.util.List;

/**
 * @author bjoerge
 * @created 23.feb.2010 18:08:01
 */
public interface ELORefDAO extends SCYBaseDAO {
	List<ELORef> getELORefs();

	void createELORef(ELORef eloRef);

	ELORef getELORefById(String id);

    List<ELORef> getAllVisibleELORefs();

    void delete(ELORef model);
}