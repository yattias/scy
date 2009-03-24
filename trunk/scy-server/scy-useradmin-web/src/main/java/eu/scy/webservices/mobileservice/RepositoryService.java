package eu.scy.webservices.mobileservice;

import roolo.api.IRepository;
import roolo.cms.repository.mock.MockRepository;
import roolo.elo.api.IELO;

import javax.jws.WebService;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.XmlTransient;
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

    @XmlTransient
    private IRepository repository;

    public void doTheBartMan(String bartManString) {
        log.info("KICKING OFF WITH THIS FUNKY MESSAGE: " + bartManString);
        try {
            IELO elo = null;//new HyperELO(bartManString);
            repository.addELO(elo);
        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    @XmlTransient 
    public void setRepository(MockRepository repository) {
        this.repository = repository;
    }
}
