package eu.scy.common.packetextension;
import java.security.InvalidParameterException;

import eu.scy.common.packetextension.SCYPacketTransformer;


public class PacketTestTransformer extends SCYPacketTransformer {

	private ActionTestPojo pojo;
	
	public PacketTestTransformer() {
		super("TestPacketTransformer");
	}

	@Override
	public void resetParser() {
		pojo = new ActionTestPojo();
	}

	@Override
	public Object getObject() {
		return pojo;
	}

	@Override
	public String getValueForXPath(String path) {
		if (path.equals("/lom[0]/username")) {
			return pojo.getUsername();
		} else if (path.equals("/lom[0]/action[0]/type")) {
			return pojo.getActiontype1();
		} else if (path.equals("/lom[0]/action[1]/type")) {
			return pojo.getActiontype2();
		} else if (path.equals("/lom[0]/action[0]@timestamp")) {
			return String.valueOf(pojo.getTimestamp1());
		} else if (path.equals("/lom[0]/action[1]@timestamp")) {
			return String.valueOf(pojo.getTimestamp2());
		} else if (path.equals("/lom[0]/tool[0]/name")) {
			return pojo.getToolName();
		} else if (path.equals("/lom[0]/tool[0]/version")) {
			return String.valueOf(pojo.getToolVersion());
		} else {
			throw new InvalidParameterException("This xpath is not known to this pojo!");
		}
	}

	@Override
	public String[] getXPaths() {
		return new String[] {"/lom[0]/username", "/lom[0]/action[0]/type", "/lom[0]/action[0]@timestamp", "/lom[0]/action[1]/type", "/lom[0]/action[1]@timestamp", "/lom[0]/tool[0]/name", "/lom[0]/tool[0]/version"};
	}

	@Override
	public void mapXMLNodeToObject(String path, String value) {
		if (path.equals("/lom[0]/username")) {
			 pojo.setUsername(value);
		} else if (path.equals("/lom[0]/action[0]/type")) {
			 pojo.setActiontype1(value);
		} else if (path.equals("/lom[0]/action[1]/type")) {
			 pojo.setActiontype2(value);
		} else if (path.equals("/lom[0]/action[0]@timestamp")) {
			 pojo.setTimestamp1(Long.parseLong(value));
		} else if (path.equals("/lom[0]/action[1]@timestamp")) {
			 pojo.setTimestamp2(Long.parseLong(value));
		} else if (path.equals("/lom[0]/tool[0]/name")) {
			 pojo.setToolName(value);
		} else if (path.equals("/lom[0]/tool[0]/version")) {
			 pojo.setToolVersion(Integer.parseInt(value));
		} else {
			throw new InvalidParameterException("This xpath is not known to this pojo!");
		}		
	}

	@Override
	public void setObject(Object object) {
		this.pojo = (ActionTestPojo) object;
	}

	@Override
	public void endNode(String path) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startNode(String path) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SCYPacketTransformer newInstance() {
		return new PacketTestTransformer();
	}

}
