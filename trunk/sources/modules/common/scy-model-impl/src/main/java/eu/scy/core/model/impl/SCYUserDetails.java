package eu.scy.core.model.impl;

import eu.scy.core.model.UserDetails;

import javax.persistence.*;


/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 03.mar.2009
 * Time: 12:23:59
 * Overridden details class in order to add SCY specific user fields
 */
@Entity(name = "eu.scy.core.model.impl.SCYUserDetails")
@Table(name="user_details")
public class SCYUserDetails implements UserDetails/*extends StudentUserDetails */{

    private Long id;

    private String username;
    private String password;
    private boolean accountNotExpired = Boolean.TRUE;
    private boolean accoundNotLocked = Boolean.TRUE;
    private boolean credentialsNotExpired = Boolean.TRUE;
    private boolean enabled = Boolean.TRUE;



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
    @Column(name="account_not_expired")
    public boolean isAccountNotExpired() {
        return accountNotExpired;
    }

    @Override
    public void setAccountNotExpired(boolean accountNotExpired) {
        this.accountNotExpired = accountNotExpired;
    }

    @Override
    @Column (name="account_not_locked")
    public boolean isAccoundNotLocked() {
        return accoundNotLocked;
    }

    @Override
    public void setAccoundNotLocked(boolean accoundNotLocked) {
        this.accoundNotLocked = accoundNotLocked;
    }

    @Override
    @Column (name="credentials_not_expired")
    public boolean isCredentialsNotExpired() {
        return credentialsNotExpired;
    }

    @Override
    public void setCredentialsNotExpired(boolean credentialsNotExpired) {
        this.credentialsNotExpired = credentialsNotExpired;
    }


    @Override
    @Column (name="enabled")
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
}
