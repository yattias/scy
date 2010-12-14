package eu.scy.core.roolo;

import eu.scy.common.scyelo.ScyElo;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 26.okt.2010
 * Time: 07:12:01
 * To change this template use File | Settings | File Templates.
 */
public class ELOWebSafeTransporter {

    private ScyElo elo;

    public ELOWebSafeTransporter(ScyElo scyElo) {
        this.elo = scyElo;
    }

    public String getUri() {
        try {
            return URLEncoder.encode(String.valueOf(elo.getUri()), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("cannot get URI");
    }

    public ScyElo getElo() {
        return elo;
    }

    public void setElo(ScyElo elo) {
        this.elo = elo;
    }
}
