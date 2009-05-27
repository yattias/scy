package eu.scy.webservices.mobileservice;

import org.apache.cxf.common.util.Base64Exception;
import org.apache.cxf.common.util.Base64Utility;

import javax.xml.bind.annotation.XmlTransient;

/**
 * Created by IntelliJ IDEA.
 * Date: 24.mar.2009
 * Time: 12:52:36
  */
public class MobileELO {
	private String title;
	private String b64Image;
	private String description;

	public MobileELO() {
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	@XmlTransient
	public byte[] getImage() {
		try {
			return Base64Utility.decode(b64Image);
		} catch (Base64Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public void setImage(byte[] image) {
		this.b64Image = Base64Utility.encode(image);
	}
	public String getB64Image() {
		return this.b64Image;
	}
	public void setB64Image(String b64Image) {
		this.b64Image = b64Image;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public String toString() {
		return "MobileELO{" +
				"title='" + title + '\'' +
				", description='" + description + '\'' +
				'}';
	}
}
