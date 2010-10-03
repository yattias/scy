package eu.scy.core.model;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 11.jan.2010
 * Time: 21:23:38
 * To change this template use File | Settings | File Templates.
 */
public interface SCYGrantedAuthority {

    Long getId();

    void setId(Long id);

    String getAuthority();

    void setAuthority(String authority);
}
