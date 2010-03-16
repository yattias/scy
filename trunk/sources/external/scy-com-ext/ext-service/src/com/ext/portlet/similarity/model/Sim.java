package com.ext.portlet.similarity.model;

import java.util.Date;
import java.util.Hashtable;

public class Sim {

	/** table with assetEntry entryId and sim value from start resource to sim resource */
	private Hashtable<Long, Double> simValueList;
	
	/** table with assetEntry entryId and last modified date from sim resource */
	private Hashtable<Long, Date> simDateList;
	
	/** table with assetEntry entryId and view count from sim resource */
	private Hashtable<Long, Integer> simViewList;

	public void setSimValueList(Hashtable<Long, Double> simValueList) {
		this.simValueList = simValueList;
	}

	public Hashtable<Long, Double> getSimValueList() {
		return simValueList;
	}

	public void setSimDateList(Hashtable<Long, Date> simDateList) {
		this.simDateList = simDateList;
	}

	public Hashtable<Long, Date> getSimDateList() {
		return simDateList;
	}

	public void setSimViewList(Hashtable<Long, Integer> simViewList) {
		this.simViewList = simViewList;
	}

	public Hashtable<Long, Integer> getSimViewList() {
		return simViewList;
	}

}
