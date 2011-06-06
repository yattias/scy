package eu.scy.client.desktop.desktoputils.metalthemes;

import java.io.Serializable;

import java.awt.Color;
import java.awt.Font;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.metal.OceanTheme;

import java.util.List;
import java.util.ArrayList;
import javax.swing.UIDefaults;
import javax.swing.AbstractButton;
import javax.swing.plaf.IconUIResource;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.net.URL;
import javax.swing.ImageIcon;

/**
 * <p>Title: Colab layout test</p>
 * <p>Description: </p>
 * This class contains the theme setting (font and colors) for the Look and Feel of a colab room
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: University of Twente</p>
 * @author Jakob Sikken
 * @version 1.0
 */
public class CustomMetalTheme extends OceanTheme implements Serializable
{

   private static final long serialVersionUID = 3319176752986169418L;
   private static String resourceDir = "resources/";
   public ColorUIResource[] colors;
   final public static int primary1 = 0;
   final public static int primary2 = 1;
   final public static int primary3 = 2;
   final public static int secondary1 = 3;
   final public static int secondary2 = 4;
   final public static int secondary3 = 5;
   final public static int black = 6;
   final public static int white = 7;
   final public static int focus = 8;
   final public static int desktop = 9;
   final public static int control = 10;
   final public static int controlShadow = 11;
   final public static int controlDarkShadow = 12;
   final public static int controlInfo = 13;
   final public static int controlHighlight = 14;
   final public static int controlDisabled = 15;
   final public static int primaryControl = 16;
   final public static int primaryControlShadow = 17;
   final public static int primaryControlDarkShadow = 18;
   final public static int primaryControlInfo = 19;
   final public static int primaryControlHighlight = 20;
   final public static int systemText = 21;
   final public static int controlText = 22;
   final public static int inactiveControlText = 23;
   final public static int inactiveSystemText = 24;
   final public static int userText = 25;
   final public static int textHighlight = 26;
   final public static int highlightedText = 27;
   final public static int windowBackground = 28;
   final public static int windowTitleBackground = 29;
   final public static int windowTitleForeground = 30;
   final public static int windowTitleInactiveBackground = 31;
   final public static int windowTitleInactiveForeground = 32;
   final public static int menuBackground = 33;
   final public static int menuForeground = 34;
   final public static int menuSelectedBackground = 35;
   final public static int menuSelectedForeground = 36;
   final public static int menuDisabledForeground = 37;
   final public static int separatorBackground = 38;
   final public static int separatorForeground = 39;
   final public static int acceleratorForeground = 40;
   final public static int acceleratorSelectedForeground = 41;
   final public static int nrOfColors = 42;
   final private static int[] primary1ColorMapped =
   {
      primaryControlDarkShadow, separatorForeground, acceleratorForeground
   };
   final private static int[] primary2ColorMapped =
   {
      focus, desktop, primaryControlShadow, menuSelectedBackground
   };
   final private static int[] primary3ColorMapped =
   {
      primaryControl, textHighlight, windowTitleBackground
   };
   final private static int[] secondary1ColorMapped =
   {
      controlDarkShadow
   };
   final private static int[] secondary2ColorMapped =
   {
      controlShadow, controlDisabled, inactiveSystemText,
      menuDisabledForeground
   };
   final private static int[] secondary3ColorMapped =
   {
      control, windowTitleInactiveBackground, menuBackground
   };
   final private static int[] blackColorMapped =
   {
      controlInfo, primaryControlInfo, systemText, userText,
      windowTitleForeground, windowTitleInactiveForeground, menuForeground,
      menuSelectedForeground, acceleratorSelectedForeground
   };
   final private static int[] whiteColorMapped =
   {
      controlHighlight, primaryControlHighlight, windowBackground,
      separatorBackground
   };
   final public static String[] colorNames =
   {
      "primary 1", "primary 2", "primary 3", "secondary 1", "secondary 2",
      "secondary 3", "black", "white", "Focus", "Desktop", "Control",
      "Control shadow", "Control dark shadow", "Control info",
      "Control highlight", "Control disabled", "Primary control",
      "Primary control shadow", "Primary control dark shadow",
      "Primary control info", "Primary control highlight", "System text",
      "Control text", "Inactive control text", "Inactive system text",
      "User text", "Text highlight", "Highlighted text", "Window background",
      "Window title background", "Window title foreground",
      "Window title inactive background", "Window title inactive foreground",
      "Menu background", "Menu foreground", "Menu selected background",
      "Menu selected foreground", "Menu disabled foreground",
      "Separator background", "Separator foreground", "Accelerator foreground",
      "Accelerator selected foreground"
   };
   public FontUIResource[] fonts;
   final public static int controlTextFont = 0;
   final public static int systemTextFont = 1;
   final public static int userTextFont = 2;
   final public static int menuTextFont = 3;
   final public static int windowTitleFont = 4;
   final public static int subTextFont = 5;
   final public static int nrOfFonts = 6;
   final public static String[] fontNames =
   {
      "control text", "system text", "user text", "menu text", "window title",
      "sub text"
   };
   private boolean styled;
   private String name;
   public final static String fileExtension = ".metalColors";

