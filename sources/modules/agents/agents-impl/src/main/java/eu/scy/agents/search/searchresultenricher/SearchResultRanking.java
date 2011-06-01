package eu.scy.agents.search.searchresultenricher;

import java.util.LinkedList;
import java.util.List;

import roolo.search.ISearchResult;

/*
 * A ranked list of SearchResults and their quality. First entry provides the best result.
 */
public class SearchResultRanking {

	private static final double WEIGHT_LENGTH = 0.5;
	
	private static final double WEIGHT_DIFFERENCE = 0.5;

	
	private List<Result> ranking;
	
	private final int capacity;
	
	private List<ISearchResult> referenceResults;
	
	public SearchResultRanking(int capacity, List<ISearchResult> referenceResults) {
		this.capacity = capacity;
		this.ranking = new LinkedList<Result>();
		this.referenceResults = referenceResults;
	}
	
	/**
	 * Adds the search result in the ranking. 
	 * @param query
	 * @param results
	 */
	public void add(String query, List<ISearchResult> results) {
		double quality = evaluateQuality(this.referenceResults, results);
		Result result = new Result(query, results, quality);
		int index = -1;
		for(int i = 0; i < this.ranking.size(); i++) {
			if(result.compareTo(this.ranking.get(i)) >= 0) {
				index = i;
				break;
			}
		}
		if(index > -1) {
			this.ranking.add(index, result);
			if(this.ranking.size() > this.capacity) {
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

	private double evaluateQuality(List<ISearchResult> referenceResult, List<ISearchResult> newResult) {
		double lengthQuality = evaluateLength(newResult.size(), referenceResult.size());
		double differenceQuality = evaluateDifference(newResult, referenceResult);

		return WEIGHT_LENGTH * lengthQuality + WEIGHT_DIFFERENCE * differenceQuality;
	}
	
	private static int getIdenticalUriCount(List<ISearchResult> a, List<ISearchResult> b) {
		// get the number of identical eloUris
		int identicalResults = 0;
		for(ISearchResult bRes : b){
			for(ISearchResult aRes : a){
				if(bRes.getUri().equals(aRes.getUri())) {
					identicalResults++;
					break;
				}
			}
		}
		return identicalResults;
	}
	
	private static double evaluateDifference(List<ISearchResult> newResult, List<ISearchResult> refResult) {
		int identicalUri = getIdenticalUriCount(newResult, refResult);
		int minUri = Math.min(newResult.size(), refResult.size());
		// TODO total similiarity is overweighted... 
		return identicalUri / minUri;
	}
	
	private static double evaluateLength(int newResult, int refResult) {
		double quality;
		if(newResult < refResult) {
			// +1 to avoid division by zero
			quality = ((refResult - newResult + 1) / (refResult + 1));			
		} else {
			quality = ((newResult - refResult + 1) / (newResult + 1));
		}

		double penalty = 1.0;
		if(newResult < SearchResultEnricherAgent.INFIMUM_GOOD_SEARCH_RESULT) {
			// query has not enough results, so use a penalty 
			penalty = (SearchResultEnricherAgent.INFIMUM_GOOD_SEARCH_RESULT - newResult) / SearchResultEnricherAgent.INFIMUM_GOOD_SEARCH_RESULT;
		} else if(newResult > SearchResultEnricherAgent.SUPREMUM_GOOD_SEARCH_RESULT ) {
			// query has too many results, so use a penalty 
			penalty = (newResult - SearchResultEnricherAgent.INFIMUM_GOOD_SEARCH_RESULT) / newResult;
		}
			

//		if(newResult < SearchResultEnricherAgent.INFIMUM_GOOD_SEARCH_RESULT) {
//			// query has not enough results, so use a penalty 
//			penalty = (SearchResultEnricherAgent.INFIMUM_GOOD_SEARCH_RESULT - newResult) / SearchResultEnricherAgent.INFIMUM_GOOD_SEARCH_RESULT;
//
//			if(newResult < refResult) {
//				// ... and less than the reference - bad (+1 to avoid division by zero)
//				quality = ((refResult - newResult + 1) / (refResult + 1));
//			} else
//				// ... but more than the reference - good (+1 to avoid division by zero)
//				quality = ((newResult - refResult + 1) / (newResult + 1));
//			}
//			
//		} else if(newResult > SearchResultEnricherAgent.SUPREMUM_GOOD_SEARCH_RESULT ) {
//			// query has too many results, so use a penalty 
//			penalty = (newResult - SearchResultEnricherAgent.INFIMUM_GOOD_SEARCH_RESULT) / newResult;
//			
//			if(newResult > refResult) {
//				// ... and more than the reference - bad
//				quality -= ((newResult - refResult) / (newResult));
//			} else if(newResult > refResult) {
//				// ... but less than the reference - good
//				quality += ((refResult - newResult) / (refResult));
//			}
//		} 
//		else {
//			// result number is ok
//
//			if(newResult > refResult) {
//				// ... and more than the reference - good
//				quality += ((newResult - refResult) / (newResult));
//			} else if(newResult > refResult) {
//				// ... but less than the reference - bad
//				quality -= ((refResult - newResult) / (refResult));
//			}
//		}
		return quality * penalty;
	}	
}
