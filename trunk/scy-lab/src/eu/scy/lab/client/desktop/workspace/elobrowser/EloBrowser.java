package eu.scy.lab.client.desktop.workspace.elobrowser;

import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.RegionPosition;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.PagingToolbar;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.ToolTip;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.FieldListenerAdapter;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.event.GridRowListener;
import com.gwtext.client.widgets.layout.BorderLayout;
import com.gwtext.client.widgets.layout.BorderLayoutData;
import com.gwtext.client.widgets.layout.HorizontalLayout;
import com.gwtextux.client.data.PagingMemoryProxy;

public class EloBrowser extends Panel {

	private PreviewPanel previewPanel;
	private GridPanel grid;

	public EloBrowser() {
		super("ELO-Browser");
		// FIXME only fixed height works, perhaps need a fitting wrapper panel
//		setLayout(new FitLayout());
		setHeight("600px");
//		setHeight("100%");
		setClosable(false);

		//TODO Try AnchorLayout()
		setLayout(new BorderLayout());

		setPaddings(15);

		// MemoryProxy proxy = new MemoryProxy(getCompanyData());
		PagingMemoryProxy proxy = new PagingMemoryProxy(getGridData());
		RecordDef recordDef = new RecordDef(new FieldDef[] {
				new StringFieldDef("name"), new StringFieldDef("author"),
				// new DateFieldDef("date", "n/j h:ia"),
				new StringFieldDef("date"), new StringFieldDef("iconurl") });

		ArrayReader reader = new ArrayReader(recordDef);
		final Store store = new Store(proxy, reader, true);

		ColumnConfig[] columns = new ColumnConfig[] {
				// column ID is company which is later used in
				// setAutoExpandColumn
				// new ColumnConfig("Name", "name", 160, true, null, "name"),
				new ColumnConfig("Name", "name", 160, true, null, "name"),
				new ColumnConfig("Author", "author", 160, true),
				new ColumnConfig("Date", "date", 45),
				new ColumnConfig("Icon-Url", "iconurl", 100) };

		// The Grid
		// TODO set layout and size
		ColumnModel columnModel = new ColumnModel(columns);

		grid = new GridPanel();
		grid.setStore(store);
		grid.setColumnModel(columnModel);

		grid.setFrame(true);
		grid.setStripeRows(true);
		grid.setLayout(new HorizontalLayout(0));
		grid.setHeight(300);
		grid.setMonitorResize(true);
		grid.setAutoExpandColumn("name");
		grid.setTitle("Grid that pages Local / In-Memory data");
//		grid.setLoadMask(true);

		final PagingToolbar pagingToolbar = new PagingToolbar(store);
		pagingToolbar.setPageSize(5);
		pagingToolbar.setDisplayInfo(true);
		pagingToolbar.setDisplayMsg("Displaying companies {0} - {1} of {2}");
		pagingToolbar.setEmptyMsg("No records to display");

		// Toolbar topToolbar = new Toolbar();
		// topToolbar.addFill();
		// grid.setTopToolbar(topToolbar);
		//		
		// GridSearchPlugin gridSearch = new
		// GridSearchPlugin(GridSearchPlugin.TOP);
		// gridSearch.setMode(GridSearchPlugin.LOCAL);
		// grid.addPlugin(gridSearch);

		grid.addGridRowListener(new GridRowListener() {

			public void onRowClick(GridPanel grid, int rowIndex, EventObject e) {

				String name = (grid.getSelectionModel().getSelected()
						.getAsString(grid.getSelectionModel().getSelected()
								.getFields()[0]));
				String author = (grid.getSelectionModel().getSelected()
						.getAsString(grid.getSelectionModel().getSelected()
								.getFields()[1]));
				String date = (grid.getSelectionModel().getSelected()
						.getAsString(grid.getSelectionModel().getSelected()
								.getFields()[2]));
				String iconURL = (grid.getSelectionModel().getSelected()
						.getAsString(grid.getSelectionModel().getSelected()
								.getFields()[3]));
				previewPanel.update(name, author, date, iconURL);

			}

			public void onRowContextMenu(GridPanel grid, int rowIndex,
					EventObject e) {

			}

			public void onRowDblClick(GridPanel grid, int rowIndex,
					EventObject e) {

			}

		});

		NumberField pageSizeField = new NumberField();
		pageSizeField.setWidth(40);
		pageSizeField.setSelectOnFocus(true);
		pageSizeField.addListener(new FieldListenerAdapter() {
			public void onSpecialKey(Field field, EventObject e) {
				if (e.getKey() == EventObject.ENTER) {
					int pageSize = Integer.parseInt(field.getValueAsString());
					pagingToolbar.setPageSize(pageSize);
				}
			}
		});

		ToolTip toolTip = new ToolTip("Enter page size");
		toolTip.applyTo(pageSizeField);

		pagingToolbar.addField(pageSizeField);
		grid.setBottomToolbar(pagingToolbar);
		store.load(0, 5);

		grid.setBufferResize(true);

		// adding the SearchField-Panel to the ELO-Browser
		// TODO No search integrated at the moment
		BorderLayoutData northData = new BorderLayoutData(RegionPosition.NORTH);
		northData.setSplit(true);
		northData.setSplitTip("Drag to resize");
		northData.setMinHeight(25);
		Panel searchPanel = new Panel();
		searchPanel.setLayout(new HorizontalLayout(15));
		TextField searchField = new TextField();
		Button searchButton = new Button("Search!");
		searchPanel.add(searchField);
		searchPanel.add(searchButton);
		searchPanel.setHeight(25);

		add(searchPanel, northData);

		// adding the Grid-Panel to the ELO-Browser
		BorderLayoutData centerData = new BorderLayoutData(
				RegionPosition.CENTER);

		centerData.setMinHeight(200);
		centerData.setMaxSize(300);
		
		add(grid, centerData);
		setAutoScroll(true);

		// adding the Preview-Panel to the ELO Browser
		previewPanel = new PreviewPanel("Testobject", "Sven", "8.8.2008",
				PreviewPanel.DEFAULT_IMAGE_URL);
		BorderLayoutData southData = new BorderLayoutData(RegionPosition.SOUTH);
		southData.setSplit(true);
		southData.setSplitTip("Drag to resize");
		southData.setMinHeight(150);
		previewPanel.setHeight(150);
		add(previewPanel.getPreviewPanel(), southData);

	}

	// The local Array-Data to display in the Grid
	private Object[][] getGridData() {
		return new Object[][] {
				new Object[] { "Kryptoarithmetics", "Sven", "9/1 12:00am",
						"res/icons/fireworks01.png" },
				new Object[] { "Graphsearch", "Sven M", "9/2 12:00am",
						"res/icons/flash01.png" },
				new Object[] { "Kryptoarithmetics II", "Sven", "9/4 12:00am",
						"res/icons/dreamweaver01.png" },
				new Object[] { "Dancing with animals", "Sven", "9/5 12:01pm",
						"res/icons/openoffice_draw.png" },
				new Object[] { "Kryptoarithmetics III", "Sven", "10/1 12:10am",
						"res/icons/openoffice_calc.png" } };
	}

}
