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

        inflate(getApplication(), R.layout.textformelement, this);
        
        TextView label = (TextView) findViewById(R.id.textformelement_label);
        label.setWidth(super.Column1width);
        label.setText(dfem.getTitle());
        
        ImageButton takeText = (ImageButton) findViewById(R.id.textformelement_write_text);
        takeText.setMinimumWidth(super.Column2width);
        takeText.setId(id);
        takeText.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dfec.events(DataFormElementEventTypes.ONBEFORE);
                Intent tki = new Intent();
                tki.setClass(application, DataCollectorTakeTextActivity.class);
                String datatext = "";
                tki.putExtra("datatext", datatext);
                application.startActivityForResult(tki, id);
            }
        });
        
        TextView preview = (TextView) findViewById(R.id.textformelement_preview_text);
        preview.setWidth(super.Column3width + Column4width);
        
        ImageButton btnDetails = new ImageButton(application);
        btnDetails.setId(id);
        btnDetails.setMinimumWidth(Column5width);

        if (dfem.getDataList().size() < 1) {
            btnDetails.setVisibility(INVISIBLE);
        } else {
            btnDetails.setVisibility(VISIBLE);
        }
        
        btnDetails.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dfec.openDetail(application, id);
            }
        });

        if (dfem.getStoredData(dfem.getDataList().size() - 1) != null) {
            String tmp = new String(dfem.getStoredData(dfem.getDataList().size() - 1));
            preview.setText(tmp);
        }
    }
}
