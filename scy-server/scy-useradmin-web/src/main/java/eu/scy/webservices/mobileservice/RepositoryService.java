package eu.scy.webservices.mobileservice;

import javax.jws.WebService;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 24.mar.2009
 * Time: 10:45:45
 * To change this template use File | Settings | File Templates.
 */

@WebService
public class RepositoryService {

    private static Logger log = Logger.getLogger("RepositoryService.class");

    public void doTheBartMan(String bartManString) {
        log.info("KICKING OFF WITH THIS FUNKY MESSAGE: " + bartManString);
    }

}
