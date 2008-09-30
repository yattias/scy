package eu.scy.lab.client.tools.map;


import java.util.Collection;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
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
import com.google.gwt.maps.client.geocode.Geocoder;
import com.google.gwt.maps.client.geocode.LatLngCallback;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.MarkerOptions;
import com.google.gwt.maps.client.overlay.PolyStyleOptions;
import com.google.gwt.maps.client.overlay.Polygon;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Ext;
import com.gwtext.client.core.ExtElement;
import com.gwtext.client.widgets.BoxComponent;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.ToolbarItem;
import com.gwtext.client.widgets.event.ButtonListener;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.TextFieldListener;
import com.gwtext.client.widgets.form.event.TextFieldListenerAdapter;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.menu.Menu;

/**
 * Prototype of a MapTool which allows the user to mark points and areas on a map 
 */
public class CopyOfMapTool extends com.gwtext.client.widgets.Panel {

	public static final String ID = "MapID";
	public static String VERSION = "MapTool 0.1";
	
	// Default location and zoom level result in Europe being shown 
	private static LatLng DEFAULT_POSITION = LatLng.newInstance(48.3416461723746 , 4.21875);
	private static int DEFAUT_ZOOM_LEVEL = 4;
	private static int DEFAULT_DETAILED_ZOOM_LEVEL = 12;
	
	//private static LatLng COLLIDE_POSITION = LatLng.newInstance(51.4279590881704, 6.8000268888473511);
	
	// FIXME: Needs to be static to update the map with the current location via native javascript.
	private static MapWidget map;
	
	private Geocoder geocoder;

//	private Image loading;
//	private ToolbarButton loadingButton;
	private ExtElement element;
	
	public CopyOfMapTool() {
		super("Map"); 
		setBorder(true);
		setId(ID);
		setClosable(true);
		setId("panel");
		
		element = Ext.get("panel");
		
		map = new MapWidget(DEFAULT_POSITION, DEFAUT_ZOOM_LEVEL);
		map.setSize("100%", "100%");

		// Add Controls for Zooming, changing MapType and some actions
		map.addControl(new SmallZoomControl());
		map.addControl(new MapTypeControl());
		map.addControl(new CreatePolygonControl());
		map.addControl(new AddMarkerControl());

		// Build the top Panel with Location bar...
//		HorizontalPanel topPanel = new HorizontalPanel();
		Toolbar topPanel = new Toolbar();
		
//		topPanel.add(getLocationFormPanel());
		
		final TextField addressBox = new TextField();
		addressBox.setEmptyText("insert adress...");
		topPanel.addField(addressBox);
		final FormPanel formPanel = new FormPanel();
		formPanel.setAction("#");

		addressBox.addListener(new TextFieldListenerAdapter(){

			public void onSpecialKey(Field field, EventObject e) {
				if (e.getKey()==EventObject.RETURN){
					formPanel.submit();
				}
				
			}
		});
		
		FlowPanel formElements = new FlowPanel();
		
//		formElements.add(addressBox);
	    
		ToolbarButton submitButton = new ToolbarButton("Go!");
		topPanel.addButton(submitButton);
		topPanel.addSeparator();
		submitButton.addListener(new ButtonListenerAdapter() {
			
			public void onClick(com.gwtext.client.widgets.Button button,
					EventObject e) {
				formPanel.submit();
			}
		});
			
//		formElements.add(submitButton);
		formPanel.add(formElements);
		formPanel.addFormHandler(new FormHandler() {
			public void onSubmit(FormSubmitEvent event) {
				showAddress(addressBox.getText());
				event.setCancelled(true);
			}

			public void onSubmitComplete(FormSubmitCompleteEvent event) {}
		});
		
		// ... Button for current location...

		ToolbarButton currentLocationButton = new ToolbarButton("?");

		currentLocationButton.addListener(new ButtonListenerAdapter() {
			
			
			public void onClick(com.gwtext.client.widgets.Button button,
					EventObject e) {
				gotoCurrentPositionJSNI();
			}
		});
		topPanel.addButton(currentLocationButton);
		topPanel.addSeparator();

		if (MapServiceSwitch.getInstance().checkForGears()) {
			final ToolbarButton offlineButton = new ToolbarButton("go off");
			offlineButton.addListener(new ButtonListenerAdapter() {
				public void onClick(com.gwtext.client.widgets.Button button,
						EventObject e) {
					boolean b = !MapServiceSwitch.getInstance().getOnline();
					if (!b) {
						offlineButton.setText("go on");
					} else {
						offlineButton.setText("go off");
					}
					MapServiceSwitch.getInstance().setOnline(b);
				}
			});
			topPanel.addButton(offlineButton);
		}
		
		// .. and loading indicator
//		loading = new Image("js/ext/resources/images/default/shared/blue-loading.gif");
//		loading.setPixelSize(16, 16);
//		element.unmask();
//		stopLoadingIndicator();
//		loadingButton = new ToolbarButton();
//		loadingButton.setDisabled(true);
//		loadingButton.setIcon("js/ext/resources/images/default/shared/blue-loading.gif");
//		topPanel.addButton(loadingButton);
		
		// Put Top Panel and Map together
		//setSpacing(10);
		//FIXME no static size
//		setSize(500, 400);
		add(topPanel);
		add(map);
		
		geocoder = new Geocoder();
		addSavedMarkers();
	}

