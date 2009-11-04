package eu.scy.actionlogging;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.actionlogging.api.IAction;
import eu.scy.actionlogging.api.IContext;
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
	private IContext context;
	
	private String dataType;
	
	private String data;
	
	private Map<String, String> attributes;
	
    public Action() {
    	setId(UUID.randomUUID().toString());
    	time = TimeFormatHelper.getInstance().getCurrentTimeMillisAsISO8601(); 	
    	attributes = new HashMap<String, String>();
    }
     
    @Override
    public void setContext(IContext context) {
    	this.context = context;
    }
    
    @Override
    public IContext getContext() {
    	return context;
    }
    
    @Override
    public void addContext(ContextConstants constant, String value) {
    	if(context == null) {
    		context = new Context();
    	}
    	context.set(constant, value);
    }
    
    @Override
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
    @Override
	public String getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
    @Override
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @return the time
	 */
    @Override
	public String getTime() {
		return time;
	}

	/**
	 * @param time the time to set
	 */
    @Override
	public void setTime(String time) {
		this.time = time;
	}

	/**
	 * @return the type
	 */
    @Override
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
    @Override
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the dataType
	 */
    @Override
	public String getDataType() {
		return dataType;
	}

	/**
	 * @param dataType the dataType to set
	 */
    @Override
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	/**
	 * @return the data
	 */
    @Override
	public String getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
    @Override
	public void setData(String data) {
		this.data = data;
	}

	/**
	 * @return the attributes
	 */
	@Override
	public Map<String, String> getAttributes() {
		return attributes;
	}
    
}
