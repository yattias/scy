package eu.scy.tools.gstyler.client.plugins.qoc;

import eu.scy.tools.gstyler.client.graph.GWTGraph;
import eu.scy.tools.gstyler.client.graph.application.AbstractGraphPlugin;
import eu.scy.tools.gstyler.client.graph.application.GraphApplication;
import eu.scy.tools.gstyler.client.plugins.CreateEdgeButton;
import eu.scy.tools.gstyler.client.plugins.DeleteEdgeButton;
import eu.scy.tools.gstyler.client.plugins.MoveNodesButton;
import eu.scy.tools.gstyler.client.plugins.ShowExampleButton;
import eu.scy.tools.gstyler.client.plugins.qoc.edges.NegativeEdge;
import eu.scy.tools.gstyler.client.plugins.qoc.edges.PositiveEdge;
import eu.scy.tools.gstyler.client.plugins.qoc.edges.QuestionEdge;
import eu.scy.tools.gstyler.client.plugins.qoc.nodes.CriterionNode;
import eu.scy.tools.gstyler.client.plugins.qoc.nodes.OptionNode;
import eu.scy.tools.gstyler.client.plugins.qoc.nodes.QuestionNode;

public class QOCPlugin extends AbstractGraphPlugin {

    public QOCPlugin(final GraphApplication graphApplication) {
        super(graphApplication);

        getGraph().addNode(new QuestionNode(), 5, 5);
        getGraph().addNode(new OptionNode(), 5, 60);
        getGraph().addNode(new CriterionNode(), 5, 130);

        getActionsPanel().add(new ShowExampleButton(this));
        getActionsPanel().add(new CreateEdgeButton("Create PositiveEdges", this, new PositiveEdge()));
        getActionsPanel().add(new CreateEdgeButton("Create NegativeEdges", this, new NegativeEdge()));
        getActionsPanel().add(new CreateEdgeButton("Create QuestionEdges", this, new QuestionEdge()));
        getActionsPanel().add(new DeleteEdgeButton(this));
        getActionsPanel().add(new MoveNodesButton(this));
    }

    @Override
    public String getName() {
        return "QOC";
    }

    public void addExampleDocument(GWTGraph graph) {
        QuestionNode questionNode = new QuestionNode();
        questionNode.getModel().setQuestion("What to eat?");
        questionNode.getNodeView().updateFromModel();
        OptionNode optionPizza = new OptionNode();
        optionPizza.getModel().setOption("Pizza");
        optionPizza.getNodeView().updateFromModel();
        OptionNode optionFalafel = new OptionNode();
        optionFalafel.getModel().setOption("Falafel");
        optionFalafel.getNodeView().updateFromModel();        
        OptionNode optionKebap = new OptionNode();
        optionKebap.getModel().setOption("Kebap");
        optionKebap.getNodeView().updateFromModel();
        CriterionNode criterionTaste = new CriterionNode();
        criterionTaste.getModel().setCriterion("Taste");
        criterionTaste.getModel().setRelevance(90);
        criterionTaste.getNodeView().updateFromModel();
        CriterionNode criterionVegetarian = new CriterionNode();
        criterionVegetarian.getModel().setCriterion("Vegetarian");
        criterionVegetarian.getNodeView().updateFromModel();
        CriterionNode criterionPrice = new CriterionNode();
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
        graph.addEdge(new QuestionEdge(questionNode, optionFalafel));
        graph.addEdge(new QuestionEdge(questionNode, optionKebap));
        graph.addEdge(new QuestionEdge(questionNode, optionPizza));
        graph.addEdge(new PositiveEdge(optionPizza, criterionTaste));
        graph.addEdge(new NegativeEdge(optionPizza, criterionPrice));
        graph.addEdge(new NegativeEdge(optionKebap, criterionVegetarian));
        graph.addEdge(new PositiveEdge(optionFalafel, criterionTaste));
        graph.addEdge(new PositiveEdge(optionFalafel, criterionVegetarian));
    }
}
