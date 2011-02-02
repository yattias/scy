package eu.scy.server.exporter;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import roolo.api.IRepository;
import roolo.elo.api.IELO;

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
}
