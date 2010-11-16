package info.collide.android.scydatacollector.formelements;

import info.collide.android.scydatacollector.DataCollectorFormActivity;
import info.collide.android.scydatacollector.DataCollectorShowImageActivity;
import info.collide.android.scydatacollector.DataFormElementModel;
import info.collide.android.scydatacollector.R;
import info.collide.android.scydatacollector.DataFormElementEventModel.DataFormElementEventTypes;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class DataFormElementImageView extends DataFormElementView {

    public DataFormElementImageView(final DataFormElementController dfec, final DataFormElementModel dfem, final DataCollectorFormActivity application, final int id) {
        super(dfem, application, dfec);

        inflate(getApplication(), R.layout.pictureformelement, this);
        
        TextView label = (TextView) findViewById(R.id.picutureformelement_label);
        label.setWidth(super.Column1width);
        label.setText(dfem.getTitle() + " Test");


        ImageButton btnTakeImage = (ImageButton) findViewById(R.id.pictureformelement_take_picture);
        btnTakeImage.setMinimumWidth(super.Column2width);
        btnTakeImage.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dfec.events(DataFormElementEventTypes.ONBEFORE);
                Intent i = new Intent("android.media.action.IMAGE_CAPTURE");
                getApplication().startActivityForResult(i, id);
            }
        });
        
        ImageButton imgButton = (ImageButton) findViewById(R.id.pictureformelement_show_picture);
        imgButton.setMinimumWidth(Column3width + Column4width);
        imgButton.setId(id);
        imgButton.setVisibility(INVISIBLE);

        ImageButton btnDetails = new ImageButton(application);
        btnDetails.setId(id);
        btnDetails.setMinimumWidth(Column5width);
        btnDetails.setVisibility(INVISIBLE);
        btnDetails.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dfec.openDetail(application, id);
            }
        });
        
        if (dfem.getStoredData(dfem.getDataList().size() - 1) != null) {

            byte[] buf = dfem.getStoredData(dfem.getDataList().size() - 1);
            Bitmap bm = BitmapFactory.decodeByteArray(buf, 0, buf.length);

            Bitmap icon = Bitmap.createScaledBitmap(bm, super.Column3width - 20, super.RowMaxHeight - 20, true);

            imgButton.setImageBitmap(icon);

            imgButton.setVisibility(View.VISIBLE);
            btnDetails.setVisibility(VISIBLE);
            imgButton.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {
                    Intent tki = new Intent();
                    tki.setClass(application, DataCollectorShowImageActivity.class);
                    tki.putExtra("dataimage", dfem.getStoredData(dfem.getDataList().size() - 1));
                    application.startActivity(tki);

                }
            });

        }
    }
}
