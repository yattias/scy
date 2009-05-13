package eu.scy.client.tools.scysimulator;

import sqv.data.DataAgent;
import sqv.data.DataServer;
import sqv.data.IDataClient;


/**
 * @author Lars Bollen
 *
 */
public final class SCYDataAgent extends DataAgent 
{	
	public SCYDataAgent(IDataClient dataClient, DataServer dataServer)
	{
		super(dataClient, dataServer);
		//System.out.println("SCYDataAgent() called.");
	}
	
	public void register()
	{
		//System.out.println("SCYDataAgent.register() called.");
		if (m_dataServer != null)
		{
			m_dataServer.register(this);
		}
	}
	
	public void release() {
		super.release();
		//System.out.println("SCYDataAgent.register() called.");
	}
	
	public void setServer(DataServer dataServer) {
		super.setServer(dataServer);
		//System.out.println("SCYDataAgent.setServer() called.");
	}
	
	/** 
	 * The BasicDataAgent does not implement a local buffer.
	 * It retrieves the values directly from the DataServer
	 * 
	 * @see sqv.data.IDataAgentClient#getData()
	 */
	public final boolean getData() 
	{
		//System.out.println("SCYDataAgent.getData() called.");
		if (m_dataServer != null)
		{
			//put the data in the list of modelVariables
			boolean retval = m_dataServer.getData(m_clientData); 
			
			if (retval == true)
			{
				m_hasData = false;
			}
			
			return retval;
		}
		else
		{
			return false;
		}
	}

	/**
	 * @see sqv.data.IDataAgentClient#setData()
	 */
	public final boolean setData() 
	{
		//System.out.println("SCYDataAgent.setData() called.");
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
	public final boolean skipData(int number) 
	{
		//System.out.println("SCYDataAgent.skipData() called.");
		// TODO Auto-generated method stub
		return false;
	}

	public void unregister() {
		if (m_dataServer != null)
		{
			m_dataServer.unregister(this);
		}
		
	}

}
