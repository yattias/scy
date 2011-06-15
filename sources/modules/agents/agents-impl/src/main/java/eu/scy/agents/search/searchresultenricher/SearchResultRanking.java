package eu.scy.agents.search.searchresultenricher;

import java.util.LinkedList;
import java.util.List;

import roolo.search.ISearchResult;

/*
 * A ranked list of SearchResults and their quality. First entry provides the best result.
 */
public class SearchResultRanking {

	public static final double WEIGHT_LENGTH = 0.3333;

	public static final double WEIGHT_DIFFERENCE = 0.3333;

	public static final double WEIGHT_RELEVANCE = 0.3333;

    private static final double OPTIMUM_SIMILARITY_VALUE = 0.75;
    
	private static final int NUMBER_OF_RELEVANCE_ENTRIES = 3;

	private List<Result> ranking;

	private final int capacity;

	private List<ISearchResult> referenceResults;

	public SearchResultRanking(int capacity,
			List<ISearchResult> referenceResults) {
		this.capacity = capacity;
		this.ranking = new LinkedList<Result>();
		this.referenceResults = referenceResults;
	}

	/**
	 * Adds the search result in the ranking.
	 * 
	 * @param query
	 * @param results
	 */
	public void add(String query, List<ISearchResult> results) {
		double lengthQuality = evaluateLength(results.size(), this.referenceResults.size());
		double similarityQuality = evaluateSimilarity(results, this.referenceResults);
		double relevanceQuality = evaluateRelevance(results, this.referenceResults);

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
				this.ranking.remove(this.ranking.size() - 1);
			}
		}
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

//	private double evaluateQuality(List<ISearchResult> referenceResult,
//			List<ISearchResult> newResult) {
//		double lengthQuality = evaluateLength(newResult.size(),
//				referenceResult.size());
//		double similarityQuality = evaluateSimilarity(newResult,
//				referenceResult);
//		double relevanceQuality = evaluateRelevance(newResult, referenceResult);
//
//        double result = WEIGHT_LENGTH * lengthQuality + WEIGHT_DIFFERENCE
//                    * similarityQuality + WEIGHT_RELEVANCE * relevanceQuality;
//		if(newResult.isEmpty()) {
//            return result - 1;
//		} else {
//            return result;
//        }
//	}

	private double evaluateRelevance(List<ISearchResult> newResult, List<ISearchResult> referenceResult) {
        if(newResult.isEmpty()) {
            return 0.0;
        }
		// Compare the mean of the RELEVANCE_NUMBER first search results.
		int n = NUMBER_OF_RELEVANCE_ENTRIES;
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
			List<ISearchResult> refResult) {
        if(newResult.isEmpty()) {
            return 0.0;
        }
		double intersectionCount = getIntersectionUriCount(newResult, refResult);
        double unionCount = getUnionUriCount(newResult, refResult);

        // calculate ratio of intersection and union (+1 to avoid division by zero)
        double ratio = (intersectionCount + 1) / (unionCount + 1);

        // calculate distance between optimum similarity value and the calculated ratio
        double distance = Math.abs(OPTIMUM_SIMILARITY_VALUE - ratio);

		return OPTIMUM_SIMILARITY_VALUE - distance;
	}

	private static double evaluateLength(int newResult, int refResult) {
        if(newResult == 0) {
            return 0.0;
        }
        
		double quality;
		if (newResult < refResult) {
			// +1 to avoid division by zero
			quality = (((double)refResult - newResult + 1) / (refResult + 1));
		} else {
			quality = (((double)newResult - refResult + 1) / (newResult + 1));
		}

		double penalty = 1.0;
		if (newResult < SearchResultEnricherAgent.INFIMUM_GOOD_SEARCH_RESULT) {
			// query has not enough results, so use a penalty
			penalty = (SearchResultEnricherAgent.INFIMUM_GOOD_SEARCH_RESULT - (double)newResult)
					/ SearchResultEnricherAgent.INFIMUM_GOOD_SEARCH_RESULT;
		} else if (newResult > SearchResultEnricherAgent.SUPREMUM_GOOD_SEARCH_RESULT) {
			// query has too many results, so use a penalty
			penalty = ((double)newResult - SearchResultEnricherAgent.INFIMUM_GOOD_SEARCH_RESULT)
					/ newResult;
		}

		return quality * penalty;
	}
}
