package eu.scy.core.model.impl;

import eu.scy.core.model.*;
import net.sf.sail.webapp.domain.authentication.MutableUserDetails;
import net.sf.sail.webapp.domain.authentication.impl.PersistentUserDetails;
import net.sf.sail.webapp.domain.sds.SdsUser;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.okt.2008
 * Time: 05:49:47
 * To change this template use File | Settings | File Templates.
 */

@Entity(name = "eu.scy.core.model.impl.SCYUserImpl")
@Table(name = "users")
@org.hibernate.annotations.Proxy(proxyClass = User.class)
public class SCYUserImpl implements User {

    @Transient
    public static final String DATA_STORE_NAME = "users";

    @Transient
    public static final String COLUMN_NAME_SDS_USER_FK = "sds_user_fk";

    @Transient
    public static final String COLUMN_NAME_USER_DETAILS_FK = "user_details_fk";

    @Transient
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id = null;

    @Version
    @Column(name = "OPTLOCK")
    private Integer version = null;

    @Transient
    private SdsUser sdsUser = null;

    private SCYUserDetails userDetails;


    /**
     * @return the id
     */
    @SuppressWarnings("unused")
    public Long getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    @SuppressWarnings("unused")
    private void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the version
     */
    @SuppressWarnings("unused")
    private Integer getVersion() {
        return version;
    }

    /**
     * @param version
     *            the version to set
     */
    @SuppressWarnings("unused")
    private void setVersion(Integer version) {
        this.version = version;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result
                + ((this.sdsUser == null) ? 0 : this.sdsUser.hashCode());
        result = PRIME
                * result
                + ((this.userDetails == null) ? 0 : this.userDetails.hashCode());
        return result;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final SCYUserImpl other = (SCYUserImpl) obj;
        if (this.sdsUser == null) {
            if (other.sdsUser != null)
                return false;
        } else if (!this.sdsUser.equals(other.sdsUser))
            return false;
        if (this.userDetails == null) {
            if (other.userDetails != null)
                return false;
        } else if (!this.userDetails.equals(other.userDetails))
            return false;
        return true;
    }


    @OneToOne(cascade = CascadeType.ALL, targetEntity = SCYUserDetails.class)
    @JoinColumn(name = COLUMN_NAME_USER_DETAILS_FK, nullable = false, unique = true)
    public MutableUserDetails getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(MutableUserDetails mutableUserDetails) {
        this.userDetails = (SCYUserDetails) mutableUserDetails;
    }


    public void setSdsUser(SdsUser sdsUser) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Transient
    public SdsUser getSdsUser() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
