package eu.scy.client.tools.scydynamics.logging.parser;

import java.awt.BorderLayout;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;
import org.jfree.data.xy.XYDataset;
import eu.scy.actionlogging.api.IAction;

@SuppressWarnings("serial")
public class UserTimeline extends JPanel {

	private ParserModel model;
	private HistogramDataset dataset;
	private JFreeChart chart;
	private ChartPanel chartPanel;

	public UserTimeline(ParserModel model) {
		super();
		this.model = model;
		dataset = createDataset();
        chart = createChart(dataset);
        chartPanel = new ChartPanel(chart);
        chartPanel.setMouseZoomable(true, false);
        this.setLayout(new BorderLayout());
        this.add(chartPanel, BorderLayout.CENTER); 
	}
	
	private JFreeChart createChart(final XYDataset dataset) {
        final JFreeChart chart = ChartFactory.createTimeSeriesChart(
            "user actions per time", 
            "time", 
            "#actions [%]",
            dataset, 
            true, 
            true, 
            false
        );
        final XYItemRenderer renderer = chart.getXYPlot().getRenderer();
        final StandardXYToolTipGenerator g = new StandardXYToolTipGenerator(
            StandardXYToolTipGenerator.DEFAULT_TOOL_TIP_FORMAT,
            new SimpleDateFormat("d-MMM-yyyy"), new DecimalFormat("0.00")
        );
        renderer.setToolTipGenerator(g);
        chart.removeLegend();
        return chart;
    }
	
	private HistogramDataset createDataset() {
		HistogramDataset dataset = new HistogramDataset();
		dataset.setType(HistogramType.RELATIVE_FREQUENCY);
		// calculate the number of bins
		// -> 3 minutes / bin (3 minutes = 180000 millis)
		int bins = 3;
		try {
			long earliest = model.getEarliestAction().getTimeInMillis();
			long latest = model.getLatestAction().getTimeInMillis();
			bins = (int) ((latest-earliest) / 18000);
			if (bins < 3) {
				bins = 3;
			}
		} catch (Exception ex) {
			// in case of no data...
		}
		for (UserModel userModel: model.getUserModels().values()) {
			//dataset.addSeries(userModel.getUserName(), getTimeValues(userModel), bins);
			dataset.addSeries(userModel.getUserName(), getTimeValues(userModel), 1000);
		}
		//System.out.println("UserTimeline: dataset created with "+dataset.getSeriesCount()+" series in "+bins+" bins.");
		return dataset;
	}
	
	
	private double[] getTimeValues(UserModel userModel) {
		double[] values = new double[userModel.getActions().size()];
		int i=0;
		for (IAction action: userModel.getActions()) {
			values[i++] = action.getTimeInMillis();
		}
		return values;
	}

	public void update() {
		dataset = createDataset();
		chartPanel.setChart(createChart(dataset));
		chartPanel.updateUI();
	}

}
