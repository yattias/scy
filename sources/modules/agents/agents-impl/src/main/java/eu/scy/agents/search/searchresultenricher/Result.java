package eu.scy.agents.search.searchresultenricher;

import java.util.List;

import roolo.search.ISearchResult;

public class Result implements Comparable<Result> {	
	
	private static final double WEIGHT_LENGTH = 0.3333;
	private static final double WEIGHT_SIMILARITY = 0.3333;
	private static final double WEIGHT_RELEVANCE = 0.3333;
	
	private String query;
	
	private List<ISearchResult> result;
	
    private double lengthQuality;

    private double similarityQuality;

    private double relevanceQuality;
	
	public Result(String query, List<ISearchResult> result, double lengthQuality, double similarityQuality, double relevanceQuality) {
		this.query = query;
		this.result = result;
		this.lengthQuality = lengthQuality;
        this.similarityQuality = similarityQuality;
        this.relevanceQuality = relevanceQuality;
	}

	public String getQuery() {
		return this.query;
	}

	public List<ISearchResult> getResult() {
		return result;
	}
	
    public double getLengthQuality() {
        return lengthQuality;
    }

    public double getRelevanceQuality() {
        return relevanceQuality;
    }

    public double getSimilarityQuality() {
        return similarityQuality;
    }

    public double getQuality() {
        double result = WEIGHT_LENGTH * lengthQuality +
                WEIGHT_SIMILARITY * similarityQuality +
                WEIGHT_RELEVANCE * relevanceQuality;
		if(this.result.isEmpty()) {
            return result - 1;
		} else {
            return result;
        }
    }

    @Override
    public String toString() {
        return "Query: " + this.query + " | Results: " + this.result.size() + " | Quality: [ Length: " +
                this.lengthQuality + " | Similarity: " + this.similarityQuality + " | Relevance: " +
                this.relevanceQuality + " ]";
    }

	@Override
	public int compareTo(Result that) {
		Double thisQuality = this.getQuality();
		return thisQuality.compareTo(that.getQuality());
	}
	
}