	/**
	 * Needed as a rather ugly hack to work around issues
	 * adding a MapWidget into a gwt-ext Panel
	 * see http://code.google.com/p/gwt-google-apis/issues/detail?id=127
	 */
	public void init() {
		map.setVisible(true);
		map.checkResize();
	}

	private void addSavedMarkers() {
		MapServiceAsync mapService = (MapServiceAsync) GWT.create(MapService.class);
		
		AsyncCallback<Collection<MarkerBean>> callback = new AsyncCallback<Collection<MarkerBean>>() {

			public void onFailure(Throwable caught) {
				GWT.log("Could nod get markers from server.", caught);
			}

			public void onSuccess(Collection<MarkerBean> result) {
				GWT.log("Got markers from server. Adding them to map.",  null);
				for (MarkerBean bean : result) {
					Marker marker = new Marker( LatLng.newInstance(bean.getLatitude(), bean.getLongitude()));
					marker.addMarkerClickHandler(new MyMarkerClickHandler(map, marker));
					map.addOverlay(marker);
				}
			}
			
		};
		mapService.getMarkers(callback);
	}

	private Widget getLocationFormPanel() {
		final FormPanel formPanel = new FormPanel();
		formPanel.setAction("#");
		FlowPanel formElements = new FlowPanel();
		final TextBox addressBox = new TextBox();
		addressBox.setVisibleLength(55);
		addressBox.setText("");
		addressBox.addKeyboardListener(new KeyboardListener() {
			public void onKeyUp(Widget sender, char keyCode, int modifiers) {}
			public void onKeyDown(Widget sender, char keyCode, int modifiers) {}

			public void onKeyPress(Widget sender, char keyCode, int modifiers) {
				if (keyCode == KEY_ENTER) {
					formPanel.submit();
				}
			}
		});
		formElements.add(addressBox);
	    
		Button submitButton = new Button("Go!");
		submitButton.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				formPanel.submit();
			}
		});
		formElements.add(submitButton);
		formPanel.add(formElements);
		formPanel.addFormHandler(new FormHandler() {
			public void onSubmit(FormSubmitEvent event) {
				showAddress(addressBox.getText());
				event.setCancelled(true);
			}

			public void onSubmitComplete(FormSubmitCompleteEvent event) {}
		});
		return formPanel;
	}

	private void stopLoadingIndicator() {
//		loadingButton.setVisible(false);
//		loadingButton.hide();
	}

	private void startLoadingIndicator() {
//		loadingButton.setVisible(true);
//		loadingButton.show();
	}
	
	private void showAddress(final String address) {
		element.mask("loading...");
//		startLoadingIndicator();
		final InfoWindow info = map.getInfoWindow();
		geocoder.getLatLng(address, new LatLngCallback() {
			public void onFailure() {
				MessageBox.alert("Adress not found: " + address);
//				stopLoadingIndicator();
//				element.unmask();
			}

			public void onSuccess(LatLng point) {
				map.setCenter(point, DEFAULT_DETAILED_ZOOM_LEVEL);
				final Marker marker = new Marker(point);
				map.addOverlay(marker);
				marker.addMarkerClickHandler(new MarkerClickHandler() {
					public void onClick(MarkerClickEvent event) {
						info.open(marker, new InfoWindowContent(address));
					}
				});
//				stopLoadingIndicator();
//				element.unmask();
			}
		});
	}
    
	public native void gotoCurrentPositionJSNI() /*-{
	 	//FIXME: Normally should also check for google.gears, but I have no idea on how to get that object from here
		if (!$wnd.google ) {
  			//location.href = "http://gears.google.com/?action=install&message=<your welcome message>"&return=<your website url>";
          	alert("Unable to get your current position: Gears is not installed.");
          	return;
        }
	  	try {
	    	var geolocation = $wnd.google.gears.factory.create('beta.geolocation');
	    	// For debugging use: alert('your position: ' + p.latitude + ', ' + p.longitude)
	    	geolocation.getCurrentPosition( function(p) { @eu.scy.lab.client.tools.map.MapTool::gotoPosition(DD)(p.latitude,p.longitude); },
	                                   		function(err) { alert('Error retrieving your location: ' + err.message);},
						           			{ enableHighAccuracy: true,
	                                     	gearsRequestAddress: true });
	  	} catch (e) {
	    	alert('Error using Geolocation API: ' + e.message);
	    	return;
	  	}
	}-*/;

	public static void gotoPosition(double latitude, double longitude) {
		LatLng latLng = LatLng.newInstance(latitude, longitude);
		map.setCenter(latLng, DEFAULT_DETAILED_ZOOM_LEVEL);
	}

	/**
	 * Enables the user to draw Polygons on the map 
	 */
	private class CreatePolygonControl extends CustomControl {

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
	private class AddMarkerControl extends CustomControl {

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
							
							MarkerBean bean = new MarkerBean(event.getLatLng().getLatitude(), event.getLatLng().getLongitude());
							
							MapServiceAsync mapService = (MapServiceAsync) GWT.create(MapService.class);
							AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
								// FIXME: Error handling
								public void onFailure(Throwable caught) { GWT.log("Could not add marker.", caught); }
								public void onSuccess(Boolean result) {}
								
							};
							mapService.addMarker(bean, callback );
							
							marker.addMarkerClickHandler(new MyMarkerClickHandler(map, marker));
							
							map.addOverlay(marker);
							map.removeMapClickHandler(this);
						} 
					});
				}
			});
			return addMarkerButton;
		}
	}
	
	
}
