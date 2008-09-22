package eu.scy.lab.client.startupview.lastMission;

import java.util.Date;


public class Mission {
	
	private String title;
	private String lastVisitedDate;
	
	public Mission(String title, String lastVisitedDate){
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
	public static Mission GetMissionByName(String name) {
		// TODO connect to remote service
		final Date today = new Date();
		return new Mission(name,today.toString());
	}
	
	

}
