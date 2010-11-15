package info.collide.android.scydatacollector.formelements;

import info.collide.android.scydatacollector.DataCollectorFormActivity;
import info.collide.android.scydatacollector.DataCollectorShowImageActivity;
import info.collide.android.scydatacollector.DataFormElementModel;
import info.collide.android.scydatacollector.R;
import info.collide.android.scydatacollector.DataFormElementEventModel.DataFormElementEventTypes;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class DataFormElementImageView extends DataFormElementView {

    public DataFormElementImageView(final DataFormElementController dfec, final DataFormElementModel dfem, final DataCollectorFormActivity application, final int id) {
        super(dfem, application, dfec);

        TextView label = new TextView(getApplication());
        label.setWidth(super.Column1width);
        label.setGravity(Gravity.CENTER_VERTICAL);
        ImageButton btnTakeImage = new ImageButton(getApplication());
        btnTakeImage.setMinimumWidth(super.Column2width);
        label.setText(dfem.getTitle());
        // btnTakeImage.setText("Foto");
        btnTakeImage.setImageResource(R.drawable.ic_menu_camera);
        // btnTakeImage.setMaxHeight(RowMaxHeight);

        ImageButton imgButton = new ImageButton(getApplication());
        imgButton.setMinimumWidth(Column3width + Column4width);
        imgButton.setMaxHeight(RowMaxHeight);

        ImageButton _btnDetails = new ImageButton(application);
        // _btnDetails.setText("Details");
        _btnDetails.setImageResource(R.drawable.ic_menu_info_details);
        _btnDetails.setMaxHeight(RowMaxHeight);
        _btnDetails.setId(id);

        _btnDetails.setMinimumWidth(Column5width);
        _btnDetails.setVisibility(INVISIBLE);
        _btnDetails.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                dfec.openDetail(application, id);
            }
        });

        imgButton.setId(id);
        imgButton.setVisibility(INVISIBLE);
        btnTakeImage.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // todo Datensatz wird immer gemacht
                dfec.events(DataFormElementEventTypes.ONBEFORE);
                Intent i = new Intent("android.media.action.IMAGE_CAPTURE");
                getApplication().startActivityForResult(i, id);
            }
        });

        if (dfem.getStoredData(dfem.getDataList().size() - 1) != null) {

            byte[] buf = dfem.getStoredData(dfem.getDataList().size() - 1);
            Bitmap bm = BitmapFactory.decodeByteArray(buf, 0, buf.length);

            Bitmap icon = Bitmap.createScaledBitmap(bm, super.Column3width - 20, super.RowMaxHeight - 20, true);

            imgButton.setImageBitmap(icon);

            imgButton.setVisibility(View.VISIBLE);
            _btnDetails.setVisibility(VISIBLE);
            imgButton.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {
                    Intent tki = new Intent();
                    tki.setClass(application, DataCollectorShowImageActivity.class);
                    tki.putExtra("dataimage", dfem.getStoredData(dfem.getDataList().size() - 1));
                    application.startActivity(tki);

                }
            });

        }

        addView(label);
        addView(btnTakeImage);
        addView(imgButton);
        addView(_btnDetails);

    }
}
