package eu.scy.mobile.toolbroker;

import eu.scy.mobile.toolbroker.model.IELO;

/**
 * Created by IntelliJ IDEA.
 * User: Bjørge Næss
 * Date: 02.mar.2009
 * Time: 11:22:38
  */
public interface IELOService {
    IELO getELO(int i);
    void updateELO(IELO elo);
}
