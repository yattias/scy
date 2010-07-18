package eu.scy.core.persistence.hibernate;

import eu.scy.core.model.ELORef;
import eu.scy.core.persistence.ELORefDAO;

import java.util.List;

/**
 * @author bjoerge
 * @created 23.feb.2010 14:39:13
 */
public class ELORefDAOHibernate extends ScyBaseDAOHibernate implements ELORefDAO {

	@Override
	public List<ELORef> getELORefs() {
		return getSession().createQuery("from ELORefImpl order by viewings desc").list();
	}

	@Override
	public void createELORef(ELORef eloRef) {
		getHibernateTemplate().save(eloRef);
	}

	@Override
	public ELORef getELORefById(String id) {
		return (ELORef) getSession().createQuery("from ELORefImpl where id = :id").setString("id", id)
				.setMaxResults(1)
				.uniqueResult();
	}

    @Override
    public List<ELORef> getAllVisibleELORefs() {
        return getSession().createQuery("from ELORefImpl where hidden = :hidden order by viewings desc")
                .setBoolean("hidden", false)
                .list();
    }

    @Override
    public void delete(ELORef model) {
        model.setHidden(true);
        save(model);

    }
}