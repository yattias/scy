package eu.scy.lab.client.desktop.workspace;


import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.TabPanel;


public class TabbedWorkspace extends TabPanel {

	public TabbedWorkspace() {
		super();
		setBorder(false);
		setPaddings(15);
//		setLayout(new FitLayout());

		setResizeTabs(true);
		setMinTabWidth(115);
		String html = "<p>Use the navigation bar on the left to navigate through the SCY-Lab";
		addHtmlTab("Welcome!", html);
		setTabWidth(135);
		setEnableTabScroll(true);
	}

	public Panel addHtmlTab(String title, String html) {
		Panel tab = new Panel();
		tab.setAutoScroll(true);
		tab.setTitle(title);
		tab.setIconCls("tab-icon");
		tab.setHtml(html);
		tab.setClosable(true);
		add(tab);
		return tab;
	}

	public boolean containsComponentID(String id) {
		for (Component c : getComponents()) {
			if (c.getId().equals(id)) {
				return true;
			}
		}
		return false;
	}
}
