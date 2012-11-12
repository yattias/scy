package eu.scy.client.tools.scydynamics.logging.parser.actiongraph;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;
import eu.scy.client.tools.scydynamics.logging.parser.actiongraph.ActionSequenceMatrix.ActionType;

@SuppressWarnings("serial")
public class ActionGraph extends JPanel {
	
	private double sum;

	public ActionGraph(double[][] matrix) {
		super();
		this.sum = ActionSequenceMatrix.getSum(matrix);
		this.setLayout(new BorderLayout());
		this.setBackground(Color.WHITE);
		this.add(createGraph(matrix), BorderLayout.CENTER);
	}

	private Component createGraph(double[][] matrix) {
		Graph<String, ActionEdge> graph = new SparseMultigraph<String, ActionEdge>();
		addVertices(graph, matrix);
		addEdges(graph, matrix);
		Layout<String, ActionEdge> layout = new CircleLayout(graph);
		layout.setSize(new Dimension(750,750));
		BasicVisualizationServer<String, ActionEdge> visualizationServer = new BasicVisualizationServer<String, ActionEdge>(layout);
		visualizationServer.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
		visualizationServer.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);
		visualizationServer.getRenderContext().setEdgeStrokeTransformer(edgeStrokeTransformer);
		visualizationServer.getRenderContext().setEdgeDrawPaintTransformer(edgeDrawPaintTransformer);
		visualizationServer.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
		visualizationServer.setPreferredSize(new Dimension(750,750));
		visualizationServer.setBackground(Color.WHITE);
		return visualizationServer;
	}

	private void addVertices(Graph<String, ActionEdge> graph, double[][] matrix) {
		for (int i=0; i<matrix.length; i++) {
			graph.addVertex(ActionSequenceMatrix.ActionType.values()[i].toString());
		}	
	}

	private void addEdges(Graph<String, ActionEdge> graph, double[][] matrix) {
		for (int row=0; row<matrix.length; row++) {
			for (int column=0; column<matrix.length; column++) {
				double value = matrix[row][column];
				if (value != 0) {
					String from = ActionSequenceMatrix.ActionType.values()[row].toString();
					String to = ActionSequenceMatrix.ActionType.values()[column].toString();
					graph.addEdge(new ActionEdge(value, sum), from ,to, EdgeType.DIRECTED);
				}
			}
		}	
	}
	
	private Transformer<ActionEdge, Stroke> edgeStrokeTransformer = 
            new Transformer<ActionEdge, Stroke>() {
          public Stroke transform(ActionEdge edge) {
        	  Stroke edgeStroke = new BasicStroke((float) edge.getThickness());
              return edgeStroke;
          }
	};
	
	private Transformer<ActionEdge, Paint> edgeDrawPaintTransformer = 
            new Transformer<ActionEdge, Paint>() {
          public Paint transform(ActionEdge edge) {
        	  Paint edgePaint = new Color(255,0,0,128);
              return edgePaint;
          }
	};
	
}
