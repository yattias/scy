package eu.scy.ws.example.mock.dao;

import eu.scy.ws.example.mock.api.GeoImageCollector;
import eu.scy.ws.example.mock.api.ELO;
import eu.scy.ws.example.mock.api.ELOTextContent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created: 10.feb.2009 10:50:57
 *
 * @author Bjørge Næss
 */
public class MockELODAO {
	private HashMap<Integer, ELO> mockELOS;
	public MockELODAO() {
		mockELOS = new HashMap<Integer, ELO>();
		add(new ELO(0, "ELO zero", new ELOTextContent("I am emerging as the zeroth learning object")));
		add(new ELO(1, "ELO one", new ELOTextContent("I am emerging as the first learning object")));
		add(new ELO(2, "ELO two", new ELOTextContent("I am emerging as the second learning object")));

		ELO imgElo = new ELO(3, "I am an image collector ELO");
		add(imgElo);

		ArrayList<String> imgList1 = new ArrayList<String>();
		imgList1.add("http://localhost:9998/elos/image/bergen1.jpg");
		imgList1.add("http://localhost:9998/elos/image/bergen2.jpg");
		imgList1.add("http://localhost:9998/elos/image/bergen3.jpg");
		imgList1.add("http://localhost:9998/elos/image/bergen4.jpg");
		imgList1.add("http://localhost:9998/elos/image/bergen5.jpg");

		ArrayList<String> imgList2 = new ArrayList<String>();
		imgList2.add("http://localhost:9998/elos/image/bergen5.jpg");
		imgList2.add("http://localhost:9998/elos/image/bergen4.jpg");
		imgList2.add("http://localhost:9998/elos/image/bergen3.jpg");
		imgList2.add("http://localhost:9998/elos/image/bergen2.jpg");
		imgList2.add("http://localhost:9998/elos/image/bergen1.jpg");

		GeoImageCollector gic1 = new GeoImageCollector("Dummy Image Collector 1", imgList1);
		GeoImageCollector gic2 = new GeoImageCollector("Dummy Image Collector 2", imgList2);

		getELO(1).setContent(gic1);
		getELO(3).setContent(gic2);

		add(new ELO(4, "ELO four", new ELOTextContent("I am emerging as the fourth learning object")));
		add(new ELO(5, "ELO five", new ELOTextContent("I am emerging as the fifth learning object")));
		add(new ELO(6, "ELO six", new ELOTextContent("I am emerging as the sixth learning object")));
		add(new ELO(7, "ELO seven", new ELOTextContent("I am emerging as the seventh learning object")));
		add(new ELO(8, "ELO eight", new ELOTextContent("I am emerging as the eighth learning object")));
		add(new ELO(9, "ELO nine", new ELOTextContent("I am emerging as the ninth learning object")));

		// Make some of the ELOS children of the others
		mockELOS.get(1).addChildELO(mockELOS.get(2));
		mockELOS.get(1).addChildELO(mockELOS.get(3));
		mockELOS.get(1).addChildELO(mockELOS.get(4));
		mockELOS.get(4).addChildELO(mockELOS.get(5));
		mockELOS.get(4).addChildELO(mockELOS.get(6));
		mockELOS.get(8).addChildELO(mockELOS.get(9));
	}
	public ELO getELO(Integer id) {
		return mockELOS.get(id);
	}

	public void saveELO(ELO elo) {
		mockELOS.put(elo.getId(), (ELO)elo);
	}

	public void deleteELO(ELO elo) {
		mockELOS.remove(elo.getId());

	}
	public List<ELO> getAll() {
		return new ArrayList<ELO>(mockELOS.values());
	}
	private void add(ELO elo) {
		mockELOS.put(elo.getId(), (ELO)elo);
	}
}
