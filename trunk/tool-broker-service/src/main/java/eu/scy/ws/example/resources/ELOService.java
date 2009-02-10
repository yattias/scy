package eu.scy.ws.example.resources;

import com.sun.jersey.spi.resource.Singleton;
import eu.scy.ws.example.api.dao.DAOFactory;
import eu.scy.ws.example.api.dao.ELODAO;
import eu.scy.ws.example.api.ELO;

import javax.ws.rs.*;

/**
 * This is a Webservice that exposes ELOS to the world the RESTful-way.
 * The client decides on what format to send and recieve the data by setting
 * the Accept HTTP header
 *
 * Created: 04.feb.2009 16:26:48
 * Example implementation of a RESTful WS
 * @author Bjørge Næss
 */

// Set as singleton to keep the same instance of the eloDAO between requests
@Singleton
@Path("/elos")
public class ELOService {

	private ELODAO eloDAO = DAOFactory.getInstance().getELODAO();

	/**
	 *
	 * @return Help text
	 */
	@GET
    @Produces({"text/plain"})
	public String getAll() {
		return "Usage: GET /elos/<eloid>";
	}

	/**
	 * This method is called when the relative url /elo/<someid> is requested.
	 * @param eloId Id of elo to retrieve
	 * @return The ELO in the format specified by the clients request header "Accept" (one of the types defined by the @Produces annotation)
	 */
	
	@GET
	@Path("{eloId}")
    @Produces({"application/xml", "application/json"})
	public ELO getElo(@PathParam("eloId") Integer eloId) {
		System.out.println("Someone requested MockELO with ID " + eloId);
		ELO elo = eloDAO.getELO(eloId);
		System.out.println("elo = " + elo);
		return elo;
	}

	/**
	 * This method is called when a POST request is sendt to the relative url /elo/<someid>
	 * The ELO can be sendt in either JSON or XML format
	 * @param eloId The ID of the ELO to update
	 * @param elo The ELO object
	 * @return Some dummy text
	 */
	@POST
    @Consumes({"application/xml", "application/json"})
    @Produces({"text/plain"})
	@Path("{eloId}")
	public synchronized String postElo(@PathParam("eloId") Integer eloId, ELO elo) {
		eloDAO.saveELO(elo);
		return "Elo updated";
	}
}
