package eu.scy.ws.example.mock;

import eu.scy.ws.example.mock.api.ELO;

import javax.ws.rs.*;
import javax.activation.DataSource;

import com.sun.jersey.spi.resource.Singleton;
import org.codehaus.jettison.json.JSONString;
import net.sf.json.JSONObject;

/**
 * Created by IntelliJ IDEA.
 * User: Bjørge Næss
 * Date: 23.feb.2009
 * Time: 12:08:50
 * To change this template use File | Settings | File Templates.
 */
public interface MockELOService {

	@GET
    @Produces({"text/plain"})
    String getAll();

    @GET
	@Path("{eloId}")
    @Produces({"application/json"})
    String getElo(@PathParam("eloId") Integer eloId);

	@POST
	@Path("{eloId}")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    String postElo(@PathParam("eloId") Integer eloId, String elo);

	@GET
	@Path("/image/{filename}")
    @Produces({"image/jpeg"})
    DataSource getImage(@PathParam("filename") String filename);
}