   // InternalFrame Icon
   // Delegates to different icons based on button state
   private static class IFIcon
      extends IconUIResource
   {

      private Icon pressed;

      public IFIcon(Icon normal, Icon pressed)
      {
         super(normal);
         this.pressed = pressed;
      }

      @Override
      public void paintIcon(Component c, Graphics g, int x, int y)
      {
         ButtonModel model = ((AbstractButton) c).getModel();
         if (model.isPressed() && model.isArmed())
         {
            pressed.paintIcon(c, g, x, y);
         }
         else
         {
            super.paintIcon(c, g, x, y);
         }
      }
   }

   // ComponentOrientation Icon
   // Delegates to different icons based on component orientation
   private static class COIcon
      extends IconUIResource
   {

      private Icon rtl;

      public COIcon(Icon ltr, Icon rtl)
      {
         super(ltr);
         this.rtl = rtl;
      }

      @Override
      public void paintIcon(Component c, Graphics g, int x, int y)
      {
         if (c.getComponentOrientation().isLeftToRight())
         {
            super.paintIcon(c, g, x, y);
         }
         else
         {
            rtl.paintIcon(c, g, x, y);
         }
      }
   }

   public CustomMetalTheme()
   {
      colors = new ColorUIResource[nrOfColors];
      setColor(primary1, new ColorUIResource(super.getPrimary1()));
      setColor(primary2, new ColorUIResource(super.getPrimary2()));
      setColor(primary3, new ColorUIResource(super.getPrimary3()));
      setColor(secondary1, new ColorUIResource(super.getSecondary1()));
      setColor(secondary2, new ColorUIResource(super.getSecondary2()));
      setColor(secondary3, new ColorUIResource(super.getSecondary3()));
      setColor(black, new ColorUIResource(super.getBlack()));
      setColor(white, new ColorUIResource(super.getWhite()));
      setColor(focus, new ColorUIResource(super.getFocusColor()));
      setColor(desktop, new ColorUIResource(super.getDesktopColor()));
      setColor(control, new ColorUIResource(super.getControl()));
      setColor(controlShadow, new ColorUIResource(super.getControlShadow()));
      setColor(controlDarkShadow,
         new ColorUIResource(super.getControlDarkShadow()));
      setColor(controlInfo, new ColorUIResource(super.getControlInfo()));
      setColor(controlHighlight, new ColorUIResource(super.getControlHighlight()));
      setColor(controlDisabled, new ColorUIResource(super.getControlDisabled()));
      setColor(primaryControl, new ColorUIResource(super.getPrimaryControl()));
      setColor(primaryControlShadow,
         new ColorUIResource(super.getPrimaryControlShadow()));
      setColor(primaryControlDarkShadow,
         new ColorUIResource(super.getPrimaryControlDarkShadow()));
      setColor(primaryControlInfo,
         new ColorUIResource(super.getPrimaryControlInfo()));
      setColor(primaryControlHighlight,
         new ColorUIResource(super.getPrimaryControlHighlight()));
      setColor(systemText, new ColorUIResource(super.getSystemTextColor()));
      setColor(controlText, new ColorUIResource(super.getControlTextColor()));
      setColor(inactiveControlText,
         new ColorUIResource(super.getInactiveControlTextColor()));
      setColor(inactiveSystemText,
         new ColorUIResource(super.getInactiveSystemTextColor()));
      setColor(userText, new ColorUIResource(super.getUserTextColor()));
      setColor(textHighlight, new ColorUIResource(super.getTextHighlightColor()));
      setColor(highlightedText,
         new ColorUIResource(super.getHighlightedTextColor()));
      setColor(windowBackground, new ColorUIResource(super.getWindowBackground()));
      setColor(windowTitleBackground,
         new ColorUIResource(super.getWindowTitleBackground()));
      setColor(windowTitleForeground,
         new ColorUIResource(super.getWindowTitleForeground()));
      setColor(windowTitleInactiveBackground,
         new ColorUIResource(super.getWindowTitleInactiveBackground()));
      setColor(windowTitleInactiveForeground,
         new ColorUIResource(super.getWindowTitleInactiveForeground()));
      setColor(menuBackground, new ColorUIResource(super.getMenuBackground()));
      setColor(menuForeground, new ColorUIResource(super.getMenuForeground()));
      setColor(menuSelectedBackground,
         new ColorUIResource(super.getMenuSelectedBackground()));
      setColor(menuSelectedForeground,
         new ColorUIResource(super.getMenuSelectedForeground()));
      setColor(menuDisabledForeground,
         new ColorUIResource(super.getMenuDisabledForeground()));
      setColor(separatorBackground,
         new ColorUIResource(super.getSeparatorBackground()));
      setColor(separatorForeground,
         new ColorUIResource(super.getSeparatorForeground()));
      setColor(acceleratorForeground,
         new ColorUIResource(super.getAcceleratorForeground()));
      setColor(acceleratorSelectedForeground,
         new ColorUIResource(super.getAcceleratorSelectedForeground()));
      fonts = new FontUIResource[nrOfFonts];
      setFont(controlTextFont, super.getControlTextFont());
      setFont(systemTextFont, super.getSystemTextFont());
      setFont(userTextFont, super.getUserTextFont());
      setFont(menuTextFont, super.getMenuTextFont());
      setFont(windowTitleFont, super.getWindowTitleFont());
      setFont(subTextFont, super.getSubTextFont());
      setName(super.getName());
   }

//	/**
//	 * Add this theme's custom entries to the defaults table.
//	 *
//	 * @param table the defaults table, non-null
//	 * @throws NullPointerException if the parameter is null
//	 */
//	@Override
//	public void addCustomEntriesToTable(UIDefaults table)
//	{
//		super.addCustomEntriesToTable(table);
//		if (ColabFactory.isUseOceanTheme())
//		{
////			addColabOceanEntriesToTable(table);
//		}
//	}
   /**
    * Add the colab ocean theme entries to the defaults table.
    *
    * @param table the defaults table, non-null
    * @throws NullPointerException if the parameter is null
    */
   public void addColabOceanEntriesToTable(UIDefaults table)
   {
      Object focusBorder = new UIDefaults.ProxyLazyValue(
         "javax.swing.plaf.BorderUIResource$LineBorderUIResource",
         new Object[]
         {
            getPrimary1()
         });
      Color cccccc = new ColorUIResource(0xCCCCCC);
      Color dadada = new ColorUIResource(0xDADADA);
      Color c8ddf2 = new ColorUIResource(0xC8DDF2);
      c8ddf2 = getPrimary2();
//		Object directoryIcon = ColabFactory.getIcon("icons/ocean/directory.gif");
//		Object fileIcon = ColabFactory.getIcon("icons/ocean/file.gif");
      java.util.List buttonGradient = null;
      java.util.List frameGradient = null;
      java.util.List sliderGradient = null;
      java.util.List menuBarGradient = null;
      buttonGradient = getGradient(.3F, 0F, getDarkerColor(getSecondary3(), .95),
         getSecondary3(),
         getDarkerColor(getSecondary3(), .95));
      sliderGradient = getGradient(.3F, 0F, getPrimary2(), getSecondary3(),
         getPrimary2());
      frameGradient = getGradient(.3F, 0F, getPrimary2(), getSecondary3(),
         getPrimary2());
      menuBarGradient = getGradient(1F, 0F, getWhite(), getPrimary3(),
         getPrimary3());

      table.put("Button.gradient", buttonGradient);
      table.put("Button.rollover", Boolean.TRUE);
      table.put("Button.toolBarBorderBackground", getInactiveControlTextColor());
      table.put("Button.disabledToolBarBorderBackground", cccccc);
      table.put("Button.rolloverIconType", "ocean");

      table.put("CheckBox.rollover", Boolean.TRUE);
      table.put("CheckBox.gradient", buttonGradient);

      table.put("CheckBoxMenuItem.gradient", buttonGradient);

      // home2
      table.put("FileChooser.homeFolderIcon", getIconResource("icons/ocean/homeFolder.gif"));

      // directory2
      table.put("FileChooser.newFolderIcon", getIconResource("icons/ocean/newFolder.gif"));
      // updir2
      table.put("FileChooser.upFolderIcon", getIconResource("icons/ocean/upFolder.gif"));

      // computer2
      table.put("FileView.computerIcon", getIconResource("icons/ocean/computer.gif"));
//		table.put("FileView.directoryIcon", directoryIcon);
      // disk2
      table.put("FileView.hardDriveIcon", getIconResource("icons/ocean/hardDrive.gif"));
//		table.put("FileView.fileIcon", fileIcon);
      // floppy2
      table.put("FileView.floppyDriveIcon", getIconResource("icons/ocean/floppy.gif"));

      table.put("Label.disabledForeground", getInactiveControlTextColor());

      table.put("Menu.opaque", Boolean.FALSE);

      table.put("MenuBar.gradient", menuBarGradient);
      table.put("MenuBar.borderColor", cccccc);

      table.put("InternalFrame.activeTitleGradient", frameGradient);
      // close2
      table.put("InternalFrame.closeIcon",
         new UIDefaults.LazyValue()
         {

         @Override
            public Object createValue(UIDefaults table)
            {
               return new IFIcon(getHastenedIcon("icons/ocean/close.gif", table),
                  getHastenedIcon("icons/ocean/close-pressed.gif", table));
            }
         });
      // minimize
      table.put("InternalFrame.iconifyIcon",
         new UIDefaults.LazyValue()
         {

         @Override
            public Object createValue(UIDefaults table)
            {
               return new IFIcon(getHastenedIcon("icons/ocean/iconify.gif", table),
                  getHastenedIcon("icons/ocean/iconify-pressed.gif", table));
            }
         });
      // restore
      table.put("InternalFrame.minimizeIcon",
         new UIDefaults.LazyValue()
         {

         @Override
            public Object createValue(UIDefaults table)
            {
               return new IFIcon(getHastenedIcon("icons/ocean/minimize.gif", table),
                  getHastenedIcon("icons/ocean/minimize-pressed.gif", table));
            }
         });
      // menubutton3
      table.put("InternalFrame.menuIcon", getIconResource("icons/ocean/menu.gif"));
      // maximize2
      table.put("InternalFrame.maximizeIcon",
         new UIDefaults.LazyValue()
         {

         @Override
            public Object createValue(UIDefaults table)
            {
               return new IFIcon(getHastenedIcon("icons/ocean/maximize.gif", table),
                  getHastenedIcon("icons/ocean/maximize-pressed.gif", table));
            }
         });
      // paletteclose
      table.put("InternalFrame.paletteCloseIcon",
         new UIDefaults.LazyValue()
         {

         @Override
            public Object createValue(UIDefaults table)
            {
               return new IFIcon(getHastenedIcon("icons/ocean/paletteClose.gif", table),
                  getHastenedIcon("icons/ocean/paletteClose-pressed.gif", table));
            }
         });

      table.put("List.focusCellHighlightBorder", focusBorder);

      table.put("MenuBarUI", "javax.swing.plaf.metal.MetalMenuBarUI");

      table.put("OptionPane.errorIcon", getIconResource("icons/ocean/error.png"));
      table.put("OptionPane.informationIcon", getIconResource("icons/ocean/info.png"));
      table.put("OptionPane.questionIcon", getIconResource("icons/ocean/question.png"));
      table.put("OptionPane.warningIcon", getIconResource("icons/ocean/warning.png"));

      table.put("RadioButton.gradient", buttonGradient);
      table.put("RadioButton.rollover", Boolean.TRUE);

      table.put("RadioButtonMenuItem.gradient", buttonGradient);

      table.put("ScrollBar.gradient", buttonGradient);

      table.put("Slider.gradient", sliderGradient);
      table.put("Slider.focusGradient", sliderGradient);
      table.put("Slider.altTrackColor", new ColorUIResource(0xD2E2EF));

      table.put("SplitPane.oneTouchButtonsOpaque", Boolean.FALSE);
      table.put("SplitPane.dividerFocusColor", c8ddf2);

      table.put("TabbedPane.borderHightlightColor", getPrimary1());
      table.put("TabbedPane.contentAreaColor", c8ddf2);
      table.put("TabbedPane.contentBorderInsets", new Insets(4, 2, 3, 3));
      table.put("TabbedPane.selected", c8ddf2);
      table.put("TabbedPane.tabAreaBackground", dadada);
      table.put("TabbedPane.tabAreaInsets", new Insets(2, 2, 0, 6));
      table.put("TabbedPane.unselectedBackground", getSecondary3());

      table.put("Table.focusCellHighlightBorder", focusBorder);
      table.put("Table.gridColor", getSecondary1());

      table.put("ToggleButton.gradient", buttonGradient);

      table.put("ToolBar.borderColor", cccccc);
      table.put("ToolBar.isRollover", Boolean.TRUE);

//		table.put("Tree.closedIcon", directoryIcon);

      table.put("Tree.collapsedIcon",
         new UIDefaults.LazyValue()
         {

         @Override
            public Object createValue(UIDefaults table)
            {
               return new COIcon(getHastenedIcon("icons/ocean/collapsed.gif", table),
                  getHastenedIcon("icons/ocean/collapsed-rtl.gif", table));
            }
         });

      table.put("Tree.expandedIcon", getIconResource("icons/ocean/expanded.gif"));
//		table.put("Tree.leafIcon", fileIcon);
//		table.put("Tree.openIcon", directoryIcon);
      table.put("Tree.selectionBorderColor", getPrimary1());
   }

