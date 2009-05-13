package eu.scy.lab.client.desktop.workspace;

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.TabPanel;
import com.gwtext.client.widgets.form.Label;

public class TabbedWorkspace extends TabPanel {

    public TabbedWorkspace() {
        super();
        setBorder(false);
        setPaddings(15);
        // setLayout(new FitLayout());

        setResizeTabs(true);
        setMinTabWidth(115);
        add(createWelcomePanel());
        setTabWidth(135);
        setEnableTabScroll(true);
    }

    public Panel createWelcomePanel() {
        Panel panel = new Panel("Welcome!");
        panel.setTitle("Welcome!");
        panel.setClosable(true);
        VerticalPanel verticalPanel = new VerticalPanel();
        verticalPanel.setWidth("100%");
        verticalPanel.setHeight("100%");
        verticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        verticalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        HorizontalPanel horizontalPanel = new HorizontalPanel();
        verticalPanel.add(horizontalPanel);
        String html = "<p><center><img src=\"res/images/logo_orange.png\"></p>Use the navigation bar on the left to navigate through the SCY-Lab</p>";
        Label label = new Label();
        label.setHtml(html);
        horizontalPanel.add(label);
        panel.add(verticalPanel);
        return panel;
    }

//    public Panel addHtmlTab(String title, String html) {
//        Panel tab = new Panel();
//        tab.setAutoScroll(true);
//        tab.setTitle(title);
//        tab.setIconCls("tab-icon");
//        tab.setHtml(html);
//        tab.setClosable(true);
//        add(tab);
//        return tab;
//    }

    public boolean containsComponentID(String id) {
        for (Component c : getComponents()) {
            if (c.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }
}
