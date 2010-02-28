package eu.scy.ws.example.mock.api;

/**
 * Created: 10.feb.2009 10:41:33
 *
 * @author Bjørge Næss
 */

import javax.ws.rs.Produces;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlRootElement;

@Produces("application/json")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "user")
public class User {

	private Integer id;
	private transient String username;
	private transient String password;
	private String name;

	public User() {}

	public User(Integer id, String username, String password, String name) {
		this.id=id;
		this.username=username;
		this.password=password;
		this.name = name;
	}

    public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
