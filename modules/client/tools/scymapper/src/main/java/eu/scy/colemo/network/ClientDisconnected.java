/*
 * Created on 25.okt.2004
 *
 * 
 */
package eu.scy.colemo.network;

import java.io.Serializable;

/**
 * @author �ystein
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
