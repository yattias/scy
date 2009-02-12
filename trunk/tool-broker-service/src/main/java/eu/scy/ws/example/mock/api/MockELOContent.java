package eu.scy.ws.example.mock.api;

import eu.scy.ws.example.api.ELOContent;

/**
 * Created: 11.feb.2009 09:52:20
 *
 * @author Bjørge Næss
 * 
 */
public class MockELOContent implements ELOContent {

	private int type;

	private Object content;

	public MockELOContent() {}
	public MockELOContent(String content) {
		this.content = content;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}
}
