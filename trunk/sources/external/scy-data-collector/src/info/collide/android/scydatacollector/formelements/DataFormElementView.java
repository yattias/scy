package info.collide.android.scydatacollector.formelements;

import info.collide.android.scydatacollector.DataCollectorFormActivity;
import info.collide.android.scydatacollector.DataCollectorFormViewDetailsActivity;
import info.collide.android.scydatacollector.DataFormElementEventModel;
import info.collide.android.scydatacollector.DataFormElementModel;
import info.collide.android.scydatacollector.DataFormElementModel.DataFormElementTypes;

import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.format.Time;
import android.view.Display;
import android.view.WindowManager;
import android.widget.LinearLayout;

public abstract class DataFormElementView extends LinearLayout implements Observer {

    private DataFormElementModel elementModel;

    public final static int IMAGE_TAKEN = 1;

    protected DataCollectorFormActivity application;

    public int Column1width = 80;

    public int Column2width = 55;

    public int Column3width = 55;

    public int Column4width = 55;

    public int Column5width = 70;

    public int RowMaxHeight = 50;

    public DataFormElementView(DataFormElementModel elementModel, DataCollectorFormActivity application) {
        super(application);
        
        this.application = application;
        
        Display display = ((WindowManager) application.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = display.getWidth();
        if (width <= 320) {
            Column1width = 80;
            Column2width = 55;
            Column3width = 55;
            Column4width = 55;
            Column5width = 70;
        }
        if (width > 320) {
            Column1width = (int) (80 * 1.5);
            Column2width = (int) (55 * 1.5);
            Column3width = (int) (55 * 1.5);
            Column4width = (int) (55 * 1.5);
            Column5width = (int) (70 * 1.5);
        }
        this.setDfem(elementModel);
    }

    public DataCollectorFormActivity getApplication() {
        return application;
    }

    public void setDfem(DataFormElementModel dfem) {
        this.elementModel = dfem;
    }

    public DataFormElementModel getDfem() {
        return elementModel;
    }

    public DataFormElementTypes getDataFormElementViewType() {
        return getDfem().getType();
    }

    public void events(DataFormElementEventModel.DataFormElementEventTypes type) {
        for (final DataFormElementEventModel dfeem : elementModel.getEvents()) {
            Time time;
            if (type == dfeem.getEventType()) {
                switch (dfeem.getEventDataType()) {
                    case DATE:
                        time = new Time();
                        time.setToNow();
                        dfeem.addStoredData(time.format("%H:%M:%S").getBytes());

                        break;
                    case TIME:
                        time = new Time();
                        time.setToNow();
                        dfeem.addStoredData(time.format("%H:%M:%S").getBytes());
                        break;
                    case GPS:
                        final LocationManager lm = (LocationManager) application.getSystemService(Context.LOCATION_SERVICE);
                        lm.getProvider("gps");

                        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {

                            public void onLocationChanged(Location location) {
                                lm.removeUpdates(this);
                                String[] coords = { String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()) };
                                DataFormElementGPSView.setGPSCoordinates(coords, dfeem);
                            }

                            public void onProviderDisabled(String provider) {}

                            public void onProviderEnabled(String provider) {}

                            public void onStatusChanged(String provider, int status, Bundle extras) {}
                        });
                        break;
                }
            }
        }
    }
    
    public void openDetail(DataCollectorFormActivity application, int id) {
        Intent tki = new Intent();
        tki.setClass(application, DataCollectorFormViewDetailsActivity.class);
        tki.putExtra("dfem", elementModel);
        tki.putExtra("elementpos", id);
        application.startActivityForResult(tki, id);
    }
    
    @Override
    public void update(Observable observable, Object data) {
        updateView((DataFormElementModel) observable);
    }

    protected abstract void updateView(DataFormElementModel observable);
}