package eu.scy.webservices.mobileservice;

import org.springframework.beans.factory.annotation.Autowired;

import javax.imageio.ImageIO;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlTransient;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.logging.Logger;
import java.util.Locale;
import java.net.URI;
import java.net.URISyntaxException;

import eu.scy.webapp.services.roolo.RooloManager;
import roolo.elo.api.IELO;
import roolo.elo.api.IContent;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadata;
import roolo.elo.api.metadata.RooloMetadataKeys;
import roolo.elo.metadata.keys.Contribute;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 24.mar.2009
 * Time: 10:45:45
 */

@WebService
public class RepositoryService {

    private static Logger log = Logger.getLogger("RepositoryService.class");

    private RooloManager rooloManager;


	@WebMethod
    public String saveELO(@WebParam MobileELO elo) {
        log.info("KICKING OFF WITH THIS FUNKY MOBILE ELO: " + elo);
		
		log.info("THE FUNKY MOBILE ELO IS CONVERTED INTO: " + elo);
			
        //repository.addELO(EloConverter.convert(elo));
		String fname = "c:\\" +elo.getTitle()+".jpg";
		File f = new File(fname);
		try {
			InputStream in = new ByteArrayInputStream(elo.getImage());
			BufferedImage image = ImageIO.read(in);
			FileOutputStream fos = new FileOutputStream(f);
			ImageIO.write(image, "jpeg", fos);
			in.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}
        storeEloToRoolo(elo);
		return "Image saved to "+ fname+". The highest level of fakeness is reached!";
	}


    @XmlTransient
    private void storeEloToRoolo(MobileELO melo) {
            if(getRooloManager().getEloFactory() != null) {
            IELO elo = getRooloManager().getEloFactory().createELO();
            elo.setDefaultLanguage(Locale.ENGLISH);
            elo.getMetadata().getMetadataValueContainer(getRooloManager().getMetadataTypeManager().getMetadataKey(RooloMetadataKeys.DATE_CREATED.getId())).setValue(new Long(System.currentTimeMillis()));


            try {
                elo.getMetadata().getMetadataValueContainer(getRooloManager().getMetadataTypeManager().getMetadataKey(RooloMetadataKeys.MISSION.getId())).setValue(new URI("roolo://somewhere/myMission.mission"));
                elo.getMetadata().getMetadataValueContainer(getRooloManager().getMetadataTypeManager().getMetadataKey(RooloMetadataKeys.AUTHOR.getId())).setValue(new Contribute("my vcard", System.currentTimeMillis()));
                elo.getMetadata().getMetadataValueContainer(getRooloManager().getMetadataTypeManager().getMetadataKey(RooloMetadataKeys.TYPE.getId())).setValue("scy/melo");
            } catch (URISyntaxException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

            IContent content = getRooloManager().getEloFactory().createContent();
            content.setXmlString("<melo><name>" + melo.getTitle() + "</name<description>" + melo.getDescription() + "</description></melo>");
            elo.setContent(content);
            IMetadata<IMetadataKey> resultMetadata = getRooloManager().getRepository().addELO(elo);
            getRooloManager().getEloFactory().updateELOWithResult(elo, resultMetadata);

        }
    }


	@WebMethod
    public MobileELO getELO(@WebParam int id) {
        MobileELO me = new MobileELO();
		me.setTitle("Hello. Me ELO. You ELO?");
		return me;
	}

    public RooloManager getRooloManager() {
        return rooloManager;
    }

    public void setRooloManager(RooloManager rooloManager) {
        this.rooloManager = rooloManager;
    }
}
