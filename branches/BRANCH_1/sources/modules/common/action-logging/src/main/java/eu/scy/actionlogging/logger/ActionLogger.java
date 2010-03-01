package eu.scy.actionlogging.logger;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Message;

import eu.scy.actionlogging.api.IAction;
import eu.scy.actionlogging.api.IActionLogger;
import eu.scy.actionlogging.packetextension.ActionLoggingExtension;

public class ActionLogger /*extends ScyBaseDAOHibernate */ implements IActionLogger {
    
	private XMPPConnection connection;
    
    /**
     * simple constructor for an actionlogger
     * @param user	user throwing actions (NOT! the tool)
     */
    public ActionLogger() {
    }
    
    /**
     * logs an action
     * @param tool	the tool throwing the action
     * @param action IAction thrown
     */
    public void log(String username, String tool, IAction action) {
    	Message packet = new Message();

        //ButterCode
        //FIXME: Remove hardcoded user names!!
        packet.setFrom("obama@descartes.inf.uni-due.de");            
        packet.setTo("scyhub.descartes.inf.uni-due.de");
    	
    	packet.addExtension(new ActionLoggingExtension(action));
    	connection.sendPacket(packet);
    }

	public void init(XMPPConnection connection) {
		this.connection = connection;
	}
    
}
