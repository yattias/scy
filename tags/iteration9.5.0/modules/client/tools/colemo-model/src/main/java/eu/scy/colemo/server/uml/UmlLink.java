/*
 * Created on 04.okt.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package eu.scy.colemo.server.uml;


import java.io.Serializable;

/**
*
 */
public class UmlLink extends AbstractDiagramData implements Serializable {
	private String from;
	private String to;
	private String createdBy;
		
	/**
	 *
	 */
	public UmlLink(String from, String to, String createdBy) {
		super("");
		this.from = from;
		this.to = to;
		this.createdBy = createdBy;
	}

	/**
	 * @return Returns the from.
	 */
	public String getFrom() {
		return from;
	}
	/**
	 * @param from The from to set.
	 */
	public void setFrom(String from) {
		this.from = from;
	}
	/**
	 * @return Returns the to.
	 */
	public String getTo() {
		return to;
	}
	/**
	 * @param to The to to set.
	 */
	public void setTo(String to) {
		this.to = to;
	}
	
	/**
	 * @return Returns the createdBy.
	 */
	public String getCreatedBy() {
		return createdBy;
	}
}
