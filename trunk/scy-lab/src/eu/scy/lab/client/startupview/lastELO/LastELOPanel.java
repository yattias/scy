package eu.scy.lab.client.startupview.lastELO;

import com.gwtext.client.widgets.Panel;
import com.gwtext.client.core.SortDir;
import com.gwtext.client.core.TextAlign;
import com.gwtext.client.data.*;
import com.gwtext.client.util.DateUtil;
import com.gwtext.client.util.Format;
import com.gwtext.client.widgets.*;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.event.PanelListenerAdapter;
import com.gwtext.client.widgets.grid.*;

import java.util.Date;

public class LastELOPanel extends Panel {

	public LastELOPanel() {
		super("Last ELOs");
		onModuleLoad();
	}

	private GridPanel grid;
	private boolean showPreview = false;

	private Renderer renderTopic = new Renderer() {
		public String render(Object value, CellMetadata cellMetadata,
				Record record, int rowIndex, int colNum, Store store) {
			return Format
					.format(
							"<b><a href=\"http://extjs.com/forum/showthread.php?t={2}\" target=\"_blank\">{0}</a></b>  <a href=\"http://extjs.com/forum/forumdisplay.php?f={3}\" target=\"_blank\">{1} Forum</a>",
							new String[] { (String) value,
									record.getAsString("forumtitle"),
									record.getId(),
									record.getAsString("forumid"), });
		}
	};

	private Renderer renderLast = new Renderer() {
		public String render(Object value, CellMetadata cellMetadata,
				Record record, int rowIndex, int colNum, Store store) {
			Date lastPost = record.getAsDate("lastpost");
			String lastPostStr = DateUtil.format(lastPost, "M j, Y, g:i a");
			return Format.format("{0}<br/>by {1}", new String[] { lastPostStr,
					record.getAsString("lastposter") });
		}
	};

	public void onModuleLoad() {
		Panel panel = new Panel();
		panel.setBorder(false);
//		panel.setPaddings(15);

		DataProxy dataProxy = new ScriptTagProxy(
				"http://extjs.com/forum/topics-browse-remote.php");

		final RecordDef recordDef = new RecordDef(
				new FieldDef[] { new StringFieldDef("title"),
						new StringFieldDef("forumtitle"),
						new StringFieldDef("forumid"),
						new StringFieldDef("author"),
						new IntegerFieldDef("replycount"),
						new DateFieldDef("lastpost", "lastpost", "timestamp"),
						new StringFieldDef("lastposter"),
						new StringFieldDef("excerpt") });
		JsonReader reader = new JsonReader(recordDef);
		reader.setRoot("topics");
		reader.setTotalProperty("totalCount");
		reader.setId("threadid");

		final Store store = new Store(dataProxy, reader, true);
		store.setDefaultSort("lastpost", SortDir.DESC);

		ColumnConfig topicColumn = new ColumnConfig("Topic", "title", 420,
				false, renderTopic, "topic");
		topicColumn.setCss("white-space:normal;");

		ColumnConfig authorColumn = new ColumnConfig("Author", "author", 100);
		authorColumn.setHidden(true);

		ColumnConfig repliesColumn = new ColumnConfig("Replies", "replycount",
				70);
		repliesColumn.setAlign(TextAlign.RIGHT);

		ColumnConfig lastPostColumn = new ColumnConfig("Last Post", "lastPost",
				150, true, renderLast, "last");

		ColumnModel columnModel = new ColumnModel(new ColumnConfig[] {
				topicColumn, authorColumn, repliesColumn, lastPostColumn });
		columnModel.setDefaultSortable(true);

		grid = new GridPanel();
		grid.setWidth(450);
		grid.setHeight(340);
		grid.setHeader(false);
//		grid.setTitle("Remote Paging Grid ");
		grid.setStore(store);
		grid.setColumnModel(columnModel);
		grid.setTrackMouseOver(false);
		grid.setLoadMask(true);
		grid.setSelectionModel(new RowSelectionModel());
		grid.setFrame(true);
		grid.setStripeRows(true);
		grid.setIconCls("grid-icon");
		
//		showPreview = false;
//		grid.getView().refresh();

		GridView view = new GridView() {
			public String getRowClass(Record record, int index,
					RowParams rowParams, Store store) {
				if (showPreview) {
					rowParams.setBody(Format.format("<p>{0}</p>", record
							.getAsString("excerpt")));
					return "x-grid3-row-expanded";
				} else {
					return "x-grid3-row-collapsed";
				}
			}
		};
		view.setForceFit(true);
		view.setEnableRowBody(true);
		grid.setView(view);

		PagingToolbar pagingToolbar = new PagingToolbar(store);
		pagingToolbar.setPageSize(25);
		pagingToolbar.setDisplayInfo(true);
//		pagingToolbar.setDisplayMsg("Displaying topics {0} - {1} of {2}");
		pagingToolbar.setEmptyMsg("No topics to display");

		pagingToolbar.addSeparator();
		ToolbarButton toolbarButton = new ToolbarButton("Show Preview");
		toolbarButton.setPressed(showPreview);
		toolbarButton.setEnableToggle(true);
//		toolbarButton.setCls("x-btn-text-icon details");
		toolbarButton.addListener(new ButtonListenerAdapter() {
			public void onToggle(Button button, boolean pressed) {
				toggleDetails(pressed);
			}
		});

		pagingToolbar.addButton(toolbarButton);
		grid.setBottomToolbar(pagingToolbar);

		grid.addListener(new PanelListenerAdapter() {
			public void onRender(Component component) {
				store.load(0, 25);
			}
		});
		panel.add(grid);

		add(panel);
	}

	private void toggleDetails(boolean pressed) {
		showPreview = pressed;
		grid.getView().refresh();
	}
}
