package eu.scy.ws.example.resources;

import com.sun.jersey.spi.resource.Singleton;
import eu.scy.ws.example.mock.MockELOService;
import eu.scy.ws.example.mock.dao.MockELODAO;
import eu.scy.ws.example.mock.api.ELO;

import javax.ws.rs.*;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.jws.WebService;
import java.net.URL;

import flexjson.JSONSerializer;
import flexjson.JSONDeserializer;

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
@WebService(
        endpointInterface = "eu.scy.ws.example.api.MockELOService"
)
@Path("/elos")
@Singleton
public class ELOServiceImpl implements MockELOService {

	private MockELODAO eloDAO = new MockELODAO();

	public String getAll() {
		return "Usage: GET /elos/<eloid>";
	}
	/**
	 * This method is called when the relative url /elo/<someid> is requested.
	 * @param eloId Id of elo to retrieve
	 * @return The ELO in the format specified by the clients request header "Accept" (one of the types defined by the @Produces annotation)
	 */
	public String getElo(@PathParam("eloId") Integer eloId) {
		System.out.println("Someone requested ELO with ID " + eloId);
		ELO elo = eloDAO.getELO(eloId);

        JSONSerializer s = new JSONSerializer();
        return s.deepSerialize(elo);

        //return "";//builder.create().toJson(elo);
	}
	/**
	 * This method is called when a POST request is sendt to the relative url /elo/<someid>
	 * The ELO can be sendt in either JSON or XML format
	 * @param eloId The ID of the ELO to update
	 * @param json The ELO object
	 * @return Some dummy text
	 */
	public String postElo(@PathParam("eloId") Integer eloId, String json) {
        System.out.println("Got json-string: " + json);

        JSONDeserializer ds = new JSONDeserializer();

        ELO elo = (ELO) ds.deserialize(json);

        System.out.println("elo = " + elo);
		eloDAO.saveELO(elo);
		return "{'response': {'Elo updated'}";
		/**/
	}
    public DataSource getImage(@PathParam("filename") String filename) {
        System.out.println("filename = " + filename);
        URL jpgURL = MockELODAO.class.getResource(filename);
        return new FileDataSource(jpgURL.getFile());
    }
}
