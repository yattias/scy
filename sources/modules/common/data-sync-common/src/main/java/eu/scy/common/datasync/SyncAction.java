package eu.scy.common.datasync;

import java.util.UUID;

public class SyncAction implements ISyncAction {

	public static final String PATH = "syncaction";
	
	private String id;
	
	private String timestamp;
	
	private String sessionId;
	
	private String userId;
	
	private Type type;
	
	private ISyncObject syncObject;
	
	/**
	 * Creates an instance of a {@link SyncAction}.
	 * 
	 * @param sessionId
	 * @param userId
	 * @param type
	 * @param syncObject
	 */
	public SyncAction(String sessionId, String userId, Type type, ISyncObject syncObject) {
		this();
		this.sessionId = sessionId;
		this.userId = userId;
		this.type = type;
		this.syncObject = syncObject;
	}
	
	/**
	 * Creates an instance of a {@link SyncAction}. This constructor should only be
	 * called by the PacketTransformer!
	 */
	public SyncAction() {
		id = UUID.randomUUID().toString();
		timestamp = TimeFormatHelper.getInstance().getCurrentTimeMillisAsISO8601();
		
		syncObject = new SyncObject();
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getSessionId() {
		return sessionId;
	}

	@Override
	public ISyncObject getSyncObject() {
		return syncObject;
	}

	@Override
	public String getTimestamp() {
		return timestamp;
	}

	@Override
	public Type getType() {
		return type;
	}

	@Override
	public String getUserId() {
		return userId;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	@Override
	public void setSyncObject(ISyncObject syncObject) {
		this.syncObject = syncObject;
	}

	@Override
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public void setType(Type type) {
		this.type = type;
	}

	@Override
	public void setUserId(String userId) {
		this.userId = userId;
	}

}
