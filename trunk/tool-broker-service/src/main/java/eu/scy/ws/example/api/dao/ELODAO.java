package eu.scy.ws.example.api.dao;
import eu.scy.ws.example.api.ELO;

import java.util.List;

/**
 * Created: 10.feb.2009 10:37:05
 *
 * @author Bjørge Næss
 */
public interface ELODAO {
	public ELO getELO(Integer id);
	public void saveELO(ELO elo);
	public void deleteELO(ELO elo);
	public List<ELO> getAll();
}
