package info.collide.android.scydatacollector.formelements;

import info.collide.android.scydatacollector.DataCollectorFormActivity;
import info.collide.android.scydatacollector.DataCollectorMapViewActivity;
import info.collide.android.scydatacollector.DataFormElementEventModel;
import info.collide.android.scydatacollector.DataFormElementModel;
import info.collide.android.scydatacollector.R;
import info.collide.android.scydatacollector.DataFormElementEventModel.DataFormElementEventTypes;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class DataFormElementGPSView extends DataFormElementView {

    private ImageButton takeGPSButton;
    private Button previewButton;

    public static void setGPSCoordinates(String[] coords, Object model) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream out;
            out = new ObjectOutputStream(baos);
            out.writeObject(coords);
            out.flush();
            out.close();
            if (model.getClass() == DataFormElementModel.class) {
                ((DataFormElementModel) model).addStoredData(baos.toByteArray());
            } else if (model.getClass() == DataFormElementEventModel.class) {
                ((DataFormElementEventModel) model).addStoredData(baos.toByteArray());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String[] getGPSCoordinates(DataFormElementModel dfem) {
        String[] coords = null;
        try {
            ByteArrayInputStream baos = new ByteArrayInputStream(dfem.getStoredData(dfem.getDataList().size() - 1));
            ObjectInputStream in;
            in = new ObjectInputStream(baos);
            coords = (String[]) in.readObject();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return coords;
    }

    public static String[] getGPSCoordinates(DataFormElementEventModel dfeem, int pos) {
        String[] coords = null;
        try {
            ByteArrayInputStream baos = new ByteArrayInputStream(dfeem.getStoredData(pos));

            ObjectInputStream in;
            in = new ObjectInputStream(baos);
            coords = (String[]) in.readObject();

            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return coords;
    }

    public DataFormElementGPSView(final DataFormElementModel elementModel, DataCollectorFormActivity application, final int id) {
        super(elementModel, application);

        inflate(getApplication(), R.layout.gpsformelement, this);
        
        TextView label = (TextView) findViewById(R.id.gpsformelement_label);
        label.setWidth(super.Column1width);
        label.setText(elementModel.getTitle());

        takeGPSButton = (ImageButton) findViewById(R.id.gpsformelement_capture_gps);
        previewButton = (Button) findViewById(R.id.gpsformelement_show_details);

        previewButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent tki = new Intent();
                tki.setClass(getApplication(), DataCollectorMapViewActivity.class);
                tki.putExtra("datagps", getGPSCoordinates(elementModel));
                getApplication().startActivityForResult(tki, id);
            }
        });
        
        takeGPSButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                takeGPSButton.setEnabled(false);
                previewButton.setEnabled(false);
                events(DataFormElementEventTypes.ONBEFORE);
                previewButton.setText(R.string.msgPositionCapturing);

                final LocationManager lm = (LocationManager) getApplication().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {

                    public void onLocationChanged(Location location) {
                        previewButton.setEnabled(true);
                        takeGPSButton.setEnabled(true);
                        lm.removeUpdates(this);

                        previewButton.setText(R.string.msgShowGPS);
                        String[] coords = { String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()) };
                        setGPSCoordinates(coords, elementModel);
                        events(DataFormElementEventTypes.ONAFTER);
                    }

                    public void onProviderDisabled(String provider) {
                        previewButton.setText(R.string.gpsProviderOff);
                    }

                    public void onProviderEnabled(String provider) {
                        previewButton.setText(R.string.gpsProviderOn);
                    }

                    public void onStatusChanged(String provider, int status, Bundle extras) {
                    }
                });
            }
        });
        updateView(elementModel);
    }

    @Override
    protected void updateView(DataFormElementModel elementModel) {
        if (elementModel.getStoredData(elementModel.getDataList().size() - 1) != null) {
            previewButton.setText(R.string.msgShowGPS);
            previewButton.setEnabled(true);
        }
    }

}
