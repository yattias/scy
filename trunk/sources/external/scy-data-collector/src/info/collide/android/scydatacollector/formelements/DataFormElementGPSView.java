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
import android.widget.TextView;

public class DataFormElementGPSView extends DataFormElementView {

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

    public DataFormElementGPSView(final DataFormElementController dfec, final DataFormElementModel dfem, DataCollectorFormActivity application, final int id) {
        super(dfem, application, dfec);
        TextView label = new TextView(getApplication());
        label.setWidth(super.Column1width);

        Button takeGPS = new Button(getApplication());
        takeGPS.setWidth(super.Column2width);
        final Button preview = new Button(getApplication());

        if (dfem.getStoredData(dfem.getDataList().size() - 1) != null) {
            preview.setText("Position anzeigen");
            preview.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {

                    Intent tki = new Intent();
                    tki.setClass(getApplication(), DataCollectorMapViewActivity.class);
                    tki.putExtra("datagps", getGPSCoordinates(dfem));
                    getApplication().startActivityForResult(tki, id);

                }

            });

        } else {
            preview.setVisibility(INVISIBLE);
        }
        // preview.setMaxLines(2);
        // preview.setHeight(super.RowMaxHeight / 2);
        // preview.setWidth(super.Column3width + super.Column4width);
        // preview.setTag(new String("Preview"));
        takeGPS.setText("GPS");
        takeGPS.setWidth(super.Column2width);
        // final int pos = table.getChildCount();
        takeGPS.setId(id);

        // preview.setPadding(0, 0, 0, 0);
        // label.setLayoutParams(params);
        takeGPS.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                dfec.events(DataFormElementEventTypes.ONBEFORE);
                final LocationManager lm = (LocationManager) getApplication().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
                // LocationProvider provider =
                lm.getProvider("gps");
                preview.setVisibility(VISIBLE);
                preview.setText(R.string.msgPositionCapturing);

                lm.requestLocationUpdates("gps", 0, 0, new LocationListener() {

                    public void onLocationChanged(Location location) {
                        lm.removeUpdates(this);

                        // TODO Auto-generated method stub
                        // Location loc =
                        // lm.getLastKnownLocation("gps");
                        preview.setText(R.string.msgShowGPS);
                        // preview.setText("Lat:" +
                        // location.getLatitude()
                        // + " Lng:" + location.getLongitude());
                        String[] coords = { String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()) };

                        setGPSCoordinates(coords, dfem);

                        dfec.events(DataFormElementEventTypes.ONAFTER);
                        preview.setOnClickListener(new OnClickListener() {

                            public void onClick(View v) {

                                Intent tki = new Intent();
                                tki.setClass(getApplication(), DataCollectorMapViewActivity.class);
                                tki.putExtra("datagps", getGPSCoordinates(dfem));
                                getApplication().startActivityForResult(tki, id);

                            }

                        });
                    }

                    public void onProviderDisabled(String provider) {
                        // TODO Auto-generated method stub
                        preview.setText("provider aus");
                    }

                    public void onProviderEnabled(String provider) {
                        // TODO Auto-generated method stub
                        preview.setText("provider an");
                    }

                    public void onStatusChanged(String provider, int status, Bundle extras) {
                    // TODO Auto-generated method stub
                    }
                });

            }
        });

        label.setText(dfem.getTitle());

        addView(label);
        addView(takeGPS);
        addView(preview);
    }

}
