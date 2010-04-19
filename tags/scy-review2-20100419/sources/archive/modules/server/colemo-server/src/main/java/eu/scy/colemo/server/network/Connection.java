/*
 * Created on 05.okt.2004
 *
 * 
 */
package eu.scy.colemo.server.network;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

//import eu.scy.colemo.contributions.AddClass;
import eu.scy.colemo.network.LogOn;
import eu.scy.colemo.network.NetworkMessage;

/**
 * @author Øystein
 */
public class Connection extends Thread {

    private Logger log = Logger.getLogger(Connection.class);

    private Socket socket;
    private ObjectInputStream objectIn;
    private ObjectOutputStream objectOut;
    private Receiver receiver;

    public Connection(Socket socket, Receiver receiver) {
        log.info("Created new connection with receiver: " + receiver.getClass().getName());
        this.socket = socket;
        this.receiver = receiver;

        try {
            objectOut = new ObjectOutputStream(socket.getOutputStream());
            objectIn = new ObjectInputStream(socket.getInputStream());

            //start();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void run() {
        log.info("Receiver is " + receiver.getClass().getName());
        for (; ;) {

            try {
                receiver.receive(objectIn.readObject());
            } catch (IOException e) {
                receiver.receive(new ClientDisconnected(this));
                log.info(e.getClass().getName() + "::: " + e.getMessage());
                this.close();
            } catch (Exception e) {
                receiver.receive(new ClientDisconnected(this));
                log.info(e.getClass().getName() + "::: " + e.getMessage());
                //e.printStackTrace();
                this.close();
            }

        }
    }

    synchronized public void send(Object o) {
        try {
            log.debug("Sending: " + o);
            objectOut.writeObject(o);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {
            objectOut.flush();

            objectOut.reset();
        } catch (Exception e) {
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
        try {
            log.debug("Logging on + reading object....");
            Object o = objectIn.readObject();
            log.debug("O is: " + o.getClass().getName());


            if (o instanceof NetworkMessage) {
                NetworkMessage message = (NetworkMessage) o;
                if (message.getObject() instanceof LogOn) {
                    return (LogOn) message.getObject();
                }
            } /*else if (o.getClass().getName().equals(AddClass.class.getName())) { // added now
                log.info("Was add class");
                AddClass add = (AddClass) o;
                send(add);

            } else {
                log.info("Did not find type: " + o.getClass().getName());
                log.info("Is not: " + AddClass.class.getName());
            }   */
        }
        catch (Exception e) {
            e.printStackTrace();

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
