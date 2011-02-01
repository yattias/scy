package eu.scy.client.tools.scysimulator;

import sqv.data.DataAgent;
import sqv.data.DataServer;
import sqv.data.IDataClient;


/**
 * @author Lars Bollen
 *
 */
public final class SCYDataAgent extends DataAgent {	
	public SCYDataAgent(IDataClient dataClient, DataServer dataServer) {
		super(dataClient, dataServer);
	}
	
	public void register() {
		if (m_dataServer != null) {
			m_dataServer.register(this);
		}
	}
	
	public void release() {
		super.release();
	}
	
	public void setServer(DataServer dataServer) {
		super.setServer(dataServer);
	}
	
	/** 
	 * The BasicDataAgent does not implement a local buffer.
	 * It retrieves the values directly from the DataServer
	 * 
	 * @see sqv.data.IDataAgentClient#getData()
	 */
	public final boolean getData() {
		if (m_dataServer != null) {
			//put the data in the list of modelVariables
			boolean retval = m_dataServer.getData(m_clientData); 	
			if (retval == true) {
				m_hasData = false;
			}
			
			return retval;
		}
		else {
			return false;
		}
	}

	/**
	 * @see sqv.data.IDataAgentClient#setData()
	 */
	public final boolean setData() 
	{
		if (m_dataServer != null)
		{
			//push the data to the server
			return m_dataServer.setData(this, m_clientData); 
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * @see sqv.data.IDataAgentClient#skipData(int)
	 */
	public final boolean skipData(int number) {
		return false;
	}

	public void unregister() {
		if (m_dataServer != null) {
			m_dataServer.unregister(this);
		}
	}

}