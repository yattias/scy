package eu.scy.ws.example.mock.dao;

import eu.scy.ws.example.api.User;
import eu.scy.ws.example.api.dao.UserDAO;
import eu.scy.ws.example.mock.api.MockUser;

import java.util.HashMap;

/**
 * Created: 10.feb.2009 10:54:44
 *
 * @author Bjørge Næss
 */
public class MockUserDAO implements UserDAO {
	private HashMap<Integer, MockUser> mockUsers;

	public MockUserDAO() {
		mockUsers = new HashMap<Integer, MockUser>();
		mockUsers.put(0, new MockUser(0, "scy", "scytastic", "Scy Student"));
		mockUsers.put(1, new MockUser(1, "scywalker", "scybastic", "Scy Scywalker"));
		mockUsers.put(2, new MockUser(2, "luke", "scywalker", "Luke Shoewalker"));
		mockUsers.put(3, new MockUser(3, "scyzophrenic", "scinsane", "Peter Pan"));
		mockUsers.put(3, new MockUser(3, "scy", "scy", "Scy global user"));
	}
	public User getUser(Integer id) {
		return mockUsers.get(id);
	}

	public User getUser(String username, String password) {
		for (User u : mockUsers.values()) {
			if (u.getPassword().equals(password) && u.getUsername().equals(username)) return u;
		}
		return null;
	}

	public void saveUser(MockUser user) {
		mockUsers.put(user.getId(), user);
	}

	public void deleteUser(MockUser user) {
		mockUsers.remove(user.getId());
	}
}
