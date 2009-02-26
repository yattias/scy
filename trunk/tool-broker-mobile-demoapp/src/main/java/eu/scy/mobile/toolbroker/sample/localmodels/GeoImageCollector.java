package eu.scy.mobile.toolbroker.sample.localmodels;

import com.sun.me.web.request.Arg;
import com.sun.me.web.request.ProgressInputStream;
import com.sun.me.web.request.ProgressListener;

import javax.microedition.lcdui.Image;
import javax.microedition.io.HttpConnection;
import javax.microedition.io.Connector;
import java.util.Vector;
import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;

/**
 * Created: 11.feb.2009 09:50:17
 *
 * @author Bjørge Næss
 */


public class GeoImageCollector implements ProgressListener {
    //TODO: Move this to a more appropriate place
    public Image loadImage(final String location) {
        HttpConnection conn = null;
        InputStream is = null;
        try {
            conn = (HttpConnection) Connector.open(location);
                        conn.setRequestProperty("accept", "image/*");

            final int responseCode = conn.getResponseCode();
            if (responseCode != HttpConnection.HTTP_OK) return null;

            final int totalToReceive = conn.getHeaderFieldInt(Arg.CONTENT_LENGTH, 0);
            is = new ProgressInputStream(conn.openInputStream(), totalToReceive, this, null, 1024);

            final ByteArrayOutputStream bos = new ByteArrayOutputStream(Math.max(totalToReceive, 8192));
            final byte[] buffer = new byte[4096];
            for (int nread = is.read(buffer); nread >= 0; nread = is.read(buffer)) {
                bos.write(buffer, 0, nread);
            }
            return Image.createImage(new ByteArrayInputStream(bos.toByteArray()));
        }
        catch (IOException e) {
            e.printStackTrace();
        } finally {
             try {
                if (is != null)is.close();
                if (conn != null) conn.close();

            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        return null;
    }
	private String name;

	private Vector images = new Vector();

	public GeoImageCollector() {}
	public GeoImageCollector(String name, Vector images) {
		this.name = name;
		this.images = images;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Vector getImages() {
		return images;
	}

	public void setImages(Vector images) {
		this.images = images;
	}

	public void addImage(String image) {
		this.images.addElement(image);
	}

	public String toString() {
		return "I am a GeoImageCollector (" +
				"name='" + name + '\'' +
				')';
	}

	public void readProgress(Object o, int i, int i1) {
	}

	public void writeProgress(Object o, int i, int i1) {
	}
}
