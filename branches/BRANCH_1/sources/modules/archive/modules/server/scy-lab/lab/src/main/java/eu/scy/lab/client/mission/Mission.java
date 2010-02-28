package eu.scy.lab.client.mission;

import java.util.Date;


public class Mission {
	
	private String title;
	private String lastVisitedDate;
	private String description;
	private String goal;
	
	public Mission(String title, String lastVisitedDate){
		this(title,lastVisitedDate,new String(""),new String(""));
	}
	
	public Mission(String title, String lastVisitedDate,String goal, String description){
	    this.title = title;
            this.lastVisitedDate = lastVisitedDate;
            this.goal = goal;
            this.description = description;	}

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

    
    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    
    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    
    /**
     * @return the goal
     */
    public String getGoal() {
        return goal;
    }

    
    /**
     * @param goal the goal to set
     */
    public void setGoal(String goal) {
        this.goal = goal;
    }
	
	

}
