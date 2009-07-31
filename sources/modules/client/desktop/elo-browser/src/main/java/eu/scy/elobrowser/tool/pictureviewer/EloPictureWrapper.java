/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.elobrowser.tool.pictureviewer;

import eu.scy.client.tools.drawing.ELOLoadedChangedEvent;
import eu.scy.client.tools.drawing.ELOLoadedChangedListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import roolo.api.IRepository;
import roolo.elo.api.IELO;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadata;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.metadata.RooloMetadataKeys;
import javax.swing.JTextArea;
import javax.swing.text.DateFormatter;
import roolo.api.search.IMetadataQuery;
import roolo.api.search.IQuery;
import roolo.api.search.ISearchResult;
import roolo.cms.repository.mock.BasicMetadataQuery;
import roolo.cms.repository.search.BasicSearchOperations;
import roolo.elo.metadata.keys.Contribute;

public class EloPictureWrapper { 

	public static final String scyMeloType = "scy/melo";
	public static final String scyImageType = "scy/image";
    private static final Logger logger = Logger.getLogger(EloPictureWrapper.class.getName());
    public static final String untitledDocName = "untitled";
    private IRepository<IELO<IMetadataKey>, IMetadataKey> repository;
    private IMetadataTypeManager<IMetadataKey> metadataTypeManager;
    private IELOFactory<IMetadataKey> eloFactory;
    private IMetadataKey uriKey;
    private IMetadataKey titleKey;
    private IMetadataKey typeKey;
    private IMetadataKey dateCreatedKey;
    private IMetadataKey missionKey;
    private IMetadataKey authorKey;
    private IMetadataKey descriptionKey;
    private IELO<IMetadataKey> elo = null;
    private CopyOnWriteArrayList<ELOLoadedChangedListener> eloLoadedChangedListeners = new CopyOnWriteArrayList<ELOLoadedChangedListener>();
    private Image image;
    private String title;
    private String description;
    private String author;
    private long dateCreated;

    public EloPictureWrapper() {
    }

    public void addELOLoadedChangedListener(
            ELOLoadedChangedListener eloLoadedChangedListener) {
        if (!eloLoadedChangedListeners.contains(eloLoadedChangedListener)) {
            eloLoadedChangedListeners.add(eloLoadedChangedListener);
        }
    }

    public void removeELOLoadedChangedListener(
            ELOLoadedChangedListener eloLoadedChangedListener) {
        if (eloLoadedChangedListeners.contains(eloLoadedChangedListener)) {
            eloLoadedChangedListeners.remove(eloLoadedChangedListener);
        }
    }

    private void sendELOLoadedChangedListener() {
        ELOLoadedChangedEvent eloLoadedChangedEvent = new ELOLoadedChangedEvent(
                this, elo);
        for (ELOLoadedChangedListener eloLoadedChangedListener : eloLoadedChangedListeners) {
            eloLoadedChangedListener.eloLoadedChanged(eloLoadedChangedEvent);
        }
    }

    public IRepository<IELO<IMetadataKey>, IMetadataKey> getRepository() {
        return repository;
    }

    public void setMetadataTypeManager(IMetadataTypeManager<IMetadataKey> metadataTypeManager) {
        this.metadataTypeManager = metadataTypeManager;
        uriKey = metadataTypeManager.getMetadataKey(RooloMetadataKeys.URI.getId());
        logger.info("retrieved key " + uriKey.getId());
        titleKey = metadataTypeManager.getMetadataKey(RooloMetadataKeys.TITLE.getId());
        logger.info("retrieved key " + titleKey.getId());
        typeKey = metadataTypeManager.getMetadataKey(RooloMetadataKeys.TYPE.getId());
        logger.info("retrieved key " + typeKey.getId());
        dateCreatedKey = metadataTypeManager.getMetadataKey(RooloMetadataKeys.DATE_CREATED.getId());
        logger.info("retrieved key " + dateCreatedKey.getId());
        missionKey = metadataTypeManager.getMetadataKey(RooloMetadataKeys.MISSION.getId());
        logger.info("retrieved key " + missionKey.getId());
        authorKey = metadataTypeManager.getMetadataKey(RooloMetadataKeys.AUTHOR.getId());
        logger.info("retrieved key " + authorKey.getId());
        descriptionKey = metadataTypeManager.getMetadataKey(RooloMetadataKeys.DESCRIPTION.getId());
        logger.info("retrieved key " + descriptionKey.getId());
    }

    public void setEloFactory(IELOFactory<IMetadataKey> eloFactory) {
        this.eloFactory = eloFactory;
    }

    public URI getEloUri() {
        if (elo == null) {
            return null;
        } else {
            return elo.getUri();
        }
    }

