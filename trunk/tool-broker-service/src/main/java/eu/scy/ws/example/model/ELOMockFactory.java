package eu.scy.ws.example.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created: 05.feb.2009 13:43:06
 * Just produce some mock-ELOS 
 * @author Bjørge Næss
 */
public class ELOMockFactory {
	private HashMap<Integer,ELO> mockELOS;
	public ELOMockFactory() {
		mockELOS = new HashMap<Integer, ELO>();
		mockELOS.put(0, new ELO("0", "ELO zero", "I am emerging as the zeroth learning object"));
		mockELOS.put(1, new ELO("1", "ELO one", "I am emerging as the first learning object"));
		mockELOS.put(2, new ELO("2", "ELO two", "I am emerging as the second learning object"));
		mockELOS.put(3, new ELO("3", "ELO three", "I am emerging as the third learning object"));
		mockELOS.put(4, new ELO("4", "ELO four", "I am emerging as the fourth learning object"));
		mockELOS.put(5, new ELO("5", "ELO five", "I am emerging as the fifth learning object"));
		mockELOS.put(6, new ELO("6", "ELO six", "I am emerging as the sixth learning object"));
		mockELOS.put(7, new ELO("7", "ELO seven", "I am emerging as the seventh learning object"));
		mockELOS.put(8, new ELO("8", "ELO eight", "I am emerging as the eighth learning object"));
		mockELOS.put(9, new ELO("9", "ELO nine", "I am emerging as the ninth learning object"));

		// Make some of the ELOS children of the others
		mockELOS.get(1).addChildELO(mockELOS.get(2));
		mockELOS.get(1).addChildELO(mockELOS.get(3));
		mockELOS.get(1).addChildELO(mockELOS.get(4));
		mockELOS.get(4).addChildELO(mockELOS.get(5));
		mockELOS.get(4).addChildELO(mockELOS.get(6));
	}

	public ELO getELO(Integer id) {
		return mockELOS.get(id);
	}
	public void saveELO(Integer id, ELO elo) {
		mockELOS.put(id, elo);
	}
	public void deleteELO(Integer id) {
		mockELOS.remove(id);
	}

	public List<ELO> getAll() {
		return new ArrayList<ELO>(mockELOS.values());
	}
}
