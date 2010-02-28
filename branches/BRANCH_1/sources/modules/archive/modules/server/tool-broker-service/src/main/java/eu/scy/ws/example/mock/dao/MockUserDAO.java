package eu.scy.ws.example.mock.dao;

import eu.scy.ws.example.mock.api.User;

import java.util.HashMap;

/**
 * Created: 10.feb.2009 10:54:44
 *
 * @author Bjørge Næss
 */
public class MockUserDAO {
	private HashMap<Integer, User> mockUsers;

	public MockUserDAO() {
		mockUsers = new HashMap<Integer, User>();
		mockUsers.put(0, new User(0, "scy", "scytastic", "Scy Student"));
		mockUsers.put(1, new User(1, "scywalker", "scybastic", "Scy Scywalker"));
		mockUsers.put(2, new User(2, "luke", "scywalker", "Luke Shoewalker"));
		mockUsers.put(3, new User(3, "scyzophrenic", "scinsane", "Peter Pan"));
		mockUsers.put(3, new User(3, "scy", "scy", "Scy global user"));
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

	public void saveUser(User user) {
		mockUsers.put(user.getId(), user);
	}

	public void deleteUser(User user) {
		mockUsers.remove(user.getId());
	}
}
