package eu.scy.client.tools.scydynamics.domain;

import java.awt.Color;
import java.awt.Dimension;
import java.util.HashSet;
import java.util.logging.Logger;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import colab.um.draw.JdFigure;
import colab.um.draw.JdFlow;
import colab.um.draw.JdFlowCtr;
import colab.um.draw.JdLink;
import colab.um.draw.JdRelation;
import colab.um.draw.JdStock;
import eu.scy.client.tools.scydynamics.editor.ModelEditor;
import eu.scy.client.tools.scydynamics.logging.ModellingLoggerFES;
import eu.scy.client.tools.scydynamics.store.SCYDynamicsStore.StoreType;

@SuppressWarnings("serial")
public class FeedbackPanel extends JPanel {

	private final static Logger debugLogger = Logger.getLogger(FeedbackPanel.class.getName());
    
	private ModelEditor editor;
	
	private String ROWKEY_CORRECT = "correct";
	private String ROWKEY_INCORRECT = "incorrect";
	private String ROWKEY_NONAME = "no name";
	
	private String COLUMNKEY_VARIABLES = "variables";
	private String COLUMNKEY_RELATIONS = "relations";
	private String COLUMNKEY_DIRECTIONS = "directions";
	
	private String resultAsString = "";

	public FeedbackPanel(ModelEditor editor) {
		this.editor = editor;
		this.add(new JLabel("<html>Click on the play button on the left<br>to get feedback on the variables and relations<br>in your model.</html>"));
	}

	private CategoryDataset createDatasetFake() {
		DefaultCategoryDataset result = new DefaultCategoryDataset();
		result.addValue(9, ROWKEY_CORRECT, COLUMNKEY_VARIABLES);
		result.addValue(1, ROWKEY_INCORRECT, COLUMNKEY_VARIABLES);
		result.addValue(1, ROWKEY_NONAME, COLUMNKEY_VARIABLES);

		result.addValue(8, ROWKEY_CORRECT, COLUMNKEY_RELATIONS);
		result.addValue(1, ROWKEY_INCORRECT, COLUMNKEY_RELATIONS);

		result.addValue(7, ROWKEY_CORRECT, COLUMNKEY_DIRECTIONS);
		result.addValue(1, ROWKEY_INCORRECT, COLUMNKEY_DIRECTIONS);
		
		resultAsString = "";
		return result;
	}
	
