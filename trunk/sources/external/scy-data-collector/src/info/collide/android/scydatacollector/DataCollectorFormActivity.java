package info.collide.android.scydatacollector;

import info.collide.android.scydatacollector.formelements.DataFormElementView;
import info.collide.android.scydatacollector.formelements.DataFormElementViewFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;

public class DataCollectorFormActivity extends Activity {

    final private static String DCFM_TITLE = "dcfm";

    private static final int UPDATE_PROGESS = 0;
    
    private static ImageButton finishButton;

    private DataCollectorFormModel formModel;
    
    private DataCollectorConfiguration configuration;

    private TableLayout table;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form);

        if (savedInstanceState != null && savedInstanceState.isEmpty()) {
            formModel = DataCollectorFormModel.fromByteArray(savedInstanceState.getByteArray(DCFM_TITLE));
            Log.d("DataCollector", "Created form activity with saved instance " + formModel.getTitle());
        } else {
            formModel = new DataCollectorFormModel();
            
            Bundle extras = getIntent().getExtras();
            Long formid = extras.getLong("dataform");
            
            DataCollectorContentProvider dccp = new DataCollectorContentProvider(this);
            dccp.getDCFM(this, formid, formModel);
            Log.d("DataCollector", "Created form activity with title: " + formModel.getTitle());
        }
        configuration = new DataCollectorConfiguration(this);

        // set title of form
        setTitle(getResources().getString(R.string.formular) + " " + formModel.getTitle());

        table = (TableLayout) findViewById(R.id.formtable);

        // filling table with rows
        table.removeAllViews();
        for (DataFormElementModel elementModel : formModel.getElementModels()) {
            DataFormElementView view = DataFormElementViewFactory.createView(elementModel, this, table.getChildCount());
            elementModel.addObserver(view);
            table.addView(view);
        }

        // finish button
        LinearLayout lastRow = new LinearLayout(this);

        finishButton = new ImageButton(this);
        finishButton.setImageResource(R.drawable.check);
        finishButton.setOnClickListener(new OnClickListener() {

            public void onClick(View view) {
                final ContentValues cv = new ContentValues();
                String username = configuration.getUserName();
                try {
                    cv.put("dcfm", formModel.toByteArray());
                    cv.put("username", username);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Uri uri = Uri.parse("content://info.collide.android.scydatacollector.DataCollectorProvider.Forms/forms");
                updateDCFM(cv, uri);
            }
        });
        lastRow.setGravity(Gravity.CENTER);

        lastRow.addView(finishButton);
        android.view.ViewGroup.LayoutParams params = finishButton.getLayoutParams();
        params.width = LayoutParams.FILL_PARENT;

        table.addView(lastRow);
    }
    
    private void updateDCFM(final ContentValues cv, final Uri uri) {
        new AsyncTask<Object, Void, Void>() {

            @Override
            protected void onPreExecute() {
                showDialog(UPDATE_PROGESS);
            }
            
            @Override
            protected Void doInBackground(Object... params) {
                getApplication().getContentResolver().update(uri, cv, null, null);
                return null;
            }
            
            @Override
            protected void onPostExecute(Void result) {
                removeDialog(UPDATE_PROGESS);
                finish();
            }
            
        }.execute(cv, uri);
    }
    
    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == UPDATE_PROGESS) {
            String wait = getResources().getString(R.string.msgPleaseWait);
            ProgressDialog dialog = new ProgressDialog(this);
            dialog.setTitle(R.string.msgSaveToSQL);
            dialog.setMessage(wait);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            return dialog;
        }
        return null;
    }

    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        Log.d("DataCollector", "onResult received requestCode " + requestCode + ", resultCode " + resultCode + " and " + data);
        if (data != null) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                if (bundle.containsKey("fromdetail") == true) {
                    int elementPos = bundle.getInt("elementpos");
                    DataFormElementModel newDFEM = (DataFormElementModel) bundle.getSerializable("dfem");
                    newDFEM.addObserver(formModel);
                    formModel.setDfElement(newDFEM, elementPos);
                } else {
                    final ArrayList<DataFormElementModel> elementModels = formModel.getElementModels();
                    DataFormElementModel elementModel = elementModels.get(requestCode);
                    switch (elementModel.getType()) {
                        case IMAGE:
                            Bitmap picture = (Bitmap) bundle.get("data");
                            Log.d("DataCollector", "onResult received image, picture size " + picture.getWidth() + " x " + picture.getHeight());
                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            picture.compress(CompressFormat.PNG, 0, bos);
                            byte[] bitmapdata = bos.toByteArray();
                            elementModel.addStoredData(bitmapdata);
                            break;
                        case TEXT:
                            final byte[] byteData = bundle.getByteArray("datatext");
                            elementModel.addStoredData(byteData);
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
                                elementModel.addStoredData(baos.toByteArray());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                    }
                    // update on after event
                    // dfec.events(DataFormElementEventTypes.ONAFTER);
                }
            }
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        formModel = DataCollectorFormModel.fromByteArray(savedInstanceState.getByteArray(DCFM_TITLE));
        Log.d("DataCollector", "Restored with instance is empty? " + savedInstanceState.isEmpty());
        int index = 0;
        for (DataFormElementModel elementModel : formModel.getElementModels()) {
            View child = table.getChildAt(index++);
            if (child instanceof DataFormElementView) {
                DataFormElementView view = (DataFormElementView) child;
                elementModel.addObserver(view);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        /**************************************************
         * Here we do _temporary_ save all critical data, like our selectedProduct.
         **************************************************/
        try {
            outState.putByteArray(DCFM_TITLE, formModel.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onSaveInstanceState(outState);
    }
}
