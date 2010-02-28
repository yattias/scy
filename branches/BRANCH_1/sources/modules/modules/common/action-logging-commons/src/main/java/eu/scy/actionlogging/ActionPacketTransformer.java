package eu.scy.actionlogging;

import java.util.ArrayList;

import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.common.packetextension.SCYPacketTransformer;

/**
 * @author giemza, weinbrenner
 * 
 */
public class ActionPacketTransformer extends SCYPacketTransformer {

    private static final String actionPath = "/" + Action.PATH;

    private static final String contextPath = actionPath + "/" + Context.PATH + "/";

    private static final String attributesPath = actionPath + "/attributes/";

    private Action pojo;

    public ActionPacketTransformer() {
        super(Action.PATH);
    }

    public ActionPacketTransformer(Action pojo) {
        super(Action.PATH, pojo);
    }

    @Override
    public Object getObject() {
        return pojo;
    }

    @Override
    public String getValueForXPath(String path) {
        if (path.equals(actionPath + "@user")) {
            return pojo.getUser();
        /*} else if (path.equals(actionPath + "@time")) {
            return pojo.getTime();*/
        } else if (path.equals(actionPath + "@timemillis")) {
            return String.valueOf(pojo.getTimeInMillis());
        } else if (path.equals(actionPath + "@type")) {
            return pojo.getType();
        } else if (path.equals(actionPath + "@id")) {
            return pojo.getId();
        /*} else if (path.equals(actionPath + "@datatype")) {
            return pojo.getDataType();
        } else if (path.equals(actionPath + "@data")) {
            return pojo.getData();*/
        } else if (path.startsWith(contextPath)) {
            String constant = path.substring(contextPath.length());
            ContextConstants key = ContextConstants.valueOf(constant);
            return pojo.getContext(key);
        } else if (path.startsWith(attributesPath)) {
            String attribute = path.substring(attributesPath.length());
            return pojo.getAttribute(attribute);
        }
        return null;
    }

    @Override
    public String[] getXPaths() {
        if (pojo == null) {
            throw new IllegalStateException("Object must not be null before using the transformer!");
        }
        ArrayList<String> paths = new ArrayList<String>();
        paths.add(actionPath + "@user");
        //paths.add(actionPath + "@time");
        paths.add(actionPath + "@timemillis");
        paths.add(actionPath + "@type");
        paths.add(actionPath + "@id");
        //paths.add(actionPath + "@datatype");
        //paths.add(actionPath + "@data");
        for (ContextConstants context : ContextConstants.values()) {
            paths.add(actionPath + "/" + Context.PATH + "/" + context.toString());
        }
        for (String key : pojo.getAttributes().keySet()) {
            paths.add(attributesPath + key);
        }
        return (String[]) paths.toArray(new String[paths.size()]);
    }

    @Override
    public void mapXMLNodeToObject(String path, String value) {
        if (path.equals(actionPath + "@user")) {
            pojo.setUser(value);
        /*} else if (path.equals(actionPath + "@time")) {
            pojo.setTime(value);*/
        } else if (path.equals(actionPath + "@timemillis")) {
            pojo.setTimeInMillis(Long.parseLong(value));
        } else if (path.equals(actionPath + "@type")) {
            pojo.setType(value);
        } else if (path.equals(actionPath + "@id")) {
            pojo.setId(value);
        /*} else if (path.equals(actionPath + "@datatype")) {
            pojo.setDataType(value);
        } else if (path.equals(actionPath + "@data")) {
            pojo.setData(value);*/
        } else if (path.startsWith(contextPath)) {
            String constant = path.substring(contextPath.length());
            ContextConstants key = ContextConstants.valueOf(constant);
            pojo.addContext(key, value);
        } else if (path.startsWith(attributesPath)) {
            String attribute = path.substring(attributesPath.length());
            pojo.addAttribute(attribute, value);
        }
    }

    @Override
    public void resetParser() {
        pojo = new Action();
    }

    @Override
    public void setObject(Object object) {
        pojo = (Action) object;
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
		return new ActionPacketTransformer();
	}

}