	private CategoryDataset createDataset() {
		DefaultCategoryDataset result = new DefaultCategoryDataset();
		
		// the nodes
		//----------
		HashSet<String> correctNodeNames = new HashSet<String>();
		int incorrectNodes = 0;
		int noNameNodes = 0;
		for (String nodeName: editor.getModel().getNodes().keySet()) {
			if (nodeName.startsWith("Stock_")
				||	nodeName.startsWith("Aux_")
				|| nodeName.startsWith("Const_"))
			{
				noNameNodes++;
			} else if (editor.getDomain().proposeNames(nodeName) == null) {
				String conceptType = null;
				try {
					String conceptName = editor.getDomain().getConceptByTerm(nodeName);
					Concept concept = editor.getDomain().getConceptSet().getConcept(conceptName);
					conceptType = concept.getType();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (conceptType != null && conceptType.equalsIgnoreCase("unused")) {
					incorrectNodes++;
				} else {
					correctNodeNames.add(nodeName);
				}
			} else {
				incorrectNodes++;
			}
		}
		result.addValue(correctNodeNames.size(), ROWKEY_CORRECT, COLUMNKEY_VARIABLES);
		resultAsString = correctNodeNames.size()+",";
		result.addValue(incorrectNodes, ROWKEY_INCORRECT, COLUMNKEY_VARIABLES);
		resultAsString = resultAsString.concat(incorrectNodes+",");
		result.addValue(noNameNodes, ROWKEY_NONAME, COLUMNKEY_VARIABLES);
		resultAsString = resultAsString.concat(noNameNodes+",");
		
		// the edges
		//----------
		//System.out.println("the edges: --------------");
		int correctRelations = 0;
		int incorrectRelations = 0;
		int correctDirections = 0;
		int incorrectDirections = 0;
		for (JdLink link: editor.getModel().getLinks()) {
			if (link instanceof JdRelation) {
				if (isRelationInDomain(link.getFigure1(), link.getFigure2())) {
					correctRelations++;
					correctDirections++;
				} else if (isRelationInDomain(link.getFigure2(), link.getFigure1())) {
					correctRelations++;
					incorrectDirections++;
				} else {
					incorrectRelations++;
				}
			}
		}
		
		result.addValue(correctRelations, ROWKEY_CORRECT, COLUMNKEY_RELATIONS);
		resultAsString = resultAsString.concat(correctRelations+",");
		result.addValue(incorrectRelations, ROWKEY_INCORRECT, COLUMNKEY_RELATIONS);
		resultAsString = resultAsString.concat(incorrectRelations+",");
		
		result.addValue(correctDirections, ROWKEY_CORRECT, COLUMNKEY_DIRECTIONS);
		resultAsString = resultAsString.concat(correctDirections+",");
		result.addValue(incorrectDirections, ROWKEY_INCORRECT, COLUMNKEY_DIRECTIONS);
		resultAsString = resultAsString.concat(incorrectDirections+"");
		
		return result;
	}
	
	private boolean isRelationInDomain(JdFigure from, JdFigure to) {
		try {
			if (from == null || to == null) {
				return false;
			}
			
			if (from instanceof JdFlowCtr) {
				//System.out.println("an edge FROM a jdflowctr? this shouldn't happen, returning false.");
				return false;
			} else if (to instanceof JdFlowCtr) {
				JdFlowCtr flowCtr = (JdFlowCtr) to;
				JdStock incomingStock = getIncomingStock(flowCtr);
				JdStock outgoingStock = getOutgoingStock(flowCtr);
				if (incomingStock != null) {
					// situation:
					// "from" is a constant or aux
					// "to" is a flow-ctr that is incoming to a stock
					String incomingConcept = editor.getDomain().getConceptByTerm(incomingStock.getProperties().get("label").toString());
					String incomingId = editor.getDomain().getNodeByConcept(incomingConcept).getId();
					incomingId = incomingId + "_in";
					String nodeConcept = editor.getDomain().getConceptByTerm(from.getProperties().get("label").toString());
					String nodeId = editor.getDomain().getNodeByConcept(nodeConcept).getId();
					return editor.getDomain().getEdgeBetweenNodeIds(nodeId, incomingId)!=null;
				} else if (outgoingStock != null) {
					// situation:
					// "from" is a constant or aux
					// "to" is a flow-ctr that is outgoing from a stock
					String outgoingConcept = editor.getDomain().getConceptByTerm(outgoingStock.getProperties().get("label").toString());
					String outgoingId = editor.getDomain().getNodeByConcept(outgoingConcept).getId();
					outgoingId = outgoingId + "_out";
					String nodeConcept = editor.getDomain().getConceptByTerm(from.getProperties().get("label").toString());
					String nodeId = editor.getDomain().getNodeByConcept(nodeConcept).getId();
					return editor.getDomain().getEdgeBetweenNodeIds(nodeId, outgoingId)!=null;
				} else {
					return false;
				}
			} else {
				// "regular" relation, check with domain
				//System.out.println("***** regular relation, chechking domain.");
				String label1 = from.getProperties().get("label").toString();
				String label2 = to.getProperties().get("label").toString();
				return editor.getDomain().getEdgeBetweenNodeTerms(label1, label2) != null;
			}
		} catch (Exception e) {
			return false;
		}
	}
	
	private JdStock getIncomingStock(JdFlowCtr flowCtr) {
		for (JdStock stock: editor.getModel().getStocks()) {
			if (((JdFlow) (flowCtr.getParent())).getFigure2() == stock) {
				return stock;
			}
		}
		return null;
	}
	
	private JdStock getOutgoingStock(JdFlowCtr flowCtr) {
		for (JdStock stock: editor.getModel().getStocks()) {
			if (((JdFlow) (flowCtr.getParent())).getFigure1() == stock) {
				return stock;
			}
		}
		return null;
	}
	
	public void update() {
		CategoryDataset data;
		if (editor.getDomain() == null) {
			data = createDatasetFake();
		} else {
			data = createDataset();
		}
		
		JFreeChart chart = ChartFactory.createStackedBarChart(
				"", // chart title
				"", // domain axis label
				"", // range axis label
				data, // data
				PlotOrientation.VERTICAL, // plot orientation
				true, // legend
				true, // tooltips
				false); // urls
		chart.setBackgroundPaint(this.getBackground());
		
		StackedBarRenderer renderer = new StackedBarRenderer();
		StandardBarPainter barPainter = new StandardBarPainter();
		renderer.setMaximumBarWidth(.1);
		renderer.setBarPainter(barPainter);
		renderer.setSeriesPaint(0, Color.GREEN);
		renderer.setSeriesPaint(1, Color.RED);
		renderer.setSeriesPaint(2, Color.YELLOW);
		CategoryPlot plot = (CategoryPlot) chart.getPlot();
		plot.setRenderer(renderer);
		
		
		final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new Dimension(600,500));
		this.removeAll();
		this.add(chartPanel);
		this.repaint();
		editor.getActionLogger().logFeedbackRequested(editor.getModelXML(), resultAsString);
		editor.doAutosave(StoreType.ON_FEEDBACK);
	}

}
