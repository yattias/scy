package info.collide.android.scydatacollector.formelements;

import info.collide.android.scydatacollector.DataCollectorFormActivity;
import info.collide.android.scydatacollector.DataCollectorShowImageActivity;
import info.collide.android.scydatacollector.DataFormElementModel;
import info.collide.android.scydatacollector.R;
import info.collide.android.scydatacollector.DataFormElementEventModel.DataFormElementEventTypes;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class DataFormElementImageView extends DataFormElementView {

    private ImageButton imageButton;
    private ImageButton detailsButton;

    public DataFormElementImageView(final DataFormElementModel elementModel, final DataCollectorFormActivity application, final int id) {
        super(elementModel, application);

        inflate(getApplication(), R.layout.pictureformelement, this);
        
        TextView label = (TextView) findViewById(R.id.pictureformelement_label);
        label.setWidth(super.Column1width);
        label.setText(elementModel.getTitle());

        ImageButton btnTakeImage = (ImageButton) findViewById(R.id.pictureformelement_take_picture);
        btnTakeImage.setMinimumWidth(super.Column2width);
        btnTakeImage.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                events(DataFormElementEventTypes.ONBEFORE);
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                getApplication().startActivityForResult(i, id);
            }
        });
        
        imageButton = (ImageButton) findViewById(R.id.pictureformelement_show_picture);
        imageButton.setMinimumWidth(Column3width + Column4width);
        imageButton.setVisibility(INVISIBLE);

        detailsButton = (ImageButton) findViewById(R.id.pictureformelement_show_details);
        detailsButton.setMinimumWidth(Column5width);
        detailsButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                openDetail(application, id);
            }
        });
        detailsButton.setVisibility(INVISIBLE);
        updateView(elementModel);
    }

    @Override
    protected void updateView(final DataFormElementModel elementModel) {
        if (elementModel.getStoredData(elementModel.getDataList().size() - 1) != null) {
            byte[] buf = elementModel.getStoredData(elementModel.getDataList().size() - 1);
            Bitmap bm = BitmapFactory.decodeByteArray(buf, 0, buf.length);
            Log.d("DataCollector", "Received update on image view to show picture " + bm.getWidth() + " x " + bm.getHeight());

            Bitmap icon = Bitmap.createScaledBitmap(bm, super.Column3width - 20, super.RowMaxHeight - 20, true);

            imageButton.setImageBitmap(icon);

            imageButton.setVisibility(VISIBLE);
            detailsButton.setVisibility(VISIBLE);
            imageButton.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {
                    Intent tki = new Intent();
                    tki.setClass(application, DataCollectorShowImageActivity.class);
                    tki.putExtra("dataimage", elementModel.getStoredData(elementModel.getDataList().size() - 1));
                    application.startActivity(tki);
                }
            });

        }
    }
}
