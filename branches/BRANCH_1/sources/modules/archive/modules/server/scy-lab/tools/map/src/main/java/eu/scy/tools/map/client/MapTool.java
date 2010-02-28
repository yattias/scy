package eu.scy.tools.map.client;

import java.util.Collection;

import com.google.gwt.core.client.GWT;
import com.google.gwt.maps.client.InfoWindow;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.control.MapTypeControl;
import com.google.gwt.maps.client.control.SmallZoomControl;
import com.google.gwt.maps.client.event.MapClickHandler;
import com.google.gwt.maps.client.event.MarkerClickHandler;
import com.google.gwt.maps.client.geocode.Geocoder;
import com.google.gwt.maps.client.geocode.LatLngCallback;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.MarkerOptions;
import com.google.gwt.maps.client.overlay.PolyStyleOptions;
import com.google.gwt.maps.client.overlay.Polygon;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.TextFieldListenerAdapter;

/**
 * Prototype of a MapTool which allows the user to mark points and areas on a map
 */
public class MapTool extends com.gwtext.client.widgets.Panel {

    public static final String TOOL_ID = "MapID";

    public static String VERSION = "MapTool 0.1";

    // Default location and zoom level result in Europe being shown
    private static LatLng DEFAULT_POSITION = LatLng.newInstance(48.3416461723746, 4.21875);

    private static int DEFAUT_ZOOM_LEVEL = 4;

    private static int DEFAULT_DETAILED_ZOOM_LEVEL = 12;

    // private static LatLng COLLIDE_POSITION = LatLng.newInstance(51.4279590881704, 6.8000268888473511);

    // FIXME: Needs to be static to update the map with the current location via native javascript.
    private static MapWidget map;

    private Marker shownLocation;
    
    private Geocoder geocoder;

    private Toolbar toolbar;

    public MapTool() {
        super("Map");
        setBorder(true);
        setId(TOOL_ID);
        setClosable(true);
        
        createMap();
        createToolbar();
        
        geocoder = new Geocoder();
        addSavedMarkers();
        
        // FIXME: This is a rather ugly hack to work around issues
        // adding a MapWidget into a gwt-ext Panel
        // see http://code.google.com/p/gwt-google-apis/issues/detail?id=127
        Timer t = new Timer() {
            
            @Override
            public void run() {
                init();
            }
        };
        t.schedule(500);
    }

    private void createMap() {
        map = new MapWidget(DEFAULT_POSITION, DEFAUT_ZOOM_LEVEL);
        map.setSize("100%", "100%");

        // Add Controls for Zooming, changing MapType and some actions
        map.addControl(new SmallZoomControl());
        map.addControl(new MapTypeControl());
        add(map);
    }

    private void createToolbar() {
        toolbar = new Toolbar();

        // Setup the form to handle user input
        final FormPanel formPanel = new FormPanel();
        formPanel.setAction("#");
        FlowPanel formElements = new FlowPanel();
        formPanel.add(formElements);

        // ... Button for current location...
        ToolbarButton currentLocationButton = new ToolbarButton("?");
        currentLocationButton.setTooltip("Centers the map on your current location");
        currentLocationButton.addListener(new ButtonListenerAdapter() {

            public void onClick(com.gwtext.client.widgets.Button button, EventObject e) {
                if (checkForGears()) {
                    //LoadIndicator.start("Getting your location...");
                    gotoCurrentPositionJSNI();
                } else {
                    Window.alert("Could not get your current Location: Gears is not installed.");
                }
            }
        });
        toolbar.addButton(currentLocationButton);
        toolbar.addSeparator();
        
        // TextField to enter Adresses
        final TextField addressBox = new TextField();
        addressBox.setEmptyText("insert adress...");
        addressBox.setWidth(180);
        toolbar.addField(addressBox);
        addressBox.addListener(new TextFieldListenerAdapter() {

            public void onSpecialKey(Field field, EventObject e) {
                if (e.getKey() == EventObject.RETURN) {
                    formPanel.submit();
                }
            }
        });

        // configure form action
        formPanel.addFormHandler(new FormHandler() {

            public void onSubmit(FormSubmitEvent event) {
                if (addressBox.getText().equals("")) {
                    Window.alert("Please enter an address.");
                } else {
                    showAddress(addressBox.getText());
                }
                event.setCancelled(true);
            }

            public void onSubmitComplete(FormSubmitCompleteEvent event) {}
        });
        
        ToolbarButton submitButton = new ToolbarButton("Go!");
        toolbar.addButton(submitButton);
        toolbar.addSeparator();
        submitButton.addListener(new ButtonListenerAdapter() {

            public void onClick(com.gwtext.client.widgets.Button button, EventObject e) {
                formPanel.submit();
            }
        });

        ToolbarButton addMarker = new ToolbarButton("Add Marker");
        addMarker.addListener( new AddMarkerListener(map) );
        toolbar.addButton(addMarker);
        toolbar.addSeparator();
 
        ToolbarButton createPolygon = new ToolbarButton("Mark Area");
        createPolygon.addListener( new MarkAreaListener() );
        toolbar.addButton(createPolygon);
        
        setTopToolbar(toolbar);
    }