   /**
    * Creates a List, which specifies a gradient (in the format as expected by the new Metal UI in jdk 1.5)
    *
    * @param ratio1 float
    *   Normally between 0 and 1. Specifies the end of the gradient from color1 to color2.
    * @param ratio2 float
    *   Normally between 0 and 1. Specifies the length of color2, after it the gradient from color2
    *   to color1. If (2*ratio1+ratio2)<1, a new gradient starts from this position from color1 to
    *   color3.
    * @param color1 ColorUIResource
    * @param color2 ColorUIResource
    * @param color3 ColorUIResource
    * @return List
    */
   private List getGradient(float ratio1, float ratio2, ColorUIResource color1,
      ColorUIResource color2, ColorUIResource color3)
   {
      List list = new ArrayList();
      list.add(new Float(ratio1));
      list.add(new Float(ratio2));
      list.add(color1);
      list.add(color2);
      list.add(color3);
      return list;
   }

   private ColorUIResource getDarkerColor(Color color, double factor)
   {
      return new ColorUIResource(Math.max((int) (color.getRed() * factor), 0),
         Math.max((int) (color.getGreen() * factor), 0),
         Math.max((int) (color.getBlue() * factor), 0));
   }

   private ColorUIResource getLighterColor(Color color, double factor)
   {
      return new ColorUIResource(Math.min((int) (color.getRed() * factor), 255),
         Math.min((int) (color.getGreen() * factor), 255),
         Math.min((int) (color.getBlue() * factor), 255));
   }

