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
import eu.scy.client.tools.scydynamics.model.Model;
import eu.scy.client.tools.scydynamics.store.SCYDynamicsStore.StoreType;

@SuppressWarnings("serial")
public class FeedbackPanel extends JPanel {

	private final static Logger debugLogger = Logger.getLogger(FeedbackPanel.class.getName());
    
	private ModelEditor modelEditor;
	
	private String ROWKEY_CORRECT = "correct";
	private String ROWKEY_INCORRECT = "incorrect";
	private String ROWKEY_NONAME = "no name";
	
	private String COLUMNKEY_VARIABLES = "variables";
	private String COLUMNKEY_RELATIONS = "relations";
	private String COLUMNKEY_DIRECTIONS = "directions";
		
	public FeedbackPanel(ModelEditor modelEditor) {
		this.modelEditor = modelEditor;
		this.add(new JLabel("<html>Click on the play button on the left<br>to get feedback on the variables and relations<br>in your model.</html>"));
	}

	private CategoryDataset createDatasetFake() {
		DefaultCategoryDataset result = new DefaultCategoryDataset();
		result.addValue(1, ROWKEY_CORRECT, COLUMNKEY_VARIABLES);
		result.addValue(2, ROWKEY_INCORRECT, COLUMNKEY_VARIABLES);
		result.addValue(3, ROWKEY_NONAME, COLUMNKEY_VARIABLES);
		result.addValue(4, ROWKEY_CORRECT, COLUMNKEY_RELATIONS);
		result.addValue(5, ROWKEY_INCORRECT, COLUMNKEY_RELATIONS);
		result.addValue(6, ROWKEY_CORRECT, COLUMNKEY_DIRECTIONS);
		result.addValue(7, ROWKEY_INCORRECT, COLUMNKEY_DIRECTIONS);		
		return result;
	}
	
	private CategoryDataset createDataset(Model model, Domain domain) {
		Feedback feedback = new Feedback(model, domain);
		DefaultCategoryDataset result = new DefaultCategoryDataset();		
		result.addValue(feedback.getCorrectNames(), ROWKEY_CORRECT, COLUMNKEY_VARIABLES);
		result.addValue(feedback.getIncorrectNames(), ROWKEY_INCORRECT, COLUMNKEY_VARIABLES);
		result.addValue(feedback.getUnnamed(), ROWKEY_NONAME, COLUMNKEY_VARIABLES);				
		result.addValue(feedback.getCorrectRelations(), ROWKEY_CORRECT, COLUMNKEY_RELATIONS);
		result.addValue(feedback.getIncorrectRelations(), ROWKEY_INCORRECT, COLUMNKEY_RELATIONS);
		result.addValue(feedback.getCorrectDirections(), ROWKEY_CORRECT, COLUMNKEY_DIRECTIONS);
		result.addValue(feedback.getIncorrectDirections(), ROWKEY_INCORRECT, COLUMNKEY_DIRECTIONS);
		modelEditor.getActionLogger().logFeedbackRequested(modelEditor.getModelXML(), feedback.toString());
		modelEditor.doAutosave(StoreType.ON_FEEDBACK);
		return result;
	}
		
	public void update() {
		CategoryDataset data;
		if (modelEditor.getDomain() == null) {
			data = createDatasetFake();
		} else {
			data = createDataset(modelEditor.getModel(), modelEditor.getDomain());
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

	}

}
