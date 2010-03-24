package eu.scy.roolows;

import javax.ws.rs.Produces;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Sven
 */

 @Produces("application/json")
 @XmlAccessorType(XmlAccessType.FIELD)
 @XmlRootElement(name="user")
public class User {

    private Integer id;
    private transient String username;
    private transient String password;
    private String name;


   public User(Integer id, String username, String password, String name) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
    }

     public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    

      public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }




}