   private Icon getIconResource(String iconID)
   {
      return getIcon(iconID);
   }

   // makes use of getIconResource() to fetch an icon and then hastens it
   // - calls createValue() on it and returns the actual icon
   private Icon getHastenedIcon(String iconID, UIDefaults table)
   {
      //Object res = getIconResource(iconID);
      //return (Icon) ( (UIDefaults.LazyValue) res).createValue(table);
      Icon icon = getIcon(iconID);
      return icon;
   }

   /** A utility function to get a url to a resource in the program */
   public URL getResource(String name)
   {
      return this.getClass().getResource(resourceDir + name);
   }

   /** A utility function to get a icon from the resources in the program */
   public Icon getIcon(String name)
   {
      URL url = getResource(name);
      if (url != null)
      {
//			System.out.println("getIcon(" + name + "), url " + url.toString());
         return new ImageIcon(url);
      }
      else
      {
         return null;
      }
   }

   public void copyTheme(CustomMetalTheme anOtherTheme)
   {
      for (int i = 0; i < colors.length; i++)
      {
         colors[i] = anOtherTheme.colors[i];
      }
      for (int i = 0; i < fonts.length; i++)
      {
         fonts[i] = anOtherTheme.fonts[i];
      }
      setName(anOtherTheme.getName());
   }

