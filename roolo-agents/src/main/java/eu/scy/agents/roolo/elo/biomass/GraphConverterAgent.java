package eu.scy.agents.roolo.elo.biomass;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;

import org.jdom.Element;

import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;
import eu.scy.agents.impl.elo.AbstractELOAgent;

public class GraphConverterAgent<T extends IELO<K>, K extends IMetadataKey> extends
AbstractELOAgent<T, K> {
    
    static class Node {
        
        String id;
        String name;
        public int number;
        
    }
    
    private LinkedHashMap<String, Node> nodes;
    private int[] edgeValues;
    private int counter;
    
    public GraphConverterAgent() {
        this.nodes = new LinkedHashMap<String, Node>();
        this.edgeValues = new int[6];
        for (int i = 0; i < this.edgeValues.length; i++) {
            this.edgeValues[i] = -1;
        }
        
    }
    
    public void processElo(T elo) {
    // IMetadataKey allScoreKey = typeManager.getMetadataKey("all_score");
    // if (allScoreKey == null) {
    // IMetadataKey score = new StringMetadataKey("all_score",
    // "/agentdata/biomass/allScore",
    // I18nType.UNIVERSAL, MetadataValueCount.SINGLE, new LongValidator());
    // getMetadataTypeManager().registerMetadataKey(score);
    // allScoreKey = score;
    // }
    //
    // IMetadataKey cappedScoreKey =
    // getMetadataTypeManager().getMetadataKey("capped_score");
    // if (cappedScoreKey == null) {
    // IMetadataKey score = new StringMetadataKey("capped_score",
    // "/agentdata/biomass/cappedScore", I18nType.UNIVERSAL,
    // MetadataValueCount.SINGLE, new LongValidator());
    // getMetadataTypeManager().registerMetadataKey(score);
    // cappedScoreKey = score;
    // }
    //
    // IContent content = elo.getContent();
    // if (content == null) {
    // return;
    // }
    // StringReader reader = new StringReader(content.getXml());
    // SAXBuilder builder = new SAXBuilder();
    // try {
    // Element rootElement = builder.build(reader).getRootElement();
    // if (this.readGraph(rootElement)) {
    // this.calculateScore(elo);
    // }
    // // this.writeArff(elo);
    // } catch (JDOMException e) {
    // e.printStackTrace();
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
        
    }
    
    @SuppressWarnings("unchecked")
    private void calculateScore(T elo) {
    // int score = 0;
    // IMetadataKey allScoreKey =
    // getMetadataTypeManager().getMetadataKey("all_score");
    // IMetadataKey cappedScoreKey =
    // getMetadataTypeManager().getMetadataKey("capped_score");
    //
    // if (this.edgeValues[0] == 1) {
    // score += 1;
    // }
    // if (this.edgeValues[1] == 2) {
    // score += 1;
    // }
    // if (this.edgeValues[2] == 4) {
    // score += 1;
    // }
    // if ((this.edgeValues[3] == 1) || (this.edgeValues[3] == 3)) {
    // score += 1;
    // }
    // if (this.edgeValues[4] == 4) {
    // score += 1;
    // }
    //
    // IMetadataValueContainer scoreContainer =
    // elo.getMetadata().getMetadataValueContainer(
    // (K) allScoreKey);
    // scoreContainer.setValue(score);
    //
    // IMetadataValueContainer cappedScoreContainer =
    // elo.getMetadata().getMetadataValueContainer(
    // (K) cappedScoreKey);
    // cappedScoreContainer.setValue(score > 3 ? 3 : score);
    }
    
    @SuppressWarnings("unchecked")
    private void writeArff(T elo) throws IOException {
        this.counter++;
        File file = new File(System.getProperty("user.home") + "/ude_biomass_graph.arff");
        BufferedWriter writer = null;
        if (!file.exists()) {
            writer = new BufferedWriter(new FileWriter(file));
            writer.write("@relation graph\n");
            writer.write("\n");
            for (String nodeID : this.nodes.keySet()) {
                Node n = this.nodes.get(nodeID);
                writer.write("@attribute " + n.name.replace(' ', '_').toLowerCase()
                        + " {-1,0,1,2,3,4,5}\n");
            }
            writer.write("@attribute file string");
            writer.write("\n");
            writer.write("@data\n");
        } else {
            writer = new BufferedWriter(new FileWriter(file, true));
        }
        System.out.println();
        for (int i = 0; i < this.edgeValues.length; i++) {
            writer.write("" + this.edgeValues[i] + " ");
        }
        
        // String title = (String) elo.getMetadata().getMetadataValueContainer(
        // (K) getMetadataTypeManager().getMetadataKey("title")).getValue();
        // if ("".equals(title.trim())) {
        // writer.write("unknown title");
        // } else {
        // writer.write(title);
        // }
        // writer.write("\t\t\n");
        // writer.close();
    }
    
    @SuppressWarnings( { "cast", "unchecked" })
    private boolean readGraph(Element rootElement) {
        if ((rootElement == null) || (!"DocumentRoot".equals(rootElement.getName()))) {
            return false;
        }
        
        this.nodes.clear();
        this.edgeValues = new int[6];
        for (int i = 0; i < this.edgeValues.length; i++) {
            this.edgeValues[i] = -1;
        }
        
        Element sessionData = rootElement.getChild("SessionData");
        Element workspaces = sessionData.getChild("Workspaces");
        Element workspace = workspaces.getChild("Workspace");
        List<Element> jgraphList = (List<Element>) workspace.getChildren("JGraph");
        
        for (Element jgraph : jgraphList) {
            List<Element> arrays = jgraph.getChildren("ARRAY");
            if (arrays != null) {
                for (Element array : arrays) {
                    List<Element> nodesList = (List<Element>) array.getChildren("NodeInfo");
                    for (Element nodeElement : nodesList) {
                        Element stockNode = nodeElement.getChild("StockNode");
                        if (stockNode != null) {
                            Node node = new Node();
                            node.id = stockNode.getAttributeValue("ID");
                            node.name = stockNode.getAttributeValue("name");
                            node.number = this.getNodeNumber(node);
                            this.nodes.put(node.id, node);
                        }
                    }
                    
                    List<Element> edgesList = (List<Element>) array.getChildren();
                    for (Element graphEdge : edgesList) {
                        if (graphEdge.getName().contains("Edge")) {
                            List<Element> ids = (List<Element>) graphEdge.getChildren("UniqueID");
                            Node inNode = this.nodes.get(ids.get(0).getTextTrim());
                            
                            Node outNode = this.nodes.get(ids.get(1).getTextTrim());
                            
                            int type = Integer.parseInt(graphEdge.getChild("IconType")
                                    .getAttributeValue("type"));
                            if (inNode.number != -1) {
                                this.edgeValues[inNode.number] = type;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
    
    private int getNodeNumber(Node node) {
        if ("Hours of Light".equals(node.name)) {
            return 0;
        } else if ("Carbon Dioxide".equals(node.name)) {
            return 1;
        } else if ("Temperature".equals(node.name)) {
            return 2;
        } else if ("Oxygen".equals(node.name)) {
            return 3;
        } else if ("Water".equals(node.name)) {
            return 4;
        } else if ("Biomass".equals(node.name)) {
            return 5;
        }
        return -1;
    }
}
