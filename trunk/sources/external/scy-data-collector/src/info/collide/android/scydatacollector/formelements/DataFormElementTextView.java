package info.collide.android.scydatacollector.formelements;

import info.collide.android.scydatacollector.DataCollectorFormActivity;
import info.collide.android.scydatacollector.DataCollectorTakeTextActivity;
import info.collide.android.scydatacollector.DataFormElementModel;
import info.collide.android.scydatacollector.R;
import info.collide.android.scydatacollector.DataFormElementEventModel.DataFormElementEventTypes;
import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class DataFormElementTextView extends DataFormElementView {

    public DataFormElementTextView(final DataFormElementController dfec, final DataFormElementModel dfem, final DataCollectorFormActivity application, final int id) {
        super(dfem, application, dfec);

        TextView label = new TextView(application);
        label.setWidth(super.Column1width);
        ImageButton takeText = new ImageButton(application);
        TextView preview = new TextView(application);
        preview.setMaxLines(2);
        preview.setWidth(super.Column3width + Column4width);
        // takeText.setText("Text");
        takeText.setMinimumWidth(super.Column2width);
        takeText.setImageResource(R.drawable.text);
        takeText.setId(id);
        ImageButton _btnDetails = new ImageButton(application);
        // _btnDetails.setText("Details");
        _btnDetails.setImageResource(R.drawable.ic_menu_info_details);
        _btnDetails.setMaxHeight(RowMaxHeight);
        _btnDetails.setId(id);

        _btnDetails.setMinimumWidth(Column5width);

        if (dfem.getDataList().size() < 1) {
            _btnDetails.setVisibility(INVISIBLE);
        } else
            _btnDetails.setVisibility(VISIBLE);
        _btnDetails.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                dfec.openDetail(application, id);
            }
        });

        final int _id = id;

        if (dfem.getStoredData(dfem.getDataList().size() - 1) != null) {
            String tmp = new String(dfem.getStoredData(dfem.getDataList().size() - 1));
            preview.setText(tmp);
        }

        takeText.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                dfec.events(DataFormElementEventTypes.ONBEFORE);
                Intent tki = new Intent();
                tki.setClass(application, DataCollectorTakeTextActivity.class);
                String datatext = "";
                // if (dfem.getStoredData(dfem.getDataList().size() - 1) !=
                // null) {
                // datatext = new
                // String(dfem.getStoredData(dfem.getDataList().size() - 1));
                // }
                tki.putExtra("datatext", datatext);
                application.startActivityForResult(tki, _id);
            }
        });

        label.setText(dfem.getTitle());

        addView(label);
        addView(takeText);
        addView(preview);
        addView(_btnDetails);
    }
}
