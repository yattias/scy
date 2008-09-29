package eu.scy.lab.client.startupview.lastMission;

import java.util.Date;
import java.util.Vector;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.SortDir;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.DateFieldDef;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.GroupingStore;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.SortState;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.GroupingView;
import com.gwtext.client.widgets.grid.event.GridRowListener;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.layout.HorizontalLayout;
import com.gwtextux.client.data.PagingMemoryProxy;

import eu.scy.lab.client.date.DateRenderer;

public class LastMissionPanel extends Panel {

	private Vector<Mission> missions;

	private GridPanel grid;
	
	private DateRenderer renderer = new DateRenderer("dd.MM.yyyy");

	public LastMissionPanel() {

		super("Last Missions");
		onModuleLoad();

	}

	public void onModuleLoad() {

		// FIXME Setup a new Date Representation (not Date-Class)
		setLayout(new FitLayout());
		setClosable(false);

		// MemoryProxy proxy = new MemoryProxy(getCompanyData());
		PagingMemoryProxy proxy = new PagingMemoryProxy(
				getGridDataWithDate(getGridData()));
		RecordDef recordDef = new RecordDef(new FieldDef[] {
				new StringFieldDef("name"), new StringFieldDef("author"),
				new DateFieldDef("date", "dd.MM.yyyy"),
				// new DateFieldDef("date"),
				new StringFieldDef("relativedate") });

		ArrayReader reader = new ArrayReader(recordDef);
		final GroupingStore store = new GroupingStore();
		store.setReader(reader);
		store.setDataProxy(proxy);
		store.setGroupField("relativedate");

		ColumnConfig tempDateColumn = new ColumnConfig("relative Date",
				"relativedate");
		tempDateColumn.setHidden(true);

		ColumnConfig[] columns = new ColumnConfig[] {
				// column ID is company which is later used in
				new ColumnConfig("Name", "name", 160, false, null, "name"),
				new ColumnConfig("Author", "author", 160, false),
				new ColumnConfig("Date", "date", 45, false, renderer),
				tempDateColumn };
		ColumnModel columnModel = new ColumnModel(columns);
		store.setSortInfo(new SortState("date", SortDir.ASC));

		GroupingView gridView = new GroupingView();
		gridView.setForceFit(true);
		gridView
				.setGroupTextTpl("{text} ({[values.rs.length]} {[values.rs.length > 1 ? \"Items\" : \"Item\"]})");

		// The Grid
		// TODO set layout and size

		grid = new GridPanel();
		grid.setStore(store);
		grid.setColumnModel(columnModel);

		grid.setFrame(true);
		grid.setStripeRows(true);
		grid.setLayout(new HorizontalLayout(0));
		grid.setMonitorResize(true);
		grid.setAutoExpandColumn("name");
		grid.setHeader(false);

		gridView.fitColumns(true);
		grid.setView(gridView);
		grid.setIconCls("grid-icon");

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

			}

			public void onRowContextMenu(GridPanel grid, int rowIndex,
					EventObject e) {

			}

			public void onRowDblClick(GridPanel grid, int rowIndex,
					EventObject e) {

			}

		});

		Button browseMission = new Button("Browse Mission!",
				new ButtonListenerAdapter() {
					public void onClick(Button button, EventObject e) {
						// TODO Auto-generated method stub
						MessageBox
								.alert("Starting Missionbrowser when feature is enabled");
					}
				});
		addButton(browseMission);

		// store.load(0, 5);
		store.load();

		grid.setBufferResize(true);

		// adding the SearchField-Panel to the ELO-Browser
		// TODO No search integrated at the moment

		// adding the Grid-Panel to the ELO-Browser
		add(grid);
		setAutoScroll(true);

	}

	// The local Array-Data to display in the Grid
	private Object[][] getGridData() {
		
		return new Object[][] {
				new Object[] { "Kryptoarithmetics", "Sven",new Date(108, 8, 22) },
				new Object[] { "Graphsearch", "Sven M", new Date(108, 8, 21) },
				new Object[] { "Kryptoarithmetics II","Sven", new Date(108, 8, 20) },
				new Object[] { "Dancing with animals","Sven", new Date(108, 8, 21) },
				new Object[] { "Kryptoarithmetics III","Sven", new Date(108, 8, 22) } };
	}


	private Object[][] getGridDataWithDate(Object[][] ungroupedData) {
		Object[][] groupedData = new Object[ungroupedData.length][ungroupedData[0].length + 1];

		for (int i = 0; i < ungroupedData.length; i++) {
			for (int j = 0; j < ungroupedData[i].length; j++) {
				groupedData[i][j] = ungroupedData[i][j];
			}
			//FIXME Parsing doesnt work
			//My parsing example
			Date indexDate = new Date();
			String dateString3 = DateTimeFormat.getLongTimeFormat().format(indexDate);
			DateTimeFormat dtf2 = DateTimeFormat.getLongTimeFormat();
			System.out.println("dateString3: "+dateString3);
			Date date3 = dtf2.parse(dateString3);
			System.out.println("Date 3: "+ date3);
			
//			String string = groupedData[i][2].toString();
//			Date indexDate = dateTimeFormat.parse(string);
//			System.out.println("string: "+ string);
//			System.out.println("indexDate: "+dateTimeFormat.format(indexDate));
			
			// new Date(groupedData[i][2])
			// groupedData[i][ungroupedData[i].length]="today";
			groupedData[i][ungroupedData[i].length] = createRelativeDate(indexDate);
		}

		// testing
		for (int i = 0; i < groupedData.length; i++) {
			for (int j = 0; j < groupedData[i].length; j++) {
				System.out.print(groupedData[i][j]+"\t");
			}
			System.out.println("");
		}
		return groupedData;
	}

	private String createRelativeDate(Date date) {
		Date today = new Date();
		//FIXME CHange this!
		if ((date.getDate() == today.getDate())
				&& (date.getMonth() == today.getMonth())
				&& (date.getYear() == today.getYear())) {
			return "today";
		} else if ((date.getMonth() == today.getMonth())
				&& (date.getYear() == today.getYear())) {
			return "this month";
		} else
			return "older";
	}

	/**
	 * @return the last missions from a remote service
	 */
	// TODO connect to remote service and correct return statement
	public Vector<Mission> getLastMissions() {

		Vector<Mission> missions = new Vector<Mission>();
		missions.add(new Mission("CO2 neutral house", "09/22/2008"));
		missions.add(new Mission("Tutorial Mission", "09/21/2008"));
		return missions;
	}

	/**
	 * @param missions
	 *            the missions to set
	 */
	public void setMissions(Vector<Mission> missions) {
		this.missions = missions;
	}

	/**
	 * @return the missions
	 */
	public Vector<Mission> getMissions() {
		return missions;
	}

}
