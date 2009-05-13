package eu.scy.lab.client.desktop.workspace.elobrowser;

import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.RegionPosition;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.PagingToolbar;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.ToolTip;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.FieldListenerAdapter;
import com.gwtext.client.widgets.form.event.TextFieldListenerAdapter;
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
    
    private Store store;
    
    private TextField searchField;
    
    private ColumnModel columnModel;
    
    public EloBrowser() {
        super("ELO-Browser");
        // FIXME only fixed height works, perhaps need a fitting wrapper panel
        setClosable(false);
        setLayout(new BorderLayout());
        
        setPaddings(15);
        
        PagingMemoryProxy proxy = new PagingMemoryProxy(new String[0][0]);
        RecordDef recordDef = new RecordDef(new FieldDef[] { new StringFieldDef("title"), new StringFieldDef("author"), new StringFieldDef("date"), new StringFieldDef("version"), new StringFieldDef("id") });
        
        ArrayReader reader = new ArrayReader(recordDef);
        store = new Store(proxy, reader, false);
        
        ColumnConfig[] columns = new ColumnConfig[] {
                // column ID is company which is later used in setAutoExpandColumn
                new ColumnConfig("Title", "title", 160, true, null, "title"), new ColumnConfig("Author", "author", 160, true), new ColumnConfig("Date", "date", 75), new ColumnConfig("Version", "version", 100), new ColumnConfig("Id", "id", 100) };
        
        // The Grid
        columnModel = new ColumnModel(columns);
        
        grid = new GridPanel();
        grid.setStore(store);
        grid.setColumnModel(columnModel);
        
        grid.setFrame(true);
        grid.setStripeRows(true);
        grid.setLayout(new HorizontalLayout(0));
        grid.setHeight(300);
        grid.setMonitorResize(true);
        grid.setAutoExpandColumn("title");
        grid.setTitle("Browse ELOs"); // TODO: i18n
        
        final PagingToolbar pagingToolbar = new PagingToolbar(store);
        pagingToolbar.setPageSize(5);
        pagingToolbar.setDisplayInfo(true);
        pagingToolbar.setDisplayMsg("Displaying companies {0} - {1} of {2}");
        pagingToolbar.setEmptyMsg("No records to display");
        
        grid.addGridRowListener(new GridRowListener() {
            
            public void onRowClick(GridPanel grid, int rowIndex, EventObject e) {
                
                String title = (grid.getSelectionModel().getSelected().getAsString(grid.getSelectionModel().getSelected().getFields()[0]));
                String author = (grid.getSelectionModel().getSelected().getAsString(grid.getSelectionModel().getSelected().getFields()[1]));
                String date = (grid.getSelectionModel().getSelected().getAsString(grid.getSelectionModel().getSelected().getFields()[2]));
                String version = (grid.getSelectionModel().getSelected().getAsString(grid.getSelectionModel().getSelected().getFields()[3]));
                String id = (grid.getSelectionModel().getSelected().getAsString(grid.getSelectionModel().getSelected().getFields()[4]));
                previewPanel.update(title, author, date, version, id);
                
            }
            
            public void onRowContextMenu(GridPanel grid, int rowIndex, EventObject e) {
                
            }
            
            public void onRowDblClick(GridPanel grid, int rowIndex, EventObject e) {
                
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
        
        searchField = new TextField();
        Toolbar topToolbar = new Toolbar();
        topToolbar.addField(searchField);
        
        final SearchButtonListener searchListener = new SearchButtonListener(grid, searchField);
        final ToolbarButton searchButton = new ToolbarButton("Search!", searchListener);
        searchButton.setId("search");
        
        searchField.addListener(new TextFieldListenerAdapter() {
            
            public void onSpecialKey(Field field, EventObject e) {
                if (e.getKey() == EventObject.RETURN) {
                    searchListener.onClick(searchButton, e);
                }
            }
        });
        topToolbar.addButton(searchButton);
        grid.setTopToolbar(topToolbar);
        
        // adding the Grid-Panel to the ELO-Browser
        BorderLayoutData centerData = new BorderLayoutData(RegionPosition.CENTER);
        
        centerData.setMinHeight(200);
        centerData.setMaxSize(300);
        
        add(grid, centerData);
        setAutoScroll(true);
        
        // adding the Preview-Panel to the ELO Browser
        previewPanel = new PreviewPanel();
        BorderLayoutData southData = new BorderLayoutData(RegionPosition.SOUTH);
        southData.setSplit(true);
        southData.setSplitTip("Drag to resize");
        southData.setMinHeight(150);
        previewPanel.setHeight(200);
        add(previewPanel.getPreviewPanel(), southData);
        
    }
    
    /**
     * @return the store
     */
    public Store getStore() {
        return store;
    }
    
    /**
     * @param store
     *            the store to set
     */
    public void setStore(Store store) {
        this.store = store;
    }
}