   /**
    * Copies the 8 basic colors (primary, secondary, black and white) to the different items according to the default Metal theme setup.
    */
   public void applyMetalThemeSetup()
   {
      copyColors(primary1ColorMapped, primary1);
      copyColors(primary2ColorMapped, primary2);
      copyColors(primary3ColorMapped, primary3);
      copyColors(secondary1ColorMapped, secondary1);
      copyColors(secondary2ColorMapped, secondary2);
      copyColors(secondary3ColorMapped, secondary3);
      copyColors(blackColorMapped, black);
      copyColors(whiteColorMapped, white);
      setColor(controlText, getColor(controlInfo));
      setColor(inactiveControlText, getColor(controlDisabled));
      setColor(highlightedText, getColor(controlText));
   }

   /**
    * Copies the 8 basic colors (primary, secondary, black and white) to the different items according to the colab theme setup.
    */
   public void applyColabThemeSetup()
   {
      applyMetalThemeSetup();
      setColor(desktop, getColor(primary3));
      setColor(windowTitleBackground, getColor(primary2));
      setColor(textHighlight, getColor(primary2));
      setColor(primaryControl, getColor(primary2));
      setColor(controlShadow, getColor(primary2));
   }

   /**
    * Copies the 8 basic colors (primary, secondary, black and white) to the different items according to the colab theme setup.
    */
   public void applyScyThemeSetup()
   {
      applyMetalThemeSetup();
      setColor(desktop, getColor(secondary1));
      setColor(windowTitleBackground, getColor(primary2));
      setColor(textHighlight, getColor(primary2));
      setColor(primaryControl, getColor(primary2));
      setColor(controlShadow, getColor(primary2));
   }

