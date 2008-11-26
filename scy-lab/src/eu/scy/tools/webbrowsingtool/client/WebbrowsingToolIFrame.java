package eu.scy.tools.webbrowsingtool.client;

import java.util.Vector;

import com.google.gwt.core.client.EntryPoint;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.TabPanel;
import com.gwtext.client.widgets.Viewport;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.VType;
import com.gwtext.client.widgets.layout.FitLayout;

public class WebbrowsingToolIFrame extends Panel implements EntryPoint {

    private Vector<Note> notes;

    private Panel header;

    private Panel main;

    public WebbrowsingToolIFrame() {
        // Testing
        notes = new Vector<Note>();
        notes.add(new Note("Test1", "Testtext blabla bla", "Testannotation bla"));
        notes.add(new Note("Test2", "Testtext blabla bla bla 2", "Testannotation blabla2"));

        buildGUI();
    }

    private void buildGUI() {

        Panel wrapper = new Panel();
        this.setLayout(new FitLayout());

        header = buildHeader();
        main = buildMainPanel();

        wrapper.add(header);
        wrapper.add(main);

        this.add(wrapper);

        @SuppressWarnings("unused")
        Viewport view = new Viewport(this);

    }

    private Panel buildMainPanel() {
        TabPanel tabPanel = new TabPanel();
        tabPanel.setHeight(570);
        tabPanel.setMonitorResize(true);
        BrowseTab browseTab = new BrowseTab();
        tabPanel.add(browseTab);
        HighlightTab highlightTab = new HighlightTab(notes);
        browseTab.setHighlightTab(highlightTab);
        tabPanel.add(highlightTab);
        return tabPanel;
    }


    private Panel buildHeader() {
        FormPanel formPanel = new FormPanel();
        TextField source = new TextField("Source");
        source.setVtype(VType.URL);
        TextField date = new TextField("Date");
        TextField keyword = new TextField("Keyword");
        formPanel.add(source);
        formPanel.add(date);
        formPanel.add(keyword);
        return formPanel;
    }

    public void onModuleLoad() {

    }

}
