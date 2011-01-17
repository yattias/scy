package info.collide.android.scydatacollector;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class DataCollectorShowImageActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.image);

        Bundle extras = getIntent().getExtras();

        Button btnOK = (Button) findViewById(R.id.btnImageViewShowClose);
        final ImageView imgview = (ImageView) findViewById(R.id.ImageViewShow);
        byte[] buf = extras.getByteArray("dataimage");
        Bitmap bm = BitmapFactory.decodeByteArray(buf, 0, buf.length);
        // imgview.setMaxHeight(390);
        imgview.setImageBitmap(bm);

        // Display display = getWindowManager().getDefaultDisplay();
        // int height = display.getHeight();

        // imgview.setMinimumHeight(height-100);

        btnOK.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });

    }

}