   private void copyColors(int[] colors, int sourceColor)
   {
      for (int i = 0; i < colors.length; i++)
      {
         setColor(colors[i], getColor(sourceColor));
      }
   }

// these are blue in Metal Default Theme
   @Override
   protected ColorUIResource getPrimary1()
   {
      return colors[primary1];
   }

   @Override
   protected ColorUIResource getPrimary2()
   {
      return colors[primary2];
   }

   @Override
   protected ColorUIResource getPrimary3()
   {
      return colors[primary3];
   }

// these are gray in Metal Default Theme
   @Override
   protected ColorUIResource getSecondary1()
   {
      return colors[secondary1];
   }

   @Override
   protected ColorUIResource getSecondary2()
   {
      return colors[secondary2];
   }

   @Override
   protected ColorUIResource getSecondary3()
   {
      return colors[secondary3];
   }

   @Override
   protected ColorUIResource getBlack()
   {
      return colors[black];
   }

   @Override
   protected ColorUIResource getWhite()
   {
      return colors[white];
   }

   @Override
   public ColorUIResource getFocusColor()
   {
      if (styled)
      {
         return colors[focus];
      }
      else
      {
         return super.getFocusColor();
      }
   }

   @Override
   public ColorUIResource getDesktopColor()
   {
      if (styled)
      {
         return super.getDesktopColor();
      }
      else
      {
         return colors[desktop];
      }
   }

   @Override
   public ColorUIResource getControl()
   {
      if (styled)
      {
         return super.getControl();
      }
      else
      {
         return colors[control];
      }
   }

   @Override
   public ColorUIResource getControlShadow()
   {
      if (styled)
      {
         return super.getControlShadow();
      }
      else
      {
         return colors[controlShadow];
      }
   }

