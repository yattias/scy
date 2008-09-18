package eu.scy.lab.client.desktop.workspace;

import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.TabPanel;

public class TabbedWorkspace extends TabPanel {

	private int index;

	public TabbedWorkspace() {
		super();
		setBorder(false);
		setPaddings(15);
		setHeight("auto");

		setResizeTabs(true);
		setMinTabWidth(115);
		String html = "<p>Use the navigation bar on the left to navigate through the SCY-Lab";
		addTab("Welcome!", html);
		setTabWidth(135);
		setEnableTabScroll(true);
		setActiveTab(0);
	}
	

	public Panel addTab(String title, String html) {
		Panel tab = new Panel();
		tab.setAutoScroll(true);
		tab.setTitle(title);
		tab.setIconCls("tab-icon");
		tab.setHtml(html);
		tab.setClosable(true);
		add(tab);
		++index;
		return tab;
	}


	public TabbedWorkspace getWorkspace() {
		return this;
	}
	
	public int getIndex(){
		return index;
	}

}
