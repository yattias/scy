package info.collide.android.scydatacollector;

import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class DataCollectorMapViewActivity extends MapActivity {

    MapView mapView;

    MapController mc;

    GeoPoint p;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        class MapOverlay extends com.google.android.maps.Overlay {

            public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when) {
                super.draw(canvas, mapView, shadow);

                // ---translate the GeoPoint to screen pixels---
                Point screenPts = new Point();
                mapView.getProjection().toPixels(p, screenPts);

                // ---add the marker---
                Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.mappin);
                canvas.drawBitmap(bmp, screenPts.x, screenPts.y - 50, null);
                return true;
            }

            public boolean onTouchEvent(MotionEvent event, MapView mapView) {
                // ---when user lifts his finger---
                if (event.getAction() == 1) {
                    p = mapView.getProjection().fromPixels((int) event.getX(), (int) event.getY());
                    mc.animateTo(p);
                    Toast.makeText(getBaseContext(), "Lat:" + p.getLatitudeE6() / 1E6 + "," + "Lng:" + p.getLongitudeE6() / 1E6, Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapview);

        ImageButton btnMapViewOK = (ImageButton) findViewById(R.id.btnMapViewClose);
        btnMapViewOK.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {

                String coords[] = { String.valueOf(p.getLatitudeE6() / 1E6), String.valueOf(p.getLongitudeE6() / 1E6) };
                Bundle bundle = new Bundle();
                bundle.putStringArray("datagps", coords);
                Intent mIntent = new Intent();
                mIntent.putExtras(bundle);
                setResult(RESULT_OK, mIntent);
                finish();
            }
        });

        ImageButton btnMapViewCancel = (ImageButton) findViewById(R.id.btnMapViewCancel);
        btnMapViewCancel.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {

                Bundle extras = getIntent().getExtras();

                String coordinates[] = extras.getStringArray("datagps");

                Bundle bundle = new Bundle();
                bundle.putStringArray("datagps", coordinates);
                Intent mIntent = new Intent();
                mIntent.putExtras(bundle);
                setResult(RESULT_OK, mIntent);
                finish();
            }
        });

        mapView = (MapView) findViewById(R.id.mapView);
        // LinearLayout zoomLayout = (LinearLayout) findViewById(R.id.zoom);
        // View zoomView =
        mapView.setBuiltInZoomControls(true);

        // zoomLayout.addView(zoomView, new LinearLayout.LayoutParams(
        // LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        // mapView.displayZoomControls(true);

        mc = mapView.getController();

        Bundle extras = getIntent().getExtras();

        String coordinates[] = extras.getStringArray("datagps");

        double lat = Double.parseDouble(coordinates[0]);
        double lng = Double.parseDouble(coordinates[1]);

        p = new GeoPoint((int) (lat * 1E6), (int) (lng * 1E6));

        mc.animateTo(p);

        mc.setZoom(17);
        mapView.setSatellite(true);

        // ---Add a location marker---
        MapOverlay mapOverlay = new MapOverlay();
        List<Overlay> listOfOverlays = mapView.getOverlays();
        listOfOverlays.clear();
        listOfOverlays.add(mapOverlay);

        mapView.invalidate();
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }

}
