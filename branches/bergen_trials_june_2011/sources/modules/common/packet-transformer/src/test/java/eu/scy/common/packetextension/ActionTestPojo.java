package eu.scy.common.packetextension;

public class ActionTestPojo {

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((actiontype1 == null) ? 0 : actiontype1.hashCode());
		result = prime * result
				+ ((actiontype2 == null) ? 0 : actiontype2.hashCode());
		result = prime * result + (int) (timestamp1 ^ (timestamp1 >>> 32));
		result = prime * result + (int) (timestamp2 ^ (timestamp2 >>> 32));
		result = prime * result
				+ ((toolName == null) ? 0 : toolName.hashCode());
		result = prime * result + toolVersion;
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ActionTestPojo other = (ActionTestPojo) obj;
		if (actiontype1 == null) {
			if (other.actiontype1 != null)
				return false;
		} else if (!actiontype1.equals(other.actiontype1))
			return false;
		if (actiontype2 == null) {
			if (other.actiontype2 != null)
				return false;
		} else if (!actiontype2.equals(other.actiontype2))
			return false;
		if (timestamp1 != other.timestamp1)
			return false;
		if (timestamp2 != other.timestamp2)
			return false;
		if (toolName == null) {
			if (other.toolName != null)
				return false;
		} else if (!toolName.equals(other.toolName))
			return false;
		if (toolVersion != other.toolVersion)
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	private String username;
	
	private String actiontype1;
	
	private long timestamp1;
	
	private String actiontype2;
	
	private long timestamp2;
	
	private String toolName;
	
	private int toolVersion;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getActiontype1() {
		return actiontype1;
	}

	public void setActiontype1(String actiontype1) {
		this.actiontype1 = actiontype1;
	}

	public long getTimestamp1() {
		return timestamp1;
	}

	public void setTimestamp1(long timestamp1) {
		this.timestamp1 = timestamp1;
	}

	public String getActiontype2() {
		return actiontype2;
	}

	public void setActiontype2(String actiontype2) {
		this.actiontype2 = actiontype2;
	}

	public long getTimestamp2() {
		return timestamp2;
	}

	public void setTimestamp2(long timestamp2) {
		this.timestamp2 = timestamp2;
	}

	public String getToolName() {
		return toolName;
	}

	public void setToolName(String toolName) {
		this.toolName = toolName;
	}

	public int getToolVersion() {
		return toolVersion;
	}

	public void setToolVersion(int toolVersion) {
		this.toolVersion = toolVersion;
	}
	
	
	
}
