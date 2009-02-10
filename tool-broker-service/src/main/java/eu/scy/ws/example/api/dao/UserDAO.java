package eu.scy.ws.example.api.dao;

import eu.scy.ws.example.api.User;
import eu.scy.ws.example.mock.api.MockUser;

/**
 * Created: 10.feb.2009 10:40:10
 *
 * @author Bjørge Næss
 */
public interface UserDAO {
	public User getUser(Integer id);
	public User getUser(String username, String password);
	public void saveUser(MockUser user);
	public void deleteUser(MockUser user);
}
