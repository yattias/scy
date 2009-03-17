package eu.scy.core.model.impl;

import org.telscenter.sail.webapp.domain.authentication.impl.StudentUserDetails;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 03.mar.2009
 * Time: 12:23:59
 * To change this template use File | Settings | File Templates.
 */
@Entity (name = "eu.scy.core.model.impl.SCYUserDetails")
@Table(name = "user_details")
public class SCYUserDetails extends StudentUserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id = null;
    
    public Long getId() {
        return id;
    }

    @SuppressWarnings("unused")
    private void setId(Long id) {
        this.id = id;
    }


}
