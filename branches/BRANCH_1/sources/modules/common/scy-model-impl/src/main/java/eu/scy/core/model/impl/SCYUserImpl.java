package eu.scy.core.model.impl;

import eu.scy.core.model.*;
import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.okt.2008
 * Time: 05:49:47
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(name = "users")
//org.hibernate.annotations.Proxy(proxyClass = User.class)
public class SCYUserImpl implements User, Serializable {

    private long id;


    private UserDetails userDetails;

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    @OneToOne(cascade = CascadeType.ALL, targetEntity = SCYUserDetails.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_details_fk", nullable = false, unique = true)
    public UserDetails getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(SCYUserDetails userDetails) {
        this.userDetails = (UserDetails) userDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SCYUserImpl)) return false;

        SCYUserImpl scyUser = (SCYUserImpl) o;

        if (id != scyUser.id) return false;
        if (!userDetails.equals(scyUser.userDetails)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + userDetails.hashCode();
        return result;
    }
}
