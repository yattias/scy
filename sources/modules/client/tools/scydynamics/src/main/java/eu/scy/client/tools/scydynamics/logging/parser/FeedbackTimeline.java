package eu.scy.client.tools.scydynamics.logging.parser;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Stroke;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYDotRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.util.ShapeUtilities;

import colab.um.xml.model.JxmModel;

import eu.scy.actionlogging.api.IAction;
import eu.scy.client.tools.scydynamics.domain.Feedback;
import eu.scy.client.tools.scydynamics.logging.ModellingLogger;
import eu.scy.client.tools.scydynamics.logging.ModellingLoggerFES;
import eu.scy.client.tools.scydynamics.model.Model;

@SuppressWarnings("serial")
public class FeedbackTimeline extends JPanel {

	private float[][] patterns = {{10.0f},{10.0f,10.0f},{5.0f,20.0f}};
	private JFreeChart chart;
	private ChartPanel chartPanel;
	private UserModel userModel;
	private XYSeriesCollection feedbackDataset;
	private XYSeriesCollection modelRanDataset;

	public FeedbackTimeline(UserModel userModel) {
		super();
		this.userModel = userModel;
		feedbackDataset = createFeedbackDataset();
		modelRanDataset = createModelRanDataset();
        chart = createChart();
        chartPanel = new ChartPanel(chart);
        chartPanel.setMouseZoomable(true, false);
        this.setLayout(new BorderLayout());
        this.add(chartPanel, BorderLayout.CENTER);
	}
	
	private JFreeChart createChart() {
		XYPlot xyPlot = new XYPlot();
		XYLineAndShapeRenderer feedbackRenderer = new XYLineAndShapeRenderer(true, false);
		ValueAxis timeAxis = new DateAxis("time");
		timeAxis.setLowerMargin(0.02D);
		timeAxis.setUpperMargin(0.02D);
		NumberAxis valueAxis = new NumberAxis("feedback");
		valueAxis.setAutoRangeIncludesZero(false);
		xyPlot.setDomainAxis(timeAxis);
		xyPlot.setRangeAxis(valueAxis);
		
		xyPlot.setDataset(0, feedbackDataset);
		xyPlot.setRenderer(0, feedbackRenderer);
		
		feedbackRenderer.setDrawSeriesLineAsPath(true);
		// correct variables
		feedbackRenderer.setSeriesPaint(0,Color.GREEN);
		feedbackRenderer.setSeriesStroke(0, getStroke(0, 2));
		// incorrect variables
		feedbackRenderer.setSeriesPaint(1,Color.RED);
		feedbackRenderer.setSeriesStroke(1, getStroke(0, 2));
		// unnamed variables
		feedbackRenderer.setSeriesPaint(2,Color.YELLOW);
		feedbackRenderer.setSeriesStroke(2, getStroke(0, 2));
		// correct relations
		feedbackRenderer.setSeriesPaint(3,Color.ORANGE);
		feedbackRenderer.setSeriesStroke(3, getStroke(1, 2));
		// incorrect relations
		feedbackRenderer.setSeriesPaint(4,Color.MAGENTA);
		feedbackRenderer.setSeriesStroke(4, getStroke(1, 2));
		// correct directions
		feedbackRenderer.setSeriesPaint(5,Color.DARK_GRAY);
		feedbackRenderer.setSeriesStroke(5, getStroke(2, 5));
		// incorrect directions
		feedbackRenderer.setSeriesPaint(6,Color.PINK);
        feedbackRenderer.setSeriesStroke(6, getStroke(2, 5));        
        
        XYDotRenderer xyDotRenderer = new XYDotRenderer();
        xyDotRenderer.setDotHeight(7);
        xyDotRenderer.setDotWidth(7);
//        xyDotRenderer.setSeriesShape(0, ShapeUtilities.createDiamond(5));
//        xyDotRenderer.setSeriesPaint(0, Color.GREEN);
//        xyDotRenderer.setSeriesShape(1, ShapeUtilities.createDiamond(5));
//        xyDotRenderer.setSeriesPaint(1, Color.RED);
//        xyDotRenderer.setSeriesShape(2, ShapeUtilities.createDiamond(5));
//        xyDotRenderer.setSeriesPaint(2, Color.BLUE);

        xyDotRenderer.setSeriesPaint(0, Color.GREEN);
        xyDotRenderer.setSeriesPaint(1, Color.RED);
        xyDotRenderer.setSeriesPaint(2, Color.BLUE);
        
        xyPlot.setDataset(1, modelRanDataset);
        xyPlot.setRenderer(1, xyDotRenderer);
        xyPlot.setDomainAxis(1, timeAxis);
        xyPlot.setRangeAxis(1, new NumberAxis("requests"));       
		JFreeChart chart = new JFreeChart("feedback per time for user "+userModel.getUserName(), JFreeChart.DEFAULT_TITLE_FONT, xyPlot, true);
		return chart;
	}
	
