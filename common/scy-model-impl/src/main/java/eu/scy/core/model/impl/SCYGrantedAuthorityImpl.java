package eu.scy.core.model.impl;

import eu.scy.core.model.SCYGrantedAuthority;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 03.feb.2010
 * Time: 06:34:27
 * Role
 */
@Entity(name = "eu.scy.core.model.impl.SCYGrantedAuthority")
@Table(name="granted_authorities")
public class SCYGrantedAuthorityImpl implements SCYGrantedAuthority, Serializable {

    private Long id;

    private Integer version;


    private String authority;



    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Version
    @Column(name = "OPTLOCK")
    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    @Column(name = "authority", unique = true, nullable = false)
    public String getAuthority() {
        return authority;
    }

    @Override
    public void setAuthority(String authority) {
        this.authority = authority;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result
                + ((this.authority == null) ? 0 : this.authority.hashCode());
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
        final SCYGrantedAuthorityImpl other = (SCYGrantedAuthorityImpl) obj;
        if (this.authority == null) {
            if (other.authority != null)
                return false;
        } else if (!this.authority.equals(other.authority))
            return false;
        return true;
    }

	public int compareTo(Object arg0) {
		return 0;
	}
}
