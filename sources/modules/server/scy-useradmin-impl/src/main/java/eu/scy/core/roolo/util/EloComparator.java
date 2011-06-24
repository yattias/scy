package eu.scy.core.roolo.util;

import eu.scy.common.scyelo.ScyElo;
import eu.scy.core.model.transfer.TransferElo;

import java.sql.Time;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 13.jun.2011
 * Time: 16:49:57
 * To change this template use File | Settings | File Templates.
 */
public class EloComparator implements Comparator {
    @Override
    public int compare(Object o1, Object o2) {
        ScyElo elo1 = (ScyElo) o1;
        ScyElo elo2 = (ScyElo) o2;

        int returnValue = 1;

        Date date1 = new Date(elo1.getDateLastModified());
        Date date2 =  new Date(elo2.getDateLastModified());

        return date1.compareTo(date2);
    }
}
