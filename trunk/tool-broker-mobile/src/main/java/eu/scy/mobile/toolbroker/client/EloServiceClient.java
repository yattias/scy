package eu.scy.mobile.toolbroker.client;

import eu.scy.mobile.toolbroker.model.ELO;
import eu.scy.mobile.toolbroker.serializers.JSONSerializer;
import eu.scy.mobile.toolbroker.serializers.Serializers;
import org.json.me.JSONObject;

/**
 * Created: 11.feb.2009 12:21:06
 *
 * @author Bjørge Næss
 */
public class EloServiceClient extends ServiceClient {
    private final static String REMOTE_TYPE = "eu.scy.ws.example.mock.api.ELO";

    public EloServiceClient(String baseUrl) {
		super(baseUrl);
	}
	public ELO getELO(int i) {
    	System.out.println("Looking for ELO with ID = " + i);
        JSONSerializer serializer = Serializers.getByRemoteType(REMOTE_TYPE);

        if (serializer == null) System.err.println("Warning:  no serializer found for type "+ REMOTE_TYPE);
        else return (ELO) serializer.deserialize(getJSON("/elos/"+i));

        return null;
	}
	public void updateELO(ELO elo) {
		JSONSerializer serializer = Serializers.getByRemoteType(REMOTE_TYPE);

        if (serializer == null) System.err.println("Warning:  no serializer found for type "+ REMOTE_TYPE);
        else {
            JSONObject jsonObj = serializer.serialize(elo);
            postJSON("/elos/"+elo.getId(), jsonObj);
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
