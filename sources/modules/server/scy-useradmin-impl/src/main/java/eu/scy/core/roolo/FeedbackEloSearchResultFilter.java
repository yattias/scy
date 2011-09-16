package eu.scy.core.roolo;

import eu.scy.core.model.transfer.TransferElo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 06.sep.2011
 * Time: 06:18:12
 * To change this template use File | Settings | File Templates.
 */
public class FeedbackEloSearchResultFilter implements Comparator {

    private static Logger log = Logger.getLogger("FeedbackEloSearchResultFilter.class");

    private FeedbackEloSearchFilter feedbackEloSearchFilter;
    private SimpleDateFormat formatter = new SimpleDateFormat("dd.MM HH:mm");

    public List sort(List <TransferElo> elosToBeSorted) {
        Collections.sort(elosToBeSorted, this);
        return elosToBeSorted;
    }


    @Override
    public int compare(Object o1, Object o2) {
        TransferElo first = (TransferElo) o1;
        TransferElo last = (TransferElo) o2;
        if(feedbackEloSearchFilter.getCriteria() != null) {
            if(feedbackEloSearchFilter.getCriteria().equals("NEWEST")){
                return compareOnDate(first, last);
            } else if(feedbackEloSearchFilter.getCriteria().equals("MOST_VIEWED")) {
                return compareOnMostViewed(first,last);
            } else if(feedbackEloSearchFilter.getCriteria().equals("HIGHEST_SCORED")) {
                return compareOnHighestScored(first, last);
            }
            
        }

        //default to newest
        return compareOnDate(first, last);

    }

    private int compareOnHighestScored(TransferElo first, TransferElo last) {
        Integer firstScore = null;
        Integer lastScore = null;
        if(first.getFeedbackEloTransfer().getScore() != null && first.getFeedbackEloTransfer().getScore().length() > 0) {
            firstScore = Integer.parseInt(first.getFeedbackEloTransfer().getScore());
        }
        if(last.getFeedbackEloTransfer().getScore() != null && last.getFeedbackEloTransfer().getScore().length() > 0) {
            lastScore = Integer.parseInt(last.getFeedbackEloTransfer().getScore());
        }

        if(firstScore != null && lastScore != null) {
            if(firstScore > lastScore) return -1;
            else if(firstScore < lastScore) return 1;
            else if(firstScore == lastScore) return 0;

        }
        return 1;
    }

    private int compareOnMostViewed(TransferElo first, TransferElo last) {
        Integer firstShown = new Integer(first.getFeedbackEloTransfer().getShown());
        Integer lastShown = new Integer(last.getFeedbackEloTransfer().getShown());
        if(firstShown > lastShown) return -1;
        else if(firstShown == lastShown) return 0;
        return 1;
    }

    private int compareOnDate(TransferElo first, TransferElo last) {
        try {
            Date firstDate = formatter.parse(first.getCreatedDate());
            Date lastDate = formatter.parse(last.getCreatedDate());
            if(firstDate.after(lastDate)) return -1;
            else if(firstDate.equals(lastDate)) return 0;
            return 1;
        } catch (ParseException e) {
            log.warning(e.getMessage());
            return -1;
        }
    }

    public FeedbackEloSearchFilter getFeedbackEloSearchFilter() {
        return feedbackEloSearchFilter;
    }

    public void setFeedbackEloSearchFilter(FeedbackEloSearchFilter feedbackEloSearchFilter) {
        this.feedbackEloSearchFilter = feedbackEloSearchFilter;
    }

    public List<TransferElo> filter(List<TransferElo> eloList) {
        if(feedbackEloSearchFilter.getOwner() == null ||(feedbackEloSearchFilter.getOwner() != null &&  feedbackEloSearchFilter.getOwner().equalsIgnoreCase("ALL"))) return eloList;
        List <TransferElo> rList = new LinkedList<TransferElo>();
        for (int i = 0; i < eloList.size(); i++) {
            TransferElo transferElo = eloList.get(i);
            if(transferElo.getCreatedBy().equals(feedbackEloSearchFilter.getOwner())) {
                rList.add(transferElo);
            }
        }

        return rList;
    }
}
