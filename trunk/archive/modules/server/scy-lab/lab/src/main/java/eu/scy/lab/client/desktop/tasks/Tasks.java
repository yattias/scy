package eu.scy.lab.client.desktop.tasks;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.gears.client.Factory;
import com.google.gwt.gears.client.GearsException;
import com.google.gwt.gears.client.database.Database;
import com.google.gwt.gears.client.database.DatabaseException;
import com.google.gwt.gears.client.database.ResultSet;
import com.google.gwt.user.client.Window;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.UrlParam;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.EditorGridPanel;
import com.gwtext.client.widgets.grid.GridEditor;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.event.EditorGridListenerAdapter;
import com.gwtextux.client.data.PagingMemoryProxy;

import eu.scy.lab.client.connectivity.Connectivity;

public class Tasks {

    private Database db;

    private GridPanel grid;

    private ArrayList<StringIntegerPair<Integer, String>> tasks;

    private Store store;

    private int id;

    private Toolbar toolbar;

    public Tasks() {

        if (Connectivity.checkForGears()) {
            // Create the database if it doesn't exist.
            try {
                db = Factory.getInstance().createDatabase();
                db.open("scy-lab-tasks");
                // The 'int' type will store up to 8 byte ints depending on the magnitude of the value added.
                db.execute("create table if not exists tasks (ID int, Task string)");
            } catch (GearsException e) {
                MessageBox.alert(e.toString());
            }
        }

        grid = createGrid();
        grid.setBorder(false);
        grid.setFrame(false);
        grid.setPaddings(0);
        grid.setHeader(false);
        // FitLayout might cause trouble
        // grid.setLayout(new FitLayout());

        grid.setHideColumnHeader(true);

    }

    public GridPanel createGrid() {
        tasks = new ArrayList<StringIntegerPair<Integer, String>>();
        
        if (Connectivity.checkForGears()) {
            // Fetch previous results from the database.
            try {
                ResultSet rs = db.execute("select * from tasks order by ID");
                for (int i = 0; rs.isValidRow(); ++i, rs.next()) {
                    tasks.add(new StringIntegerPair<Integer, String>(rs.getFieldAsInt(0), rs.getFieldAsString(1)));
                }
                rs.close();
            } catch (DatabaseException e) {
                MessageBox.alert(e.toString());
            }
        }

        // Display the list of results in a table
        id = tasks.size();

        final RecordDef recordDef = new RecordDef(new FieldDef[] { new IntegerFieldDef("id"), new StringFieldDef("tasks") });

        ArrayReader reader = new ArrayReader(recordDef);
        PagingMemoryProxy proxy = new PagingMemoryProxy(getGridData());
        store = new Store(proxy, reader);

        ColumnConfig configID = new ColumnConfig(null, "id", 15, false, null, "id");
        configID.setHidden(true);
        ColumnConfig config = new ColumnConfig(null, "tasks", 185, false, null, "tasks");
        config.setEditor(new GridEditor(new TextField()));

        ColumnConfig[] columns = { configID, config };

        ColumnModel columnmodel = new ColumnModel(columns);

        final EditorGridPanel grid = new EditorGridPanel();

        toolbar = new Toolbar();
        ToolbarButton button = new ToolbarButton("Add Task...", new ButtonListenerAdapter() {

            public void onClick(Button button, EventObject e) {
                if (!Connectivity.checkForGears()) {
                    Window.alert("Unable to add task: Gears is not installed.");
                    return;
                }
                // int indexID = id;
                String text = "EXAMPLE TASK";
                Record plant = recordDef.createRecord(new Object[] { id, text });
                try {
                    db.execute("insert into tasks(ID,Task) values (" + id + ", '" + text + "')");
                    id++;
                } catch (DatabaseException e1) {
                    MessageBox.alert(e1.toString());
                }
                grid.stopEditing();
                store.insert(id - 1, plant);
                grid.startEditing(id - 1, 1);
            }
        });
        toolbar.addButton(button);

        grid.setStore(store);
        grid.setAutoHeight(true);
        grid.setAutoWidth(true);
        grid.setColumnModel(columnmodel);
        grid.setAutoExpandColumn("tasks");
        grid.setFrame(true);
        grid.setMonitorResize(true);
        grid.setAutoScroll(false);
        grid.setClicksToEdit(1);
        grid.setBorder(false);
        grid.addEditorGridListener(new EditorGridListenerAdapter() {

            public void onAfterEdit(GridPanel grid, Record record, java.lang.String field, java.lang.Object newValue, java.lang.Object oldValue, int rowIndex, int colIndex) {
                try {
                    db.execute("UPDATE tasks SET TASK='" + newValue + "' WHERE ID=" + (rowIndex));
                } catch (DatabaseException e) {
                    MessageBox.alert(e.toString());
                }
            }
        });

        store.load(new UrlParam[] { new UrlParam("rnd", new Date().getTime() + "") });

        return grid;

    }

    public Object[][] getGridData() {
        tasks = new ArrayList<StringIntegerPair<Integer, String>>();
        if (Connectivity.checkForGears() == false) {
            return new Object[0][0];
        }
        
        try {

            ResultSet rs = db.execute("select * from tasks order by ID");
            for (int i = 0; rs.isValidRow(); ++i, rs.next()) {
                tasks.add(new StringIntegerPair<Integer, String>(rs.getFieldAsInt(0), rs.getFieldAsString(1)));
            }
            rs.close();
        } catch (DatabaseException e) {
            MessageBox.alert(e.toString());
        }

        // FIXME With enabled Delete-Feature this might cause problems, ID from table varies from id (row)
        Object[][] result = new Object[tasks.size()][2];

        for (int i = 0; i < tasks.size(); i++) {
            result[i][0] = tasks.get(i).getKey();
            result[i][1] = tasks.get(i).getValue();
        }

        return result;
    }

    public GridPanel getGrid() {
        return this.grid;
    }

    public Toolbar getToolbar() {
        return this.toolbar;
    }

}