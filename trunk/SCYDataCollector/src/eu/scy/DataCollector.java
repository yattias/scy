package eu.scy;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.kobjects.base64.Base64;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.view.Menu;
import android.view.SubMenu;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class DataCollector extends Activity {
    /** Called when the activity is first created. */

	public final static int IMAGE_TAKEN = 1;
	
	private Button takePhoto;
	
	private Button upload;
	
	private TextView title;
	
	private ImageView preview;
	
	private Bitmap picture;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // get views
        takePhoto = (Button) findViewById(R.id.takePhoto);
        upload = (Button) findViewById(R.id.upload);
        title = (TextView) findViewById(R.id.title);
        preview = (ImageView) findViewById(R.id.preview);
        
        // register listeners
        takePhoto.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Intent i = new Intent("android.media.action.IMAGE_CAPTURE");
				startActivityForResult(i, IMAGE_TAKEN);
			}
        	
        });
        
        
        upload.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				if(picture == null) {
					AlertDialog alertDialog = new AlertDialog.Builder(DataCollector.this).create();
					alertDialog.setMessage("You have to take a picture first!");
					alertDialog.setTitle("Warning");
					alertDialog.setCanceledOnTouchOutside(true);
					alertDialog.show();
				} else if(title.getText() != null && title.getText().length() == 0) {
					AlertDialog alertDialog = new AlertDialog.Builder(DataCollector.this).create();
					alertDialog.setMessage("You have to provide a title first!");
					alertDialog.setTitle("Warning");
					alertDialog.setCanceledOnTouchOutside(true);
					alertDialog.show();
				} else {
					saveELO();
				}
			}
        	
        });
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	SubMenu subMenu = menu.addSubMenu("Exit");
    	
    	
    	return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public void onWindowAttributesChanged(LayoutParams params) {
    	super.onWindowAttributesChanged(params);
    	updatePreview();
    }
    
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	
    	if(requestCode == IMAGE_TAKEN) {
    		Bundle bundle = data.getExtras();
    		if(bundle != null) {
    			picture = (Bitmap) bundle.get("data");
    			updatePreview();
    		}
    	}
    }
    
    private void updatePreview() {
    	if(picture != null) {
    		preview.setImageBitmap(picture);
    	}
    }
    
    // webservice stuff - very hard coded right now :(

	private static final String SOAP_ACTION = "saveData";
    private static final String METHOD_NAME = "saveData";
    private static final String NAMESPACE = "http://mobileservice.webservices.scy.eu/";
    private static final String URL = "http://192.168.178.29:8080/scy-useradmin-web/services/RepositoryService";
     
    void saveELO() {
        try {
        	// get title
        	String t = title.getText().toString();
        	
        	// get picture as base64 encoded String
        	byte[] picArray = getBitmapAsByteArray(picture);
        	String pic = Base64.encode(picArray);

        	// set-up SOAP message
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("arg0", t);
            request.addProperty("arg1", pic);
     
            // set-up transport and send
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.call(SOAP_ACTION, envelope);
     
            // handle result
            Object result = envelope.getResponse();
        } catch (Exception e) {
            e.printStackTrace();
            
            String x = e.toString();
            System.out.println(x);
        }
    }

	private byte[] getBitmapAsByteArray(Bitmap bitmap) throws IOException {
		// Serialize to a byte array
		ByteArrayOutputStream bos = new ByteArrayOutputStream() ;
		bitmap.compress(CompressFormat.JPEG, 100, bos);
      
		// Get the bytes of the serialized object
		return bos.toByteArray();
	}
   
}