    // FIXME: will be added to official api, replace when released
    public static native boolean checkForGears() /*-{
        //FIXME: Normally should also check for google.gears, but I have no idea on how to get that object from here
        if ($wnd.google) {
            return true;
        } else {
            return false;
        }
    }-*/;
    
    /**
     * Needed as a rather ugly hack to work around issues adding a MapWidget into a gwt-ext Panel see http://code.google.com/p/gwt-google-apis/issues/detail?id=127
     */
    public void init() {
        if (map != null) {
            map.setVisible(true);
            map.checkResize();
        }
    }

    private void addSavedMarkers() {
        AsyncCallback<Collection<MarkerBean>> callback = new AsyncCallback<Collection<MarkerBean>>() {

            public void onFailure(Throwable caught) {
                GWT.log("Could nod get markers from server.", caught);
            }

            public void onSuccess(Collection<MarkerBean> result) {
                GWT.log("Got markers from server. Adding them to map.", null);
                for (MarkerBean markerBean : result) {
                    MarkerOptions options = MarkerOptions.newInstance();
                    options.setDraggable(true);
                    Marker marker = new Marker(LatLng.newInstance(markerBean.getLatitude(), markerBean.getLongitude()), options);
                    marker.addMarkerClickHandler(new ShowMarkerInfoHandler(map, marker));
                    marker.addMarkerDragEndHandler( new UpdateMarkerPositionHandler(marker, markerBean));
                    map.addOverlay(marker);
                }
            }

        };
        MapServiceSwitch.getInstance().getMarkers(callback);
    }

    private void showAddress(final String address) {
        final InfoWindow info = map.getInfoWindow();
        //LoadIndicator.start();
        
        geocoder.getLatLng(address, new LatLngCallback() {

            public void onFailure() {
                //LoadIndicator.stop();
                MessageBox.alert("Adress not found: " + address);
            }

            public void onSuccess(LatLng point) {
                //LoadIndicator.stop();
                if (shownLocation != null) {
                    map.removeOverlay(shownLocation);
                }
                map.setCenter(point, DEFAULT_DETAILED_ZOOM_LEVEL);
                shownLocation = new Marker(point);
                map.addOverlay(shownLocation);
                shownLocation.addMarkerClickHandler(new MarkerClickHandler() {

                    public void onClick(MarkerClickEvent event) {
                        info.open(shownLocation, new InfoWindowContent(address));
                    }
                });
            }
        });
    }

    public native void gotoCurrentPositionJSNI() /*-{
        try {
            var geolocation = $wnd.google.gears.factory.create('beta.geolocation');
            // For debugging use: alert('your position: ' + p.latitude + ', ' + p.longitude)
            geolocation.getCurrentPosition( function(p) { @eu.scy.tools.map.client.MapTool::gotoPosition(DD)(p.latitude,p.longitude); },
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
        //LoadIndicator.stop();
    }

    /**
     * Enables the user to draw Polygons on the map
     */
    private class MarkAreaListener extends ButtonListenerAdapter {

        public void onClick(com.gwtext.client.widgets.Button button, EventObject e) {
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
    }

    /**
     * Enables the user to add markers to the map
     */
    private class AddMarkerListener extends ButtonListenerAdapter {

        private MapWidget map;
        
        public AddMarkerListener(MapWidget map) {
            this.map = map;
        }

        public void onClick(com.gwtext.client.widgets.Button button, EventObject e) {
            map.addMapClickHandler(new MapClickHandler() {

                public void onClick(MapClickEvent event) {

                    MarkerOptions options = MarkerOptions.newInstance();
                    options.setDraggable(true);
                    final Marker marker = new Marker(event.getLatLng(), options);

                    final MarkerBean markerBean = new MarkerBean(event.getLatLng().getLatitude(), event.getLatLng().getLongitude());

                    AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {

                        // FIXME: Error handling
                        public void onFailure(Throwable caught) {
                            GWT.log("Could not add marker.", caught);
                        }

                        public void onSuccess(Boolean result) {}

                    };
                    MapServiceSwitch.getInstance().addMarker(markerBean, callback);

                    marker.addMarkerClickHandler(new ShowMarkerInfoHandler(map, marker));
                    marker.addMarkerDragEndHandler(new UpdateMarkerPositionHandler(marker, markerBean) );
                    
                    map.addOverlay(marker);
                    map.removeMapClickHandler(this);
                }
            });
        }
    }
}
