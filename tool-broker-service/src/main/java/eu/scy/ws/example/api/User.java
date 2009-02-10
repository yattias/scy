package eu.scy.ws.example.api;

/**
 * Created: 10.feb.2009 10:52:14
 *
 * @author Bjørge Næss
 */
public interface User {
	Integer getId();

	String getUsername();

	String getPassword();

	String getName();

	void setId(Integer id);

	void setUsername(String username);

	void setPassword(String password);
}