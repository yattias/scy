/**
 * 
 */
package eu.scy.notification;

import eu.scy.common.packetextension.SCYPacketTransformer;

/**
 * @author giemza
 *
 */
public class NotificationPacketTransformer extends SCYPacketTransformer {

	private static final String notificationPath = "/" + Notification.PATH;
	
	private static final String propertiesPath = notificationPath + "/" + "properties";
	
	
	private Notification pojo;
	
	/**
	 * @param name
	 */
	public NotificationPacketTransformer() {
		super(Notification.PATH);
	}

	/**
	 * @param name
	 * @param object
	 */
	public NotificationPacketTransformer(Object object) {
		super(Notification.PATH, object);
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
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see eu.scy.common.packetextension.SCYPacketTransformer#getXPaths()
	 */
	@Override
	public String[] getXPaths() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see eu.scy.common.packetextension.SCYPacketTransformer#mapXMLNodeToObject(java.lang.String, java.lang.String)
	 */
	@Override
	public void mapXMLNodeToObject(String path, String value) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see eu.scy.common.packetextension.SCYPacketTransformer#resetParser()
	 */
	@Override
	public void resetParser() {
		pojo = new Notification();
	}

	/* (non-Javadoc)
	 * @see eu.scy.common.packetextension.SCYPacketTransformer#setObject(java.lang.Object)
	 */
	@Override
	public void setObject(Object object) {
		pojo = (Notification) object;
	}

}