   @Override
   public ColorUIResource getControlDarkShadow()
   {
      if (styled)
      {
         return super.getControlDarkShadow();
      }
      else
      {
         return colors[controlDarkShadow];
      }
   }

   @Override
   public ColorUIResource getControlInfo()
   {
      if (styled)
      {
         return super.getControlInfo();
      }
      else
      {
         return colors[controlInfo];
      }
   }

   @Override
   public ColorUIResource getControlHighlight()
   {
      if (styled)
      {
         return super.getControlHighlight();
      }
      else
      {
         return colors[controlHighlight];
      }
   }

   @Override
   public ColorUIResource getControlDisabled()
   {
      if (styled)
      {
         return super.getControlDisabled();
      }
      else
      {
         return colors[controlDisabled];
      }
   }

   @Override
   public ColorUIResource getPrimaryControl()
   {
      if (styled)
      {
         return super.getPrimaryControl();
      }
      else
      {
         return colors[primaryControl];
      }
   }

   @Override
   public ColorUIResource getPrimaryControlShadow()
   {
      if (styled)
      {
         return super.getPrimaryControlShadow();
      }
      else
      {
         return colors[primaryControlShadow];
      }
   }

   @Override
   public ColorUIResource getPrimaryControlDarkShadow()
   {
      if (styled)
      {
         return super.getPrimaryControlDarkShadow();
      }
      else
      {
         return colors[primaryControlDarkShadow];
      }
   }

   @Override
   public ColorUIResource getPrimaryControlInfo()
   {
      if (styled)
      {
         return super.getPrimaryControlInfo();
      }
      else
      {
         return colors[primaryControlInfo];
      }
   }

   @Override
   public ColorUIResource getPrimaryControlHighlight()
   {
      if (styled)
      {
         return super.getPrimaryControlHighlight();
      }
      else
      {
         return colors[primaryControlHighlight];
      }
   }

   /**
    * Returns the color used, by default, for the text in labels
    * and titled borders.
    */
   @Override
   public ColorUIResource getSystemTextColor()
   {
      if (styled)
      {
         return super.getSystemTextColor();
      }
      else
      {
         return colors[systemText];
      }
   }

   @Override
   public ColorUIResource getControlTextColor()
   {
      if (styled)
      {
         return super.getControlTextColor();
      }
      else
      {
         return colors[controlText];
      }
   }

   @Override
   public ColorUIResource getInactiveControlTextColor()
   {
      if (styled)
      {
         return super.getInactiveControlTextColor();
      }
      else
      {
         return colors[inactiveControlText];
      }
   }

   @Override
   public ColorUIResource getInactiveSystemTextColor()
   {
      if (styled)
      {
         return super.getInactiveSystemTextColor();
      }
      else
      {
         return colors[inactiveSystemText];
      }
   }

   @Override
   public ColorUIResource getUserTextColor()
   {
      if (styled)
      {
         return super.getUserTextColor();
      }
      else
      {
         return colors[userText];
      }
   }

   @Override
   public ColorUIResource getTextHighlightColor()
   {
      if (styled)
      {
         return super.getTextHighlightColor();
      }
      else
      {
         return colors[textHighlight];
      }
   }

   @Override
   public ColorUIResource getHighlightedTextColor()
   {
      if (styled)
      {
         return super.getHighlightedTextColor();
      }
      else
      {
         return colors[highlightedText];
      }
   }

   @Override
   public ColorUIResource getWindowBackground()
   {
      if (styled)
      {
         return super.getWindowBackground();
      }
      else
      {
         return colors[windowBackground];
      }
   }

   @Override
   public ColorUIResource getWindowTitleBackground()
   {
      if (styled)
      {
         return super.getWindowTitleBackground();
      }
      else
      {
         return colors[windowTitleBackground];
      }
   }

   @Override
   public ColorUIResource getWindowTitleForeground()
   {
      if (styled)
      {
         return super.getWindowTitleForeground();
      }
      else
      {
         return colors[windowTitleForeground];
      }
   }

   @Override
   public ColorUIResource getWindowTitleInactiveBackground()
   {
      if (styled)
      {
         return super.getWindowTitleInactiveBackground();
      }
      else
      {
         return colors[windowTitleInactiveBackground];
      }
   }

