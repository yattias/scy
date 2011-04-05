package eu.scy.server.exporter;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import roolo.api.IRepository;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataTypeManager;

public class RetrieveElos {

    private static final ConfigLoader configLoader = ConfigLoader.getInstance();

    public String getXmlELO(URI uri) {
        IELO elo = null;
        IRepository repository = configLoader.getRepository();
        elo = repository.retrieveELO(uri);
        if (elo == null) {
            return null;
        }
        String xmlString = elo.getXml();
        return xmlString;
    }

    public String[] getXmlELOs(URI[] uris) {
        List<String> elos = new ArrayList<String>();
        for (URI uri : uris) {
            String xmlELO = getXmlELO(uri);
            if (xmlELO != null) {
                elos.add(xmlELO);
            }
        }
        return (elos.toArray(new String[elos.size()]));

    }
    public static void main(String[] args) throws URISyntaxException {
		IRepository repository = configLoader.getRepository();
		IMetadataTypeManager typeManager = configLoader.getTypeManager();
	IELO elo = repository.retrieveELO(new URI("roolo://scy.collide.info/scy-collide-server/66159.66159#0"));
	//List<IELO> retrieveAllELOs = repository.retrieveAllELOs();
    String xml = elo.getMetadata().getXml();
//    for (IELO ielo : retrieveAllELOs) {
//		System.out.println(elo.getUri());
//	}
    System.out.println(xml);

//    RooloServices rs;
//    ScyElo portfolioElo = ScyElo.loadLastVersionElo(new URI("roolo://scy.collide.info/scy-collide-server/66159.66159#0"), getMissionELOService());
//    //portfolioElo.loadMetadata(portfolioElo.getElo().getUri(), getMissionELOService());
//    portfolioElo.getContent().setXmlString(xmlContent);
//    portfolioElo.updateElo();
    }
}
