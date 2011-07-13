package colab.vt.whiteboard.component;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.MediaSizeName;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.ToolTipManager;
import javax.swing.WindowConstants;

import org.jdom.Element;

import colab.vt.whiteboard.component.events.WhiteboardContainerChangedListener;
import colab.vt.whiteboard.component.events.WhiteboardContainerListChangedEvent;
import colab.vt.whiteboard.component.events.WhiteboardContainerListChangedListener;
import colab.vt.whiteboard.component.state.FillColorSelector;
import colab.vt.whiteboard.component.state.LineColorSelector;
import colab.vt.whiteboard.component.state.PenSizeSelector;
import colab.vt.whiteboard.utils.XmlUtils;
import javax.swing.SwingUtilities;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI Builder, which is free
 * for non-commercial use. If Jigloo is being used commercially (ie, by a corporation, company or
 * business for any purpose whatever) then you should purchase a license for each developer using
 * Jigloo. Please visit www.cloudgarden.com for details. Use of Jigloo implies acceptance of these
 * licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS
 * CODE CANNOT BE USED LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class WhiteboardPanel extends javax.swing.JPanel implements MouseMotionListener,
   MouseListener, MouseWheelListener, KeyListener, Printable
{

   private static final long serialVersionUID = -7521977041993296134L;
   // @SuppressWarnings("unused")
   private static final Logger logger = Logger.getLogger(WhiteboardPanel.class.getName());
   public static final double minimumScale = -10;
   public static final double maximumScale = 10;
   public static final double minimumPenSize = 0.1;
   public static final double maximumPenSize = 50;
   private static final int stateSize = 23;
   private boolean printEvents = false;
   private boolean showGrid = false;
   private boolean snapToGrid = false;
   private int mainGridSize = 20;
   private int nrOfSubGridLines = 1;
   private Paint mainGridPaint = new Color(176, 176, 255);
   private Paint subGridPaint = new Color(224, 224, 255);
   private double currentPenSize = 1.0;
   private Color currentLineColor = Color.black;
   private Color currentFillColor = new Color(255, 255, 255, 0);
   private JToolBar toolbar;
   private HashMap<WhiteboardAction, JToggleButton> toolbarButtons = new HashMap<WhiteboardAction, JToggleButton>();
   private ArrayList<WhiteboardAction> whiteboardActions = new ArrayList<WhiteboardAction>();
   private WhiteboardAction currentWhiteboardAction = null;
   private WhiteboardContainer temporaryWhiteboardContainer = null;
   private ArrayList<WhiteboardContainer> whiteboardContainerList = new ArrayList<WhiteboardContainer>();
   private HashMap<String, WhiteboardContainer> whiteboardContainerHashMap = new HashMap<String, WhiteboardContainer>();
   private CopyOnWriteArrayList<WhiteboardContainerChangedListener> whiteboardContainerChangedListeners = new CopyOnWriteArrayList<WhiteboardContainerChangedListener>();
   private CopyOnWriteArrayList<WhiteboardContainerListChangedListener> whiteboardContainerListChangedListeners = new CopyOnWriteArrayList<WhiteboardContainerListChangedListener>();
   private long paintCounter = 0;
   private static WhiteboardPanel whiteBoardPanel;

   /**
    * Auto-generated main method to display this JPanel inside a new JFrame.
    */
   public static void main(String[] args)
   {
      JFrame frame = new JFrame();
      whiteBoardPanel = new WhiteboardPanel();
      frame.getContentPane().add(whiteBoardPanel);
      frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
      frame.addWindowListener(new WindowListener()
      {

         @Override
         public void windowActivated(WindowEvent e)
         {
         }

         @Override
         public void windowClosed(WindowEvent e)
         {
         }

         @Override
         public void windowClosing(WindowEvent e)
         {
            whiteBoardPanel.dispose();
         }

         @Override
         public void windowDeactivated(WindowEvent e)
         {
         }

         @Override
         public void windowDeiconified(WindowEvent e)
         {
         }

         @Override
         public void windowIconified(WindowEvent e)
         {
         }

         @Override
         public void windowOpened(WindowEvent e)
         {
         }
      });
      frame.setTitle("Test of WhiteBoardPanel");
      frame.pack();
      frame.setVisible(true);
   }

   public WhiteboardPanel()
   {
      super();
      initGUI();
      setForeground(Color.black);
      setBackground(Color.white);
      // this.setDoubleBuffered(true);
      addActions();
   }

   private void addActions()
   {
      WhiteboardAction selectionAction = addWhiteboardAction(new SelectionAction(this, "Sel"));
      addWhiteboardAction(new DeleteAction2(this, "Delete"));
      addWhiteboardAction(new LineAction(this, "Line"));
      addWhiteboardAction(new RectangleAction(this, "Rect"));
      addWhiteboardAction(new OvalAction(this, "Oval"));
      addWhiteboardAction(new FreehandAction(this, "Free"));
      addWhiteboardAction(new SimpleTextAction(this, "Text"));
      addWhiteboardAction(new InsertImageAction(this, "Insert image"));
      addWhiteboardAction(new TagPointerAction(this, "Tag"));
      addWhiteboardAction(new InfoAction(this, "Info"));
      addWhiteboardAction(new PenSizeSelector(this, stateSize));
      addWhiteboardAction(new LineColorSelector(this, stateSize));
      addWhiteboardAction(new FillColorSelector(this, stateSize));
      // addWhiteboardAction(new AlphaCompositeSelector(this, stateSize));
      setCurrentAction(selectionAction);
   }

   private void initGUI()
   {
      try
      {
         setPreferredSize(new Dimension(400, 300));
         this.setLayout(null);
         {
            toolbar = new JToolBar(JToolBar.HORIZONTAL);
            toolbar.setFloatable(false);
            this.add(toolbar);
            toolbar.setBounds(0, 0, 5000, 30);
         }
         this.addMouseListener(this);
         this.addMouseMotionListener(this);
         this.addMouseWheelListener(this);
         this.addKeyListener(this);
         this.setFocusable(true);
         ToolTipManager.sharedInstance().registerComponent(this);
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
   }

   public void addWhiteboardContainerListChangedListener(
      WhiteboardContainerListChangedListener whiteboardObjectContainerListChangedListener)
   {
      if (!whiteboardContainerListChangedListeners.contains(whiteboardObjectContainerListChangedListener))
      {
         whiteboardContainerListChangedListeners.add(whiteboardObjectContainerListChangedListener);
      }
   }

   public void removeWhiteboardContainerListChangedListener(
      WhiteboardContainerListChangedListener whiteboardObjectContainerListChangedListener)
   {
      if (whiteboardContainerListChangedListeners.contains(whiteboardObjectContainerListChangedListener))
      {
         whiteboardContainerListChangedListeners.remove(whiteboardObjectContainerListChangedListener);
      }
   }

   public void addWhiteboardContainerChangedListener(
      WhiteboardContainerChangedListener whiteboardObjectContainerChangedListener)
   {
      if (!whiteboardContainerChangedListeners.contains(whiteboardObjectContainerChangedListener))
      {
         whiteboardContainerChangedListeners.add(whiteboardObjectContainerChangedListener);
         for (WhiteboardContainer whiteboardContainer : whiteboardContainerList)
         {
            whiteboardContainer.addWhiteboardContainerChangedListener(whiteboardObjectContainerChangedListener);
         }
      }
   }

   public void removeWhiteboardContainerChangedListener(
      WhiteboardContainerChangedListener whiteboardObjectContainerChangedListener)
   {
      if (whiteboardContainerChangedListeners.contains(whiteboardObjectContainerChangedListener))
      {
         whiteboardContainerChangedListeners.remove(whiteboardObjectContainerChangedListener);
         for (WhiteboardContainer whiteboardContainer : whiteboardContainerList)
         {
            whiteboardContainer.removeWhiteboardContainerChangedListener(whiteboardObjectContainerChangedListener);
         }
      }
   }

   public void dispose()
   {
      for (WhiteboardContainer whiteboardContainer : whiteboardContainerList)
      {
         whiteboardContainer.dispose();
      }
   }

   public boolean isEmpty()
   {
      return whiteboardContainerList.isEmpty();
   }

   @Override
   public String getToolTipText(MouseEvent event)
   {
      return currentWhiteboardAction.getToolTipText(event);
   }

   @Override
   public void mouseDragged(MouseEvent e)
   {
      printMouseEvent("mouseDragged", e);
      if (currentWhiteboardAction instanceof MouseMotionListener)
      {
         ((MouseMotionListener) currentWhiteboardAction).mouseDragged(e);
      }
   }

   @Override
   public void mouseMoved(MouseEvent e)
   {
      // printMouseEvent("mouseMoved",e);
      if (currentWhiteboardAction instanceof MouseMotionListener)
      {
         ((MouseMotionListener) currentWhiteboardAction).mouseMoved(e);
      }
   }

   @Override
   public void mouseClicked(MouseEvent e)
   {
      printMouseEvent("mouseClicked", e);
      if (currentWhiteboardAction instanceof MouseListener)
      {
         ((MouseListener) currentWhiteboardAction).mouseClicked(e);
      }
   }

   @Override
   public void mouseEntered(MouseEvent e)
   {
      printMouseEvent("mouseEntered", e);
      if (currentWhiteboardAction instanceof MouseListener)
      {
         ((MouseListener) currentWhiteboardAction).mouseEntered(e);
      }
   }

   @Override
   public void mouseExited(MouseEvent e)
   {
      printMouseEvent("mouseExited", e);
      if (currentWhiteboardAction instanceof MouseListener)
      {
         ((MouseListener) currentWhiteboardAction).mouseExited(e);
      }
   }

   @Override
   public void mousePressed(MouseEvent e)
   {
      printMouseEvent("mousePressed", e);
      if (currentWhiteboardAction instanceof MouseListener)
      {
         ((MouseListener) currentWhiteboardAction).mousePressed(e);
      }
   }

   @Override
   public void mouseReleased(MouseEvent e)
   {
      printMouseEvent("mouseReleased", e);
      if (currentWhiteboardAction instanceof MouseListener)
      {
         ((MouseListener) currentWhiteboardAction).mouseReleased(e);
      }
   }

   @Override
   public void mouseWheelMoved(MouseWheelEvent e)
   {
      // if (printEvents)
      // System.out.println("mouseWheelMoved: " + e);
      if (currentWhiteboardAction instanceof MouseWheelListener)
      {
         ((MouseWheelListener) currentWhiteboardAction).mouseWheelMoved(e);
      }
   }

   private void printMouseEvent(String label, MouseEvent mouseEvent)
   {
      // if (printEvents)
      // System.out.println(label + ": " + mouseEvent);
   }

   @Override
   public void keyPressed(KeyEvent e)
   {
      printKeyEvent("keyPressed", e);
      if (currentWhiteboardAction instanceof KeyListener)
      {
         ((KeyListener) currentWhiteboardAction).keyPressed(e);
      }
   }

   @Override
   public void keyReleased(KeyEvent e)
   {
      printKeyEvent("keyReleased", e);
      if (currentWhiteboardAction instanceof KeyListener)
      {
         ((KeyListener) currentWhiteboardAction).keyReleased(e);
      }
   }

   @Override
   public void keyTyped(KeyEvent e)
   {
      printKeyEvent("keyTyped", e);
      if (currentWhiteboardAction instanceof KeyListener)
      {
         ((KeyListener) currentWhiteboardAction).keyTyped(e);
      }
   }

   private void printKeyEvent(String label, KeyEvent keyEvent)
   {
      // if (printEvents)
      // System.out.println(label + ": " + keyEvent);
   }

   protected void deleteWhiteboardActions()
   {
      removeCurrentAction();
      toolbar.removeAll();
      whiteboardActions.clear();
      toolbarButtons.clear();
   }

   protected WhiteboardAction addWhiteboardAction(WhiteboardAction whiteboardAction)
   {
      whiteboardActions.add(whiteboardAction);
      JToggleButton toggleButton = whiteboardAction.getToggleButton();
      if (toggleButton != null)
      {
         toggleButton.setText("");
         toolbar.add(toggleButton);
         toolbarButtons.put(whiteboardAction, toggleButton);
      }
      else
      {
         toolbar.add(whiteboardAction.getToolbarButton());
      }
      return whiteboardAction;
   }

   protected void setCurrentAction(WhiteboardAction whiteboardAction)
   {
      if (currentWhiteboardAction != whiteboardAction)
      {
         removeCurrentAction();
         currentWhiteboardAction = whiteboardAction;
         // if (currentWhiteboardAction instanceof MouseMotionListener)
         // addMouseMotionListener((MouseMotionListener)currentWhiteboardAction);
         // if (currentWhiteboardAction instanceof MouseListener)
         // addMouseListener((MouseListener)currentWhiteboardAction);
         // if (currentWhiteboardAction instanceof MouseWheelListener)
         // addMouseWheelListener((MouseWheelListener)currentWhiteboardAction);
         Cursor defaultCursor = currentWhiteboardAction.getDefaultCursor();
         if (defaultCursor == null)
         {
            defaultCursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
         }
         setCursor(defaultCursor);
         JToggleButton toggleButton = toolbarButtons.get(currentWhiteboardAction);
         if (toggleButton != null)
         {
            toggleButton.setSelected(true);
         }
      }
      else if (currentWhiteboardAction != null)
      {
         JToggleButton toggleButton = toolbarButtons.get(currentWhiteboardAction);
         if (toggleButton != null)
         {
            toggleButton.setSelected(true);
         }
      }
      if (currentWhiteboardAction != null)
      {
         this.requestFocusInWindow();
      }
   }

   private void removeCurrentAction()
   {
      if (currentWhiteboardAction != null)
      {
         // if (currentWhiteboardAction instanceof MouseMotionListener)
         // this.removeMouseMotionListener((MouseMotionListener) currentWhiteboardAction);
         // if (currentWhiteboardAction instanceof MouseListener)
         // this.removeMouseListener((MouseListener) currentWhiteboardAction);
         // if (currentWhiteboardAction instanceof MouseWheelListener)
         // this.removeMouseWheelListener((MouseWheelListener) currentWhiteboardAction);
         JToggleButton toggleButton = toolbarButtons.get(currentWhiteboardAction);
         if (toggleButton != null)
         {
            toggleButton.setSelected(false);
         }
         // toolbarButtons.get(currentWhiteboardAction).setSelected(false);
      }
      currentWhiteboardAction = null;
   }

   public void setTemporaryWhiteboardContainer(WhiteboardContainer whiteboardContainer)
   {
      if (temporaryWhiteboardContainer != null)
      {
         logger.severe("setting new temporaryWhiteboardContainer, while still an old one is set");
      }
      temporaryWhiteboardContainer = whiteboardContainer;
      setDefaults(whiteboardContainer);
      WhiteboardContainerListChangedEvent whiteboardContainerListChangedEvent = new WhiteboardContainerListChangedEvent(
         this, this, temporaryWhiteboardContainer, null);
      for (WhiteboardContainerListChangedListener whiteboardContainerListChangedListener : whiteboardContainerListChangedListeners)
      {
         whiteboardContainerListChangedListener.whiteboardContainerAdded(whiteboardContainerListChangedEvent);
      }
      for (WhiteboardContainerChangedListener whiteboardContainerChangedListener : whiteboardContainerChangedListeners)
      {
         whiteboardContainer.addWhiteboardContainerChangedListener(whiteboardContainerChangedListener);
      }
   }

   public void setDefaults(WhiteboardContainer whiteboardContainer)
   {
      whiteboardContainer.setPenSize(currentPenSize);
      whiteboardContainer.setLineColor(currentLineColor);
      whiteboardContainer.setFillColor(currentFillColor);
   }

   public void makeTemporaryWhiteboardContainerFinal(WhiteboardContainer whiteboardContainer,
      boolean makeFinal)
   {
      if (temporaryWhiteboardContainer == null)
      {
         logger.severe("temporaryWhiteboardContainer is not set");
         return;
      }
      if (temporaryWhiteboardContainer != whiteboardContainer)
      {
         logger.severe("temporaryWhiteboardContainer is different from the supplied one");
      }
      if (makeFinal)
      {
         whiteboardContainerList.add(temporaryWhiteboardContainer);
         whiteboardContainerHashMap.put(temporaryWhiteboardContainer.getId(),
            temporaryWhiteboardContainer);
         whiteboardContainer.sendWhiteboardContainerChangedEvent();
      }
      else
      {
         WhiteboardContainerListChangedEvent whiteboardContainerListChangedEvent = new WhiteboardContainerListChangedEvent(
            this, this, temporaryWhiteboardContainer, null);
         for (WhiteboardContainerListChangedListener whiteboardContainerListChangedListener : whiteboardContainerListChangedListeners)
         {
            whiteboardContainerListChangedListener.whiteboardContainerDeleted(whiteboardContainerListChangedEvent);
         }
         for (WhiteboardContainerChangedListener whiteboardContainerChangedListener : whiteboardContainerChangedListeners)
         {
            whiteboardContainer.removeWhiteboardContainerChangedListener(whiteboardContainerChangedListener);
         }
      }
      temporaryWhiteboardContainer = null;
   }

   public void deleteWhiteboardContainer(WhiteboardContainer whiteboardContainer)
   {
      whiteboardContainerList.remove(whiteboardContainer);
      whiteboardContainerHashMap.remove(whiteboardContainer.getId());
      whiteboardContainer.dispose();
      whiteboardContainer.repaint();
      WhiteboardContainerListChangedEvent whiteboardContainerListChangedEvent = new WhiteboardContainerListChangedEvent(
         this, this, whiteboardContainer, null);
      for (WhiteboardContainerListChangedListener whiteboardContainerListChangedListener : whiteboardContainerListChangedListeners)
      {
         whiteboardContainerListChangedListener.whiteboardContainerDeleted(whiteboardContainerListChangedEvent);
      }
   }

   public void deleteAllWhiteboardContainers()
   {
      ArrayList<WhiteboardContainer> whiteboardContainerListCopy = new ArrayList<WhiteboardContainer>(
         whiteboardContainerList);
      for (WhiteboardContainer whiteboardContainer : whiteboardContainerListCopy)
      {
         deleteWhiteboardContainer(whiteboardContainer);
      }
      WhiteboardContainerListChangedEvent whiteboardContainerListChangedEvent = new WhiteboardContainerListChangedEvent(
         this, this, null, null);
      for (WhiteboardContainerListChangedListener whiteboardContainerListChangedListener : whiteboardContainerListChangedListeners)
      {
         whiteboardContainerListChangedListener.whiteboardContainersCleared(whiteboardContainerListChangedEvent);
      }
   }

   public List<WhiteboardContainer> getSelectedWhiteboardContainers()
   {
      return getSelectedWhiteboardContainers(false);
      // ArrayList<WhiteboardContainer> selectedWhiteboardContainer = new
      // ArrayList<WhiteboardContainer>();
      // for (WhiteboardContainer whiteboardContainer : whiteboardContainerList)
      // if (whiteboardContainer.isSelected())
      // selectedWhiteboardContainer.add(whiteboardContainer);
      //
      // return selectedWhiteboardContainer;
   }

   public List<WhiteboardContainer> getSelectedWhiteboardContainers(boolean addLocked)
   {
      ArrayList<WhiteboardContainer> selectedWhiteboardContainer = new ArrayList<WhiteboardContainer>();
      for (WhiteboardContainer whiteboardContainer : whiteboardContainerList)
      {
         if (whiteboardContainer.isSelected())
         {
            if (whiteboardContainer.isLocked())
            {
               if (addLocked)
               {
                  selectedWhiteboardContainer.add(whiteboardContainer);
               }
            }
            else
            {
               selectedWhiteboardContainer.add(whiteboardContainer);
            }
         }
      }

      return selectedWhiteboardContainer;
   }

   @Override
   protected void paintComponent(Graphics g)
   {
      ++paintCounter;
//      // System.out.println("WhiteboardPanel.paintComponent " + paintCounter);
      super.paintComponent(g);
      Graphics2D g2d = (Graphics2D) g;
      g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      if (showGrid)
      {
         smartDrawGrid(g2d);
      }
      // testDrawGrid((Graphics2D) g);
      for (WhiteboardContainer whiteboardContainer : whiteboardContainerList)
      {
         whiteboardContainer.paint(g2d);
      }
      if (temporaryWhiteboardContainer != null)
      {
         temporaryWhiteboardContainer.paint(g2d);
      }
   }

   @SuppressWarnings("unused")
   private void testDrawGrid(Graphics2D g)
   {
      int nrOfLines = 0;
      int nrOfSmartLines = 0;
      final int nrOfTimes = 10000;
      long startMillis = System.currentTimeMillis();
      for (int i = 0; i < nrOfTimes; i++)
      {
         nrOfLines = drawGrid(g);
      }
      long usedMillis = System.currentTimeMillis() - startMillis;
      double drawTime = 1.0 * usedMillis / nrOfTimes;
      startMillis = System.currentTimeMillis();
      for (int i = 0; i < nrOfTimes; i++)
      {
         nrOfSmartLines = smartDrawGrid(g);
      }
      usedMillis = System.currentTimeMillis() - startMillis;
      double smartDrawTime = 1.0 * usedMillis / nrOfTimes;
      logger.info("grid draw time = " + drawTime + " (" + nrOfLines + "), smart draw time = "
         + smartDrawTime + " (" + nrOfSmartLines + ")");
   }

   private int drawGrid(Graphics2D g)
   {
      int nrOfLinesDrawn = 0;
      int height = getHeight();
      int width = getWidth();
      Paint oldPaint = g.getPaint();
      // draw subGrid
      if (nrOfSubGridLines > 0)
      {
         int subGridSize = mainGridSize / (nrOfSubGridLines + 1);
         g.setPaint(subGridPaint);
         int x = 0;
         while (x < width)
         {
            for (int i = 1; i <= nrOfSubGridLines; i++)
            {
               g.drawLine(x + i * subGridSize, 0, x + i * subGridSize, height);
               ++nrOfLinesDrawn;
            }
            x += mainGridSize;
         }
         int y = 0;
         while (y < height)
         {
            for (int i = 1; i <= nrOfSubGridLines; i++)
            {
               g.drawLine(0, y + i * subGridSize, width, y + i * subGridSize);
               ++nrOfLinesDrawn;
            }
            y += mainGridSize;
         }
      }
      // draw mainGrid
      g.setPaint(mainGridPaint);
      int x = 0;
      while (x < width)
      {
         g.drawLine(x, 0, x, height);
         ++nrOfLinesDrawn;
         x += mainGridSize;
      }
      int y = 0;
      while (y < height)
      {
         g.drawLine(0, y, width, y);
         ++nrOfLinesDrawn;
         y += mainGridSize;
      }
      g.setPaint(oldPaint);
      return nrOfLinesDrawn;
   }

   private int smartDrawGrid(Graphics2D g)
   {
      int nrOfLinesDrawn = 0;
      Rectangle clipBounds = g.getClipBounds();
      int height = getHeight();
      int width = getWidth();
      height = Math.min(height, clipBounds.y + clipBounds.height);
      width = Math.min(width, clipBounds.x + clipBounds.width);
      int minX = clipBounds.x;
      int minY = clipBounds.y;
      Paint oldPaint = g.getPaint();
      // draw subGrid
      if (nrOfSubGridLines > 0)
      {
         int subGridSize = mainGridSize / (nrOfSubGridLines + 1);
         g.setPaint(subGridPaint);
         int x = 0;
         while (x < width)
         {
            for (int i = 1; i <= nrOfSubGridLines; i++)
            {
               int xDraw = x + i * subGridSize;
               if (xDraw >= minX)
               {
                  g.drawLine(xDraw, minY, xDraw, height);
                  ++nrOfLinesDrawn;
               }
            }
            x += mainGridSize;
         }
         int y = 0;
         while (y < height)
         {
            for (int i = 1; i <= nrOfSubGridLines; i++)
            {
               int yDraw = y + i * subGridSize;
               if (yDraw >= minY)
               {
                  g.drawLine(minX, yDraw, width, yDraw);
                  ++nrOfLinesDrawn;
               }
            }
            y += mainGridSize;
         }
      }
      // draw mainGrid
      g.setPaint(mainGridPaint);
      int x = 0;
      while (x < width)
      {
         if (x >= minX)
         {
            g.drawLine(x, minY, x, height);
            ++nrOfLinesDrawn;
         }
         x += mainGridSize;
      }
      int y = 0;
      while (y < height)
      {
         if (y >= minY)
         {
            g.drawLine(minX, y, width, y);
            ++nrOfLinesDrawn;
         }
         y += mainGridSize;
      }
      g.setPaint(oldPaint);
      return nrOfLinesDrawn;
   }

   protected WhiteboardContainer getWhiteboardContainerUnderMouse(int x, int y)
   {
      for (int i = whiteboardContainerList.size() - 1; i >= 0; i--)
      {
         if (whiteboardContainerList.get(i).isContentUnderMouse(x, y))
         {
            return whiteboardContainerList.get(i);
         }
      }
      return null;
   }

   public ArrayList<WhiteboardContainer> getWhiteboardContainers()
   {
      return whiteboardContainerList;
   }

   public WhiteboardContainer getWhiteboardContainer(String key)
   {
      return whiteboardContainerHashMap.get(key);
   }

   public void deselectAllWhiteboardContainers()
   {
      for (WhiteboardContainer whiteboardContainer : getWhiteboardContainers())
      {
         if (whiteboardContainer.isSelected())
         {
            whiteboardContainer.repaint();
            whiteboardContainer.setSelected(false);
            whiteboardContainer.repaint();
         }
      }
   }

   public Element getStatus()
   {
      Element status = new Element(XmlNames.whiteboard);
      status.addContent(getWhiteboardSettingStatus());
      status.addContent(getWhiteboardToolStatus());
      status.addContent(getWhiteboardContainerStatus());
      return status;
   }

   public Element getContentStatus()
   {
      Element status = new Element(XmlNames.whiteboard);
      status.addContent(getWhiteboardContainerStatus());
      return status;
   }

   private Element getWhiteboardSettingStatus()
   {
      Element settings = new Element(XmlNames.settings);
      XmlUtils.addXmlTag(settings, XmlNames.penSize, currentPenSize);
      XmlUtils.addXmlTag(settings, XmlNames.lineColor, currentLineColor);
      XmlUtils.addXmlTag(settings, XmlNames.fillColor, currentFillColor);
      XmlUtils.addXmlTag(settings, XmlNames.showGrid, showGrid);
      XmlUtils.addXmlTag(settings, XmlNames.snapToGrid, snapToGrid);
      XmlUtils.addXmlTag(settings, XmlNames.mainGridSize, mainGridSize);
      XmlUtils.addXmlTag(settings, XmlNames.nrOfSubGridLines, nrOfSubGridLines);
      return settings;
   }

   private Element getWhiteboardToolStatus()
   {
      Element tools = new Element(XmlNames.tools);
      for (WhiteboardAction whiteboardAction : whiteboardActions)
      {
         tools.addContent(whiteboardAction.getStatus());
      }
      return tools;
   }

   private Element getWhiteboardContainerStatus()
   {
      Element whiteboardContainers = new Element(XmlNames.whiteboardContainers);
      for (WhiteboardContainer whiteboardContainer : whiteboardContainerList)
      {
         whiteboardContainers.addContent(whiteboardContainer.getStatus());
      }
      return whiteboardContainers;
   }

   public void setStatus(Element status)
   {
      setWhiteboardSettingsStatus(status.getChild(XmlNames.settings));
      setWhiteboardToolStatus(status.getChild(XmlNames.tools));
      setWhiteboardContainerStatus(status.getChild(XmlNames.whiteboardContainers));
      WhiteboardContainerListChangedEvent whiteboardContainerListChangedEvent = new WhiteboardContainerListChangedEvent(
         this, this, null, status);
      for (WhiteboardContainerListChangedListener whiteboardContainerListChangedListener : whiteboardContainerListChangedListeners)
      {
         whiteboardContainerListChangedListener.whiteboardPanelLoaded(whiteboardContainerListChangedEvent);
      }
   }

   private final static int maxSetContentStatusTryCount = 1000;

   public void setContentStatus(Element status)
   {
      setContentStatusLater(status, maxSetContentStatusTryCount);
   }

   /**
    * In SCY the panel seems to be added a bit later to the screen,
    * so that the content is set before it is added to the screen.
    * Delay the setting of the content until we have a graphics environment
   */
   private void setContentStatusLater(final Element status, final int count)
   {
      SwingUtilities.invokeLater(new Runnable()
      {

         @Override
         public void run()
         {
            if (getGraphics() != null || count < 0)
            {
               if (getGraphics() == null){
                  logger.severe("trying to set status while graphics is still null");
               }
               System.out.println("now setting content after "+ (maxSetContentStatusTryCount-count) + " tries");
               realSetContentStatus(status);
            }
            else
            {
               Runnable sleeper = new Runnable(){

                  @Override
                  public void run()
                  {
                     try
                     {
                        Thread.sleep(10);
                     }
                     catch (InterruptedException ex)
                     {
                        logger.log(Level.WARNING, "unexpected exception during sleeping", ex);
                     }
                     setContentStatusLater(status, count - 1);
                  }
               };
               new Thread(sleeper).start();
            }
         }
      });
   }

   private void realSetContentStatus(Element status)
   {
      setWhiteboardContainerStatus(status.getChild(XmlNames.whiteboardContainers));
      WhiteboardContainerListChangedEvent whiteboardContainerListChangedEvent = new WhiteboardContainerListChangedEvent(
         this, this, null, status);
      for (WhiteboardContainerListChangedListener whiteboardContainerListChangedListener : whiteboardContainerListChangedListeners)
      {
         whiteboardContainerListChangedListener.whiteboardContainersLoaded(whiteboardContainerListChangedEvent);
      }
   }

   private void setWhiteboardSettingsStatus(Element settings)
   {
      if (settings != null)
      {
         currentPenSize = XmlUtils.getDoubleValueFromXmlTag(settings, XmlNames.penSize);
         currentLineColor = XmlUtils.getColorValueFromXmlTag(settings, XmlNames.lineColor);
         currentFillColor = XmlUtils.getColorValueFromXmlTag(settings, XmlNames.fillColor);
         showGrid = XmlUtils.getBooleanValueFromXmlTag(settings, XmlNames.showGrid);
         snapToGrid = XmlUtils.getBooleanValueFromXmlTag(settings, XmlNames.snapToGrid);
         mainGridSize = XmlUtils.getIntValueFromXmlTag(settings, XmlNames.mainGridSize);
         nrOfSubGridLines = XmlUtils.getIntValueFromXmlTag(settings, XmlNames.nrOfSubGridLines);
      }
   }

   private void setWhiteboardToolStatus(Element tools)
   {
      if (tools != null)
      {
         WhiteboardAction currentAction = null;
         deleteWhiteboardActions();
         @SuppressWarnings("unchecked")
         List<Element> toolElements = tools.getChildren(XmlNames.tool);
         for (Element toolStatus : toolElements)
         {
            WhiteboardAction whiteboardAction = createWhiteboardAction(toolStatus);
            if (whiteboardAction != null)
            {
               whiteboardAction.setStatus(toolStatus);
               addWhiteboardAction(whiteboardAction);
               if (currentAction == null)
               {
                  currentAction = whiteboardAction;
               }
            }
         }
         setCurrentAction(currentAction);
      }
   }

   private WhiteboardAction createWhiteboardAction(Element toolStatus)
   {
      WhiteboardAction whiteboardAction = null;
      String toolType = toolStatus.getAttributeValue(XmlNames.type);
      if (XmlNames.select.equals(toolType))
      {
         whiteboardAction = new SelectionAction(this, "Sel");
      }
      else if (XmlNames.delete.equals(toolType))
      {
         whiteboardAction = new DeleteAction2(this, "Del");
      }
      else if (XmlNames.line.equals(toolType))
      {
         whiteboardAction = new LineAction(this, "Line");
      }
      else if (XmlNames.rectangle.equals(toolType))
      {
         whiteboardAction = new RectangleAction(this, "Rect");
      }
      else if (XmlNames.oval.equals(toolType))
      {
         whiteboardAction = new OvalAction(this, "Oval");
      }
      else if (XmlNames.freehand.equals(toolType))
      {
         whiteboardAction = new FreehandAction(this, "Free");
      }
      else if (XmlNames.text.equals(toolType))
      {
         whiteboardAction = new SimpleTextAction(this, "Text");
      }
      else if (XmlNames.image.equals(toolType))
      {
         whiteboardAction = new InsertImageAction(this, "Insert image");
      }
      else if (XmlNames.tagPointer.equals(toolType))
      {
         whiteboardAction = new TagPointerAction(this, "Text");
      }
      else if (XmlNames.info.equals(toolType))
      {
         whiteboardAction = new InfoAction(this, "Info");
      }
      else if (XmlNames.penSize.equals(toolType))
      {
         whiteboardAction = new PenSizeSelector(this, stateSize);
      }
      else if (XmlNames.lineColor.equals(toolType))
      {
         whiteboardAction = new LineColorSelector(this, stateSize);
      }
      else if (XmlNames.fillColor.equals(toolType))
      {
         whiteboardAction = new FillColorSelector(this, stateSize);
      }
      else
      {
         logger.warning("Unknown tool type: " + toolType);
      }
      return whiteboardAction;
   }

   private void setWhiteboardContainerStatus(Element status)
   {
      @SuppressWarnings("unchecked")
      Iterator<Element> childIterator = status.getChildren(XmlNames.whiteboardContainer).iterator();
      while (childIterator.hasNext())
      {
         Element childStatus = childIterator.next();
         String id = childStatus.getAttributeValue(XmlNames.id);
         WhiteboardContainer whiteboardContainer = whiteboardContainerHashMap.get(id);
         if (whiteboardContainer == null)
         {
            whiteboardContainer = createWhiteboardContainer(childStatus);
            if (whiteboardContainer != null)
            {
               whiteboardContainerList.add(whiteboardContainer);
               whiteboardContainerHashMap.put(whiteboardContainer.getId(), whiteboardContainer);
            }
         }
         if (whiteboardContainer != null)
         {
            whiteboardContainer.setStatus(childStatus);
            for (WhiteboardContainerChangedListener whiteboardContainerChangedListener : whiteboardContainerChangedListeners)
            {
               whiteboardContainer.addWhiteboardContainerChangedListener(whiteboardContainerChangedListener);
            }
         }
      }
   }

   private WhiteboardContainer createWhiteboardContainer(Element status)
   {
      WhiteboardContainer whiteboardContainer = null;
      String type = status.getAttributeValue(XmlNames.type);
      if (XmlNames.objectContainer.equals(type))
      {
         whiteboardContainer = new WhiteboardObjectContainer(this, status);
      }
      else
      {
         logger.warning("Unknow container type in status: " + type);
      }
      return whiteboardContainer;
   }

   public void printGraphics12() throws PrinterException
   {
      PrinterJob printJob = PrinterJob.getPrinterJob();
      printJob.setPrintable(this);
      if (printJob.printDialog())
      {
         printJob.print();
      }
   }

   public void printGraphicsDoc() throws PrintException
   {
      DocFlavor flavor = DocFlavor.INPUT_STREAM.POSTSCRIPT;
      PrintRequestAttributeSet aSet = new HashPrintRequestAttributeSet();
      // aSet.add(OrientationRequested.PORTRAIT);
      aSet.add(MediaSizeName.ISO_A4);
      PrintService[] printServices = PrintServiceLookup.lookupPrintServices(flavor, aSet);
      StringBuilder builder = new StringBuilder("Found print services:\n");
      for (int i = 0; i < printServices.length; i++)
      {
         builder.append("" + i + ": " + printServices[i].getName());
         builder.append("\n");
      }
      logger.fine(builder.toString());
      // logger.fine("nr of PrintService: " + printServices.length);
      PrintService selectedPrintService = selectPrintService(printServices);
      if (selectedPrintService != null)
      {
         logger.fine("Using PrintService: " + selectedPrintService.getName());
         DocPrintJob printJob = selectedPrintService.createPrintJob();
         Doc doc = new PrintableWhiteboardPanel(this);
         printJob.print(doc, aSet);
      }
   }
   private String selectedPrintServiceName = null;

   private PrintService selectPrintService(PrintService[] printServices)
   {
      selectedPrintServiceName = (String) JOptionPane.showInputDialog(this, "Select printer",
         "Select printer", JOptionPane.QUESTION_MESSAGE, null,
         getPrinterNames(printServices), selectedPrintServiceName);
      if (selectedPrintServiceName != null && selectedPrintServiceName.length() > 0)
      {
         for (PrintService printService : printServices)
         {
            if (selectedPrintServiceName.equalsIgnoreCase(printService.getName()))
            {
               return printService;
            }
         }
      }
      return null;
   }

   private String[] getPrinterNames(PrintService[] printServices)
   {
      String[] printerNames = new String[printServices.length];
      int i = 0;
      for (PrintService printService : printServices)
      {
         printerNames[i++] = printService.getName();
      }
      return printerNames;
   }

   @Override
   public int print(Graphics graphics, PageFormat pageFormat, int pageIndex)
      throws PrinterException
   {
      logger.fine("printing page " + pageIndex);
      if (pageIndex == 0)
      {
         // pageFormat.setOrientation(PageFormat.LANDSCAPE);
         Rectangle2D enclosingScreenRectangle = getEnclosingScreenRectangle();
         final double whiteSpace = 30;
         final double borderInset = 20;
         logger.info("enclosingScreenRectangle: " + enclosingScreenRectangle);
         // logger.info("pageFormat: " + pageFormat);
         double xPageOffset = pageFormat.getImageableX() + whiteSpace;
         double yPageOffset = pageFormat.getImageableY() + whiteSpace;
         double xPrintOffset = 1 + xPageOffset - enclosingScreenRectangle.getMinX();
         double yPrintOffset = 1 + yPageOffset - enclosingScreenRectangle.getMinY();
         double xPrintScale = (pageFormat.getImageableWidth() - 2 * pageFormat.getImageableX())
            / (enclosingScreenRectangle.getWidth() + 2 * whiteSpace);
         double yPrintScale = (pageFormat.getImageableHeight() - 2 * pageFormat.getImageableY())
            / (enclosingScreenRectangle.getHeight() + 2 * whiteSpace);
         double printScale = Math.min(xPrintScale, yPrintScale);
         // printScale *= .925;
         // printScale *= .99;
         printScale = Math.min(printScale, 1.0);
         // printScale = 1.0;
         logger.info("factors: xPrintOffset=" + xPrintOffset + ", yPrintOffset=" + yPrintOffset
            + ", printScale=" + printScale);
         Graphics2D g2d = (Graphics2D) graphics.create();
         g2d.translate(xPrintOffset, yPrintOffset);
         g2d.scale(printScale, printScale);
         paintComponent(g2d);
         g2d.setColor(Color.DARK_GRAY);
         Rectangle2D drawBounds = new Rectangle2D.Double(enclosingScreenRectangle.getX()
            - whiteSpace + borderInset, enclosingScreenRectangle.getY() - whiteSpace
            + borderInset, enclosingScreenRectangle.getWidth() + 2 * whiteSpace - 2
            * borderInset, enclosingScreenRectangle.getHeight() + 2 * whiteSpace - 2
            * borderInset);
         g2d.draw(drawBounds);
         g2d.dispose();
         return Printable.PAGE_EXISTS;
      }
      return Printable.NO_SUCH_PAGE;
   }

   class PrintableWhiteboardPanel implements Doc
   {

      private final Printable printable;

      PrintableWhiteboardPanel(Printable printable)
      {
         this.printable = printable;
      }

      @Override
      public DocAttributeSet getAttributes()
      {
         return null;
      }

      @Override
      public DocFlavor getDocFlavor()
      {
         return DocFlavor.SERVICE_FORMATTED.PRINTABLE;
      }

      @Override
      public Object getPrintData() throws IOException
      {
         return printable;
      }

      @Override
      public Reader getReaderForText() throws IOException
      {
         return null;
      }

      @Override
      public InputStream getStreamForBytes() throws IOException
      {
         return null;
      }
   }

   public Rectangle2D getEnclosingScreenRectangle()
   {
      Rectangle2D enclosingScreenRectangle = new Rectangle2D.Double(0, 0, 100, 100);
      Iterator<WhiteboardContainer> iterator = whiteboardContainerList.iterator();
      if (iterator.hasNext())
      {
         enclosingScreenRectangle = iterator.next().getScreenUpdateRectangle();
         while (iterator.hasNext())
         {
            Rectangle2D screenRectangle = iterator.next().getScreenUpdateRectangle();
            enclosingScreenRectangle = enclosingScreenRectangle.createUnion(screenRectangle);
         }
      }
      // Graphics2D g2d = (Graphics2D) getGraphics();
      // g2d.setColor(new Color(0,0,255,16));
      // g2d.fill(enclosingScreenRectangle);
      return enclosingScreenRectangle;
   }

   public double getCurrentPenSize()
   {
      return currentPenSize;
   }

   public void setCurrentPenSize(double currentPenSize)
   {
      this.currentPenSize = currentPenSize;
   }

   public Color getCurrentLineColor()
   {
      return currentLineColor;
   }

   public void setCurrentLineColor(Color currentLineColor)
   {
      this.currentLineColor = currentLineColor;
   }

   public Color getCurrentFillColor()
   {
      return currentFillColor;
   }

   public void setCurrentFillColor(Color currentFillColor)
   {
      this.currentFillColor = currentFillColor;
   }
}
