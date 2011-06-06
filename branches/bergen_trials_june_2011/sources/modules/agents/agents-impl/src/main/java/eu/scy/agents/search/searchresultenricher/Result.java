package eu.scy.agents.search.searchresultenricher;

import java.util.List;

import roolo.search.ISearchResult;

public class Result implements Comparable<Result> {	
	
	private String query;
	
	private List<ISearchResult> result;
	
	private double quality;
	
	public Result(String query, List<ISearchResult> result, double quality) {
		this.query = query;
		this.result = result;
		this.quality = quality;
	}
	
	public String getQuery() {
		return this.query;
	}

	public List<ISearchResult> getResult() {
		return result;
	}
	
	public double getQuality() {
		return this.quality;
	}

	@Override
	public int compareTo(Result that) {
		Double thisQuality = this.getQuality();
		return thisQuality.compareTo(that.getQuality());
	}
	
}
