/**
 * 
 */
package eu.scy.common.datasync;

import java.util.ArrayList;

import eu.scy.common.datasync.ISyncAction.Type;
import eu.scy.common.packetextension.SCYPacketTransformer;

/**
 * @author giemza
 * 
 */
public class SyncActionPacketTransformer extends SCYPacketTransformer {

	private static final String syncActionPath = "/" + SyncAction.PATH;

	private static final String syncObjectPath = syncActionPath + "/"
			+ SyncObject.PATH;

	private static final String propertiesPath = syncObjectPath
			+ "/properties/";

	private SyncAction pojo;

	/**
	 * @param name
	 */
	public SyncActionPacketTransformer() {
		super(SyncAction.PATH);
	}

	/**
	 * @param name
	 * @param object
	 */
	public SyncActionPacketTransformer(Object object) {
		super(SyncAction.PATH, object);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.scy.common.packetextension.SCYPacketTransformer#getObject()
	 */
	@Override
	public Object getObject() {
		return pojo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.scy.common.packetextension.SCYPacketTransformer#getValueForXPath(java
	 * .lang.String)
	 */
	@Override
	public String getValueForXPath(String path) {
		if (path.equals(syncActionPath + "@id")) {
			return pojo.getId();
		} else if (path.equals(syncActionPath + "@timestamp")) {
			return Long.toString(pojo.getTimestamp());
		} else if (path.equals(syncActionPath + "@sessionId")) {
			return pojo.getSessionId();
		} else if (path.equals(syncActionPath + "@userId")) {
			return pojo.getUserId();
		} else if (path.equals(syncActionPath + "@type")) {
			return pojo.getType().toString();
		} else if (path.equals(syncObjectPath + "@id")) {
			return pojo.getSyncObject().getID();
		} else if (path.equals(syncObjectPath + "@toolname")) {
			return pojo.getSyncObject().getToolname();
		} else if (path.equals(syncObjectPath + "@creator")) {
			return pojo.getSyncObject().getCreator();
		} else if (path.equals(syncObjectPath + "@creationtime")) {
			return Long.toString(pojo.getSyncObject().getCreationTime());
		} else if (path.equals(syncObjectPath + "@lastmodificator")) {
			return pojo.getSyncObject().getLastModificator();
		} else if (path.equals(syncObjectPath + "@lastmodificationtime")) {
			return Long.toString(pojo.getSyncObject().getLastModificationTime());
		} else if (path.startsWith(propertiesPath)) {
			String attribute = path.substring(propertiesPath.length());
			return pojo.getSyncObject().getProperty(attribute);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.scy.common.packetextension.SCYPacketTransformer#getXPaths()
	 */
	@Override
	public String[] getXPaths() {
		if (pojo == null) {
			throw new IllegalStateException("Object must not be null before using the transformer!");
		}
		ArrayList<String> paths = new ArrayList<String>();
		paths.add(syncActionPath + "@id");
		paths.add(syncActionPath + "@timestamp");
		paths.add(syncActionPath + "@userId");
		paths.add(syncActionPath + "@sessionId");
		paths.add(syncActionPath + "@type");
		paths.add(syncObjectPath + "@id");
		paths.add(syncObjectPath + "@toolname");
		paths.add(syncObjectPath + "@creator");
		paths.add(syncObjectPath + "@lastmodificator");
		paths.add(syncObjectPath + "@creationtime");
		paths.add(syncObjectPath + "@lastmodificationtime");
		for (String key : pojo.getSyncObject().getProperties().keySet()) {
			paths.add(propertiesPath + key);
		}
		return (String[]) paths.toArray(new String[paths.size()]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.scy.common.packetextension.SCYPacketTransformer#mapXMLNodeToObject
	 * (java.lang.String, java.lang.String)
	 */
	@Override
	public void mapXMLNodeToObject(String path, String value) {
		if (path.equals(syncActionPath + "@id")) {
			pojo.setId(value);
		} else if (path.equals(syncActionPath + "@timestamp")) {
			pojo.setTimestamp(Long.parseLong(value));
		} else if (path.equals(syncActionPath + "@sessionId")) {
			pojo.setSessionId(value);
		} else if (path.equals(syncActionPath + "@userId")) {
			pojo.setUserId(value);
		} else if (path.equals(syncActionPath + "@type")) {
			pojo.setType(Type.valueOf(value));
		} else if (path.equals(syncObjectPath + "@id")) {
			pojo.getSyncObject().setID(value);
		} else if (path.equals(syncObjectPath + "@toolname")) {
			pojo.getSyncObject().setToolname(value);
		} else if (path.equals(syncObjectPath + "@creator")) {
			pojo.getSyncObject().setCreator(value);
		} else if (path.equals(syncObjectPath + "@creationtime")) {
			pojo.getSyncObject().setCreationTime(Long.parseLong(value));
		} else if (path.equals(syncObjectPath + "@lastmodificator")) {
			pojo.getSyncObject().setCreator(value);
		} else if (path.equals(syncObjectPath + "@lastmodificationtime")) {
			pojo.getSyncObject().setLastModificationTime(Long.parseLong(value));
		} else if (path.startsWith(propertiesPath)) {
			String attribute = path.substring(propertiesPath.length());
			pojo.getSyncObject().setProperty(attribute, value);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.scy.common.packetextension.SCYPacketTransformer#resetParser()
	 */
	@Override
	public void resetParser() {
		pojo = new SyncAction();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.scy.common.packetextension.SCYPacketTransformer#setObject(java.lang
	 * .Object)
	 */
	@Override
	public void setObject(Object object) {
		pojo = (SyncAction) object;
	}

	@Override
	public void endNode(String path) {
		// ignore
	}

	@Override
	public void startNode(String path) {
		// ignore
	}

	@Override
	public SCYPacketTransformer newInstance() {
		return new SyncActionPacketTransformer();
	}

}
