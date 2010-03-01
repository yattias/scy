package eu.scy.mobile.toolbroker.model.impl;

import eu.scy.mobile.toolbroker.model.IUser;

/**
 * Created: 13.feb.2009 12:03:38
 *
 * @author Bjørge Næss
 */
public class User implements IUser {
	private Integer id;
	private String username;
	private String password;
	private String name;

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
