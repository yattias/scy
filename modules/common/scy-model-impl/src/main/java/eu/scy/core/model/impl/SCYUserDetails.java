package eu.scy.core.model.impl;

import org.telscenter.sail.webapp.domain.authentication.impl.StudentUserDetails;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 03.mar.2009
 * Time: 12:23:59
 * Overridden details class in order to add SCY specific user fields
 */
@Entity (name = "eu.scy.core.model.impl.SCYUserDetails")
//Table(name = "user_details")
public class SCYUserDetails extends StudentUserDetails {

}
