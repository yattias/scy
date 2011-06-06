package eu.scy.agents.search.searchresultenricher;

import java.util.LinkedList;
import java.util.List;

import roolo.search.ISearchResult;

/*
 * A ranked list of SearchResults and their quality. First entry provides the best result.
 */
public class SearchResultRanking {

	private static final double WEIGHT_LENGTH = 0.3333;

	private static final double WEIGHT_DIFFERENCE = 0.3333;

	private static final double WEIGHT_RELEVANCE = 0.3333;

	private static final int RELEVANCE_NUMBER = 3;

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
		double quality = evaluateQuality(this.referenceResults, results);
		Result result = new Result(query, results, quality);
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
		return this.ranking.toString();
	}

	private double evaluateQuality(List<ISearchResult> referenceResult,
			List<ISearchResult> newResult) {
		double lengthQuality = evaluateLength(newResult.size(),
				referenceResult.size());
		double similarityQuality = evaluateSimilarity(newResult,
				referenceResult);
		double relevanceQuality = evaluateRelevance(newResult, referenceResult);

		return WEIGHT_LENGTH * lengthQuality + WEIGHT_DIFFERENCE
				* similarityQuality + WEIGHT_RELEVANCE * relevanceQuality;
	}

	private double evaluateRelevance(List<ISearchResult> newResult, List<ISearchResult> referenceResult) {
		// Compare the mean of the RELEVANCE_NUMBER first search results.
		int n = RELEVANCE_NUMBER;
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

	private static int getIdenticalUriCount(List<ISearchResult> a, List<ISearchResult> b) {
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

	private static double evaluateSimilarity(List<ISearchResult> newResult,
			List<ISearchResult> refResult) {
		int identicalUri = getIdenticalUriCount(newResult, refResult);
		int minUri = Math.min(newResult.size(), refResult.size());
		// TODO full similiarity is overweighted...

		// +1 to avoid division by zero
		return ((double)identicalUri + 1) / ((double)minUri + 1);
	}

	private static double evaluateLength(int newResult, int refResult) {
		double quality;
		if (newResult < refResult) {
			// +1 to avoid division by zero
			quality = (((double)refResult - newResult + 1) / ((double)refResult + 1));
		} else {
			quality = (((double)newResult - refResult + 1) / ((double)newResult + 1));
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
