/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.elobrowser.tool.pictureviewer;

import colab.utils.ExtensionFileFilter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Locale;
import javax.swing.JFileChooser;
import org.apache.log4j.Logger;
import roolo.api.IRepository;
import roolo.elo.api.IELO;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.metadata.RooloMetadataKeys;

/**
 *
 * @author sikkenj
 */
public class PictureImporter
{

	 private final static Logger logger = Logger.getLogger(PictureImporter.class);
	 private IRepository<IELO<IMetadataKey>, IMetadataKey> repository;
	 private IELOFactory<IMetadataKey> eloFactory;
	 private IMetadataTypeManager<IMetadataKey> metadataTypeManager;
	 private IMetadataKey titleKey;
	 private IMetadataKey typeKey;
	 private IMetadataKey dateCreatedKey;
	 private IMetadataKey authorKey;
	 private IMetadataKey descriptionKey;
	 private String scyType = "scy/image";

	 private File defaultParent = null;

	 public void setEloFactory(IELOFactory<IMetadataKey> eloFactory)
	 {
		  this.eloFactory = eloFactory;
	 }

	 public void setRepository(IRepository<IELO<IMetadataKey>, IMetadataKey> repository)
	 {
		  this.repository = repository;
	 }

	 public void setMetadataTypeManager(IMetadataTypeManager<IMetadataKey> metadataTypeManager)
	 {
		  this.metadataTypeManager = metadataTypeManager;
		  titleKey = metadataTypeManager.getMetadataKey(RooloMetadataKeys.TITLE.getId());
		  typeKey = metadataTypeManager.getMetadataKey(RooloMetadataKeys.TYPE.getId());
		  dateCreatedKey = metadataTypeManager.getMetadataKey(RooloMetadataKeys.DATE_CREATED.getId());
		  authorKey = metadataTypeManager.getMetadataKey(RooloMetadataKeys.AUTHOR.getId());
		  descriptionKey = metadataTypeManager.getMetadataKey(RooloMetadataKeys.DESCRIPTION.getId());
	 }

	 public void setScyType(String scyType)
	 {
		  this.scyType = scyType;
	 }

	 public void importPicture()
	 {
		  JFileChooser chooser = new JFileChooser();
		  ExtensionFileFilter filter = new ExtensionFileFilter("jpg", "images");
		  chooser.setFileFilter(filter);
		  chooser.setCurrentDirectory(defaultParent);
		  int returnVal = chooser.showOpenDialog(null);
		  if (returnVal == JFileChooser.APPROVE_OPTION)
		  {
//				System.out.println("You chose to open this file: " +
//					chooser.getSelectedFile().getName());
				File selectedFile = chooser.getSelectedFile();
				try
				{
					defaultParent = chooser.getCurrentDirectory();
					 String name = selectedFile.getName();
					 int pointPos = name.indexOf('.');
					 if (pointPos >= 0)
					 {
						  name = name.substring(0, pointPos);
					 }
					 byte[] imageBytes = getFileBytes(selectedFile);
					 IELO<IMetadataKey> elo = eloFactory.createELO();
					 elo.setDefaultLanguage(Locale.ENGLISH);
					 elo.getMetadata().getMetadataValueContainer(titleKey).setValue(name);
					 elo.getMetadata().getMetadataValueContainer(typeKey).setValue(scyType);
					 elo.getMetadata().getMetadataValueContainer(dateCreatedKey).setValue(
						 new Long(System.currentTimeMillis()));
					 elo.getMetadata().getMetadataValueContainer(descriptionKey).setValue("imported from " + selectedFile.getAbsolutePath());
					 elo.getContent().setBytes(imageBytes);
					 repository.addELO(elo);
				} catch (Exception e)
				{
					 logger.error("failed to import image " + selectedFile.getAbsolutePath(), e);
				}
		  }

	 }

	 private byte[] getFileBytes(File file) throws FileNotFoundException, IOException
	 {
		  byte[] bytes = new byte[(int) file.length()];
		  FileInputStream inputStream = null;
		  try
		  {
				inputStream = new FileInputStream(file);
				inputStream.read(bytes);
				return bytes;
		  } finally
		  {
				if (inputStream != null)
				{
					 inputStream.close();
				}
		  }
	 }

	 public static void main(String[] args)
	 {
		  PictureImporter pictureImporter = new PictureImporter();
		  pictureImporter.importPicture();
	 }
}
