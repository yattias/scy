package eu.scy.core.model;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * User: Henrik
 * Date: 17.jun.2008
 * Time: 00:06:52
 * Roles are used by the permission scheme of SCY to identify type of user and his/her authorities
 */
@Entity
@Table(name = "role")
public class Role extends SCYBaseObject{
}
