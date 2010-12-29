package info.collide.android.scydatacollector;

import info.collide.android.scydatacollector.formelements.DataFormElement;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;

public class DataCollectorFormView implements Observer {

    private static ImageButton btnFertig;

    private final DataCollectorFormActivity application;

    private final DataCollectorFormModel dcfm;

    ProgressDialog mypd;

    public DataCollectorFormView(DataCollectorFormModel dcfm, DataCollectorFormActivity application) {
        this.application = application;
        this.dcfm = dcfm;
        dcfm.addObserver(this);
    }

    public LinearLayout createLastRow(String caption, final Context activity) {
        LinearLayout ll = new LinearLayout(activity);
        
        btnFertig = new ImageButton(activity);
        // btnFertig.setText(caption);
        btnFertig.setImageResource(R.drawable.check);
        btnFertig.setOnClickListener(new OnClickListener() {

            public void onClick(View view) {
                final ContentValues cv = new ContentValues();
                String username = new DataCollectorConfiguration(application).getUserName();
                try {
                    cv.put("dcfm", dcfm.toByteArray());
                    cv.put("username", username);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Uri uri = Uri.parse("content://info.collide.android.scydatacollector.DataCollectorProvider.Forms/forms");
                updateDCFM(cv, uri);
            }

            /**
             * @param cv
             */
            private void updateDCFM(final ContentValues cv, final Uri uri) {
                final ProgressDialog mypd = ProgressDialog.show(application, activity.getResources().getString(R.string.msgSaveToSQL), activity.getResources().getString(R.string.msgPleaseWait), false);

                Thread t = new Thread() {

                    public void run() {
                        application.getContentResolver().update(uri, cv, null, null);
                        mypd.dismiss();
                        application.finish();
                    }
                };
                t.start();
            }
        });
        ll.setGravity(Gravity.CENTER);

        ll.addView(btnFertig);
        android.view.ViewGroup.LayoutParams params = btnFertig.getLayoutParams();
        params.width = LayoutParams.FILL_PARENT;

        return ll;
    }

    public void addbtnFertigListener(OnClickListener ocl) {
        btnFertig.setOnClickListener(ocl);
    }

    public void update(Observable observable, Object data) {
        this.application.setTitle(application.getResources().getString(R.string.formular) + " " + dcfm.getTitle());

        TableLayout table = (TableLayout) application.findViewById(R.id.formtable);

        // TODO EXCEPTION beim 2ten aufruf wenn ich immer nur das eine Object
        // aktualisiere
        if (!(observable == null && data == null)) {
            // if (!(observable == null && data == null)) {
            int i = 0;
            for (DataFormElementModel el : dcfm.getDfElements()) {
                if (el.hasChanged()) {
                    if (table.getChildCount() > 0) {
                        table.removeViewAt(i);
                        table.addView(new DataFormElement(el, application, i).getView(), i);
                    }
                }
                i++;
            }
        } else {
            // int i = 0;
            table.removeAllViews();
            for (DataFormElementModel el : dcfm.getDfElements()) {
                table.addView(new DataFormElement(el, application, table.getChildCount()).getView());
            }
            table.addView(createLastRow(application.getResources().getString(R.string.btnDone), application));
        }
    }

    public void invalidate() {
        if (mypd != null)
            mypd.dismiss();
        application.finish();
    }
}
