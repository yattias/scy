package colab.vt.whiteboard.component;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import org.jdom.Element;

import colab.vt.whiteboard.utils.Cursors;
import colab.vt.whiteboard.utils.XmlUtils;

public class TagPointerAction extends SimpleShapeAction implements PopupMenuListener
{
	private static final long serialVersionUID = 2363285453449655889L;
	// @SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(TagPointerAction.class.getName());

	private Cursor defaultCursor = Cursors.getTagPointerCursor();
	private Cursor popupCursor = Cursors.getTagPointerEditAreaCursor();
	private final Random random = new Random(System.currentTimeMillis());
	private TagDefinition initialTagPointerDefinition = new TagDefinition("", new Color(128, 128,
				128, 128), "click here to change");
	private JPopupMenu tagDefinitionsMenu;
	private TagDefinitionModel tagDefinitions;
	private WhiteboardTagPointer activeWhiteboardTagPointer = null;
	private WhiteboardObjectContainer activeWhiteboardObjectContainer = null;
	private boolean tagDefinitionsMenuActive = false;
	private boolean tagDefinitionsMenuAboutToStop = false;

	class TagDefinitionChangeAction extends AbstractAction
	{
		private static final long serialVersionUID = 6065647888918097052L;

		final TagDefinition tagDefinition;

		public TagDefinitionChangeAction(TagDefinition tagDefinition, String menuName)
		{
			super(menuName);
			this.tagDefinition = tagDefinition;
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			if (activeWhiteboardTagPointer != null)
			{
				activeWhiteboardObjectContainer.repaint();
				activeWhiteboardTagPointer.setTagDefinition(tagDefinition);
				activeWhiteboardObjectContainer.repaint();
			}
		}
	}

	public TagPointerAction(WhiteboardPanel whiteboardPanel, String label)
	{
		super(whiteboardPanel, label, Cursors.getImageIcon("iconTagPointer.png"));
		setTagDefinitions(createTagDefinitionModel());
	}

	private TagDefinitionModel createTagDefinitionModel()
	{
		List<TagLevel> tagLevels = new ArrayList<TagLevel>();
		for (int i = 1; i <= 5; i++)
		{
			List<TagLevel> subTagLevels = new ArrayList<TagLevel>();
			for (int j = 1; j <= 3; j++)
			{
				TagDefinition tagDefinition = new TagDefinition("" + i + j, getRandomcolor(),
							"tooltip explaining " + i + j);
				subTagLevels.add(new TagLevel("" + j, tagDefinition));
			}
			Color color = new Color(getRandomcolor().getRGB());
			tagLevels.add(new TagLevel("" + i, color, "tooltip explaining " + i, subTagLevels));
		}
		return new TagDefinitionModel(tagLevels);
	}

	private void setTagDefinitions(TagDefinitionModel tagDefinitions)
	{
		this.tagDefinitions = tagDefinitions;
		createTagDefinitionsMenu();
	}

	private void createTagDefinitionsMenu()
	{
		tagDefinitionsMenu = new JPopupMenu();
		for (TagLevel tagLevel : tagDefinitions.getTagLevels())
		{
			tagDefinitionsMenu.add(tagDefinitionsMenu.add(createTagLevelMenuItem(tagLevel)));
		}
		tagDefinitionsMenu.addPopupMenuListener(this);
	}

	private JMenuItem createTagLevelMenuItem(TagLevel tagLevel)
	{
		JMenuItem menuItem;
		if (tagLevel.getTagDefinition() != null)
		{
			TagDefinitionChangeAction tagDefinitionChangeAction = new TagDefinitionChangeAction(
						tagLevel.getTagDefinition(), tagLevel.getName());
			menuItem = new JMenuItem(tagDefinitionChangeAction);
			menuItem.addActionListener(this);
			menuItem.setToolTipText(tagLevel.getTagDefinition().getTooltip());
		}
		else
		{
			menuItem = new JMenu(tagLevel.getName());
			for (TagLevel subTagLevel : tagLevel.getTagLevels())
			{
				menuItem.add(createTagLevelMenuItem(subTagLevel));
			}
			menuItem.setToolTipText(tagLevel.getTooltip());
		}
		menuItem.setForeground(tagLevel.getColor());
		return menuItem;
	}

	@Override
	public Cursor getDefaultCursor()
	{
		return defaultCursor;
	}

