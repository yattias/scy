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

    private ImageButton detailsButton;
    private TextView previewText;

    public DataFormElementTextView(final DataFormElementModel dfem, final DataCollectorFormActivity application, final int id) {
        super(dfem, application);

        inflate(getApplication(), R.layout.textformelement, this);
        
        TextView label = (TextView) findViewById(R.id.textformelement_label);
        label.setWidth(super.Column1width);
        label.setText(dfem.getTitle());
        
        ImageButton takeText = (ImageButton) findViewById(R.id.textformelement_write_text);
        takeText.setMinimumWidth(super.Column2width);
        takeText.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                events(DataFormElementEventTypes.ONBEFORE);
                Intent tki = new Intent();
                tki.setClass(application, DataCollectorTakeTextActivity.class);
                String datatext = previewText.getText().toString();
                tki.putExtra("datatext", datatext);
                application.startActivityForResult(tki, id);
            }
        });
        
        previewText = (TextView) findViewById(R.id.textformelement_preview_text);
        previewText.setWidth(super.Column3width + Column4width);
        
        detailsButton = (ImageButton) findViewById(R.id.textformelement_show_details);
//        detailsButton.setMinimumWidth(Column5width);
        detailsButton.setVisibility(INVISIBLE);

        detailsButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                openDetail(application, id);
            }
        });
        updateView(dfem);
    }
    
    protected final void updateView(DataFormElementModel elementModel) {
        if (elementModel.getDataList().size() > 0) {
            detailsButton.setVisibility(VISIBLE);
        } else {
            detailsButton.setVisibility(INVISIBLE);
        }
        if (elementModel.getStoredData(elementModel.getDataList().size() - 1) != null) {
            String tmp = new String(elementModel.getStoredData(elementModel.getDataList().size() - 1));
            previewText.setText(tmp);
        }
    }
}
