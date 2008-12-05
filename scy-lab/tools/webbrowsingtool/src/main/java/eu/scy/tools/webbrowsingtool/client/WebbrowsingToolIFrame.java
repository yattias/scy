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

    private NotesPanel notesPanel;

    private Panel header;

    private Panel main;

    public WebbrowsingToolIFrame() {
        // Testing
        notesPanel = new NotesPanel(new Vector<Note>());
        notesPanel.addNote(new Note("Primary goals", "It should be \"simple, object oriented, and familiar\".\n It should be \"robust and secure\".\n It should be \"architecture neutral and portable\".\n It should execute with \"high performance\".\n It should be \"interpreted, threaded, and dynamic\".", "Very important!"));
        notesPanel.addNote(new Note("Applets", "An applet is placed in an HTML document using the <applet> HTML element. The applet tag has three attributes set: code=\"Hello\" specifies the name of the Applet class and width=\"200\" height=\"200\" sets the pixel width and height of the applet. Applets may also be embedded in HTML using either the object or embed element[15], although support for these elements by Web browsers is inconsistent.[16] However, the applet tag is deprecated, so the object tag is preferred where supported.", "How to integrate applets in your website."));

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
        HighlightTab highlightTab = new HighlightTab(notesPanel);
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