	@Override
	public Element getStatus()
	{
		Element status = super.getStatus();
		Element tagDefinitionsElement = new Element(XmlNames.tagDefinitions);
		status.addContent(tagDefinitionsElement);
		for (TagLevel subTagLevel : tagDefinitions.getTagLevels())
		{
			tagDefinitionsElement.addContent(getTagLevelStatus(subTagLevel));
		}
		return status;
	}

	private Element getTagLevelStatus(TagLevel tagLevel)
	{
		Element tagLevelElement = new Element(XmlNames.tagLevel);
		XmlUtils.addXmlTag(tagLevelElement, XmlNames.name, tagLevel.getName());
		if (tagLevel.getTagDefinition() != null)
		{
			Element tagPointerDefinition = new Element(XmlNames.tagDefinition);
			XmlUtils.addXmlTag(tagPointerDefinition, XmlNames.tag, tagLevel.getTagDefinition()
						.getTag());
			XmlUtils.addXmlTag(tagPointerDefinition, XmlNames.color, tagLevel.getTagDefinition()
						.getColor());
			XmlUtils.addXmlTag(tagPointerDefinition, XmlNames.tooltip, tagLevel.getTagDefinition()
						.getTooltip());
			tagLevelElement.addContent(tagPointerDefinition);
		}
		else
		{
			XmlUtils.addXmlTag(tagLevelElement, XmlNames.color, tagLevel.getColor());
			XmlUtils.addXmlTag(tagLevelElement, XmlNames.tooltip, tagLevel.getTooltip());
			for (TagLevel subTagLevel : tagLevel.getTagLevels())
			{
				tagLevelElement.addContent(getTagLevelStatus(subTagLevel));
			}
		}
		return tagLevelElement;
	}

	@Override
	public void setStatus(Element status)
	{
		super.setStatus(status);
		Element tagDefinitionsElement = status.getChild(XmlNames.tagDefinitions);
		setTagDefinitions(new TagDefinitionModel(setTagLevels(tagDefinitionsElement)));
	}

	private List<TagLevel> setTagLevels(Element tagLevelsElement)
	{
		List<TagLevel> tagLevelList = new ArrayList<TagLevel>();
		@SuppressWarnings("unchecked")
		List<Element> tagLevels = tagLevelsElement.getChildren(XmlNames.tagLevel);
		for (Element tagLevel : tagLevels)
		{
			tagLevelList.add(setTagLevel(tagLevel));
		}
		return tagLevelList;
	}

	private TagLevel setTagLevel(Element tagLevel)
	{
		String name = tagLevel.getChildText(XmlNames.name);
		Element tagDefintion = tagLevel.getChild(XmlNames.tagDefinition);
		if (tagDefintion != null)
		{
			// it only contains a TagDefinition
			String tag = tagDefintion.getChildText(XmlNames.tag);
			Color color = XmlUtils.getColorValueFromXmlTag(tagDefintion, XmlNames.color);
			String tooltip = tagDefintion.getChildText(XmlNames.tooltip);
			TagDefinition tagDef = new TagDefinition(tag, color, tooltip);
			return new TagLevel(name, tagDef);
		}
		// it contains a list of tagLevels
		Color color = XmlUtils.getColorValueFromXmlTag(tagLevel, XmlNames.color);
		String tooltip = tagLevel.getChildText(XmlNames.tooltip);
		List<TagLevel> tagLevels = setTagLevels(tagLevel);
		return new TagLevel(name, color, tooltip, tagLevels);
	}

	@Override
	protected WhiteboardSimpleShape createWhiteboardSimpleShape()
	{
		return new WhiteboardTagPointer(initialTagPointerDefinition.getTag(),
					initialTagPointerDefinition.getColor(), initialTagPointerDefinition.getTooltip(),
					getWhiteboardPanel().getGraphics());
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		// if (tagDefinitionsMenuActive)
		// {
		// setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		// return;
		// }
		WhiteboardObjectContainer whiteboardTagPointerContainer = getWhiteboardTagPointerContainerUnderMouse(
					e.getX(), e.getY());
		if (whiteboardTagPointerContainer != null)
		{
			WhiteboardTagPointer whiteboardTagPointer = (WhiteboardTagPointer) whiteboardTagPointerContainer
						.getWhiteboardObject();
			if (whiteboardTagPointer.isLocationInTagBounds(whiteboardTagPointerContainer
						.getObjectMousePoint(e.getX(), e.getY())))
			{
				setCursor(popupCursor);
				return;
			}
		}
		setCursor(defaultCursor);
	}

