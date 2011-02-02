package eu.scy.monitor.client;

import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TabPanel;
import com.rednels.ofcgwt.client.ChartWidget;
import com.rednels.ofcgwt.client.model.ChartData;
import com.rednels.ofcgwt.client.model.axis.XAxis;
import com.rednels.ofcgwt.client.model.axis.YAxis;
import com.rednels.ofcgwt.client.model.elements.BarChart;
import com.rednels.ofcgwt.client.model.elements.LineChart;
import com.rednels.ofcgwt.client.model.elements.PieChart;
import com.rednels.ofcgwt.client.model.elements.BarChart.BarStyle;
import com.rednels.ofcgwt.client.model.elements.LineChart.LineStyle;

public class StatisticsView extends TabPanel {

    public StatisticsView() {
        setSize("100%", "100%");
        add(getUsagePanel(), "Usage per Tool");
        add(getActionsPanel(), "Actions per User");
        add(getLoginsPanel(), "Logins per User");
        selectTab(0);
    }

    private Panel getUsagePanel() {
        Panel panel = new HorizontalPanel();
        final ChartWidget chart = new ChartWidget();
        chart.setSize("400", "300");
        chart.setJsonData(getUsageData().toString());
        panel.add(chart);
        return panel;
    }

    private Panel getActionsPanel() {
        Panel panel = new HorizontalPanel();
        final ChartWidget chart = new ChartWidget();
        chart.setSize("400", "300");
        chart.setJsonData(getActionsData().toString());
        panel.add(chart);
        return panel;
    }

    private Panel getLoginsPanel() {
        Panel panel = new HorizontalPanel();
        final ChartWidget chart = new ChartWidget();
        chart.setSize("700", "300");
        chart.setJsonData(getLoginsData().toString());
        panel.add(chart);
        return panel;
    }

    private ChartData getUsageData() {
        ChartData cd = new ChartData("Usage per Tool", "font-size: 14px; font-family: Verdana; text-align: center;");
        cd.setBackgroundColour("#ffffff");
        PieChart pie = new PieChart();
        pie.setAlpha(0.3f);
        // pie.setNoLabels(true);
        pie.setTooltip("#label#: #val# times<br>#percent#");
        pie.setAnimate(true);
        pie.setGradientFill(true);
        pie.setColours("#ff0000", "#00ff00", "#0000ff", "#ff9900", "#ff00ff");
        pie.addSlices(new PieChart.Slice(Random.nextInt(11), "Map"));
        pie.addSlices(new PieChart.Slice(Random.nextInt(5), "Video"));
        pie.addSlices(new PieChart.Slice(Random.nextInt(3), "CO2SIM"));
        pie.addSlices(new PieChart.Slice(Random.nextInt(8), "Drawing"));
        cd.addElements(pie);
        return cd;
    }

    private ChartData getActionsData() {
        ChartData cd = new ChartData("Actions per User", "font-size: 14px; font-family: Verdana; text-align: center;");
        cd.setBackgroundColour("#ffffff");
        XAxis xa = new XAxis();
        xa.setLabels("Adam", "Lars", "Stefan");
        // xa.setMax(3);
        cd.setXAxis(xa);
        YAxis ya = new YAxis();
        ya.setSteps(16);
        ya.setMax(160);
        cd.setYAxis(ya);
        BarChart bchart = new BarChart(BarStyle.NORMAL);
        bchart.setColour("#00aa00");
        bchart.setTooltip("#val# Actions");
        for (int t = 0; t < 3; t++) {
            bchart.addValues(Random.nextInt(50) + 50);
        }
        cd.addElements(bchart);
        return cd;
    }

    private ChartData getLoginsData() {
        ChartData cd = new ChartData("Logins per day and user", "font-size: 14px; font-family: Verdana; text-align: center;");
        cd.setBackgroundColour("#ffffff");

        LineChart lc1 = new LineChart(LineStyle.NORMAL);
        lc1.setText("Adam");
        lc1.setColour("#ff0000");
        lc1.setTooltip("#val# Logins");
        for (int t = 0; t < 30; t++) {
            lc1.addValues(Random.nextInt(10));
        }
        LineChart lc2 = new LineChart(LineStyle.HOLLOW);
        lc2.setColour("#00ff00");
        lc2.setText("Lars");
        lc2.setTooltip("#val# Logins");
        for (int t = 0; t < 30; t++) {
            lc2.addValues(Random.nextInt(10));
        }
        LineChart lc3 = new LineChart(LineStyle.DOT);
        lc3.setColour("#0000ff");
        lc3.setText("Stefan");
        lc3.setTooltip("#val# Logins");
        for (int t = 0; t < 30; t++) {
            lc3.addValues(Random.nextInt(10));
        }
        XAxis xa = new XAxis();
        xa.setMin(1);
        xa.setMax(32);
        cd.setXAxis(xa);

        YAxis ya = new YAxis();
        ya.setMax(12);
        ya.setMin(0);
        cd.setYAxis(ya);

        cd.addElements(lc1);
        cd.addElements(lc2);
        cd.addElements(lc3);
        return cd;

    }
}
