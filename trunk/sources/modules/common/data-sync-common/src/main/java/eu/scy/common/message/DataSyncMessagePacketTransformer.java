/**
 * 
 */
package eu.scy.common.message;

import java.util.ArrayList;

import eu.scy.common.message.SyncMessage.Event;
import eu.scy.common.message.SyncMessage.Response;
import eu.scy.common.message.SyncMessage.Type;
import eu.scy.common.packetextension.SCYPacketTransformer;

/**
 * @author giemza
 *
 */
public class DataSyncMessagePacketTransformer extends SCYPacketTransformer {

	private static final String syncCommandPath = "/" + SyncMessage.PATH; 
	
	private SyncMessage pojo;
	
	public DataSyncMessagePacketTransformer() {
		super(SyncMessage.PATH);
	}
	
	public DataSyncMessagePacketTransformer(SyncMessage pojo) {
		super(SyncMessage.PATH, pojo);
	}
	
	
	/* (non-Javadoc)
	 * @see eu.scy.common.packetextension.SCYPacketTransformer#getObject()
	 */
	@Override
	public Object getObject() {
		return pojo;
	}

	/* (non-Javadoc)
	 * @see eu.scy.common.packetextension.SCYPacketTransformer#getValueForXPath(java.lang.String)
	 */
	@Override
	public String getValueForXPath(String path) {
		if(path.equals(syncCommandPath + "/" + "event")) {
			return (pojo.getEvent() != null ? pojo.getEvent().toString() : null);
		} else if(path.equals(syncCommandPath + "/" + "userId")) {
			return pojo.getUserId();
		} else if(path.equals(syncCommandPath + "/" + "toolId")) {
			return pojo.getToolId();
		} else if(path.equals(syncCommandPath + "/" + "type")) {
			return (pojo.getType() != null ? pojo.getType().toString() : null);
		} else if(path.equals(syncCommandPath + "/" + "response")) {
			return (pojo.getResponse() != null ? pojo.getResponse().toString() : null);
		} else if(path.equals(syncCommandPath + "/" + "message")) {
			return pojo.getMessage();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see eu.scy.common.packetextension.SCYPacketTransformer#getXPaths()
	 */
	@Override
	public String[] getXPaths() {
		if(pojo == null) {
			throw new IllegalStateException("Object must not be null before using the transformer!");
		}
		ArrayList<String> paths = new ArrayList<String>();
		paths.add(syncCommandPath + "/" + "event");
		paths.add(syncCommandPath + "/" + "userId");
		paths.add(syncCommandPath + "/" + "toolId");
		paths.add(syncCommandPath + "/" + "type");
		paths.add(syncCommandPath + "/" + "response");
		paths.add(syncCommandPath + "/" + "message");
		return (String[]) paths.toArray(new String[paths.size()]);
	}

	/* (non-Javadoc)
	 * @see eu.scy.common.packetextension.SCYPacketTransformer#mapXMLNodeToObject(java.lang.String, java.lang.String)
	 */
	@Override
	public void mapXMLNodeToObject(String path, String value) {
		if(path.equals(syncCommandPath + "/" + "event")) {
			pojo.setEvent(Event.valueOf(value));
		} else if(path.equals(syncCommandPath + "/" + "userId")) {
			pojo.setUserId(value);
		} else if(path.equals(syncCommandPath + "/" + "toolId")) {
			pojo.setToolId(value);
		} else if(path.equals(syncCommandPath + "/" + "type")) {
			pojo.setType(Type.valueOf(value));
		} else if(path.equals(syncCommandPath + "/" + "response")) {
			pojo.setResponse(Response.valueOf(value));
		} else if(path.equals(syncCommandPath + "/" + "message")) {
			pojo.setMessage(value);
		}
	}

	/* (non-Javadoc)
	 * @see eu.scy.common.packetextension.SCYPacketTransformer#resetParser()
	 */
	@Override
	public void resetParser() {
		pojo = new SyncMessage();
	}

	/* (non-Javadoc)
	 * @see eu.scy.common.packetextension.SCYPacketTransformer#setObject(java.lang.Object)
	 */
	@Override
	public void setObject(Object object) {
		pojo = (SyncMessage) object;
	}

}