	private WhiteboardObjectContainer getWhiteboardTagPointerContainerUnderMouse(int x, int y)
	{
		WhiteboardContainer mouseWhiteboardContainer = getWhiteboardPanel()
					.getWhiteboardContainerUnderMouse(x, y);
		if (mouseWhiteboardContainer != null)
		{
			if (mouseWhiteboardContainer instanceof WhiteboardObjectContainer)
			{
				WhiteboardObjectContainer mouseWhiteboardObjectContainer = (WhiteboardObjectContainer) mouseWhiteboardContainer;
				if (mouseWhiteboardObjectContainer.getWhiteboardObject() instanceof WhiteboardTagPointer)
				{
					return mouseWhiteboardObjectContainer;
				}
			}
		}
		return null;

	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		if (activeWhiteboardTagPointer != null)
		{
			activeWhiteboardTagPointer = null;
			activeWhiteboardObjectContainer = null;
			return;
		}
		getWhiteboardPanel().deselectAllWhiteboardContainers();
		WhiteboardContainer mouseWhiteboardContainer = getWhiteboardPanel()
					.getWhiteboardContainerUnderMouse(e.getX(), e.getY());
		if (mouseWhiteboardContainer != null)
		{
			if (mouseWhiteboardContainer instanceof WhiteboardObjectContainer)
			{
				WhiteboardObjectContainer mouseWhiteboardObjectContainer = (WhiteboardObjectContainer) mouseWhiteboardContainer;
				if (mouseWhiteboardObjectContainer.getWhiteboardObject() instanceof WhiteboardTagPointer)
				{
					WhiteboardTagPointer whiteboardTagPointer = (WhiteboardTagPointer) mouseWhiteboardObjectContainer
								.getWhiteboardObject();
					if (whiteboardTagPointer.isLocationInTagBounds(mouseWhiteboardContainer
								.getObjectMousePoint(e.getX(), e.getY())))
					{
						tagDefinitionsMenuActive = true;
						tagDefinitionsMenuAboutToStop = false;
						tagDefinitionsMenu.show(getWhiteboardPanel(), e.getX(), e.getY());
						activeWhiteboardTagPointer = whiteboardTagPointer;
						activeWhiteboardObjectContainer = mouseWhiteboardObjectContainer;
						return;
					}
				}
			}
		}
		super.mousePressed(e);
	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		if (!tagDefinitionsMenuActive)
			super.mouseDragged(e);
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		if (!tagDefinitionsMenuActive)
		{
			super.mouseReleased(e);
		}
		// System.out.println("MouseEvent:" + e);
		if (tagDefinitionsMenuActive && tagDefinitionsMenuAboutToStop)
		{
			tagDefinitionsMenuActive = false;
			// System.out.println("tagDefinitionsMenuActive set to false");
		}
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		logger.info("tagDefinitionsMenu: " + e);
		if (e.getSource() instanceof JMenuItem)
		{
			JMenuItem menuItem = (JMenuItem) e.getSource();
			if (menuItem.getAction() instanceof TagDefinitionChangeAction)
			{
				TagDefinitionChangeAction tagDefinitionChangeAction = (TagDefinitionChangeAction) menuItem
							.getAction();
				tagDefinitionChangeAction.actionPerformed(null);
				activeWhiteboardTagPointer = null;
				activeWhiteboardObjectContainer = null;
				tagDefinitionsMenuActive = false;
				return;
			}
		}
		super.actionPerformed(e);
	}

	@Override
	public String getType()
	{
		return XmlNames.tagPointer;
	}

	private Color getRandomcolor()
	{
		return new Color(getRandom(0, 255), getRandom(0, 255), getRandom(0, 255), getRandom(64, 192));
	}

	private int getRandom(int min, int max)
	{
		return min + random.nextInt(max - min);
	}

	@Override
	public void popupMenuCanceled(PopupMenuEvent e)
	{
		tagDefinitionsMenuAboutToStop = true;
	}

	@Override
	public void popupMenuWillBecomeInvisible(PopupMenuEvent e)
	{
		tagDefinitionsMenuAboutToStop = true;
	}

	@Override
	public void popupMenuWillBecomeVisible(PopupMenuEvent e)
	{
		// TODO Auto-generated method stub

	}

}
