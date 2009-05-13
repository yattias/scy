package eu.scy.lab.client.startupview.lastMission;

import java.util.Date;
import java.util.Vector;

import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.SortDir;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.DateFieldDef;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.SortState;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.event.GridRowListener;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.layout.HorizontalLayout;
import com.gwtextux.client.data.PagingMemoryProxy;

import eu.scy.lab.client.date.DateRenderer;
import eu.scy.lab.client.mission.Mission;

public class LastMissionPanel extends Panel {
    
    private Vector<Mission> missions;
    
    private GridPanel grid;
    
    private DateRenderer renderer = new DateRenderer("dd.MM.yyyy");
    
    public LastMissionPanel() {
        
        super("Last Missions");
        onModuleLoad();
        
    }
    
    public void onModuleLoad() {
        
        setLayout(new FitLayout());
        setClosable(false);
        
        PagingMemoryProxy proxy = new PagingMemoryProxy(getGridDataWithDate(getGridData()));
        RecordDef recordDef = new RecordDef(new FieldDef[] { new StringFieldDef("name"), new StringFieldDef("author"), new DateFieldDef("date", "dd.MM.yyyy"),
                // new DateFieldDef("date"),
                new StringFieldDef("relativedate") });
        
        ArrayReader reader = new ArrayReader(recordDef);
        final Store store = new Store(reader);
        // store.setReader();
        store.setDataProxy(proxy);
        // store.setGroupField("relativedate");
        
        ColumnConfig tempDateColumn = new ColumnConfig("Relative Date", "relativedate");
        tempDateColumn.setHidden(true);
        
        ColumnConfig date = new ColumnConfig("Date", "date");
        date.setRenderer(renderer);
        date.setWidth(80);
        date.setSortable(false);
        
        ColumnConfig[] columns = new ColumnConfig[] {
                // column ID is company which is later used in
                new ColumnConfig("Name", "name", 160, false, null, "name"), new ColumnConfig("Author", "author", 160, false), date, tempDateColumn };
        ColumnModel columnModel = new ColumnModel(columns);
        
        store.setInitialSortState(new SortState("date", SortDir.DESC));
        
        // FIXME GroupingView deactivated because GridView is not working
        // GroupingView gridView = new GroupingView();
        // gridView.setForceFit(true);
        // gridView.updateHeaderSortState();
        // gridView
        // .setGroupTextTpl("{text} ({[values.rs.length]} {[values.rs.length > 1 ? \"Items\" : \"Item\"]})");
        
        // The Grid
        // TODO set layout and size
        
        grid = new GridPanel();
        grid.setStore(store);
        grid.setColumnModel(columnModel);
        
        grid.setFrame(false);
        grid.setStripeRows(true);
        grid.setLayout(new HorizontalLayout(0));
        grid.setMonitorResize(true);
        grid.setAutoExpandColumn("name");
        grid.setHeader(false);
        
        // FIXME GroupingView deactivated because GridView is not working
        // gridView.fitColumns(true);
        // grid.setView(gridView);
        
        grid.setIconCls("grid-icon");
        
        grid.addGridRowListener(new GridRowListener() {
            
            public void onRowClick(GridPanel grid, int rowIndex, EventObject e) {
                
                String name = (grid.getSelectionModel().getSelected().getAsString(grid.getSelectionModel().getSelected().getFields()[0]));
                String author = (grid.getSelectionModel().getSelected().getAsString(grid.getSelectionModel().getSelected().getFields()[1]));
                String date = (grid.getSelectionModel().getSelected().getAsString(grid.getSelectionModel().getSelected().getFields()[2]));
                
            }
            
            public void onRowContextMenu(GridPanel grid, int rowIndex, EventObject e) {
                
            }
            
            public void onRowDblClick(GridPanel grid, int rowIndex, EventObject e) {
                
            }
            
        });
        
        Button browseMission = new Button("Browse Mission!", new ButtonListenerAdapter() {
            
            public void onClick(Button button, EventObject e) {
                // TODO Auto-generated method stub
                MessageBox.alert("Starting Missionbrowser when feature is enabled");
            }
        });
        addButton(browseMission);
        
        store.load();
        
        grid.setBufferResize(true);
        
        add(grid);
        setAutoScroll(true);
        
    }
    
    // The local Array-Data to display in the Grid
    @SuppressWarnings("deprecation")
    private Object[][] getGridData() {
        
        return new Object[][] { new Object[] { "Kryptoarithmetics", "Sven", new Date(108, 8, 29) }, new Object[] { "Graphsearch", "Sven M", new Date(108, 8, 21) }, new Object[] { "Kryptoarithmetics II", "Sven", new Date(108, 8, 20) }, new Object[] { "Dancing with animals", "Sven", new Date(108, 8, 21) }, new Object[] { "Kryptoarithmetics III", "Sven", new Date(108, 8, 22) } };
    }
    
    private Object[][] getGridDataWithDate(Object[][] ungroupedData) {
        Object[][] groupedData = new Object[ungroupedData.length][ungroupedData[0].length + 1];
        
        for (int i = 0; i < ungroupedData.length; i++) {
            for (int j = 0; j < ungroupedData[i].length; j++) {
                groupedData[i][j] = ungroupedData[i][j];
            }
            
            Date indexDate = new Date();
            if (groupedData[i][2] instanceof Date) {
                indexDate = (Date) groupedData[i][2];
            }
            groupedData[i][ungroupedData[i].length] = createRelativeDate(indexDate);
        }
        
        return groupedData;
    }
    
    @SuppressWarnings("deprecation")
    private String createRelativeDate(Date date) {
        Date today = new Date();
        // XXX change the relative dates
        if ((date.getDate() == today.getDate()) && (date.getMonth() == today.getMonth()) && (date.getYear() == today.getYear())) {
            return "today";
        } else if ((date.getMonth() == today.getMonth()) && (date.getYear() == today.getYear())) {
            return "this month";
        } else {
            return "older";
        }
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
