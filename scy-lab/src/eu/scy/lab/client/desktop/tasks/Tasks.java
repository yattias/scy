package eu.scy.lab.client.desktop.tasks;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.gears.client.Factory;
import com.google.gwt.gears.client.GearsException;
import com.google.gwt.gears.client.database.Database;
import com.google.gwt.gears.client.database.DatabaseException;
import com.google.gwt.gears.client.database.ResultSet;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.UrlParam;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.SimpleStore;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.MessageBox.PromptCallback;
import com.gwtext.client.widgets.event.ButtonListener;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.EditorGridPanel;
import com.gwtext.client.widgets.grid.GridEditor;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.event.GridCellListenerAdapter;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtextux.client.data.PagingMemoryProxy;

public class Tasks {

	private Panel panel;
	private Database db;
	private GridPanel grid;
	private List<String> tasks;
	private Store store;

	public Tasks() {

		// Create the database if it doesn't exist.
		try {
			db = Factory.getInstance().createDatabase();
			db.open("scy-lab-tasks");
			// The 'int' type will store up to 8 byte ints depending on the
			// magnitude of the
			// value added.
			db.execute("create table if not exists tasks (Task string)");
		} catch (GearsException e) {
			MessageBox.alert(e.toString());
		}

		// Add an entry to the database
		Button addTask = new Button("Add Task...", new ButtonListenerAdapter() {

			public void onClick(Button button, EventObject e) {
				// TODO Auto-generated method stub
				MessageBox.prompt("Add task...",
						"Enter the task you want to add!",
						new PromptCallback() {

							public void execute(String btnID, String text) {
								if (btnID.equals("ok")) {
									try {
										
										db.execute(
												"insert into tasks values (?)",
												new String[] { text });
										// update();
									} catch (DatabaseException de) {
										MessageBox.alert(de.toString());
									}

								} else if (btnID.equals("cancel")) {

								}

							}

						});

			}

		});

		grid = createGrid();

		panel = new Panel();
		panel.setAutoScroll(true);
		panel.setBorder(false);
		panel.setPaddings(5);

		panel.add(grid);
		panel.add(addTask);

	}

	// public void update() {
	// Record plant = recordDef.createRecord(new Object[]{
	// "New Plant1", "Anguinaria Canadensis", "Mostly Shady",
	// new Float(5), "", Boolean.FALSE});
	// grid.stopEditing();
	// store.insert(0, plant);
	// grid.startEditing(0, 0);
	// }
	//		
	// }

	public GridPanel createGrid() {

		// Fetch previous results from the database.
		tasks = new ArrayList<String>();
		try {
			ResultSet rs = db.execute("select * from tasks order by Task");
			for (int i = 0; rs.isValidRow(); ++i, rs.next()) {
				tasks.add(rs.getFieldAsString(0));
			}
			rs.close();
		} catch (DatabaseException e) {
			MessageBox.alert(e.toString());
		}

		// Display the list of results in a table
		// grid.removeFromParent();
		int gridSize = tasks.size();

		final RecordDef recordDef = new RecordDef(
				new FieldDef[] { new StringFieldDef("tasks") });

		ArrayReader reader = new ArrayReader(recordDef);
		PagingMemoryProxy proxy = new PagingMemoryProxy(getGridData());
		store = new Store(proxy, reader);

		ColumnConfig config = new ColumnConfig(null,"tasks",200,false,null,"tasks");
		config.setEditor(new GridEditor(new TextField()));

		ColumnConfig[] columns = { config };

		ColumnModel columnmodel = new ColumnModel(columns);

		// GridPanel grid = new GridPanel(store, columnmodel);
		// grid.setText(0, 0, "Tasks:");

		final EditorGridPanel grid = new EditorGridPanel();

		Toolbar toolbar = new Toolbar();
		ToolbarButton button = new ToolbarButton("Add Task...",
				new ButtonListenerAdapter() {
					public void onClick(Button button, EventObject e) {

						String text = "EXAMPLE TASK";
						Record plant = recordDef
								.createRecord(new Object[] { text});
						try {
							db.execute(
									"insert into tasks values (?)",
									new String[] { text });
						} catch (DatabaseException e1) {
								MessageBox.alert(e1.toString());
						}
						grid.stopEditing();
						store.insert(0, plant);
						grid.startEditing(0, 0);
					}
				});
		toolbar.addButton(button);

		grid.setStore(store);
		grid.setColumnModel(columnmodel);
		//FIXME correct height to not-fixed values
		grid.setWidth(180);
//		grid.setHeight(150);
		grid.setAutoExpandColumn("tasks");
//		grid.setTitle("Editor Grid Example");
		grid.setFrame(true);
		grid.setClicksToEdit(1);
		grid.setTopToolbar(toolbar);
		grid.setBorder(false);
//		grid.setAutoScroll(true);

		grid.addGridCellListener(new GridCellListenerAdapter() {
			public void onCellClick(GridPanel grid, int rowIndex, int colIndex,
					EventObject e) {
				if (grid.getColumnModel().getDataIndex(colIndex).equals(
						"indoor")
						&& e.getTarget(".checkbox", 1) != null) {
					Record record = grid.getStore().getAt(rowIndex);
					record.set("indoor", !record.getAsBoolean("indoor"));
				}
			}
		});
		
		  store.load(new UrlParam[]{new UrlParam("rnd", new Date().getTime() + "")});  
		
		return grid;

	}

	public Object[][] getGridData() {
		tasks = new ArrayList<String>();
		try {

			ResultSet rs = db.execute("select * from tasks order by Task");
			for (int i = 0; rs.isValidRow(); ++i, rs.next()) {
				tasks.add(rs.getFieldAsString(0));
			}
			rs.close();
		} catch (DatabaseException e) {
			MessageBox.alert(e.toString());
		}

		Object[][] result = new Object[tasks.size()][1];
		for (int i = 0; i < tasks.size(); i++) {
			result[i][0] = tasks.get(i);
		}
		return result;

	}

	public Panel getPanel() {
		return this.panel;
	}

}