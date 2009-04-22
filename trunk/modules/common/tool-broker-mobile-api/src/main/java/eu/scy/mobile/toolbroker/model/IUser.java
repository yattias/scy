package eu.scy.mobile.toolbroker.model;

/**
 * Created by IntelliJ IDEA.
 * User: Bjørge Næss
 * Date: 02.mar.2009
 * Time: 11:08:44
 * To change this template use File | Settings | File Templates.
 */
public interface IUser {
    Integer getId();

    void setId(Integer id);

    String getUsername();

    void setUsername(String username);

    String getPassword();

    void setPassword(String password);

    String getName();

    void setName(String name);
}
