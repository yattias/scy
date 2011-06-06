package eu.scy.mobile.toolbroker.client;

import eu.scy.mobile.toolbroker.model.IELO;
import eu.scy.mobile.toolbroker.IELOService;
import eu.scy.mobile.toolbroker.serializers.Serializers;
import eu.scy.mobile.toolbroker.serializer.Serializer;
import org.json.me.JSONObject;

/**
 * Created: 11.feb.2009 12:21:06
 *
 * @author Bjørge Næss
 */
public class EloServiceClient implements IELOService {
    private final static String REMOTE_TYPE = "eu.scy.ws.example.mock.api.ELO";
    private JSONServiceClient client;

    public EloServiceClient(String baseUrl) {
        client = new JSONServiceClient(baseUrl);
	}
	public IELO getELO(int i) {
    	// System.out.println("Looking for ELO with ID = " + i);
        Serializer serializer = Serializers.getByRemoteType(REMOTE_TYPE);

        if (serializer == null) System.err.println("Warning:  no serializer found for type "+ REMOTE_TYPE);
        else return (IELO) serializer.deserialize(client.getJSON("/elos/"+i));

        return null;
	}
	public void updateELO(IELO elo) {
		Serializer serializer = Serializers.getByRemoteType(REMOTE_TYPE);

        if (serializer == null) System.err.println("Warning:  no serializer found for type "+ REMOTE_TYPE);
        else {
            JSONObject jsonObj = (JSONObject) serializer.serialize(elo);
            client.postJSON("/elos/"+elo.getId(), jsonObj);
        }
	}
	/*public void saveELO(ELO elo) {
		try{
			JSONSerializer serializer = Serializers.get("elo");
			JSONObject jsonObj = serializer.serializeKey(elo);
			postJSON("/elos/"+elo.getId(), jsonObj);
		}
		catch (SerializerNotFoundException e) {
			System.err.println("ERROR:" +e.getMessage());
		}
	}*/
}
