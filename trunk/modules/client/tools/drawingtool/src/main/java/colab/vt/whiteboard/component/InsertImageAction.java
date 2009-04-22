package colab.vt.whiteboard.component;

import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFileChooser;

import colab.utils.ExtensionFileFilter;
import colab.vt.whiteboard.utils.Cursors;

public class InsertImageAction extends SimpleSelectionAction
{
	private static final long serialVersionUID = 3752188223736154889L;

	private static final Logger logger = Logger.getLogger(InsertImageAction.class.getName());

	private Cursor defaultCursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
	private Cursor aboveCursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
	private File lastUsedDirectory = null;

	public InsertImageAction(WhiteboardPanel whiteboardPanel, String label)
	{
		super(whiteboardPanel, label, Cursors.getImageIcon("iconInsertImage.png"));
	}

	@Override
	public String getType()
	{
		return XmlNames.image;
	}

	 @Override
	 public Cursor getDefaultCursor()
	 {
	 return defaultCursor;
	 }
	
	 @Override
	 public Cursor getAboveCursor()
	 {
	 return aboveCursor;
	 }
	
	@Override
	public void mousePressed(MouseEvent e)
	{
		JFileChooser imageChooser = new JFileChooser(lastUsedDirectory);
		ExtensionFileFilter imagesFilter = new ExtensionFileFilter(
					new String[] { "gif", "png", "jpg" }, "GIF, PNG & JPEG Images");
		imageChooser.addChoosableFileFilter(imagesFilter);
		imageChooser.showOpenDialog(getWhiteboardPanel());
		File imageFile = imageChooser.getSelectedFile();
		if (imageFile != null)
		{
			lastUsedDirectory = imageChooser.getCurrentDirectory();
			WhiteboardImage whiteboardImage = new WhiteboardImage();
			whiteboardImage.setBegin(e.getX(), e.getY());
			try
			{
				whiteboardImage.setImageFile(imageFile);
			}
			catch (IOException ioe)
			{
				// TODO Auto-generated catch block
				logger.log(Level.WARNING,"problems with loading of image from " + imageFile.getAbsolutePath(),ioe);
				return;
			}
			WhiteboardObjectContainer whiteboardObjectContainer = new WhiteboardObjectContainer(
						getWhiteboardPanel(), whiteboardImage);
			getWhiteboardPanel().setTemporaryWhiteboardContainer(whiteboardObjectContainer);
			getWhiteboardPanel()
						.makeTemporaryWhiteboardContainerFinal(whiteboardObjectContainer, true);
			whiteboardObjectContainer.repaint();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		super.mouseClicked(e);
	}

	@Override
	protected boolean applyAction(MouseEvent e, WhiteboardContainer whiteboardContainer)
	{
		return false;
	}

	@Override
	public Cursor getActionCursor()
	{
		return null;
	}

	@Override
	protected void prepareAction(MouseEvent e, WhiteboardContainer whiteboardContainer)
	{
	}

}
