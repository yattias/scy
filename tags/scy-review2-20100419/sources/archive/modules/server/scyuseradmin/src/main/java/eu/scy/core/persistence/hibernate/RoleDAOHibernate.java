package eu.scy.core.persistence.hibernate;

import eu.scy.core.model.Role;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 07.aug.2008
 * Time: 13:36:31
 * To change this template use File | Settings | File Templates.
 */
public class RoleDAOHibernate extends BaseDAOHibernate{

    public Role saveRole(String roleName) {
        Role existingRole = getRole(roleName);
        if(existingRole != null) {
            return existingRole;
        }
        Role newRole  =null;
        if(getRole(roleName) == null) {
            newRole = new Role();
            newRole.setName(roleName);
            getHibernateTemplate().save(newRole);
        } else {
            newRole = getRole(roleName);
        }
        return newRole; 
    }

    public Role getRole(String roleName) {
        return (Role) getSession().createQuery("from Role where name like :roleName")
                .setString("roleName", roleName)
                .uniqueResult();
    }

    public List<Role> getRoles() {
        return getSession().createQuery("from Role order by name")
                .list();
    }                                                                                    


}
