package eu.scy.core.model;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 18.des.2009
 * Time: 06:18:38
 * To change this template use File | Settings | File Templates.
 */
public interface UserDetails {
    String getUsername();

    void setUsername(String username);

    String getPassword();

    void setPassword(String password);

    boolean isAccountNotExpired();

    void setAccountNotExpired(boolean accountNotExpired);

    boolean isAccoundNotLocked();

    void setAccoundNotLocked(boolean accoundNotLocked);

    boolean isCredentialsNotExpired();

    void setCredentialsNotExpired(boolean credentialsNotExpired);

    boolean isEnabled();

    void setEnabled(boolean enabled);

    SCYGrantedAuthority[] getAuthorities();

    void setAuthorities(SCYGrantedAuthority[] authorities);

    Set<SCYGrantedAuthority> getGrantedAuthorities();

    void setGrantedAuthorities(Set<SCYGrantedAuthority> grantedAuthorities);

    void addAuthority(SCYGrantedAuthority authority);

    boolean hasGrantedAuthority(String authority);

    String getLocale();
    void setLocale(String locale);
}
