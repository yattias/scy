package eu.scy.core.model.impl;

import org.telscenter.sail.webapp.domain.authentication.impl.StudentUserDetails;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 03.mar.2009
 * Time: 12:23:59
 * To change this template use File | Settings | File Templates.
 */
@Entity (name = "eu.scy.core.model.impl.UserDetails")
@Table(name = "user_details")
public class UserDetails extends StudentUserDetails {
}
