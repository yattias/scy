package eu.scy.ws.example.mock.dao;

import eu.scy.ws.example.api.ELO;
import eu.scy.ws.example.api.dao.ELODAO;
import eu.scy.ws.example.mock.api.GeoImageCollector;
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
		add(new MockELO(0, "MockELO zero", "I am emerging as the zeroth learning object"));
		add(new MockELO(1, "MockELO one", "I am emerging as the first learning object"));
		add(new MockELO(2, "MockELO two", "I am emerging as the second learning object"));

		MockELO imgElo = new MockELO(3, "I am an image collector ELO");

		ArrayList<String> dummyList = new ArrayList<String>();
		dummyList.add("http://scyzophrenia.ath.cx/bergen1.jpg");
		dummyList.add("http://scyzophrenia.ath.cx/bergen2.jpg");
		dummyList.add("http://scyzophrenia.ath.cx/bergen3.jpg");

		GeoImageCollector gic = new GeoImageCollector("Dummy imagelist", dummyList);
		GeoImageCollector gic2 = new GeoImageCollector("Dummy imagelist", dummyList);
		imgElo.setContent(gic);
		add(imgElo);

		ArrayList<GeoImageCollector> gclist = new ArrayList<GeoImageCollector>();
		gclist.add(gic);
		gclist.add(gic2);
		getELO(1).setGeoImageCollectors(gclist);

		add(new MockELO(4, "MockELO four", "I am emerging as the fourth learning object"));
		add(new MockELO(5, "MockELO five", "I am emerging as the fifth learning object"));
		add(new MockELO(6, "MockELO six", "I am emerging as the sixth learning object"));
		add(new MockELO(7, "MockELO seven", "I am emerging as the seventh learning object"));
		add(new MockELO(8, "MockELO eight", "I am emerging as the eighth learning object"));
		add(new MockELO(9, "MockELO nine", "I am emerging as the ninth learning object"));

		// Make some of the ELOS children of the others
		mockELOS.get(1).addChildELO(mockELOS.get(2));
		mockELOS.get(1).addChildELO(mockELOS.get(3));
		mockELOS.get(1).addChildELO(mockELOS.get(4));
		mockELOS.get(4).addChildELO(mockELOS.get(5));
		mockELOS.get(4).addChildELO(mockELOS.get(6));
		mockELOS.get(8).addChildELO(mockELOS.get(9));
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
	private void add(MockELO elo) {
		mockELOS.put(elo.getId(), (MockELO)elo);
	}
}
