package eu.scy.modules.useradmin.pages;

import eu.scy.core.Constants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.services.Response;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.PieDataset;
import org.jfree.data.xy.XYDataset;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 05.aug.2008
 * Time: 14:14:33
 */
public class Home extends Start {

    @Inject
    private ComponentResources resources;

    @Inject
    private TypeCoercer coercer;

    public Link getGroupUserCountPieChart() {
        return resources.createActionLink("chart", false, new Object[]{"400", "300"});
    }

    public Link getSessionsStartedTimeSeriesChart() {
        return resources.createActionLink("sessionsStartedTimeSeriesChart", false, new Object[]{"400", "300"});
    }

    public StreamResponse onChart(final int width, final int height) {
        final PieDataset pieDataset = getGroupUserCountPieDataset(getCurrentProject());


        return new StreamResponse() {
            public String getContentType() {
                return Constants.CONTENT_TYPE_IMAGE_PNG;
            }

            public InputStream getStream() throws IOException {
                JFreeChart chart = ChartFactory.createPieChart("Group user count", pieDataset, true, true, true);
                PiePlot plot = (PiePlot) chart.getPlot();
                plot.setIgnoreZeroValues(true);
                plot.setIgnoreNullValues(true);
                plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0} ({1})"));                
                ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
                ChartUtilities.writeBufferedImageAsPNG(byteArray, chart.createBufferedImage(width, height));
                return new ByteArrayInputStream(byteArray.toByteArray());
            }

            public void prepareResponse(Response response) {
            }
        };
    }

    public StreamResponse onSessionsStartedTimeSeriesChart(final int width, final int height) {
        final XYDataset dataset = getStartedSessionsDataset(getCurrentProject());

        return new StreamResponse() {
            public String getContentType() {
                return Constants.CONTENT_TYPE_IMAGE_PNG;
            }

            public InputStream getStream() throws IOException {
                JFreeChart chart = ChartFactory.createTimeSeriesChart("User session statistics", "Date", "Count", dataset, true, true, true);
                ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
                ChartUtilities.writeBufferedImageAsPNG(byteArray, chart.createBufferedImage(width, height));
                return new ByteArrayInputStream(byteArray.toByteArray());
            }

            public void prepareResponse(Response response) {
            }
        };
    }
}