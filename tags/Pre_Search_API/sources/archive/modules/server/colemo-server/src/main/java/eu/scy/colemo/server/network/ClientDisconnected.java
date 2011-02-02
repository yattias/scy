/*
 * Created on 25.okt.2004
 *
 * 
 */
package eu.scy.colemo.server.network;

import java.io.Serializable;

/**
 * @author Øystein
 *
 * 
 */
public class ClientDisconnected implements Serializable{

	private Connection connection;
	
	public ClientDisconnected(Connection connection) {
		this.connection=connection;
	}

	/**
	 * @return Returns the connection.
	 */
	public Connection getConnection() {
		return connection;
	}
}
