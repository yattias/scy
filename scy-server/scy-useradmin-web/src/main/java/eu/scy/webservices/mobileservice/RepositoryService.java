package eu.scy.webservices.mobileservice;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import com.sun.xml.messaging.saaj.util.ByteInputStream;

import roolo.elo.api.IContent;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadata;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.metadata.RooloMetadataKeys;
import roolo.elo.metadata.keys.Contribute;
import eu.scy.webapp.services.roolo.RooloManager;

/**
 * Created by IntelliJ IDEA. User: Henrik Date: 24.mar.2009 Time: 10:45:45
 */

@WebService
public class RepositoryService {

	private static Logger log = Logger.getLogger("RepositoryService.class");

	@XmlElement(type = Object.class)
	private RooloManager rooloManager;

	@WebMethod(action = "saveData")
	public String saveData(@WebParam(name = "title") String title,
			@WebParam(name = "description") String description,
			@WebParam(name = "b64image") String b64) {
		MobileELO me = new MobileELO();
		me.setTitle(title);
		me.setDescription(description);
		me.setB64Image(b64);

//		saveImage(me);

		return saveELO(me);
	}
	
	/*
	 * Just a small test method to write out the image as JPEG to the harddrive
	 */
	private void saveImage(MobileELO me) {

		try {
			byte[] bs = me.getImage();
			ByteInputStream bis = new ByteInputStream(bs, bs.length);
			BufferedImage image = ImageIO.read(bis);

			File f = new File("/Users/giemza/" + me.getTitle() + ".jpg");
			ImageIO.write(image, "jpg", f);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@WebMethod(action = "saveELO")
	public String saveELO(@WebParam MobileELO elo) {
		log.info("KICKING OFF WITH THIS FUNKY MOBILE ELO: " + elo);

		// repository.addELO(EloConverter.convert(elo));
		/*
		 * String fname = "c:\\" +elo.getTitle()+".jpg"; File f = new
		 * File(fname); try { InputStream in = new
		 * ByteArrayInputStream(elo.getImage()); BufferedImage image =
		 * ImageIO.read(in); FileOutputStream fos = new FileOutputStream(f);
		 * ImageIO.write(image, "jpeg", fos); in.close(); fos.close();
		 * 
		 * } catch (IOException e) { e.printStackTrace(); //To change body of
		 * catch statement use File | Settings | File Templates. }
		 */
		storeEloToRoolo(elo);
		return "HES";// "Image saved to "+
						// fname+". The highest level of fakeness is reached!";
	}

	@WebMethod(action = "getELO")
	public MobileELO getELO(@WebParam int id) {
		MobileELO me = new MobileELO();
		me.setTitle("TestELO");
		me.setDescription("Hello. Me ELO. You ELO?");
		me.setB64Image("THISISTHEBASE64IMAGEENCODED;)TFA");
		return me;
	}

	@XmlTransient
	public RooloManager getRooloManager() {
		return rooloManager;
	}

	@XmlTransient
	public void setRooloManager(RooloManager rooloManager) {
		this.rooloManager = rooloManager;
	}

	@XmlTransient
	private void storeEloToRoolo(MobileELO melo) {
		if (rooloManager.getEloFactory() != null) {
			IELO elo = rooloManager.getEloFactory().createELO();
			elo.setDefaultLanguage(Locale.ENGLISH);
			elo.getMetadata().getMetadataValueContainer(
					rooloManager.getMetadataTypeManager().getMetadataKey(
							RooloMetadataKeys.DATE_CREATED.getId())).setValue(
					new Long(System.currentTimeMillis()));
			log.info(RooloMetadataKeys.TITLE.getId());
			log.info(rooloManager.getMetadataTypeManager().toString());
			try {
				elo.getMetadata().getMetadataValueContainer(
						rooloManager.getMetadataTypeManager().getMetadataKey(
								RooloMetadataKeys.MISSION.getId())).setValue(
						new URI("roolo://somewhere/myMission.mission"));
				elo.getMetadata().getMetadataValueContainer(
						rooloManager.getMetadataTypeManager().getMetadataKey(
								RooloMetadataKeys.AUTHOR.getId())).setValue(
						new Contribute("my vcard", System.currentTimeMillis()));
				elo.getMetadata().getMetadataValueContainer(
						rooloManager.getMetadataTypeManager().getMetadataKey(
								RooloMetadataKeys.TYPE.getId())).setValue(
						"scy/melo");
				elo.getMetadata().getMetadataValueContainer(
						rooloManager.getMetadataTypeManager().getMetadataKey(
								RooloMetadataKeys.TITLE.getId())).setValue(
						melo.getTitle());
				elo.getMetadata().getMetadataValueContainer(
						rooloManager.getMetadataTypeManager().getMetadataKey(
								RooloMetadataKeys.DESCRIPTION.getId()))
						.setValue(melo.getDescription());
			} catch (URISyntaxException e) {
				log.severe(e.getMessage()); // To change body of catch statement
											// use File | Settings | File
											// Templates.
			}

			IContent content = rooloManager.getEloFactory().createContent();
			content.setBytes(melo.getImage());
			elo.setContent(content);
			IMetadata<IMetadataKey> resultMetadata = rooloManager
					.getRepository().addELO(elo);
			rooloManager.getEloFactory().updateELOWithResult(elo,
					resultMetadata);

		}
	}
}
