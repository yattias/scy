/**
 * 
 */
package eu.scy.common.message;

import java.util.ArrayList;

import eu.scy.common.datasync.ISyncObject;
import eu.scy.common.datasync.SyncObject;
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

	private static final String syncObjectsPath =  syncCommandPath + "/" + SyncObject.PATH + "s"; // s for the plural

	private static final String syncObjectPath = syncObjectsPath + "/" + SyncObject.PATH;
	
	private static final String properiesSubPath = "/properties/";
	
	private SyncMessage pojo;
	
	private ISyncObject currentObject;
	
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
		} else if(path.equals(syncCommandPath + "/" + "sessionId")) {
			return (pojo.getSessionId());
		} else if(path.equals(syncCommandPath + "/" + "message")) {
			return pojo.getMessage();
		} else if(path.startsWith(syncObjectPath)) {
			Integer index = parseIndex(path);
			if(index != null) {
				String key = path.substring(path.indexOf("]") + 1);
				if ("@id".equals(key)) {
					return pojo.getSyncObjects().get(index).getID();
				} else if ("@toolname".equals(key)) {
					return pojo.getSyncObjects().get(index).getToolname();
				} else if ("@creator".equals(key)) {
					return pojo.getSyncObjects().get(index).getCreator();
				} else if ("@lastmodificator".equals(key)) {
					return pojo.getSyncObjects().get(index).getLastModificator();
				} else if ("@creationtime".equals(key)) {
					return Long.toString(pojo.getSyncObjects().get(index).getCreationTime());
				} else if ("@lastmodificationtime".equals(key)) {
					return Long.toString(pojo.getSyncObjects().get(index).getLastModificationTime());
				} else if (key.startsWith(properiesSubPath)) {
					key = key.substring(key.indexOf(properiesSubPath) + properiesSubPath.length());
					if(key != null) {
						return pojo.getSyncObjects().get(index).getProperty(key);
					}
				}
			}
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
		paths.add(syncCommandPath + "/" + "sessionId");
		paths.add(syncCommandPath + "/" + "message");
		for (int i = 0; i < pojo.getSyncObjects().size(); i++) {
			paths.add(syncObjectPath + "[" + i + "]" + "@id");
			paths.add(syncObjectPath + "[" + i + "]" + "@toolname");
			paths.add(syncObjectPath + "[" + i + "]" + "@creator");
			paths.add(syncObjectPath + "[" + i + "]" + "@lastmodificator");
			paths.add(syncObjectPath + "[" + i + "]" + "@creationtime");
			paths.add(syncObjectPath + "[" + i + "]" + "@lastmodificationtime");
			for (String key : pojo.getSyncObjects().get(i).getProperties().keySet()) {
				paths.add(syncObjectPath + "[" + i + "]" +  "/properties/" + key);
			}
		}
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
		} else if(path.equals(syncCommandPath + "/" + "sessionId")) {
			pojo.setSessionId(value);
		} else if(path.equals(syncCommandPath + "/" + "message")) {
			pojo.setMessage(value);
		} else if(path.startsWith(syncObjectPath)) {
			//TODO
			if(path.startsWith(syncObjectPath + "@id")) {
				currentObject.setID(value);
			} else if(path.startsWith(syncObjectPath + "@toolname")) {
				currentObject.setToolname(value);
			} else if(path.startsWith(syncObjectPath + "@creator")) {
				currentObject.setCreator(value);
			} else if(path.startsWith(syncObjectPath + "@lastmodificator")) {
				currentObject.setLastModificator(value);
			} else if(path.startsWith(syncObjectPath + "@creationtime")) {
				currentObject.setCreationTime(Long.parseLong(value));
			} else if(path.startsWith(syncObjectPath + "@lastmodificationtime")) {
				currentObject.setLastModificationTime(Long.parseLong(value));
			} else if(path.startsWith(syncObjectPath + "/properties/")) {
				String key = path.substring(path.indexOf(properiesSubPath)  + properiesSubPath.length());
//				String[] keyValue = value.substring(value.indexOf(properiesSubPath) + properiesSubPath.length()).split("=");
				currentObject.setProperty(key, value);
			}
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

	@Override
	public void startNode(String path) {
		if(syncObjectPath.equals(path)) {
			currentObject = new SyncObject();
		}
	}
	
	@Override
	public void endNode(String path) {
		if(syncObjectPath.equals(path)) {
			pojo.getSyncObjects().add(currentObject);
			currentObject = null;
		}
	}

	@Override
	public SCYPacketTransformer newInstance() {
		return new DataSyncMessagePacketTransformer();
	}
}