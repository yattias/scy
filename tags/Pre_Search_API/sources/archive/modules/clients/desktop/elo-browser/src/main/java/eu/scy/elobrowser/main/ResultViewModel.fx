/*
 * ResultViewModel.fx
 *
 * Created on 7-okt-2008, 14:39:54
 */

package eu.scy.elobrowser.main;

import eu.scy.elobrowser.main.CombinedSearchResult;
import java.lang.*;
import java.net.*;
import java.util.*;
import roolo.api.search.ISearchResult;

/**
 * @author sikken
 */

public class ResultViewModel {
   public var combinedSearchResults:CombinedSearchResult[];
   //   attribute nrOfElos = 20;
   //
   //   public function initialize()
   //   {
   //       for (i in [1..nrOfElos])
   //       {
   //	  var combinedSearchResult = CombinedSearchResult{};
   //	  insert combinedSearchResult into combinedSearchResults;
   //       }
   //   }

   
   public function newSearchResults(xSsearchResults:List,ySsearchResults:List,zSsearchResults:List) :CombinedSearchResult[] {
      var newCombinedSearchResults:CombinedSearchResult[];
      var processedUris = new HashSet();
      // process x results
      var iterator = xSsearchResults.iterator();
      while (
      iterator.hasNext()) {
         var searchResult =
         iterator.next() as ISearchResult;
         if (not processedUris.contains(searchResult.getUri()))
         {
            var combinedSearchResult = CombinedSearchResult
	      {
               uri: searchResult.getUri();
               relevanceX: searchResult.getRelevance();
               relevanceY: getSearchResultRelevance(ySsearchResults,searchResult.getUri());
               relevanceZ: getSearchResultRelevance(zSsearchResults,searchResult.getUri());
	      };
	      insert combinedSearchResult into newCombinedSearchResults;
	      processedUris.add(searchResult.getUri());
         }
      }
      // process  y results
      iterator = ySsearchResults.iterator();
      while (iterator.hasNext()) {
         var searchResult = iterator.next() as ISearchResult;
         if (not processedUris.contains(searchResult.getUri()))
         {
            var combinedSearchResult = CombinedSearchResult
	      {
               uri: searchResult.getUri();
               relevanceX: getSearchResultRelevance(xSsearchResults,searchResult.getUri());
               relevanceY: searchResult.getRelevance();
               relevanceZ: getSearchResultRelevance(zSsearchResults,searchResult.getUri());
	      };
	      insert combinedSearchResult into newCombinedSearchResults;
	      processedUris.add(searchResult.getUri());
         }
      }
      // process  z results
      iterator = zSsearchResults.iterator();
      while (iterator.hasNext()) {
         var searchResult = iterator.next() as ISearchResult;
         if (not processedUris.contains(searchResult.getUri()))
         {
            var combinedSearchResult = CombinedSearchResult
	      {
               uri: searchResult.getUri();
               relevanceX: getSearchResultRelevance(xSsearchResults,searchResult.getUri());
               relevanceY: getSearchResultRelevance(ySsearchResults,searchResult.getUri());
               relevanceZ: searchResult.getRelevance();
	      };
	      insert combinedSearchResult into newCombinedSearchResults;
	      processedUris.add(searchResult.getUri());
         }
      }
      System.out.println("Processed search results, nr of elos {newCombinedSearchResults.size()}");
      combinedSearchResults = newCombinedSearchResults;
      System.out.println("Processed search results, nr of elos {combinedSearchResults.size()}");
      return newCombinedSearchResults;
   }
   
   function getSearchResultRelevance(searchResults:List,uri:URI):Number {
      var iterator = searchResults.iterator();
      while (iterator.hasNext()) {
         var searchResult = iterator.next() as ISearchResult;
         if (uri.equals(searchResult.getUri()))
         {
            return searchResult.getRelevance();
         }
      }
      return 0.0;
   }
}
