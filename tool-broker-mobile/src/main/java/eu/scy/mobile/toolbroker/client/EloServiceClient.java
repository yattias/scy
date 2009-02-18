package eu.scy.mobile.toolbroker.client;

import eu.scy.mobile.toolbroker.model.ELO;
import eu.scy.mobile.toolbroker.serializers.JSONSerializer;
import eu.scy.mobile.toolbroker.serializers.SerializerNotFoundException;
import eu.scy.mobile.toolbroker.serializers.Serializers;
import org.json.me.JSONObject;

/**
 * Created: 11.feb.2009 12:21:06
 *
 * @author Bjørge Næss
 */
public class EloServiceClient extends ServiceClient {
	public EloServiceClient(String baseUrl) {
		super(baseUrl);
	}
	public ELO getELO(int i) {
		try{
			System.out.println("Looking for ELO with ID = " + i);
			JSONSerializer serializer = Serializers.get("elo");
			return (ELO) serializer.deserialize(getJSON("/elos/"+i));
		}
		catch (SerializerNotFoundException e) {
			System.err.println("ERROR:" +e.getMessage());
		}
		return null;
	}
	public void updateELO(ELO elo) {
		try{
			JSONSerializer serializer = Serializers.get("elo");
			JSONObject jsonObj = serializer.serialize(elo);
			postJSON("/elos/"+elo.getId(), jsonObj);
		}
		catch (SerializerNotFoundException e) {
			System.err.println("ERROR:" +e.getMessage());
		}
	}
	/*public void saveELO(ELO elo) {
		try{
			JSONSerializer serializer = Serializers.get("elo");
			JSONObject jsonObj = serializer.serialize(elo);
			postJSON("/elos/"+elo.getId(), jsonObj);
		}
		catch (SerializerNotFoundException e) {
			System.err.println("ERROR:" +e.getMessage());
		}
	}*/
}
