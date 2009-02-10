package eu.scy.ws.example.mock.dao;

import eu.scy.ws.example.api.dao.ELODAO;
import eu.scy.ws.example.api.ELO;
import eu.scy.ws.example.mock.api.MockELO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created: 10.feb.2009 10:50:57
 *
 * @author Bjørge Næss
 */
public class MockELODAO implements ELODAO {
	private HashMap<Integer, MockELO> mockELOS;
	public MockELODAO() {
		mockELOS = new HashMap<Integer, MockELO>();
		mockELOS.put(0, new MockELO(0, "MockELO zero", "I am emerging as the zeroth learning object"));
		mockELOS.put(1, new MockELO(1, "MockELO one", "I am emerging as the first learning object"));
		mockELOS.put(2, new MockELO(2, "MockELO two", "I am emerging as the second learning object"));
		mockELOS.put(3, new MockELO(3, "MockELO three", "I am emerging as the third learning object"));
		mockELOS.put(4, new MockELO(4, "MockELO four", "I am emerging as the fourth learning object"));
		mockELOS.put(5, new MockELO(5, "MockELO five", "I am emerging as the fifth learning object"));
		mockELOS.put(6, new MockELO(6, "MockELO six", "I am emerging as the sixth learning object"));
		mockELOS.put(7, new MockELO(7, "MockELO seven", "I am emerging as the seventh learning object"));
		mockELOS.put(8, new MockELO(8, "MockELO eight", "I am emerging as the eighth learning object"));
		mockELOS.put(9, new MockELO(9, "MockELO nine", "I am emerging as the ninth learning object"));

		// Make some of the ELOS children of the others
		mockELOS.get(1).addChildELO(mockELOS.get(2));
		mockELOS.get(1).addChildELO(mockELOS.get(3));
		mockELOS.get(1).addChildELO(mockELOS.get(4));
		mockELOS.get(4).addChildELO(mockELOS.get(5));
		mockELOS.get(4).addChildELO(mockELOS.get(6));
	}
	public MockELO getELO(Integer id) {
		return mockELOS.get(id);
	}

	public void saveELO(ELO elo) {
		mockELOS.put(elo.getId(), (MockELO)elo);
	}

	public void deleteELO(ELO elo) {
		mockELOS.remove(elo.getId());

	}
	public List<ELO> getAll() {
		return new ArrayList<ELO>(mockELOS.values());
	}
}
