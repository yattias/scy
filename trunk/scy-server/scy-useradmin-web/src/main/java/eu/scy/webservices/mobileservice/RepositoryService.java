package eu.scy.webservices.mobileservice;

import org.springframework.beans.factory.annotation.Autowired;
import roolo.cms.repository.mock.MockRepository;

import javax.imageio.ImageIO;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 24.mar.2009
 * Time: 10:45:45
 */

@WebService
public class RepositoryService {

    private static Logger log = Logger.getLogger("RepositoryService.class");

	@Autowired
	private MockRepository repository;

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
		return "Image saved to "+ fname+". The highest level of fakeness is reached!";
	}
	@WebMethod
    public MobileELO getELO(@WebParam int id) {
        MobileELO me = new MobileELO();
		me.setTitle("Hello. Me ELO. You ELO?");
		return me;
	}
}