	public BasicStroke getStroke(int pattern, int width) {
		BasicStroke stroke = new BasicStroke(width, BasicStroke.CAP_BUTT,  BasicStroke.JOIN_MITER, 10.0f, patterns[pattern], 0.0f);
        return stroke;
	}
	
	private XYSeriesCollection createFeedbackDataset() {	
		XYSeries variablesCorrect = new XYSeries("correct variables");
		XYSeries variablesIncorrect = new XYSeries("incorrect variables");
		XYSeries variablesUnnamed = new XYSeries("unnamed variables");
		XYSeries relationsCorrect = new XYSeries("correct relations");
		XYSeries relationsIncorrect = new XYSeries("incorrect relation");
		XYSeries directionsCorrect = new XYSeries("correct directions");
		XYSeries directionsIncorrect = new XYSeries("incorrect directions");
		
		long previousTimestamp = userModel.getActions().iterator().next().getTimeInMillis();
		long timeOffset = 0;
		
		for (IAction action: userModel.getActions()) {
			try {
				if (action.getType().equals(ModellingLogger.START_APPLICATION)) {
					timeOffset = timeOffset+(action.getTimeInMillis()-previousTimestamp);
				} else if (action.getType().endsWith("_added") ||
						action.getType().endsWith("_deleted") ||
						action.getType().equals(ModellingLogger.SPECIFICATION_CHANGED) ||
						action.getType().equals(ModellingLogger.ELEMENT_RENAMED) ||
						action.getType().equals(ModellingLogger.MODEL_LOADED) ||
						action.getType().equals(ModellingLogger.MODEL_CLEARED)) {
					String modelString = action.getAttribute("model");
					if ( modelString != null) {
						Model model = new Model(null);
						model.setXmModel(JxmModel.readStringXML(modelString));
						Feedback feedback = new Feedback(model, userModel.getDomain());
						variablesCorrect.add(action.getTimeInMillis()-timeOffset, feedback.getCorrectNames());
						variablesIncorrect.add(action.getTimeInMillis()-timeOffset, feedback.getIncorrectDirections());
						variablesUnnamed.add(action.getTimeInMillis()-timeOffset, feedback.getUnnamed());
						relationsCorrect.add(action.getTimeInMillis()-timeOffset, feedback.getCorrectRelations());
						relationsIncorrect.add(action.getTimeInMillis()-timeOffset, feedback.getIncorrectRelations());
						directionsCorrect.add(action.getTimeInMillis()-timeOffset, feedback.getCorrectDirections());
						directionsIncorrect.add(action.getTimeInMillis()-timeOffset, feedback.getIncorrectDirections());
					}
				}
				previousTimestamp = action.getTimeInMillis();
			} catch (Exception e) {
				System.out.println("FeedbackTimeline. exception for user "+userModel.getUserName()+": "+e.getMessage());
			}
		}
		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(variablesCorrect);
		dataset.addSeries(variablesIncorrect);
		dataset.addSeries(variablesUnnamed);
		dataset.addSeries(relationsCorrect);
		dataset.addSeries(relationsIncorrect);
		dataset.addSeries(directionsCorrect);
		dataset.addSeries(directionsIncorrect);	
		return dataset;
	}
	
	private XYSeriesCollection createModelRanDataset() {	
		XYSeries modelRan = new XYSeries("model ran");
		XYSeries modelRanError = new XYSeries("model error");
		XYSeries feedbackRequested = new XYSeries("feedback requested");
		
		long previousTimestamp = userModel.getActions().iterator().next().getTimeInMillis();
		long timeOffset = 0;
		for (IAction action: userModel.getActions()) {
			try {
				if (action.getType().equals(ModellingLogger.START_APPLICATION)) {
					timeOffset = timeOffset+(action.getTimeInMillis()-previousTimestamp);
				} else if (action.getType().equals(ModellingLogger.MODEL_RAN)) {
					modelRan.add(action.getTimeInMillis()-timeOffset, -1);
				} else if (action.getType().equals(ModellingLogger.MODEL_RAN_ERROR)) {
					modelRanError.add(action.getTimeInMillis()-timeOffset, -1);
				} else if (action.getType().equals(ModellingLoggerFES.FEEDBACK_REQUESTED)) {
					feedbackRequested.add(action.getTimeInMillis()-timeOffset, -2);
				}
				previousTimestamp = action.getTimeInMillis();
			} catch (Exception e) {
				System.out.println("FeedbackTimeline. exception for user "+userModel.getUserName()+": "+e.getMessage());
			}
		}
		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(modelRan);
		dataset.addSeries(modelRanError);
		dataset.addSeries(feedbackRequested);
		return dataset;
	}

}
