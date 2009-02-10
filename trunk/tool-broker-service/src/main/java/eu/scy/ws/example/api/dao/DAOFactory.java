package eu.scy.ws.example.api.dao;

import eu.scy.ws.example.mock.dao.DAOFactoryMock;

/**
 * Created: 10.feb.2009 10:38:51
 *
 * @author Bjørge Næss
 */
public abstract class DAOFactory {
	protected DAOFactory() {}

	private static DAOFactory instance;

	public static DAOFactory getInstance() {
		if (instance == null)
			instance = new DAOFactoryMock();
		return instance;
	}
	public abstract ELODAO getELODAO();
	public abstract UserDAO getUserDAO();
}
