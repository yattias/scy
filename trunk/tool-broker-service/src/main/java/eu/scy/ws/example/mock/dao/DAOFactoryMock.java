package eu.scy.ws.example.mock.dao;

import eu.scy.ws.example.api.dao.DAOFactory;
import eu.scy.ws.example.api.dao.ELODAO;
import eu.scy.ws.example.api.dao.UserDAO;

/**
 * Created: 05.feb.2009 13:43:06
 * Just produce some mock-ELOS
 * @author Bjørge Næss
 */
public class DAOFactoryMock extends DAOFactory {

	@Override
	public ELODAO getELODAO() {
		return new MockELODAO();
	}

	@Override
	public UserDAO getUserDAO() {
		return new MockUserDAO();
	}
}
