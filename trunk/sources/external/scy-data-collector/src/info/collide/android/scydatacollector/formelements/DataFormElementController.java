package info.collide.android.scydatacollector.formelements;

import info.collide.android.scydatacollector.DataCollectorFormActivity;
import info.collide.android.scydatacollector.DataCollectorFormViewDetailsActivity;
import info.collide.android.scydatacollector.DataFormElementEventModel;
import info.collide.android.scydatacollector.DataFormElementModel;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.format.Time;

public class DataFormElementController {

    private DataFormElementModel _dfem;

    private DataFormElementView _dfev;

    private Activity _activity;

    public DataFormElementController(DataFormElementModel dfem, Activity activity) {
        _dfem = dfem;
        _activity = activity;

    }

    public void events(DataFormElementEventModel.DataFormElementEventTypes type) {
        for (final DataFormElementEventModel dfeem : _dfem.getEvents()) {
            Time time;
            if (type == dfeem.getEventType())
                switch (dfeem.getEventDataType()) {
                    case DATE:
                        time = new Time();
                        time.setToNow();
                        dfeem.addStoredData((prettyTimeDate(time.monthDay) + "." + prettyTimeDate(time.month + 1) + "." + String.valueOf(time.year)).getBytes());

                        break;
                    case TIME:
                        time = new Time();
                        time.setToNow();
                        dfeem.addStoredData((prettyTimeDate(time.hour) + ":" + prettyTimeDate(time.minute) + ":" + prettyTimeDate(time.second)).getBytes());

                        break;
                    case GPS:

                        final LocationManager lm = (LocationManager) _activity.getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
                        lm.getProvider("gps");

                        lm.requestLocationUpdates("gps", 0, 0, new LocationListener() {

                            public void onLocationChanged(Location location) {
                                lm.removeUpdates(this);

                                String[] coords = { String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()) };

                                DataFormElementGPSView.setGPSCoordinates(coords, dfeem);
                            }

                            public void onProviderDisabled(String provider) {
                            // TODO Auto-generated method stub
                            }

                            public void onProviderEnabled(String provider) {
                            // TODO Auto-generated method stub
                            }

                            public void onStatusChanged(String provider, int status, Bundle extras) {
                            // TODO Auto-generated method stub
                            }
                        });

                        break;
                }
        }
    }

    private String prettyTimeDate(int value) {
        String str = String.valueOf(value);
        if (str.length() < 2)
            str = "0" + str;
        return str;
    }

    public void openDetail(DataCollectorFormActivity application, int id) {
        Intent tki = new Intent();
        tki.setClass(application, DataCollectorFormViewDetailsActivity.class);
        tki.putExtra("dfem", _dfem);
        tki.putExtra("elementpos", id);
        application.startActivityForResult(tki, id);

    }
}
