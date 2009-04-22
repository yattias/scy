/*
 * Created on 05.okt.2004
 *
 * 
 */
package eu.scy.colemo.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @author Øystein
 *
 *
 */
public class Connection extends Thread{
	
	private Socket socket;
	private ObjectInputStream objectIn;
	private ObjectOutputStream objectOut;
	private Receiver receiver;
	
	public Connection(Socket socket,Receiver receiver) {
		
		this.socket=socket;
		this.receiver=receiver;
				
		try {
			objectOut = new ObjectOutputStream(socket.getOutputStream());
			objectIn = new ObjectInputStream(socket.getInputStream());
			
			//start();
		} 
		catch (IOException e) {
			e.printStackTrace();
			System.out.println("Exception in Connection line 40: "+e);

		} 
				
	}
	public void run() {
		for(;;) {
			try {
				receiver.receive(objectIn.readObject());
			} catch (IOException e) {
				receiver.receive(new ClientDisconnected(this));
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			
		}
	}

	synchronized public void send(Object o) {
		try {
			System.out.println("Sending: "+o);
			objectOut.writeObject(o);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			objectOut.flush();
		
			objectOut.reset();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	/**
	 * @return Returns the socket.
	 */
	public Socket getSocket() {
		return socket;
	}
	/**
	 * @return Returns the receiver.
	 */
	public Receiver getReceiver() {
		return receiver;
	}
	public LogOn getLogOn() {
		try{
			Object o = objectIn.readObject();
			
			if (o instanceof NetworkMessage) {
				NetworkMessage message = (NetworkMessage)o;
				if(message.getObject() instanceof LogOn) {
					return (LogOn)message.getObject();
				}
			}
		}
		catch(Exception e) {
			
		}
			
		
		return getLogOn();
	}
	public void close() {
	    stop();
	    
		try {
			socket.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
}