    public String getELOTitle() {
        if (elo == null) {
            return null;
        } else {
            return (String) elo.getMetadata().getMetadataValueContainer(titleKey).getValue(Locale.ENGLISH);
        }
    }

/*
    	public void newTextAction()
	{
		textarea.setText("");
		elo = null;
        pictureName = untitledDocName;
		sendELOLoadedChangedListener();
	}
*/
    /*
	public void loadTextAction()
	{
		IQuery query = null;
		IMetadataQuery<IMetadataKey> metadataQuery = new BasicMetadataQuery<IMetadataKey>(typeKey,
					BasicSearchOperations.EQUALS, scyTextType, null);
		query = metadataQuery;
		List<ISearchResult> searchResults = repository.search(query);
		URI[] drawingUris = new URI[searchResults.size()];
		int i = 0;
		for (ISearchResult searchResult : searchResults)
			drawingUris[i++] = searchResult.getUri();
		URI drawingUri = (URI) JOptionPane.showInputDialog(null, "Select text", "Select text",
					JOptionPane.QUESTION_MESSAGE, null, drawingUris, null);
		if (drawingUri != null)
		{
			loadElo(drawingUri);
		}
	}
*/

    public void loadPictureAction() {
		IQuery query = null;
		IMetadataQuery<IMetadataKey> metadataQuery = new BasicMetadataQuery<IMetadataKey>(typeKey,
					BasicSearchOperations.EQUALS, scyMeloType, null);
		query = metadataQuery;
		List<ISearchResult> searchResults = repository.search(query);
		URI[] drawingUris = new URI[searchResults.size()];
		int i = 0;
		for (ISearchResult searchResult : searchResults)
			drawingUris[i++] = searchResult.getUri();
		URI drawingUri = (URI) JOptionPane.showInputDialog(null, "Select picture", "Select picture",
					JOptionPane.QUESTION_MESSAGE, null, drawingUris, null);
		if (drawingUri != null)
		{
			loadElo(drawingUri);
		}
	}

    public void loadElo(URI eloUri) {
        logger.info("Trying to load elo " + eloUri);
        IELO<IMetadataKey> newElo = repository.retrieveELO(eloUri);
        if (newElo != null) {
            String eloType = newElo.getMetadata().getMetadataValueContainer(typeKey).getValue().toString();
            if (!scyMeloType.equals(eloType) && !scyImageType.equals(eloType)) {
                throw new IllegalArgumentException("elo (" + eloUri + ") is of wrong type: " + eloType);
            }
            IMetadata metadata = newElo.getMetadata();
            title = metadata.getMetadataValueContainer(titleKey).getValue(Locale.ENGLISH).toString();
				if (scyMeloType.equals(eloType)){
            description = metadata.getMetadataValueContainer(descriptionKey).getValue(Locale.ENGLISH).toString();
            Contribute c = (Contribute) metadata.getMetadataValueContainer(authorKey).getValue();
            author = c.getVCard();
				}
            dateCreated = (Long) metadata.getMetadataValueContainer(dateCreatedKey).getValue();
            try {
                BufferedImage img = ImageIO.read(new ByteArrayInputStream(newElo.getContent().getBytes()));

                image = (Image) Image.fromBufferedImage(img);
            } catch (IOException e) {
                e.printStackTrace();
            }
            elo = newElo;
           sendELOLoadedChangedListener();
        }
    }

    /*
    public void saveTextpadAction() {
        logger.fine("save text");
        if (elo == null) {
            saveAsTextpadAction();
        } else {
            elo.getContent().setXmlString(textarea.getText());
            IMetadata<IMetadataKey> resultMetadata = repository.updateELO(elo);
            eloFactory.updateELOWithResult(elo, resultMetadata);
        }
    }
   */

    /*
    public void saveAsTextpadAction() {
        logger.fine("save as text");
        String textName = JOptionPane.showInputDialog("Enter text title:", pictureName);
        if (StringUtils.hasText(textName)) {
            setDocName(textName);
            elo = eloFactory.createELO();
            elo.setDefaultLanguage(Locale.ENGLISH);
            elo.getMetadata().getMetadataValueContainer(titleKey).setValue(pictureName);
            elo.getMetadata().getMetadataValueContainer(titleKey).setValue(pictureName, Locale.CANADA);
            elo.getMetadata().getMetadataValueContainer(typeKey).setValue("scy/text");
            elo.getMetadata().getMetadataValueContainer(dateCreatedKey).setValue(
                    new Long(System.currentTimeMillis()));
            try {
                elo.getMetadata().getMetadataValueContainer(missionKey).setValue(
                        new URI("roolo://somewhere/myMission.mission"));
                elo.getMetadata().getMetadataValueContainer(authorKey).setValue(
                        new Contribute("my vcard", System.currentTimeMillis()));
            } catch (URISyntaxException e) {
                logger.log(Level.WARNING, "failed to create uri", e);
            }
            IContent content = eloFactory.createContent();
            content.setXmlString(textarea.getText());
            elo.setContent(content);
            IMetadata<IMetadataKey> resultMetadata = repository.addELO(elo);
            eloFactory.updateELOWithResult(elo, resultMetadata);
            // updateEloWithNewMetadata(elo, eloMetadata);
            // logger.fine("metadata xml: \n" + elo.getMetadata().getXml());
            sendELOLoadedChangedListener();
        }
    }
 */
    // @Action
    // public void closeDrawingAction()
    // {
    // this.dispose();
    // }
    //
    public void setRepository(IRepository<IELO<IMetadataKey>, IMetadataKey> repository) {
        this.repository = repository;
    }

    public Image getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getAuthor() {
        return author;
    }

    public String getDateCreatedString() {
        return DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG).format(new Date(dateCreated));
    }

}
