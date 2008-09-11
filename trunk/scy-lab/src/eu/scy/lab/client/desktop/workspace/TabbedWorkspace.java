package eu.scy.lab.client.desktop.workspace;

import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.TabPanel;
import com.gwtext.client.widgets.layout.VerticalLayout;

public class TabbedWorkspace extends TabPanel {

	private int index;
	private Panel mainPanel;
	private Panel subPanel;
	// private Menu menu;

	public TabbedWorkspace() {
		super();
		setBorder(false);
		setPaddings(15);
		mainPanel = new Panel();
		mainPanel.setLayout(new VerticalLayout(15));
		subPanel = new Panel();
		

		setResizeTabs(true);
		setMinTabWidth(115);
		String html = "<p>Use the navigation bar on the left to navigate through the SCY-Lab";
		addTab("Welcome!", html);
		setTabWidth(135);
		setEnableTabScroll(true);
		setWidth(450);
		setHeight(400);
		setActiveTab(0);
	

		subPanel.add(this);
		mainPanel.add(subPanel);
		
//		mainPanel.add(this);

	}
	
	public Panel getMainPanel(){
		return mainPanel;
	}

	private Panel addTab(String title, String html) {
		Panel tab = new Panel();
		tab.setAutoScroll(true);
		tab.setTitle(title + (++index));
		tab.setIconCls("tab-icon");
		tab.setHtml(html);
		tab.setClosable(true);
		add(tab);
		return tab;
	}

//	public Viewport getViewport() {
//		return this.viewport;
//	}

	public TabbedWorkspace getWorkspace() {
		return this;
	}
	
	public int getIndex(){
		return index;
	}

}
