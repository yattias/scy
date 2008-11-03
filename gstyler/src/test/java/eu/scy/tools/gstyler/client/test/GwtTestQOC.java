package eu.scy.tools.gstyler.client.test;

import com.google.gwt.junit.client.GWTTestCase;

import eu.scy.tools.gstyler.client.graph.GWTGraph;
import eu.scy.tools.gstyler.client.graph.edge.Edge;
import eu.scy.tools.gstyler.client.plugins.qoc.edges.NegativeEdge;
import eu.scy.tools.gstyler.client.plugins.qoc.edges.PositiveEdge;
import eu.scy.tools.gstyler.client.plugins.qoc.edges.QuestionEdge;
import eu.scy.tools.gstyler.client.plugins.qoc.nodes.CriterionNode;
import eu.scy.tools.gstyler.client.plugins.qoc.nodes.OptionNode;
import eu.scy.tools.gstyler.client.plugins.qoc.nodes.QuestionNode;

public class GwtTestQOC extends GWTTestCase {

    private GWTGraph graph;

    private QuestionNode questionNode;

    private OptionNode optionPizza;

    private OptionNode optionFalafel;

    private OptionNode optionKebap;

    private CriterionNode criterionTaste;

    private CriterionNode criterionVegetarian;

    private CriterionNode criterionPrice;

    @Override
    public String getModuleName() {
        return "eu.scy.tools.gstyler.GStyler";
    }

    public void gwtSetUp() {
        graph = new GWTGraph();
        questionNode = new QuestionNode();
        questionNode.getModel().setQuestion("What to eat?");
        questionNode.getNodeView().updateFromModel();
        optionPizza = new OptionNode();
        optionPizza.getModel().setOption("Pizza");
        optionPizza.getNodeView().updateFromModel();
        optionFalafel = new OptionNode();
        optionFalafel.getModel().setOption("Falafel");
        optionFalafel.getNodeView().updateFromModel();
        optionKebap = new OptionNode();
        optionKebap.getModel().setOption("Kebap");
        optionKebap.getNodeView().updateFromModel();
        criterionTaste = new CriterionNode();
        criterionTaste.getModel().setCriterion("Taste");
        criterionTaste.getModel().setRelevance(90);
        criterionTaste.getNodeView().updateFromModel();
        criterionVegetarian = new CriterionNode();
        criterionVegetarian.getModel().setCriterion("Vegetarian");
        criterionVegetarian.getNodeView().updateFromModel();
        criterionPrice = new CriterionNode();
        criterionPrice.getModel().setCriterion("Price");
        criterionPrice.getModel().setRelevance(80);
        criterionPrice.getNodeView().updateFromModel();
        graph.addNode(questionNode, 400, 10);
        graph.addNode(optionPizza, 100, 120);
        graph.addNode(optionFalafel, 400, 120);
        graph.addNode(optionKebap, 700, 120);
        graph.addNode(criterionTaste, 50, 300);
        graph.addNode(criterionVegetarian, 300, 300);
        graph.addNode(criterionPrice, 600, 300);
        graph.addEdge(new QuestionEdge(optionFalafel, questionNode));
        graph.addEdge(new QuestionEdge(optionKebap, questionNode));
        graph.addEdge(new QuestionEdge(optionPizza, questionNode));
        graph.addEdge(new PositiveEdge(criterionTaste, optionPizza));
        graph.addEdge(new NegativeEdge(criterionPrice, optionPizza));
        graph.addEdge(new NegativeEdge(criterionVegetarian, optionKebap));
        graph.addEdge(new PositiveEdge(criterionTaste, optionFalafel));
        graph.addEdge(new PositiveEdge(criterionVegetarian, optionFalafel));
    }
    
    public void testRemoveNode() {
        assertEquals("BEST OPTION", optionFalafel.getModel().getTitle());
        graph.removeNode(optionFalafel);
        assertEquals("BEST OPTION", optionPizza.getModel().getTitle());
    }
    
    public void testAddEvaluationEdge() {
        int score = optionKebap.getModel().getScore();
        graph.addEdge(new PositiveEdge(criterionTaste, optionKebap));
        assertEquals(optionKebap.getModel().getScore(), score+criterionTaste.getModel().getRelevance());
    }
    
    public void testChangeRelevance() {
        int score = optionKebap.getModel().getScore();
        criterionVegetarian.setRelevance(criterionVegetarian.getModel().getRelevance()-20);
        assertEquals(optionKebap.getModel().getScore(), score+20);
    }
    
    public void testRemoveEvalutationEdge() {
        int score = optionFalafel.getModel().getScore() - criterionVegetarian.getModel().getRelevance();
        Edge e = graph.getEdge(optionFalafel, criterionVegetarian);
        graph.removeEdge(e);
        assertEquals(optionFalafel.getModel().getScore(), score);
    }
}
