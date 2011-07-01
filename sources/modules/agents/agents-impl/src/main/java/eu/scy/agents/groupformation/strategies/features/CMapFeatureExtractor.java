package eu.scy.agents.groupformation.strategies.features;

import info.collide.sqlspaces.client.TupleSpace;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jaxen.JaxenException;
import org.jdom.JDOMException;

import roolo.api.IRepository;
import roolo.elo.api.IContent;
import roolo.elo.api.IELO;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import roolo.search.ISearchResult;
import roolo.search.MetadataQueryComponent;
import roolo.search.Query;
import de.fhg.iais.kd.tm.elo.CmapImporter;
import de.fhg.iais.kd.tm.graphmatching.editdistance.EditCostFunction;
import de.fhg.iais.kd.tm.graphmatching.editdistance.EditDistanceApproximator;
import de.fhg.iais.kd.tm.graphmatching.graph.Edge;
import de.fhg.iais.kd.tm.graphmatching.graph.Graph;
import de.fhg.iais.kd.tm.graphmatching.graph.Vertex;

public class CMapFeatureExtractor implements FeatureExtractor {

	private TupleSpace commandSpace;
	private IRepository repository;
	
    private IELO retrieveEloFromRepository(String user){
        MetadataQueryComponent mcq = new MetadataQueryComponent(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT.getId(),"scy/mapping");
        Query q = new Query(mcq);
        HashSet <String> allowedUsers = new HashSet <String>();
        allowedUsers.add(user);
        q.setAllowedUsers(allowedUsers);
        List<ISearchResult> res = repository.search(q);
        IELO elo = repository.retrieveELO(res.get(0).getUri());
        return elo;
    }

	@Override
	public Map<String, double[]> getFeatures(Set<String> availableUsers, String mission, IELO elo) {
		Map<String, double[]> results = new LinkedHashMap<String, double[]>();
		for (String user : availableUsers) {
		    
			results.put(user, this.getCMapFeatures(user, mission, elo, retrieveEloFromRepository(user)));
		}
		return results;
	}

	public double[] getCMapFeatures(String user, String mission, IELO referenceElo, IELO userElo) {
//	    TODO: extract conversion of reference ELO to graph to calling function to speed up the computation
		double refDist = 0.0, nOfNodes = 0.0, nOfLinks = 0.0, nOfLabels = 0.0;
		try {
			IContent content = referenceElo.getContent();
			String contentText = content.getXmlString(); // TODO: find out why imageData tag is
															// sometimes not accepted by
															// org.jaxen.XPath
															// theory 1: IELO.getXml does not work here
															// (why?)
			// contentText = contentText.replaceAll("<imageData>.*</imageData>", "");
			Graph rg = CmapImporter.convertCmap(contentText);

			content = userElo.getContent();
			contentText = content.getXmlString();
			// contentText = contentText.replaceAll("<imageData>.*</imageData>",
			// "<imageData> </imageData>\n");
			Graph g = CmapImporter.convertCmap(contentText);
			EditCostFunction cost = EditCostFunction.simpleEditCost;

//			exact calculation of the edit distance is too slow!
//			refDist = EditDistanceCalculator.calcDistance(rg, g, cost);
            refDist = EditDistanceApproximator.calcDistance(rg, g, cost);
			nOfNodes = (double) g.getVertexes().size();
			nOfLinks = (double) g.getEdges().size();

			// count non-empty labels
			nOfLabels = (double) this.countLabels(g);
		} catch (JaxenException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new double[] { refDist, nOfNodes, nOfLinks, nOfLabels };
	}

	/**
	 * @param g count number of non-empty edge and vertex labels
	 */
	private int countLabels(Graph g) {
		int numOfLabels = 0;
		// process vertices to extract labels
		Collection<Vertex> v = g.getVertexes();
		Iterator<Vertex> vIt = v.iterator();
		while (vIt.hasNext()) {
			Vertex vx = (Vertex) vIt.next();
			if (!vx.getLabel().matches("")) {
				numOfLabels++;
			}
		}
		// process edges to extract labels
		Collection<Edge> e = g.getEdges();
		Iterator<Edge> eIt = e.iterator();
		while (eIt.hasNext()) {
			Edge ex = (Edge) eIt.next();
			if (!ex.getLabel().matches("")) {
				numOfLabels++;
			}
		}
		return numOfLabels;
	}

	@Override
	public TupleSpace getCommandSpace() {
		return this.commandSpace;
	}

	@Override
	public void setCommandSpace(TupleSpace commandSpace) {
		this.commandSpace = commandSpace;
	}

	@Override
	public boolean canRun(IELO elo) {
		return true;
	}

    @Override
    public void setRepository(IRepository repository) {
        this.repository = repository;      
    }

}
