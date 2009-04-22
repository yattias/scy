package eu.scy.lab.client.startupview.lastELO;

import java.util.Date;


public class ELO {
	
	private String title;
	private String lastVisitedDate;
	
	public ELO(String title, String lastVisitedDate){
		this.title = title;
		this.lastVisitedDate = lastVisitedDate;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the lastVisitedDate
	 */
	public String getLastVisitedDate() {
		return lastVisitedDate;
	}

	/**
	 * @param lastVisitedDate the lastVisitedDate to set
	 */
	public void setLastVisitedDate(String lastVisitedDate) {
		this.lastVisitedDate = lastVisitedDate;
	}

	/**
	 * @return the Mission looked up by Name
	 */
	public static ELO GetELOByName(String name) {
		// TODO connect to remote service
		final Date today = new Date();
		return new ELO(name,today.toString());
	}
	
	

}
