package info.collide.android.scydatacollector;

import info.collide.android.scydatacollector.DataFormElementEventModel.DataFormElementEventTypes;
import info.collide.android.scydatacollector.formelements.DataFormElementController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class DataCollectorFormController {

    private DataCollectorFormActivity application;

    private DataCollectorFormModel dcfm;

    private DataCollectorFormView _dcfv;

    // private String tunePath(String path) {
    // if (!path.startsWith("/")) {
    // path = "/" + path;
    // }
    // if (!path.contains(".")) {
    // ;
    // }
    // return Environment.getExternalStorageDirectory().getAbsolutePath()
    // + path;
    // }

    public DataCollectorFormController(DataCollectorFormModel dcfm, final DataCollectorFormView dcfv, final DataCollectorFormActivity application) {
        this.application = application;
        this.dcfm = dcfm;
        _dcfv = dcfv;

        Bundle extras = application.getIntent().getExtras();
        Long formid = extras.getLong("dataform");

        DataCollectorContentProvider dccp = new DataCollectorContentProvider();
        dccp.getDCFM(application, formid, dcfm);
        // dccp.close();
        dcfv.update(null, null);

        // FROM FILE
        // try {
        // dcfm.fromFile(tunePath("test.xml"));
        // } catch (IOException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // } catch (ClassNotFoundException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        // dcfv.update(null, null);
        // DataCollectorWebServicesController dcwc = new
        // DataCollectorWebServicesController(application);
        // dcwc.postFormJSON( dcfm)
    }

    private class btnFertigListener implements OnClickListener {

        // TODO einbinden in view
        public void onClick(View v) {
        // DataCollectorWebServicesController dcc = new
        // DataCollectorWebServicesController(application);

        // DataCollectorConfiguration dcc = new DataCollectorConfiguration(
        // application);
        //
        // DataCollectorContentProvider dccp = new DataCollectorContentProvider();
        // dccp.updateForm(dcfm, dcc.getUserName());
        // application.finish();
        }

    }

    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        if (data != null) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {

                if (bundle.containsKey("fromdetail") == true) {
                    int elementPos = bundle.getInt("elementpos");
                    DataFormElementModel newDFEM = (DataFormElementModel) bundle.getSerializable("dfem");

                    newDFEM.addObserver(dcfm);
                    dcfm.setDfElement(newDFEM, elementPos);
                } else {
                    final ArrayList<DataFormElementModel> elements = dcfm.getDfElements();
                    switch (elements.get(requestCode).getType()) {
                        case IMAGE:
                            // todo Datensatz lšschen wenn bei Bild Abbruch;
                            Bitmap picture = (Bitmap) bundle.get("data");

                            final ImageButton imgButton = (ImageButton) application.findViewById(requestCode);
                            Bitmap icon = Bitmap.createScaledBitmap(picture, 40, 20, true);
                            imgButton.setImageBitmap(icon);

                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            picture.compress(CompressFormat.PNG, 0 /*
                                                                    * ignored for PNG
                                                                    */, bos);
                            byte[] bitmapdata = bos.toByteArray();

                            elements.get(requestCode).addStoredData(bitmapdata);
                            break;
                        case TEXT:

                            final byte[] byteData = bundle.getByteArray("datatext");
                            elements.get(requestCode).addStoredData(byteData);
                            break;
                        case GPS:

                            final String[] coords = bundle.getStringArray("datagps");

                            ByteArrayOutputStream baos = new ByteArrayOutputStream();

                            ObjectOutputStream out;
                            try {
                                out = new ObjectOutputStream(baos);
                                out.writeObject(coords);
                                out.flush();
                                out.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        default:
                            break;
                    }
                    DataFormElementController dfec = new DataFormElementController(elements.get(requestCode), application);
                    dfec.events(DataFormElementEventTypes.ONAFTER);

                }

            }
        }
    }

}
