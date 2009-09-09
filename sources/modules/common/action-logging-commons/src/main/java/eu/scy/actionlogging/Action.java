package eu.scy.actionlogging;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.actionlogging.api.IAction;
import eu.scy.core.model.impl.ScyBaseObject;

public class Action extends ScyBaseObject implements IAction {
	
	public static final String PATH = "action";
	
	/** Maybe we want to have a real user object!? */
	private String user;
	
	/** Time in ISO8601 format */
	private String time;
	
	/** The type of the action */
	private String type;
	
	/** Action context */
	private Context context;
	
	private String dataType;
	
	private String data;
	
	private Map<String, String> attributes;
	
    public Action() {
    	setId(UUID.randomUUID().toString());
    	time = TimeFormatHelper.getInstance().getCurrentTimeMillisAsISO8601();
    	
    	attributes = new HashMap<String, String>();
    }
    
    public void setContext(Context context) {
    	this.context = context;
    }
    
    public Context getContext() {
    	return context;
    }
    
    public void addContext(ContextConstants constant, String value) {
    	if(context == null) {
    		context = new Context();
    	}
    	context.set(constant, value);
    }
    
	public String getContext(ContextConstants constant) {
		return context.get(constant);
	}
    
	@Override
    public void addAttribute(String name, String value) {	
		attributes.put(name, value);
    }
	
    @Override
	public String getAttribute(String key) {
    	return attributes.get(key);
	}
    
	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @return the time
	 */
	public String getTime() {
		return time;
	}

	/**
	 * @param time the time to set
	 */
	public void setTime(String time) {
		this.time = time;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the dataType
	 */
	public String getDataType() {
		return dataType;
	}

	/**
	 * @param dataType the dataType to set
	 */
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	/**
	 * @return the data
	 */
	public String getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(String data) {
		this.data = data;
	}

	/**
	 * @return the attributes
	 */
	public Map<String, String> getAttributes() {
		return attributes;
	}
    
}
