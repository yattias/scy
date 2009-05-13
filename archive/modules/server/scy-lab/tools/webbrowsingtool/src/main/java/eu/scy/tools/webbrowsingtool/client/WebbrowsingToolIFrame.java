package eu.scy.tools.webbrowsingtool.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RichTextArea;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.TabPanel;
import com.gwtext.client.widgets.Viewport;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.VType;
import com.gwtext.client.widgets.layout.FitLayout;

import eu.scy.tools.webbrowsingtool.client.gif.RichTextToolbar;

public class WebbrowsingToolIFrame extends Panel implements EntryPoint {


    private Panel header;

    private Panel main;
    
    private RichTextArea textArea;

    public WebbrowsingToolIFrame() {
        
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
        textArea = new RichTextArea();
        RichTextToolbar toolbar = new RichTextToolbar(textArea);
        HighlightTab highlightTab = new HighlightTab(textArea);
        BrowseTab browseTab = new BrowseTab(this,textArea);
        tabPanel.add(browseTab);
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

    
    /**
     * @return the textArea
     */
    public RichTextArea getTextArea() {
        return textArea;
    }

    
    /**
     * @param textArea the textArea to set
     */
    public void setTextArea(RichTextArea textArea) {
        this.textArea = textArea;
    }
    
    

}
