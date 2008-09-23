package eu.scy.lab.client.tools.map;


import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.maps.client.InfoWindow;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.control.ControlAnchor;
import com.google.gwt.maps.client.control.ControlPosition;
import com.google.gwt.maps.client.control.MapTypeControl;
import com.google.gwt.maps.client.control.SmallZoomControl;
import com.google.gwt.maps.client.control.Control.CustomControl;
import com.google.gwt.maps.client.event.MapClickHandler;
import com.google.gwt.maps.client.event.MarkerClickHandler;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.MarkerOptions;
import com.google.gwt.maps.client.overlay.PolyStyleOptions;
import com.google.gwt.maps.client.overlay.Polygon;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Prototype of a MapTool which allows the user to mark points and areas on a map 
 */
public class MapTool implements EntryPoint {

	private MapWidget map;
	private VerticalPanel panel;

	// Default Position: Collide, University of Duisburg-Essen
	public static LatLng DEFAULT_POSITION = LatLng.newInstance(51.4279590881704, 6.8000268888473511);
	
	public MapTool() {
		map = new MapWidget(DEFAULT_POSITION, 14);
		map.setSize("500px", "400px");

		// Add Controls
		map.addControl(new SmallZoomControl());
		map.addControl(new MapTypeControl());

		map.addControl(new CreatePolygonControl());
		map.addControl(new AddMarkerControl());

		// Set a marker on the Default Position
		final InfoWindow info = map.getInfoWindow();
		final Marker marker = new Marker(DEFAULT_POSITION);
		marker.addMarkerClickHandler(new MarkerClickHandler() {
			public void onClick(MarkerClickEvent event) {
				info.open(marker, new InfoWindowContent("Home of COLLIDE"));
			}
		});
		map.addOverlay(marker);

		panel = new VerticalPanel();
		panel.setSpacing(10);
		panel.add(map);
	}

	public void onModuleLoad() {
		RootPanel.get().add(new MapTool().getPanel());
	}

	public Widget getPanel() {
		return panel;
	}

	/**
	 * Enables the user to draw Polygons on the map 
	 */
	private static class CreatePolygonControl extends CustomControl {

		public CreatePolygonControl() {
			super(new ControlPosition(ControlAnchor.TOP_RIGHT, 230, 5));
		}

		@Override
		public boolean isSelectable() {
			return false;
		}

		@Override
		protected Widget initialize(final MapWidget map) {
			Button markAreaButton = new Button("Mark Area");
			markAreaButton.setStyleName("markareacontrol");

			markAreaButton.addClickListener(new ClickListener() {
				public void onClick(Widget sender) {
					String color = "0000FF";
					int weight = 1;
					double opacity = 0.8;
					boolean fillFlag = true;

					PolyStyleOptions style = PolyStyleOptions.newInstance(color, weight, opacity);
					final Polygon poly = new Polygon(new LatLng[0], color, weight, opacity, color, fillFlag ? .7 : 0.0);
					poly.setStrokeStyle(style);
					map.addOverlay(poly);
					poly.setDrawingEnabled();
				}
			});
			return markAreaButton;
		}
	}

	/**
	 * Enables the user to add markers to the map
	 */
	private static class AddMarkerControl extends CustomControl {

		public AddMarkerControl() {
			super(new ControlPosition(ControlAnchor.TOP_RIGHT, 350, 5));
		}

		@Override
		public boolean isSelectable() {
			return false;
		}

		@Override
		protected Widget initialize(final MapWidget map) {
			Button addMarkerButton = new Button("Add Marker");
			addMarkerButton.setStyleName("addmarkercontrol");

			addMarkerButton.addClickListener(new ClickListener() {
				public void onClick(Widget sender) {
					
					map.addMapClickHandler(new MapClickHandler() {
						
						public void onClick(MapClickEvent event) {
							MarkerOptions options = MarkerOptions.newInstance();
							options.setDraggable(true);
							final Marker marker = new Marker(event.getLatLng(), options);
							marker.addMarkerClickHandler(new MarkerClickHandler() {
								public void onClick(MarkerClickEvent event) {
									VerticalPanel panel = new VerticalPanel();
									TextArea textArea = new TextArea();
									textArea.setVisibleLines(5);
									panel.add(textArea);
									Button saveButton = new Button("Save");
									saveButton.addClickListener(new ClickListener() {
										public void onClick(Widget sender) {
											System.out.println("SaveButton clicked");
										}
										
									});
									panel.add(saveButton);
									InfoWindowContent content = new InfoWindowContent(panel);
									map.getInfoWindow().open(marker, content);
								}
								
							});
							map.addOverlay(marker);
							map.removeMapClickHandler(this);
						} });
				}
			});
			return addMarkerButton;
		}
	}
}
