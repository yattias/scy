package eu.scy.agents.search.searchresultenricher;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import roolo.search.ISearchResult;

/*
 * A ranked list of SearchResults and their quality. First entry provides the best result.
 */
public class SearchResultRanking {

	public static final String OPTIMUM_SIMILARITY_VALUE = "result_ranking_optimal_similiarity_value";
	public static final String RELEVANCE_ENTRIES = "result_ranking_relevance_entries";

	private LinkedList<Result> ranking;
	private List<ISearchResult> referenceResults;

	private int capacity;
	private double optimalSimilarityValue;
	private int relevanceEntryNumber;
	private int infGoodSearchInterval;
	private int supGoodSearchInterval;
	
	public SearchResultRanking(int capacity,
			List<ISearchResult> referenceResults, Map<String, Object> config, int infGoodSearchInterval, int supGoodSearchInterval) {
		this.ranking = new LinkedList<Result>();
		this.referenceResults = referenceResults;
		this.capacity = capacity;
		this.infGoodSearchInterval = infGoodSearchInterval;
		this.supGoodSearchInterval = supGoodSearchInterval;
		configure(config);
	}
	
	private void configure(Map<String, Object> config) {
        if(config.get(OPTIMUM_SIMILARITY_VALUE) != null) {
        	this.optimalSimilarityValue = (Double) config.get(OPTIMUM_SIMILARITY_VALUE);
        } else {
        	this.optimalSimilarityValue = 0.75;
        }
        if(config.get(RELEVANCE_ENTRIES) != null) {
        	this.relevanceEntryNumber = (Integer) config.get(RELEVANCE_ENTRIES);
        } else {
        	this.relevanceEntryNumber = 3;
        }
	}

	/**
	 * Adds the search result in the ranking.
	 * 
	 * @param query
	 * @param results
	 */
	public void add(String query, List<ISearchResult> results) {
		double lengthQuality = evaluateLength(results.size(), this.getReferenceResults().size(), this.infGoodSearchInterval, this.supGoodSearchInterval);
		double similarityQuality = evaluateSimilarity(results, this.getReferenceResults(), this.optimalSimilarityValue);
		double relevanceQuality = evaluateRelevance(results, this.getReferenceResults());

		Result result = new Result(query, results, lengthQuality, similarityQuality, relevanceQuality);
		int index = this.ranking.size();
		for (int i = 0; i < this.ranking.size(); i++) {
			if (result.compareTo(this.ranking.get(i)) >= 0) {
				index = i;
				break;
			}
		}

		if (index < this.capacity) {
			this.ranking.add(index, result);
			if (this.ranking.size() > this.capacity) {
				// Ranking capacity has been reached, remote the last entry
				this.ranking.removeLast();
			}
		}
	}

	public List<ISearchResult> getReferenceResults() {
		return referenceResults;
	}
	
	public Result[] getResults() {
		Result[] resultArray = new Result[this.ranking.size()];
		return this.ranking.toArray(resultArray);
	}

	@Override
	public String toString() {
        StringBuilder sb = new StringBuilder();
        for(Result result : this.ranking) {
            sb.append(result.toString());
            sb.append("\n");
        }
		return sb.toString().trim();
	}

	private double evaluateRelevance(List<ISearchResult> newResult, List<ISearchResult> referenceResult) {
        if(newResult.isEmpty()) {
            return 0.0;
        }
		// Compare the mean of RELEVANCE_NUMBER search results.
		int n = this.relevanceEntryNumber;
		int min = Math.min(referenceResult.size(), newResult.size());
		if (min == 0) {
			return 1.0;
		}
		if (min < n) {
			n = min;
		}
		double refMean = 0;
		double newMean = 0;
		for (int i = 0; i < n; i++) {
			refMean += referenceResult.get(i).getRelevance();
			newMean += newResult.get(i).getRelevance();
		}
		refMean /= n;
		newMean /= n;
		// Get the quotient of the means
		return (newMean + 1) / (refMean + 1);
	}

	private static int getIntersectionUriCount(List<ISearchResult> a, List<ISearchResult> b) {
		// get the number of identical eloUris
		int identicalResults = 0;
		for (ISearchResult bRes : b) {
			for (ISearchResult aRes : a) {
				if (bRes.getUri().equals(aRes.getUri())) {
					identicalResults++;
					break;
				}
			}
		}
		return identicalResults;
	}

    private static int getUnionUriCount(List<ISearchResult> a, List<ISearchResult> b) {
    	// get the number of all unique eloUris in both results
        int result = a.size();
        for (ISearchResult bRes : b) {
            boolean exists = false;
            for(ISearchResult aRes : a) {
                if(bRes.getUri().equals(aRes.getUri())) {
                    exists = true;
                    break;
                }
            }
            if(!exists) {
                result++;
            }
        }
        return result;
    }

	private static double evaluateSimilarity(List<ISearchResult> newResult,
			List<ISearchResult> refResult, double optimalSimilarityValue) {
        if(newResult.isEmpty()) {
            return 0.0;
        }
		double intersectionCount = getIntersectionUriCount(newResult, refResult);
        double unionCount = getUnionUriCount(newResult, refResult);

        // calculate ratio of intersection and union (+1 to avoid division by zero)
        double ratio = (intersectionCount) / (unionCount + 1);

        // calculate distance between optimal similarity value and the calculated ratio
        double distance = Math.abs(optimalSimilarityValue - ratio);

		return optimalSimilarityValue - distance;
	}

	private static double evaluateLength(int newResult, int refResult, int infGoodSearchInterval, int supGoodSearchInterval) {
        if(newResult == 0) {
            return 0.0;
        }
        
		double quality;
		if (newResult < refResult) {
			// +1 to avoid division by zero
			quality = (((double)refResult - newResult) / (refResult + 1));
		} else {
			quality = (((double)newResult - refResult) / (newResult + 1));
		}

		double penalty = 1.0;
		if (newResult < infGoodSearchInterval) {
			// query has not enough results, so use a penalty
			penalty = (infGoodSearchInterval - (double)newResult) / infGoodSearchInterval;
		} else if (newResult > supGoodSearchInterval) {
			// query has too many results, so use a penalty
			penalty = ((double)newResult - infGoodSearchInterval) / newResult;
		}

		return quality * penalty;
	}
}
