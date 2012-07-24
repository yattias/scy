package eu.scy.client.tools.scydynamics.logging.parser;

import java.awt.BasicStroke;
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

import eu.scy.actionlogging.api.IAction;
import eu.scy.client.tools.scydynamics.domain.Feedback;
import eu.scy.client.tools.scydynamics.logging.ModellingLogger;
import eu.scy.client.tools.scydynamics.logging.ModellingLoggerFES;

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
        this.add(chartPanel); 
	}
	
	private JFreeChart createChart() {
		XYPlot plot = new XYPlot();
		XYLineAndShapeRenderer feedbackRenderer = new XYLineAndShapeRenderer(true, false);
		ValueAxis timeAxis = new DateAxis("time");
		timeAxis.setLowerMargin(0.02D);
		timeAxis.setUpperMargin(0.02D);
		NumberAxis valueAxis = new NumberAxis("feedback");
		valueAxis.setAutoRangeIncludesZero(false);
		plot.setDomainAxis(timeAxis);
		plot.setRangeAxis(valueAxis);
		
		plot.setDataset(0, feedbackDataset);
		plot.setRenderer(0, feedbackRenderer);
		
		feedbackRenderer.setDrawSeriesLineAsPath(true);   
		feedbackRenderer.setSeriesPaint(0,Color.GREEN);
		feedbackRenderer.setSeriesStroke(0, getStroke(0, 2));
		feedbackRenderer.setSeriesPaint(1,Color.RED);
		feedbackRenderer.setSeriesStroke(1, getStroke(0, 2));
		feedbackRenderer.setSeriesPaint(2,Color.YELLOW);
		feedbackRenderer.setSeriesStroke(2, getStroke(0, 2));
		feedbackRenderer.setSeriesPaint(3,Color.CYAN);
		feedbackRenderer.setSeriesStroke(3, getStroke(1, 2));
		feedbackRenderer.setSeriesPaint(4,Color.MAGENTA);
		feedbackRenderer.setSeriesStroke(4, getStroke(1, 2));
		feedbackRenderer.setSeriesPaint(5,Color.YELLOW);
		feedbackRenderer.setSeriesStroke(5, getStroke(2, 5));
		feedbackRenderer.setSeriesPaint(6,Color.PINK);
        feedbackRenderer.setSeriesStroke(6, getStroke(2, 5));        
        XYDotRenderer dotRenderer = new XYDotRenderer();
        dotRenderer.setDotHeight(10);
        dotRenderer.setDotWidth(10);
        dotRenderer.setSeriesFillPaint(0, Color.GREEN);
        dotRenderer.setSeriesFillPaint(1, Color.RED);
        plot.setDataset(1, modelRanDataset);
        plot.setRenderer(1, dotRenderer);
        plot.setDomainAxis(1, timeAxis);
        plot.setRangeAxis(1, new NumberAxis("feedback"));       
		JFreeChart chart = new JFreeChart("feedback per time for user "+userModel.getUserName(), JFreeChart.DEFAULT_TITLE_FONT, plot, true);
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
		for (IAction action: userModel.getActions()) {
			try {
				if (action.getType().equals(ModellingLoggerFES.FEEDBACK_REQUESTED)) {
					Feedback feedback = new Feedback(action.getAttribute("feedback"));
					variablesCorrect.add(action.getTimeInMillis(), feedback.getCorrectNames());
					variablesIncorrect.add(action.getTimeInMillis(), feedback.getIncorrectDirections());
					variablesUnnamed.add(action.getTimeInMillis(), feedback.getUnnamed());
					relationsCorrect.add(action.getTimeInMillis(), feedback.getCorrectRelations());
					relationsIncorrect.add(action.getTimeInMillis(), feedback.getIncorrectRelations());
					directionsCorrect.add(action.getTimeInMillis(), feedback.getCorrectDirections());
					directionsIncorrect.add(action.getTimeInMillis(), feedback.getIncorrectDirections());
				}
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
		
		for (IAction action: userModel.getActions()) {
			try {
				if (action.getType().equals(ModellingLogger.MODEL_RAN)) {
					modelRan.add(action.getTimeInMillis(), 1);
				} else if (action.getType().equals(ModellingLogger.MODEL_RAN_ERROR)) {
					modelRanError.add(action.getTimeInMillis(), 1);
				} else if (action.getType().equals(ModellingLoggerFES.FEEDBACK_REQUESTED)) {
					feedbackRequested.add(action.getTimeInMillis(), 1.5);
				}
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
