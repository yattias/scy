/**
 * 
 */
package eu.scy.notification;

import java.util.ArrayList;

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
     * @see eu.scy.common.packetextension.SCYPacketTransformer#getValueForXPath(java.lang.String)
     */
    @Override
    public String getValueForXPath(String path) {
        if (path.equals(notificationPath + "@id")) {
            return pojo.getUniqueID();
        } else if (path.equals(notificationPath + "@timemillis")) {
            return Long.toString(pojo.getTimestamp());
        } else if (path.equals(notificationPath + "@toolId")) {
            return pojo.getToolId();
        } else if (path.equals(notificationPath + "@sender")) {
            return pojo.getSender();
        } else if (path.equals(notificationPath + "@mission")) {
            return pojo.getMission();
        } else if (path.equals(notificationPath + "@session")) {
            return pojo.getSession();
        } else if (path.startsWith(propertiesPath)) {
            String attribute = path.substring(propertiesPath.length() + 1);
            return pojo.getPropertyXMLString(attribute);
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
        paths.add(notificationPath + "@id");
        paths.add(notificationPath + "@timemillis");
        paths.add(notificationPath + "@toolId");
        paths.add(notificationPath + "@sender");
        paths.add(notificationPath + "@mission");
        paths.add(notificationPath + "@session");
        for (String key : pojo.getProperties().keySet()) {
            paths.add(propertiesPath + "/" + key);
        }
        return (String[]) paths.toArray(new String[paths.size()]);
    }

    /*
     * (non-Javadoc)
     * 
     * @see eu.scy.common.packetextension.SCYPacketTransformer#mapXMLNodeToObject(java.lang.String,
     * java.lang.String)
     */
    @Override
    public void mapXMLNodeToObject(String path, String value) {
        if (path.equals(notificationPath + "@id")) {
            pojo.setUniqueID(value);
        } else if (path.equals(notificationPath + "@timemillis")) {
            pojo.setTimestamp(Long.parseLong(value));
        } else if (path.equals(notificationPath + "@toolId")) {
            pojo.setToolId(value);
        } else if (path.equals(notificationPath + "@sender")) {
            pojo.setSender(value);
        } else if (path.equals(notificationPath + "@mission")) {
            pojo.setMission(value);
        } else if (path.equals(notificationPath + "@session")) {
            pojo.setSession(value);
        } else if (path.startsWith(propertiesPath)) {
            String attribute = path.substring(propertiesPath.length() + 1);
            pojo.addPropertyXMLString(attribute, value);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see eu.scy.common.packetextension.SCYPacketTransformer#resetParser()
     */
    @Override
    public void resetParser() {
        pojo = new Notification();
    }

    /*
     * (non-Javadoc)
     * 
     * @see eu.scy.common.packetextension.SCYPacketTransformer#setObject(java.lang.Object)
     */
    @Override
    public void setObject(Object object) {
        pojo = (Notification) object;
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
        return new NotificationPacketTransformer();
    }

}
