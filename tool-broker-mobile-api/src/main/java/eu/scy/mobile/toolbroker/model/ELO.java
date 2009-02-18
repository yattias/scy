package eu.scy.mobile.toolbroker.model;

import java.util.Vector;

/**
 * Created: 05.feb.2009 14:24:49
 *
 * @author Bjørge Næss
 */


public class ELO {
	private String title;
	private Object content;
	private int id;
	private Vector children = new Vector();

	public ELO() {}
	public ELO(int id, String title, String content) {
		this.id = id;
		this.title = title;
		this.content = content;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}
	public Vector getChildren() {
		return children;
	}
	public void addChildELO(ELO elo) {
		this.children.addElement(elo);
	}
	public void setChildren(Vector children) {
		this.children = children;
	}
}
