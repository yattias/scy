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
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class DataCollector extends Activity {

	public static final String PICTURE = "PICTURE";
	
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
					showDialog("Warning", "You have to take a photo first!");
				} else if(title.getText() != null && title.getText().length() == 0) {
					showDialog("Warning", "You have to provide a title first!");
				} else {
					saveELO();
				}
			}
        	
        });
        
        // if change the display orientation we check if a picture is being passed inside the saved state
        if(savedInstanceState != null && savedInstanceState.containsKey(PICTURE)) {
        	Log.d("DataCollector->onCreate", "contains key? " + Boolean.toString(savedInstanceState.containsKey(PICTURE)));
        	picture = (Bitmap) savedInstanceState.get(PICTURE);
        	updatePreview();
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	// we create only one menu entry called exit to close the application
    	menu.addSubMenu("Exit");
    	return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	// if the exit menu has been selected, we call finish() to shutdown the activity
    	if("Exit".equals(item.getTitle())) {
    		finish();
    	}
    	return super.onOptionsItemSelected(item);
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    	super.onSaveInstanceState(outState);
    	Log.d("DataCollector", "Saving picture to bundle");
    	// here we store the bitmap into the state to restore it on rotation change
    	outState.putParcelable(PICTURE, picture);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	
    	if(requestCode == IMAGE_TAKEN && data != null) {
    		Bundle bundle = data.getExtras();
    		if(bundle != null) {
    			picture = (Bitmap) bundle.get("data");
    			updatePreview();
    		}
    	}
    }
    
    /**
     * Updates the ImageView preview with the current picture.
     */
    private void updatePreview() {
    	if(picture != null) {
    		preview.setImageBitmap(picture);
    	}
    }

    /**
     * Shows a nice dialog with a title, a message and a "Ok" button.
     * 
     * @param title
     * @param message
     */
	private void showDialog(String title, String message) {
		AlertDialog alertDialog = new AlertDialog.Builder(DataCollector.this).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// we do nothing here
			}
		});
		alertDialog.show();
	}

	// webservice stuff - very hard coded right now :(
	
	private static final String SOAP_ACTION = "saveData";
    private static final String METHOD_NAME = "saveData";
    private static final String NAMESPACE = "http://mobileservice.webservices.scy.eu/";
    private static final String URL = "http://scy.collide.info:8080/scy-useradmin-web/services/RepositoryService";
//    private static final String URL = "http://134.91.34.218:8080/scy-useradmin-web/services/RepositoryService";
     
    /**
     * Method to save the picture and the title into the web service.
     */
    private void saveELO() {
    	final ProgressDialog pd = ProgressDialog.show(this, "Please wait...", "Your ELO is being uploaded!");
    	
    	Thread uploadThread = new Thread("UploadThread") {
    		@Override
    		public void run() {
    			try {
    				// get title
    				String t = title.getText().toString();
    				
    				// get picture as base64 encoded String
    				byte[] picArray = getBitmapAsByteArray(picture);
    				String pic = Base64.encode(picArray);
    				
    				// set-up SOAP message
    				SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
    				request.addProperty("title", t);
    				request.addProperty("description", "-");
    				request.addProperty("b64image", pic);
    				
    				// set-up transport and send
    				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
    				envelope.setOutputSoapObject(request);
    				HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
    				androidHttpTransport.call(SOAP_ACTION, envelope);
    				
    				DataCollector.this.runOnUiThread(new Runnable() {
						public void run() {
							pd.dismiss();
							showDialog("Success", "The photo has been stored successfully!");
							title.setText("");
							preview.setImageBitmap(null);
						}
    				});
    				
    				picture = null;
    				// handle result
    				//  Object result = envelope.getResponse();
    				
    				// remove progress dialog
    			} catch (final Exception e) {
    				Log.d("DataCollector->SOAP", e.getMessage());
    				DataCollector.this.runOnUiThread(new Runnable() {
						public void run() {
							pd.dismiss();
							showDialog("Error", e.toString());
						}
    				});
    			}
    		}
    	};
    	uploadThread.start();
    }

    /**
     * This method converts an bitmap into an byte array, containing a JPEG image.
     * @param bitmap
     * @return
     * @throws IOException
     */
	private byte[] getBitmapAsByteArray(Bitmap bitmap) throws IOException {
		// Serialize to a byte array
		ByteArrayOutputStream bos = new ByteArrayOutputStream() ;
		bitmap.compress(CompressFormat.JPEG, 100, bos);
      
		// Get the bytes of the serialized object
		return bos.toByteArray();
	}
   
}