package info.collide.android.scydatacollector.formelements;

import info.collide.android.scydatacollector.DataCollectorFormActivity;
import info.collide.android.scydatacollector.DataFormElementModel;
import info.collide.android.scydatacollector.R;
import info.collide.android.scydatacollector.DataFormElementEventModel.DataFormElementEventTypes;
import android.content.Context;
import android.os.Vibrator;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class DataFormElementCounterView extends DataFormElementView {

    public DataFormElementCounterView(final DataFormElementController dfec, final DataFormElementModel dfem, final DataCollectorFormActivity application, final int id) {
        super(dfem, application, dfec);

        TextView label = new TextView(getApplication());
        label.setWidth(super.Column1width);
        final EditText txt = new EditText(getApplication());

        if (dfem.getStoredData(dfem.getDataList().size() - 1) != null) {
            String tmp = new String(dfem.getStoredData(dfem.getDataList().size() - 1));
            txt.setText(tmp);
        } else
            txt.setText("0");
        txt.setInputType(InputType.TYPE_CLASS_NUMBER);
        txt.setWidth(Column2width);
        txt.setMinimumWidth(Column2width);
        txt.setMaxHeight(RowMaxHeight);
        // label.setGravity(48);
        // this.setGravity(48);

        ImageButton btnInc = new ImageButton(getApplication());
        ImageButton btnDec = new ImageButton(getApplication());
        label.setText(dfem.getTitle());
        // btnInc.setText("+");
        // btnDec.setText("-");
        btnInc.setImageResource(R.drawable.add);
        btnDec.setImageResource(R.drawable.remove_minus_sign);
        ImageButton _btnDetails = new ImageButton(application);
        // _btnDetails.setText("Details");
        _btnDetails.setImageResource(R.drawable.ic_menu_info_details);
        _btnDetails.setMaxHeight(RowMaxHeight);
        _btnDetails.setId(id);

        _btnDetails.setMinimumWidth(Column5width);

        // _btnDetails.setIm
        // _btnDetails.set
        _btnDetails.setId(id);
        if (dfem.getDataList().size() < 1) {
            _btnDetails.setVisibility(INVISIBLE);
        } else
            _btnDetails.setVisibility(VISIBLE);
        _btnDetails.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                dfec.openDetail(application, id);
            }
        });

        btnInc.setMinimumWidth(Column3width);
        btnDec.setMinimumWidth(Column4width);

        btnInc.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

                // try {
                String tmp = "0";
                if (dfem.getStoredData(dfem.getDataList().size() - 1) != null) {
                    tmp = new String(dfem.getStoredData(dfem.getDataList().size() - 1));

                }

                dfec.events(DataFormElementEventTypes.ONBEFORE);

                int counter = Integer.parseInt(tmp);
                counter++;

                dfem.addStoredData((String.valueOf(counter)).getBytes());
                vibrate(25);
                dfec.events(DataFormElementEventTypes.ONAFTER);

                // } catch (Exception e) {
                // txt.setText("0");
                // }

            }

        });
        btnDec.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

                // try {
                String tmp = "0";
                if (dfem.getStoredData(dfem.getDataList().size() - 1) != null) {
                    tmp = new String(dfem.getStoredData(dfem.getDataList().size() - 1));

                }
                dfec.events(DataFormElementEventTypes.ONBEFORE);
                int counter = Integer.parseInt(tmp);
                counter--;
                dfem.addStoredData((String.valueOf(counter)).getBytes());
                vibrate(25);
                dfec.events(DataFormElementEventTypes.ONAFTER);

                // } (Exception e) {
                // txt.setText("0");
                // }

            }
        });

        addView(label);
        addView(txt);
        addView(btnInc);
        addView(btnDec);
        addView(_btnDetails);

    }

    private static void vibrate(int i) {
        // TODO Auto-generated method stub
        Vibrator vib = (Vibrator) getApplication().getSystemService(Context.VIBRATOR_SERVICE);

        // long milliseconds = i;

        vib.vibrate(i);
    }

}
