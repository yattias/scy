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
		return getSession().createQuery("From ELORefImpl order by name")
				.list();
	}

	@Override
	public void createELORef(ELORef eloRef) {
		getHibernateTemplate().save(eloRef);
	}
}