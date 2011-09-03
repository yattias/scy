package eu.scy.core.model.transfer;

import roolo.search.ISearchResult;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 31.aug.2011
 * Time: 16:16:23
 * To change this template use File | Settings | File Templates.
 */
public class SearchResultTransfer {

    private ISearchResult searchResult;
    private Locale locale;
    private Boolean finished;

    private String title;
    private String uri;


    public SearchResultTransfer(Locale locale, ISearchResult iSearchResult) {
        this.locale = locale;
        this.searchResult = iSearchResult;
    }

    public ISearchResult getSearchResult() {
        return searchResult;
    }

    public void setSearchResult(ISearchResult searchResult) {
        this.searchResult = searchResult;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getTitle() {
        String title =  searchResult.getTitle(getLocale());
        if(title == null) {
            Collection coll = searchResult.getTitles().values();
            if(coll.size() > 0) {
                title = (String) coll.iterator().next();
            }

        }

        if(title != null) {
            return title;
        }

        return "NO_TITLE_FOUND";
    }

    public String getUri() {
        URI uri = searchResult.getUri();
        try {
            return URLEncoder.encode(uri.toString(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return searchResult.getUri().toString();
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getThumbnail() {
        return "/webapp/components/resourceservice.html?eloURI=" + getUri();
    }

    public String getDate() {
        long lastModified = getSearchResult().getDateLastModified();
        Date d = new Date(lastModified);
        return d.toString();
    }


    public String getAuthors() {
        String returnValue = "";
        List<String> authors = searchResult.getAuthors();
        for (int i = 0; i < authors.size(); i++) {
            String s = authors.get(i);
            returnValue+=" " + s;
        }
        return returnValue;
    }

    public Boolean getEloFinished() {
        return getSearchResult().isEloFinished();
    }
}
