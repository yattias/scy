package eu.scy.core.roolo;

import eu.scy.core.BaseELOService;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 14.des.2010
 * Time: 06:34:34
 * To change this template use File | Settings | File Templates.
 */
public interface RuntimeELOService extends BaseELOService {

        List getRuntimeElosForUser(String userName);

}