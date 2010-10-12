package eu.scy.core;

import eu.scy.core.model.ELORef;
import eu.scy.core.persistence.ELORefDAO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.logging.Logger;

/**
 * @author bjoerge
 * @created 23.feb.2010 19:26:30
 */
public class ELORefServiceImpl extends BaseServiceImpl implements ELORefService {

	private static Logger log = Logger.getLogger("ELORefServiceImpl.class");

	private ELORefDAO eloRefDAO;

	@Transactional
	public void createELORef(ELORef eloRef) {
		getEloRefDAO().createELORef(eloRef);
	}

	@Override
	public List<ELORef> getELORefs() {
		return getEloRefDAO().getELORefs();
	}

    @Override
    public List<ELORef> getAllVisibleELORefs() {
        return getEloRefDAO().getAllVisibleELORefs();
    }

    @Transactional
	@Override
	public void save(ELORef eloRef) {
		getEloRefDAO().save(eloRef);
	}

	@Override
	public ELORef getELORefById(String id) {
		return getEloRefDAO().getELORefById(id);
	}

    @Override
    @Transactional
    public void delete(ELORef model) {
        getEloRefDAO().delete(model);
    }

    public ELORefDAO getEloRefDAO() {
		return (ELORefDAO) getScyBaseDAO();
	}

	public void setEloRefDAO(ELORefDAO eloRefDAO) {
		this.eloRefDAO = eloRefDAO;
	}
}