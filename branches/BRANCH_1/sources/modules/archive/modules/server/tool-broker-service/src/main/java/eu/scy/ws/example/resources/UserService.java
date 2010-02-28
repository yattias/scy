package eu.scy.ws.example.resources;

import com.sun.jersey.spi.resource.Singleton;
import eu.scy.ws.example.mock.api.User;
import eu.scy.ws.example.mock.dao.MockUserDAO;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 * Created: 10.feb.2009 10:33:29
 *
 * @author Bjørge Næss
 */

// Set as singleton to keep the same instance of the eloDAO between requests
@Singleton
@Path("/users")
public class UserService {
	private MockUserDAO userDAO = new MockUserDAO();
	private User loggedInUser;

	/**
	 *
	 * @return Help text
	 */
	@GET
    @Produces({"text/plain"})
	public String getAll() {
		return "Usage: GET /users/<userid>";
	}


	/**
	 * This method is called when the relative url /elo/<someid> is requested.
	 * @param username username of user
	 * @param password password of user
	 * @return The ELO in the format specified by the clients request header "Accept" (one of the types defined by the @Produces annotation)
	 */

	@GET
	@Path("login/{username}/{password}")
	public Response login(@PathParam("username") String username, @PathParam("password") String password) {
		System.out.println("Logging in user with username " + username + " and password "+ password);
		User user = userDAO.getUser(username, password);

		System.out.println("user = " + user);

		if (user == null)
			return Response.status(Response.Status.FORBIDDEN).build();
		else {
			loggedInUser = user;
		}
		return Response.ok("Successfully logged in").build();
	}
	@GET
	@Path("{id}")
    @Produces({"application/xml", "application/json"})
	public User getUser(@PathParam("id") Integer id) {
		if (loggedInUser != null)  return userDAO.getUser(id);
		return null;

	}
}