   @Override
   public ColorUIResource getWindowTitleInactiveForeground()
   {
      if (styled)
      {
         return super.getWindowTitleInactiveForeground();
      }
      else
      {
         return colors[windowTitleInactiveForeground];
      }
   }

   @Override
   public ColorUIResource getMenuBackground()
   {
      if (styled)
      {
         return super.getMenuBackground();
      }
      else
      {
         return colors[menuBackground];
      }
   }

   @Override
   public ColorUIResource getMenuForeground()
   {
      if (styled)
      {
         return super.getMenuForeground();
      }
      else
      {
         return colors[menuForeground];
      }
   }

   @Override
   public ColorUIResource getMenuSelectedBackground()
   {
      if (styled)
      {
         return super.getMenuSelectedBackground();
      }
      else
      {
         return colors[menuSelectedBackground];
      }
   }

   @Override
   public ColorUIResource getMenuSelectedForeground()
   {
      if (styled)
      {
         return super.getMenuSelectedForeground();
      }
      else
      {
         return colors[menuSelectedForeground];
      }
   }

   @Override
   public ColorUIResource getMenuDisabledForeground()
   {
      if (styled)
      {
         return super.getMenuDisabledForeground();
      }
      else
      {
         return colors[menuDisabledForeground];
      }
   }

   @Override
   public ColorUIResource getSeparatorBackground()
   {
      if (styled)
      {
         return super.getSeparatorBackground();
      }
      else
      {
         return colors[separatorBackground];
      }
   }

   @Override
   public ColorUIResource getSeparatorForeground()
   {
      if (styled)
      {
         return super.getSeparatorForeground();
      }
      else
      {
         return colors[separatorForeground];
      }
   }

   @Override
   public ColorUIResource getAcceleratorForeground()
   {
      if (styled)
      {
         return super.getAcceleratorForeground();
      }
      else
      {
         return colors[acceleratorForeground];
      }
   }

   @Override
   public ColorUIResource getAcceleratorSelectedForeground()
   {
      if (styled)
      {
         return super.getAcceleratorSelectedForeground();
      }
      else
      {
         return colors[acceleratorSelectedForeground];
      }
   }

   public void setColor(int index, ColorUIResource color)
   {
      colors[index] = color;
   }

   public void setColor(int index, Color color)
   {
      colors[index] = color2ColorUIResource(color);
   }

   public ColorUIResource getColor(int index)
   {
      return colors[index];
   }

   public static String getColorName(int index)
   {
      return colorNames[index];
   }

   /**
    * Creates a ColorUIResource from a Color. It uses a TransparentColorUIResource, if the color is transparent.
    */
   public ColorUIResource color2ColorUIResource(Color color)
   {
      if (color.getAlpha() < 255)
      {
         return new TransparentColorUIResource(color);
      }
      else
      {
         return new ColorUIResource(color);
      }
   }

   public boolean isStyled()
   {
      return styled;
   }

   public void setStyled(boolean styled)
   {
      this.styled = styled;
   }

   @Override
   public FontUIResource getControlTextFont()
   {
      return fonts[controlTextFont];
   }

   @Override
   public FontUIResource getSystemTextFont()
   {
      return fonts[systemTextFont];
   }

   @Override
   public FontUIResource getUserTextFont()
   {
      return fonts[userTextFont];
   }

   @Override
   public FontUIResource getMenuTextFont()
   {
      return fonts[menuTextFont];
   }

   @Override
   public FontUIResource getWindowTitleFont()
   {
      return fonts[windowTitleFont];
   }

   @Override
   public FontUIResource getSubTextFont()
   {
      return fonts[subTextFont];
   }

   public void setFont(int index, FontUIResource font)
   {
      fonts[index] = font;
   }

   public void setFont(int index, Font font)
   {
      fonts[index] = new FontUIResource(font);
   }

   public FontUIResource getFont(int index)
   {
      return fonts[index];
   }

   public static String getFontName(int index)
   {
      return fontNames[index];
   }

   @Override
   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
   }

   public String getFileExtension()
   {
      return fileExtension;
   }
}
