package eu.scy.core.model.impl;

import eu.scy.core.model.SCYGrantedAuthority;
import eu.scy.core.model.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 03.mar.2009
 * Time: 12:23:59
 * Overridden details class in order to add SCY specific user fields
 */
//Entity(name = "eu.scy.core.model.impl.SCYUserDetails")
//Table(name = "user_details")
    @Entity
@Table(name = "user_details")
@Inheritance(strategy = InheritanceType.JOINED)
public class SCYUserDetails implements UserDetails, Serializable {

    private Long id;

    private String username;
    private String password;
    private boolean accountNotExpired = Boolean.TRUE;
    private boolean accoundNotLocked = Boolean.TRUE;
    private boolean credentialsNotExpired = Boolean.TRUE;
    private boolean enabled = Boolean.TRUE;
    private String locale = "en";

    private Set<SCYGrantedAuthority> grantedAuthorities = null;


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    @Column(name = "account_not_expired")
    public boolean isAccountNotExpired() {
        return accountNotExpired;
    }

    @Override
    public void setAccountNotExpired(boolean accountNotExpired) {
        this.accountNotExpired = accountNotExpired;
    }

    @Override
    @Column(name = "account_not_locked")
    public boolean isAccoundNotLocked() {
        return accoundNotLocked;
    }

    @Override
    public void setAccoundNotLocked(boolean accoundNotLocked) {
        this.accoundNotLocked = accoundNotLocked;
    }

    @Override
    @Column(name = "credentials_not_expired")
    public boolean isCredentialsNotExpired() {
        return credentialsNotExpired;
    }

    @Override
    public void setCredentialsNotExpired(boolean credentialsNotExpired) {
        this.credentialsNotExpired = credentialsNotExpired;
    }


    @Override
    @Column(name = "enabled")
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SCYUserDetails)) return false;

        SCYUserDetails that = (SCYUserDetails) o;

        if (!password.equals(that.password)) return false;
        if (!username.equals(that.username)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = username.hashCode();
        result = 31 * result + password.hashCode();
        return result;
    }


    @Override
    @Transient
    public SCYGrantedAuthority[] getAuthorities() {
        // Used by Acegi Security. This implements the required method from
        // Acegi Security. This implementation does not obtain the values
        // directly from the data store.
        return this.getGrantedAuthorities().toArray(new SCYGrantedAuthority[0]);
    }

    @Override
    @SuppressWarnings("unchecked")
    public synchronized void setAuthorities(SCYGrantedAuthority[] authorities) {
        this.setGrantedAuthorities(new HashSet(Arrays.asList(authorities)));
    }

    // EJB3 spec annotations require the use of a java <code>Collection</code>.
    // However, Acegi Security deals with an array. There are internal methods
    // to convert to and from the different data structures.
    @ManyToMany(targetEntity = eu.scy.core.model.impl.SCYGrantedAuthorityImpl.class, fetch = FetchType.EAGER)
    @JoinTable(name = "user_details_related_to_roles", joinColumns = {@JoinColumn(name = "user_details_fk", nullable = false)}, inverseJoinColumns = @JoinColumn(name = "granted_authorities_fk", nullable = false))
    
    public Set<SCYGrantedAuthority> getGrantedAuthorities() {
        /* Used only for persistence */
        return this.grantedAuthorities;
    }

    @Override
    @SuppressWarnings("unused")
    public synchronized void setGrantedAuthorities(Set<SCYGrantedAuthority> grantedAuthorities) {
        this.grantedAuthorities = grantedAuthorities;
    }

    @Override
    public synchronized void addAuthority(SCYGrantedAuthority authority) {
        if (this.grantedAuthorities == null)
            this.grantedAuthorities = new HashSet<SCYGrantedAuthority>();
        this.grantedAuthorities.add(authority);
    }

	@Override
    public boolean hasGrantedAuthority(String authority) {
		for (SCYGrantedAuthority grantedAuthority : this.grantedAuthorities) {
			if (grantedAuthority.getAuthority().equals(authority)) {
				return true;
			}
		}
		return false;
	}


    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }
}
